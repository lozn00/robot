package cn.qssq666.robot.business;

import java.util.LinkedList;

import androidx.collection.ArrayMap;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.UniqueKey;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.NickNameUtils;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public class FrequentMessageDetectionUtils {

    /**
     * 值为long类型时间
     */
    static ArrayMap<UniqueKey, LinkedList<Long>> recentMaps = new ArrayMap<>();


    /**
     * 可以检测群消息和非群消息,
     *
     * @param item
     * @param whiteNameBean
     * @return
     */
    public static String doCheckFrequentMessage(MsgItem item, GroupWhiteNameBean whiteNameBean) {

        long nowTime = AppUtils.getNowTime();
        UniqueKey uniqueKey = new UniqueKey().setAccount(item.getSenderuin()).setGroup(item.getFrienduin());
        LinkedList<Long> arrayList = recentMaps.get(uniqueKey);//首先查出他的消息记录堆栈，这个堆栈是每一个群 和q的唯一值 或者是私聊消息的唯一值
        if (arrayList == null) {
            arrayList = new LinkedList<>();
            recentMaps.put(uniqueKey, arrayList);
        }
        arrayList.addLast(nowTime);


        int count = whiteNameBean != null ? whiteNameBean.getFrequentmsgcount() : IGnoreConfig.frequentMsgDistanceSecondCount;
        if (count == 0) {
            if(RobotContentProvider.ENABLE_LOG) {
                LogUtil.writeLoge("忽略检测刷屏，消息总数配置存在错误，群：" + item.getFrienduin());
            }
            return null;
        }

        if (arrayList.size() >= count) {

            long getFirstMsgTime = arrayList.getFirst();
            long distance = DateUtils.getTimeDistance(DateUtils.TYPE_SECOND, nowTime, getFirstMsgTime);
            arrayList.removeFirst();//保持最大记录为这么多
            int distanceSeconrd = whiteNameBean != null ? IGnoreConfig.frequentMsgDistanceDurationSecond : whiteNameBean.getFrequentmsgduratiion();

            if (distanceSeconrd <= 0) {
                if(RobotContentProvider.ENABLE_LOG){

                LogUtil.writeLoge("忽略检测刷屏，间隔秒存在错误,配置存在错误,配置总数为:" + distanceSeconrd + ",群:" + item.getFrienduin());
                }
                return null;
            }
            if (distance <= distanceSeconrd) {
                String descript = "" + NickNameUtils.formatNickname(RobotContentProvider.getDbUtils(), item) + "存在恶意刷屏,在" + distanceSeconrd + "秒内发布" + count + "条信息,第一条发布时间为:" + DateUtils.getTime(getFirstMsgTime) + ",最后一条发布时间为:" + DateUtils.getTime(nowTime);
                if(RobotContentProvider.ENABLE_LOG) {
                    LogUtil.writeLog(descript);
                }
                return descript;
            }
            if(RobotContentProvider.ENABLE_LOG) {

                LogUtil.writeLog("检测刷屏，通过,无不良记录," + NickNameUtils.formatNickname(item) + "在" + distance + "秒内发布了" + arrayList.size() + "条消息,未低于" + distanceSeconrd + "秒");
            }
        } else {
            if(RobotContentProvider.ENABLE_LOG) {
                LogUtil.writeLog("检测刷屏，通过,无不良记录," + NickNameUtils.formatNickname(item) + "发言只有" + arrayList.size() + "条，未达到检测条件");
            }
        }

        return null;
    }

    static {
        LogUtil.importPackage();

    }

}
