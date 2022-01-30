package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import android.text.TextUtils;

import java.util.List;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.AdminBean;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.bean.ViolationRecordBean;
import cn.qssq666.robot.bean.ViolationWordRecordBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.CardHelper;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;

/**
 * Created by qssq on 2018/7/27 qssq666@foxmail.com
 */
public class VarCastUtil {
    public static String parseStr(IMsgModel item, DBUtils dbUtils, String argSecond) {

        return parseStr(item, dbUtils, argSecond, null, null);
    }

    public static String parseStr(IMsgModel item, DBUtils dbUtils, String argSecond, String anyValue) {
        if (anyValue != null) {
            return parseStr(item, dbUtils, argSecond, new String[]{"$any"}, new String[]{anyValue});//ignore_include

        } else {
            return parseStr(item, dbUtils, argSecond, null, null);

        }

    }

    public static String parseStr(IMsgModel item, DBUtils dbUtils, String argSecond, String[] keys, String[] values) {

        if (argSecond.contains("ig-var")) {//ignore_include
            return argSecond.replace("ig-var", "");//ignore_include
        }

        StringBuilder stringBuilder = new StringBuilder(argSecond);


        List<VarBean> varBeans = DBHelper.getVarTableUtil(dbUtils).queryAll(VarBean.class);
        if (varBeans != null) {

            for (VarBean varBean : varBeans) {

                if (varBean.getName().startsWith("r")) {//多重替换 值

                    if (StringUtils.replaceAllParseVar(stringBuilder, "$" + varBean.getName(), parseStr(item, dbUtils, varBean.getValue()))) {//把里面的u$u 啥的替换掉。


                    }
                } else {

                    if (StringUtils.replaceAllParseVar(stringBuilder, "$" + varBean.getName(), varBean.getValue())) {


                    }

                }

            }
        }

//放到最后面替换 是为了 在上面解析出来又发现变量就尴尬了。 这里是固定变量
        parseBaseVar(item, dbUtils, stringBuilder);


        StringUtils.replaceAllParseVar(stringBuilder, "$红包", DBHelper.getRedPacketBUtil(dbUtils).getTableName(RedPacketBean.class));
        StringUtils.replaceAllParseVar(stringBuilder, "$违规记录", DBHelper.getViolationRecordUtil(dbUtils).getTableName(ViolationRecordBean.class));
        StringUtils.replaceAllParseVar(stringBuilder, "$违规详单", DBHelper.getViolationWordHistoryRecordUtil(dbUtils).getTableName(ViolationWordRecordBean.class));
        StringUtils.replaceAllParseVar(stringBuilder, "$忽略QQ", DBHelper.getIgnoreQQDBUtil(dbUtils).getTableName(AccountBean.class));
        StringUtils.replaceAllParseVar(stringBuilder, "$卡片演示", CardHelper.kapian_arg_2_zhuanzhang);//ignore_include
        StringUtils.replaceAllParseVar(stringBuilder, "$文本消息", CardHelper.text_msg);//ignore_include
        StringUtils.replaceAllParseVar(stringBuilder, "$转账消息", CardHelper.kapian_arg_3_zhuanzhang);//ignore_include
        //卡片<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID="23" templateID="1" action="web" brief="向你发起了转账" sourceMsgId="0" url="https://qssq666.gitee.io/software/succ1.html?money=200.00&amp;key=c1x09104vwt0xobp1vmrr63" flag="2" adverSign="0" multiMsgFlag="0"><item layout="1" bg="-16734753"><title size="45">200元</title><title size="30">已转入余额</title><tr /></item><item layout="1"><summary size="28">QQ转账</summary></item><source name="" icon="" action="" appid="-1" /></msg>
        StringUtils.replaceAllParseVar(stringBuilder, "$高级转账", CardHelper.kapian_arg_5_zhuanzhang);//ignore_include
        StringUtils.replaceAllParseVar(stringBuilder, "$超级转账", CardHelper.kapian_arg_6_zhuanzhang);//ignore_include
        StringUtils.replaceAllParseVar(stringBuilder, "$新人入群", CardHelper.newPersionJoinGroup);//ignore_include
        if (stringBuilder.toString().contains("$我的名片")) {//不判断会死循环 不过可以做一个 parse base变量

            //parseStr
            StringUtils.replaceAllParseVar(stringBuilder, "$我的名片", parseBaseVar(item, dbUtils, CardHelper.kapian_arg_geren_my_card));//ignore_include

        }

        if (stringBuilder.toString().contains("$他的名片")) {//不判断就会死循环。

            StringUtils.replaceAllParseVar(stringBuilder, "$他的名片", parseBaseVar(item, dbUtils, CardHelper.kapian_arg_card));//ignore_include
        }

        //自定义 变量格式化key, value的替换
        if (values != null && keys != null && keys.length == values.length) {

            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                String value = values[i];
                if(TextUtils.isEmpty(key)){
                    continue;
                }
                if(TextUtils.isEmpty(value)){
                    value="";
                }
                StringUtils.replaceAllParseVar(stringBuilder, key, value);
            }

        }


        return stringBuilder.toString();
    }

    private static String parseBaseVar(IMsgModel item, DBUtils dbUtils, String str) {

        StringBuilder sb = new StringBuilder(str);

        parseBaseVar(item, dbUtils, sb);
        return sb.toString();
    }

    private static void parseBaseVar(IMsgModel item, DBUtils dbUtils, StringBuilder stringBuilder) {
        StringUtils.replaceAllParseVar(stringBuilder, "$robotname", RobotContentProvider.getInstance().mLocalRobotName + "");
        StringUtils.replaceAllParseVar(stringBuilder, "$nickname", item.getNickname());
        StringUtils.replaceAllParseVar(stringBuilder, "$username", TextUtils.isEmpty(item.getNickname()) ? item.getSenderuin() : item.getNickname());
        StringUtils.replaceAllParseVar(stringBuilder, "$g", item.getFrienduin() + "");
        StringUtils.replaceAllParseVar(stringBuilder, "$违禁词", DBHelper.getGagKeyWord(dbUtils).getTableName(GagAccountBean.class) + "");
        StringUtils.replaceAllParseVar(stringBuilder, "$管理员", DBHelper.getSuperManager(dbUtils).getTableName(AdminBean.class) + "");
        StringUtils.replaceAllParseVar(stringBuilder, "$u", item.getSenderuin() + "");
        StringUtils.replaceAllParseVar(stringBuilder, "$s", item.getSelfuin());
    }

}
