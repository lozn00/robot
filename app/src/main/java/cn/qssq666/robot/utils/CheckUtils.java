package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;
import android.net.Uri;
import android.util.Pair;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.DoWhileMsg;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.FloorUtils;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.business.MsgTyeUtils;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.business.TaskUtils;
import cn.qssq666.robot.business.ViolationRecordUtil;
import cn.qssq666.robot.business.ViolationWordHistoryRecordUtil;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.config.MemoryIGnoreConfig;
import cn.qssq666.robot.constants.DefaultTipUtil;
import cn.qssq666.robot.interfaces.INeedReplayLevel;

/**
 * Created by qssq on 2017/12/6 qssq666@foxmail.com
 */

public class CheckUtils {
    static {
        LogUtil.importPackage();


    }

    /**
     * 检查是否超过次数
     *
     * @param dbUtils
     * @param whiteNameBean
     * @param item
     * @param type
     * @param msg
     * @return
     * @aram provider
     */
    public static int doCheckGagCountThan(DBUtils dbUtils, RobotContentProvider provider, GroupWhiteNameBean whiteNameBean, MsgItem item, int type, String msg) {

        if (!whiteNameBean.isAccumlativegagdata()) {
            //LogUtil.writeLog(String.format("群%s 已设置不记录违规  忽略 惩罚 type %d  msg:%s", item.getFrienduin(), type, msg));
            return 0;
        }


        if (whiteNameBean.isOnlyrecordwordgagcount() && type != ViolationType.SEND_TEXT) {
            //LogUtil.writeLog(String.format("只记录发文字内容违规 忽略 群%s其他违规 type %d,msg %s", type, msg));
            return 0;
        } else {

            if (whiteNameBean.getMistakecount() <= 0) {
                //LogUtil.writeLog(String.format("违规次数上限填写错误 "));
                return 0;
            }

            int violationCount = ViolationRecordUtil.getViolationCount(dbUtils, item.getFrienduin(), item.getSenderuin());
            if ((violationCount + 1) >= whiteNameBean.getMistakecount()) {
                //LogUtil.writeLog(String.format("用户违规超出 历史 次数 %d ，上线次数::%d ", violationCount, whiteNameBean.getMistakecount()));
                MsgItem cloneItem = item.clone();
//                MsgReCallUtil.notifyKickPersonMsgNoJump(provider, cloneItem, false);
                MsgReCallUtil.notifyGadPersonMsgNoJump(provider, 60*150,cloneItem.clone());
                TaskUtils.joinTask("违规",cloneItem,TaskUtils.TaskType.KICK,1000*150,1+"");
                MsgItem cloneTip = item.clone();
                MsgReCallUtil.notifyJoinMsgNoJump(provider, String.format("" + whiteNameBean.getCountthantip(), violationCount + 1, whiteNameBean.getMistakecount(), whiteNameBean.getMistakecount()), cloneTip);

            } else if ((violationCount) >= whiteNameBean.getMistakethanwarncount()) {
//            } else if ((violationCount + 5) >= whiteNameBean.getMistakecount()) {
                //LogUtil.writeLog(String.format("用户违规超出 历史 次数 即将超出 已记录 %d ，上线次数::%d ", violationCount, whiteNameBean.getMistakecount()));
                MsgItem cloneTip = item.clone();
                MsgReCallUtil.notifyJoinMsgNoJump(provider, String.format("你已违规%d次,请务必小心发言,遵守群规! 当达到%d次违规后将直接踢出哈", violationCount + 1, whiteNameBean.getMistakecount()), cloneTip);

            } else {


            }

            int result = ViolationRecordUtil.addViolationCount(dbUtils, item.getFrienduin(), item.getSenderuin());
            //LogUtil.writeLog(String.format("用户违规总数插入或者更新结果:%d ", result));
            int result1 = ViolationWordHistoryRecordUtil.addRecord(dbUtils, item.getFrienduin(), item.getSenderuin(), msg);


            //LogUtil.writeLog(String.format("用户历史违规插入结果:%d ", result1));

            return violationCount + 1;

        }
    }

