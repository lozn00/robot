package cn.qssq666.robot.constants;
import cn.qssq666.robot.BuildConfig;

/**
 * broadcast barnd
 * Created by luozheng on 2017/2/13.  qssq.space
 */

public class Cns {
//    public final static String ROBOT_DOMAIN = "http://192.168.0.5:8080";
    public final static String ROBOT_DOMAIN = "http://robot.lozn.top";
    public static final String URL_REGISTER_USER =ROBOT_DOMAIN+"/user/register?username=%s&password=%s&phone=%s&email=%s&qq=%s&wechat=%s&token=%s" ;
    public static final String URL_LOGIN_USER =ROBOT_DOMAIN+"/user/login?username=%s&password=%s&token=%s" ;
    public static final String URL_FIND_USER_USER =ROBOT_DOMAIN+"/user/login?username=%s&token=%s" ;
    public static final String QUERY_USER =ROBOT_DOMAIN+"/user?token=%s" ;
    public static final String VIP_URL = ROBOT_DOMAIN+"/user/vip/";
    public static final String EXIT_URL = ROBOT_DOMAIN+"/user/logout/";
    public static final String NAT_TRAVERSE_CMD = "key_nat_traverse_cmd";
    /*
     网页版本的二维码在线生成
     */
    public static final String SCAN_PAY_URL = ROBOT_DOMAIN+"/qrcode?username=%s&token=%s";
    /**
     * 计划
     */
    public static final String PAY_PLAN_URL = ROBOT_DOMAIN+"/pay_plan?username=%s&token=%s";
    public static final String MORE_FUNC_URL = ROBOT_DOMAIN+"/more_func?username=%s&token=%s";
    /**
     *  生成为
     */
    public static final String QRCODE_GENEREATE_PAY_APP_URL = ROBOT_DOMAIN+"/geneqrcode/";
    /*
         查询二维码生成的结果
     */
    public static final String QUERY_ORDER_APP_URL = ROBOT_DOMAIN+"/queryorder?order=%s&type=%d&token=%s";
    public static final String MISC_TIP_KEYWORD = "KEY_MISC_TIP_KEYWORD";
    public static final String MISC_EMAIL_FORWARD_ENABLE = "KEY_EMAIL_FORWARD_ENABLE";
    public static final String MISC_EMAIL_SENDER_EMAIL = "KEY_EMAIL_SENDER_EMAIL";
    public static final String MISC_EMAIL_SENDER_EMAIL_PWD = "KEY_EMAIL_SENDER_EMAIL_PWD";
    public static final String MISC_EMAIL_RECEIVER_EMAIL = "KEY_EMAIL_RECEIVER_EMAIL";
    public static final String MISC_EMAIL_SERVER_ADDRESS = "KEY_EMAIL_SERVER_ADDRESS";
    public static final String MISC_EMAIL_SERVER_PORT = "KEY_EMAIL_SERVER_PORT";
    public static final String MISC_EMAIL_CONTENT = "KEY_EMAIL_CONTENT";
    public static final String MISC_TIP_ENABLE = "KEY_MISC_TIP_ENABLE";
    public static final String MISC_FLOATING_WINDOW = "KEY_FLOATING_WINDOW_ENABLE";
    public static final String PROXY_SEND_ACCOUNT = "KEY_PROXY_SEND_ACCOUNT";
    public static final String PROXY_SEND_ACCOUNT_IS_GROUP = "KEY_PROXY_SEND_ACCOUNT_ISGROUP";
    public static final String CHAT_GPT_API_SERCRET = "CHAT_GPT_API_SERCRET";
    /**
     * ignore_start
     */
    public static String NO_NOTIFICATION_FLAG = ".no_notification";//brand broad broad
    public static final String UPDATE_CODE_BROADCAST = BuildConfig.APPLICATION_ID + ".update_plugin";
    public static final String RUN_CODE_BROADCAST = BuildConfig.APPLICATION_ID + ".run";
    public static final String RUN_SIMULATOR_CODE_BROADCAST = BuildConfig.APPLICATION_ID + ".run_simulator";
    public final static String AUTHORITY = "cn.qssq666.robot";
    public final static String TULING_DOMAIN = "http://www.tuling123.com";
    public final static String ROBOT_REPLY_TULING_URL = TULING_DOMAIN + "/openapi/api";
    public final static String ROBOT_REPLY_MOLI_DOMAIN = "http://i.itpk.cn";
    public final static String ROBOT_OPEN_AI_DOMAIN = "https://chat.openai.com";
    public final static String ROBOT_OPEN_AI_DOMAIN_API = "https://api.openai.com";
    public final static String ROBOT_REPLY_MOLI_URL = ROBOT_REPLY_MOLI_DOMAIN + "/api.php";
    public final static String UPDATE_URL = "https://update.lozn.top/version/robot_update.json";
    public final static String ROBOT_KEY = "key_app_robotkey";
    public final static String ROBOT_SECRET = "key_app_reply_secret";
    public final static String SP_FILE = "COONFIG";
    public final static String UPDATE_KEY = "UPDATE_kEY";
    public final static String UPDATE_SECRET = "UPDATE_SECRET";
    //ignore_end
    public final static String DEFAULT_TULING_KEY = "ad1a6f65f2b2056ba7c3ea87eafd1d4d";
    public final static String TYPE_EDITATA = "TYPE_EDITATA";
    public final static String BEAN = "BEAN";

