package cn.qssq666.robot.constants;
import cn.qssq666.CoreLibrary0;
/**
 * Created by luozheng on 2017/3/5.  qssq.space
 * <p>
 * <p>
 * QQ红包消息
 * .qssq666.robot W/RobotUtil: [log]不支持的处理消息类型，您可以反馈作者增加友好支持提示, type:-2025,istroop:0,message:(来自ＱＱ:xxxxxxxxxxxx[35068264])
 * 09-23 11:41:35.364 11161-11175/cn.qssq666.robot W/RobotUtil: [log]不支持的处理消息类型，您可以反馈作者增加友好支持提示, type:-2029,istroop:0,message:[QQWallet Tips]
 * <p>
 * <p>
 * til: [log]不支持的处理消息类型，您可以反馈作者增加友好支持提示, type:-2025,istroop:1,message:0.01没抢到(174ms)
 * [群名:麦本本黑苹果交流群]
 * [群号:122328962]
 * [昵称:xxxxxxxxxxxx]
 * [ＱＱ:35068264]
 */

/*
TAG: String  = {@831441066904} "MessageForPic"
defaultSuMsgId= -1
uuid: String  = {@831447200440} "{2DB4D6E6-AE28-D494-C29B-465814E00814}.gif"
SpeedInfo: String  = null
actMsgContentValue: String  = null
action: String  = null
thumbMsgUrl: String  = {@831447197816} "/gchatpic_new/35068264/2072103467-3063434995-2DB4D6E6AE28D494C29B465814E00814/198?vuin=35068264&term=2"
serverStoreSource: String  = {@831447199488} "picplatform"
bigMsgUrl: String  = {@831447193304} ""
bigThumbMsgUrl: String  = {@831447187984} "/gchatpic_new/35068264/2072103467-3063434995-2DB4D6E6AE28D494C29B465814E00814/400?vuin=35068264&term=2"
reportInfo: ReportInfo  = {ReportInfo@831487631976} "\nReportInfo\n |-rpStep:-1\n |-rpMsgRecvTime:-1\n |-rpMsgNetwork:-1\n |-rpThumbNetwork:-1\n |-rpThumbDownMode:-1\n |-rpThumbTimeDiff:-1\n |-rpBigDownMode:-1\n |-rpBigTimeDiff:-1\n |-rpBigNetwork:-1"
rawMsgUrl: String  = {@831447191368} "/gchatpic_new/35068264/2072103467-3063434995-2DB4D6E6AE28D494C29B465814E00814/0?vuin=35068264&term=2"
picExtraObject: Object  = null
picExtraData: PicMessageExtraData  = null
path: String  = {@831447201864} "{2DB4D6E6-AE28-D494-C29B-465814E00814}.gif"
md5: String  = {@831447199752} "2DB4D6E6AE28D494C29B465814E00814" 发图片的时候为空
 */



public interface MsgTypeConstant {
    String messageID = "messageid";


    String nickname = "nickname";
    String extstr="extstr";

    public final static String ACTION_MSG = "insert/msg";//表示这目录下面所有
    String AUTHORITY = "cn.qssq666.robot";
    String AUTHORITY_CONTENT = "content://" + AUTHORITY;
    public final static String ACTION_GAD = "insert/gad";//表示这目录下面所有
    public final static String ACTION_KICK = "insert/kick";
    public final static String ERROR_JSON = "{'msg':'error',code:-1}";
    String istroop = "istroop";
    String version = "version";
    String time = "time";
    String senderuin = "senderuin";
    String frienduin = "frienduin";
    String selfuin = "selfuin";
    String type = "type";
    String code = "code";
    String apptype="apptype";
    String extrajson="extrajson";
    String msg = "msg";
    int MSG_TYPE_TEXT = -1000;