    public interface ViolationType {
        int SEND_TEXT = 1;
        int VOICE = 2;
        int VIDEO = 3;
        int PIC = 4;
        int REDPACKET = 5;
        int NICKNAME_FORMAT = 6;
        //昵称包含敏感词
        int NICKNAME_Violation = 7;
        int OTHER_Violation = 8;

        int frequent_Violation = 10;
    }

    /**
     * 用于判断是否需要回复,但是 楼层记录则 所有群记录,而 刷屏 则只在设定的情况生效.
     *
     * @param
     * @param contentProvider
     * @param isgroupMsg
     * @return Pair first如果不为空就直接返回
     */

    public static DoWhileMsg checkGroupMsg(RobotContentProvider contentProvider, DBUtils dbUtils, MsgItem item, boolean isgroupMsg) {


        if (MemoryIGnoreConfig.isIgnoreGroupNo(item.getFrienduin())) {
            Pair<Uri, Integer> uriIntegerPair = Pair.create(RobotContentProvider.getFailUri("忽略群号（被临时屏蔽）" + item.getFrienduin()), INeedReplayLevel.INTERCEPT_ALL);
            return new DoWhileMsg(uriIntegerPair);
        }

        if (dbUtils != null) {

            if (isgroupMsg) {
                FloorUtils.onReceiveNewMsg(dbUtils, item);
            }
        }

        if (!contentProvider.mCfeanbleGroupReply) {//mei
            //LogUtil.writeLog("群消息开关未打开,不处理" + item);
            return new DoWhileMsg(Pair.create(contentProvider.getFailUri("群消息开关未打开"), INeedReplayLevel.INTERCEPT_ALL));
        } else if (contentProvider.mCfOnlyReplyWhiteNameGroup) {//如果开启了白名单
            GroupWhiteNameBean account = AccountUtil.findAccount(contentProvider.mQQGroupWhiteNames, item.getFrienduin(), true);
            if (account != null) {
                //不在白名单 忽略但是如果白名单忽略 开启了 且不在黑名单

                //如果不子啊白名单 但是   开启只回复白名单模式群后非白名单群艾特我也可以回复

                if (ConfigUtils.replyNeedAt(contentProvider)) {

                    if (ConfigUtils.hasAtRobotAndClearSelf(contentProvider, item)) {
                        //LogUtil.writeLog("白名单模式,艾特合格 且不在忽略模式 " + item.toSimpleString());
                    } else {
                        //LogUtil.writeLog("白名单模式,开启了白名单艾特回复功能,但是并没有艾特!" + item.toSimpleString());
                    }

                } else {

                    return new DoWhileMsg(Pair.create(contentProvider.getFailUri("逻辑通过 群配置存在此信息!如果看不到消息请检查" + DefaultTipUtil.INSTANCE.getHostNameAndTip()), INeedReplayLevel.ANY)).setWhiteNameBean(account);
                }
                //对于白名胆群 还需要检测一个东西，那就是刷屏，所以替换问题似乎有点矛盾

//                        if (mCfNotWhiteNameReplyIfAite && item.getMessage().contains(getRobotName()) && !contentsContaineKey(mCfNotReplyGroupStr, item.getFrienduin())) {
//                            item.setMessage(item.getMessage().replaceAll(getRobotName(), ""));
//                            RobotUtil.writeLog("白名单模式,艾特合格 且不在忽略模式");
//                        } else {
//                            doMsgLogic = false;
//                        }
//                    doMsg=true;

                //LogUtil.writeLog("开启白名单模式," + item.getFrienduin() + "不在白名单忽略," + item);
            } else {


                String msg = "群聊需要订阅群,请进入【配置群】界面添加群" + item.getFrienduin() + " 消息体:" + item.toSimpleString();
           /*     //LogUtil.writeLog(msg);
                account = new GroupWhiteNameBean();
                account.setAccount(item.getFrienduin());
                account.setDisable(true);
                */
                return new DoWhileMsg(Pair.create(contentProvider.getFailUri(msg), INeedReplayLevel.INTERCEPT_ALL));

              /*  if (ConfigUtils.replyNeedAt(contentProvider) && contentProvider.mCfBaseWhiteNameReplyNotNeedAite == false) {//在白名单但是白名单也需要艾特
                    if (ConfigUtils.hasAtRobotAndClearSelf(contentProvider, item)) {
                        RobotUtil.writeLog("在白名单，也艾特了 " + item.toSimpleString());
                        doMsg = true;

                    } else {
                        RobotUtil.writeLog("在白名单但是没有艾特" + item.getFrienduin() + item.toSimpleString());
                        doMsg = false;
                    }


                }
*/
            }


        } else if (contentProvider.mCfNotReplyGroup && RobotUtil.contentsContaineKey(contentProvider.mCfNotReplyGroupStr, item.getFrienduin())) {//黑名单模式 判断是否在 如果在则忽略

            //LogUtil.writeLog("忽略了群黑名单" + item.getFrienduin() + "," + item);
        } else {

            if (ConfigUtils.replyNeedAt(contentProvider) && !ConfigUtils.hasAtRobotAndClearSelf(contentProvider, item)) {
                //LogUtil.writeLog("非白费黑名单单但是没有开启了艾特模式 并没有艾特:" + item.getFrienduin());

                return new DoWhileMsg(Pair.create(contentProvider.getFailUri("沒有艾特"), INeedReplayLevel.LEVEL_NOT_AITE));
            } else {
                Pair<Uri, Integer> objectIntegerPair = Pair.create(contentProvider.getSuccUri(""), INeedReplayLevel.ANY);
                return new DoWhileMsg(objectIntegerPair).setWhiteNameBean(AccountUtil.createGroupWhiteNameBeanFrom(contentProvider, item.getSenderuin()));
//                RobotUtil.writeLog("非白费黑名单单但是没有开启了艾特模式 有艾特:" + item.getFrienduin());
            }
        }
        return new DoWhileMsg(Pair.create(contentProvider.getFailUri("逻辑通过（建议开启群白名单模式并进入群白名单管理界面添加群白名单,未开启状态会出现一些毛病）"), INeedReplayLevel.ANY));

    }

