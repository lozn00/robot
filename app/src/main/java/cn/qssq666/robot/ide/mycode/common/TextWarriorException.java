/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package cn.qssq666.robot.ide.mycode.common;
import cn.qssq666.CoreLibrary0;import android.util.Log;
//https://github.com/luoyesiqiu/simpleC/tree/master/app/src/main/java/com/myopicmobile/textwarrior/common
//https://github.com/LingSaTuo/CreateJS
//https://github.com/brnogz/TextWarriorLibrary
//https://github.com/qtiuto/lua-for-android english
//https://github.com/XsJIONG/VIDE
//https://github.com/fengdeyingzi/CodeEditor
//https://github.com/nirenr/AndroLua_pro
public class TextWarriorException extends Exception {
	private static final boolean NDEBUG = false; // set to true to suppress assertions
	private static final long serialVersionUID = -8393914265675038931L;

	public TextWarriorException(String msg){
		super(msg);
	}

	static public void fail(final String details){
		assertVerbose(false, details);
	}

	@SuppressWarnings("all") //suppress dead code warning when NDEBUG == true
	static public void assertVerbose(boolean condition, final String details){
		if(NDEBUG){
			return;
		}

		if (!condition){
			/* BlackBerry dialog way of displaying errors
		        UiApplication.getUiApplication().invokeLater(new Runnable()
		        {
		            public void run()
		            {
		                Dialog.alert(details);
		            }
		        });
		    */

			/* For Android, a Context has to be passed into this method
			 * to display the error message on the device screen */

			System.err.print("TextWarrior assertion failed: ");
			System.err.println(details);
			Log.d("lua",details);
		}
	}
}