    public final static String[] OPERA_MENU = {"编辑", "删除"};
    public final static String INTENT_POSITION = "INTENT_POSITION";
    public final static String TABLE_KEY_REPLY_TABLE = "KEYWORD_TABLE_";
    public final static String TABLE_WHTE_GROUP_NAME_TABLE = "";
    public final static String TABLE_QQIGNORE_TABLE = "ig";
    public final static String TABLE_ignore_gag_TABLE = "ig_gag";
    public final static String TABLE_REDPACKET_TABLE = "";
    public final static String TABLE_QQ_GROUP_MANAGER_TABLE = "";
    public final static String TABLE_SUPER_MANAGER_TABLE = "manager";
    public final static String TABLE_GAG_KEYWORD = "gag";
    public final static String TABLE_FLOOR = "floor";
    public final static String PRIVATE_MSG_MUST_INCLUDE_QQGROUP = "QQ消息发送此命令群必须携带群号";
    public final static String[] OPERA_MENU_EDIT = {"编辑", "删除"};
    public final static String FIELD_ENABLE = "disable";
    //操作类型 用于判断是禁言还是踢人
    public final static String FIELD_ACTION = "action";
    public final static String FIELD_SILENCE = "silence";
    public final static String FIELD_TYPE = "type";
    public final static String FIELD_GROUPNICKNAME = "groupnickname";
    public final static String FIELD_NICKNAME = "nickname";
    public final static String DEFAULT_QQ = "35068264";
    public final static String DEFAULT_QQ_SMALL_ADMIN = "35068264";
    public static final String DEFAULT_GAG_WORD1 = "操你,搞你,搞死,爱爱,SB,傻b,傻逼,你妈,加群,加Q";
    public static final String DEFAULT_GAG_WORD = "爸,啪,站街,援交,儿子,妈,孙子,爷,奶,杂,傻,日你,我日,淫,贱,嫖,娼,裸,性爱,砍头,血,腥,密,恐,密恐,屎,大便,黄色,赌,拉群,黄片,微信,成人,电影,+Q,色情,黄色,18禁,sm,傻,乳房,尤物,龟头,阴道,屄,下体"
            + "售,卖,逼,直播,下载,玩j,上床,看片,插入,撸管,摸你,犯贱,好湿";
    public static final String DEFAULT_GAG_SHUAPIN = "\nbr";
    public static final String DEFAULT_GAG_SILENCE = "我草泥马,我艹尼玛,我日你妈,我日你全家,搞你妈,全家死光光";
    public static final String DEFAULT_GAG_KICK = "群主是个傻逼,群主求踢,求你踢我,求踢我,快踢我,踢不掉我,踢我啊,可以踢了我吗,快踢我,快踢了我,踢了我吧,不踢我就发广告,这个群全是垃圾,给我飞机票";
    public static final String DEFAULT_GAG_KICK_FOVER = "给我永久飞机票,求永久踢,垃圾群求踢";

