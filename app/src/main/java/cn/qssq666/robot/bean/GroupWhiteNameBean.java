package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;

import cn.qssq666.db.DBUtils;
import cn.qssq666.db.anotation.DBIgnore;
import cn.qssq666.db.anotation.Table;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.interfaces.TwoDataHolder;
import cn.qssq666.robot.plugin.sdk.interfaces.IGroupConfig;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.ParseUtils;

/**
 * Created by qssq on 2017/12/20 qssq666@foxmail.com
 */

@Table("groupconfig")
public class GroupWhiteNameBean extends AccountBean implements TwoDataHolder, Cloneable, IGroupConfig {
    public boolean isCurrentGroupAdmin() {
        return isCurrentGroupAdmin;
    }

    @DBIgnore
    private boolean isCurrentGroupAdmin;

    public void setAllowGenerateCardMsg(boolean allowGenerateCardMsg) {
        this.allowGenerateCardMsg = allowGenerateCardMsg;
    }

    private boolean allowGenerateCardMsg = true;

    public boolean isEnglishdialogue() {
        return englishdialogue;
    }

    public void setEnglishdialogue(boolean englishdialogue) {
        this.englishdialogue = englishdialogue;
    }

    private boolean englishdialogue;

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins;
    }

    private String admins;

    public boolean isReplyTranslate() {
        return replyTranslate;
    }

    public void setReplyTranslate(boolean replyTranslate) {
        this.replyTranslate = replyTranslate;
    }

    private boolean replyTranslate = true;

    public GroupWhiteNameBean() {

    }

    public GroupWhiteNameBean(String account) {
        super(account);
    }

    @Override
    public AccountBean setAccount(String account) {
        super.setAccount(account);
        return GroupWhiteNameBean.this;
    }


    public boolean isDisable() {
        return disable;
    }


    public void setDisable(boolean disable) {
        this.disable = disable;
    }


    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    private String postfix = "";

    public boolean isBreaklogic() {
        return breaklogic;
    }

    public void setBreaklogic(boolean breaklogic) {
        this.breaklogic = breaklogic;
    }

    public boolean isCmdsilent() {
        return cmdsilent;
    }

    public void setCmdsilent(boolean cmdsilent) {
        this.cmdsilent = cmdsilent;
    }

    private boolean cmdsilent;
    /**
     * 当触发违规后逻辑是否继续执行
     */
    private boolean breaklogic;


    public int getPicgagsecond() {
        return picgagsecond;
    }

    public void setPicgagsecond(int picgagsecond) {
        this.picgagsecond = picgagsecond;
    }

    private int picgagsecond = 60 * 5;

    public int getMistakethanwarncount() {
        if (mistakethanwarncount == 0) {
            mistakethanwarncount = getMistakecount() >= 2 ? getMistakecount() / 2 : 30;
        }

        return mistakethanwarncount;
    }

    public void setMistakethanwarncount(int mistakethanwarncount) {
        this.mistakethanwarncount = mistakethanwarncount;
    }

    private int mistakethanwarncount = 3;

    public String getPicgagsecondtip() {
        if (TextUtils.isEmpty(picgagsecondtip)) {
            return "禁止发图片哈";
        }
        return picgagsecondtip;
    }

    public void setPicgagsecondtip(String picgagsecondtip) {
        this.picgagsecondtip = picgagsecondtip;
    }

    private String picgagsecondtip = "本群禁止发图片";


    /**
     * 违规启用艾特回复
     *
     * @return
     */
    public boolean isBannedaite() {
        return bannedaite;
    }

    public void setBannedaite(boolean bannedaite) {
        this.bannedaite = bannedaite;
    }

    /**
     * 违规的时候提示 艾特群成员进行操作回复
     */
    private boolean bannedaite;

    public boolean isAllowqrcode() {
        return allowqrcode;
    }

    public void setAllowqrcode(boolean allowqrcode) {
        this.allowqrcode = allowqrcode;
    }

    private boolean allowqrcode=true;

    public String getUpgradeinfo() {
        return upgradeinfo;
    }

    public void setUpgradeinfo(String upgradeinfo) {
        this.upgradeinfo = upgradeinfo;
    }

    private String upgradeinfo;

    public boolean isLocalword() {
        return localword;
    }

    public void setLocalword(boolean localword) {
        this.localword = localword;
    }


    public boolean isNetword() {
        return netword;
    }

    public void setNetword(boolean netword) {
        this.netword = netword;
    }


    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    private boolean admin = true;
    private boolean localword = true;

    public boolean isKickviolations() {
        return kickviolations;
    }

    public void setKickviolations(boolean kickviolations) {
        this.kickviolations = kickviolations;
    }

    public boolean isKickviolationsforver() {
        return kickviolationsforver;
    }

    public void setKickviolationsforver(boolean kickviolationsforver) {
        this.kickviolationsforver = kickviolationsforver;
    }

    private boolean kickviolations = false;
    private boolean kickviolationsforver = false;
    private boolean netword = true;
    private boolean bannedword = true;

    public String getVideogagtip() {
        return videogagtip;
    }

    public void setVideogagtip(String videogagtip) {
        this.videogagtip = videogagtip;
    }

    public int getVideogagminute() {
        return videogagminute;
    }

    public void setVideogagminute(int videogagminute) {
        this.videogagminute = videogagminute;
    }

    private String videogagtip;
    private int videogagminute;


    public boolean isBannevideo() {
        return bannevideo;
    }

    public void setBannevideo(boolean bannevideo) {
        this.bannevideo = bannevideo;
    }

    private boolean bannevideo = false;
    private boolean nicknameban = false;
    private boolean illegalnickname = false;
    private boolean frequentmsg = true;

    public long getBanredpacketminute() {
        return banredpacketminute;
    }

    public void setBanredpacketminute(long banredpacketminute) {
        this.banredpacketminute = banredpacketminute;
    }

    private boolean banpasswordredpacket = false;


    public String getBanredpackettip() {

        if (TextUtils.isEmpty(banredpackettip)) {
            return "禁止发红包哈";
        }
        return banredpackettip;
    }

    public void setBanredpackettip(String banredpackettip) {
        this.banredpackettip = banredpackettip;
    }

    private long banredpacketminute;
    private String banredpackettip = "本群禁止发%s红包哈!";

    public boolean isBanvoiceredpacket() {
        return banvoiceredpacket;
    }

    public void setBanvoiceredpacket(boolean banvoiceredpacket) {
        this.banvoiceredpacket = banvoiceredpacket;
    }

    public boolean isBannormalredpacket() {
        return bannormalredpacket;
    }

    public void setBannormalredpacket(boolean bannormalredpacket) {
        this.bannormalredpacket = bannormalredpacket;
    }

    public boolean isBanexclusiveredpacket() {
        return banexclusiveredpacket;
    }

    public void setBanexclusiveredpacket(boolean banexclusiveredpacket) {
        this.banexclusiveredpacket = banexclusiveredpacket;
    }

    private boolean banvoiceredpacket = false;
    private boolean bannormalredpacket = false;
    private boolean banexclusiveredpacket = false;
    private boolean banvoice = false;
    private boolean bancardmsg = false;

    public boolean isNeedaite() {
        return needaite;
    }

    public void setNeedaite(boolean needaite) {
        this.needaite = needaite;
    }

    public boolean isReplayatperson() {
        return replayatperson;
    }

    public void setReplayatperson(boolean replayatperson) {
        this.replayatperson = replayatperson;
    }

    private boolean needaite;
    private boolean replayatperson;

    public boolean isSelfcmdnotneedaite() {
        return selfcmdnotneedaite;
    }

    public void setSelfcmdnotneedaite(boolean selfcmdnotneedaite) {
        this.selfcmdnotneedaite = selfcmdnotneedaite;
    }

    private boolean selfcmdnotneedaite = false;

    public boolean isAllowmusic() {
        return allowmusic;
    }

    public void setAllowmusic(boolean allowmusic) {
        this.allowmusic = allowmusic;
    }

    private boolean allowmusic = true;

    public boolean isAllowModifyCard() {
        return allowModifyCard;
    }

    public void setAllowModifyCard(boolean allowModifyCard) {
        this.allowModifyCard = allowModifyCard;
    }

    private boolean allowModifyCard = true;

    public boolean isAllowMenu() {
        return allowMenu;
    }

    public void setAllowMenu(boolean allowMenu) {
        this.allowMenu = allowMenu;
    }

    private boolean allowMenu;

    public boolean isAllowtext2pic() {
        return allowtext2pic;
    }

    public void setAllowtext2pic(boolean allowtext2pic) {
        this.allowtext2pic = allowtext2pic;
    }

    public boolean isAllowsearchpic() {
        return allowsearchpic;
    }

    public void setAllowsearchpic(boolean allowsearchpic) {
        this.allowsearchpic = allowsearchpic;
    }

    private boolean allowtext2pic = true;

    public boolean isAllowzan() {
        return allowzan;
    }

    public void setAllowzan(boolean allowzan) {
        this.allowzan = allowzan;
    }

    private boolean allowzan = true;
    private boolean allowsearchpic = true;
    private boolean bancall = false;
    private boolean banpic = false;

    public boolean isRevokemsg() {
        return revokemsg;
    }

    public void setRevokemsg(boolean revokemsg) {
        this.revokemsg = revokemsg;
    }

    private boolean revokemsg = true;

    public boolean isAllowTranslate() {
        return allowTranslate;
    }

    public void setAllowTranslate(boolean allowTranslate) {
        this.allowTranslate = allowTranslate;
    }

    private boolean allowTranslate = true;
    private boolean joingroupreply = true;
    private String joingroupword = "欢迎加入$group群,要多多活跃哈!";
    private String remark = "";

    public boolean isAutornamecard() {
        return autornamecard;
    }

    public void setAutornamecard(boolean autornamecard) {
        this.autornamecard = autornamecard;
    }

    /**
     * 自动修改名片，名片格式为模板参数
     */
    private boolean autornamecard = true;

    public String getNameCardvarTemplete() {
        return nameCardvarTemplete;
    }

    public void setNameCardvarTemplete(String nameCardvarTemplete) {
        this.nameCardvarTemplete = nameCardvarTemplete;
    }

    private String nameCardvarTemplete = "$nickname-$area-$phone-N";

    public int getNotparamgagminute() {
        return notparamgagminute = notparamgagminute == 0 ? 1 : notparamgagminute;
    }

    public void setNotparamgagminute(int notparamgagminute) {
        this.notparamgagminute = notparamgagminute;
    }

    private int notparamgagminute = 1;
    private String groupnickanmekeyword = "^([\\u4e00-\\u9fa5_a-zA-Z0-9]{1,5})[\\_\\-\\--]([\\u4e00-\\u9fa5]{1,5})[\\_\\-\\--]([\\u4e00-\\u9fa5_a-zA-Z0-9]{1,12})[\\_\\-\\--][R|N]$";// INGNOE_INCLUDE

    public boolean isBancardmsg() {
        return bancardmsg;
    }

    public void setBancardmsg(boolean bancardmsg) {
        this.bancardmsg = bancardmsg;
    }

    public String getVoicegagtip() {
        if (TextUtils.isEmpty(voicegagtip)) {
            return "禁止发语音哈";
        }
        return voicegagtip;
    }

    public void setVoicegagtip(String voicegagtip) {
        this.voicegagtip = voicegagtip;
    }

    public long getVoicegagminute() {
        return voicegagminute;
    }

    public void setVoicegagminute(long voicegagminute) {
        this.voicegagminute = voicegagminute;
    }

    private String voicegagtip = "本群禁止发语音";

    public String getCardmsggagtip() {
        if (TextUtils.isEmpty(cardmsggagtip)) {
            return "禁止发卡片消息哈";
        }
        return cardmsggagtip;
    }

    public void setCardmsggagtip(String cardmsggagtip) {
        this.cardmsggagtip = cardmsggagtip;
    }

    public long getCardmsgminute() {
        return cardmsgminute;
    }

    public void setCardmsgminute(long cardmsgminute) {
        this.cardmsgminute = cardmsgminute;
    }

    private String cardmsggagtip = "本群禁止发卡片消息";
    private long voicegagminute = 5;
    private long cardmsgminute = 5;
    @Deprecated
    private String groupnickanmekeyword1 = "";
    /**
     * 禁用秒
     */
    private int groupnickanmegagtime = 60;

    public int getMistakecount() {
        return mistakecount;
    }

    public void setMistakecount(int mistakecount) {
        this.mistakecount = mistakecount;
    }

    /**
     * 犯错最大禁言次数
     */
    private int mistakecount = 30;

    /**
     * ^[\u4e00-\u9fa5]{1,2}\-[\u4e00-\u9fa5]{1,2}\-NR|R$
     */
    public String getGroupnicknamegagtip() {
        if (TextUtils.isEmpty(groupnicknamegagtip)) {
            return "";
        }
        return groupnicknamegagtip;
    }

    public void setGroupnicknamegagtip(String groupnicknamegagtip) {
        this.groupnicknamegagtip = groupnicknamegagtip;
    }

    /**
     * 昵称不合法请输入如
     */
    private String groupnicknamegagtip = "账号$u的昵称$nickname 】不合法!执行禁言$any 分钟，请输入如非root机【阿毛-深圳-红米4A-N】，昵称最长4位，地区最长4位最短1位只能中文，手机型号最长10位，最后一个-后面只能是R或N(没有root)";


    //            "账号$uin的昵称$nickname 不合法!执行禁言$any分钟，请输入如非root机【阿毛-深圳-红米4A-N】，昵称最长4位，地区最长4位最短1位只能中文，手机型号最长10位，最后一个-后面只能是R或N(没有root)";
    private boolean redpackettitlebanedword;


    public boolean isAccumlativegagdata() {
        return accumlativegagdata;
    }

    public void setAccumlativegagdata(boolean accumlativegagdata) {
        this.accumlativegagdata = accumlativegagdata;
    }

    /**
     * 累加违规总数
     */
    private boolean accumlativegagdata;

    public boolean isOnlyrecordwordgagcount() {
        return onlyrecordwordgagcount;
    }

    public void setOnlyrecordwordgagcount(boolean onlyrecordwordgagcount) {
        this.onlyrecordwordgagcount = onlyrecordwordgagcount;
    }

    private boolean onlyrecordwordgagcount;

    public String getCountthantip() {
        return TextUtils.isEmpty(countthantip) ? "违规次数超出,踢出" : countthantip;
    }

    public void setCountthantip(String countthantip) {
        this.countthantip = countthantip;
    }

    private String countthantip = " 已违规%d次 超过最大次数%d次数执行踢出操作！";
    /**
     * 秒
     */
    private int frequentmsgduratiion = 30;
    /**
     * 发言多少条
     */
    private int frequentmsgcount = 30;

    public boolean isFitercommand() {
        return fitercommand;
    }

    public void setFitercommand(boolean fitercommand) {
        this.fitercommand = fitercommand;
    }

    /**
     * 禁言多少分钟
     */

    /**
     * 比如求禁言，机器人回复了禁言你干嘛，这时候又会再次触发回复了》
     */
    private boolean fitercommand;
    private int frequentmsggagtime = 60 * 60;

    public boolean isBannedword() {
        return bannedword;
    }

    public void setBannedword(boolean bannedword) {
        this.bannedword = bannedword;
    }


    /**
     * 昵称敏感词检测
     *
     * @return
     */
    public boolean isNicknameban() {
        return nicknameban;
    }

    public void setNicknameban(boolean nicknameban) {
        this.nicknameban = nicknameban;
    }


    public boolean isIllegalnickname() {
        return illegalnickname;
    }

    public void setIllegalnickname(boolean illegalnickname) {
        this.illegalnickname = illegalnickname;
    }


    public boolean isFrequentmsg() {
        return frequentmsg;
    }

    public void setFrequentmsg(boolean frequentmsg) {
        this.frequentmsg = frequentmsg;
    }


    public boolean isBanpasswordredpacket() {
        return banpasswordredpacket;
    }

    public void setBanpasswordredpacket(boolean banpasswordredpacket) {
        this.banpasswordredpacket = banpasswordredpacket;
    }


    public boolean isBanvoice() {
        return banvoice;
    }

    public void setBanvoice(boolean banvoice) {
        this.banvoice = banvoice;
    }

    //   @Bindable
    public boolean isBancall() {
        return bancall;
    }

    public void setBancall(boolean bancall) {
        this.bancall = bancall;
        //    notifyPropertyChanged(BR.bancall);
    }

    //   @Bindable
    public boolean isBanpic() {
        return banpic;
    }

    public void setBanpic(boolean banpic) {
        this.banpic = banpic;
        //     notifyPropertyChanged(BR.banpic);
    }


    //  @Bindable
    public boolean isJoingroupreply() {
        return joingroupreply;
    }

    public void setJoingroupreply(boolean joingroupreply) {
        this.joingroupreply = joingroupreply;
        //     notifyPropertyChanged(BR.joingroupreply);
    }


    //  @Bindable
    public String getJoingroupword() {
        return joingroupword;
    }

    public void setJoingroupword(String joingroupword) {
        this.joingroupword = joingroupword;
        //     notifyPropertyChanged(BR.joingroupword);
    }


    public boolean isRedpackettitlebanedword() {
        return redpackettitlebanedword;
    }

    public void setRedpackettitlebanedword(boolean redpackettitlebanedword) {
        this.redpackettitlebanedword = redpackettitlebanedword;
    }


    //    @Bindable
    public int getFrequentmsgduratiion() {
        return frequentmsgduratiion;
    }

    public void setFrequentmsgduratiion(int frequentmsgduratiion) {
        this.frequentmsgduratiion = frequentmsgduratiion;
//         notifyPropertyChanged(BR.frequentmsgduratiion);
    }

    //   @Bindable
    public int getFrequentmsgcount() {
        return frequentmsgcount;
    }

    public void setFrequentmsgcount(int frequentmsgcount) {
        this.frequentmsgcount = frequentmsgcount;
        //     notifyPropertyChanged(cn.qssq666.robot.BR.frequentmsgcount);
    }

    //    @Bindable
    public int getFrequentmsggagtime() {
        return frequentmsggagtime;
    }

    public void setFrequentmsggagtime(int frequentmsggagtime) {
        this.frequentmsggagtime = frequentmsggagtime;
//        notifyPropertyChanged(cn.qssq666.robot.BR.frequentmsggagtime);
    }


    //    @Bindable
    public String getGroupnickanmekeyword() {
        return groupnickanmekeyword;
    }


    public void setGroupnickanmekeyword(String groupnickanmekeyword) {
        this.groupnickanmekeyword = groupnickanmekeyword;
        //   notifyPropertyChanged(cn.qssq666.robot.BR.groupnickanmekeyword);
    }

    //    @Bindable
    public String getGroupnickanmekeyword1() {
        return groupnickanmekeyword1;
    }

    public void setGroupnickanmekeyword1(String groupnickanmekeyword1) {
        this.groupnickanmekeyword1 = groupnickanmekeyword1;
//        notifyPropertyChanged(cn.qssq666.robot.BR.groupnickanmekeyword1);
    }


    //    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
