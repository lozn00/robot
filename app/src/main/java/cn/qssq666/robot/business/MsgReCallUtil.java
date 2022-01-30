package cn.qssq666.robot.business;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.qssq666.robot.bean.AtBean;
import cn.qssq666.robot.bean.GroupConfig;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.constants.ControlCode;
import cn.qssq666.robot.constants.IPluginRequestCall;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.selfplugin.IPluginHolder;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.NickNameUtils;
import cn.qssq666.robot.utils.RobotUtil;
import cn.qssq666.robot.xbean.MusicCardInfo;

/**
 * Created by qssq on 2018/1/24 qssq666@foxmail.com
 */

public class MsgReCallUtil {
    private static String mLastMessage;

    /**
     * @param contentProvider
     * @param message
     * @param postFix         白名单单独设置群尾巴的时候
     * @param msgItem
     */
    public static void notifyHasDoWhileReply(RobotContentProvider contentProvider, String message, String postFix, IMsgModel msgItem) {
        notifyHasDoWhileReply(contentProvider, message + postFix, msgItem);
    }

    public static void notifyHasDoWhileReply(RobotContentProvider contentProvider, String message, IMsgModel msgItem) {

        msgItem.setMessage(message);
        notifyHasDoWhileReply(contentProvider, msgItem);
    }

    public static void notifyHasDoWhileReply(RobotContentProvider contentProvider, IMsgModel msgItem) {

        if (msgItem.getCode() >= 0 && MsgTyeUtils.isGroupMsg(msgItem) && IGnoreConfig.groupMsgLessSecondIgnore > 0) {
            GroupConfig currentGroupConfig = IGnoreConfig.getCurrentGroupConfig(msgItem);
            if (currentGroupConfig != null) {
                long currentTime = new Date().getTime();
                long timeDistance = DateUtils.getTimeDistance(DateUtils.TYPE_MS, currentTime, currentGroupConfig.getTime());
                if (timeDistance < IGnoreConfig.groupMsgLessSecondIgnore) {
                    //LogUtil.writeLog("发小重复群回复 ,小于间隔 因此忽略，设置的间隔时间" + IGnoreConfig.groupMsgLessSecondIgnore + "毫秒,当前间隔" + timeDistance + "秒来自群" + msgItem.getFrienduin() + "消息:" + msgItem.getMessage());
                    return;

                }
            } else {

                //LogUtil.writeLog("已开启重复群回复指定间隔忽略,当前是第一次收到本消息" + msgItem.getMessage() + "群" + msgItem.getFrienduin());
            }
        } else {
            //未开启
        }
//        Log.w(TAG,"通知QQ处理消息:"+msgItem);
        notifyJoinMsgNoJump(contentProvider, msgItem);
    }

    public static void notifyJoinReplaceMsgJump(RobotContentProvider contentProvider, String msg, IMsgModel msgItem) {
        msgItem.setCode(ControlCode.SUCC);
        notifyJoinMsgNoJump(contentProvider, "" + msg, msgItem);
    }

    public static void notifyAtMsgJump(RobotContentProvider contentProvider, String qq, String nickname, String msg, IMsgModel msgItem) {
        notifyAtMsgJump(contentProvider, qq, nickname, msg, msgItem, false);
    }

