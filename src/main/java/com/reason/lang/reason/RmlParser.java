package com.reason.lang.reason;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.reason.lang.CommonParser;
import com.reason.lang.ParserScope;
import com.reason.lang.ParserState;

import static com.intellij.codeInsight.completion.CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED;
import static com.intellij.lang.parser.GeneratedParserUtilBase.current_position_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.empty_element_parsed_guard_;
import static com.reason.lang.ParserScopeEnum.*;
import static com.reason.lang.ParserScopeType.groupExpression;
import static com.reason.lang.ParserScopeType.scopeExpression;

public class RmlParser extends CommonParser {

    RmlParser() {
        super(RmlTypes.INSTANCE);
    }

    @Override
    protected void parseFile(PsiBuilder builder, ParserState state) {
        IElementType tokenType = null;

        //long parseStart = System.currentTimeMillis();

        int c = current_position_(builder);
        while (true) {
            //long parseTime = System.currentTimeMillis();
            //if (5 < parseTime - parseStart) {
            // Protection: abort the parsing if too much time spent
            //break;
            //}

            state.previousTokenElementType = tokenType;
            tokenType = builder.getTokenType();
            if (tokenType == null) {
                break;
            }

            // Anything can be a new expression
            // A new element inside a { block starts an expression
            //if (parserState.isInScopeExpression() && parserState.isScopeElementType(m_types.LBRACE)) {
            //    parserState.add(markScope(builder, genericExpression, tokenType, startExpression, tokenType));
            //}

            // special keywords that can be used as lower identifier in records
            if (tokenType == m_types.REF && state.isResolution(recordBinding)) {
                parseLIdent(builder, state);
            } else if (tokenType == m_types.LIST && state.isResolution(recordBinding)) {
                parseLIdent(builder, state);
            } else if (tokenType == m_types.METHOD && state.isResolution(recordBinding)) {
                parseLIdent(builder, state);
            } else if (tokenType == m_types.STRING && state.isResolution(recordBinding)) {
                parseLIdent(builder, state);
            }
            //
            else if (tokenType == m_types.SEMI) {
                parseSemi(builder, state);
            } else if (tokenType == m_types.EQ) {
                parseEq(builder, state);
            } else if (tokenType == m_types.UNDERSCORE) {
                parseUnderscore(state);
            } else if (tokenType == m_types.ARROW) {
                parseArrow(builder, state);
            } else if (tokenType == m_types.TRY) {
                parseTry(builder, state);
            } else if (tokenType == m_types.SWITCH) {
                parseSwitch(builder, state);
            } else if (tokenType == m_types.LIDENT) {
                parseLIdent(builder, state);
            } else if (tokenType == m_types.UIDENT) {
                parseUIdent(builder, state);
            } else if (tokenType == m_types.ARROBASE) {
                parseArrobase(builder, state);
            } else if (tokenType == m_types.PERCENT) {
                parsePercent(builder, state);
            } else if (tokenType == m_types.COLON) {
                parseColon(builder, state);
            } else if (tokenType == m_types.STRING) {
                parseString(builder, state);
            } else if (tokenType == m_types.PIPE) {
                parsePipe(builder, state);
            } else if (tokenType == m_types.TILDE) {
                parseTilde(builder, state);
            } else if (tokenType == m_types.COMMA) {
                parseComma(state);
            } else if (tokenType == m_types.AND) {
                parseAnd(builder, state);
            } else if (tokenType == m_types.FUN) {
                parseFun(builder, state);
            } else if (tokenType == m_types.ASSERT) {
                parseAssert(builder, state);
            } else if (tokenType == m_types.IF) {
                parseIf(builder, state);
            }
            // ( ... )
            else if (tokenType == m_types.LPAREN) {
                parseLParen(builder, state);
            } else if (tokenType == m_types.RPAREN) {
                parseRParen(builder, state);
            }
            // { ... }
            else if (tokenType == m_types.LBRACE) {
                parseLBrace(builder, state);
            } else if (tokenType == m_types.RBRACE) {
                parseRBrace(builder, state);
            }
            // [ ... ]
            // [> ... ]
            else if (tokenType == m_types.LBRACKET) {
                parseLBracket(builder, state);
            } else if (tokenType == m_types.BRACKET_GT) {
                parseBracketGt(builder, state);
            } else if (tokenType == m_types.RBRACKET) {
                parseRBracket(builder, state);
            }
            // < ... >
            else if (tokenType == m_types.LT) {
                parseLt(builder, state);
            } else if (tokenType == m_types.TAG_LT_SLASH) {
                parseLtSlash(builder, state);
            } else if (tokenType == m_types.GT || tokenType == m_types.TAG_AUTO_CLOSE) {
                parseGtAutoClose(builder, state);
            }
            // {| ... |}
            else if (tokenType == m_types.ML_STRING_OPEN) {
                parseMlStringOpen(builder, state);
            } else if (tokenType == m_types.ML_STRING_CLOSE) {
                parseMlStringClose(builder, state);
            }
            // {j| ... |j}
            else if (tokenType == m_types.JS_STRING_OPEN) {
                parseJsStringOpen(builder, state);
            } else if (tokenType == m_types.JS_STRING_CLOSE) {
                parseJsStringClose(builder, state);
            }
            // Starts an expression
            else if (tokenType == m_types.OPEN) {
                parseOpen(builder, state);
            } else if (tokenType == m_types.INCLUDE) {
                parseInclude(builder, state);
            } else if (tokenType == m_types.EXTERNAL) {
                parseExternal(builder, state);
            } else if (tokenType == m_types.TYPE) {
                parseType(builder, state);
            } else if (tokenType == m_types.MODULE) {
                parseModule(builder, state);
            } else if (tokenType == m_types.LET) {
                parseLet(builder, state);
            } else if (tokenType == m_types.VAL) {
                parseVal(builder, state);
            } else {
                // if local scope, starts a new expression
                if (shouldStartExpression(state)) {
                    state.addStart(markScope(builder, genericExpression, m_types.GENERIC_EXPR, groupExpression, null));
                }
            }

            if (state.dontMove) {
                state.dontMove = false;
            } else {
                builder.advanceLexer();
            }

            if (!empty_element_parsed_guard_(builder, "reasonFile", c)) {
                break;
            }

            c = builder.rawTokenIndex();
        }
    }

