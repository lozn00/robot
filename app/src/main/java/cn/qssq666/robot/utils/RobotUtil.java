package cn.qssq666.robot.utils;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.AdminBean;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.GroupAdaminBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.NickNameBean;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.bean.TwoBean;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.enums.GAGTYPE;
import cn.qssq666.robot.misc.SQLCns;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;

/**
 * Created by luozheng on 2017/3/5.  qssq.space
 */

public class RobotUtil {


    public static String printTestTime(long j) {
//        Context applicationContext = this.a.getApplication().getApplicationContext();
        String secondStr = "秒";//applicationContext.getString(2131430448);
        String minuteStr = "分钟";//""+ applicationContext.getString(2131430449);
        String hourStr = "小时";//applicationContext.getString(2131430450);
        if (j < 60) {
            return 1 + secondStr;
        }
        long second = 59 + j;
        long day = second / 86400;//86400 等于1天 3600 等于60分钟 1小时
        long hour = (second - (86400 * day)) / 3600;
        second = ((second - (86400 * day)) - (3600 * hour)) / 60;
        String str = "";
        if (day > 0) {
            str = str + day + hourStr;
        }
        if (hour > 0) {
            str = str + hour + minuteStr;
        }
        if (second > 0) {
            return str + second + secondStr;
        }
        return str;
    }


    public static void insertDefaultWord(DBUtils dbUtils) {
        //接龙 碰碰  下注 雷群
        dbUtils.setAlias(Cns.TABLE_KEY_REPLY_TABLE);
        ReplyWordBean replyWordBean = new ReplyWordBean("情迁最新版").appendAsk("下载").appendAnswer("更新").appendAsk("升级").appendAsk("情随事迁").appendAsk("内置");
        dbUtils.insert(replyWordBean.appendAnswer("还不知道最新版如何下载？作者网盘有最新版啊哥哥 而且每一个作者的软件都可以找到作者博客 因为作者每一个软件都有版本升级的链接").appendAnswer("需要下载最新版吗? 是的话自己去下载 入口就在你的作者的每一个软件上 找到网址  总之你完全没必要再向别人索要 也完全至于被别人勒索 自己动手丰衣足食 自己仔细找找 可以从软件上找到更新入口进行下载 屡教不改伸手禁言 5-30天 或者你百度搜ok "));
        dbUtils.insert(new ReplyWordBean("饿不饿").appendAnswer("我下面给你吃").appendAnswer("额 什么鬼").appendAnswer("你好污").appendAnswer("你好坏").appendAnswer("流氓"));
        dbUtils.insert(new ReplyWordBean("拉我").appendAnswer("赌").appendAnswer("碰碰").appendAnswer("加我").appendAnswer("私聊").appendAnswer("私我").appendAnswer("雷群").appendAnswer("雷").appendAnswer("套路群").appendAnswer("站街").appendAnswer("请注意言辞!内容涉嫌敏感下次触发加禁言30天！多次警告还是不听者直接永久踢群!"));


        dbUtils.insert(new ReplyWordBean("举报").appendAsk("拉黑").appendAsk("贩卖").appendAsk("盗版").appendAsk("假群").appendAsk("投诉").appendAnswer("若要拉黑贩卖 请加作者qq153016267或联系作者群的管理 提供贩卖诈骗证据 我将计入黑名单"));
        dbUtils.insert(new ReplyWordBean("闪退").appendAsk("崩溃").appendAsk("无响应").appendAsk("没反应").appendAsk("警告").appendAnswer("卡").appendAnswer("篡改").appendAnswer("秒退").appendAnswer("安装失败").appendAnswer("无法安装").appendAnswer("打开不了").appendAnswer("内置还是插件出毛病了?不管出现什么问题").appendAnswer("你都应该看看是否是最新版").appendAnswer("如果不是最新版多多关注群信息群公告!出现问题注意描述清楚 不清楚的话只能视为低级问题 谢谢合作 对于无响应无法安装都可以尝试强制杀死程序(需要情迁工具箱打开软件详情终止程序)清除数据或者卸载 下载最新版  重装解决!"));
        dbUtils.insert(new ReplyWordBean("打赏").appendAsk("支付宝").appendAsk("微信").appendAsk("付款").appendAsk("扫码").appendAnswer("想要打赏作者吗？想不用1分钱，每天免费打赏作者吗?你们知道作者有多辛苦吗？作者没日没夜的用业余时间不停的忙碌又1年了，1年的陪伴，又能得到什么？没有时间找对象，老大不小了，多可怜啊，用下面的姿势中了红包随便用掉不用过1天就报废了，也等于白搭，或者直接再打赏给作者也行，lozn.top/about 里面有支付宝打赏方式的，【支付宝】年终红包再加10亿！12月24日还有机会获得圣诞惊喜红包！长按复制此消息，打开最新版支付宝就能领取！EoN14O57oI"));

    }