    public static void notifyAtMsgJump(RobotContentProvider contentProvider, String qq, String nickname, String msg, IMsgModel msgItem, boolean fromPluginSend) {


        if (contentProvider.isDisableAtFunction()) {
            notifyJoinMsgNoJumpDisableAt(contentProvider, msg, msgItem);
            return;

        }


        if (!fromPluginSend && RobotContentProvider.getInstance().mCFBaseEnablePlugin && RobotContentProvider.getInstance().mAllowPluginInterceptEndMsg) {

            msgItem.setMessage(msg);//修复模仿说话的问题。

            Pair<IPluginHolder, Boolean> iPluginHolderBooleanPair = RobotContentProvider.getInstance().doPluginLogic(msgItem, RobotContentProvider.getInstance().mLuaPluginList,
                    null, IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END);

            if (iPluginHolderBooleanPair.second) {


                IMsgModel iMsgModel = msgItem.setMessage(msgItem.getMessage() + "\n收尾被插件[" + iPluginHolderBooleanPair.first.getPluginInterface().getPluginName() + "]Lua插件拦截！！【死循环,拦截后请修改成其它类型消息然后传递,否则死循环!!qq153016267】");
                iMsgModel.setNickname("收尾拦截");
                return;
            }

            iPluginHolderBooleanPair = RobotContentProvider.getInstance().doPluginLogic(msgItem, RobotContentProvider.getInstance().mPluginList, null, IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END)
            ;

            if (iPluginHolderBooleanPair.second) {


                IMsgModel iMsgModel = msgItem.setMessage(msgItem.getMessage() + "\n收尾被插件[" + iPluginHolderBooleanPair.first.getPluginInterface().getPluginName() + "]Lua插件拦截！！【死循环,拦截后请修改成其它类型消息然后传递,否则死循环!!qq153016267】");
                iMsgModel.setNickname("收尾拦截");
                return;
            }

        }


        //LogUtil.writeLog((msgItem.getCode() < 0 ? "[发送警告给宿主(艾特):]" : "[请求发送消息()]:") + msgItem.toString());
        msgItem.setCode(ControlCode.AITE);
//        msgItem.getIstroop()
        AtBean atbean = new AtBean();

        if (nickname != null && !msg.contains("@") && !msg.contains(nickname)) {
            atbean.setNickname("@" + nickname);
            msg = msg.replace(nickname, atbean.getNickname());
            msgItem.setMessage(msg);
        } else {
            atbean.setNickname("" + nickname);
        }


        atbean.setMsg(msg);
        atbean.setSenderuin(qq);
        msgItem.setMessage(JSON.toJSONString(atbean));
        adjectSenderUin(msgItem);
//        msgItem.setSenderuin(msgItem.getSelfuin());//由于是机器人自身发的，所以这个消息要防止再次回流，所以把这个消息的发送者标示为自己就可以解决这个问题
        contentProvider.notifyChange(replyFinalWrap(contentProvider, msgItem), null);

    }

    public static void notifyAtMsgJumpB(RobotContentProvider contentProvider, List<? extends AtBean> atBeanList, IMsgModel msgItem) {

        notifyAtMsgJumpB(contentProvider, atBeanList, msgItem);
        ;
    }

    public static void notifyAtMsgJumpB(RobotContentProvider contentProvider, String msg, List<? extends AtBean> atBeanList, IMsgModel msgItem) {
        if (contentProvider.isDisableAtFunction()) {
            notifyJoinMsgNoJump(contentProvider, "艾特功能被禁用，无法进行批量艾特,", msgItem);
            return;

        }
        msgItem.setCode(ControlCode.AITE);
        msgItem.setExtstr(msg);
        String atListJson = JSON.toJSONString(atBeanList);
        msgItem.setMessage(atListJson);
        //LogUtil.writeLog("[发送批量艾特给宿主]" + msgItem.toString());
        contentProvider.notifyChange(replyFinalWrap(contentProvider, msgItem), null);

    }

    public static void notifyJoinMsgNoJump(RobotContentProvider contentProvider, String message, IMsgModel msgItem) {

        msgItem.setMessage(message);
        notifyJoinMsgNoJump(contentProvider, msgItem);
    }

    public static void notifyJoinMsgNoJumpDisableAt(RobotContentProvider contentProvider, String message, IMsgModel msgItem) {
        notifyJoinMsgNoJumpDisableAt(contentProvider, message, msgItem, false);
    }

    public static void notifyJoinMsgNoJumpDisableAt(RobotContentProvider contentProvider, String message, IMsgModel msgItem, boolean fromPluginLoad) {
        msgItem.setMessage(message);
        notifyJoinMsgNoJumpDisableAt(contentProvider, msgItem, fromPluginLoad);
    }

    public static void notifyJoinMsgNoJumpDisableAt(RobotContentProvider contentProvider, IMsgModel msgItem) {
        notifyJoinMsgNoJumpDisableAt(contentProvider, msgItem, false);
    }