    public static final String DEFAULT_GAG_RAG = "reg[拉加私]我";
    public static final String DEFAULT_GAG_RAG_FULL = "fullreg你妈逼,你麻痹";
//    public static final String DEFAULT_GAG_RAG_GLOBAL_DISABLE_NUMBER = "greg[0-9]{5,10}";
    public static final String DEFAULT_GAG_QSSQ = ",加我,私我,私聊,Q我,q我,拉我,给我,发我,免费,内置,插件,秒抢,抢红包,外挂,协议挂,有g,发我";
    public static final String ROBOT_NAME = "情迁聊天机器人";
    public static final String DEFAULT_QQ_1 = "35697438";
    public static final String DEFAULT_QQ_2 = "35068264";
    public static final String DEFAULT_QQ_3 = "153016267";
    public static final String DOMAIN = "http://lozn.top";
    public static final String DOMAIN_UPDATE = "lozn.top/update";
    public static final String UPDATE_ABOUT_URL = "https://lozn.top/about";
    public static final String UPDATE_ALL_URL = "https://lozn.top/update";
    public static final String PLUGIN_MAIN_ENTRY_FILE = "cn.qssq666.robot.plugin.sdk.control.PluginMainImpl";
    public static final String PLUGIN_INFO_ENTRY_FILE = "cn.qssq666.robot.plugin.sdk.interfaces.RobotGlobaInfo";
    //https://www.jianshu.com/p/76c80d2f91e4
    public static final String SDK_DEVELOPER_JIANSHU_URL = "https://www.jianshu.com/p/76c80d2f91e4";
    public static final String SDK_DEVELOPER_URL_LRA = "https://www.jianshu.com/p/92c7bf329653";
    public static final String SDK_DEVELOPER_URL_JS = "https://www.jianshu.com/p/80b73494134a";
    //    public static final String SDK_LUA_DOC_URL = DOMAIN_UPDATE + "/robot/lua_sdk_doc.html";
    public static final String PLUGIN_DOWNLOAD = DOMAIN_UPDATE + "/robot/download.html";
    public static final String PLUGIN_DOWNLOAD_LUA = DOMAIN_UPDATE + "/robot/download_lua.html";
    public static final String SDK_DOWNLOAD_URL = "https://jcenter.bintray.com/cn/qssq666/robot_sdk/";
    public static final String HELPER_URL = DOMAIN + "/robot/help.html";
    public static final String LUA_PLUGIN_MARKET_URL = DOMAIN_UPDATE + "/robot/lua_plugin_market.html";
    public static final String JS_PLUGIN_MARKET_URL = DOMAIN_UPDATE + "/robot/js_plugin_market.html";
    /**
     * http://songsearch.kugou.com/song_search_v2?&keyword=%E8%80%81%E7%94%B7%E5%AD%A9&page=1&pagesize=1&showtype=1&iscorrection=1&platform=WebFilter
     */
    public static final String KUGOU_URL = "http://songsearch.kugou.com/song_search_v2?&keyword=%s&page=1&pagesize=%d&iscorrection=1&platform=WebFilter&showtype=0&author=bwn";
    public static final String REQUEST_OPENAI_TOKEN_URL = "https://chat.openai.com/api/auth/session";
    //ScalarsConverterFactory
    /**
     *
     */
    public static final String TRANSLATE_URL = "http://fanyi.youdao.com";
    /**
     * http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=EF1640A750D7944C7E0E6B22C491CB2A
     */
    public static final String KUGOU_FILE_URL = "http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=%s";
    //    public static final String DEFAULT_ROBOT_ICON ="http://q1.qlogo.cn/g?b=qq&nk=202927128&s=100";
//    public static final String DEFAULT_ROBOT_ICON = "https://timgsa.baidu.com/timg?image&quality=80&size=b999
// 9_10000&sec=1517164385321&di=dbc634e0e3dcf11c22bf41eff4815088&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D2258373738%2C1974479502%26fm%3D214%26gp%3D0.jpg";
    //小冰头像地址
    public static final String DEFAULT_ROBOT_XIAOBIN_ICON = "http://image.coolapk.com/apk_logo/2018/0129/xiaobin-126207-o_1c4uorglj7v0dco19q7kbm1249q-uid-669281@350x350.jpg";
    public static final String DEFAULT_ROBOT_ICON = DEFAULT_ROBOT_XIAOBIN_ICON;
    //    public static final String DEFAULT_ROBOT_ICON = "http://image.coolapk.com/apk_logo/2017/0506/qq-for-137859-o_1bfctmgg81u5cj2gpgn12ee1hon1i-uid-669281.png";
//    public static final String DEFAULT_ROBOT_ICON = "http://q1.qlogo.cn/g?b=qq&nk=202927128&s=100";
    public static final String MY_URL = "https://qssq666.gitee.io/software/s.html";
    public static final String DEFAULT_MUSIC_URL = "https://github.com/qssq/feedback/issues";
    public static final java.lang.String FILE_HAS_TEST = "file_has_test";
    public static final String ALL_PERSON = "所有人";
    public static final String ALL_PERSON_AITE = "@艾特全体";
    public static final String ALL_PERSON_1 = "全体";
    public static final String CUSTOM_DB_PATH = "key_app_db_path";
    public static final String SP_DEFAULT_REPLY_API_INDEX = "SP_DEFAULT_REPLY_API_INDEX";
    public static final String TIP_PLEASE_GROUP_CHAT = "请在群里发送此命令";
    public static final String DEFAULT_GAG_RAG_GLOBAL_DISABLE_WEBSITE = "greghttp|\\.cn|\\.com|\\.org|\\.net|\\.tk|\\.me";
    public static String DEFAULT_GROUP = "225832062";
    public static String DEFAULT_QQ_MUIC_URL = "https://i.gtimg.cn/open/app_icon/01/07/98/56/1101079856_100_m.png?date=20180123";

    public static String formatNickname(String qq, String nickname) {
        return String.format("%s(%s)", qq, nickname);
    }
}

