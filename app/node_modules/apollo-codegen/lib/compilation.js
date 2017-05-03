'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Compiler = undefined;

var _extends2 = require('babel-runtime/helpers/extends');

var _extends3 = _interopRequireDefault(_extends2);

var _toConsumableArray2 = require('babel-runtime/helpers/toConsumableArray');

var _toConsumableArray3 = _interopRequireDefault(_toConsumableArray2);

var _assign = require('babel-runtime/core-js/object/assign');

var _assign2 = _interopRequireDefault(_assign);

var _entries = require('babel-runtime/core-js/object/entries');

var _entries2 = _interopRequireDefault(_entries);

var _slicedToArray2 = require('babel-runtime/helpers/slicedToArray');

var _slicedToArray3 = _interopRequireDefault(_slicedToArray2);

var _keys = require('babel-runtime/core-js/object/keys');

var _keys2 = _interopRequireDefault(_keys);

var _map = require('babel-runtime/core-js/map');

var _map2 = _interopRequireDefault(_map);

var _from = require('babel-runtime/core-js/array/from');

var _from2 = _interopRequireDefault(_from);

var _values = require('babel-runtime/core-js/object/values');

var _values2 = _interopRequireDefault(_values);

var _create = require('babel-runtime/core-js/object/create');

var _create2 = _interopRequireDefault(_create);

var _set = require('babel-runtime/core-js/set');

var _set2 = _interopRequireDefault(_set);

exports.compileToIR = compileToIR;
exports.printIR = printIR;

var _graphql = require('graphql');

var _graphql2 = require('./utilities/graphql');

var _printing = require('./utilities/printing');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const builtInScalarTypes = new _set2.default([_graphql.GraphQLString, _graphql.GraphQLInt, _graphql.GraphQLFloat, _graphql.GraphQLBoolean, _graphql.GraphQLID]);

function isBuiltInScalarType(type) {
  return builtInScalarTypes.has(type);
}

// Parts of this code are adapted from graphql-js

function compileToIR(schema, document) {
  const compiler = new Compiler(schema, document);

  const operations = (0, _create2.default)(null);

  compiler.operations.forEach(operation => {
    operations[operation.name.value] = compiler.compileOperation(operation);
  });

  const fragments = (0, _create2.default)(null);

  compiler.fragments.forEach(fragment => {
    fragments[fragment.name.value] = compiler.compileFragment(fragment);
  });

  const typesUsed = compiler.typesUsed;

  return { schema: schema, operations: operations, fragments: fragments, typesUsed: typesUsed };
}

class Compiler {
  constructor(schema, document) {
    this.schema = schema;

    this.typesUsedSet = new _set2.default();

    this.fragmentMap = (0, _create2.default)(null);
    this.operations = [];

    for (const definition of document.definitions) {
      switch (definition.kind) {
        case _graphql.Kind.OPERATION_DEFINITION:
          this.operations.push(definition);
          break;
        case _graphql.Kind.FRAGMENT_DEFINITION:
          this.fragmentMap[definition.name.value] = definition;
          break;
      }
    }

    this.compiledFragmentMap = (0, _create2.default)(null);
  }

  addTypeUsed(type) {
    if (type instanceof _graphql.GraphQLEnumType || type instanceof _graphql.GraphQLInputObjectType || type instanceof _graphql.GraphQLScalarType && !isBuiltInScalarType(type)) {
      this.typesUsedSet.add(type);
    }
    if (type instanceof _graphql.GraphQLInputObjectType) {
      for (const field of (0, _values2.default)(type.getFields())) {
        this.addTypeUsed((0, _graphql.getNamedType)(field.type));
      }
    }
  }

  get typesUsed() {
    return (0, _from2.default)(this.typesUsedSet);
  }

  fragmentNamed(fragmentName) {
    return this.fragmentMap[fragmentName];
  }

  get fragments() {
    return (0, _values2.default)(this.fragmentMap);
  }

