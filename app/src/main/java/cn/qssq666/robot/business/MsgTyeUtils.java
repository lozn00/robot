package cn.qssq666.robot.business;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.interfaces.RedPacketMessageType;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;

import static cn.qssq666.robot.constants.MsgTypeConstant.MSG_TYPE_APPLICATION_MSG;
import static cn.qssq666.robot.constants.MsgTypeConstant.MSG_TYPE_REDPACKET_1;
import static cn.qssq666.robot.constants.MsgTypeConstant.MSG_TYPE_SYSTEM_MSG;

/**
 * Created by qssq on 2017/6/4 qssq666@foxmail.com
 */

public class MsgTyeUtils {

    public static boolean isGroupMsg(IMsgModel item) {
        boolean groupMsg = isnQQGroupTextMsg(item) || isPicAndTextMsg(item) || isGroupReplyMsg(item)
                || isGroupPicMsg(item)
                || isDiscussionGroupMsg(item);
        if (!groupMsg) {
            if (item.getIstroop() == 1) {
                //-2002
                if (item.getType() == MsgTypeConstant.MSG_TYPE_REDPACKET
                        || item.getType() == MsgTypeConstant.MSG_TYPE_REDPACKET_1
                        || item.getType() == MsgTypeConstant.MSG_TYPE_REDPACKET_1
                        || item.getType() == MsgTypeConstant.MSG_TYPE_JOIN_GROUP
                        || item.getType() == MsgTypeConstant.MSG_TYPE_STRUCT_MSG
                        || item.getType() == MsgTypeConstant.MSG_TYPE_MEDIA_PTT
                        || item.getType() == MsgTypeConstant.MSG_TYPE_MIX_PIC || isPicMsg(item) || isVideoMsg(item)
                        ) {
                    return true;
                }

            }
        }
        return groupMsg;
    }


    public static boolean isnQQGroupTextMsg(IMsgModel item) {
        return item.getIstroop() == 1 && isTextMsg(item);
    }


