package cn.qssq666.robot.business;

import androidx.core.util.Pair;

import java.util.List;

import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public abstract class BaseQueryImpl {

    public static <T extends BaseQueryImpl> T getInstance(Class<T> myclasss) {
        try {
            T t = myclasss.newInstance();
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  abstract  void doAction(MsgItem item, boolean isgroupMsg, GroupWhiteNameBean nameBean, String[] args, Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> atPair, String text);
}