  compileOperation(operationDefinition) {
    const filePath = (0, _graphql2.filePathForNode)(operationDefinition);
    const operationName = operationDefinition.name.value;
    const operationType = operationDefinition.operation;

    const variables = operationDefinition.variableDefinitions.map(node => {
      const name = node.variable.name.value;
      const type = (0, _graphql.typeFromAST)(this.schema, node.type);
      this.addTypeUsed((0, _graphql.getNamedType)(type));
      return { name: name, type: type };
    });

    const source = (0, _graphql.print)(withTypenameFieldAddedWhereNeeded(this.schema, operationDefinition));

    const rootType = (0, _graphql2.getOperationRootType)(this.schema, operationDefinition);

    const groupedVisitedFragmentSet = new _map2.default();
    const groupedFieldSet = this.collectFields(rootType, operationDefinition.selectionSet, undefined, groupedVisitedFragmentSet);

    const fragmentsReferencedSet = (0, _create2.default)(null);

    var _resolveFields = this.resolveFields(rootType, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet);

    const fields = _resolveFields.fields;

    const fragmentsReferenced = (0, _keys2.default)(fragmentsReferencedSet);

    return { filePath: filePath, operationName: operationName, operationType: operationType, variables: variables, source: source, fields: fields, fragmentsReferenced: fragmentsReferenced };
  }

  compileFragment(fragmentDefinition) {
    const filePath = (0, _graphql2.filePathForNode)(fragmentDefinition);
    const fragmentName = fragmentDefinition.name.value;

    const source = (0, _graphql.print)(withTypenameFieldAddedWhereNeeded(this.schema, fragmentDefinition));

    const typeCondition = (0, _graphql.typeFromAST)(this.schema, fragmentDefinition.typeCondition);
    const possibleTypes = this.possibleTypesForType(typeCondition);

    const groupedVisitedFragmentSet = new _map2.default();
    const groupedFieldSet = this.collectFields(typeCondition, fragmentDefinition.selectionSet, undefined, groupedVisitedFragmentSet);

    const fragmentsReferencedSet = (0, _create2.default)(null);

    var _resolveFields2 = this.resolveFields(typeCondition, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet);

    const fields = _resolveFields2.fields,
          fragmentSpreads = _resolveFields2.fragmentSpreads,
          inlineFragments = _resolveFields2.inlineFragments;

    const fragmentsReferenced = (0, _keys2.default)(fragmentsReferencedSet);

    return { filePath: filePath, fragmentName: fragmentName, source: source, typeCondition: typeCondition, possibleTypes: possibleTypes, fields: fields, fragmentSpreads: fragmentSpreads, inlineFragments: inlineFragments, fragmentsReferenced: fragmentsReferenced };
  }

  collectFields(parentType, selectionSet) {
    let groupedFieldSet = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : (0, _create2.default)(null);
    let groupedVisitedFragmentSet = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : new _map2.default();

    if (!(0, _graphql.isCompositeType)(parentType)) {
      throw new Error(`parentType should be a composite type, but is "${String(parentType)}"`);
    }

    for (const selection of selectionSet.selections) {
      switch (selection.kind) {
        case _graphql.Kind.FIELD:
          {
            const fieldName = selection.name.value;
            const responseName = selection.alias ? selection.alias.value : fieldName;

            const field = (0, _graphql2.getFieldDef)(this.schema, parentType, selection);
            if (!field) {
              throw new _graphql.GraphQLError(`Cannot query field "${fieldName}" on type "${String(parentType)}"`, [selection]);
            }

            if (groupedFieldSet) {
              if (!groupedFieldSet[responseName]) {
                groupedFieldSet[responseName] = [];
              }

              groupedFieldSet[responseName].push([parentType, {
                responseName: responseName,
                fieldName: fieldName,
                args: argumentsFromAST(selection.arguments),
                type: field.type,
                directives: selection.directives,
                selectionSet: selection.selectionSet
              }]);
            }
            break;
          }
        case _graphql.Kind.INLINE_FRAGMENT:
          {
            const typeCondition = selection.typeCondition;
            const inlineFragmentType = typeCondition ? (0, _graphql.typeFromAST)(this.schema, typeCondition) : parentType;

            const effectiveType = parentType instanceof _graphql.GraphQLObjectType ? parentType : inlineFragmentType;

            this.collectFields(effectiveType, selection.selectionSet, groupedFieldSet, groupedVisitedFragmentSet);
            break;
          }
        case _graphql.Kind.FRAGMENT_SPREAD:
          {
            const fragmentName = selection.name.value;

            const fragment = this.fragmentNamed(fragmentName);
            if (!fragment) throw new _graphql.GraphQLError(`Cannot find fragment "${fragmentName}"`);

            const typeCondition = fragment.typeCondition;
            const fragmentType = (0, _graphql.typeFromAST)(this.schema, typeCondition);

            if (groupedVisitedFragmentSet) {
              let visitedFragmentSet = groupedVisitedFragmentSet.get(parentType);
              if (!visitedFragmentSet) {
                visitedFragmentSet = {};
                groupedVisitedFragmentSet.set(parentType, visitedFragmentSet);
              }

              if (visitedFragmentSet[fragmentName]) continue;
              visitedFragmentSet[fragmentName] = true;
            }

            const effectiveType = parentType instanceof _graphql.GraphQLObjectType ? parentType : fragmentType;

            this.collectFields(effectiveType, fragment.selectionSet, null, groupedVisitedFragmentSet);
            break;
          }
      }
    }

    return groupedFieldSet;
  }

