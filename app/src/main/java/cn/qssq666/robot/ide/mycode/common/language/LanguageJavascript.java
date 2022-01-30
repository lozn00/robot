package cn.qssq666.robot.ide.mycode.common.language;


import cn.qssq666.robot.ide.mycode.common.Language;

//ignore_start
public class LanguageJavascript extends Language {
    private static Language _theOne = (Language) null;
    private static final String[] keywords = new String[]{"abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "let",


            "debugger", "default", "delete", "do", "double", "else", "enum", "export", "extends", "false", "final", "finally", "float", "for", "function",


            "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while",
            "with"};

    /*

    onReceiveMsgIsNeedIntercept|onReceiveOtherIntercept|onReceiveRobotFinalCallMsgIsNeedIntercept" +
            "|onDestory|onCreate|getAuthorName|getPackageName|getPluginName|getVersionName|" +
            "getVersionCode|getDescript|getNickname|getSenderuin|getMessage|getSelfuin|getFrienduin|getIstroop|getType|setMessage|msgitem
     */
    private final static char[] OPERATORS = {
            '(', ')', '{', '}', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', '[', ']', '<', '>',
            '?', '~', '%', '^'
    };

    private final static String package_robot_api = "addQQGroupManager|addScript|addVar|clearPost|deleteScript|" +
            "deleteVar|dp2px|executeAction|executeBySql|executeBySql|executeScript|executeShell|executeSql|fetchVar|" +
            "getArgsFrom|getBaseDir|getContext|getHandler|getSharedPreferences|isGroupMsg|isPicMsg|" +
            "isPrivateMsg|isRunAtMainThread|modifyGroupMemberCard|modifyGroupName" +
            "|modifyGroupNameInfo|modifyQQInfo|modifyVar|parseGagTime|parseVar|post|queryDataBySql" +
            "|queryGroupMemberList|queryGroupsList|queryQQ|queryQQGroup" +
            "|queryQQMemberList|readBooleanConfig" +
            "|readFloatConfig|readIntConfig|readStringConfig|readStringSetConfig|removeQQGroupManager|" +
            "revokeMsg|sendAnsyncDownload|sendAnsyncDownload|sendAsyncGetRequest|" +
            "sendAsyncPostJSONRequest|sendAsyncPostRequest|sendAsyncPostRequest|" +
            "sendAtMsg|sendDiscussisonMsg|sendDiscussisonPrivateMsg|sendGagMsg|sendGroupMsg|sendGroupPrivateMsg|sendGroupVoiceMsg" +
            "|sendKickMsg|sendMsg|sendMsgCardMsg|sendPicAndTextMsg|sendPicMsg|" +
            "sendPrivateMsg|sendPrivateVoiceMsg|sendQQMsg|sendRequestExitDiscussionMsg|sendRequestExitGroupMsg|sendSyncDownRequest" +
            "|sendSyncRequest|showDebugToast|sp2px|text2Pic|writConfig|zanPerson";
    private final static String package_robot_config = "checkSensitiveWord|checkSensitiveWordAndUseSystemGag|" +
            "currentGroupRobotIsShouldReply|generalQuery|generalQueryItem|getRobotVersion|getRobotVersionName|hasAite|hasAiteMe|" +
            "isAtGroupWhiteNames|isCompatibility|isEnableGroupMsg|isEnableGroupWhilteName|isEnablePrivateReply|isManager|" +
            "isNeedAiteReply|isReplyAiteUser|queryGroupConfig";

    private final static String logw = "info|i|w|wran|d|debug|v|e|error";
    /*
        public final int VERSION_CODE = BuildConfig.VERSION_CODE;
public final String VERSION_NAME = BuildConfig.VERSION_NAME;
public final String BUILD_TIME_STR = BuildConfig.BUILD_TIME_STR;
public final String APPLICATION_ID = BuildConfig.APPLICATION_ID;

     */
    private final static String robot = "VERSION_CODE|VERSION_NAME|BUILD_TIME_STR|APPLICATION_ID";
    private final static String package_package = "config|api|log|robot";
    private final static String[] names = package_package.split("\\|");

    LanguageJavascript() {


        super.setKeywords(keywords);
        super.setOperators(OPERATORS);
        super.setNames(names);
        super.addBasePackage("log", logw.split("\\|"));
        super.addBasePackage("robot", robot.split("\\|"));
        super.addBasePackage("api", package_robot_api.split("\\|"));
        super.addBasePackage("config", package_robot_config.split("\\|"));
    }

    public static Language getInstance() {
        if (_theOne == null) {
            _theOne = new LanguageJavascript();
        }

        return _theOne;
    }
  /**
     * Whether the word after c is a token
     */
    public boolean isWordStart2(char c) {
        return (c == '.');
    }

    public boolean isLineAStart(char c) {
        return false;
    }

    /**
     * Whether c0c1L is a token, where L is a sequence of characters until the end of the line
     */
    public boolean isLineStart(char c0, char c1) {
        return (c0 == '/' && c1 == '/');
    }

    /**
     * Whether c0c1 signifies the start of a multi-line token
     */
    public boolean isMultilineStartDelimiter(char c0, char c1) {
        return (c0 == '/' && c1 == '*');
    }

    /**
     * Whether c0c1 signifies the end of a multi-line token
     */
    public boolean isMultilineEndDelimiter(char c0, char c1) {
        return (c0 == '*' && c1 == '/');
    }
//ignore_end
    //ignore_end
}
