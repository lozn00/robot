package cn.qssq666.robot.bean;

/**
 * Created by qssq on 2017/6/9 qssq666@foxmail.com
 */

public class GroupConfig {
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    long time;
    String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupConfig that = (GroupConfig) o;

        if (!content.equals(that.content)) return false;
        return account.equals(that.account);

    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + account.hashCode();
        return result;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    String account;


}
