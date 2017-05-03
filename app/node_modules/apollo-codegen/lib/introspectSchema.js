'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.introspect = undefined;

var _stringify = require('babel-runtime/core-js/json/stringify');

var _stringify2 = _interopRequireDefault(_stringify);

var _asyncToGenerator2 = require('babel-runtime/helpers/asyncToGenerator');

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

let introspect = exports.introspect = (() => {
  var _ref = (0, _asyncToGenerator3.default)(function* (schemaContents) {
    const schema = (0, _graphql.buildASTSchema)((0, _graphql.parse)(schemaContents));
    return yield (0, _graphql.graphql)(schema, _utilities.introspectionQuery);
  });

  return function introspect(_x) {
    return _ref.apply(this, arguments);
  };
})();

var _fs = require('fs');

var _fs2 = _interopRequireDefault(_fs);

var _graphql = require('graphql');

var _utilities = require('graphql/utilities');

var _errors = require('./errors');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = (() => {
  var _ref2 = (0, _asyncToGenerator3.default)(function* (schemaPath, outputPath) {
    if (!_fs2.default.existsSync(schemaPath)) {
      throw new _errors.ToolError(`Cannot find GraphQL schema file: ${schemaPath}`);
    }

    const schemaContents = _fs2.default.readFileSync(schemaPath).toString();
    const result = yield introspect(schemaContents);

    if (result.errors) {
      throw new _errors.ToolError(`Errors in introspection query result: ${result.errors}`);
    }

    _fs2.default.writeFileSync(outputPath, (0, _stringify2.default)(result, null, 2));
  });

  function introspectSchema(_x2, _x3) {
    return _ref2.apply(this, arguments);
  }

  return introspectSchema;
})();
//# sourceMappingURL=introspectSchema.js.map