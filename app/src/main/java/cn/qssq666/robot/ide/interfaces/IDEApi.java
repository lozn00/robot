package cn.qssq666.robot.ide.interfaces;

/**
 * Created by qssq on 2018/12/24 qssq666@foxmail.com
 */
public interface IDEApi {
    public void setLang(int instance);

    void paste(String content);

    void setTextCode(String s);

    String getTextCode();

    void undo();
    void formatCode();

    void redo();

    void requestCodeFocus();
}
