package cn.qssq666.robot.bean;

import java.util.Objects;

import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.utils.DateUtils;

/**
 * Created by luozheng on 2017/3/5.  qssq.space
 */

public class MsgItem implements Cloneable, IMsgModel {

    private long messageID;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String nickname;

    public int getIstroop() {
        return istroop;
    }

    public MsgItem setIstroop(int istroop) {
        this.istroop = istroop;
        return this;
    }

    public String getSenderuin() {
        return senderuin;
    }

    public MsgItem setSenderuin(String senderuin) {
        this.senderuin = senderuin;
        return this;
    }

    public String getFrienduin() {
        return frienduin;
    }

    public MsgItem setFrienduin(String frienduin) {
        this.frienduin = frienduin;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MsgItem setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getType() {
        return type;
    }

    @Override
    public long getMessageID() {
        return this.messageID;
    }

    public MsgItem setType(int type) {
        this.type = type;
        return this;
    }

    int istroop;
    String senderuin;

    public int getDirection() {
        return direction;
    }

    public MsgItem setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    /**
     * 1 为回复，0 为发送
     */
    int direction;
    String frienduin;
    String message;

    public String getExtrajson() {
        return extrajson;
    }

    public void setExtrajson(String extrajson) {
        this.extrajson = extrajson;
    }

    String extrajson;


    public String getExtstr() {
        return extstr;
    }

    public void setExtstr(String extstr) {
        this.extstr = extstr;
    }

    String extstr;

    public String getSelfuin() {
        return selfuin == null ? "" : selfuin;
    }

    public void setSelfuin(String selfuin) {
        this.selfuin = selfuin;
    }

    String selfuin;
    int type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    String version;

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    private String apptype;

    public long getTime() {
        return time;
    }

    public MsgItem setTime(long time) {
        this.time = time;
        return this;
    }

    long time;

    public int getCode() {
        return code;
    }

    public MsgItem setCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public void setUniversalAction(int type, Object obj) {

    }

    @Override
    public Object getUniversalAction(int type) {
        return null;
    }


    /**
     * 如果code=0才处理 否则 忽略
     */
    int code;

    @Override
    public String toString() {
        return "{" +
                "昵称=" + nickname +
                ", 是否群消息:" + istroop +
                ", 发送人:" + senderuin  +
                ", 朋友:" + frienduin +
                ", 消息体:" + message  +
                ", 自己账号:" + selfuin + '\'' +
                ", 消息类型:" + type +
                ", 来源类型：" + apptype +
                ",时间：" + DateUtils.getTime(time) +
                ",时间数值：" + time+
                ",方向：" + direction+
                ",处理指令：" + code +
                '}';

    }

    public String toSimpleString() {
        return "[" + nickname + "(" + senderuin + "):" + message + "]";
    }

    @Override
    public MsgItem clone() {
        MsgItem obj = null;
        try {
            obj = (MsgItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgItem msgItem = (MsgItem) o;
        return messageID == msgItem.messageID && istroop == msgItem.istroop && direction == msgItem.direction && type == msgItem.type && time == msgItem.time && code == msgItem.code && Objects.equals(nickname, msgItem.nickname) && Objects.equals(senderuin, msgItem.senderuin) && Objects.equals(frienduin, msgItem.frienduin) && Objects.equals(message, msgItem.message) && Objects.equals(extrajson, msgItem.extrajson) && Objects.equals(extstr, msgItem.extstr) && Objects.equals(selfuin, msgItem.selfuin) && Objects.equals(version, msgItem.version) && Objects.equals(apptype, msgItem.apptype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageID, nickname, istroop, senderuin, direction, frienduin, message, extrajson, extstr, selfuin, type, version, apptype, time, code);
    }

    public void setMessagID(long id) {
        this.messageID = id;
    }
}
