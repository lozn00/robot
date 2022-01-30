package cn.qssq666.robot.utils;

import java.io.File;

public class PicPathUtil {
    public static File getPicRootdir() {
        return new File("/sdcard/qssq666/pic");
    }

    public static File getTextRootdir() {
        return new File(getPicRootdir(), "text");
    }
}
