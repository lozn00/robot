package cn.qssq666.robot.utils;
import android.content.Context;
import androidx.core.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.RobotContentProvider;

/**
 * Created by qssq on 2017/12/24 qssq666@foxmail.com
 */

public class ConfigUtils {
    static {
        LogUtil.importPackage();

    }

    private static final String ATKEY_JSON_TROOP = "troop_at_info_list";
    private static final String ATKEY_JSON_DISC = "disc_at_info_list";
    /*
    {"code":0,"extrajson":"","extstr":"{\"disc_at_info_list\":\"[{\\\"flag\\\":0,\\\"uin\\\":1449924790,\\\"startPos\\\":3,\\\"textLen\\\":19}]\"}","frienduin":"474240677","istroop":1,"message":"gag@小智....河南..荣耀8...NR ","nickname":"情随事迁","selfuin":"202927128","senderuin":"153016267","time":1515246420000,"type":-1000}
     */

    public static boolean replyNeedAt(RobotContentProvider contentProvider) {
        return contentProvider.mCfBaseReplyNeedAite;
    }

    public static boolean hasAtRobotAndClearSelf(RobotContentProvider contentProvider, MsgItem item) {
        if (TextUtils.isEmpty(item.getExtstr()) || !RobotUtil.isJsonObjectMessage(item.getExtstr())) {
            //LogUtil.writeLog("非法消息," + item.getExtstr() + ",无法启用艾特回复功能");
            return false;
        }

        JSONObject jsonObject = JSON.parseObject(item.getExtstr());

        //需要把message格式化一下替换删除
        Pair<Boolean, String> isAtMsgPair = isAtMsg(jsonObject);

        if (!isAtMsgPair.first) {
            return false;
        } else {
            List<GroupAtBean> troop_at_info_list1 = JSON.parseArray(jsonObject.getString(isAtMsgPair.second), GroupAtBean.class);
            GroupAtBean atModel = AccountUtil.findAccount(troop_at_info_list1, item.getSelfuin(), false);
            if (atModel != null) {
                doDeleteAtMsg(item, atModel);
                return true;
            } else {
                clearFromList(item, true, troop_at_info_list1);//艾特模式
                //LogUtil.writeLog("发现有人，但是不是艾特机器人" + atModel);
                return false;
            }
        }

    }

    /**
     * 前者代表是否艾特，后者代表是否有艾特机器人
     *
     * @param item
     * @return
     */
    public static Pair<Boolean, Boolean> clearAllAtMsg(MsgItem item) {
        if (TextUtils.isEmpty(item.getExtstr()) || !RobotUtil.isJsonObjectMessage(item.getExtstr())) {
            return Pair.create(false, false);
        }

        JSONObject jsonObject = JSON.parseObject(item.getExtstr());

        //需要把message格式化一下替换删除
        Pair<Boolean, String> isAtMsgPair = isAtMsg(jsonObject);
        if (!isAtMsgPair.first) {
            return Pair.create(false, false);
        } else {
            boolean findSelf = false;//  (Exceptions are not yet supported across processes.)
            List<GroupAtBean> troop_at_info_list1 = JSON.parseArray(isAtMsgPair.second, GroupAtBean.class);
            findSelf = clearFromList(item, findSelf, troop_at_info_list1);

            return Pair.create(true, findSelf);
        }

    }


    public static Pair<Boolean, String> isAtMsg(JSONObject jsonObject) {
        boolean hasAt = jsonObject.containsKey(ATKEY_JSON_TROOP);
        if (hasAt) {
            return Pair.create(true, ATKEY_JSON_TROOP);

        } else {

            hasAt = jsonObject.containsKey(ATKEY_JSON_DISC);
            if (hasAt) {
                return Pair.create(true, ATKEY_JSON_DISC);
            } else {
                return Pair.create(false, null);
            }

        }

    }

    /**
     * 前者代表是否艾特，后者代表艾特数组 以及是否包含自己.
     *
     * @param item
     * @return Pair<Boolean, Pair<Boolean, List<GroupAtBean>>>    first pair,是否艾特  后者  是否包含自己以及艾特数组
     */


