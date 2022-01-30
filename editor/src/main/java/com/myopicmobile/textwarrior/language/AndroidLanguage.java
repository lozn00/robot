package com.myopicmobile.textwarrior.language;

import com.myopicmobile.textwarrior.common.Language;

/**
 * Created by Administrator on 2017/12/31.
 */

public class AndroidLanguage extends Language{
    private static Language _theOne = null;

    private final static String[] keywords = {
            "break","case","catch","class","const",
            "continue","debugger","default","delete","do",
            "else","export","extends","finally","for",
            "function","if","import","in","instanceof",
            "new","return","final","in","float",
            "for","super","switch","this",
            "throw","try","typeof","var","void",
            "while","let","with","yield","activity",
            "true","false","null","undefined"
    };
    private final static String ACTIVITY = "setOnErrListener|addOnErrListener|removeOnErrListener|removeAllOnErrListener|sendErr|sendWoring|" +
            "setContentView|getView|NewOne|Msuper";
    private final static char[] BASIC_OPERATORS = {
            '\t','\"','\'','(', ')', '{', '}', '[', ']', '<', '>',
            '.', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', ':',
            '?', '~', '%', '^'
    };

    public static String[] funtions = {"setContentView","setSupportActionBar","getSupportActionBar","findViewById","importClass","importPackage"};

    public   final  static  String[] name={"onCreate","onResume","onStart","onPause","onStop","onPostCreate","onConfigurationChanged","onPostResume",
            "onDestroy","onTitleChanged","onSupportActionModeStarted","onSupportActionModeFinished","onWindowStartingSupportActionMode",
            "onMenuOpened","onPanelClosed","onSaveInstanceState","onKeyDown","openOptionsMenu","closeOptionsMenu","onBackPressed","onMultiWindowModeChanged",
            "onPictureInPictureModeChanged","onCreatePanelMenu","onLowMemory","onKeyLongPress","onKeyUp","onKeyMultiple","onKeyShortcut","onTouchEvent","onWindowFocusChanged","onCreateOptionsMenu",
            "onPrepareOptionsMenu","onOptionsItemSelected","onOptionsMenuClosed","setContentView","setSupportActionBar","getSupportActionBar","findViewById","importClass","importPackage"};
    public static Language getInstance(){
        if(_theOne == null){
            _theOne = new AndroidLanguage();
        }
        return _theOne;
    }

    private AndroidLanguage(){
        setKeywords(keywords);
        setOperatorlist(BASIC_OPERATORS);
        setNames(new String[]{});//
        addBasePackage("activity",ACTIVITY.split("\\|"));
    }

    public boolean isLineAStart(char c){
        return false;
    }
}