    private void parseUnderscore(ParserState state) {
        if (state.isResolution(let)) {
            state.currentResolution(letNamed);
            state.complete();
        }
    }

    private void parseIf(PsiBuilder builder, ParserState state) {
        state.add(markCompleteScope(builder, ifThenStatement, m_types.IF_STMT, groupExpression, m_types.IF));
    }

    private void parseAssert(PsiBuilder builder, ParserState state) {
        state.addStart(markComplete(builder, assert_, m_types.ASSERT_STMT));
        state.dontMove = advance(builder);
        if (builder.getTokenType() != m_types.LPAREN) {
            state.add(markCompleteScope(builder, assertScope, m_types.SCOPED_EXPR, groupExpression, null));
        }
    }

    private void parseFun(PsiBuilder builder, ParserState state) {
        if (state.isResolution(letNamedEq)) {
            state.add(markScope(builder, funBody, m_types.LET_BINDING, groupExpression, m_types.FUN));
        }
    }

    private void parseAnd(PsiBuilder builder, ParserState state) {
        state.endUntilStart();
        if (isTypeResolution(state)) {
            state.endUntilScopeExpression(null);
            state.dontMove = advance(builder);
            state.addStart(mark(builder, type, m_types.EXP_TYPE));
            state.add(mark(builder, typeConstrName, m_types.TYPE_CONSTR_NAME));
        } else if (isLetResolution(state)) {
            state.endUntilScopeExpression(null);
            state.dontMove = advance(builder);
            state.addStart(mark(builder, let, m_types.LET_STMT));
        } else if (isModuleResolution(state)) {
            state.endUntilScopeExpression(null);
            state.dontMove = advance(builder);
            state.addStart(mark(builder, module, m_types.MODULE_STMT));
        }
    }

    private void parseComma(ParserState state) {
        if (state.isResolution(namedSymbolSignature)) {
            state.complete();
            state.popEnd();
        } else if (state.isResolution(recordSignature)) {
            state.complete();
            state.popEnd();
            state.popEnd();
        }
    }

