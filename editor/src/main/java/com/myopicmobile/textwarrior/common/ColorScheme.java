/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */

package com.myopicmobile.textwarrior.common;

import java.util.HashMap;

public abstract class ColorScheme
{
	public enum Colorable
	{
		FOREGROUND, BACKGROUND, SELECTION_FOREGROUND, SELECTION_BACKGROUND,
		CARET_FOREGROUND, CARET_BACKGROUND, CARET_DISABLED, LINE_HIGHLIGHT,
		NON_PRINTING_GLYPH, COMMENT, KEYWORD,STRING,
		INTEGER_LITERAL,OPERATOR,VAR_CONST_LET,LR,DOUBLE_SYMBOL_DELIMITED_MULTILINE,
		BREAK,CASE,CATCH,CLASS,CONST,CONTINUE,DEBUGGER,DEFAULT,DELETE,DO,ELSE,EXPORT,EXTEDNS,FINALLY,
		FOR,FUNCTION,IF,IMPORT,IN,INSTANCEOF,NEW,RETURN,SUPER,SWITCH,THIS,THROW,TRY,TYPEOF,VAR,VOID,WHILE,LET,WITH,YIELD,LIBS_INTHIS,
			TRUE,FALSE,NULL,UNDEFINED,HTML_VALUE,VARNAME,FUNCTIONNAME
		}

	protected HashMap<Colorable, Integer> _colors = generateDefaultColors();

	public void setColor(Colorable colorable, int color)
	{
		_colors.put(colorable, color);
	}

	public int getColor(Colorable colorable)
	{
		Integer color = _colors.get(colorable);
		if (color == null)
		{
			TextWarriorException.fail("Color not specified for " + colorable);
			return 0;
		}
		return color.intValue();
	}

	// Currently, color scheme is tightly coupled with semantics of the token types
	public int getTokenColor(int tokenType)
	{
		Colorable element;
		switch (tokenType)
		{
			case Lexer.BREAK://常规
				element = Colorable.BREAK;
				break;
			case Lexer.CASE://常规
				element = Colorable.CASE;
				break;
			case Lexer.CATCH://常规
				element = Colorable.CATCH;
				break;
			case Lexer.CLASS://常规
				element = Colorable.CLASS;
				break;
			case Lexer.CONTINUE://常规
				element = Colorable.CONTINUE;
				break;
			case Lexer.DEBUGGER://常规
				element = Colorable.DEBUGGER;
				break;
			case Lexer.DEFAULT://常规
				element = Colorable.DEFAULT;
				break;
			case Lexer.DELETE://常规
				element = Colorable.DELETE;
				break;
			case Lexer.DO://常规
				element = Colorable.DO;
				break;
			case Lexer.ELSE://常规
				element = Colorable.ELSE;
				break;
			case Lexer.EXPORT://常规
				element = Colorable.EXPORT;
				break;
			case Lexer.EXTEDNS://常规
				element = Colorable.EXTEDNS;
				break;
			case Lexer.FINALLY://常规
				element = Colorable.FINALLY;
				break;
			case Lexer.FOR://常规
				element = Colorable.FOR;
				break;
			case Lexer.IF://常规
				element = Colorable.IF;
				break;
			case Lexer.IMPORT://常规
				element = Colorable.IMPORT;
				break;
			case Lexer.IN://常规
				element = Colorable.IN;
				break;
			case Lexer.INSTANCEOF://常规
				element = Colorable.INSTANCEOF;
				break;
			case Lexer.NEW://常规
				element = Colorable.NEW;
				break;
			case Lexer.RETURN://常规
				element = Colorable.RETURN;
				break;
			case Lexer.SUPER://常规
				element = Colorable.SUPER;
				break;
			case Lexer.SWITCH://常规
				element = Colorable.SWITCH;
				break;
			case Lexer.THIS://常规
				element = Colorable.THIS;
				break;
			case Lexer.THROW://常规
				element = Colorable.THROW;
				break;
			case Lexer.TRY://常规
				element = Colorable.TRY;
				break;
			case Lexer.TYPEOF://常规
				element = Colorable.TYPEOF;
				break;
			case Lexer.VOID://常规
				element = Colorable.VOID;
				break;
			case Lexer.WHILE://常规
				element = Colorable.WHILE;
				break;
			case Lexer.WITH://常规
				element = Colorable.WITH;
				break;
			case Lexer.TRUE://常规
				element = Colorable.TRUE;
				break;
			case Lexer.FALSE://常规
				element = Colorable.FALSE;
				break;
			case Lexer.YIELD://常规
				element = Colorable.YIELD;
				break;
			case Lexer.LIBS_INTHIS://常规
				element = Colorable.LIBS_INTHIS;
				break;




			case Lexer.NORMAL://常规
				element = Colorable.FOREGROUND;
				break;
			case Lexer.KEYWORD://关键词
				element = Colorable.KEYWORD;
				break;
			case Lexer.FUNCTION://function
				element = Colorable.FUNCTION;
				break;
			case Lexer.VAR_CONST_LET://var const let
				element = Colorable.VAR_CONST_LET;
				break;
			case Lexer.LR://括号
				element = Colorable.LR;
				break;
			case Lexer.DOUBLE_SYMBOL_DELIMITED_MULTILINE://null undefined
				element = Colorable.DOUBLE_SYMBOL_DELIMITED_MULTILINE;
				break;
			case Lexer.INTEGER_LITERAL://数字
				element = Colorable.INTEGER_LITERAL;
				break;
			case Lexer.COMMENT://注释
				element = Colorable.COMMENT;
				break;
			case Lexer.SINGLE_SYMBOL_DELIMITED_A: //字符串
				element = Colorable.STRING;
				break;
			case Lexer.OPERATOR: //数学运算符
				element=Colorable.OPERATOR;
				break;
			case Lexer.HTML_VALUE: //数学运算符
				element=Colorable.HTML_VALUE;
				break;
			case Lexer.VARNAME: //数学运算符
				element=Colorable.VARNAME;
				break;
			case Lexer.FUNCTIONNAME: //数学运算符
				element=Colorable.FUNCTIONNAME;
				break;
			default:
				TextWarriorException.fail("Invalid token type");
				element = Colorable.FOREGROUND;
				break;
		}
		return getColor(element);
	}

