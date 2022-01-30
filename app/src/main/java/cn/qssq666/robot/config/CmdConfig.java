package cn.qssq666.robot.config;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qssq666.robot.anotation.REMARK;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.utils.StringUtils;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 */

public class CmdConfig {
    // ignore_start
    @REMARK(id = 1, group = "屏蔽操作", remark = "临时屏蔽群或者屏蔽qq")
    public static final String IGNORE_PINGBI = "屏蔽";//"屏蔽";
    @REMARK(id = 1, group = "屏蔽操作", remark = "")
    public static final String IGNORE_QUXIAO_PIGNBI = "解除屏蔽";//"解除屏蔽";

    @REMARK(id = 1, group = "屏蔽操作", remark = "清空所有屏蔽")
    public static final String CLEAR_PINBI = "清空屏蔽";//"解除屏蔽";
    @REMARK(id = 1, group = "屏蔽操作", remark = "清空所有屏蔽")
    public static final String CLEAR_PINBI_1 = "清除屏蔽";//"解除屏蔽";


    @REMARK(id = 1, group = "无视功能", remark = "解决两个机器人同时被响应问题,输入此命令机器人将无视自己")
    public static final String IGNORE_TEMP_IGNORE_ME = "无视模式";//"无视模式";
    @REMARK(id = 1, group = "无视功能", remark = "")
    public static final String IGNORE_TEMP_IGNORE_ME_DISABLE = "关闭无视模式";//"无视模式";


    @REMARK(id = 1, group = "启用本群", remark = "把本群添加到白名单中,通常情况添加命令只对机器人自己发送消息响应,非机器人自身而是管理员则需要在群里艾特机器人输入如下不带参数命令")

    public static final String ADD_WHITE_NAMES_1 = "添加白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "启用本群", remark = "")
    public static final String ADD_WHITE_NAMES_2 = "加白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "启用本群", remark = "")
    public static final String ADD_WHITE_NAMES_3 = "启用机器人";//"解除屏蔽";
    @REMARK(id = 1, group = "启用本群", remark = "")
    public static final String ADD_WHITE_NAMES = "添加群白名单";//"解除屏蔽";


    @REMARK(id = 1, group = "停用本群", remark = "把本群从白名单中移除")
    public static final String REMOVE_WHITE_NAMES = "移除群白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_1 = "移除白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_2 = "删除白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_3 = "删白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_4 = "停用白名单";//"解除屏蔽";

    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_6 = "删除群白名单";//"解除屏蔽";
    @REMARK(id = 1, group = "停用本群", remark = "")
    public static final String REMOVE_WHITE_NAMES_5 = "停用机器人";//"解除屏蔽";
    public static final String LIST_WHITE_NAME = "群白名单";//"解除屏蔽";
    public static final String LIST_QQ_IGNORES = "忽略QQ";//"解除屏蔽";

    @REMARK(id = 1, group = "机器人状态", remark = "检查机器人是否已被宿主绑定")
    public static final String STATE_INFO = "状态";

    @REMARK(id = 1, group = "系统", remark = "开启wifi adb|需要root")
    public static final String WIFI_ADB = "无线调试";

    @REMARK(id = 1, group = "机器人信息", remark = "")
    public static final String VERSION = "版本";

    @REMARK(id = 1, group = "系统", remark = "查询更新日志")

    public static final String VERSION_UPDATE = "更新日志";

    @REMARK(id = 1, group = "系统", remark = "查看帮助信息")
    public static final String HELP = "帮助";//"状态";
    @REMARK(id = 1, group = "菜单", remark = "查看帮助信息")
    public static final String HELP_MENU = "菜单";//"状态";
    @REMARK(id = 1, group = "系统", remark = "")
    public static final String HELP_3 = "功能";//"状态";
    @REMARK(id = 1, group = "系统", remark = "")
    public static final String CMD = "命令";//"状态";
    @REMARK(id = 1, group = "系统", remark = "")
    public static final String CMD1 = "cmd";//"状态";