    private void parseTilde(PsiBuilder builder, ParserState state) {
        IElementType nextToken = builder.rawLookup(1);
        if (m_types.LIDENT == nextToken) {
            state.add(mark(builder, namedSymbol, m_types.NAMED_SYMBOL));
        }
    }

    private void parsePipe(PsiBuilder builder, ParserState state) {
        if (state.isResolution(typeNamedEq)) {
            state.add(markCompleteScope(builder, typeNamedEqVariant, m_types.VARIANT_EXP, groupExpression, m_types.PIPE));
        } else if (state.isResolution(typeNamedEqVariant)) {
            state.popEnd();
            state.add(markCompleteScope(builder, typeNamedEqVariant, m_types.VARIANT_EXP, groupExpression, m_types.PIPE));
        } else if (state.isInScopeExpression()) {
            // By default, a pattern match
            if (state.isResolution(patternMatchBody)) {
                state.popEnd();
            }
            if (state.isResolution(patternMatch)) {
                state.popEnd();
            }
            state.add(markCompleteScope(builder, patternMatch, m_types.PATTERN_MATCH_EXPR, groupExpression, null));
        }
    }

    private void parseString(PsiBuilder builder, ParserState state) {
        if (state.isResolution(annotationName) || state.isResolution(macroName)) {
            state.endAny();
        } else if (state.isResolution(brace)) {
            IElementType nextToken = builder.lookAhead(1);
            if (m_types.COLON.equals(nextToken)) {
                state.currentResolution(jsObject);
                state.setTokenElementType(m_types.RECORD);
                state.dontMove = wrapWith(m_types.RECORD_FIELD, builder);
            }
        } else if (state.isResolution(jsObject)) {
            IElementType nextToken = builder.lookAhead(1);
            if (m_types.COLON.equals(nextToken)) {
                state.dontMove = wrapWith(m_types.RECORD_FIELD, builder);
            }
        }
    }

    private void parseMlStringOpen(PsiBuilder builder, ParserState state) {
        if (state.isResolution(annotationName) || state.isResolution(macroName)) {
            state.endAny();
        }

        state.add(markScope(builder, multilineStart, m_types.SCOPED_EXPR, scopeExpression, m_types.ML_STRING_OPEN));
    }

    private void parseMlStringClose(PsiBuilder builder, ParserState state) {
        ParserScope scope = state.endUntilScopeExpression(m_types.ML_STRING_OPEN);
        state.dontMove = advance(builder);

        if (scope != null) {
            scope.complete();
            state.popEnd();
        }

        state.updateCurrentScope();
    }

    private void parseJsStringOpen(PsiBuilder builder, ParserState state) {
        if (state.isResolution(annotationName) || state.isResolution(macroName)) { // use space notifier like in tag ?
            state.endAny();
        }

        state.add(markScope(builder, interpolationStart, m_types.SCOPED_EXPR, scopeExpression, m_types.JS_STRING_OPEN));
        state.dontMove = advance(builder);
        state.add(markComplete(builder, interpolationString, m_types.INTERPOLATION_EXPR));
    }

    private void parseJsStringClose(PsiBuilder builder, ParserState parserState) {
        ParserScope scope = parserState.endUntilScopeExpression(m_types.JS_STRING_OPEN);
        parserState.dontMove = advance(builder);

        if (scope != null) {
            scope.complete();
            parserState.popEnd();
        }
    }

    private void parseLet(PsiBuilder builder, ParserState state) {
        state.endAny();
        state.addStart(mark(builder, let, m_types.LET_STMT));
    }

    private void parseVal(PsiBuilder builder, ParserState state) {
        state.endAny();
        state.addStart(mark(builder, let, m_types.LET_STMT));
    }

    private void parseModule(PsiBuilder builder, ParserState state) {
        if (state.notResolution(annotationName)) {
            state.endUntilScopeExpression(null);
            state.addStart(mark(builder, module, m_types.MODULE_STMT));
        }
    }