//        notifyPropertyChanged(BR.remark);
    }


    /**
     * 禁言多少秒
     *
     * @return
     */
    //  @Bindable
    public int getGroupnickanmegagtime() {
        return groupnickanmegagtime;
    }

    public void setGroupnickanmegagtime(int groupnickanmegagtime) {
        this.groupnickanmegagtime = groupnickanmegagtime;
    }


    @Override
    public String getShowTitle() {
        return "群号:" + getAccount();
    }

    @Override
    public String getShowContent() {


        return "备注:" + getRemark();
    }


    public String toStringJSON() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return super.toString() + JSON.toJSONString(this);
    }

    public String getConfig() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("群备注:%s\n", remark));
        sb.append(String.format("是否禁用:%b\n", disable));
        sb.append(String.format("机器人有管理权限:%b\n", admin));
        sb.append(String.format("回复需要艾特:%b\n", isNeedaite()));
        sb.append(String.format("回复艾特发言人:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(replayatperson)));
        sb.append(String.format("是否启用点歌:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(isAllowmusic()) + ""));
        sb.append(String.format("是否启用本地词库:%b\n", localword));
        sb.append(String.format("是否启用网络词库:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(netword)));
        sb.append(String.format("机器人是否是管理员:%b\n",ParseUtils.parseBoolean2ChineseBooleanStr( admin)));
        sb.append(String.format("禁止发专属红包:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(banexclusiveredpacket)));
        sb.append(String.format("禁止发普通红包:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(bannormalredpacket)));
        sb.append(String.format("禁止发语音红包:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(banvoiceredpacket)));
        sb.append(String.format("禁止发口令红包:%b\n",ParseUtils.parseBoolean2ChineseBooleanStr( banpasswordredpacket)));
        sb.append(String.format("禁止发图片消息:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(banpic)));
        sb.append(String.format("禁止发语音消息:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(banvoice)));
        sb.append(String.format("禁止发视频消息:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(bannevideo)));
        sb.append(String.format("禁止发卡片消息:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(bancardmsg)));
        sb.append(String.format("违规次数是否有上限:%b\n",ParseUtils.parseBoolean2ChineseBooleanStr( accumlativegagdata)));
        sb.append(String.format("只累计发言违规:%b\n", isOnlyrecordwordgagcount()));
        sb.append(String.format("检测刷屏:%b\n",ParseUtils.parseBoolean2ChineseBooleanStr( frequentmsg)));
        sb.append(String.format("检测群名片敏感词:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(nicknameban)));
        sb.append(String.format("检测消息敏感词:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(bannedword)));
        sb.append(String.format("检测群名片格式:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(illegalnickname)));
        sb.append(String.format("检测红包标题:%b\n",ParseUtils.parseBoolean2ChineseBooleanStr( redpackettitlebanedword)));
        sb.append(String.format("启用加群回复语:%b\n", ParseUtils.parseBoolean2ChineseBooleanStr(joingroupreply)));
        sb.append(String.format("本群回复尾巴:%s\n", postfix));
        sb.append(String.format("群员违规次数上限:%d\n", mistakecount));
        sb.append(String.format("禁言命令无参数禁言分钟:%d\n", notparamgagminute));
        return sb.toString();
    }
/*
    @Override
    public String toString() {
        return super.toString() + "GroupWhiteNameBean{" +
                "enable1=" + enable1 +
                ", enable2=" + enable2 +
                ", enable3=" + enable3 +
                ", enable4=" + enable4 +
                ", enable5=" + enable5 +
                ", enable6=" + enable6 +
                ", enable7=" + enable7 +
                ", enable8=" + enable8 +
                ", enable9=" + enable9 +
                ", enable10=" + enable10 +
                ", enable11=" + enable11 +
                ", enable12=" + enable12 +
                ", enable13=" + enable13 +
                ", enable14=" + enable14 +
                ", enable15=" + enable15 +
                ", enable16=" + enable16 +
                ", enable17=" + enable17 +
                ", enable18=" + enable18 +
                ", enable19=" + enable19 +
                ", enable20=" + enable20 +
                ", enable21=" + enable21 +
                ", enable22=" + enable22 +
                ", enable23=" + enable23 +
                ", enable24=" + enable24 +
                ", enable25=" + enable25 +
                ", upgradeinfo='" + upgradeinfo + '\'' +
                ", admin=" + admin +
                ", localword=" + localword +
                ", netword=" + netword +
                ", bannedword=" + bannedword +
                ", nicknameban=" + nicknameban +
                ", illegalnickname=" + illegalnickname +
                ", frequentmsg=" + frequentmsg +
                ", banpasswordredpacket=" + banpasswordredpacket +
                ", banvoice=" + banvoice +
                ", bancall=" + bancall +
                ", banpic=" + banpic +
                ", joingroupreply=" + joingroupreply +
                ", joingroupword='" + joingroupword + '\'' +
                ", remark='" + remark + '\'' +
                ", groupnickanmekeyword='" + groupnickanmekeyword + '\'' +
                ", groupnickanmekeyword1='" + groupnickanmekeyword1 + '\'' +
                ", groupnickanmegagtime=" + groupnickanmegagtime +
                ", redpackettitlebanedword=" + redpackettitlebanedword +
                ", frequentmsgduratiion=" + frequentmsgduratiion +
                ", frequentmsgcount=" + frequentmsgcount +
                ", frequentmsggagtime=" + frequentmsggagtime +
                '}';
    }
*/


    @Override
    public GroupWhiteNameBean clone() {
        GroupWhiteNameBean obj = null;
        try {
            obj = (GroupWhiteNameBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }


    @Override
    public boolean universalQueryBoolean(int type, Object... args) {
        return false;
    }

    @Override
    public Object universalQueryByFieldName(String name) {
        Field field = null;
        try {
            field.setAccessible(true);
            field = this.getClass().getField(name);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public boolean universalsetFieldValue(String name, Object value) {
        Field field = null;
        try {
            field.setAccessible(true);
            field = this.getClass().getField(name);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return false;
    }


    @Override
    public String universalQueryString(int type, Object... args) {
        return null;
    }

    @Override
    public boolean universalSaveInfo() {

        DBUtils dbUtils = RobotContentProvider.getDbUtils();
        int update = DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).update(this);
        return update > 0;
    }

    public boolean isAllowGenerateCardMsg() {
        return allowGenerateCardMsg;
    }

    public void setIsCurrentGroupAdmin(boolean isCurrentGroupAdmin) {
        this.isCurrentGroupAdmin = isCurrentGroupAdmin;
    }
}
