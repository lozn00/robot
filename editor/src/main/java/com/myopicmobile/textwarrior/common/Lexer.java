/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;

import com.myopicmobile.textwarrior.Lexer.JSLexer;
import com.myopicmobile.textwarrior.language.AndroidLanguage;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Does lexical analysis of a text for C-like languages.
 * The programming language syntax used is set as a static class variable.
 */
public class Lexer {
    public final static int NORMAL = 0;//常规
    public final static int KEYWORD = 1;//保留词
    public final static int INTEGER_LITERAL = 2;//数字
    public final static int LR = 3;//括号
    public final static int VAR_CONST_LET = 4;// var let const
    public final static int OPERATOR = 5;//数学运算符号
    public final static int FUNCTION = 6;//function
    public final static int COMMENT = 7;//注释
    public final static int SINGLE_SYMBOL_DELIMITED_A = 8;//字符串
    public final static int DOUBLE_SYMBOL_DELIMITED_MULTILINE = 9;//null undefined

    public final static int BREAK = 10;
    public final static int CASE = 11;
    public final static int CATCH = 12;
    public final static int CLASS = 13;
    public final static int CONTINUE = 14;
    public final static int DEBUGGER = 15;
    public final static int DEFAULT = 16;
    public final static int DELETE = 17;
    public final static int DO = 18;
    public final static int ELSE = 19;
    public final static int EXPORT = 20;
    public final static int EXTEDNS = 21;
    public final static int FINALLY = 22;
    public final static int FOR = 23;
    public final static int IF = 24;
    public final static int IMPORT = 25;
    public final static int IN = 26;
    public final static int INSTANCEOF = 27;
    public final static int NEW = 28;
    public final static int RETURN = 29;
    public final static int SUPER = 30;
    public final static int SWITCH = 31;
    public final static int THIS = 32;
    public final static int THROW = 33;
    public final static int TRY = 34;
    public final static int TYPEOF = 35;
    public final static int VOID = 36;
    public final static int WHILE = 37;
    public final static int WITH = 38;
    public final static int TRUE = 39;
    public final static int FALSE = 40;
    public final static int YIELD = 41;
    public final static int LIBS_INTHIS = 42;
    public final static int HTML_VALUE = 43;//Html Value
    public final static int VARNAME = 44;
    public final static int FUNCTIONNAME = 45;//Html Value
    private static boolean menable = true;
    private static Language _globalLanguage = LanguageNonProg.getInstance();

    synchronized public static void setLanguage(Language lang) {
        _globalLanguage = lang;
    }

    synchronized public static Language getLanguage() {
        return _globalLanguage;
    }

    public static void setEnable(boolean enable) {
        menable = enable;
    }

    public static void clear() {
        functions.clear();
        vars.clear();
        mAndroid = new String[]{};
        if (getLanguage() instanceof AndroidLanguage) {
            for (String s : AndroidLanguage.funtions) {
                functions.add(s);
            }
            mAndroid = AndroidLanguage.name;
        }
    }

    private DocumentProvider _hDoc;
    private LexThread _workerThread = null;
    LexCallback _callback = null;

    public Lexer(LexCallback callback) {
        _callback = callback;
    }

    public void tokenize(DocumentProvider hDoc) {
        if (!Lexer.getLanguage().isProgLang()) {
            return;
        }

        //tokenize will modify the state of hDoc; make a copy
        setDocument(new DocumentProvider(hDoc));
        if (_workerThread == null) {
            _workerThread = new LexThread(this);
            _workerThread.start();
        } else {
            _workerThread.restart();
        }
    }

    void tokenizeDone(List<Pair> result) {
        if (_callback != null) {
            _callback.lexDone(result);
        }
        _workerThread = null;
    }

    public void cancelTokenize() {
        if (_workerThread != null) {
            _workerThread.abort();
            _workerThread = null;
        }
    }

    public synchronized void setDocument(DocumentProvider hDoc) {
        _hDoc = hDoc;
    }

    public synchronized DocumentProvider getDocument() {
        return _hDoc;
    }

    private static ArrayList<String> functions = new ArrayList<>();
    private static ArrayList<String> vars = new ArrayList<>();

    public static String[] mAndroid = new String[]{};

    public ArrayList<String> getFunctions() {
        return functions;
    }

    public ArrayList<String> getVars() {
        return vars;
    }

    private class LexThread extends Thread {
        private boolean rescan = false;
        private final Lexer _lexManager;
        /**
         * can be set by another thread to stop the scan immediately
         */
        private final Flag _abort;
        /** A collection of Pairs, where Pair.first is the start
         *  position of the token, and Pair.second is the type of the token.*/
        /**
         * pair的集合，first表示token的开始，second表示token的类型
         */
        private List<Pair> _tokens;

