package cn.qssq666.robot.constants;

/**
 * Created by qssq on 2018/8/31 qssq666@foxmail.com
 */
public interface CardHelper {

    //ignore_start
    String demo = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<msg  serviceID=\"1\" brief=\"新消息\" templateID=\"\" action=\"新消息\" sourceMsgId=\"0\" url=\"\" flag=\"1\" adverSign=\"0\" multiMsgFlag=\"0\">" +
            "    <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"10\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"15\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"20\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"25\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"30\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"35\">%s</title>" +
            "    </item>" +
            "</msg>";
    String text_msg = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<msg  serviceID=\"1\" brief=\"新消息\" templateID=\"\" action=\"新消息\" sourceMsgId=\"0\" url=\"\" flag=\"1\" adverSign=\"0\" multiMsgFlag=\"0\">" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"25\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"30\">%s</title>" +
            "    </item>" +
            "     <item layout=\"0\">" +
            "        <title color=\"#ff0000\" size=\"35\">%s</title>" +
            "    </item>" +
            "</msg>";
    String kapian_arg_2_zhuanzhang = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"33\" templateID=\"1\" action=\"web\" brief=\"情迁聊天机器人\" sourceMsgId=\"0\" url=\"https://lozn.top/update\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" bg=\"-23296\"><title size=\"41\">%s</title><tr /></item><item layout=\"1\"><summary size=\"28\">%s</summary></item><source name=\"\" icon=\"\" action=\"\" appid=\"-1\" /></msg>";
    String kapian_arg_3_zhuanzhang = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"33\" templateID=\"1\" action=\"web\" brief=\"情迁聊天机器人\" sourceMsgId=\"0\" url=\"%s\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" bg=\"-23296\"><title size=\"41\">%s</title><tr /></item><item layout=\"1\"><summary size=\"28\">%s</summary></item><source name=\"\" icon=\"\" action=\"\" appid=\"-1\" /></msg>";
    //卡片<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID="23" templateID="1" action="web" brief="向你发起了转账" sourceMsgId="0" url="https://qssq666.gitee.io/software/succ1.html?money=200.00&amp;key=c1x09104vwt0xobp1vmrr63" flag="2" adverSign="0" multiMsgFlag="0"><item layout="1" bg="-16734753"><title size="45">200元</title><title size="30">已转入余额</title><tr /></item><item layout="1"><summary size="28">QQ转账</summary></item><source name="" icon="" action="" appid="-1" /></msg>
    //卡片 $高级转账(200.c1x09104vwt0xobp1vmrr63,200元,已转入金额,QQ转账)
    String kapian_arg_5_zhuanzhang = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"23\" templateID=\"1\" action=\"web\" brief=\"情迁聊天机器人卡片系统\" sourceMsgId=\"0\" url=\"https://qssq666.gitee.io/software/succ1.html?money=%s&amp;key=%s\" flag=\"2\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" bg=\"-16734753\"><title size=\"45\">%s</title><title size=\"30\">%s</title><tr /></item><item layout=\"1\"><summary size=\"28\">%s</summary></item><source name=\"\" icon=\"\" action=\"\" appid=\"-1\" /></msg>";


    String kapian_arg_card = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"44\" templateID=\"15\" action=\"plugin\" " +
            "actionData=\"AppCmd://OpenContactInfo/?uin=11474|78493\" a_actionData=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin={arg0,$u}\" " +
            "i_actionData=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin={arg0,$u}\" brief=\"" +
            "{arg1,腾讯特别推荐关注网络红人}\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"0\" mode=\"1\">" +
            "<summary color=\"#FF0000\">" +
            "{arg2,腾讯特别推荐网红人气账号}</summary><hr hidden=\"false\" style=\"0\" /></item><item layout=\"2\" mode=\"1\">" +
            "<picture cover=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin={arg0,$u}\" w=\"0\" h=\"0\" />" +
            "<title color=\"#FF0000\"></title><summary color=\"#00DB00\">" +
            "{arg3,腾讯网络红人}</summary></item><source name=\"" +
            "{arg3,腾讯官方特别推荐认证网红}\" icon=\"http://t.cn/RVIeaZK\" action=\"\" appid=\"-1\" /></msg>";
    String kapian_arg_geren_my_card = kapian_arg_card.replace("{arg0,$u}", "$u");
    String kapian_arg_6_zhuanzhang = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"23\" templateID=\"1\" action=\"web\" brief=\"%s\" sourceMsgId=\"0\" url=\"https://qssq666.gitee.io/software/succ1.html?money=%s&amp;key=%s\" flag=\"2\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"1\" bg=\"-16734753\"><title size=\"45\">%s</title><title size=\"30\">%s</title><tr /></item><item layout=\"1\"><summary size=\"28\">%s</summary></item><source name=\"\" icon=\"\" action=\"\" appid=\"-1\" /></msg>";

    String newPersionJoinGroup="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"104\" templateID=\"1\" action=\"\" brief=\"%s\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\" action_type=\"0\" advertiser_id=\"0\" aid=\"0\" dest_type=\"0\" product_type=\"0\"><picture cover=\"\" w=\"0\" h=\"0\" /><title>%s</title></item><source name=\"\" icon=\"\" action=\"\" appid=\"0\" /></msg>";
    //ignore_end
}
