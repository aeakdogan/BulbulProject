'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.typeDeclaration = typeDeclaration;
exports.propertyDeclaration = propertyDeclaration;
exports.propertyDeclarations = propertyDeclarations;

var _printing = require('../utilities/printing');

function typeDeclaration(generator, _ref, closure) {
  let interfaceName = _ref.interfaceName;

  generator.printNewlineIfNeeded();
  generator.printNewline();
  generator.print(`export type ${interfaceName} =`);
  generator.pushScope({ typeName: interfaceName });
  generator.withinBlock(closure);
  generator.popScope();
  generator.print(';');
}

function propertyDeclaration(generator, _ref2, closure) {
  let propertyName = _ref2.propertyName,
      typeName = _ref2.typeName,
      description = _ref2.description,
      isArray = _ref2.isArray,
      isNullable = _ref2.isNullable,
      inInterface = _ref2.inInterface,
      fragmentSpreads = _ref2.fragmentSpreads;

  generator.printOnNewline(description && `// ${description}`);
  if (closure) {
    generator.printOnNewline(`${propertyName}:`);
    if (isNullable) {
      generator.print(' ?');
    }
    if (isArray) {
      if (!isNullable) {
        generator.print(' ');
      }
      generator.print('Array<');
    }
    if (fragmentSpreads && fragmentSpreads.length > 0) {
      if (!isNullable) {
        generator.print(' ');
      } else {
        generator.print('(');
      }
      generator.print(`${fragmentSpreads.map(n => `${n}Fragment`).join(' & ')} &`);
    }
    generator.pushScope({ typeName: propertyName });
    generator.withinBlock(closure);
    generator.popScope();
    if (isNullable && fragmentSpreads && fragmentSpreads.length > 0) {
      generator.print(')');
    }
    if (isArray) {
      generator.print(' >');
    }
  } else if (fragmentSpreads && fragmentSpreads.length > 0) {
    generator.printOnNewline(`${propertyName}: ${isArray ? 'Array<' : ''}${fragmentSpreads.map(n => `${n}Fragment`).join(' & ')}${isArray ? '>' : ''}`);
  } else {
    generator.printOnNewline(`${propertyName}: ${typeName}`);
  }
  generator.print(',');
}

function propertyDeclarations(generator, properties) {
  if (!properties) return;
  properties.forEach(property => propertyDeclaration(generator, property));
}
//# sourceMappingURL=language.js.map