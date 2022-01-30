package cn.qssq666.robot.event;

import java.util.List;

import cn.qssq666.robot.bean.AccountBean;

/**
 * Created by qssq on 2017/11/29 qssq666@foxmail.com
 */

public class OnUpdateAccountListEvent {
    public List< ? extends AccountBean> getList() {
        return list;
    }

    public void setList(List<AccountBean> list) {
        this.list = list;
    }

    List<? extends AccountBean> list;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int type;


}
