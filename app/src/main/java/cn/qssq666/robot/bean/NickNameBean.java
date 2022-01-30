package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import com.alibaba.fastjson.JSON;

import cn.qssq666.db.anotation.Table;
import cn.qssq666.robot.interfaces.ThreeDataHolder;

/**
 * Created by qssq on 2017/12/23 qssq666@foxmail.com
 */

@Table("nickname")
public class NickNameBean extends AccountBean implements ThreeDataHolder {

    public String getNickname() {
        return nickname;
    }

    public NickNameBean setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    private String nickname;

    public String getTroopno() {
        return troopno;
    }

    public NickNameBean setTroopno(String troopno) {
        this.troopno = troopno;
        return this;
    }

    private String troopno;

    public int getIstroop() {
        return istroop;
    }

    public void setIstroop(int istroop) {
        this.istroop = istroop;
    }

    private int istroop;

    @Override
    public String getShowTitle() {
        return nickname;
    }

    @Override
    public String getShowContent() {


        return "QQ:" + getAccount();
    }

    @Override
    public String getShowContent1() {
        if (getTroopno() == null || getTroopno().equals(getAccount())) {
            return "";
        } else {
            return "群号:" + getTroopno();
        }
    }

    @Override
    public NickNameBean setAccount(String account) {
        super.setAccount(account);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NickNameBean that = (NickNameBean) o;

        if (troopno != null ? !troopno.equals(that.troopno) : that.troopno != null) return false;
        return getAccount().equals(that.getAccount());
    }

    @Override
    public int hashCode() {
        int result = troopno != null ? troopno.hashCode() : 0;
        result = 31 * result + getAccount().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
