package com.myopicmobile.textwarrior.common;

/**
 * Created by Administrator on 2018/1/1.
 */

public interface JFlex {
     JavaScriptType next_token() throws java.io.IOException;
     void yypushback(int number);
     String yytext();
     char yycharat(int pos);
     int yylength();
     int yystate();
     void yyreset(java.io.Reader reader);
     void yyclose() throws java.io.IOException;
     int getHtml_ValueLength();
     int getChar_StringLength();
     int getStringLength();
}
