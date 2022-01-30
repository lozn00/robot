package cn.qssq666.robot.event;

/**
 * Created by qssq on 2018/12/2 qssq666@foxmail.com
 */
public class RefreshEvent {
    public boolean isUpdateUi() {
        return updateUi;
    }

    public RefreshEvent setUpdateUi(boolean updateUi) {
        this.updateUi = updateUi;
        return this;
    }

    boolean updateUi;
}