	public void color() {

		_colors.put(Colorable.BREAK, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.CASE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.CATCH, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.CLASS, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.CONTINUE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.DEBUGGER, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.DEFAULT, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.DELETE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.DO, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.ELSE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.EXPORT, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.EXTEDNS, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.FINALLY, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.FOR, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.IF, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.IMPORT, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.IN, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.INSTANCEOF, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.NEW, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.RETURN, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.SUPER, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.SWITCH, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.THIS, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.THROW, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.TRY, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.TYPEOF, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.VOID, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.WHILE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.WITH, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.TRUE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.FALSE, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.YIELD, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.LIBS_INTHIS, _colors.get(Colorable.KEYWORD)); //关键字
		_colors.put(Colorable.FUNCTION, _colors.get(Colorable.KEYWORD)); //function
		_colors.put(Colorable.VAR_CONST_LET, _colors.get(Colorable.KEYWORD)); //VAR_CONST_LET

		_colors.put(Colorable.DOUBLE_SYMBOL_DELIMITED_MULTILINE, _colors.get(Colorable.KEYWORD)); //null undefined
	}
	/**
	 * Whether this color scheme uses a dark background, like black or dark grey.
	 */
	public abstract boolean isDark();


	private HashMap<Colorable, Integer> generateDefaultColors()
	{
		// High-contrast, black-on-white color scheme
		HashMap<Colorable, Integer> colors = new HashMap<Colorable, Integer>(Colorable.values().length);
		colors.put(Colorable.FOREGROUND, 0xff809c4f);//前景色
		colors.put(Colorable.BACKGROUND, 0x00000000);
		colors.put(Colorable.SELECTION_FOREGROUND, 0xffffffff);//选择文本的前景色
		colors.put(Colorable.SELECTION_BACKGROUND, 0xff404040);//选择文本的背景色
		colors.put(Colorable.CARET_FOREGROUND, 0x000000);
		colors.put(Colorable.CARET_BACKGROUND, 0x0000ff);
		colors.put(Colorable.CARET_DISABLED, 0xff545454);//光标
		colors.put(Colorable.LINE_HIGHLIGHT, 0x20888888);
		colors.put(Colorable.NON_PRINTING_GLYPH, 0xff515151);//行号

		colors.put(Colorable.VARNAME, 0xff809c4f);
		colors.put(Colorable.FUNCTIONNAME, 0xff1E90FF);
//		colors.put(Colorable.FUNCTIONNAME, 0xff809c4f);


		colors.put(Colorable.COMMENT, 0xFF008000); //注释
//		colors.put(Colorable.COMMENT, 0xff8d5735); //注释
		colors.put(Colorable.IMPORT, 0xffF08080);
//		colors.put(Colorable.IMPORT, 0xff8d5735);
		colors.put(Colorable.KEYWORD, 0xffb2403f); //关键字
		colors.put(Colorable.LR, 0xff000000);//括号
		colors.put(Colorable.INTEGER_LITERAL, 0xffae7ca2); //数字
		colors.put(Colorable.STRING, 0xff0000FF); //字符串
//		colors.put(Colorable.STRING, 0xffcd9e56); //字符串
		colors.put(Colorable.OPERATOR, 0xffff0000);//宏定义
		colors.put(Colorable.HTML_VALUE, 0xff6FA8BB);//宏定义

		colors.put(Colorable.BREAK, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.CASE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.CATCH, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.DOUBLE_SYMBOL_DELIMITED_MULTILINE, colors.get(Colorable.KEYWORD)); //null undefined
		colors.put(Colorable.CLASS, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.CONTINUE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.DEBUGGER, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.DEFAULT, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.DELETE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.DO, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.ELSE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.EXPORT, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.EXTEDNS, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.FINALLY, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.FOR, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.IF, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.IMPORT, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.IN, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.INSTANCEOF, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.NEW, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.RETURN, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.SUPER, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.SWITCH, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.THIS, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.THROW, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.TRY, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.TYPEOF, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.VOID, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.WHILE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.WITH, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.TRUE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.FALSE, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.YIELD, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.LIBS_INTHIS, colors.get(Colorable.KEYWORD)); //关键字
		colors.put(Colorable.FUNCTION, colors.get(Colorable.KEYWORD)); //function
		colors.put(Colorable.VAR_CONST_LET, colors.get(Colorable.KEYWORD)); //VAR_CONST_LET
		return colors;
	}
}