        public LexThread(Lexer p) {
            _lexManager = p;
            _abort = new Flag();
        }

        @Override
        public void run() {
            do {
                rescan = false;
                _abort.clear();
                if (menable)
                    tokenize();
                else {
                    List<Pair> tokens = new ArrayList<>();
                    tokens.add(new Pair(0, NORMAL));
                    _tokens = tokens;
                }
            }
            while (rescan);

            if (!_abort.isSet()) {
                // lex complete
                _lexManager.tokenizeDone(_tokens);
            }
        }

        public void restart() {
            rescan = true;
            _abort.set();
        }

        public void abort() {
            _abort.set();
        }


        public void tokenize() {
            functions.clear();
            vars.clear();
            DocumentProvider hDoc = getDocument();
            Language language = Lexer.getLanguage();
            //这里用ArrayList速度会发生质的飞跃
            List<Pair> tokens = new ArrayList<>();

            //language.isProgLang()返回真
            if (!language.isProgLang()) {
                tokens.add(new Pair(0, NORMAL));
                _tokens = tokens;
                return;
            }
            StringReader stringReader = new StringReader(hDoc.toString());
            JFlex cLexer;
            if (language instanceof LanguageJavascript) {
                cLexer = new JavaScriptLexer(stringReader);
            } else if (language instanceof AndroidLanguage) {
                cLexer = new JSLexer(stringReader);
            } else {
                cLexer = new JavaScriptLexer(stringReader);
            }
            try {
                JavaScriptType cType = null;
                JavaScriptType last = null;
                int idx = 0;
                while ((cType = cLexer.next_token()) != JavaScriptType.EOF) {
                    if (cType == JavaScriptType.SPACE || cType == JavaScriptType.IDENTIFIER) ;
                    else {
                        last = cType;
                    }
                    switch (cType) {
                        case COMMA: {
                            tokens.add(new Pair(idx, OPERATOR));
                            break;
                        }
                        case SEMICOLON: {
                            tokens.add(new Pair(idx, OPERATOR));
                            break;
                        }
                        case IDENTIFIER: {
                            if (last == JavaScriptType.FUNCTION) {
                                if (!functions.contains(cLexer.yytext())) {
                                    functions.add(cLexer.yytext());
                                    tokens.add(new Pair(idx, FUNCTIONNAME));
                                }
                            } else if (last == JavaScriptType.VAR || last == JavaScriptType.LET || last == JavaScriptType.CONST) {
                                tokens.add(new Pair(idx, VARNAME));
                                if (!vars.contains(cLexer.yytext())) vars.add(cLexer.yytext());
                            }/////////////下方对，先不管
                            else {
                                if (vars.contains(cLexer.yytext())) {
                                    tokens.add(new Pair(idx, VARNAME));
                                } else if (functions.contains(cLexer.yytext())) {
                                    tokens.add(new Pair(idx, FUNCTIONNAME));
                                } else {
                                    tokens.add(new Pair(idx, NORMAL));
                                }
                            }
                            break;
                        }
                        case BREAK://关键词|保留词
                            tokens.add(new Pair(idx, BREAK));
                            break;
                        case CASE:
                            tokens.add(new Pair(idx, CASE));
                            break;
                        case CATCH:
                            tokens.add(new Pair(idx, CATCH));
                            break;
                        case CLASS:
                            tokens.add(new Pair(idx, CLASS));
                            break;
                        case CONTINUE:
                            tokens.add(new Pair(idx, CONTINUE));
                            break;
                        case DEBUGGER:
                            tokens.add(new Pair(idx, DEBUGGER));
                            break;
                        case DEFAULT:
                            tokens.add(new Pair(idx, DEFAULT));
                            break;
                        case DELETE:
                            tokens.add(new Pair(idx, DELETE));
                            break;
                        case DO:
                            tokens.add(new Pair(idx, DO));
                            break;
                        case ELSE:
                            tokens.add(new Pair(idx, ELSE));
                            break;
                        case EXPORT:
                            tokens.add(new Pair(idx, EXPORT));
                            break;
                        case EXTEDNS:
                            tokens.add(new Pair(idx, EXTEDNS));
                            break;
                        case FINALLY:
                            tokens.add(new Pair(idx, FINALLY));
                            break;
                        case FOR:
                            tokens.add(new Pair(idx, FOR));
                            break;
                        case IF:
                            tokens.add(new Pair(idx, IF));
                            break;
                        case IMPORT:
                            tokens.add(new Pair(idx, IMPORT));
                            break;
                        case IN:
                            tokens.add(new Pair(idx, IN));
                            break;
                        case INSTANCEOF:
                            tokens.add(new Pair(idx, INSTANCEOF));
                            break;
                        case NEW:
                            tokens.add(new Pair(idx, NEW));
                            break;
                        case RETURN:
                            tokens.add(new Pair(idx, RETURN));
                            break;
                        case SUPER:
                            tokens.add(new Pair(idx, SUPER));
                            break;
                        case SWITCH:
                            tokens.add(new Pair(idx, SWITCH));
                            break;
                        case THIS:
                            tokens.add(new Pair(idx, THIS));
                            break;
                        case THROW:
                            tokens.add(new Pair(idx, THROW));
                            break;
                        case TRY:
                            tokens.add(new Pair(idx, TRY));
                            break;
                        case TYPEOF:
                            tokens.add(new Pair(idx, TYPEOF));
                            break;
                        case VOID:
                            tokens.add(new Pair(idx, VOID));
                            break;
                        case LIBS_INTHIS:
                            if (language instanceof LanguageJavascript) {
                                tokens.add(new Pair(idx, LIBS_INTHIS));
                            }else{
                                tokens.add(new Pair(idx, NORMAL));
                            }
                            break;
                        case WHILE:
                            tokens.add(new Pair(idx, WHILE));
                            break;
                        case WITH:
                            tokens.add(new Pair(idx, WITH));
                            break;
                        case TRUE:
                            tokens.add(new Pair(idx, TRUE));
                            break;
                        case FALSE:
                            tokens.add(new Pair(idx, FALSE));
                            break;
                        case YIELD:
                            tokens.add(new Pair(idx, YIELD));
                            break;
                        case FUNCTION:
                            tokens.add(new Pair(idx, FUNCTION));
                            break;
                        case COMMENTS://注释
                            tokens.add(new Pair(idx, COMMENT));
                            break;
                        case VAR://var const let
                        case CONST:
                        case LET:
                            tokens.add(new Pair(idx, VAR_CONST_LET));
                            break;
                        case LBRACE:
                            tokens.add(new Pair(idx, LR));
                            break;
                        case LPAREN://括号
                        case LBRACK:
                            tokens.add(new Pair(idx, LR));
                            break;
                        case RBRACE:
                        case RPAREN:
                        case RBRACK:
                            tokens.add(new Pair(idx, LR));
                            break;
                        case EQ://数学运算符号
                            tokens.add(new Pair(idx, OPERATOR));
                            break;
                        case GT:
                        case LT:
                        case NOT:
                        case COMP:
                        case QUESTION:
                        case COLON:
                        case EQEQ:
                        case LTEQ:
                        case GTEQ:
                        case NOTEQ:
                        case ANDAND:
                        case OROR:
                        case PLUSPLUS:
                        case MINUSMINUS:
                        case PLUS:
                        case MINUS:
                        case MULT:
                        case DIV:
                        case AND:
                        case OR:
                        case XOR:
                        case MOD:
                        case AT:
                        case LSHIFT:
                        case RSHIFT:
                        case URSHIFT:
                        case PLUSEQ:
                        case MINUSEQ:
                        case MULTEQ:
                        case DIVEQ:
                        case ANDEQ:
                        case OREQ:
                        case XOREQ:
                        case MODEQ:
                        case LSHIFTEQ:
                        case RSHIFTEQ:
                        case URSHIFTEQ:
                        case DIV_COMMENT:
                            tokens.add(new Pair(idx, OPERATOR));
                            break;
                        case INTEGER_LITERAL://数字
                        case FLOATING_POINT_LITERAL:
                            tokens.add(new Pair(idx, INTEGER_LITERAL));
                            break;
                        case NULL://null  undefined
                        case UNDEFINED:
                            tokens.add(new Pair(idx, DOUBLE_SYMBOL_DELIMITED_MULTILINE));
                            break;

                        case STRING_LITERAL://字符串
                        case CHAR_LITERAL:
                            tokens.add(new Pair(idx, SINGLE_SYMBOL_DELIMITED_A));
                            break;
                        case HTML_VALUE:
                            tokens.add(new Pair(idx + 1, HTML_VALUE));
                            break;
                        default:
                            if (ProjectAutoTip.hasKey(cLexer.yytext()))
                                tokens.add(new Pair(idx,VARNAME));
                            else
                            tokens.add(new Pair(idx, NORMAL));
                            break;
                    }
                    if (cType == JavaScriptType.STRING_LITERAL) {
                        idx += (cLexer.getStringLength());
                    } else if (cType == JavaScriptType.CHAR_LITERAL) {
                        idx += cLexer.getChar_StringLength();
                    } else {
                        idx += (cLexer.yytext().length());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tokens.isEmpty()) {
                tokens.add(new Pair(0, NORMAL));
            }
            _tokens = tokens;
        }

    }//end inner class

    public interface LexCallback {
        public void lexDone(List<Pair> results);
    }
}
