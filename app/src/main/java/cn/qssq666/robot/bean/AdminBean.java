package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import cn.qssq666.db.anotation.Table;
import cn.qssq666.robot.interfaces.TwoDataHolder;

/**
 * Created by qssq on 2017/12/23 qssq666@foxmail.com
 */

@Table("admin")
public class AdminBean extends AccountBean implements TwoDataHolder {
    public int getLevel() {
        return level;
    }

    public AdminBean setLevel(int level) {
        this.level = level;
        return this;
    }

    private int level;

    @Override
    public AdminBean setAccount(String account) {
        super.setAccount(account);
        return this;
    }

    @Override
    public String getShowTitle() {
        return getAccount();
    }

    @Override
    public String getShowContent() {
        return "等级:" + level + "";
    }
}