    private void parseType(PsiBuilder builder, ParserState state) {
        if (state.notResolution(module)) {
            state.endUntilScopeExpression(null);
            state.addStart(mark(builder, type, m_types.EXP_TYPE));
            state.dontMove = advance(builder);
            state.add(mark(builder, typeConstrName, m_types.TYPE_CONSTR_NAME));
        }
    }

    private void parseExternal(PsiBuilder builder, ParserState state) {
        state.endAny();
        state.addStart(mark(builder, external, m_types.EXTERNAL_STMT));
    }

    private void parseOpen(PsiBuilder builder, ParserState state) {
        state.endAny();
        state.addStart(mark(builder, open, m_types.OPEN_STMT));
    }

    private void parseInclude(PsiBuilder builder, ParserState state) {
        state.endAny();
        state.addStart(mark(builder, include, m_types.INCLUDE_STMT));
    }

    private void parsePercent(PsiBuilder builder, ParserState parserState) {
        if (parserState.isResolution(macro)) {
            parserState.complete();
            parserState.add(markComplete(builder, macroName, m_types.MACRO_NAME));
            parserState.complete();
        }
    }

    private void parseColon(PsiBuilder builder, ParserState state) {
        if (state.isResolution(externalNamed)) {
            state.dontMove = advance(builder);
            state.add(markCompleteScope(builder, externalNamedSignature, m_types.SIG_SCOPE, groupExpression, m_types.SIG));
        } else if (state.isResolution(letNamed)) {
            state.dontMove = advance(builder);
            state.add(markCompleteScope(builder, letNamedSignature, m_types.SIG_SCOPE, groupExpression, m_types.SIG));
        } else if (state.isResolution(moduleNamed)) {
            // Module signature
            //   MODULE UIDENT COLON ...
            state.currentResolution(moduleNamedSignature);
            state.complete();
        } else if (state.isResolution(namedSymbol)) {
            state.complete();
            state.popEnd();

            state.dontMove = advance(builder);
            state.add(markScope(builder, namedSymbolSignature, m_types.SIG_SCOPE, groupExpression, null));
        } else if (state.isResolution(recordField)) {
            state.complete();
            state.dontMove = advance(builder);
            state.add(markScope(builder, recordSignature, m_types.SIG_SCOPE, groupExpression, m_types.SIG));
        }
    }

    private void parseArrobase(PsiBuilder builder, ParserState parserState) {
        if (parserState.isResolution(annotation)) {
            parserState.complete();
            parserState.add(markComplete(builder, annotationName, m_types.MACRO_NAME));
        }
    }

    private void parseLt(PsiBuilder builder, ParserState state) {
        // Can be a symbol or a JSX tag
        IElementType nextTokenType = builder.rawLookup(1);
        if (nextTokenType == m_types.LIDENT || nextTokenType == m_types.UIDENT || nextTokenType == m_types.OPTION) {
            // Surely a tag
            // Note that option is a ReasonML keyword but also a JSX keyword !
            builder.remapCurrentToken(m_types.TAG_LT);
            ParserScope tagScope = markCompleteScope(builder, startTag, m_types.TAG_START, groupExpression, m_types.TAG_LT);
            state.add(tagScope);

            state.dontMove = advance(builder);

            builder.remapCurrentToken(m_types.TAG_NAME);
            state.dontMove = wrapWith(nextTokenType == m_types.UIDENT ? m_types.UPPER_SYMBOL : m_types.LOWER_SYMBOL, builder);
        }
    }

    private void parseLtSlash(PsiBuilder builder, ParserState state) {
        IElementType nextTokenType = builder.rawLookup(1);
        if (nextTokenType == m_types.LIDENT || nextTokenType == m_types.UIDENT) {
            // A closing tag
            builder.remapCurrentToken(m_types.TAG_LT);
            state.add(markCompleteScope(builder, closeTag, m_types.TAG_CLOSE, groupExpression, m_types.TAG_LT));

            state.dontMove = advance(builder);

            builder.remapCurrentToken(m_types.TAG_NAME);
            state.dontMove = wrapWith(nextTokenType == m_types.UIDENT ? m_types.UPPER_SYMBOL : m_types.LOWER_SYMBOL, builder);
        }
    }

