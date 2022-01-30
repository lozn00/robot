package cn.qssq666.robot.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;

import cn.qssq666.db.anotation.ID;
import cn.qssq666.robot.BR;
import cn.qssq666.robot.interfaces.IAccountBean;
import cn.qssq666.robot.interfaces.IDisable;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class AccountBean extends BaseObservable implements Parcelable, IAccountBean, IDisable {

    protected AccountBean(Parcel in) {
        account = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountBean> CREATOR = new Creator<AccountBean>() {
        @Override
        public AccountBean createFromParcel(Parcel in) {
            return new AccountBean(in);
        }

        @Override
        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDisplayValue() {
        return account;
    }

    @ID
    int id;

    public boolean disable;
    //    @Unique
    String account;


    public AccountBean() {
    }

    public AccountBean(String account) {
        this.account = account;
    }

    @Bindable
    public String getAccount() {
        return account;
    }

    public AccountBean setAccount(String account) {
        this.account = account;
        notifyPropertyChanged(BR.account);
        return this;
    }

    @Bindable
    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
        notifyPropertyChanged(BR.disable);
    }

    public String toStringJSON() {

        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return account;//编辑的时候是直接根据toString取得
    }
}
