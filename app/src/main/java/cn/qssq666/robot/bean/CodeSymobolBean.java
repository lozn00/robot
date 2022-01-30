package cn.qssq666.robot.bean;

/**
 * Created by qssq on 2018/11/17 qssq666@foxmail.com
 */
public class CodeSymobolBean {
    public CodeSymobolBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public CodeSymobolBean(String title, String content, int codeLocation) {
        this.title = title;
        this.content = content;
        this.codeLocation = codeLocation;
    }

    public static CodeSymobolBean getInstance(String title) {
        return new CodeSymobolBean(title, null);
    }

    public static CodeSymobolBean getInstance(String title, String content) {
        return new CodeSymobolBean(title, content);
    }

    public static CodeSymobolBean getInstance(String title, String content, int action) {
        return new CodeSymobolBean(title, content, action);
    }

    public String getTitle() {
        return title;
    }

    public CodeSymobolBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        if (content == null) {
            return this.title;
        }
        return content;
    }

    public CodeSymobolBean setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 显示得标题
     */
    String title;
    /**
     * 真实插入得内容
     */
    String content;

    public int getCodeLocation() {
        return codeLocation;
    }

    public void setCodeLocation(int codeLocation) {
        this.codeLocation = codeLocation;
    }

    int codeLocation = CodeLocation.KEEP;

    public interface CodeLocation {
        int KEEP = 1;
        int LINE_START = 1;
        int LINE_END = 2;
    }
}
