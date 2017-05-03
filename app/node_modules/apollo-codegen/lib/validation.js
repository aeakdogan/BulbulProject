'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.validateQueryDocument = validateQueryDocument;
exports.NoAnonymousQueries = NoAnonymousQueries;
exports.NoExplicitTypename = NoExplicitTypename;
exports.NoTypenameAlias = NoTypenameAlias;

var _graphql = require('graphql');

var _errors = require('./errors');

function validateQueryDocument(schema, document) {
  const rules = [NoAnonymousQueries, NoExplicitTypename, NoTypenameAlias].concat(_graphql.specifiedRules);

  const validationErrors = (0, _graphql.validate)(schema, document, rules);
  if (validationErrors && validationErrors.length > 0) {
    for (const error of validationErrors) {
      (0, _errors.logError)(error);
    }
    throw new _errors.ToolError("Validation of GraphQL query document failed");
  }
}

function NoAnonymousQueries(context) {
  return {
    OperationDefinition: function OperationDefinition(node) {
      if (!node.name) {
        context.reportError(new _graphql.GraphQLError('Apollo iOS does not support anonymous operations', [node]));
      }
      return false;
    }
  };
}

function NoExplicitTypename(context) {
  return {
    Field: function Field(node) {
      const fieldName = node.name.value;
      if (fieldName == "__typename") {
        context.reportError(new _graphql.GraphQLError('Apollo iOS inserts __typename automatically when needed, please do not include it explicitly', [node]));
      }
    }
  };
}

function NoTypenameAlias(context) {
  return {
    Field: function Field(node) {
      const aliasName = node.alias && node.alias.value;
      if (aliasName == "__typename") {
        context.reportError(new _graphql.GraphQLError('Apollo iOS needs to be able to insert __typename when needed, please do not use it as an alias', [node]));
      }
    }
  };
}
//# sourceMappingURL=validation.js.map