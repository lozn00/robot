package cn.qssq666.robot.event;

import cn.qssq666.robot.bean.RedPacketBean;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class RedPacketEvent {
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

    public RedPacketBean getBean() {
        return bean;
    }

    public void setBean(RedPacketBean bean) {
        this.bean = bean;
    }

    RedPacketBean bean;
}
