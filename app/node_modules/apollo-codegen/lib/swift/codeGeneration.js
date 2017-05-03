'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _stringify = require('babel-runtime/core-js/json/stringify');

var _stringify2 = _interopRequireDefault(_stringify);

var _slicedToArray2 = require('babel-runtime/helpers/slicedToArray');

var _slicedToArray3 = _interopRequireDefault(_slicedToArray2);

var _entries = require('babel-runtime/core-js/object/entries');

var _entries2 = _interopRequireDefault(_entries);

var _extends2 = require('babel-runtime/helpers/extends');

var _extends3 = _interopRequireDefault(_extends2);

var _values = require('babel-runtime/core-js/object/values');

var _values2 = _interopRequireDefault(_values);

exports.generateSource = generateSource;
exports.classDeclarationForOperation = classDeclarationForOperation;
exports.initializerDeclarationForProperties = initializerDeclarationForProperties;
exports.structDeclarationForFragment = structDeclarationForFragment;
exports.structDeclarationForSelectionSet = structDeclarationForSelectionSet;
exports.initializationForProperty = initializationForProperty;
exports.dictionaryLiteralForFieldArguments = dictionaryLiteralForFieldArguments;
exports.propertiesFromFields = propertiesFromFields;
exports.propertyFromField = propertyFromField;
exports.structNameForProperty = structNameForProperty;
exports.typeNameForFragmentName = typeNameForFragmentName;
exports.typeDeclarationForGraphQLType = typeDeclarationForGraphQLType;

var _graphql = require('graphql');

var _graphql2 = require('../utilities/graphql');

var _changeCase = require('change-case');

var _inflected = require('inflected');

var _inflected2 = _interopRequireDefault(_inflected);

var _printing = require('../utilities/printing');

var _language = require('./language');

var _values3 = require('./values');

var _types = require('./types');

var _CodeGenerator = require('../utilities/CodeGenerator');

var _CodeGenerator2 = _interopRequireDefault(_CodeGenerator);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function generateSource(context) {
  const generator = new _CodeGenerator2.default(context);

  generator.printOnNewline('//  This file was automatically generated and should not be edited.');
  generator.printNewline();
  generator.printOnNewline('import Apollo');

  context.typesUsed.forEach(type => {
    typeDeclarationForGraphQLType(generator, type);
  });

  (0, _values2.default)(context.operations).forEach(operation => {
    classDeclarationForOperation(generator, operation);
  });

  (0, _values2.default)(context.fragments).forEach(fragment => {
    structDeclarationForFragment(generator, fragment);
  });

  return generator.output;
}

function classDeclarationForOperation(generator, _ref) {
  let operationName = _ref.operationName,
      operationType = _ref.operationType,
      variables = _ref.variables,
      fields = _ref.fields,
      fragmentsReferenced = _ref.fragmentsReferenced,
      source = _ref.source;


  let className;
  let protocol;

  switch (operationType) {
    case 'query':
      className = `${(0, _changeCase.pascalCase)(operationName)}Query`;
      protocol = 'GraphQLQuery';
      break;
    case 'mutation':
      className = `${(0, _changeCase.pascalCase)(operationName)}Mutation`;
      protocol = 'GraphQLMutation';
      break;
    default:
      throw new _graphql.GraphQLError(`Unsupported operation type "${operationType}"`);
  }

  (0, _language.classDeclaration)(generator, {
    className: className,
    modifiers: ['public', 'final'],
    adoptedProtocols: [protocol]
  }, () => {
    if (source) {
      generator.printOnNewline('public static let operationDefinition =');
      generator.withIndent(() => {
        (0, _values3.multilineString)(generator, source);
      });
    }

    if (fragmentsReferenced && fragmentsReferenced.length > 0) {
      generator.printOnNewline('public static let queryDocument = operationDefinition');
      fragmentsReferenced.forEach(fragment => {
        generator.print(`.appending(${typeNameForFragmentName(fragment)}.fragmentDefinition)`);
      });
    }

    if (variables && variables.length > 0) {
      const properties = variables.map((_ref2) => {
        let name = _ref2.name,
            type = _ref2.type;

        const propertyName = (0, _language.escapeIdentifierIfNeeded)((0, _changeCase.camelCase)(name));
        const typeName = (0, _types.typeNameFromGraphQLType)(generator.context, type);
        const isOptional = !(type instanceof _graphql.GraphQLNonNull || type.ofType instanceof _graphql.GraphQLNonNull);
        return { name: name, propertyName: propertyName, type: type, typeName: typeName, isOptional: isOptional };
      });
      generator.printNewlineIfNeeded();
      (0, _language.propertyDeclarations)(generator, properties);
      generator.printNewlineIfNeeded();
      initializerDeclarationForProperties(generator, properties);
      generator.printNewlineIfNeeded();
      generator.printOnNewline(`public var variables: GraphQLMap?`);
      generator.withinBlock(() => {
        generator.printOnNewline((0, _printing.wrap)(`return [`, (0, _printing.join)(properties.map((_ref3) => {
          let name = _ref3.name,
              propertyName = _ref3.propertyName;
          return `"${name}": ${propertyName}`;
        }), ', ') || ':', `]`));
      });
    } else {
      initializerDeclarationForProperties(generator, []);
    }

    structDeclarationForSelectionSet(generator, {
      structName: "Data",
      fields: fields
    });
  });
}