    private void parseGtAutoClose(PsiBuilder builder, ParserState state) {
        if (state.isCurrentCompositeElementType(m_types.TAG_PROPERTY)) {
            state.popEnd();
        }

        if (state.isResolution(startTag) || state.isResolution(closeTag)) {
            builder.remapCurrentToken(m_types.TAG_GT);
            state.dontMove = advance(builder);
            state.popEnd();
        }
    }

    private void parseLIdent(PsiBuilder builder, ParserState state) {
        boolean processSingleParam = false;

        if (state.isResolution(modulePath)) {
            state.popEnd();
        }

        if (state.isResolution(maybeRecord)) {
            // Maybe a record, we must check
            IElementType nextTokenType = builder.lookAhead(1);
            if (nextTokenType == m_types.COLON) {
                // Yes, this is a record binding
                state.currentResolution(recordBinding);
                state.currentCompositeElementType(m_types.RECORD_EXPR);
            }
        }

        if (state.isResolution(typeConstrName)) {
            // TYPE LIDENT ...
            state.currentResolution(typeNamed);
            state.complete();
            state.setPreviousComplete();
        } else if (state.isResolution(external)) {
            state.currentResolution(externalNamed);
            state.complete();
        } else if (state.isResolution(let)) {
            state.currentResolution(letNamed);
            state.complete();
        } else if (state.isResolution(letNamedEq)) {
            if (state.previousTokenElementType == m_types.EQ) {
                IElementType nextElementType = builder.lookAhead(1);
                if (nextElementType == m_types.ARROW) {
                    // Single (paren less) function parameters
                    state.add(markComplete(builder, function, m_types.FUN_EXPR));
                    state.add(markComplete(builder, parameters, m_types.FUN_PARAMS));
                    processSingleParam = true;
                }
            }
        } else if (state.isResolution(startTag)) {
            // This is a property
            state.endAny();
            builder.remapCurrentToken(m_types.PROPERTY_NAME);
            state.add(markComplete(builder, tagProperty, m_types.TAG_PROPERTY));
            builder.setWhitespaceSkippedCallback((type, start, end) -> {
                if (state.isResolution(tagProperty) || (state.isResolution(tagPropertyEq) && state.notInScopeExpression())) {
                    state.popEnd();
                    builder.setWhitespaceSkippedCallback(null);
                }
            });
        } else if (state.isResolution(recordBinding)) {
            state.add(markScope(builder, recordField, m_types.RECORD_FIELD, groupExpression, null));
        } else if (shouldStartExpression(state)) {
            state.addStart(mark(builder, genericExpression, builder.getTokenType()));
        }

        state.dontMove = wrapWith(m_types.LOWER_SYMBOL, builder);

        if (processSingleParam) {
            state.popEnd();
        }
    }

    private void parseLBracket(PsiBuilder builder, ParserState parserState) {
        IElementType nextTokenType = builder.rawLookup(1);
        if (nextTokenType == m_types.ARROBASE) {
            parserState.add(markScope(builder, annotation, m_types.ANNOTATION_EXPR, scopeExpression, m_types.LBRACKET));
        } else if (nextTokenType == m_types.PERCENT) {
            parserState.add(markScope(builder, macro, m_types.MACRO_EXPR, scopeExpression, m_types.LBRACKET));
        } else {
            parserState.add(markScope(builder, bracket, m_types.SCOPED_EXPR, scopeExpression, m_types.LBRACKET));
        }
    }

    private void parseBracketGt(PsiBuilder builder, ParserState parserState) {
        parserState.add(markScope(builder, bracketGt, m_types.SCOPED_EXPR, scopeExpression, m_types.LBRACKET));
    }

    private void parseRBracket(PsiBuilder builder, ParserState state) {
        ParserScope scope = state.endUntilScopeExpression(m_types.LBRACKET);

        state.dontMove = advance(builder);

        if (scope != null) {
            if (scope.isNotResolution(annotation)) {
                scope.complete();
            }
            state.popEnd();
        }
    }