    /**
     * 禁用艾特
     *
     * @param contentProvider
     * @param msgItem
     */
    public static void notifyJoinMsgNoJumpDisableAt(RobotContentProvider contentProvider, IMsgModel msgItem, boolean fromPluginLoad) {


        {

            if (!fromPluginLoad && RobotContentProvider.getInstance().mCFBaseEnablePlugin && RobotContentProvider.getInstance().mAllowPluginInterceptEndMsg) {

                Pair<IPluginHolder, Boolean> iPluginHolderBooleanPair = RobotContentProvider.getInstance().doPluginLogic(msgItem, RobotContentProvider.getInstance().mJSPluginList, null, IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END);
                if (iPluginHolderBooleanPair.second) {


                    IMsgModel iMsgModel = msgItem.setMessage(msgItem.getMessage() + "\n收尾被插件[" + iPluginHolderBooleanPair.first.getPluginInterface().getPluginName() + "]Lua插件拦截！！【死循环,拦截后请修改成其它类型消息然后传递,否则死循环!!qq153016267】");
                    iMsgModel.setNickname("收尾拦截");
                    return;
                }


                iPluginHolderBooleanPair = RobotContentProvider.getInstance().doPluginLogic(msgItem, RobotContentProvider.getInstance().mLuaPluginList, null, IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END);
                if (iPluginHolderBooleanPair.second) {


                    IMsgModel iMsgModel = msgItem.setMessage(msgItem.getMessage() + "\n收尾被插件[" + iPluginHolderBooleanPair.first.getPluginInterface().getPluginName() + "]Lua插件拦截！！【死循环,拦截后请修改成其它类型消息然后传递,否则死循环!!qq153016267】");
                    iMsgModel.setNickname("收尾拦截");
                    return;
                }
                iPluginHolderBooleanPair = RobotContentProvider.getInstance().doPluginLogic(msgItem, RobotContentProvider.getInstance().mPluginList, null, IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END);
                if (iPluginHolderBooleanPair.second) {


                    IMsgModel iMsgModel = msgItem.setMessage(msgItem.getMessage() + "\n收尾被插件[" + iPluginHolderBooleanPair.first.getPluginInterface().getPluginName() + "]java插件拦截！！【死循环,拦截后请修改成其它类型消息然后传递,否则死循环!!qq153016267】");
                    iMsgModel.setNickname("收尾拦截");
                    return;
                }

            }

            //LogUtil.writeLog((msgItem.getCode() < 0 ? "[发送警告给宿主:]" : "[请求发送消息]:") + msgItem.toString());
//            msgItem.setSenderuin(msgItem.getSelfuin());//文本消息都应该把发发送者改成自己，而禁言和踢人是用来接收参数 ，只有发言消息需要这么做 因为这种消息会回流自身

            adjectSenderUin(msgItem);
            contentProvider.notifyChange(replyFinalWrap(contentProvider, msgItem), null);

        }
    }

    public static void notifyJoinMsgNoJump(RobotContentProvider contentProvider, IMsgModel msgItem) {
        notifyJoinMsgNoJump(contentProvider, msgItem, false);
    }

    public static void notifyJoinMsgNoJump(RobotContentProvider contentProvider, IMsgModel msgItem, boolean fromPluginSend) {
        if (msgItem.getIstroop() == 1 && contentProvider.replyShowNickname() && contentProvider.isDisableAtFunction() == false) {
            notifyAtMsgJump(contentProvider, msgItem.getSenderuin(), msgItem.getNickname(), msgItem.getMessage(), msgItem, fromPluginSend);//不得死循环

        } else {
            notifyJoinMsgNoJumpDisableAt(contentProvider, msgItem, fromPluginSend);

        }
    }

    /**
     * @param contentProvider
     * @param gagDuration     秒
     * @param msgItem
     */
    public static void notifyGadPersonMsgNoJump(RobotContentProvider contentProvider, String frienduin, String senderuin, long gagDuration, IMsgModel msgItem) {
        IMsgModel clone = msgItem.clone();
        clone.setFrienduin(frienduin);
        clone.setSenderuin(senderuin);
        notifyGadPersonMsgNoJump(contentProvider, gagDuration, clone);


    }

    public static void notifyGadPersonMsgNoJump(RobotContentProvider contentProvider, long gagDuration, IMsgModel msgItem) {
        if (contentProvider.isDisableSuperFunction()) {
            return;
        }
        if (contentProvider.mCfBaseDisableGag) {
            //LogUtil.writeLog("禁言功能被禁用");
            return;
        }
        msgItem.setCode(ControlCode.GAG);
        msgItem.setMessage(gagDuration + "");
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());