function initializerDeclarationForProperties(generator, properties) {
  generator.printOnNewline(`public init`);
  generator.print('(');
  generator.print((0, _printing.join)(properties.map((_ref4) => {
    let propertyName = _ref4.propertyName,
        type = _ref4.type,
        typeName = _ref4.typeName,
        isOptional = _ref4.isOptional;
    return (0, _printing.join)([`${propertyName}: ${typeName}`, isOptional && ' = nil']);
  }), ', '));
  generator.print(')');

  generator.withinBlock(() => {
    properties.forEach((_ref5) => {
      let propertyName = _ref5.propertyName;

      generator.printOnNewline(`self.${propertyName} = ${propertyName}`);
    });
  });
}

function structDeclarationForFragment(generator, _ref6) {
  let fragmentName = _ref6.fragmentName,
      typeCondition = _ref6.typeCondition,
      possibleTypes = _ref6.possibleTypes,
      fields = _ref6.fields,
      inlineFragments = _ref6.inlineFragments,
      fragmentSpreads = _ref6.fragmentSpreads,
      source = _ref6.source;

  const structName = (0, _changeCase.pascalCase)(fragmentName);

  structDeclarationForSelectionSet(generator, {
    structName: structName,
    adoptedProtocols: ['GraphQLNamedFragment'],
    parentType: typeCondition,
    possibleTypes: possibleTypes,
    fields: fields,
    fragmentSpreads: fragmentSpreads,
    inlineFragments: inlineFragments
  }, () => {
    if (source) {
      generator.printOnNewline('public static let fragmentDefinition =');
      generator.withIndent(() => {
        (0, _values3.multilineString)(generator, source);
      });
    }
  });
}

