package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.Table;

/**
 * Created by qssq on 2018/2/25 qssq666@foxmail.com
 */

@Table("vr")
public class ViolationRecordBean {
    public int getCount() {
        return count;
    }

    public ViolationRecordBean setCount(int count) {
        this.count = count;
        return this;
    }

    public String getGroups() {
        return groups;
    }

    public ViolationRecordBean setGroups(String groups) {
        this.groups = groups;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public ViolationRecordBean setQq(String qq) {
        this.qq = qq;
        return this;
    }

    int count;
    String groups;
    String qq;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