    private void parseLBrace(PsiBuilder builder, ParserState state) {
        if (state.isResolution(typeNamedEq)) {
            state.add(markScope(builder, recordBinding, m_types.RECORD_EXPR, scopeExpression, m_types.LBRACE));
        } else if (state.isResolution(moduleNamedEq) || state.isResolution(moduleNamedSignature)) {
            state.add(markScope(builder, moduleBinding, m_types.SCOPED_EXPR, scopeExpression, m_types.LBRACE));
        } else if (state.isResolution(letNamedEq)) {
            state.add(markScope(builder, maybeRecord, m_types.SCOPED_EXPR, scopeExpression, m_types.LBRACE));
        } else if (state.isResolution(ifThenStatement)) {
            state.add(markScope(builder, brace, m_types.SCOPED_EXPR, scopeExpression, m_types.LBRACE));
        } else {
            ParserScope scope;
            if (state.isResolution(switchBinaryCondition)) {
                scope = state.endUntilScopeExpression(m_types.SWITCH);
            } else {
                scope = state.endAny();
            }

            boolean isSwitch = scope != null && scope.isResolution(switch_);
            state.add(markScope(builder, isSwitch ? switchBody : brace, m_types.SCOPED_EXPR, scopeExpression, isSwitch ? m_types.SWITCH : m_types.LBRACE));
        }
    }

    private void parseRBrace(PsiBuilder builder, ParserState state) {
        ParserScope scope = state.endUntilScopeExpression(m_types.LBRACE);
        state.dontMove = advance(builder);

        if (scope != null) {
            scope.complete();
            state.popEnd();
        }
    }

    private void parseLParen(PsiBuilder builder, ParserState state) {
        if (state.isResolution(modulePath) && state.previousTokenElementType == m_types.DOT) {
            state.currentResolution(localOpen);
            state.currentCompositeElementType(m_types.LOCAL_OPEN);
            state.complete();
            state.add(markScope(builder, paren, m_types.SCOPED_EXPR, scopeExpression, m_types.LPAREN));
        } else if (state.isResolution(ifThenStatement)) {
            state.complete();
            state.add(markCompleteScope(builder, binaryCondition, m_types.BIN_CONDITION, scopeExpression, m_types.LPAREN));
        } else {

            if (state.isResolution(external)) {
                // overloading an operator
                state.currentResolution(externalNamed);
                state.complete();
            }

            if (!state.isResolution(typeNamed) && !state.isResolution(tagPropertyEq)) { // ???
                state.endAny();
            }

            if (!state.isResolution(patternMatch) && !state.isResolution(recordSignature) && !state.isResolution(letNamedSignature) && !state.isResolution(tagPropertyEq)) {
                // just a marker that will be used only if it's a function (duplicate the current token type)
                state.add(mark(builder, genericExpression, m_types.LPAREN));
            }

            state.add(markScope(builder, paren, m_types.SCOPED_EXPR, scopeExpression, m_types.LPAREN));
        }
    }

    private void parseRParen(PsiBuilder builder, ParserState state) {
        ParserScope parenScope = state.endUntilScopeExpression(m_types.LPAREN);
        state.dontMove = advance(builder);
        IElementType nextTokenType = builder.getTokenType();

        if (parenScope != null) {
            // Remove the scope from the stack, we want to test its parent
            state.pop();

            if (nextTokenType == m_types.ARROW && !state.isResolution(patternMatch) && !state.isCurrentTokenType(m_types.SIG)) {
                parenScope.resolution(parameters);
                parenScope.compositeElementType(m_types.FUN_PARAMS);
            }

            parenScope.complete();
            parenScope.end();

            // Handle the generic scope
            if (parenScope.isResolution(parameters) && nextTokenType == m_types.ARROW) {
                // Transform the generic scope to a function scope
                state.currentResolution(function);
                state.currentCompositeElementType(m_types.FUN_EXPR);
                state.setCurrentScopeType(groupExpression);
                state.complete();
            } else if (state.isResolution(genericExpression)) {
                state.popEnd();
            }

            ParserScope scope = state.getLatestScope();
            if (scope != null && (scope.isResolution(localOpen) || scope.isResolution(tagPropertyEq))) {
                state.popEnd();
            }
        }
    }