    public static boolean isVoiceMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_MEDIA_PTT;

    }

    public static boolean isStructMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_STRUCT_MSG;

    }

    public static boolean isPrivateMsg(IMsgModel item) {
        if (isFriendMsg(item) || isNearbyPersonMsg(item)
                || isGroupPrivateMsg(item)
                || isGroupPrivateMsg1(item)
                || isDiscussionGroupPrivateMsg(item)
                ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isJoinGroupMsg(IMsgModel item) {
        //MsgTypeConstant.MSG_TYPE_JOIN_GROUP == item.getType()
        return MsgTypeConstant.MSG_TYPE_JOIN_GROUP == item.getType() && item.getIstroop() == 1;
        /*
        6.robot W/RobotUtil: [log][请求发送消息]:{昵称='595089230', 群消息类型=1, 发送人='595089230', 朋友='595089230', 消息体='情随事迁 加入了本群                    ##**##1,5,0,4,0,153016267,icon,0,0,color,0(群595089230)', 自己账号='35697438', 消息类型=-1012, 时间=2017:07:24:21:06:33, 状态码=0}
07-24 21:06:34.116 6603-6614/cn.qssq666.robot W/RobotUtil: [log]群消息开关未打开,不处理{昵称='468539550', 群消息类型=1, 发送人='468539550', 朋友='129103467', 消息体='我的有时候闪退之后就红包加密失败怎么回事，只能卸载重装', 自己账号='35697438', 消息类型=-1000, 时间=2017:07:24:21:06:33, 状态码=0}
         */
    }


    public static boolean isPicMsg(IMsgModel item) {
        return
                item.getType() == MsgTypeConstant.MSG_TYPE_MIX_PIC
                        || item.getType() == MsgTypeConstant.MSG_TYPE_MEDIA_PIC
                        || item.getType() == MsgTypeConstant.MSG_TYPE_PIC
                        || item.getType() == MsgTypeConstant.MSG_TYPE_PIC_AND_TEXT_MIXED
                        || item.getType() == MsgTypeConstant.MSG_TYPE_PIC_QSECRETARY
                        || item.getType() == MsgTypeConstant.MSG_TYPE_PIC_WITH_TEXT
                        || item.getType() == MsgTypeConstant.MSG_TYPE_TROOP_TOPIC
                        || item.getType() == MsgTypeConstant.MSG_TYPE_MIX_PIC
                        || item.getType() == MsgTypeConstant.MSG_TYPE_TROOP_TOPIC_OPEN_TIPS
                        || item.getType() == MsgTypeConstant.MSG_TYPE_TROOP_EFFECT_PIC;
//        return item.getType() == MSG_TYPE_PIC;
    }

    public static boolean isPicAndTextMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_PIC_WITH_TEXT;
    }

    public static boolean isTextMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_TEXT;
    }

    public static boolean isUnKnowType(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_TEXT != item.getType();
    }

    public static boolean isShuoShuoType(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_SHUOSHUO == item.getType();
    }

    /**
     * 没有添加好友附近的人
     *
     * @param item
     * @return
     */
    public static boolean isNearbyPersonMsg(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_TEXT == item.getType() && (item.getIstroop() == 1001 || item.getIstroop() == 1000);//1000应该是群私聊陌生人信息。
    }

    public static boolean isFriendMsg(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_TEXT == item.getType() && item.getIstroop() == 0;
    }

    public static boolean isRedPacketMsg(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_REDPACKET == item.getType() || MsgTypeConstant.MSG_TYPE_REDPACKET_1 == item.getType();
    }

    public static boolean isGroupRedPacketMsg(IMsgModel item) {
        return isRedPacketMsg(item) && item.getIstroop() == 1;
    }


    public static boolean isFriendPacketMsg(IMsgModel item) {
        return isRedPacketMsg(item) && item.getIstroop() == 0;
    }

    /**
     * 群陌生人私聊
     *
     * @param item
     * @return
     */
    public static boolean isGroupPrivateMsg(IMsgModel item) {
        return MsgTypeConstant.MSG_TYPE_TEXT == item.getType() && item.getIstroop() == MsgTypeConstant.MSG_ISTROP_GROUP_PRIVATE_MSG_1;
    }
    // istroop =1000

    public static boolean isSelfMsg(IMsgModel item) {
        return isRobotSelftMsg(item);
    }

    public static boolean isSelfMsg(IMsgModel item, String qq) {
        return item.getSelfuin().equals(qq) ||"1".equals(item.getSelfuin());
    }

    public static boolean isPublicSystemMsg(IMsgModel item) {
        return item.getType() == MSG_TYPE_SYSTEM_MSG;
    }

    public static boolean isAPPlicationMsg(IMsgModel item) {
        return item.getType() == MSG_TYPE_APPLICATION_MSG;
    }

    public static boolean isRedpackagetMsg(IMsgModel item) {
        return item.getType() == MSG_TYPE_REDPACKET_1;
    }

    public static boolean isVideoMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_MEDIA_SHORTVIDEO;
    }


    public static boolean isDiscussionGroupMsg(IMsgModel item) {
        return isTextMsg(item) && item.getIstroop() == MsgTypeConstant.MSG_ISTROOP_DISCUSSION_GROUP_MSG;
//        discussion group
    }

    public static boolean isDiscussionGroupPrivateMsg(IMsgModel item) {
        return isTextMsg(item) && item.getIstroop() == MsgTypeConstant.MSG_ISTROOP_DISCUSSION_GROUP_PRIVATE_MSG;
//        discussion group
    }


    public static boolean isGroupPrivateMsg1(IMsgModel item) {
        return isTextMsg(item) && item.getIstroop() == MsgTypeConstant.MSG_ISTROOP__GROUP_PRIVATE_MSG;
//        discussion group
    }
    public static boolean isRobotSelftMsg(IMsgModel item) {
        if (item.getIstroop() == MsgTypeConstant.MSG_ISTROP_GROUP_PRIVATE_MSG_1 ) {


            if (item.getSelfuin().equals(item.getExtrajson())) {//但是是自己主动发送过去的还是会有死循环的。没法辨别是否是自己发送的.
                return true;
            }
        }


        if (item.getIstroop() == MsgTypeConstant.MSG_ISTROOP__GROUP_PRIVATE_MSG) {


            if (item.getSelfuin().equals(item.getExtrajson())) {//但是是自己主动发送过去的还是会有死循环的。没法辨别是否是自己发送的.
                return true;

            }
        }



        return item.getSenderuin().equals(item.getSelfuin());
    }

    public static boolean isGroupPicMsg(IMsgModel item) {
        return isPicMsg(item) && item.getIstroop() == 1;
    }

    public static boolean isGroupReplyMsg(IMsgModel item) {
        return isReplyMsg(item) && item.getIstroop() == 1;
    }

    private static boolean isReplyMsg(IMsgModel item) {
        return item.getType() == MsgTypeConstant.MSG_TYPE_REPLY || item.getType() == MsgTypeConstant.MSG_TYPE_REPLY_UNKNOWN;
    }


    public static boolean isGroupPicAndAppendTextMsg(IMsgModel item) {
        return isPicMsg(item) && item.getIstroop() == 1;
    }

    public static boolean isVoiceRedPacket(int type) {
        return type == RedPacketMessageType.VOICE_PASSWORD || type == RedPacketMessageType.VOICE_PASSWORD_TWO;
    }

}

  /*  IMsgItem item = RobotUtil.contentValuesToIMsgItem(values);
    //陌生人 从关注 也就是附近的人 进去 istroop =1001       系统消息 type=-2030                   系统消息-2030 群主开启了禁言则发送者为群号  收到应用消息  -20006
    //PPT_uRL 发送者1000000  类型 -2006
    String errMsg = filterRepeatMsgNotNullResutnErrMsg(item);  //-2000 不知道是啥哈
    //-2015 发表了说说    -1049 群陌生聊天发起
        if (errMsg != null) {
        return getFailUri(errMsg);//-2000 图片消息 ， istroop 1 应该是群会话类型。但是 message==null..
    } //空消息忽略 。type--20000
        if (MsgTyeUtils.isSelfMsg(item)) {//1000 233  2072  1527     群私聊消息 类型应该是1000  类型 就是-1000SSS
57:29.671 15016-15090/com.tencent.mobileqq:MSF D/MSF.C.NetConnTag: netSend ssoSeq:9818 uin:*8264 cmd:1378023387 10223
07-23 14:57:41.281 16474-16485/cn.qssq666.robot D/RobotContentProvider: 收到的contentvalues:senderuin=153016267 time=1500793060000 frienduin=102289352 nickname=罗正 selfuin=35068264 istroop=1 type=-1000 msg=fffffffff,insertUri:content://cn.qssq666.robot/insert/msg
07-23 14:57:52.441 16474-16485/cn.qssq666.robot D/RobotContentProvider: 收到的contentvalues:senderuin=35697438 time=1500793072000 frienduin=102289352 nickname=打击贩卖 selfuin=35068264 istroop=1 type=-1000 msg=nihao,insertUri:content://cn.qssq666.robot/insert/msg



}*/