function structDeclarationForSelectionSet(generator, _ref7, beforeClosure) {
  let structName = _ref7.structName;
  var _ref7$adoptedProtocol = _ref7.adoptedProtocols;
  let adoptedProtocols = _ref7$adoptedProtocol === undefined ? ['GraphQLMappable'] : _ref7$adoptedProtocol,
      parentType = _ref7.parentType,
      possibleTypes = _ref7.possibleTypes,
      fields = _ref7.fields,
      fragmentSpreads = _ref7.fragmentSpreads,
      inlineFragments = _ref7.inlineFragments;

  (0, _language.structDeclaration)(generator, { structName: structName, adoptedProtocols: adoptedProtocols }, () => {
    if (beforeClosure) {
      beforeClosure();
    }

    if (possibleTypes) {
      generator.printNewlineIfNeeded();
      generator.printOnNewline('public static let possibleTypes = [');
      generator.print((0, _printing.join)(possibleTypes.map(type => `"${String(type)}"`), ', '));
      generator.print(']');
    }

    const properties = fields && propertiesFromFields(generator.context, fields);

    const fragmentProperties = fragmentSpreads && fragmentSpreads.map(fragmentName => {
      const fragment = generator.context.fragments[fragmentName];
      if (!fragment) {
        throw new _graphql.GraphQLError(`Cannot find fragment "${fragmentName}"`);
      }
      const propertyName = (0, _changeCase.camelCase)(fragmentName);
      const typeName = typeNameForFragmentName(fragmentName);
      const isProperSuperType = (0, _graphql2.isTypeProperSuperTypeOf)(generator.context.schema, fragment.typeCondition, parentType);
      return { propertyName: propertyName, typeName: typeName, bareTypeName: typeName, isProperSuperType: isProperSuperType };
    });

    const inlineFragmentProperties = inlineFragments && inlineFragments.map(inlineFragment => {
      const bareTypeName = 'As' + (0, _changeCase.pascalCase)(String(inlineFragment.typeCondition));
      const propertyName = (0, _changeCase.camelCase)(bareTypeName);
      const typeName = bareTypeName + '?';
      return (0, _extends3.default)({}, inlineFragment, { propertyName: propertyName, typeName: typeName, bareTypeName: bareTypeName });
    });

    generator.printNewlineIfNeeded();

    if (parentType) {
      generator.printOnNewline('public let __typename: String');
    }

    (0, _language.propertyDeclarations)(generator, properties);

    if (fragmentProperties && fragmentProperties.length > 0) {
      generator.printNewlineIfNeeded();
      (0, _language.propertyDeclaration)(generator, { propertyName: 'fragments', typeName: 'Fragments' });
    }

    if (inlineFragmentProperties && inlineFragmentProperties.length > 0) {
      generator.printNewlineIfNeeded();
      (0, _language.propertyDeclarations)(generator, inlineFragmentProperties);
    }

    generator.printNewlineIfNeeded();
    generator.printOnNewline('public init(reader: GraphQLResultReader) throws');
    generator.withinBlock(() => {
      if (parentType) {
        generator.printOnNewline(`__typename = try reader.value(for: Field(responseName: "__typename"))`);
      }

      if (properties) {
        properties.forEach(property => initializationForProperty(generator, property));
      }

      if (fragmentProperties && fragmentProperties.length > 0) {
        generator.printNewlineIfNeeded();
        fragmentProperties.forEach((_ref8) => {
          let propertyName = _ref8.propertyName,
              typeName = _ref8.typeName,
              bareTypeName = _ref8.bareTypeName,
              isProperSuperType = _ref8.isProperSuperType;

          generator.printOnNewline(`let ${propertyName} = try ${typeName}(reader: reader`);
          if (isProperSuperType) {
            generator.print(')');
          } else {
            generator.print(`, ifTypeMatches: __typename)`);
          }
        });
        generator.printOnNewline(`fragments = Fragments(`);
        generator.print((0, _printing.join)(fragmentSpreads.map(fragmentName => {
          const propertyName = (0, _changeCase.camelCase)(fragmentName);
          return `${propertyName}: ${propertyName}`;
        }), ', '));
        generator.print(')');
      }

      if (inlineFragmentProperties && inlineFragmentProperties.length > 0) {
        generator.printNewlineIfNeeded();
        inlineFragmentProperties.forEach((_ref9) => {
          let propertyName = _ref9.propertyName,
              typeName = _ref9.typeName,
              bareTypeName = _ref9.bareTypeName;

          generator.printOnNewline(`${propertyName} = try ${bareTypeName}(reader: reader, ifTypeMatches: __typename)`);
        });
      }
    });

    if (fragmentProperties && fragmentProperties.length > 0) {
      (0, _language.structDeclaration)(generator, {
        structName: 'Fragments'
      }, () => {
        fragmentProperties.forEach((_ref10) => {
          let propertyName = _ref10.propertyName,
              typeName = _ref10.typeName,
              isProperSuperType = _ref10.isProperSuperType;

          if (!isProperSuperType) {
            typeName += '?';
          }
          (0, _language.propertyDeclaration)(generator, { propertyName: propertyName, typeName: typeName });
        });
      });
    }

    if (inlineFragmentProperties && inlineFragmentProperties.length > 0) {
      inlineFragmentProperties.forEach(property => {
        structDeclarationForSelectionSet(generator, {
          structName: property.bareTypeName,
          parentType: property.typeCondition,
          possibleTypes: property.possibleTypes,
          adoptedProtocols: ['GraphQLConditionalFragment'],
          fields: property.fields,
          fragmentSpreads: property.fragmentSpreads
        });
      });
    }

    if (properties) {
      properties.filter(property => property.isComposite).forEach(property => {
        structDeclarationForSelectionSet(generator, {
          structName: structNameForProperty(property),
          parentType: (0, _graphql.getNamedType)(property.type),
          fields: property.fields,
          fragmentSpreads: property.fragmentSpreads,
          inlineFragments: property.inlineFragments
        });
      });
    }
  });
}

function initializationForProperty(generator, _ref11) {
  let propertyName = _ref11.propertyName,
      responseName = _ref11.responseName,
      fieldName = _ref11.fieldName,
      fieldArgs = _ref11.args,
      type = _ref11.type,
      isOptional = _ref11.isOptional;

  const isList = type instanceof _graphql.GraphQLList || type.ofType instanceof _graphql.GraphQLList;

  const methodName = isOptional ? isList ? 'optionalList' : 'optionalValue' : isList ? 'list' : 'value';

  const fieldInitArgs = (0, _printing.join)([`responseName: "${responseName}"`, responseName != fieldName ? `fieldName: "${fieldName}"` : null, fieldArgs && fieldArgs.length && `arguments: ${dictionaryLiteralForFieldArguments(fieldArgs)}`], ', ');
  const args = [`for: Field(${fieldInitArgs})`];

  generator.printOnNewline(`${propertyName} = try reader.${methodName}(${(0, _printing.join)(args, ', ')})`);
}