    private void parseEq(PsiBuilder builder, ParserState state) {
        if (state.isResolution(typeNamed)) {
            state.popEnd();
            state.currentResolution(typeNamedEq);
            state.dontMove = advance(builder);
            state.add(markCompleteScope(builder, typeNamedEq, m_types.TYPE_BINDING, groupExpression, null));
        } else if (state.isResolution(letNamed) || state.isResolution(letNamedSignature)) {
            if (state.isResolution(letNamedSignature)) {
                state.popEnd();
            }
            state.currentResolution(letNamedEq);
            state.dontMove = advance(builder);
            state.add(markCompleteScope(builder, letNamedEq, m_types.LET_BINDING, scopeExpression, null));
        } else if (state.isResolution(tagProperty)) {
            state.currentResolution(tagPropertyEq);
        } else if (state.isResolution(moduleNamed)) {
            state.currentResolution(moduleNamedEq);
            state.complete();
        } else if (state.isResolution(externalNamedSignature)) {
            state.complete();
            state.endUntilStart();
        }
    }

    private void parseSemi(PsiBuilder builder, ParserState state) {
        if (state.isResolution(patternMatchBody)) {
            state.endAny();
        } else {
            // End current start-expression scope
            ParserScope scope = state.endUntilStart();
            if (state.isStart(scope)) {
                state.dontMove = advance(builder);
                state.popEnd();
            }
        }
    }

    private void parseUIdent(PsiBuilder builder, ParserState state) {
        if (DUMMY_IDENTIFIER_TRIMMED.equals(builder.getTokenText())) {
            return;
        }

        if (state.isResolution(open)) {
            // It is a module name/path
            state.complete();
        } else if (state.isResolution(include)) {
            // It is a module name/path
            state.complete();
        } else if (state.isResolution(module)) {
            state.currentResolution(moduleNamed);
        } else if ((state.isResolution(startTag) || state.isResolution(closeTag)) && state.previousTokenElementType == m_types.DOT) {
            // a namespaced custom component
            builder.remapCurrentToken(m_types.TAG_NAME);
        } else if (state.isResolution(typeNamedEqVariant) && state.previousTokenElementType == m_types.PIPE) {
            builder.remapCurrentToken(m_types.VARIANT_NAME);
        } else {
            if (shouldStartExpression(state)) {
                state.addStart(mark(builder, genericExpression, builder.getTokenType()));
            }

            if (!state.isResolution(modulePath)) {
                IElementType nextElementType = builder.lookAhead(1);
                if (nextElementType == m_types.DOT) {
                    // We are parsing a module path
                    state.add(mark(builder, modulePath, m_types.UPPER_SYMBOL));
                }
            }
        }

        state.dontMove = wrapWith(m_types.UPPER_SYMBOL, builder);
    }

    private void parseSwitch(PsiBuilder builder, ParserState state) {
        boolean inScope = state.isScopeTokenElementType(m_types.LBRACE);
        ParserScope scope = markCompleteScope(builder, switch_, m_types.SWITCH_EXPR, groupExpression, m_types.SWITCH);
        state.add(scope, inScope);
        state.dontMove = advance(builder);
        state.add(markCompleteScope(builder, switchBinaryCondition, m_types.BIN_CONDITION, groupExpression, null));
    }

    private void parseTry(PsiBuilder builder, ParserState parserState) {
        parserState.add(markCompleteScope(builder, try_, m_types.TRY_EXPR, groupExpression, m_types.TRY));
        parserState.dontMove = advance(builder);
        parserState.add(markCompleteScope(builder, tryBinaryCondition, m_types.BIN_CONDITION, groupExpression, null));
    }

    private void parseArrow(PsiBuilder builder, ParserState state) {
        state.dontMove = advance(builder);
        IElementType nextTokenType = builder.getTokenType();

        if (state.isResolution(function)) {
            // let x = ($ANY) => <EXPR>
            state.add(markCompleteScope(builder, funBody, m_types.FUN_BODY, groupExpression, null));
        } else if (nextTokenType != m_types.LBRACE) {
            if (state.isResolution(patternMatch)) {
                state.add(markScope(builder, patternMatchBody, m_types.SCOPED_EXPR, groupExpression, null));
            }
        }
    }

    private boolean shouldStartExpression(ParserState state) {
        return state.isInScopeExpression() && state.isScopeTokenElementType(m_types.LBRACE);
    }
}
