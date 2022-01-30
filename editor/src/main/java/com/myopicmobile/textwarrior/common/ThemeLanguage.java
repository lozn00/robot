package com.myopicmobile.textwarrior.common;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ThemeLanguage extends Language {
    private static Language _theOne = null;
    private final String DEFAULT = "KEY_WORDS|PARENTHESES";
    private final String KEYWORDS = "BREAK|CASE|CATCH|CLASS|CONST|CONTINUE|DEBUGGER|DEFAULT|DELETE|DO|ELSE|EXPORT|EXTEDNS|FINALLY|" +
            "FOR|FUNCTION|IF|IMPORT|IN|INSTANCEOF|NEW|RETURN|SUPER|SWITCH|THIS|THROW|TRY|TYPEOF|VAR|VOID|WHILE|LET|WITH|YIELD|LIBS_INTHIS|" +
            "TRUE|FALSE|NULL|UNDEFINED";
    private final String PARENTHESES = "LPAREN|RPAREN|LBRACE|RBRACE|LBRACK|RBRACK|SEMICOLON|COMMA|DOT|EQ|SPACE|DIV|GT|" +
            "LT|NOT|COMP|QUESTION|AT|COLON|PLUS|MINUS|MULT|DIV|AND|OR|XOR|MOD";

    private final String OTHER = "INTEGER_LITERAL|FLOATING_POINT_LITERAL|COMMENTS|STRING_LITERAL|IDENTIFIER|EOF|VARNAME|FUNCTIONNAME";
    private final String EDITOR = "CARET_DISABLED|CARET_BACKGROUND|CARET_FOREGROUND|SELECT_BACKGROUND|SELECT_TEXT|INPUT_LINE|LINE_NUMBER|BACKGROUND|DEFAULT_TEXT";
    private final String[] KEY = {"DEFAULT",
            "KEYWORDS","PARENTHESES","OTHER","EDITOR"};
    public static Language getInstance(){
        if(_theOne == null){
            _theOne = new ThemeLanguage();
        }
        return _theOne;
    }
    private ThemeLanguage(){
//        setKeywords(KEY.split("\\|"));
        setKeywords(new String[]{});
        super.setNames(KEY);
        super.addBasePackage("DEFAULT",DEFAULT.split("\\|"));
        super.addBasePackage("KEYWORDS",KEYWORDS.split("\\|"));
        super.addBasePackage("PARENTHESES",PARENTHESES.split("\\|"));
        super.addBasePackage("OTHER",OTHER.split("\\|"));
        super.addBasePackage("EDITOR",EDITOR.split("\\|"));
    }

    public boolean isLineAStart(char c){
        return false;
    }
}