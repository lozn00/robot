package cn.qssq666.robot.utils;


import cn.qssq666.robot.enums.GAGTYPE;

public class MiscUtil {
    public static String getGagAction(int action) {
        switch (action) {
            case GAGTYPE.GAG:
                return "是否踢出:只禁言";
            case GAGTYPE.KICK:
                return "是否踢出:踢出";
            case GAGTYPE.KICK_FORVER:
                return "是否踢出:踢出(永久)";
            default:
                return "未知[" + action + "]";
        }
    }
}
