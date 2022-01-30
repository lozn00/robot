package cn.qssq666.robot.business;

import cn.qssq666.CoreLibrary0;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.bean.AtBean;
import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.ConfigUtils;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.NickNameUtils;

/**
 * Created by qssq on 2017/12/17 qssq666@foxmail.com
 */

public class FloorMultiUtils {

    public static boolean doMultiGagLogic(RobotContentProvider contentProvider, boolean isManager, GroupWhiteNameBean nameBean, MsgItem mainItem, List<MsgItem> itemList, String group, long gagTime) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量禁言,无数据", mainItem);
            return false;
        }
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        int gagCount = 0;
        for (int i = 0; i < itemList.size(); i++) {
            MsgItem item = itemList.get(i);
            MsgItem gagMsgItem = item;
            gagMsgItem.setIstroop(1);
            gagMsgItem.setFrienduin(group);
            gagMsgItem.setMessage(gagTime + "");
            if (MsgTyeUtils.isSelfMsg(gagMsgItem)) {
                continue;//忽略自己
            } else if (contentProvider.isManager(gagMsgItem)) {
                continue;
            } else if (isManager == false && RobotContentProvider.getInstance().isCurrentGroupAdminFromDb(nameBean, gagMsgItem.getSenderuin(), mainItem.getFrienduin())) {//不是超级管理，那么就是普通管理进入了此逻辑分支。
                LogUtil.writeLog("是当前群管理,无法进行批量操作踢人..");
                continue;
            }
            String nickname = null;
            if (ConfigUtils.isDisableAtFunction(contentProvider)) {
                nickname = NickNameUtils.formatNickname(item.getFrienduin(), item.getSenderuin());

            } else {
                nickname = NickNameUtils.queryMatchNickname(item.getFrienduin(), item.getSenderuin());
            }

            if (TextUtils.isEmpty(nickname)) {

                nickname = "" + i + "号";

            }
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(gagMsgItem.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);

            gagCount++;
            if (i < 5 && RemoteService.isIsInit()) {
                String s = RemoteService.gagUser(gagMsgItem.getFrienduin(), gagMsgItem.getSenderuin(), gagTime);
                if (s != null) {
                    sb.append(":{" + s + "}");
                } else {
                    MsgReCallUtil.notifyGadPersonMsgNoJump(contentProvider, gagTime, gagMsgItem);
                }
            } else {
                MsgReCallUtil.notifyGadPersonMsgNoJump(contentProvider, gagTime, gagMsgItem);
            }
            if (i != itemList.size() - 1) {
                sb.append("、");

            }


        }


        if (gagCount == 0) {
            return false;
        }
        String msg;

        boolean muliperson = atBeanList != null && atBeanList.size() > 1;
        if (gagTime <= 0) {
            if (muliperson) {
                msg = AppConstants.ACTION_OPERA_MULTI_NAME + "解除" + sb.toString() + "的禁言,操作总人数:" + gagCount;

            } else {
                msg = AppConstants.ACTION_OPERA_NAME + "解除" + sb.toString() + "禁言";

            }
        } else {
            msg = AppConstants.ACTION_OPERA_MULTI_NAME + "禁言" + sb.toString() + ",禁言时间" + DateUtils.getGagTime(gagTime) + ",操作总人数:" + gagCount;
        }


        //设置结果在群里触发
        mainItem.setFrienduin(group);
        mainItem.setIstroop(1);
        if (ConfigUtils.isDisableAtFunction(contentProvider)) {
            MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(contentProvider, msg, mainItem);
        } else {

            MsgReCallUtil.notifyAtMsgJumpB(contentProvider, msg, atBeanList, mainItem);
        }
        return true;
    }

    public static boolean doMultiAtGagLogic(RobotContentProvider contentProvider, boolean isManager, GroupWhiteNameBean nameBean, MsgItem mainItem, List<GroupAtBean> itemList, String group, long gagTime) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量禁言,无数据", mainItem);
            return false;
        }
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        int gagCount = 0;
        for (int i = 0; i < itemList.size(); i++) {
            GroupAtBean itemAt = itemList.get(i);
            MsgItem gagMsgItem = new MsgItem();
            gagMsgItem.setIstroop(1);
            gagMsgItem.setNickname(itemAt.getNickname());
            gagMsgItem.setSelfuin(mainItem.getSelfuin());
            gagMsgItem.setSenderuin(itemAt.getUin());
            gagMsgItem.setFrienduin(group);
            gagMsgItem.setMessage(gagTime + "");
            if (MsgTyeUtils.isSelfMsg(gagMsgItem)) {
                continue;//忽略自己
            } else if (contentProvider.isManager(gagMsgItem)) {
                continue;
            } else if (isManager == false && RobotContentProvider.getInstance().isCurrentGroupAdminFromDb(nameBean, gagMsgItem.getSenderuin(), mainItem.getFrienduin())) {//不是超级管理，那么就是普通管理进入了此逻辑分支。
                LogUtil.writeLog("是当前群管理,无法进行批量操作踢人..");
                continue;
            }
            String nickname = null;
            nickname = NickNameUtils.formatNickname(mainItem.getFrienduin(), itemAt.getUin(), itemAt.getNickname());

            if (TextUtils.isEmpty(nickname)) {
                nickname = "" + i + "号";//理论不可能昵称为空
            }
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(gagMsgItem.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);
            if (i != itemList.size() - 1) {
                sb.append("、");

            }
            gagCount++;

            if (i < 5 && RemoteService.isIsInit()) {
                String s = RemoteService.gagUser(gagMsgItem.getFrienduin(), gagMsgItem.getSenderuin(), gagTime);
                if (s != null) {
                    sb.append("结果:" + s);
                } else {
                    MsgReCallUtil.notifyGadPersonMsgNoJump(contentProvider, gagTime, gagMsgItem);
                }
            } else {
                MsgReCallUtil.notifyGadPersonMsgNoJump(contentProvider, gagTime, gagMsgItem);
            }
        }

        if (gagCount == 0) {
            return false;
        }
        String msg;
        boolean piliang = atBeanList != null && atBeanList.size() > 1;
        if (gagTime <= 0) {
            if (piliang) {

                msg = AppConstants.ACTION_OPERA_MULTI_NAME + "批除" + sb.toString() + "的禁言,操作总人数:" + gagCount + "人";
            } else {

                msg = AppConstants.ACTION_OPERA_NAME + "解除" + sb.toString() + "禁言\n";
            }


        } else {
            if (piliang) {
                msg = AppConstants.ACTION_OPERA_MULTI_NAME + "禁言" + sb.toString() + "禁言时间" + DateUtils.getGagTime(gagTime) + ",操作总人数:" + gagCount;

            } else {

                msg = AppConstants.ACTION_OPERA_NAME + "禁言:" + sb.toString() + "\n禁言时间" + DateUtils.getGagTime(gagTime);
            }
        }


        //设置结果在群里触发
        mainItem.setFrienduin(group);
        mainItem.setIstroop(1);
        if (ConfigUtils.isDisableAtFunction(contentProvider)) {
            MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(contentProvider, msg, mainItem);
        } else {

            MsgReCallUtil.notifyAtMsgJumpB(contentProvider, msg, atBeanList, mainItem);
        }
        return true;
    }


    public static boolean doMultiFloorEachLogicFromAt(RobotContentProvider contentProvider, MsgItem mainItem, List<GroupAtBean> itemList, String group, IMultiEachCallBack<MsgItem> iNotifyAction) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量禁言,无数据", mainItem);
            return false;
        }
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        int gagCount = 0;
        for (int i = 0; i < itemList.size(); i++) {
            GroupAtBean itemAt = itemList.get(i);
            MsgItem gagMsgItem = new MsgItem();
            gagMsgItem.setIstroop(1);
            gagMsgItem.setNickname(itemAt.getNickname());
            gagMsgItem.setSelfuin(mainItem.getSelfuin());
            gagMsgItem.setSenderuin(itemAt.getUin());
            gagMsgItem.setFrienduin(group);
            gagMsgItem.setMessage("");

            if (iNotifyAction.onEachDoAndisIgnore(gagMsgItem)) {
                continue;
            }
            String nickname = null;
            nickname = NickNameUtils.formatNickname(mainItem.getFrienduin(), itemAt.getUin(), itemAt.getNickname());

            if (TextUtils.isEmpty(nickname)) {
                nickname = "" + i + "号";//理论不可能昵称为空
            }
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(gagMsgItem.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);
            if (i != itemList.size() - 1) {
                sb.append("、");

            }
            gagCount++;
        }

        if (gagCount == 0) {
            iNotifyAction.onFailEnd();
            return false;
        }

        iNotifyAction.onEnd(atBeanList, sb.toString());

        return true;
    }

    public static boolean doMultiFloorEachLogic(RobotContentProvider contentProvider, MsgItem mainItem, List<MsgItem> itemList, String group, IMultiEachCallBack<MsgItem> iNotifyAction) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量禁言,无数据", mainItem);
            return false;
        }
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        int gagCount = 0;
        for (int i = 0; i < itemList.size(); i++) {
            MsgItem itemAt = itemList.get(i);
            MsgItem gagMsgItem = new MsgItem();
            gagMsgItem.setIstroop(1);
            gagMsgItem.setNickname(itemAt.getNickname());
            gagMsgItem.setSelfuin(mainItem.getSelfuin());
            gagMsgItem.setSenderuin(itemAt.getSenderuin());
            gagMsgItem.setFrienduin(group);

            if (iNotifyAction.onEachDoAndisIgnore(gagMsgItem)) {
                continue;
            }
            iNotifyAction.onEachDoAndisIgnore(gagMsgItem);
            String nickname = null;
            nickname = NickNameUtils.formatNickname(mainItem.getFrienduin(), itemAt.getSenderuin(), itemAt.getNickname());

            if (TextUtils.isEmpty(nickname)) {
                nickname = "" + i + "号";//理论不可能昵称为空
            }
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(gagMsgItem.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);
            if (i != itemList.size() - 1) {
                sb.append("、");

            }
            gagCount++;
        }

        if (gagCount == 0) {
            iNotifyAction.onFailEnd();
            return false;
        }
        iNotifyAction.onEnd(atBeanList, sb.toString());

        return true;
    }

    public interface IMultiEachCallBack<T> {

        boolean onEachDoAndisIgnore(T bean);

        void onEnd(List<AtBean> atBeanList, String info);

        void onFailEnd();
    }

    public static void doMultiKickLogic(RobotContentProvider contentProvider, boolean isManager, GroupWhiteNameBean nameBean, MsgItem mainItem, List<MsgItem> itemList, String group, boolean forver) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量踢出,无数据", mainItem);
            return;
        }

        StringBuffer sb = new StringBuffer();

        for (MsgItem item : itemList) {


            MsgItem kickItem = item;
            kickItem.setIstroop(1);

//            Md5
            kickItem.setFrienduin(group);

            if (contentProvider.isManager(kickItem)) {
                continue;
            } else if (MsgTyeUtils.isSelfMsg(kickItem)) {
                continue;
            } else if (isManager == false && RobotContentProvider.getInstance().isCurrentGroupAdminFromDb(nameBean, kickItem.getSenderuin(), mainItem.getFrienduin())) {//不是超级管理，那么就是普通管理进入了此逻辑分支。
                LogUtil.writeLog("是当前群管理,无法进行批量操作踢人..");
                continue;
            }
            sb.append(NickNameUtils.formatNickname(item));
            MsgReCallUtil.notifyKickPersonMsgNoJump(contentProvider, kickItem, forver);
        }

        MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "请求踢出" + sb.toString() + ",是否永久" + forver, mainItem);
    }


    public static boolean doMultiKickLogicByAt(RobotContentProvider contentProvider, boolean isManager, GroupWhiteNameBean nameBean, MsgItem mainItem, List<GroupAtBean> itemList, String group, boolean forver) {
        if (itemList == null) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "无法批量踢出,无数据", mainItem);
            return false;
        }

        StringBuffer sb = new StringBuffer();

        int count = 0;
        for (GroupAtBean atBean : itemList) {

            MsgItem kickItem = new MsgItem();
            kickItem.setIstroop(1);
            kickItem.setSelfuin(mainItem.getSelfuin());
            kickItem.setSenderuin(atBean.getUin());
            kickItem.setFrienduin(mainItem.getFrienduin());
            kickItem.setNickname(atBean.getNickname());

            kickItem.setFrienduin(group);

            if (contentProvider.isManager(kickItem)) {
                continue;
            } else if (MsgTyeUtils.isSelfMsg(kickItem)) {
                continue;
            } else if (nameBean == null || (isManager == false && RobotContentProvider.getInstance().isCurrentGroupAdminFromDb(nameBean, kickItem.getSenderuin(), mainItem.getFrienduin()))) {
                LogUtil.writeLog("是当前群管理,无法进行批量操作踢人..");
                continue;
            }
            sb.append(NickNameUtils.formatNickname(kickItem));
            MsgReCallUtil.notifyKickPersonMsgNoJump(contentProvider, kickItem, forver);
            count++;

        }

        if (count == 0) {
            return false;
        }
        MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, "请求踢出" + sb.toString() + ",是否永久" + forver, mainItem);
        return true;
    }

}
