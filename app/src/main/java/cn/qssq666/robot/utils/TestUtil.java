package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.bean.AtBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;

/**
 * Created by qssq on 2018/11/15 qssq666@foxmail.com
 */
public class TestUtil {
    public static MsgItem createTestItem() {
        MsgItem msgItem = new MsgItem();
        msgItem.setNickname("testnickname");
        msgItem.setApptype("testtype");
        msgItem.setCode(0);
        msgItem.setExtstr("{}");
        msgItem.setFrienduin("153016267");
        msgItem.setSenderuin("153016267");
        msgItem.setSelfuin("35068264");
        msgItem.setExtrajson("{}");
        msgItem.setTime(System.currentTimeMillis());
        msgItem.setIstroop(0);
        msgItem.setVersion("1.0");
        msgItem.setMessage("TESTMSG");
        msgItem.setVersion("1.0");
        return msgItem;
    }

    public static List<AtBeanModelI> createAiteItem() {
        List<AtBeanModelI> list = new ArrayList<>();
        AtBean atBean = new AtBean();
        atBean.setMsg("艾特模型1");
        atBean.setNickname("小冰的朋友");
        atBean.setSenderuin("202927128");
        list.add(atBean);
        atBean = new AtBean();
        atBean.setMsg("哈哈哈");
        atBean.setNickname("小冰");
        atBean.setSenderuin("35697438");
        list.add(atBean);
        return list;
    }

    public static String greenWrapFont(String str, String afters) {
        return colorWrap("#006400", str, afters);
    }
    public static String blackWrapFont(String str, String afters) {
        return colorWrap("#000000", str, afters);
    }
    public static String grayWrapFont(String str, String afters) {
        return colorWrap("#cccccc", str, afters);
    }
    public static String qianYellowWrapFont(String str, String afters) {
        return colorWrap("#FFFFCC", str, afters);
    }

    public static String blueWrapFont(String str, String afters) {
        return colorWrap("#00008B", str, afters);
    }

    public static String redWrapFont(String str, String afters) {
        return colorWrap("red", str, afters);
    }

    public static String warnYellowWrapFont(String str, String afters) {
        return colorWrap("#A0522D", str, afters);
    }


    public static String colorWrap(String color, String str, String afters) {
        return "<font color='" + color + "'>" + str + "</font>" + (afters == null ? "" : afters);
    }
}
