'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.escapedString = escapedString;
exports.multilineString = multilineString;

var _printing = require('../utilities/printing');

function escapedString(string) {
  return string.replace(/"/g, '\\"');
}

function multilineString(context, string) {
  const lines = string.split('\n');
  lines.forEach((line, index) => {
    const isLastLine = index != lines.length - 1;
    context.printOnNewline(`"${escapedString(line)}"` + (isLastLine ? ' +' : ''));
  });
}
//# sourceMappingURL=values.js.map