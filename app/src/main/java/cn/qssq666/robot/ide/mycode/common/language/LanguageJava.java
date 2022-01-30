package cn.qssq666.robot.ide.mycode.common.language;


import cn.qssq666.robot.ide.mycode.common.Language;

//ignore_start
public class LanguageJava extends Language {
    private static Language _theOne = (Language) null;
    private static final String[] keywords = new String[]{"void", "boolean", "byte", "char", "short", "int", "long", "float", "double", "strictfp", "import", "package", "new", "class", "interface", "extends", "implements", "enum", "public", "private", "protected", "static", "abstract", "final", "native", "volatile", "assert", "try", "throw", "throws", "catch", "finally", "instanceof", "super", "this", "if", "else", "for", "do", "while", "switch", "case", "default", "continue", "break", "return", "synchronized", "transient", "true", "false", "null"};

    public LanguageJava() {
        super.setKeywords(keywords);
    }

    public static Language getInstance() {
        if (_theOne == null) {
            _theOne = new LanguageJava();
        }

        return _theOne;
    }

    public boolean isLineAStart(char var1) {
        return false;
    }

    //ignore_end
}
