package cn.qssq666.robot.config;
import cn.qssq666.CoreLibrary0;import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import java.util.Date;

import cn.qssq666.robot.bean.GroupConfig;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.utils.LogUtil;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class IGnoreConfig {
    //在多少秒内的关键词算刷屏
    public static final long MAX_SHUAPIN_TIME_SECOND_MS = 20;
    //刷屏屏蔽时间
    public static final long SHUAPIN_TIME_DURATION = 1000 * 5;
    public static final long SHUAPIN_GAG_SECOND = 60 * 60;
    public static final long DEFAULT_GAG_TIME = 60;
    public static final String DEFAULT_GAG_TIME_STR = "1分钟";
    public static final long DEFAULT_GAG_TIME_SHORT = 60;//最短60没法子
    public static final String REDPACKET_GAG_DURATION = "10天";
    public static final long REDPACKET_KICK_DELAY_TIME_MINUTE = 30;
    public static final String EMPTY_REPLY_WORD = "我是情迁聊天机器人,有事找我请说话，别光艾特我不说话,测试我是否正常你可以骂我，我可以保证不会禁言你哦!";
    /**
     * 临时屏蔽人主要是解决机器人ko问题，实际上 私聊消息的话也需要.群消息呢，则直接禁言.
     */
    public static Object tempNoDrawPerson;
    public static long distanceNetHistoryTimeIgnore = 3;
    public static long distancedulicateCacheHistory = 0;
    public static long distanceStatupTimeIgnore = 10;

    public static long groupMsgLessSecondIgnore = 200;

    public static ArrayMap<GroupConfig, GroupConfig> sGroupListRecordMap = new ArrayMap<>();
    public static int frequentMsgDistanceSecondCount = 15;//如果5秒钟发了30条。 且最新的一条和最短的一条间隔为多少就认为是刷屏
    public static int frequentMsgDistanceDurationSecond = 10;//如果5秒钟发了30条。 且最新的一条和最短的一条间隔为多少就认为是刷屏

    public static GroupConfig getCurrentGroupConfig(GroupConfig config) {
        if (sGroupListRecordMap.size() > 4500) {//太大的时候清空
            synchronized (IGnoreConfig.class) {
                for (int i = 0; i < 2000; i++) {
                    sGroupListRecordMap.remove(i);

                }
//            sGroupListRecordMap.clear();
            }
            //LogUtil.writeLog("容量爆满已清空1000个,当前总数:" + sGroupListRecordMap.size());
        }
        GroupConfig result = sGroupListRecordMap.get(config);
        sGroupListRecordMap.put(config, config);
        return result;
    }




    public static GroupConfig getCurrentGroupConfig(IMsgModel item) {
        GroupConfig config = new GroupConfig();
        config.setTime(new Date().getTime());//让60秒时间递增
        config.setContent(item.getMessage());
        config.setAccount(item.getFrienduin());
        return getCurrentGroupConfig(config);


    }

    static {
        LogUtil.importPackage();

    }
}
