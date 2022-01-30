package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;
/**
 * Created by luozheng on 2017/4/25.  qssq.space
 */

public class QQGroupBean {
    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_online_friend_count() {
        return group_online_friend_count;
    }

    public void setGroup_online_friend_count(String group_online_friend_count) {
        this.group_online_friend_count = group_online_friend_count;
    }

    public String getGroup_friend_count() {
        return group_friend_count;
    }

    public void setGroup_friend_count(String group_friend_count) {
        this.group_friend_count = group_friend_count;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getSeqid() {
        return seqid;
    }

    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    private String group_name;
    private String group_online_friend_count;
    private String group_friend_count;
    private String group_id;
    private String seqid;

    @Override
    public String toString() {
        return "QQGroupBean{" +
                "group_name='" + group_name + '\'' +
                ", group_online_friend_count='" + group_online_friend_count + '\'' +
                ", group_friend_count='" + group_friend_count + '\'' +
                ", group_id='" + group_id + '\'' +
                ", seqid='" + seqid + '\'' +
                '}';
    }
}
