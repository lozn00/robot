package cn.qssq666.robot.bean;

import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;

/**
 * Created by qssq on 2017/12/24 qssq666@foxmail.com
 */

public class AtBean implements AtBeanModelI {

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    private String uin;
    String msg;
    String nickname;

    public String getMsg() {
        return msg;
    }

    public String getSenderuin() {
        return getUin();
    }

    public String getNickname() {
        return nickname;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public void setSenderuin(String senderuin) {
//        this.senderuin = senderuin;

        setUin(senderuin);
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }




}
