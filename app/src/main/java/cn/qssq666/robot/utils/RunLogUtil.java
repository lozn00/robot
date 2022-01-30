package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import java.io.File;

/**
 * Created by qssq on 2018/8/3 qssq666@foxmail.com
 */
public class RunLogUtil {
    public static File getLogFile() {
        return new File("/data/local/tmp/cn.qssq666.robot_status.txt");
    }

    public void writeLog(){

    }

    public String readLog(){
        return "";
    }
}
