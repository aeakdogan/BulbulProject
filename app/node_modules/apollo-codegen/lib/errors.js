'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _create = require('babel-runtime/core-js/object/create');

var _create2 = _interopRequireDefault(_create);

exports.ToolError = ToolError;
exports.logError = logError;
exports.logErrorMessage = logErrorMessage;

var _graphql = require('graphql');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// ToolError is used for errors that are part of the expected flow
// and for which a stack trace should not be printed

function ToolError(message) {
  this.message = message;
}

ToolError.prototype = (0, _create2.default)(Error.prototype, {
  constructor: { value: ToolError },
  name: { value: 'ToolError' }
});

const isRunningFromXcodeScript = process.env.XCODE_VERSION_ACTUAL;

function logError(error) {
  if (error instanceof ToolError) {
    logErrorMessage(error.message);
  } else if (error instanceof _graphql.GraphQLError) {
    const fileName = error.source && error.source.name;
    if (error.locations) {
      for (const location of error.locations) {
        logErrorMessage(error.message, fileName, location.line);
      }
    } else {
      logErrorMessage(error.message, fileName);
    }
  } else {
    console.log(error.stack);
  }
}

function logErrorMessage(message, fileName, lineNumber) {
  if (isRunningFromXcodeScript) {
    if (fileName && lineNumber) {
      // Prefixing error output with file name, line and 'error: ',
      // so Xcode will associate it with the right file and display the error inline
      console.log(`${fileName}:${lineNumber}: error: ${message}`);
    } else {
      // Prefixing error output with 'error: ', so Xcode will display it as an error
      console.log(`error: ${message}`);
    }
  } else {
    console.log(message);
  }
}
//# sourceMappingURL=errors.js.map