    public static Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> clearAndFetchAtArray(MsgItem item) {

        List<GroupAtBean> empty = null;
        if (TextUtils.isEmpty(item.getExtstr()) || !RobotUtil.isJsonObjectMessage(item.getExtstr())) {
            return Pair.create(false, Pair.create(false, empty));
        }

        JSONObject jsonObject = JSON.parseObject(item.getExtstr());

        //需要把message格式化一下替换删除
        Pair<Boolean, String> hasAtPair = isAtMsg(jsonObject);

        if (!hasAtPair.first) {
            return Pair.create(false, Pair.create(false, empty));
        } else {
            boolean findSelf = false;
            // '[', but {

            try{

            List<GroupAtBean> troop_at_info_list1 = JSON.parseArray(jsonObject.getString(hasAtPair.second), GroupAtBean.class);
            /**
             * 分离数据要从后面开始分离 否则出毛病因为里面的标记位置是结束索引和开始索引,一旦被删除
             */

            ;
            Collections.reverse(troop_at_info_list1);
            findSelf = clearFromList(item, findSelf, troop_at_info_list1);
            return Pair.create(true, Pair.create(findSelf, troop_at_info_list1));

            }catch (Exception e){

                Log.e("AITE_DATA_ERROR", jsonObject.toJSONString()+"数据错误:"+Log.getStackTraceString(e));

                //  List<GroupAtBean> troop_at_info_list1 = JSON.parseArray(jsonObject.getString(hasAtPair.second), GroupAtBean.class);
                //            /**
                List<GroupAtBean> b = new ArrayList<>();
                return Pair.create(false, Pair.create(findSelf, b));
            }
        }

    }


    public static boolean clearFromList(MsgItem item, boolean findSelf, List<GroupAtBean> troop_at_info_list1) {
        GroupAtBean groupAtBean = null;
        if (troop_at_info_list1 != null) {
            String backMessage = item.getMessage();
            for (GroupAtBean bean : troop_at_info_list1) {
                if (item.getSelfuin().equals(bean.getUin())) {
                    findSelf = true;//这样做是机器人是艾特自己的虽然是空消息，也不需要这样忽略
                    groupAtBean = bean;
                }
                doDeleteAtMsg(item, bean);
            }


        }
        if (groupAtBean != null) {//移除自己
            troop_at_info_list1.remove(groupAtBean);
        }
        return findSelf;
    }

    public static String doDeleteAtMsg(MsgItem item, GroupAtBean atModel) {
        String message = item.getMessage();

        if (atModel.getStartPos() < message.length() && atModel.getTextLen() <= message.toCharArray().length) {
            String separateText = StringUtils.bSubstringByChar(message, atModel.getStartPos(), atModel.getTextLen());
            atModel.setNickname(separateText);
            String fixMessage = message.replace(separateText, "");
            item.setMessage(fixMessage);//因为删除索引早就标记了,所以删除后直接替换会出现问题，想过反过来数组删除方法和先批量处理然后组个还原方法.
            //LogUtil.writeLog("split text:" + separateText + ",删除艾特文本之后信息:" + message + ",len:" + atModel.getTextLen() + ",开始位置" + atModel.getStartPos());
            return fixMessage;
        } else {
            //LogUtil.writeLog("分离艾特数据失败,数据校验不合法" + atModel);

        }
        return message;
    }


    public static boolean isDisableAtFunction(RobotContentProvider contentProvider) {
        return contentProvider.mCfBaseDisableAtFunction;
    }


    public static GroupAtBean fetchLastAtBean(MsgItem item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair) {
        if (atPair.first && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {

            GroupAtBean groupAtBean = atPair.second.second.get(atPair.second.second.size() - 1);
            if (!item.getSelfuin().equals(groupAtBean.getAccount())) {
                return groupAtBean;
            }

        }
        return null;


    }

    public static GroupAtBean createGroupAtBean(String qq, String nickname) {
        GroupAtBean groupAtBean = new GroupAtBean();
        groupAtBean.setNickname(nickname);
        groupAtBean.setSenderuin(qq);
        groupAtBean.setTextLen(nickname.toCharArray().length);
        return groupAtBean;

    }

    public static Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> createAllAiteMsg(String nickname) {
        return createAiteMsg("0", nickname);

    }

    public static Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> createAiteMsg(String qq, String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            nickname = "全体同胞";
        }

        List<GroupAtBean> list = new ArrayList<>();
        list.add(createGroupAtBean(qq, nickname));
        Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> pair = Pair.create(true, Pair.create(false, list));
        return pair;

    }

    public static boolean IsNeedAt(GroupWhiteNameBean nameBean) {
        return nameBean != null && nameBean.isNeedaite();
    }

    public static void saveAppUIPerference(Context context, String key, String value) {
        SPUtils.switchDefaultSystemConfig();
        SPUtils.setValue(context, key, value);
        SPUtils.switchAppConfig();
    }
    public static void saveAppUIPerference(Context context, String key, boolean value) {
        SPUtils.switchDefaultSystemConfig();
        SPUtils.setValue(context, key, value);
        SPUtils.switchAppConfig();
    }
    public static void saveAppUIPerference(Context context, String key, int value) {
        SPUtils.switchDefaultSystemConfig();
        SPUtils.setValue(context, key, value);
        SPUtils.switchAppConfig();
    }
    public static void saveAppUIPerference(Context context, String key, long value) {
        SPUtils.switchDefaultSystemConfig();
        SPUtils.setValue(context, key, value);
        SPUtils.switchAppConfig();
    }

}
