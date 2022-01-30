package cn.qssq666.robot.constants;

/**
 * Created by qssq on 2018/9/16 qssq666@foxmail.com
 */
public interface ServiceExecCode {

    int QUERY_NICKNAME=1;
    int QUERY_CITY=2;
    int QUERY_ADDRESS=3;
    int QUERY_USER_INFO=4;
    int QUERY_GROUP_INFO=5;

    int CMD_KILL_QQ=6;
    int ADD_VOTE=7;
    int QUERY_LOGIN_INFO=8;
    int QUERY_CURRENT_LOGIN_QQ=9;
    int QUERY_CURRENT_LOGIN_NICKNAME=10;
    int QUERY_GROUP_NAME=11;
    int QUERY_GROUP_INFO_FIELD=12;
    int CMD_GAG_USER=13;
    int REVOKE = 15;
    int GAG = 16;

    /*


    public String getCurrentAccountUin() {
        return getAccount();
    }

    public String getCurrentNickname() {
        return ContactUtils.l(this, getCurrentAccountUin());
    }

    QQAppInterface.java
     */
}
