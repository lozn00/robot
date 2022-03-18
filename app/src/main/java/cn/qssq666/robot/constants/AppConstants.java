package cn.qssq666.robot.constants;

import cn.qssq666.db.ReflectUtils;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.config.CmdConfig;

/**
 * Created by luozheng on 2017/5/6.  qssq.space
 */

public interface AppConstants {
    // ignore_start
    String CONFIG_SELFUIN = "CONFIG_SELFUIN";
    String CONFIG_NICKNAME = "CONFIG_USER_CARD";
    String CONFIG_friendUIN = "CONFIG_friendUIN";
    String CONFIG_MSG = "CONFIG_MSG";
    String CONFIG_TEST_CONTENT = "CONFIG_TEST_CONTENT";
    String CONFIG_TEST_REG = "CONFIG_TEST_REG";
    String CONFIG_ISTROOP = "CONFIG_ISTROOP";
    String CONFIG_SENDERUIN = "CONFIG_SENDERUIN";
    String CONFIG_STARTUPTIME = "CONFIG_STARTUP_TIME";
    String CONFIG_LAST_EXECUTE_QUERY_SQL_DEFAULT_VALUE = "select * from " + (Cns.TABLE_REDPACKET_TABLE + ReflectUtils.getTableNameByClass(RedPacketBean.class)) + " limit 0,5";//ignore_include
    String CONFIG_LAST_EXECUTE_QUERY_SQL = "config_sql_query";
    //执行命令的默认值
    String CONFIG_LAST_EXECUTE_SQL_COMMEND = "config_sql_commend";

    String CONFIG_LAST_EXECUTE_SQL_DEFAULT_VALUE = "update " + (Cns.TABLE_REDPACKET_TABLE + ReflectUtils.getTableNameByClass(RedPacketBean.class)) + " set money=100";//ignore_include


    String CONFIG_LAST_EXECUTE_TIMESTAMP = "config_sql_timestamp";
    String CONFIG_LAST_EXECUTE_TIMESTAMP_VALUE = "2017-9-23 17:37:24";
    String ACTION_OPERA_NAME = "[操作]";
    String ACTION_OPERA_TIP= "[提示]";
    String ACTION_OPERA_NAME_FORBID = "[操作禁止]";
    String ACTION_OPERA_MULTI_NAME = "[批量操作]";
    String ACTION_OPERA_ALL_RESPONSE_NAME= "[无限制命令]";
    String ACTION_TEMP_FORVER= "本次自动修改临时有效（本命令旨在解决傻瓜用户不会操作）";
    String CONFIG_WORD_IMPORT_PATH = "CONFIG_WORD_IMPORT_PATH";
    String CONFIG_WORDS_SPLIT_FLAG = "CONFIG_WORDS_SPLIT_FLAG";
    String CONFIG_ASK_AND_ANASWER_SPLIT_FLAG = "CONFIG_ASK_AND_ANASWER_SPLIT_FLAG";
    String ALL_PERSON_FLAG = "1000";
    String FUNC_IS_DISABLE_TIP = "功能已被禁用,请打开机器人程序进入白名单管理界面编辑开启此功能。";
    String EXAMPLE_FORMAT = "举例：\n" + CmdConfig.CARD_MSG + " $卡片演示([转账]已入账888元,点击查看详情)\n" +
            "" + CmdConfig.CARD_MSG + "$文本消息(你好,我好,大家好)\n" +
            "" + CmdConfig.CARD_MSG + "$我的名片\n" +
            "" + CmdConfig.CARD_MSG + "$超级转账(群主发起了转账,9999,c1x09104vwt0xobp1vmrr63,9999元,已转入金额,QQ转账)\n" +
            "" + CmdConfig.CARD_MSG + "$新人入群(我是山鸡，多多关照,新人入群)\n" +
            "" + CmdConfig.CARD_MSG + "$他的名片($u|qq,default|null|自定义内容,default,情迁软件工作室特别授权认证)\n" +
            ""+ CmdConfig.CARD_MSG + " $转账消息(https://lozn.top/update,[转账]点击百分百中奖,点击查看详情)\n" +
            ""+CmdConfig.CARD_MSG +" xml格式体\n这里|符号代表可选择左边的规则也可以选择|右边的规则| 代表或的意思,default或者不填写代表默认值,否则显示传递的值。" ;
    String INTENT_FORM_CODE_VIEW = "INTENT_FROM_CODE_VIEW";


    // ignore_end
}
