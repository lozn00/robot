package cn.qssq666.robot.utils;

import java.util.ArrayList;

/**
 * Created by qssq on 2018/11/24 qssq666@foxmail.com
 */
public class MenuUtil {
    private ArrayList<String> menus;

    public static MenuUtil build() {
        MenuUtil menuUtil = new MenuUtil();
        return menuUtil;
    }

    public static MenuUtil build(String[] menu) {
        MenuUtil menuUtil = new MenuUtil(menu);
        return menuUtil;
    }

    public MenuUtil(String[] menu) {

        this.menus = new ArrayList<>();
        for (String s : menu) {
            menus.add(s);
        }
    }

    public MenuUtil() {
        this.menus = new ArrayList<>();
    }

    public ArrayList<String> getMenus() {
        return menus;
    }

    public String[] getMenuArrays() {
        String[] menu_arr = new String[menus.size()];
        for (int i = 0; i < menus.size(); i++) {
            menu_arr[i] = this.menus.get(i);
        }
        return menu_arr;
    }

    public String[] add(String menu) {
        this.menus.add(menu);
        return getMenuArrays();
    }

    public MenuUtil append(String menu) {
        this.menus.add(menu);
        return this;
    }
}
