#!/usr/bin/env node
'use strict';

var _asyncToGenerator2 = require('babel-runtime/helpers/asyncToGenerator');

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _slicedToArray2 = require('babel-runtime/helpers/slicedToArray');

var _slicedToArray3 = _interopRequireDefault(_slicedToArray2);

var _glob = require('glob');

var _glob2 = _interopRequireDefault(_glob);

var _process = require('process');

var _process2 = _interopRequireDefault(_process);

var _path = require('path');

var _path2 = _interopRequireDefault(_path);

var _yargs = require('yargs');

var _yargs2 = _interopRequireDefault(_yargs);

var _ = require('.');

var _errors = require('./errors');

require('source-map-support/register');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Make sure unhandled errors in async code are propagated correctly
_process2.default.on('unhandledRejection', error => {
  throw error;
});

_process2.default.on('uncaughtException', handleError);

function handleError(error) {
  (0, _errors.logError)(error);
  _process2.default.exit(1);
}

_yargs2.default.command(['introspect-schema <schema>', 'download-schema'], 'Generate an introspection JSON from a local GraphQL file or from a remote GraphQL server', {
  output: {
    demand: true,
    describe: 'Output path for GraphQL schema file',
    default: 'schema.json',
    normalize: true,
    coerce: _path2.default.resolve
  },
  header: {
    alias: 'H',
    describe: 'Additional header to send to the server as part of the introspection query request',
    type: 'array',
    coerce: arg => {
      let additionalHeaders = {};
      for (const header of arg) {
        var _header$split = header.split(/\s*:\s*/),
            _header$split2 = (0, _slicedToArray3.default)(_header$split, 2);

        const name = _header$split2[0],
              value = _header$split2[1];

        if (!(name && value)) {
          throw new _errors.ToolError('Headers should be specified as "Name: Value"');
        }
        additionalHeaders[name] = value;
      }
      return additionalHeaders;
    }
  },
  insecure: {
    alias: 'K',
    describe: 'Allows "insecure" SSL connection to the server',
    type: 'boolean'
  }
}, (() => {
  var _ref = (0, _asyncToGenerator3.default)(function* (argv) {
    const schema = argv.schema,
          output = argv.output,
          header = argv.header,
          insecure = argv.insecure;


    const urlRegex = /^https?:\/\//i;
    if (urlRegex.test(schema)) {
      yield (0, _.downloadSchema)(schema, output, header, insecure);
    } else {
      yield (0, _.introspectSchema)(schema, output);
    }
  });

  return function (_x) {
    return _ref.apply(this, arguments);
  };
})()).command('generate [input...]', 'Generate code from a GraphQL schema and query documents', {
  schema: {
    demand: true,
    describe: 'Path to GraphQL schema file',
    default: 'schema.json',
    normalize: true,
    coerce: _path2.default.resolve
  },
  output: {
    describe: 'Output directory for the generated files',
    normalize: true,
    coerce: _path2.default.resolve
  },
  target: {
    demand: false,
    describe: 'Code generation target language',
    choices: ['swift', 'json', 'ts', 'typescript', 'flow'],
    default: 'swift'
  },
  "passthrough-custom-scalars": {
    demand: false,
    describe: "Don't attempt to map custom scalars [temporary option]",
    default: false
  },
  "custom-scalars-prefix": {
    demand: false,
    describe: "Prefix for custom scalars. (Implies that passthrough-custom-scalars is true if set)",
    default: '',
    normalize: true
  }
}, argv => {
  let input = argv.input;

  // Use glob if the user's shell was unable to expand the pattern

  if (input.length === 1 && _glob2.default.hasMagic(input[0])) {
    input = _glob2.default.sync(input[0]);
  }
  const inputPaths = input.map(input => _path2.default.resolve(input));

  const options = { passthroughCustomScalars: argv["passthrough-custom-scalars"] || argv["custom-scalars-prefix"] !== '', customScalarsPrefix: argv["custom-scalars-prefix"] || '' };
  (0, _.generate)(inputPaths, argv.schema, argv.output, argv.target, options);
}).fail(function (message, error) {
  handleError(error ? error : new _errors.ToolError(message));
}).help().version().strict().argv;
//# sourceMappingURL=cli.js.map