    int MSG_TYPE_PROXY_SEND_TEXT = -99999;
    int MSG_TYPE_SHUOSHUO = -2015;
    int MSG_TYPE_PIC = -2000;
    int MSG_TYPE_PIC_WITH_TEXT = -1035;
    int MSG_TYPE_REDPACKET = -2025;
    int MSG_TYPE_REDPACKET_1 = -2500;



    // type:-1000,istroop:1000,message 应该是陌生人
    /*
    type=-1012
     */
    int MSG_TYPE_JOIN_GROUP = -1012;//的contentvalues:senderuin=474240677 time=1500696221000 frienduin=474240677 nickname=笑容不是为我 selfuin=35068264 istroop=1 type=-1012 msg=Smily. 加入了本群，点击修改TA的群名片
    int MSG_TYPE_REPLY = -1049;
    int MSG_TYPE_REPLY_UNKNOWN = -1051;
    int MSG_TYPE_SYSTEM_MSG = -2030; //如果来自群 则是istroop
    //应用消息 message=-20006 但是istroop=1则是群系统消息
    int MSG_TYPE_APPLICATION_MSG = -1012;
    int MSG_ISTROOP_DISCUSSION_GROUP_MSG = 3000;
    //讨论组私聊消息
    int MSG_ISTROOP_DISCUSSION_GROUP_PRIVATE_MSG = 1004;
    int MSG_ISTROOP__GROUP_PRIVATE_MSG = 1000;//frienduin 是 qq,senderuin是 好友。
    int MSG_ISTROP_GROUP_PRIVATE_MSG_1 =10000;

