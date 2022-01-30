package cn.qssq666.robot.plugin.util;
import cn.qssq666.CoreLibrary0;import java.io.File;
import java.io.FileFilter;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class PluginFileFilter  implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        String absolutePath = pathname.getAbsolutePath();
        return absolutePath.endsWith(".apk")||absolutePath.endsWith(".jar")||absolutePath.endsWith(".dex")||absolutePath.endsWith("zip");
    }
}
