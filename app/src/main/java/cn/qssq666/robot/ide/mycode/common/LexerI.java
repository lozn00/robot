package cn.qssq666.robot.ide.mycode.common;

import java.io.IOException;

/**
 * Created by qssq on 2018/12/24 qssq666@foxmail.com
 */
public interface LexerI {
    int yylength();

    LuaTokenTypes advance() throws IOException;

    String yytext();
}