        if (RemoteService.isIsInit()) {
            String s = RemoteService.gagUser(msgItem.getFrienduin(), msgItem.getSenderuin(), gagDuration);
            if (s != null) {
                contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
            }
        } else {
            contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
        }
    }


    public static void notifyRequestExitGroupJump(RobotContentProvider contentProvider, IMsgModel msgItem, String group) {
        if (contentProvider.isDisableSuperFunction()) {
            return;
        }

        msgItem.setFrienduin(group);
        msgItem.setCode(ControlCode.QUIT_GROUP);
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }

    public static void notifyRequestExitDiscussionJump(RobotContentProvider contentProvider, IMsgModel msgItem, String group) {
        if (contentProvider.isDisableSuperFunction()) {
            return;
        }

        msgItem.setFrienduin(group);
        msgItem.setCode(ControlCode.QUIT_DISCUSSION);
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }

    public static void notifySendPicMsg(RobotContentProvider contentProvider, String path, IMsgModel msgItem) {
        if (contentProvider.isDisableSuperFunction()) {
            return;
        }
        msgItem.setCode(ControlCode.PIC);
        msgItem.setMessage(path + "");
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }

    public static void notifySendVoiceCall(RobotContentProvider contentProvider, String qq, IMsgModel msgItem) {
        msgItem.setCode(ControlCode.VOICE_CALL);
        msgItem.setMessage(qq + "");
        msgItem.setSenderuin(qq);
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }


    public static void notifyUniversalMsg(RobotContentProvider contentProvider, int type, String message, IMsgModel msgItem) {
        notifyUniversalMsg(contentProvider, type, message, msgItem, false);
    }

    public static void notifyUniversalMsg(RobotContentProvider contentProvider, int type, String message, IMsgModel msgItem, boolean fromPluginLoad) {
        if (contentProvider.isDisableSuperFunction()) {
            return;
        }

        msgItem.setCode(type);
        msgItem.setMessage(message + "");
        //LogUtil.writeLog("[发送禁言请求给宿主:]禁言时间:" + gagDuration + ",所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + "," + msgItem.getNickname() + ",禁言分钟" + msgItem.getMessage());
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }


    public static Uri replyFinalWrap(RobotContentProvider provider, IMsgModel msgItem) {
        if (mLastMessage != null && msgItem.getMessage().equals(mLastMessage)) {
            if (msgItem.getSelfuin().equals(msgItem.getSenderuin())) {

                    msgItem.setMessage("");

                    if (msgItem.getIstroop() == 1&&msgItem.getSenderuin().equals(msgItem.getSelfuin())) {
                        msgItem.setSenderuin("" + new Random().nextInt(15000));
                        msgItem.setType(-1006);//消息忽略
                    }
                    msgItem.setMessage((new Random().nextInt(15000)) + "["+msgItem.getSenderuin()+":重复消息]:" + mLastMessage + "");
            }
        }
        mLastMessage = msgItem.getMessage();
        return RobotUtil.msgItemToUri(msgItem, msgItem.getMessage());
    }

    public static void notifyKickPersonMsgNoJump(RobotContentProvider contentProvider, IMsgModel msgItem, boolean forverkick) {
        if (contentProvider.mStopUseAdvanceFunc) {
            return;
        }
        msgItem.setCode(ControlCode.KICK);
        msgItem.setMessage(forverkick + "");
        //LogUtil.writeLog("[发送踢人请求给宿主:]所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + ",是否永久踢出：" + forverkick);
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }

    public static void notifMusicCardJump(RobotContentProvider contentProvider, IMsgModel msgItem, MusicCardInfo info) {

        notifMusicCardJump(contentProvider, msgItem, info.toString());
    }

    public static void notifMusicCardJump(RobotContentProvider contentProvider, IMsgModel msgItem, String xmlInfo) {


        if (contentProvider.mStopUseAdvanceFunc) {
            return;
        }
        if (contentProvider.mCfBaseDisableGag) {
            //LogUtil.writeLog("点歌功能被禁用");
            return;
        }

        msgItem.setCode(ControlCode.StrucMSG);
        msgItem.setMessage(xmlInfo + "");
        adjectSenderUin(msgItem);

        //LogUtil.writeLog("[发送结构体消息给宿主:]所在群:" + msgItem.getFrienduin() + ",QQ:" + msgItem.getSenderuin() + ",结构体：" + xmlInfo);
        contentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }

    public static void adjectSenderUin(IMsgModel msgItem) {
        if (!msgItem.getSenderuin().equals(msgItem.getSelfuin())) {
            msgItem.setSenderuin(msgItem.getSenderuin());
        }
    }


    public static void notifyNotManagerMsg(RobotContentProvider contentProvider, IMsgModel item) {
        notifyHasDoWhileReply(contentProvider, "你不是管理员,无权限操作此命令", item);
    }

    static {
        LogUtil.importPackage();
    }

    public static void notifyTest(RobotContentProvider robotContentProvider, boolean isgroupMsg, String msg) {
        IMsgModel msgItem = new MsgItem();
        msgItem.setCode(ControlCode.TEST);
        msgItem.setMessage(msg + "");
        msgItem.setIstroop(isgroupMsg ? 1 : 0);
        msgItem.setSenderuin("153016267");
        msgItem.setFrienduin(isgroupMsg ? "153016267" : "1000000");
        msgItem.setFrienduin(msgItem.getSenderuin());
        robotContentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }


    public static void notifyRequestModifyName(RobotContentProvider robotContentProvider, MsgItem clone, String text) {
        IMsgModel msgItem = clone;
        msgItem.setCode(ControlCode.MODIFY_GROUP_MEMBER_CARD_NAME);
        msgItem.setMessage(text + "");
        robotContentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }


    public static void notifyZanPerson(RobotContentProvider robotContentProvider, MsgItem clone, String qq, int zancount) {
        IMsgModel msgItem = clone;
        msgItem.setCode(ControlCode.ADD_LIKE);
        msgItem.setSenderuin(qq);
        msgItem.setMessage(zancount + ",1");//忽略点赞检查！
        robotContentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }


    public static void smartReplyMsg(String msssage, boolean isgroupMsg, GroupWhiteNameBean nameBean, MsgItem item) {

        if (nameBean != null && isgroupMsg) {
            String nickname;
            if (nameBean.isReplayatperson()) {

                nickname = NickNameUtils.formatNicknameFromNickName(item.getSenderuin(), item.getNickname());
            } else {
                nickname = item.getNickname();
                if (item.getSenderuin().equals(nickname)) {
                    item.setNickname("qq" + nickname + "");
                }
            }


            notifyAtMsgJump(RobotContentProvider.getInstance(), item.getSenderuin(), nickname, msssage, item, false);
        } else {
            notifyHasDoWhileReply(RobotContentProvider.getInstance(), item.setMessage(msssage));
        }
    }

    /**
     * 根据ID撤回消息
     *
     * @param robotContentProvider
     * @param group
     * @param qq
     * @param messageID
     * @param item
     */
    public static void notifyRevokeMsgJump(RobotContentProvider robotContentProvider, String group, String qq, long messageID, MsgItem item) {
        notifyRevokeMsgJump(robotContentProvider, group, qq, messageID, "", item);
    }

    /**
     * 根据 撤回消息
     *
     * @param robotContentProvider
     * @param group
     * @param qq
     * @param item
     */
    public static void notifyRevokeMsgJumpByCount(RobotContentProvider robotContentProvider, String group, String qq, int revokeCount, MsgItem item) {


        notifyRevokeMsgJump(robotContentProvider, group, qq, 1, revokeCount + "", item);
    }

    /**
     * @param robotContentProvider
     * @param group
     * @param qq
     * @param messageID
     * @param typeData             message  科代表总数
     * @param item
     */
    public static void notifyRevokeMsgJump(RobotContentProvider robotContentProvider, String group, String qq, long messageID, String typeData, MsgItem item) {

        IMsgModel msgItem = item.clone();
        msgItem.setCode(ControlCode.REVOKE_MSG_1);
        msgItem.setSenderuin(qq);
        msgItem.setFrienduin(group);
        msgItem.setMessage(typeData);
        ((MsgItem) msgItem).setMessagID(messageID);//忽略点赞检查
        robotContentProvider.notifyChange(RobotUtil.msgItemToUri(msgItem), null);
    }
}