    @REMARK(id = 1, group = "菜单", remark = "")
    public static final String CARD_MSG = "卡片";//";
    @REMARK(id = 1, group = "禁言", remark = "支持楼层禁言私聊控制群\n[[1-200|qq号 禁言时间]|\n[群号 [1-200|qq号 禁言时间(可空)]\n如gag 1 0 解除楼上禁言,gag 艾特一个人 100分钟 禁言这个人100分钟")
    public static final String GAG = "禁言";//"状态";
    @REMARK(id = 1, group = "禁言", remark = "")
    public static final String GAG1 = "gag";//"状态";
    @REMARK(id = 1, group = "禁言", remark = "")
    public static final String BIZUI = "闭嘴";//"状态";
    public static final String GAG_SHUTUP = "shutup";//"状态";
    @REMARK(id = 1, group = "踢", remark = "支持楼层踢人[[1-200|qq号 true|false]|[群号 [1-200|qq号 1|0]|群消息:直接输入踢可以踢掉楼上发言者，也可以艾特一个人然后输入T")
    public static final String KICK = "踢";//"状态";
    @REMARK(id = 1, group = "踢", remark = "")
    public static final String KICK_2 = "T";//"状态";
    @REMARK(id = 1, group = "踢", remark = "")
    public static final String KICK_1 = "kick";//"状态";
    public static final String FLOOR = "floor";//"状态";

    @REMARK(id = 1, group = "加敏感词", remark = "添加敏感词")
    public static final String ADD_GAG = "加敏感词";
    public static final String DEL_GAG = "删敏感词";
    // return DBHelper.getGagKeyWord(AppContext.dbUtils).insert(accountBean);

    public static final String AITE_CMD = "艾特";//"状态";
    @REMARK(id = 1, group = "添加词库", remark = "问|问1,问2 答|答1,答2")
    public static final String ADD_WORD_CMD = "添加词库";//"状态";

    public static final String DELETE_WORD_CMD = "删除词库";//"状态";
    public static final String UPDATE_WORD_CMD = "更新词库";//"状态";
    //    public static final String SUPER_MAANGER = "管理";//"状态";
    @REMARK(id = 1, group = "查看管理员", remark = "查看机器人的所有管理员")
    public static final String MAANGER_ALL = "管理员";//"状态";
    @REMARK(id = 1, group = "查看超级管理员", remark = "查看超级管理员")
    public static final String SUPER_MAANGER_CMD1 = "超级管理员";//"状态";
    @REMARK(id = 1, group = "管理员增删", remark = "")
    public static final String ADD_MANAGER = "添加管理员";//"状态";
    @REMARK(id = 1, group = "管理员增删", remark = "")
    public static final String DELETE_MANAGER = "删除管理员";//"状态";

    @REMARK(id = 1, group = "管理员增删", remark = "")
    public static final String ADD_CURRENT_GROUP_MAANAGER = "置本群管理";//"状态";

    @REMARK(id = 1, group = "二维码", remark = "")
    public static final String QRCODE = "qrcode";//"状态";
    @REMARK(id = 1, group = "二维码", remark = "")
    public static final String QRCODE_1 = "二维码";//"状态";
    @REMARK(id = 1, group = "管理员增删", remark = "")
    public static final String REMOVE_CURRENT_GROUP_MAANAGER = "移本群管理";//"状态";


    public static final String CLEAR_TASK = "清除任务";

    public static final String SHOW_JIANRONG = "兼容信息";


    @REMARK(id = 1, group = "点歌", remark = "点歌支持参数为歌曲名 或者歌曲名 10 或者 歌曲名 下载地址 或艾特一个人 然后输入歌曲名")

    public static final String FECTCH_MUSIC1 = "来首";
    @REMARK(id = 1, group = "点歌", remark = "")
    public static final String FECTCH_MUSIC = "点歌";
    @REMARK(id = 1, group = "点歌", remark = "")
    public static final String FECTCH_MUSIC2 = "我想听";


