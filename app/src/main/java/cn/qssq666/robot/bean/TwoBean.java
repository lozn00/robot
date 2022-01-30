package cn.qssq666.robot.bean;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import cn.qssq666.db.anotation.ID;

/**
 * Created by qssq on 2017/12/1 qssq666@foxmail.com
 */

public class TwoBean extends BaseObservable {


    @Bindable
    public int getId() {
        return id;
    }

    public TwoBean setId(int id) {
        this.id = id;
        return this;
    }


    @ID
    int id;

    public boolean isDisable() {
        return disable;
    }

    public TwoBean setDisable(boolean disable) {
        this.disable = disable;
        return this;
    }

    boolean disable;

    public String getAccount() {
        return account;
    }

    public TwoBean setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getQqgroup() {
        return qqgroup;
    }

    public TwoBean setQqgroup(String qqgroup) {
        this.qqgroup = qqgroup;
        return this;
    }

    private String account;
    /**
     * group是关键字 会报错
     */
    private String qqgroup;
}
