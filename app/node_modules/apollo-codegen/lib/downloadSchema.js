'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _stringify = require('babel-runtime/core-js/json/stringify');

var _stringify2 = _interopRequireDefault(_stringify);

var _assign = require('babel-runtime/core-js/object/assign');

var _assign2 = _interopRequireDefault(_assign);

var _asyncToGenerator2 = require('babel-runtime/helpers/asyncToGenerator');

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _nodeFetch = require('node-fetch');

var _nodeFetch2 = _interopRequireDefault(_nodeFetch);

var _fs = require('fs');

var _fs2 = _interopRequireDefault(_fs);

var _path = require('path');

var _path2 = _interopRequireDefault(_path);

var _https = require('https');

var _https2 = _interopRequireDefault(_https);

var _utilities = require('graphql/utilities');

var _errors = require('./errors');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Based on https://facebook.github.io/relay/docs/guides-babel-plugin.html#using-other-graphql-implementations

const defaultHeaders = {
  'Accept': 'application/json',
  'Content-Type': 'application/json'
};

exports.default = (() => {
  var _ref = (0, _asyncToGenerator3.default)(function* (url, outputPath, additionalHeaders, insecure) {
    const headers = (0, _assign2.default)(defaultHeaders, additionalHeaders);
    const agent = insecure ? new _https2.default.Agent({ rejectUnauthorized: false }) : null;

    let result;
    try {
      const response = yield (0, _nodeFetch2.default)(url, {
        method: 'POST',
        headers: headers,
        body: (0, _stringify2.default)({ 'query': _utilities.introspectionQuery }),
        agent: agent
      });

      result = yield response.json();
    } catch (error) {
      throw new _errors.ToolError(`Error while fetching introspection query result: ${error.message}`);
    }

    if (result.errors) {
      throw new _errors.ToolError(`Errors in introspection query result: ${result.errors}`);
    }

    const schemaData = result;
    if (!schemaData.data) {
      throw new _errors.ToolError(`No introspection query result data found, server responded with: ${(0, _stringify2.default)(result)}`);
    }

    _fs2.default.writeFileSync(outputPath, (0, _stringify2.default)(schemaData, null, 2));
  });

  function downloadSchema(_x, _x2, _x3, _x4) {
    return _ref.apply(this, arguments);
  }

  return downloadSchema;
})();
//# sourceMappingURL=downloadSchema.js.map