    @REMARK(id = 1, group = "翻译", remark = "")
    public static final String TRANSLATE = "翻译";//"状态";


    @REMARK(id = 1, group = "改名片", remark = "")
    public static final String MODIFY_CARD_NAME = "改名";
    @REMARK(id = 1, group = "改名片", remark = "")
    public static final String MODIFY_CARD_NAME2 = "修改群名片";
    @REMARK(id = 1, group = "改名片", remark = "")
    public static final String MODIFY_CARD_NAME3 = "修改名片";
    @REMARK(id = 1, group = "改名片", remark = "")
    public static final String MODIFY_CARD_NAME1 = "起名";


    public static final String TEST_URL = "访问网址";
    public static final String PLUGIN_INFO = "插件信息";
    public static final String TASK = "任务";

    public static final String SEND_MSG = "发消息";
    public static final String WEIGUI_ = "违规记录";
    @REMARK(id = 1, group = "傻瓜模式", remark = "输入此命令将自动响应,不管是私聊或者群聊，不管有没有添加到群白名单,此命令只要是管理员就可以响应。")
    public static final String RESPONSE_All_CMD = "傻瓜模式";


    @REMARK(id = 1, group = "搜照片", remark = "搜美女、帅哥、人物、风景图片")
    public static final String SEARCH_2 = "看图";
    @REMARK(id = 1, group = "搜照片", remark = "")
    public static final String SEARCH = "搜";
    @REMARK(id = 1, group = "搜照片", remark = "")
    public static final String SEARCH_1 = "search";


    @REMARK(id = 1, group = "字转图", remark = "文字转图片")
    public static final String TEXT_2PIC = "字转图";

    @REMARK(id = 1, group = "字转图", remark = "")
    public static final String TEXT_2PIC_1 = "t2p";

    public static final String CONFIG = "配置";
    public static final String BATCH_ = "批处理";
    @REMARK(id = 1, group = "任务相关", remark = "立即执行任务中的任务")
    public static final String LIJI_EXECUTE = "立即执行";


    @REMARK(id = 1, group = "点赞", remark = "给用户点赞,如点赞 35068264 点赞 ")
    public static final String ADD_LIKE = "赞";//"解除屏蔽";
    @REMARK(id = 1, group = "撤回", remark = "根据账户此人最近发言的所有信息")
    public static final String REVOKE_MSG = "撤回";
    @REMARK(id = 1, group = "撤回", remark = "根据账户此人最近发言的所有信息")
    public static final String REVOKE_MSG_1 = "revoke";

    @REMARK(id = 1, group = "高级功能", remark = "")
    public static final String VOICE_CALL = "call";//"状态";

    // ignore_end


