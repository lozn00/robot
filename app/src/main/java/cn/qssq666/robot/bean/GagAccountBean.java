package cn.qssq666.robot.bean;

import cn.qssq666.db.anotation.DBIgnore;
import cn.qssq666.robot.enums.GAGTYPE;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.MiscUtil;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public class GagAccountBean extends AccountBean implements Cloneable {
    public GagAccountBean() {
    }

    public GagAccountBean(String account,long duration, boolean silence, int action) {
        super(account);
        this.duration = duration;
        this.silence = silence;
        this.action = action;
    }

    public GagAccountBean(String account, long duration) {
        super(account);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;//4800
    }

    public GagAccountBean setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public GagAccountBean setAccount(String account) {
        super.setAccount(account);
        return this;
    }

    public long duration = 60;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getReason() {
        if(reason==null){
            return "";
        }
        return reason;
    }

    @Override
    public String toString() {
        return "" +
                "禁言时间=" + DateUtils.getGagTime(duration) +
                ", 原因='" + reason + '\'' +
                ", flag=" + flag +
                ",禁言是否发消息告知=" + (silence?"不":"是") +
                ", action=" + MiscUtil.getGagAction(action) +
                '}';
    }

    public GagAccountBean setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String reason="";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @DBIgnore
    public String target="";
    public int flag;

    public boolean isSilence() {
        return silence;
    }

    public GagAccountBean setSilence(boolean silence) {
        this.silence = silence;
        return this;
    }

    public boolean silence;

    public int getAction() {
        return action;
    }

    public GagAccountBean setAction(int action) {

        this.action = action;
        return this;
    }

    /**
     * 0是禁言 1 是踢人
     */
    public int action;

    public boolean isKick() {
        return action == GAGTYPE.KICK ||action==GAGTYPE.KICK_FORVER;
    }
    public boolean isGag(){
        return action==0;
    }

    @Override
    public GagAccountBean clone()  {
        try {
            return (GagAccountBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