  possibleTypesForType(type) {
    if ((0, _graphql.isAbstractType)(type)) {
      return this.schema.getPossibleTypes(type);
    } else {
      return [type];
    }
  }

  mergeSelectionSets(parentType, fieldSet, groupedVisitedFragmentSet) {
    const groupedFieldSet = (0, _create2.default)(null);

    for (const _ref of fieldSet) {
      var _ref2 = (0, _slicedToArray3.default)(_ref, 2);

      const field = _ref2[1];

      const selectionSet = field.selectionSet;

      if (selectionSet) {
        this.collectFields(parentType, selectionSet, groupedFieldSet, groupedVisitedFragmentSet);
      }
    }

    return groupedFieldSet;
  }

  resolveFields(parentType, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet) {
    const fields = [];

    for (let _ref3 of (0, _entries2.default)(groupedFieldSet)) {
      var _ref4 = (0, _slicedToArray3.default)(_ref3, 2);

      let responseName = _ref4[0];
      let fieldSet = _ref4[1];

      fieldSet = fieldSet.filter((_ref5) => {
        var _ref6 = (0, _slicedToArray3.default)(_ref5, 1);

        let typeCondition = _ref6[0];
        return (0, _graphql.isTypeSubTypeOf)(this.schema, parentType, typeCondition);
      });
      if (fieldSet.length < 1) continue;

      var _fieldSet$ = (0, _slicedToArray3.default)(fieldSet[0], 2);

      const firstField = _fieldSet$[1];

      const fieldName = firstField.fieldName;
      const args = firstField.args;
      const type = firstField.type;

      let field = { responseName: responseName, fieldName: fieldName, type: type };

      if (args && args.length > 0) {
        field.args = args;
      }

      const isConditional = fieldSet.some((_ref7) => {
        var _ref8 = (0, _slicedToArray3.default)(_ref7, 2);

        let field = _ref8[1];

        return field.directives && field.directives.some(directive => {
          const directiveName = directive.name.value;
          return directiveName == 'skip' || directiveName == 'include';
        });
      });

      if (isConditional) {
        field.isConditional = true;
      }

      const bareType = (0, _graphql.getNamedType)(type);

      this.addTypeUsed(bareType);

      if ((0, _graphql.isCompositeType)(bareType)) {
        const subSelectionGroupedVisitedFragmentSet = new _map2.default();
        const subSelectionGroupedFieldSet = this.mergeSelectionSets(bareType, fieldSet, subSelectionGroupedVisitedFragmentSet);

        var _resolveFields3 = this.resolveFields(bareType, subSelectionGroupedFieldSet, subSelectionGroupedVisitedFragmentSet, fragmentsReferencedSet);

        const fields = _resolveFields3.fields,
              fragmentSpreads = _resolveFields3.fragmentSpreads,
              inlineFragments = _resolveFields3.inlineFragments;

        (0, _assign2.default)(field, { fields: fields, fragmentSpreads: fragmentSpreads, inlineFragments: inlineFragments });
      }

      fields.push(field);
    }

    const fragmentSpreads = this.fragmentSpreadsForParentType(parentType, groupedVisitedFragmentSet);
    const inlineFragments = this.resolveInlineFragments(parentType, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet);

    if (fragmentsReferencedSet) {
      _assign2.default.apply(Object, [fragmentsReferencedSet].concat((0, _toConsumableArray3.default)(groupedVisitedFragmentSet.values())));

      // TODO: This is a really inefficient way of keeping track of fragments referenced by other fragments
      // We need to either cache compiled fragments or find a way to make resolveFields smarter
      for (let fragmentName of fragmentSpreads) {
        const fragment = this.fragmentNamed(fragmentName);
        if (!fragment) throw new _graphql.GraphQLError(`Cannot find fragment "${fragmentName}"`);

        var _compileFragment = this.compileFragment(fragment);

        const fragmentsReferencedFromFragment = _compileFragment.fragmentsReferenced;

        for (let fragmentReferenced of fragmentsReferencedFromFragment) {
          fragmentsReferencedSet[fragmentReferenced] = true;
        }
      }
    }

    return { fields: fields, fragmentSpreads: fragmentSpreads, inlineFragments: inlineFragments };
  }