    public static void insertDefaulWhiteNames(DBUtils dbUtils) {
        dbUtils.setAlias(Cns.TABLE_WHTE_GROUP_NAME_TABLE);
//        dbUtils.insert(new AccountBean("129103467"));
//        dbUtils.insert(new AccountBean("618725848"));
    }

    public static void insertDefaulIgnores(DBUtils dbUtils) {
        if (TextUtils.isEmpty(dbUtils.getAliasName())) {
            dbUtils.setAlias(Cns.TABLE_QQIGNORE_TABLE);

        }
        dbUtils.insert(new AccountBean("12345678"));
    }

    public static void insertDefaultRedPacket(DBUtils dbUtils) {
        dbUtils.setAlias(Cns.TABLE_REDPACKET_TABLE);
        dbUtils.insert(new RedPacketBean().setCreatedAt(new Date().getTime()).setQq(Cns.DEFAULT_QQ).setMessage("情迁大佬给您发了1万元红包(可惜是假的)").setMoney("10000").setIstroop(0).setResult(200).setNickname("情随事迁"));
    }

    public static void insertDefaultQQGroupAdmin(DBUtils dbUtils) {

        dbUtils.insert(new TwoBean().setAccount(Cns.DEFAULT_QQ).setQqgroup(Cns.DEFAULT_GROUP));
        dbUtils.insert(new TwoBean().setAccount(Cns.DEFAULT_QQ_1).setQqgroup(Cns.DEFAULT_GROUP));
        dbUtils.insert(new TwoBean().setAccount(Cns.DEFAULT_QQ_3).setQqgroup(Cns.DEFAULT_GROUP));
        dbUtils.insert(new TwoBean().setAccount(Cns.DEFAULT_QQ_2).setQqgroup(Cns.DEFAULT_GROUP));
    }

    public static void insertDefaultNickInfo(DBUtils dbUtils) {
        dbUtils.insert(new NickNameBean().setAccount(Cns.DEFAULT_QQ).setTroopno(Cns.DEFAULT_GROUP).setNickname("情随事迁"));
        dbUtils.insert(new NickNameBean().setAccount(Cns.DEFAULT_QQ_1).setTroopno(Cns.DEFAULT_GROUP).setNickname("情迁小号1"));
        dbUtils.insert(new NickNameBean().setAccount(Cns.DEFAULT_QQ_2).setTroopno(Cns.DEFAULT_GROUP).setNickname("情迁小号2"));
        dbUtils.insert(new NickNameBean().setAccount(Cns.DEFAULT_QQ_3).setTroopno(Cns.DEFAULT_GROUP).setNickname("情迁小号3"));

    }


    public static void insertDefaultAdmin(DBUtils dbUtils) {
        dbUtils.insert(new AdminBean().setAccount(Cns.DEFAULT_QQ).setLevel(10000));
        dbUtils.insert(new AdminBean().setAccount(Cns.DEFAULT_QQ_1).setLevel(500));
        dbUtils.insert(new AdminBean().setAccount(Cns.DEFAULT_QQ_3).setLevel(300));
    }