    /**
     * 寻找是否包含空格，没有空格的直接加上去 找不到就返回 空
     *
     * @param str
     * @return Pair<String       ,               String> 前者代表命令 或者代表参数字符串
     */
    public static Pair<String, String> fitParam(String str) {
        //全角转换为半角
//        str = str.replaceAll("  ", " ");

        str = StringUtils.deleteMulitiSpace(str);
//            str = str.replaceAll("　", " ");

        //多个空格化为一个空格
        str = str.trim();
        Field[] declaredFields = CmdConfig.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                if (declaredField.isSynthetic()) {
                    continue;
                }
                if (declaredField.getName().equals("serialVersionUID")) {
                    continue;
                }
                String cmd = declaredField.get(null) + "";
                if (cmd == null) {
                    continue;
                }

                if (str.startsWith(cmd)) {
                    String textContent = str.replaceFirst(cmd, "");//只替换一个 ignore_include
                    String args = StringUtils.trim(textContent);
                    return Pair.create(cmd, args);

                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class CmdInfo {

        private String usage;

        public List<String> getCommands() {
            return commands;
        }

        public void setCommands(List<String> commands) {
            this.commands = commands;
        }

        List<String> commands = new ArrayList<>();

    }

    public static String printSupportCmd() {

        StringBuffer sb = new StringBuffer();
        sb.append(Cns.ROBOT_NAME + "操作命令(大部分只对管理员有效)\n");
        HashMap<String, CmdInfo> map = new HashMap<>();
        String otherName = "未分组";
        CmdInfo otherCmdInfo = new CmdInfo();
        map.put(otherName, otherCmdInfo);
        Field[] declaredFields = CmdConfig.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isSynthetic()) {
                continue;
            }

            if (declaredField.getName().equals("serialVersionUID")) {
                continue;
            }


            try {
                String cmd = declaredField.get(null) + "";
                if (cmd == null) {
                    continue;
                }
//                sb.append(cmd+"");


                if (declaredField.isAnnotationPresent(REMARK.class)) {
//                    sb.append("\n->" + declaredField.getAnnotation(REMARK.class).group() + "\n");
                    CmdInfo cmdInfo = null;

                    REMARK annotation = declaredField.getAnnotation(REMARK.class);
                    String group = annotation.group();
                    if (map.containsKey(group)) {
                        cmdInfo = map.get(group);
                    } else {


                        cmdInfo = new CmdInfo();

                        map.put(group, cmdInfo);

                    }
                    if (TextUtils.isEmpty(cmdInfo.usage)) {
                        cmdInfo.usage = annotation.remark();
                    }

                    cmdInfo.getCommands().add(cmd);


                } else {


                    otherCmdInfo.commands.add(cmd);


                }


//                sb.append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                Log.e("ERROR:", "printSupportCmd:" + declaredField.getName(), e);
//                e.printStackTrace();
            }
        }

        for (Map.Entry<String, CmdInfo> entry : map.entrySet()) {


            sb.append("====" + entry.getKey()
                    + "====\n");//ignore_include

            if (!TextUtils.isEmpty(entry.getValue().usage)) {
                sb.append("解释:" + entry.getValue().usage + "\n");//ignore_include
            }
            sb.append("命令名:\n[");
            List<String> commands = entry.getValue().getCommands();
            for (int i = 0; i < commands.size(); i++) {
                String currentCmd = commands.get(i);
                sb.append(currentCmd + "");
                if (i < commands.size() - 1) {
                    sb.append(",");

                }
            }
            sb.append("]\n");

        }
        return sb.toString();
    }


    public static class ChildCmd {
        //ignore_start
        public static final String CONFIG_SHOW = "首选项";

        // screencap -p /sdcard/screenshots/01.png
        public static final String CONFIG_SCREENCAP= "截图";
        public static final String CONFIG_VIEW_PIC= "看图";

        public static final String CONFIG_USER_CARD = "名片";
        public static final String CONFIG_GROUP_INFO = "群信息";
        public static final String CONFIG_QQINFO = "QQ信息";
        public static final String CONFIG_MODIFY = "修改首选项";
        public static final String CONFIG_RELOAD = "重载";
        public static final String CONFIG_RESTART= "重启";
        public static final String CONFIG_KILL= "自杀";
        public static final String CONFIG_SQL = "SQL";
        public static final String CONFIG_EXECUTE = "运行";
        public static final String CONFIG_LAUNCHER_APP = "启动";
        public static final String CONFIG_OPEN = "打开";
        public static final String CONFIG_CARD = "卡片";
        public static final String CONFIG_PRINT = "print";
        public static final String CONFIG_ADD_VAR = "添加变量";
        public static final String CONFIG_DELETE_VAR = "删除变量";
        public static final String CONFIG_MODIFY_VAR = "修改变量";
        public static final String CONFIG_EXIT_GROUP = "退群";
        public static final String CONFIG_CAST_URI_ENCODE = "地址编码";
        public static final String CONFIG_CAST_URI_DECODE = "地址解码";
        public static final String CONFIG_WEB_ENCODE = "网页编码";
        public static final String CONFIG_WEB_DECODE = "网页解码";
        public static final String CONFIG_JSON_ENCODE = "json解码";
        public static final String CONFIG_EXIT_DISCUSSION = "退讨论组";

        //ignore_end
    }
}
