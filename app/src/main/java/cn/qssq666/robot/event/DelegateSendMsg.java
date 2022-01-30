package cn.qssq666.robot.event;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class DelegateSendMsg {
    private DelegateSendMsg() {


    }

    public static DelegateSendMsg createDelegateMsg(int type, IMsgModel item) {
        return createDelegateMsg(type, null, item);
    }

    public static DelegateSendMsg createDelegateMsg(int type, Object object, IMsgModel item) {
        DelegateSendMsg delegateSendMsg = new DelegateSendMsg();
        delegateSendMsg.setObject(object);
        delegateSendMsg.setMsgItem(item);
        delegateSendMsg.setType(type);
        return delegateSendMsg;
    }

    public IMsgModel getMsgItem() {
        return msgItem;
    }

    private void setMsgItem(IMsgModel msgItem) {
        this.msgItem = msgItem;
    }

    IMsgModel msgItem;

    public int getType() {
        return type;
    }

    private void setType(int type) {
        this.type = type;
    }

    private int type;


    public Object getObject() {
        return object;
    }

    public DelegateSendMsg setObject(Object object) {
        this.object = object;
        return this;
    }

    Object object;

    @Override
    public String toString() {
        return "DelegateSendMsg{" +
                "msgItem=" + msgItem +
                ", type=" + type +
                ", object=" + object +
                '}';
    }
}
