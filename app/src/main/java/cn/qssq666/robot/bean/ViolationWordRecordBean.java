package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.Table;

/**
 * Created by qssq on 2018/2/25 qssq666@foxmail.com
 */

@Table("vwr")
public class ViolationWordRecordBean {

    public String getGroups() {
        return groups;
    }

    public ViolationWordRecordBean setGroups(String groups) {
        this.groups = groups;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public ViolationWordRecordBean setQq(String qq) {
        this.qq = qq;
        return this;
    }

    String groups;
    String qq;

    public long getTime() {
        return time;
    }

    public ViolationWordRecordBean setTime(long time) {
        this.time = time;
        return this;
    }

    long time;

    public String getWord() {
        return word;
    }

    public ViolationWordRecordBean setWord(String word) {

        this.word = word;
        return this;
    }

    String word;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
