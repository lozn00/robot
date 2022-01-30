package cn.qssq666.robot.bean;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 * 建议 obj存总数，根据发言时间间隔和总数,
 */


public class UniqueKey<T> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        UniqueKey<?> uniqueKey = (UniqueKey<?>) o;

        if (group != null ? !group.equals(uniqueKey.group) : uniqueKey.group != null) return false;
        return account != null ? account.equals(uniqueKey.account) : uniqueKey.account == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    T obj;

    public String getGroup() {
        return group;

    }

    public UniqueKey<T> setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public UniqueKey<T> setAccount(String account) {
        this.account = account;
        return this;
    }

    String group;
    String account;

}
