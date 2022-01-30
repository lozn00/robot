package cn.qssq666.robot.bean;

/**
 * Created by qssq on 2018/8/18 qssq666@foxmail.com
 */
public class CheckSplitInfo {

    public String getWordSplit() {
        return wordSplit;
    }

    public void setWordSplit(String wordSplit) {
        this.wordSplit = wordSplit;
    }

    public String getAskanswerSplit() {
        return askanswerSplit;
    }

    public void setAskanswerSplit(String askanswerSplit) {
        this.askanswerSplit = askanswerSplit;
    }

    /**
     * 词条分隔符
     */
    public String wordSplit;
    /**
     * 问题与答案分隔符
     */
    public String askanswerSplit;
}
