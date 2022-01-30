package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.Table;

/**
 * Created by qssq on 2017/12/6 qssq666@foxmail.com
 */

@Table("floor")
public class FloorBean extends AccountBean{
    public FloorBean(String account, String data) {
        super(account);
        this.data = data;
    }

    public FloorBean(String data) {
        this.data = data;
    }

    public FloorBean() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    String data;
}
