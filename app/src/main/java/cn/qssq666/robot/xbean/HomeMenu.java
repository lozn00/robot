package cn.qssq666.robot.xbean;

/**
 * Created by qssq on 2018/1/18 qssq666@foxmail.com
 */

public class HomeMenu {
    public HomeMenu() {
    }

    public HomeMenu(int id, String title) {
        this.title = title;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color;
}
