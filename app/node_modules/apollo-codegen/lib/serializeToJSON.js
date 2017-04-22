'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _stringify = require('babel-runtime/core-js/json/stringify');

var _stringify2 = _interopRequireDefault(_stringify);

var _values = require('babel-runtime/core-js/object/values');

var _values2 = _interopRequireDefault(_values);

exports.default = serializeToJSON;
exports.serializeAST = serializeAST;

var _graphql = require('graphql');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function serializeToJSON(context) {
  return serializeAST({
    operations: (0, _values2.default)(context.operations),
    fragments: (0, _values2.default)(context.fragments),
    typesUsed: context.typesUsed.map(serializeType)
  }, '\t');
}

function serializeAST(ast, space) {
  return (0, _stringify2.default)(ast, function (key, value) {
    if ((0, _graphql.isType)(value)) {
      if (expandTypes) {
        return serializeType(value);
      } else {
        return String(value);
      }
    } else {
      return value;
    }
  }, space);
}

function serializeType(type) {
  if (type instanceof _graphql.GraphQLEnumType) {
    return serializeEnumType(type);
  } else if (type instanceof _graphql.GraphQLInputObjectType) {
    return serializeInputObjectType(type);
  } else if (type instanceof _graphql.GraphQLScalarType) {
    return serializeScalarType(type);
  }
}

function serializeEnumType(type) {
  const name = type.name,
        description = type.description;

  const values = type.getValues();

  return {
    kind: 'EnumType',
    name: name,
    description: description,
    values: values.map(value => ({ name: value.name, description: value.description }))
  };
}

function serializeInputObjectType(type) {
  const name = type.name,
        description = type.description;

  const fields = (0, _values2.default)(type.getFields());

  return {
    kind: 'InputObjectType',
    name: name,
    description: description,
    fields: fields
  };
}

function serializeScalarType(type) {
  const name = type.name,
        description = type.description;


  return {
    kind: 'ScalarType',
    name: name,
    description: description
  };
}
//# sourceMappingURL=serializeToJSON.js.map