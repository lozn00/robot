package com.myopicmobile.textwarrior.language;


import com.myopicmobile.textwarrior.common.Language;

/**
 * Created by Administrator on 2018/2/4.
 */

public class JSProjectLanguage extends Language {
    private static Language _theOne = null;
    private final static String[] keywords = {
            "break","case","catch","class","const",
            "continue","debugger","default","delete","do",
            "else","export","extends","finally","for",
            "function","if","import","in","instanceof",
            "new","return","final","in","float",
            "for","super","switch","this",
            "throw","try","typeof","var","void",
            "while","let","with","yield",
            "true","false","null","undefined"
    };
    private final static char[] BASIC_OPERATORS = {
            '\t','\"','\'','(', ')', '{', '}', '[', ']', '<', '>',
            '.', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', ':',
            '?', '~', '%', '^'
    };
    private  final  static  String[] funtions={};
    public static Language getInstance(){
        if(_theOne == null){
            _theOne = new JSProjectLanguage();
        }
        return _theOne;
    }

    private JSProjectLanguage(){
        setKeywords(keywords);
        setOperatorlist(BASIC_OPERATORS);
        setNames(funtions);//
    }

    public boolean isLineAStart(char c){
        return false;
    }
}
