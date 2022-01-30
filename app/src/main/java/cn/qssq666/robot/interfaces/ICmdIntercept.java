package cn.qssq666.robot.interfaces;

/**
 * Created by qssq on 2018/2/21 qssq666@foxmail.com
 */

public interface ICmdIntercept<T> {
    boolean isNeedIntercept(T bean);
    void onComplete(String name);

}
