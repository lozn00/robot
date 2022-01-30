package cn.qssq666.robot.event;

import cn.qssq666.robot.bean.ReplyWordBean;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class WordEvent {
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

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    boolean all;

    public ReplyWordBean getBean() {
        return bean;
    }

    public void setBean(ReplyWordBean bean) {
        this.bean = bean;
    }

    ReplyWordBean bean;
}
