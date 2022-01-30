package cn.qssq666.robot.event;

import cn.qssq666.robot.bean.AccountBean;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class AccountAddOrChangeEvent {
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    boolean edit;


    public AccountBean getBean() {
        return bean;
    }

    public void setBean(AccountBean bean) {
        this.bean = bean;
    }

    AccountBean bean;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

}