    int MSG_TYPE_0x7F = -2006;
    int MSG_TYPE_ACTIVATE_FRIENDS = -5003;
    int MSG_TYPE_ACTIVITY = -4002;
    int MSG_TYPE_AI_SPECIAL_GUIDE = -1052;
    int MSG_TYPE_APPROVAL_GRAY_TIPS = -2041;
    int MSG_TYPE_APPROVAL_MSG = -2040;
    int MSG_TYPE_ARK_APP = -5008;
    int MSG_TYPE_ARK_BABYQ_REPLY = -5016;
    int MSG_TYPE_ARK_SDK_SHARE = -5017;
    int MSG_TYPE_AUTHORIZE_FAILED = -4005;
    int MSG_TYPE_AUTOREPLY = -10000;
    int MSG_TYPE_BAT_PROCESS_FILE = -3013;
    int MSG_TYPE_BIZ_DATA = -2023;
    int MSG_TYPE_C2C_CHAT_FREQ_CALL_TIP = -1014;
    int MSG_TYPE_C2C_KEYWORD_CALL_TIP = -1015;
    int MSG_TYPE_C2C_MIXED = -30002;
    int MSG_TYPE_COLOR_RING_TIPS = -3012;
    int MSG_TYPE_CONFIGURABLE_GRAY_TIPS = 2024;
    int MSG_TYPE_CONFIGURABLE_TAB_VISIBLE_GRAY_TIPS = -2042;
    int MSG_TYPE_DATE_FEED = -1042;
    int MSG_TYPE_DEVICE_CLOSEGROUPCHAT = -4506;
    int MSG_TYPE_DEVICE_DISMISSBIND = -4507;
    int MSG_TYPE_DEVICE_FILE = -4500;
    int MSG_TYPE_DEVICE_LITTLE_VIDEO = -4509;
    int MSG_TYPE_DEVICE_OPENGROUPCHAT = -4505;
    int MSG_TYPE_DEVICE_PTT = -4501;
    int MSG_TYPE_DEVICE_SHORT_VIDEO = -4503;
    int MSG_TYPE_DEVICE_SINGLESTRUCT = -4502;
    int MSG_TYPE_DEVICE_TEXT = -4508;
    int MSG_TYPE_DINGDONG_SCHEDULE_MSG = -5010;
    int MSG_TYPE_DING_DONG_GRAY_TIPS = -2034;
    int MSG_TYPE_DISCUSS_PUSH = -1004;
    int MSG_TYPE_DISCUSS_UPGRADE_TO_GROUP_TIPS = -1050;
    int MSG_TYPE_DISC_CREATE_CALL_TIP = -1016;
    int MSG_TYPE_DISC_PTT_FREQ_CALL_TIP = -1017;
    int MSG_TYPE_ENTER_TROOP = -4003;
    int MSG_TYPE_FAILED_MSG = -2013;
    int MSG_TYPE_FILE_RECEIPT = -3008;
    int MSG_TYPE_FLASH_CHAT = -5013;
    int MSG_TYPE_FOLD_MSG_GRAY_TIPS = -5011;
    int MSG_TYPE_FORWARD_IMAGE = -20000;
    int MSG_TYPE_FRIEND_SYSTEM_STRUCT_MSG = -2050;
    int MSG_TYPE_GAME_INVITE = -3004;
    int MSG_TYPE_GAME_PARTY_GRAY_TIPS = -2049;
    int MSG_TYPE_GAME_SHARE = -3005;
    int MSG_TYPE_GRAY_TIPS = -5000;
    int MSG_TYPE_GRAY_TIPS_TAB_VISIBLE = -5001;
    int MSG_TYPE_GROUPDISC_FILE = -2014;
    int MSG_TYPE_HIBOOM = -5014;
    int MSG_TYPE_HOMEWORK_PRAISE = -2043;
    int MSG_TYPE_HONGBAO_KEYWORDS_TIPS = -1045;
    int MSG_TYPE_HOT_CHAT_TO_SEE_TIP = 1018;
    int MSG_TYPE_INCOMPATIBLE_GRAY_TIPS = -5002;
    int MSG_TYPE_INTERACT_AND_FOLLOW = -2055;
    int MSG_TYPE_LIFEONLINEACCOUNT = -5004;
    int MSG_TYPE_LIGHTALK_MSG = -2026;
    int MSG_TYPE_LOCAL_COMMON = -4000;
    int MSG_TYPE_LOCAL_URL = -4001;
    int MSG_TYPE_LONG_MIX = -1036;
    int MSG_TYPE_LONG_TEXT = -1037;
    int MSG_TYPE_MEDAL_NEWS = -2062;
    int MSG_TYPE_MEDIA_EMO = -2001;
    int MSG_TYPE_MEDIA_FILE = -2005;
    int MSG_TYPE_MEDIA_FUNNY_FACE = -2010;
    int MSG_TYPE_MEDIA_MARKFACE_PIC = -2007;//essage:it is marketface,
    int MSG_TYPE_MEDIA_MULTI09 = -2003;
    int MSG_TYPE_MEDIA_MULTI513 = -2004;
    int MSG_TYPE_MEDIA_PIC = -2000;
    int MSG_TYPE_MEDIA_PTT = -2002;//发送语音录音文件
    int MSG_TYPE_MEDIA_SECRETFILE = -2008;
    int MSG_TYPE_MEDIA_SHORTVIDEO = -2022;
    int MSG_TYPE_MEDIA_VIDEO = -2009;
    int MSG_TYPE_MEETING_NOTIFY = -5006;
    //图片和文字混
    int MSG_TYPE_MIX_PIC = -1035;
    int MSG_TYPE_MULTI_TEXT_VIDEO = -4008;
    int MSG_TYPE_MULTI_VIDEO = -2016;
    int MSG_TYPE_MY_ENTER_TROOP = -4004;
    int MSG_TYPE_NEARBY_DATING_SAFETY_TIP = -1028;
    int MSG_TYPE_NEARBY_DATING_TIP = -1024;
    int MSG_TYPE_NEARBY_FLOWER_TIP = -2037;
    int MSG_TYPE_NEARBY_LIVE_TIP = -2053;
    int MSG_TYPE_NEARBY_MARKET = -2027;
    int MSG_TYPE_NEARBY_RECOMMENDER = -4011;
    int MSG_TYPE_NEW_FRIEND_TIPS = -1013;
    int MSG_TYPE_NEW_FRIEND_TIPS_GAME_ADDEE = -1019;
    int MSG_TYPE_NEW_FRIEND_TIPS_GAME_ADDER = -1018;
    int MSG_TYPE_NULL = -999;
    int MSG_TYPE_ONLINE_FILE_REQ = -3007;
    int MSG_TYPE_OPERATE_TIPS = -1041;
    int MSG_TYPE_PA_PHONE_MSG_TIPS = -1048;
    int MSG_TYPE_PC_PUSH = -3001;
    int MSG_TYPE_PIC_AND_TEXT_MIXED = -3000;
    int MSG_TYPE_PIC_QSECRETARY = -1032;
    int MSG_TYPE_PL_NEWS = -2060;
    int MSG_TYPE_POKE_MSG = -5012;
    int MSG_TYPE_PSTN_CALL = -2046;
    int MSG_TYPE_PTT_QSECRETARY = -1031;
    int MSG_TYPE_PUBLIC_ACCOUNT = -3006;
    int MSG_TYPE_QLINK_AP_CREATE_SUC_TIPS = -3011;
    int MSG_TYPE_QLINK_FILE_TIPS = -3009;
    int MSG_TYPE_QLINK_SEND_FILE_TIPS = -3010;
    int MSG_TYPE_QQSTORY = -2051;
    int MSG_TYPE_QQSTORY_COMMENT = -2052;
    int MSG_TYPE_QQSTORY_LATEST_FEED = -2061;
    int MSG_TYPE_QQWALLET_MSG = -2025;
    int MSG_TYPE_QQWALLET_TIPS = -2029;
    int MSG_TYPE_QZONE_NEWEST_FEED = -2015;
    int MSG_TYPE_RECOMMAND_TIPS = -5007;
    int MSG_TYPE_RED_PACKET_TIPS = -1044;
    int MSG_TYPE_RENEWAL_TAIL_TIP = -4020;
    int MSG_TYPE_REPLY_TEXT = -1049;
    int MSG_TYPE_REVOKE_GRAY_TIPS = -2031;
    int MSG_TYPE_SCRIBBLE_MSG = -7001;
    int MSG_TYPE_SENSITIVE_MSG_MASK_TIPS = -1046;
    int MSG_TYPE_SHAKE_WINDOW = -2020;
    int MSG_TYPE_SHARE_HOT_CHAT_GRAY_TIPS = -2033;
    int MSG_TYPE_SHARE_LBS_PUSH = -4010;
    int MSG_TYPE_SHIELD_MSG = -2012;
    int MSG_TYPE_SINGLE_WAY_FRIEND_MSG = -2019;
    int MSG_TYPE_SOUGOU_INPUT_TIPS = -1043;
    int MSG_TYPE_SPECIALCARE_TIPS = -5005;
    int MSG_TYPE_SPLIT_LINE_GRAY_TIPS = -4012;
    int MSG_TYPE_STICKER_MSG = -2058;
    int MSG_TYPE_STRUCT_LONG_TEXT = -1051;
    int MSG_TYPE_STRUCT_MSG = -2011;
    int MSG_TYPE_STRUCT_TROOP_NOTIFICATION = -2021;
    int MSG_TYPE_SYSTEM_STRUCT_MSG = -2018;
    int MSG_TYPE_TEAM_WORK_FILE_IMPORT_SUCCESS_TIPS = -2063;
    int MSG_TYPE_TEXT_FRIEND_FEED = -1034;
    int MSG_TYPE_TEXT_GROUPMAN_ACCEPT = -1021;
    int MSG_TYPE_TEXT_GROUPMAN_ADDREQUEST = -1020;
    int MSG_TYPE_TEXT_GROUPMAN_INVITE = -1023;
    int MSG_TYPE_TEXT_GROUPMAN_REFUSE = -1022;
    int MSG_TYPE_TEXT_GROUP_CREATED = -1047;
    int MSG_TYPE_TEXT_QSECRETARY = -1003;
    int MSG_TYPE_TEXT_RECOMMEND_CIRCLE = -1033;
    int MSG_TYPE_TEXT_RECOMMEND_CONTACT = -1030;
    int MSG_TYPE_TEXT_RECOMMEND_TROOP = -1039;
    int MSG_TYPE_TEXT_RECOMMEND_TROOP_BUSINESS = -1040;
    int MSG_TYPE_TEXT_SAFE = -1002;
    int MSG_TYPE_TEXT_SYSTEM_ACCEPT = -1008;
    int MSG_TYPE_TEXT_SYSTEM_ACCEPTANDADD = -1007;
    int MSG_TYPE_TEXT_SYSTEM_ADDREQUEST = -1006;
    int MSG_TYPE_TEXT_SYSTEM_ADDSUCCESS = -1010;
    int MSG_TYPE_TEXT_SYSTEM_OLD_VERSION_ADDREQUEST = -1011;
    int MSG_TYPE_TEXT_SYSTEM_REFUSE = -1009;
    int MSG_TYPE_TEXT_VIDEO = -1001;
    int MSG_TYPE_TIM_DOUFU_GUIDE = -3015;
    int MSG_TYPE_TIM_GUIDE = -3014;
    int MSG_TYPE_TROOP_DELIVER_GIFT = -2035;
    int MSG_TYPE_TROOP_DELIVER_GIFT_OBJ = -2038;
    int MSG_TYPE_TROOP_EFFECT_PIC = -5015;
    int MSG_FOLLOW=-2035;
    int MSG_TYPE_TROOP_FEE = -2036;
    int MSG_TYPE_TROOP_GAP_GRAY_TIPS = -2030;
    int MSG_TYPE_TROOP_MIXED = -30003;
    int MSG_TYPE_TROOP_NEWER_POBING = -2059;
    int MSG_TYPE_TROOP_OBJ_MSG_VIDEO = -2017;
    int MSG_TYPE_TROOP_REWARD = -2048;
    int MSG_TYPE_TROOP_SIGN = -2054;
    int MSG_TYPE_TROOP_STORY = -2057;
    int MSG_TYPE_TROOP_TIPS_ADD_MEMBER = -1012;
    int MSG_TYPE_TROOP_TOPIC = -2044;
    int MSG_TYPE_TROOP_TOPIC_OPEN_TIPS = -2032;
    int MSG_TYPE_TROOP_UNREAD_TIPS = -4009;
    int MSG_TYPE_TROOP_WANT_GIFT_MSG = -2056;
    int MSG_TYPE_UNCOMMONLY_USED_CONTACTS = -1026;
    int MSG_TYPE_UNCOMMONLY_USED_CONTACTS_CANCEL_SET = -1027;
    int MSG_TYPE_UNITE_GRAY_HISTORY_INVI = -5021;
    int MSG_TYPE_UNITE_GRAY_NORMAL = -5040;
    int MSG_TYPE_UNITE_GRAY_TAB_INVI = -5020;
    int MSG_TYPE_UNITE_TAB_DB_INVI = -5022;
    int MSG_TYPE_UNITE_TAB_HISTORI_INVI = -5023;
    int MSG_TYPE_VAS_APOLLO = -2039;
    int MSG_TYPE_VIP_AIO_SEND_TIPS = -4022;
    int MSG_TYPE_VIP_DONATE = -2047;
    int MSG_TYPE_VIP_KEYWORD = -4021;
    int MSG_TYPE_VIP_VIDEO = -2045;
    int MSG_VERSION_CODE = 3;
    int MSG_VERSION_CODE_FOR_PICPTT = 3;

    //艾特研究  @四川-男-简单 给我也扫一下 在extStr中{"vip_font_id":"147751","troop_at_info_list":"[{\"textLen\":8,\"startPos\":0,\"uin\":1573939982,\"flag\":0}]"}
}