  resolveInlineFragments(parentType, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet) {
    return this.collectPossibleTypes(parentType, groupedFieldSet, groupedVisitedFragmentSet).map(typeCondition => {
      var _resolveFields4 = this.resolveFields(typeCondition, groupedFieldSet, groupedVisitedFragmentSet, fragmentsReferencedSet);

      const fields = _resolveFields4.fields,
            fragmentSpreads = _resolveFields4.fragmentSpreads;

      const possibleTypes = this.possibleTypesForType(typeCondition);
      return { typeCondition: typeCondition, possibleTypes: possibleTypes, fields: fields, fragmentSpreads: fragmentSpreads };
    });
  }

  collectPossibleTypes(parentType, groupedFieldSet, groupedVisitedFragmentSet) {
    if (!(0, _graphql.isAbstractType)(parentType)) return [];

    const possibleTypes = new _set2.default();

    for (const fieldSet of (0, _values2.default)(groupedFieldSet)) {
      for (const _ref9 of fieldSet) {
        var _ref10 = (0, _slicedToArray3.default)(_ref9, 1);

        const typeCondition = _ref10[0];

        if (this.schema.isPossibleType(parentType, typeCondition)) {
          possibleTypes.add(typeCondition);
        }
      }
    }

    // Also include type conditions for fragment spreads
    if (groupedVisitedFragmentSet) {
      for (const effectiveType of groupedVisitedFragmentSet.keys()) {
        if (this.schema.isPossibleType(parentType, effectiveType)) {
          possibleTypes.add(effectiveType);
        }
      }
    }

    return (0, _from2.default)(possibleTypes);
  }

  fragmentSpreadsForParentType(parentType, groupedVisitedFragmentSet) {
    if (!groupedVisitedFragmentSet) return [];

    let fragmentSpreads = new _set2.default();

    for (const _ref11 of groupedVisitedFragmentSet) {
      var _ref12 = (0, _slicedToArray3.default)(_ref11, 2);

      const effectiveType = _ref12[0];
      const visitedFragmentSet = _ref12[1];

      if (!(0, _graphql2.isTypeProperSuperTypeOf)(this.schema, effectiveType, parentType)) continue;

      for (const fragmentName of (0, _keys2.default)(visitedFragmentSet)) {
        fragmentSpreads.add(fragmentName);
      }
    }

    return (0, _from2.default)(fragmentSpreads);
  }
}

exports.Compiler = Compiler;
const typenameField = { kind: _graphql.Kind.FIELD, name: { kind: _graphql.Kind.NAME, value: '__typename' } };

function withTypenameFieldAddedWhereNeeded(schema, ast) {
  function isOperationRootType(type) {
    return type === schema.getQueryType() || type === schema.getMutationType() || type === schema.getSubscriptionType();
  }

  const typeInfo = new _graphql.TypeInfo(schema);

  return (0, _graphql.visit)(ast, (0, _graphql.visitWithTypeInfo)(typeInfo, {
    leave: {
      SelectionSet: node => {
        const parentType = typeInfo.getParentType();

        if (!isOperationRootType(parentType)) {
          return (0, _extends3.default)({}, node, { selections: [typenameField].concat((0, _toConsumableArray3.default)(node.selections)) });
        }
      }
    }
  }));
}

function sourceAt(location) {
  return location.source.body.slice(location.start, location.end);
}

function argumentsFromAST(args) {
  return args.map(arg => {
    return { name: arg.name.value, value: (0, _graphql2.valueFromValueNode)(arg.value) };
  });
}

function printIR(_ref13) {
  let fields = _ref13.fields,
      inlineFragments = _ref13.inlineFragments,
      fragmentSpreads = _ref13.fragmentSpreads;

  return fields && (0, _printing.wrap)('<', (0, _printing.join)(fragmentSpreads, ', '), '> ') + (0, _printing.block)(fields.map(field => `${field.name}: ${String(field.type)}` + (0, _printing.wrap)(' ', printIR(field))).concat(inlineFragments && inlineFragments.map(inlineFragment => `${String(inlineFragment.typeCondition)}` + (0, _printing.wrap)(' ', printIR(inlineFragment)))));
}
//# sourceMappingURL=compilation.js.map