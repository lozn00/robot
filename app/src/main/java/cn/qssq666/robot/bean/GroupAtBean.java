package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.interfaces.IAccountBean;

/**
 * Created by qssq on 2017/12/24 qssq666@foxmail.com
 */

public class GroupAtBean extends AtBean implements IAccountBean, AtBeanModelI {
    //    troop_at_info_list
    public int getTextLen() {
        return textLen;
    }

    public void setTextLen(int textLen) {
        this.textLen = textLen;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }





    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    int textLen;//\EncryptUtilN.a7(new int[]{1967,2199,2163,2163,2143,2335})startPos\EncryptUtilN.a7(new int[]{1732,1964,1924,1908,2100})uin\EncryptUtilN.a7(new int[]{785,1017,985,977,985,1013,985,1005,981,985,1009,961,1153})flag\":0}
    int startPos = 0;


    /**
     * AtBeanModelI
     */

    /**
     * 昵称用在清除艾特得到真shi数据附加的
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    short flag = 0;

    @Override
    public String getAccount() {
        return getSenderuin();
    }


    @Override
    public String toString() {
        return "GroupAtBean{" +
                "textLen=" + textLen +
                ", startPos=" + startPos +
                ", uin='" + getUin() + '\'' +
                ", flag=" + flag +
                '}';
    }


}
