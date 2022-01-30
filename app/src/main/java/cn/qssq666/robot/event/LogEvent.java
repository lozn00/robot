package cn.qssq666.robot.event;

/**
 * Created by luozheng on 2017/3/12.  qssq.space
 */

public class LogEvent {
    public String getLog() {
        return log;
    }

    public LogEvent setLog(String log) {
        this.log = log;
        return this;
    }

    String log;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    long time;
}
