package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import cn.qssq666.db.anotation.ColumnType;
import cn.qssq666.db.anotation.ID;
import cn.qssq666.db.anotation.Table;

/**
 * Created by qssq on 2017/9/23 qssq666@foxmail.com
 */

@Table("luckmoney")//// : 2017/9/23 ignore_include
public class RedPacketBean {
    public static final int RESULT_NOT_DRAW_SUCC = 404;
    public static final int NOT_ENABLE_REDPACKET = 403;
    public static final int DRAW_SUCC = 200;

    public int getId() {
        return id;
    }

    public RedPacketBean setId(int id) {
        this.id = id;
        return this;
    }

    @ID
    int id;

    public String getMoney() {
        return money;
    }

    public RedPacketBean setMoney(String money) {
        this.money = money;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RedPacketBean setMessage(String message) {
        this.message = message;
        return this;
    }


    public RedPacketBean setResult(int result) {
        this.result = result;
        return this;
    }

    public int getIstroop() {
        return istroop;
    }

    public RedPacketBean setIstroop(int istroop) {
        this.istroop = istroop;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public RedPacketBean setQq(String qq) {
        this.qq = qq;
        return this;
    }

    public String getQqgroup() {
        return qqgroup;
    }

    public RedPacketBean setQqgroup(String qqgroup) {
        this.qqgroup = qqgroup;
        return this;
    }

    @JSONField(serialize = false)
    public String getResultMsg() {
        switch (result) {
            case 200:
                return "成功";
            case 403:
                return "未开启";
            case 404:
                return "没抢到";
            case -1:
            case 0:
                return "需要升级内置";
            case 1:
                return "已被领取";
            case 8:
                return "别人专属";
            case 6:
                return "自己私包";
            default:
                return "未知错误(" + result + ",type:"+type+")";
        }
    }

    private String qq = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private  int type;
    private String qqgroup = "";
    @ColumnType("REAL")// ignore_include
    private String money = "";
    private String message = "";

    public String getNickname() {
        return nickname;
    }

    public RedPacketBean setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getGroupnickname() {
        return groupnickname;
    }

    public RedPacketBean setGroupnickname(String groupnickname) {
        this.groupnickname = groupnickname;
        return this;
    }

    private String nickname = "";
    private String groupnickname = "";

    public int getResult() {
        return result;
    }

    private int result = -1;

    public long getCreatedAt() {
        return createdAt;
    }

    public RedPacketBean setCreatedAt(long createdAt) {

        this.createdAt = createdAt;
        return this;
    }

    private long createdAt = new Date().getTime();
    private int istroop;
    //, type:-2025,istroop:1,message:0.01没抢到(174ms)
}
