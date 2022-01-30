/*
 * Copyright (c) 2011 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;

/**
 * from javascript project
 * Singleton class containing the symbols and operators of the Javascript language
 */
public class LanguageJavascript extends Language {
//    private static Language _theOne = null;

    private final static String[] keywords = {
            "break", "case", "catch", "class", "const",
            "continue", "debugger", "default", "delete", "do",
            "else", "export", "extends", "finally", "for",
            "function", "if", "import", "in", "instanceof",
            "new", "return", "final", "in", "float",
            "for", "super", "switch", "this",
            "throw", "try", "typeof", "var", "void",
            "while", "let", "with", "yield", "libs_inthis", "true", "false", "null", "undefined",
            "config", "api", "log", "robot", "toast"

            ,

            "addQQGroupManager", "addScript", "addVar", "clearPost", "deleteScript",
            "deleteVar", "dp2px", "executeAction", "executeBySql", "executeBySql", "executeScript", "executeShell", "executeSql", "fetchVar",
            "getArgsFrom", "getBaseDir", "getContext", "getHandler", "getSharedPreferences", "isGroupMsg", "isPicMsg",
            "isPrivateMsg", "isRunAtMainThread", "modifyGroupMemberCard", "modifyGroupName"
            , "modifyGroupNameInfo", "modifyQQInfo", "modifyVar", "parseGagTime", "parseVar", "post", "queryDataBySql"
            , "queryGroupMemberList", "queryGroupsList", "queryQQ", "queryQQGroup"
            , "queryQQMemberList", "readBooleanConfig", "readFloatConfig", "readIntConfig", "readStringConfig", "readStringSetConfig", "removeQQGroupManager",
            "revokeMsg", "sendAnsyncDownload", "sendAnsyncDownload", "sendAsyncGetRequest",
            "sendAsyncPostJSONRequest", "sendAsyncPostRequest", "sendAsyncPostRequest",
            "sendAtMsg", "sendDiscussisonMsg", "sendDiscussisonPrivateMsg", "sendGagMsg", "sendGroupMsg", "sendGroupPrivateMsg", "sendGroupVoiceMsg"
            , "sendKickMsg", "sendMsg", "sendMsgCardMsg", "sendPicAndTextMsg", "sendPicMsg"
            , "sendPrivateMsg", "sendPrivateVoiceMsg", "sendQQMsg", "sendRequestExitDiscussionMsg", "sendRequestExitGroupMsg", "sendSyncDownRequest"
            , "sendSyncRequest", "showDebugToast", "sp2px", "text2Pic", "writConfig", "zanPerson",


            "checkSensitiveWord", "checkSensitiveWordAndUseSystemGag",
            "currentGroupRobotIsShouldReply", "generalQuery", "generalQueryItem", "getRobotVersion", "getRobotVersionName", "hasAite", "hasAiteMe",
            "isAtGroupWhiteNames", "isCompatibility", "isEnableGroupMsg", "isEnableGroupWhilteName", "isEnablePrivateReply", "isManager",
            "isNeedAiteReply", "isReplyAiteUser", "queryGroupConfig",


    };


    private final static String package_robot_api[] = {

            "addQQGroupManager", "addScript", "addVar", "clearPost", "deleteScript",
            "deleteVar", "dp2px", "executeAction", "executeBySql", "executeBySql", "executeScript", "executeShell", "executeSql", "fetchVar",
            "getArgsFrom", "getBaseDir", "getContext", "getHandler", "getSharedPreferences", "isGroupMsg", "isPicMsg",
            "isPrivateMsg", "isRunAtMainThread", "modifyGroupMemberCard", "modifyGroupName"
            , "modifyGroupNameInfo", "modifyQQInfo", "modifyVar", "parseGagTime", "parseVar", "post", "queryDataBySql"
            , "queryGroupMemberList", "queryGroupsList", "queryQQ", "queryQQGroup"
            , "queryQQMemberList", "readBooleanConfig", "readFloatConfig", "readIntConfig", "readStringConfig", "readStringSetConfig", "removeQQGroupManager",
            "revokeMsg", "sendAnsyncDownload", "sendAnsyncDownload", "sendAsyncGetRequest",
            "sendAsyncPostJSONRequest", "sendAsyncPostRequest", "sendAsyncPostRequest",
            "sendAtMsg", "sendDiscussisonMsg", "sendDiscussisonPrivateMsg", "sendGagMsg", "sendGroupMsg", "sendGroupPrivateMsg", "sendGroupVoiceMsg" +
            "sendKickMsg", "sendMsg", "sendMsgCardMsg", "sendPicAndTextMsg", "sendPicMsg",
            "sendPrivateMsg", "sendPrivateVoiceMsg", "sendQQMsg", "sendRequestExitDiscussionMsg", "sendRequestExitGroupMsg", "sendSyncDownRequest" +
            "sendSyncRequest", "showDebugToast", "sp2px", "text2Pic", "writConfig", "zanPerson",


    };


    private final static String package_robot_config[] = {"checkSensitiveWord", "checkSensitiveWordAndUseSystemGag",
            "currentGroupRobotIsShouldReply", "generalQuery", "generalQueryItem", "getRobotVersion", "getRobotVersionName", "hasAite", "hasAiteMe",
            "isAtGroupWhiteNames", "isCompatibility", "isEnableGroupMsg", "isEnableGroupWhilteName", "isEnablePrivateReply", "isManager",
            "isNeedAiteReply", "isReplyAiteUser", "queryGroupConfig"};

    private final static String logw = "info|wran|debug|error";
    /*
        public final int VERSION_CODE = BuildConfig.VERSION_CODE;
public final String VERSION_NAME = BuildConfig.VERSION_NAME;
public final String BUILD_TIME_STR = BuildConfig.BUILD_TIME_STR;
public final String APPLICATION_ID = BuildConfig.APPLICATION_ID;

     */
    private final static String robot = "VERSION_CODE|VERSION_NAME|BUILD_TIME_STR|APPLICATION_ID";
    private final static String package_package = "config|api|log|robot";


    private final static String INTHIS = "startActivity|sendMessage|startAS|useEZ|runOnUiThread|addCallBack|" +
            "removeCallBack|clearCallBack|addOnChange|clearOnChange|removeOnChange";
    private final static char[] BASIC_OPERATORS = {
            '\t', '\"', '\'', '(', ')', '{', '}', '[', ']', '<', '>',
            '.', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', ':',
            '?', '~', '%', '^'
    };
    private final static String[] funtions = {};

    public static Language getInstance() {
//        if (_theOne == null) {
        return new LanguageJavascript();
//        }
//        return _theOne;
    }

    private LanguageJavascript() {
//        String[] split = package_robot_api.split("\\|");
      /*  String[] keyboardNew = new String[keywords.length + split.length];

        for (int i = 0; i < keywords.length; i++) {
            keyboardNew[i] = keyboardNew[i];
            ;
        }
        for (int i = keywords.length; i < keyboardNew.length; i++) {
            keyboardNew[i] = keyboardNew[i - keywords.length];
            ;
        }*/
        setKeywords(keywords);
        setOperatorlist(BASIC_OPERATORS);
        setNames(funtions);//
        addBasePackage("config", package_robot_config);
        addBasePackage("robot", robot.split("\\|"));
        addBasePackage("log", logw.split("\\|"));
        addBasePackage("api", package_robot_api);
//        addBasePackage("api", package_robot_api.split("\\|"));
        addBasePackage("libs_inthis", INTHIS.split("\\|"));
    }

    public boolean isLineAStart(char c) {
        return false;
    }
}
