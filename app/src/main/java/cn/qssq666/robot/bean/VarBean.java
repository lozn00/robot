package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.Table;
import cn.qssq666.db.anotation.TableIgnoreField;
import cn.qssq666.robot.interfaces.TwoDataHolder;

/**
 * Created by qssq on 2018/7/27 qssq666@foxmail.com
 */
@Table("var")
@TableIgnoreField(value = {"account", "disable"})
public class VarBean extends AccountBean implements TwoDataHolder {
    public String getName() {
        return name;
    }

    public VarBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public VarBean setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getDisplayValue() {
        return getName();
    }

    public int getType() {
        return type;
    }

    public VarBean setType(int type) {
        this.type = type;
        return this;
    }

    public String name;
    public String value;
    public int type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getShowTitle() {
        return getName();
    }

    @Override
    public String getShowContent() {
        return getValue();
    }
}
