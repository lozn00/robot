package cn.qssq666.robot.event;

import cn.qssq666.robot.bean.GroupWhiteNameBean;

/**
 * Created by qssq on 2017/12/20 qssq666@foxmail.com
 */

public class GroupDetailEvent {
    public GroupWhiteNameBean getBean() {
        return bean;
    }

    public void setBean(GroupWhiteNameBean bean) {
        this.bean = bean;
    }

    GroupWhiteNameBean bean;
}
