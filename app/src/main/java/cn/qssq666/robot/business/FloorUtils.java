package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.FloorBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.NickNameUtils;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.RegexUtils;
import cn.qssq666.robot.utils.StringUtils;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public class FloorUtils<T> {
    public static final int STR_LENGTH = 10;
    public static final String MULTI_FLOOR_SPLIT = "-";
    private static HashMap<String, ArrayList<MsgItem>> baseGroupMsgItems = new HashMap<>();
    private static int DEFAULT_FLOOR = 1;


    private static void removeTop(ArrayList<MsgItem> msgItems, int count) {
        for (int i = 0; i < count; i++) {
            msgItems.remove(msgItems.size() - 1);
        }

    }

    private static ArrayList<MsgItem> getFloorData(String group) {
        return baseGroupMsgItems.get(group);


    }


    private static String florDataToStrSplit(ArrayList<MsgItem> floorData) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < floorData.size(); i++) {
            sb.append("" + floorData.get(i).getSenderuin());
            if (i != floorData.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    public static ArrayList<MsgItem> initGroupFloorFromDb(DBUtils dbUtils, String group, MsgItem msgItem) {
        ArrayList<MsgItem> msgItems = new ArrayList<>();

        FloorBean queryByColumn = DBHelper.getFloorUtil(dbUtils).queryByColumn(FloorBean.class, FieldCns.FIELD_ACCOUNT, group);
        String[] qqs = queryByColumn != null ? queryByColumn.getData().split(",") : null;

        if (qqs != null && qqs.length > 0) {
            for (String qq : qqs) {
                msgItems.add(new MsgItem().setSenderuin(qq).setFrienduin(group).setType(msgItem.getType()).setIstroop(msgItem.getIstroop()));

            }
        }
        return msgItems;


    }


    static {
        LogUtil.importPackage();

    }

    public static void saveAllGroupFloorToDb() {
        for (String group : baseGroupMsgItems.keySet()) {
            saveGroupFloorToDb(group);

        }
      /*  for (Map.Entry<String, ArrayList<MsgItem>> entry : baseGroupMsgItems.entrySet()) {

            saveGroupFloorToDb(entry.getKey());
        }*/
    }

    public static boolean saveGroupFloorToDb(String group) {
        ArrayList<MsgItem> floorData = getFloorData(group);
        if (floorData != null) {//first need delete floor
            FloorBean floorBean = DBHelper.getFloorUtil(AppContext.getDbUtils()).queryByColumn(FloorBean.class, FieldCns.FIELD_ACCOUNT, group);
            if (floorBean != null) {
                floorBean.setData(florDataToStrSplit(floorData));
                int update = DBHelper.getFloorUtil(AppContext.getDbUtils()).update(floorBean);
                //LogUtil.writeLog("更新" + group + "群数据结果:" + update);
//            DBHelper.getFloorUtil(AppContext.getDbUtils()).deleteByColumn(FloorBean.class, DBCns.account, group);
            } else {

                floorBean = new FloorBean();
                floorBean.setAccount(group);
                floorBean.setData(florDataToStrSplit(floorData));
                long insert = DBHelper.getFloorUtil(AppContext.getDbUtils()).insert(floorBean);
                //LogUtil.writeLog("存储" + group + "群数据结果:" + insert);
            }
            return true;
        }

        return false;


    }


    public static boolean isMultiFloorData(String str) {
        Pair pair = parseMultiFloorData(str);
        return pair != null;
    }

    public static Pair<Integer, Integer> parseMultiFloorData(String str) {

        if (str.contains(MULTI_FLOOR_SPLIT)) {
            String strLeft = StringUtils.getStrLeft(str, MULTI_FLOOR_SPLIT);
            String strRight = StringUtils.getStrRight(str, MULTI_FLOOR_SPLIT);
            if (!TextUtils.isEmpty(strLeft) && !TextUtils.isEmpty(strRight) && RegexUtils.checkNoSignDigit(strLeft) && RegexUtils.checkNoSignDigit(strRight)) {

                int a = Integer.parseInt(strLeft);
                int b = Integer.parseInt(strRight);
                if (a < 0 || b < 0) {
                    return null;
                } else if (b < a) {
                    return null;
                }
                return Pair.create(a, b);
            }
        }
        return null;
    }

    public static boolean isFloorData(String str) {
        if (TextUtils.isEmpty(str) || (str.length() > 0 && str.length() <= 2 && RegexUtils.checkNoSignDigit(str))) {
            return true;
        }

        return false;
    }


    public static String getFloorInputDataInValidMsg(String str) {
        return "无法根据编号{" + str + "}锁定楼层,那么请输入详细的群号或q号";
    }


    public static void onReceiveNewMsg(DBUtils dbUtils, MsgItem msgItem) {
        String group = msgItem.getFrienduin();
        ArrayList<MsgItem> msgItems = baseGroupMsgItems.get(group);
        if (msgItems == null) {
            msgItems = initGroupFloorFromDb(dbUtils, group, msgItem);

            baseGroupMsgItems.put(group, msgItems);
        }

        synchronized (msgItems) {
//        msgItems.addLast();
//            msgItems.addLast();
            msgItems.remove(msgItem);
            msgItems.add(0, msgItem.clone());
            if (msgItems.size() >= 160) {
                removeTop(msgItems, 50);
            }
        }


    }


    public static String printFloorData(String group, int count) {
        StringBuffer sb = new StringBuffer();
        ArrayList<MsgItem> floorData = getFloorData(group);
        sb.append("当前群数据楼层数据报表");
        if (floorData != null) {
            int printCont = floorData.size() > count ? count : floorData.size();
            sb.append("楼层总数：" + floorData.size());
            sb.append("\n");
            floorData.size();
            for (int i = 0; i < printCont; i++) {
                MsgItem msgItem = floorData.get(i);
//                sb.append(String.format("%d.%s(%s)", i, msgItem.getSenderuin(), msgItem.getNickname()));
                String nicknameStr = null;
                if (TextUtils.isEmpty(msgItem.getNickname()) || msgItem.getNickname().equals(msgItem.getSenderuin())) {
                    nicknameStr = i + "." + NickNameUtils.formatNickname(group, msgItem.getSenderuin(), "");
                } else {
                    nicknameStr = String.format("%d.%s(%s)", i, msgItem.getSenderuin(), msgItem.getNickname());
                }
                sb.append(nicknameStr);

                sb.append("\n");
            }
        } else {
            sb.append("楼层总数：" + 0);

        }
        return sb.toString();

    }

    public static int gettFloorCount(String group) {
        ArrayList<MsgItem> msgItems = baseGroupMsgItems.get(group);
        if (msgItems == null) {
            return 0;
        }
        return msgItems.size();


    }


    /*
    队列是FIFO的（先进先出）；

    堆栈式FILO的（现今后出）；
     */
    public static String getFloorQQ(String group) {
        return getFloorQQ(group, DEFAULT_FLOOR);
    }

    public static String getFloorQQ(String group, String floorNumber) {
        return getFloorQQ(group, TextUtils.isEmpty(floorNumber) ? DEFAULT_FLOOR : ParseUtils.parseInt(floorNumber));
    }

    public static String getFloorQQ(String group, int floorNumber) {
        MsgItem floor = getFloor(group, floorNumber);
        if (floor != null) {
            return floor.getSenderuin();
        }
        return null;

    }

    public static List<MsgItem> getFloors(String group, int startFloorNumber, int endFloorNumber) {
        ArrayList<MsgItem> msgItems = baseGroupMsgItems.get(group);
        if (msgItems == null) {
            return null;
        } else {
            synchronized (msgItems) {
                endFloorNumber = endFloorNumber < msgItems.size() ? endFloorNumber : msgItems.size() - 1;
                if (endFloorNumber < startFloorNumber) {
                    startFloorNumber = endFloorNumber;
                }

                ArrayList<MsgItem> findFloorMsgItems = new ArrayList<>();
                for (int i = startFloorNumber; i <= endFloorNumber; i++) {
                    MsgItem item = msgItems.get(i);

                    findFloorMsgItems.add(item);
                }

                return findFloorMsgItems;


                //                if (msgItems.getLast())
            }
//            return msgItems.get()
        }


    }


    public static MsgItem getFloor(String group, int floorNumber) {


        ArrayList<MsgItem> msgItems = baseGroupMsgItems.get(group);
        if (msgItems == null) {
            return null;
        } else {
            synchronized (msgItems) {
                if (!msgItems.isEmpty() && floorNumber < msgItems.size()) {
                    return msgItems.get(floorNumber);
                }
                //                if (msgItems.getLast())
            }
//            return msgItems.get()
            return null;
        }

    }

    /**
     * 举个例子 我要添加管理员，可以从群里添加和私聊添加，私聊添加只能直接给值了,  但是如果为空则返回null,群消息 则获取楼层默认，而 群消息有参数则判断是否楼层，是楼层则获取楼层，否则也是直接取值
     *
     * @param item
     * @param arg1
     * @return
     */
    public static String getQQByMsgItemFromValue1(MsgItem item, String arg1) {
        String group = item.getFrienduin();
        String account;

        if (TextUtils.isEmpty(arg1)) {

            boolean groupMsg = MsgTyeUtils.isGroupMsg(item);
            if (groupMsg) {
                account = FloorUtils.getFloorQQ(group);
                return account;
            }

        } else {
            if (MsgTyeUtils.isGroupMsg(item)) {


                if (FloorUtils.isFloorData(arg1)) {
                    account = FloorUtils.getFloorQQ(group, arg1);
                    return account;
                } else {
                    return arg1;
                }
               /* else {
                    Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
                    if (pair != null) {
                        List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                        FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, item, floors, group, gagTime);
                        return true;
                    }
                }*/


            } else {

                return arg1;
            }


        }


        return null;


    }
}
