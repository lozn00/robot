package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.Table;
import cn.qssq666.robot.interfaces.TwoDataHolder;

/**
 * Created by qssq on 2018/7/27 qssq666@foxmail.com
 */
@Table("groupadmin")
//@TableIgnoreField(value = {"account", "disable"})//不创建的字段
public class GroupAdaminBean extends AccountBean implements TwoDataHolder {
    public String getName() {
        return name;
    }

    public GroupAdaminBean setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDisplayValue() {
        return getName();
    }


    public String name;

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String groups;
    public boolean all;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getShowTitle() {
        return getAccount();
    }

    @Override
    public String getShowContent() {
        return groups;
    }
}