    public static Uri doIsNeedRefreshGag(RobotContentProvider contentProvider, String frequentMessage, MsgItem item, GroupWhiteNameBean whiteNameBean) {

        if (frequentMessage != null) {

            if (MsgTyeUtils.isGroupMsg(item) && !contentProvider.isManager(item)) {
                IGnoreConfig.tempNoDrawPerson = item.getSenderuin();
                contentProvider.getHandler().removeCallbacks(contentProvider.getTempRunnable());
                contentProvider.getHandler().postDelayed(contentProvider.getTempRunnable(), IGnoreConfig.SHUAPIN_TIME_DURATION);
                if (MsgTyeUtils.isGroupMsg(item) && !contentProvider.isManager(item)) {
                    MsgItem gagItem = item.clone();
                    long gagtime = whiteNameBean == null ? IGnoreConfig.SHUAPIN_GAG_SECOND : whiteNameBean.getFrequentmsggagtime() * 60;
                    MsgReCallUtil.notifyGadPersonMsgNoJump(contentProvider, gagtime, gagItem);
                    MsgReCallUtil.notifyRevokeMsgJumpByCount(contentProvider,item.getFrienduin(),item.getSenderuin(),100,item);
                    MsgReCallUtil.notifyJoinReplaceMsgJump(contentProvider, NickNameUtils.formatNickname(gagItem) + "存在恶意刷屏可疑性,强制禁言", item);

                }
                //EncyptUtil.HOOKLog("恶意刷屏" + QQConfig.finalErr);
                String errMsg = "发现" + item.getSenderuin() + "恶意刷屏,已添加到屏蔽列表,是否是群消息:" + MsgTyeUtils.isGroupMsg(item);
                //LogUtil.writeLog(errMsg);
                return contentProvider.getFailUri(errMsg);
            }


        }
        return null;
    }
}
