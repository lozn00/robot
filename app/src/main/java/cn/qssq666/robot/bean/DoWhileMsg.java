package cn.qssq666.robot.bean;

import android.net.Uri;
import android.util.Pair;

/**
 * Created by qssq on 2018/2/19 qssq666@foxmail.com
 */

public class DoWhileMsg {

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    Object object;

    public DoWhileMsg(Pair<Uri, Integer> pair) {
        this.pair = pair;
    }

    public DoWhileMsg() {
    }


    public DoWhileMsg setPairX(Pair<Integer, Uri> pair) {

        this.pair = new Pair<>(pair.second, pair.first);
        return this;
    }

    public Pair<Uri, Integer> getPair() {
        return pair;
    }

    public void setPair(Pair<Uri, Integer> pair) {
        this.pair = pair;
    }

    Pair<Uri, Integer> pair;

    public GroupWhiteNameBean getWhiteNameBean() {
        return whiteNameBean;
    }

    public DoWhileMsg setWhiteNameBean(GroupWhiteNameBean whiteNameBean) {
        this.whiteNameBean = whiteNameBean;
        return this;
    }

    GroupWhiteNameBean whiteNameBean;
}
