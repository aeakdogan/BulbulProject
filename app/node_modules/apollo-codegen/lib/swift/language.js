'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _set = require('babel-runtime/core-js/set');

var _set2 = _interopRequireDefault(_set);

var _toConsumableArray2 = require('babel-runtime/helpers/toConsumableArray');

var _toConsumableArray3 = _interopRequireDefault(_toConsumableArray2);

exports.classDeclaration = classDeclaration;
exports.structDeclaration = structDeclaration;
exports.propertyDeclaration = propertyDeclaration;
exports.propertyDeclarations = propertyDeclarations;
exports.protocolDeclaration = protocolDeclaration;
exports.protocolPropertyDeclaration = protocolPropertyDeclaration;
exports.protocolPropertyDeclarations = protocolPropertyDeclarations;
exports.escapeIdentifierIfNeeded = escapeIdentifierIfNeeded;

var _printing = require('../utilities/printing');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function classDeclaration(generator, _ref, closure) {
  let className = _ref.className,
      modifiers = _ref.modifiers,
      superClass = _ref.superClass;
  var _ref$adoptedProtocols = _ref.adoptedProtocols;
  let adoptedProtocols = _ref$adoptedProtocols === undefined ? [] : _ref$adoptedProtocols,
      properties = _ref.properties;

  generator.printNewlineIfNeeded();
  generator.printNewline();
  generator.print((0, _printing.wrap)('', (0, _printing.join)(modifiers, ' '), ' '));
  generator.print(`class ${className}`);
  generator.print((0, _printing.wrap)(': ', (0, _printing.join)([superClass].concat((0, _toConsumableArray3.default)(adoptedProtocols)), ', ')));
  generator.pushScope({ typeName: className });
  generator.withinBlock(closure);
  generator.popScope();
}

function structDeclaration(generator, _ref2, closure) {
  let structName = _ref2.structName,
      description = _ref2.description;
  var _ref2$adoptedProtocol = _ref2.adoptedProtocols;
  let adoptedProtocols = _ref2$adoptedProtocol === undefined ? [] : _ref2$adoptedProtocol;

  generator.printNewlineIfNeeded();
  generator.printOnNewline(description && `/// ${description}`);
  generator.printOnNewline(`public struct ${structName}`);
  generator.print((0, _printing.wrap)(': ', (0, _printing.join)(adoptedProtocols, ', ')));
  generator.pushScope({ typeName: structName });
  generator.withinBlock(closure);
  generator.popScope();
}

function propertyDeclaration(generator, _ref3) {
  let propertyName = _ref3.propertyName,
      typeName = _ref3.typeName,
      description = _ref3.description;

  generator.printOnNewline(`public let ${propertyName}: ${typeName}`);
  generator.print(description && ` /// ${description}`);
}

function propertyDeclarations(generator, properties) {
  if (!properties) return;
  properties.forEach(property => propertyDeclaration(generator, property));
}

function protocolDeclaration(generator, _ref4, closure) {
  let protocolName = _ref4.protocolName,
      adoptedProtocols = _ref4.adoptedProtocols,
      properties = _ref4.properties;

  generator.printNewlineIfNeeded();
  generator.printOnNewline(`public protocol ${protocolName}`);
  generator.print((0, _printing.wrap)(': ', (0, _printing.join)(adoptedProtocols, ', ')));
  generator.pushScope({ typeName: protocolName });
  generator.withinBlock(closure);
  generator.popScope();
}

function protocolPropertyDeclaration(generator, _ref5) {
  let propertyName = _ref5.propertyName,
      typeName = _ref5.typeName;

  generator.printOnNewline(`var ${propertyName}: ${typeName} { get }`);
}

function protocolPropertyDeclarations(generator, properties) {
  if (!properties) return;
  properties.forEach(property => protocolPropertyDeclaration(generator, property));
}

const reservedKeywords = new _set2.default(['associatedtype', 'class', 'deinit', 'enum', 'extension', 'fileprivate', 'func', 'import', 'init', 'inout', 'internal', 'let', 'open', 'operator', 'private', 'protocol', 'public', 'static', 'struct', 'subscript', 'typealias', 'var', 'break', 'case', 'continue', 'default', 'defer', 'do', 'else', 'fallthrough', 'for', 'guard', 'if', 'in', 'repeat', 'return', 'switch', 'where', 'while', 'as', 'Any', 'catch', 'false', 'is', 'nil', 'rethrows', 'super', 'self', 'Self', 'throw', 'throws', 'true', 'try', 'associativity', 'convenience', 'dynamic', 'didSet', 'final', 'get', 'infix', 'indirect', 'lazy', 'left', 'mutating', 'none', 'nonmutating', 'optional', 'override', 'postfix', 'precedence', 'prefix', 'Protocol', 'required', 'right', 'set', 'Type', 'unowned', 'weak', 'willSet']);

function escapeIdentifierIfNeeded(identifier) {
  if (reservedKeywords.has(identifier)) {
    return '`' + identifier + '`';
  } else {
    return identifier;
  }
}
//# sourceMappingURL=language.js.map