function dictionaryLiteralForFieldArguments(args) {
  function expressionFromValue(value) {
    if (value.kind === 'Variable') {
      return `reader.variables["${value.variableName}"]`;
    } else if (Array.isArray(value)) {
      return (0, _printing.wrap)('[', (0, _printing.join)(value.map(expressionFromValue), ', '), ']');
    } else if (typeof value === 'object') {
      return (0, _printing.wrap)('[', (0, _printing.join)((0, _entries2.default)(value).map((_ref12) => {
        var _ref13 = (0, _slicedToArray3.default)(_ref12, 2);

        let key = _ref13[0],
            value = _ref13[1];

        return `"${key}": ${expressionFromValue(value)}`;
      }), ', ') || ':', ']');
    } else {
      return (0, _stringify2.default)(value);
    }
  }

  return (0, _printing.wrap)('[', (0, _printing.join)(args.map(arg => {
    return `"${arg.name}": ${expressionFromValue(arg.value)}`;
  }), ', ') || ':', ']');
}

function propertiesFromFields(context, fields) {
  return fields.map(field => propertyFromField(context, field));
}

function propertyFromField(context, field) {
  const name = field.name || field.responseName;
  const propertyName = (0, _language.escapeIdentifierIfNeeded)((0, _changeCase.camelCase)(name));

  const type = field.type;
  const isOptional = field.isConditional || !(type instanceof _graphql.GraphQLNonNull);
  const bareType = (0, _graphql.getNamedType)(type);

  if ((0, _graphql.isCompositeType)(bareType)) {
    const bareTypeName = (0, _language.escapeIdentifierIfNeeded)((0, _changeCase.pascalCase)(_inflected2.default.singularize(name)));
    const typeName = (0, _types.typeNameFromGraphQLType)(context, type, bareTypeName, isOptional);
    return (0, _extends3.default)({}, field, { propertyName: propertyName, typeName: typeName, bareTypeName: bareTypeName, isOptional: isOptional, isComposite: true });
  } else {
    const typeName = (0, _types.typeNameFromGraphQLType)(context, type, undefined, isOptional);
    return (0, _extends3.default)({}, field, { propertyName: propertyName, typeName: typeName, isOptional: isOptional, isComposite: false });
  }
}

function structNameForProperty(property) {
  return (0, _changeCase.pascalCase)(_inflected2.default.singularize(property.responseName));
}

function typeNameForFragmentName(fragmentName) {
  return (0, _changeCase.pascalCase)(fragmentName);
}

function typeDeclarationForGraphQLType(generator, type) {
  if (type instanceof _graphql.GraphQLEnumType) {
    enumerationDeclaration(generator, type);
  } else if (type instanceof _graphql.GraphQLInputObjectType) {
    structDeclarationForInputObjectType(generator, type);
  }
}

function enumerationDeclaration(generator, type) {
  const name = type.name,
        description = type.description;

  const values = type.getValues();

  generator.printNewlineIfNeeded();
  generator.printOnNewline(description && `/// ${description}`);
  generator.printOnNewline(`public enum ${name}: String`);
  generator.withinBlock(() => {
    values.forEach(value => generator.printOnNewline(`case ${(0, _language.escapeIdentifierIfNeeded)((0, _changeCase.camelCase)(value.name))} = "${value.value}"${(0, _printing.wrap)(' /// ', value.description)}`));
  });
  generator.printNewline();
  generator.printOnNewline(`extension ${name}: JSONDecodable, JSONEncodable {}`);
}

function structDeclarationForInputObjectType(generator, type) {
  const structName = type.name,
        description = type.description;

  const adoptedProtocols = ['GraphQLMapConvertible'];
  const properties = propertiesFromFields(generator.context, (0, _values2.default)(type.getFields()));

  (0, _language.structDeclaration)(generator, { structName: structName, description: description, adoptedProtocols: adoptedProtocols }, () => {
    generator.printOnNewline(`public var graphQLMap: GraphQLMap`);

    generator.printNewlineIfNeeded();
    generator.printOnNewline(`public init`);
    generator.print('(');
    generator.print((0, _printing.join)(properties.map((_ref14) => {
      let propertyName = _ref14.propertyName,
          type = _ref14.type,
          typeName = _ref14.typeName,
          isOptional = _ref14.isOptional;
      return (0, _printing.join)([`${propertyName}: ${typeName}`, isOptional && ' = nil']);
    }), ', '));
    generator.print(')');

    generator.withinBlock(() => {
      generator.printOnNewline((0, _printing.wrap)(`graphQLMap = [`, (0, _printing.join)(properties.map((_ref15) => {
        let name = _ref15.name,
            propertyName = _ref15.propertyName;
        return `"${name}": ${propertyName}`;
      }), ', ') || ':', `]`));
    });
  });
}
//# sourceMappingURL=codeGeneration.js.map