    public static void insertDefaultGroupAdmin(DBUtils dbUtils) {
        dbUtils.insert(new GroupAdaminBean().setAccount(Cns.DEFAULT_QQ));
        dbUtils.insert(new GroupAdaminBean().setAccount(Cns.DEFAULT_QQ_1));
        dbUtils.insert(new GroupAdaminBean().setAccount(Cns.DEFAULT_QQ_3));
    }

    public static void insertDefaultGagWrod(DBUtils dbUtils) {
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_WORD.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1天")));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_QSSQ.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1天")));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_RAG.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1天")));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_RAG_FULL.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1天")));
//        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_RAG_GLOBAL_DISABLE_NUMBER).setDuration(ParseUtils.parseGagStr2Secound("1天")));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_RAG_GLOBAL_DISABLE_WEBSITE.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1天")).setReason("禁止发外链"));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_SHUAPIN.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("1小时")).setReason("禁止发QQ等号码"));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_WORD1.replace(",",ClearUtil.wordSplit)).setDuration(60 * 60));
        //静默演示
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_SILENCE.replace(",",ClearUtil.wordSplit)).setDuration(60 * 60 * 60));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_KICK.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("30分钟")).setAction(GAGTYPE.KICK));
        dbUtils.insert(new GagAccountBean().setAccount(Cns.DEFAULT_GAG_KICK_FOVER.replace(",",ClearUtil.wordSplit)).setDuration(ParseUtils.parseGagStr2Secound("30分钟")).setAction(GAGTYPE.KICK_FORVER))
        ;
    }


    public static void insertDefaultVarInfo(DBUtils dbUtils) {

        dbUtils.insert(new VarBean().setName("禁用网络词库").setValue(SQLCns.SQL_CONSTANT_DISENABLE_NET_WORK));//ignore_include
        dbUtils.insert(new VarBean().setName("启用网络词库").setValue(SQLCns.SQL_CONSTANT_ENABLE_NET_WORK));//ignore_include
        dbUtils.insert(new VarBean().setName("白名单信息").setValue(SQLCns.SQL_CONSTANT_ENABLE_GROUP));
        dbUtils.insert(new VarBean().setName("红包福利").setValue("https://qssq666.gitee.io/software/s.html"));
        dbUtils.insert(new VarBean().setName("领取的红包").setValue("配置SQL select nickname,money from $红包 where result=200 and money>0"));//ignore_include
        dbUtils.insert(new VarBean().setName("抢红包总数").setValue("select sum(money) as 抢到金额 from $红包 where result=200 and money>0"));//ignore_include
        dbUtils.insert(new VarBean().setName("状态栏进程应用").setValue("dumpsys statusbar"));//ignore_include

        dbUtils.insert(new VarBean().setName("息屏亮屏").setValue("input keyevent 26  "));//ignore_include
        dbUtils.insert(new VarBean().setName("返回键").setValue("input keyevent 4 "));//ignore_include
        dbUtils.insert(new VarBean().setName("回回键").setValue("input keyevent 6"));//ignore_include
        dbUtils.insert(new VarBean().setName("PORT").setValue("service.adb.tcp.port"));//ignore_include
        dbUtils.insert(new VarBean().setName("服务").setValue("service list"));//ignore_include
        dbUtils.insert(new VarBean().setName("分辨率").setValue("wm size"));//ignore_include
        dbUtils.insert(new VarBean().setName("第三方应用").setValue("pm list packages -3"));//ignore_include
        dbUtils.insert(new VarBean().setName("窗口焦点").setValue("dumpsys input | grep Focus"));//ignore_include
        dbUtils.insert(new VarBean().setName("窗口焦点").setValue("call phone 1 s16 \"10010\""));//ignore_include
        //adb shell dumpsys battery
        dbUtils.insert(new VarBean().setName("电池状态").setValue("dumpsys battery"));//ignore_include

    }

    public static boolean isEmptyArg(String[] args) {
        if (args.length == 0 || (args.length == 1 && TextUtils.isEmpty(args[0]))) {
            return true;
        } else {
            return false;
        }
    }

    private static final String TAG1 = "RobotUtil";

    public static boolean isContentsEmpty(String content) {
        boolean empty = TextUtils.isEmpty(content.trim());
        return empty;
    }

    public static boolean contentsContaineKey(String ignorecontents, String key) {
        if (isContentsEmpty(ignorecontents)) {
            return false;
        } else {
            String[] split = ignorecontents.split(",");
            for (String currentKey : split) {
                if (key.contains(currentKey)) {
                    return true;
                }
            }
            return false;

        }
    }


    static {
        LogUtil.importPackage();

    }


    public static MsgItem contentValuesToMsgItem(ContentValues values) {
        //
        //LogUtil.writeLog("ContentValues:" + values.toString());
        Integer code = values.getAsInteger(MsgTypeConstant.code);
        String extraStr = values.getAsString(MsgTypeConstant.extstr);
        String extraJson = values.getAsString(MsgTypeConstant.extrajson);
        Integer isstroop = values.getAsInteger(MsgTypeConstant.istroop);
        Integer type = values.getAsInteger(MsgTypeConstant.type);
        String senderUin = values.getAsString(MsgTypeConstant.senderuin);
        String selfuin = values.getAsString(MsgTypeConstant.selfuin);
        String friendUin = values.getAsString(MsgTypeConstant.frienduin);
        String nickname = values.getAsString(MsgTypeConstant.nickname);
        String msg = values.getAsString(MsgTypeConstant.msg);
        String version = values.getAsString(MsgTypeConstant.version);
        Long msgID = values.getAsLong(MsgTypeConstant.messageID);
        String apptype = values.getAsString(MsgTypeConstant.apptype);
        Long time = values.getAsLong(MsgTypeConstant.time);
        MsgItem item = new MsgItem();
        item.setIstroop(isstroop == null ? 0 : isstroop);
        item.setType(type);
        item.setSenderuin(senderUin);
        item.setFrienduin(friendUin);
        item.setSelfuin(selfuin);
        item.setMessage(TextUtils.isEmpty(msg) ? "" : msg);
        item.setExtstr(extraStr);
        item.setExtrajson(extraJson);
        item.setVersion(version);
        item.setApptype(apptype);
        item.setNickname(nickname);
        item.setMessagID(msgID==null?0:msgID);
        item.setTime(time == null ? 0 : time);
        item.setCode(code == null ? 0 : code);
        return item;
    }

    public static MsgItem uriToMsgItem(Uri uri) {
        String istroopStr = uri.getQueryParameter(MsgTypeConstant.istroop);
        String typeStr = uri.getQueryParameter(MsgTypeConstant.type);
        String extStr = uri.getQueryParameter(MsgTypeConstant.extstr);
        String extStrJson = uri.getQueryParameter(MsgTypeConstant.extrajson);
        String senderUin = uri.getQueryParameter(MsgTypeConstant.senderuin);
        String selfUin = uri.getQueryParameter(MsgTypeConstant.selfuin);
        String friendUin = uri.getQueryParameter(MsgTypeConstant.frienduin);
        String msg = uri.getQueryParameter(MsgTypeConstant.msg);
        String nickname = uri.getQueryParameter(MsgTypeConstant.nickname);
        String code = uri.getQueryParameter(MsgTypeConstant.code);
        String timeStr = uri.getQueryParameter(MsgTypeConstant.time);
        String version = uri.getQueryParameter(MsgTypeConstant.version);
        String apptype = uri.getQueryParameter(MsgTypeConstant.apptype);
        String msgId= uri.getQueryParameter(MsgTypeConstant.messageID);
        MsgItem item = new MsgItem();
        item.setIstroop(TextUtils.isEmpty(istroopStr) ? -1 : Integer.parseInt(istroopStr));
        item.setType(TextUtils.isEmpty(typeStr) ? -1 : Integer.parseInt(typeStr));
        item.setMessagID(TextUtils.isEmpty(msgId) ? 0: Long.parseLong(msgId));
        item.setSenderuin(senderUin);
        item.setSelfuin(selfUin);
        item.setExtstr(extStr);
        item.setExtrajson(extStrJson);
        item.setFrienduin(friendUin);
        item.setTime(TextUtils.isEmpty(timeStr) ? 0 : Long.parseLong(timeStr));
        item.setMessage(msg);
        item.setCode(stringToInt(code));
        item.setNickname(nickname);
        item.setVersion(version);
        item.setApptype(apptype);
        item.setTime(timeStr == null ? 0 : Long.parseLong(timeStr));
      /*  try {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return item;
    }

    public static Uri msgItemToUri(IMsgModel item) {
        return msgItemToUri(item, item.getMessage());

    }

    public static Uri msgItemToUri(IMsgModel item, String message) {
        return Uri.parse(MsgTypeConstant.AUTHORITY_CONTENT).buildUpon().appendPath(RobotContentProvider.ACTION_MSG).appendQueryParameter(MsgTypeConstant.frienduin, item.getFrienduin())
                .appendQueryParameter(MsgTypeConstant.istroop, item.getIstroop() + "")
                .appendQueryParameter(MsgTypeConstant.type, item.getType() + "")
                .appendQueryParameter(MsgTypeConstant.msg, message)
                .appendQueryParameter(MsgTypeConstant.code, item.getCode() + "")
                .appendQueryParameter(MsgTypeConstant.extstr, item.getExtstr() + "")
                .appendQueryParameter(MsgTypeConstant.extrajson, item.getExtrajson() + "")
                .appendQueryParameter(MsgTypeConstant.nickname, item.getNickname())
                .appendQueryParameter(MsgTypeConstant.time, item.getTime() + "")
                .appendQueryParameter(MsgTypeConstant.messageID, item.getMessageID() + "")
                .appendQueryParameter(MsgTypeConstant.selfuin, item.getSelfuin() + "")
                .appendQueryParameter(MsgTypeConstant.senderuin, item.getSenderuin()).build();

    }

    public static String genResultJSONStrign(String message, int code) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MsgTypeConstant.msg, message);
            jsonObject.put(MsgTypeConstant.code, code);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return MsgTypeConstant.ERROR_JSON;
        }
    }

    public static int stringToInt(String str) {
        return str == null || str == "" ? 0 : Integer.parseInt(str);
    }

/*    public static UniqueKey getUniqueKey(MsgItem item) {
//        Log.w(TAG1,"UNIKEY "+"[" + item.getSenderuin() + "]" + item.getFrienduin() + "_" + item.getMessage());
//        return new UniqueKey()
    }*/

    public static String getUniqueKey(IMsgModel item) {
//        Log.w(TAG1,"UNIKEY "+"[" + item.getSenderuin() + "]" + item.getFrienduin() + "_" + item.getMessage());
        return "[" + item.getSenderuin() + "]" + item.getFrienduin()+ "_"+item.getIstroop()+ "_" + item.getMessage()+item.getMessageID();
    }

    public static String isRegxKey(String keyWord) {
        if (keyWord.startsWith("reg")) {
            return keyWord.replace("reg", "");
        }
        return null;
    }

    public static String isRegxFullKey(String keyWord) {
        if (keyWord.startsWith("fullreg")) {
            return keyWord.replace("fullreg", "");
        }
        return null;
    }
    public static String isGlobalReg(String keyWord) {
        if (keyWord.startsWith("greg")) {
            return keyWord.replace("greg", "");
        }
        return null;
    }


    public static boolean isJsonObjectMessage(String message) {
        return message.startsWith("{") && message.endsWith("}");
    }


    public static boolean isJsonArrMessage(String message) {
        return message.startsWith("[") && message.endsWith("]");
    }

    public static File getBaseDir() {
        return new File(Environment.getExternalStorageDirectory(), "qssq666");
    }
}
