package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import cn.qssq666.db.anotation.ID;
import cn.qssq666.robot.utils.ClearUtil;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class ReplyWordBean implements Parcelable {
    protected ReplyWordBean(Parcel in) {

        ask = in.readString();
        answer = in.readString();
        id = in.readInt();
    }

    public ReplyWordBean(String ask) {
        this.ask = ask;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ask);
        dest.writeString(answer);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReplyWordBean> CREATOR = new Creator<ReplyWordBean>() {
        @Override
        public ReplyWordBean createFromParcel(Parcel in) {
            return new ReplyWordBean(in);
        }

        @Override
        public ReplyWordBean[] newArray(int size) {
            return new ReplyWordBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public ReplyWordBean() {
    }

    public ReplyWordBean(String ask, String answer) {
        this.ask = ask;
        this.answer = answer;
    }

    public ReplyWordBean appendAsk(String word){
        this.ask=this.ask+ ClearUtil.wordSplit+word;
        return this;
    }
    public ReplyWordBean appendAnswer(String word){
        this.answer=this.answer+ ClearUtil.wordSplit+word;
        return this;
    }



    public String getAsk() {
        return ask;
    }

    public ReplyWordBean setAsk(String ask) {
        this.ask = ask;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public ReplyWordBean setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    String ask;
    String answer;
    @ID
    int id;

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    private long createdAt = new Date().getTime();

    @Override
    public String toString() {
        return "ask='" + ask + '\'' +
                ",answer='" + answer + "";
    }
}
