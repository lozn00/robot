package cn.qssq666.robot.xbean;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.utils.DateUtils;

public class MusicCardInfo {
    private int duration;



    /*
    <?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID="1" templateID="1" action="web" brief="[分享] 免费领取超级会员" sourceMsgId="0" url="mqqapi://card/show_pslcard?uin=1378424839&amp;account_flag=8460565&amp;s=1487557867&amp;card_type=public_account&amp;_wv=3" flag="0" adverSign="0" multiMsgFlag="0"><item layout="2"><picture cover="http://url.cn/417bdos" w="0" h="0" /><title>免费领取超级会员</title><summary>关注后领取</summary></item><source name="" icon="" action="app" i_actionData="tencent65520://" appid="65520" /></msg>
     */

/*    public String toString() {
        return "<?xml version='1.Fix0SendGag' encoding='UTF-8' standalone='yes' ?><msg serviceID='2' templateID='1' action='web' brief='[点歌] "
         + fileNmae(this.oFix0CardInfo)
                + "' sourcePublicUin="" sourceMsgId="0" url="https://www.coolapk.com/apk/com.specher.qqrobot" flag="0"
                 sType="" adverSign="0" adverKey=""><item layout="2"><audio cover="" + this.oOreceve_action + "" src="" + this.url + "" />
                 <title>" + fileNmae(this.fileNmae) + "</title><summary>时长:" + fileNmae(this.Fix0ODuration) + "</summary></item><source name="SpQQ机器人 - Sp群管插件"
                  icon="http://image.coolapk.com/apk_logo/2016/1102/qq-for-109247-o_1b0ggs0js1vnr12ep7eioh9quhq-uid-582947.png"
                  url="" action="app" a_actionData="com.specher.qqrobot" /></msg>";
    }*/


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getTitlebrief() {
        return titlebrief;
    }

    public void setTitlebrief(String titlebrief) {
        this.titlebrief = titlebrief;
    }

    public String getMusictitle() {
        return musictitle;
    }

    public void setMusictitle(String musictitle) {
        this.musictitle = musictitle;
    }

    public String getSharesource() {
        return sharesource;
    }

    public void setSharesource(String sharesource) {
        this.sharesource = sharesource;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getAudioCover() {
        return audioCover;
    }

    public void setAudioCover(String audioCover) {
        this.audioCover = audioCover;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String titlebrief;

    public String getExtraStr() {
        return extraStr;
    }

    public void setExtraStr(String extraStr) {
        this.extraStr = extraStr;
    }

    String extraStr;
    private String musictitle;
    private String sharesource;
    private String actionData;
    private String audioFile;
    private String audioCover;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String author;
    private String icon;
    /*
    可能是没有传递bundle导致传递了默认的bundle,
    崩溃原因 StructMsgFactory类 com.tencent.mobileqq.structmsg.StructMsgForAudioShare cannot be cast to com.tencent.mobileqq.structmsg.StructMsgForGeneralShare

    public static AbsStructMsg a(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        switch (bundle.getInt("req_type", 1)) {
            case 2:
                return new StructMsgForAudioShare(bundle);
            case 3:
                return new StructMsgForHypertext(bundle);
            case 5:
                return new StructMsgForImageShare(bundle);
            default:
                return new StructMsgForGeneralShare(bundle);
        }
    }

     */


    /*
    <?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID="1" templateID="1" action="web" brief="[分享] 免费领取超级会员" sourceMsgId="0" url="mqqapi://card/show_pslcard?uin=1378424839&amp;account_flag=8460565&amp;s=1487557867&amp;card_type=public_account&amp;_wv=3" flag="0" adverSign="0" multiMsgFlag="0"><item layout="2"><picture cover="http://url.cn/417bdos" w="0" h="0" /><title>免费领取超级会员</title><summary>关注后领取</summary></item><source name="" icon="" action="app" i_actionData="tencent65520://" appid="65520" /></msg>
     */

/*    public String toString() {
        return "<?xml version='1.Fix0SendGag' encoding='UTF-8' standalone='yes' ?><msg serviceID='2' templateID='1' action='web' brief='[点歌] "
         + fileNmae(this.oFix0CardInfo)
                + "' sourcePublicUin="" sourceMsgId="0" url="https://www.coolapk.com/apk/com.specher.qqrobot" flag="0"
                 sType="" adverSign="0" adverKey=""><item layout="2"><audio cover="" + this.oOreceve_action + "" src="" + this.url + "" />
                 <title>" + fileNmae(this.fileNmae) + "</title><summary>时长:" + fileNmae(this.Fix0ODuration) + "</summary></item><source name="SpQQ机器人 - Sp群管插件"
                  icon="http://image.coolapk.com/apk_logo/2016/1102/qq-for-109247-o_1b0ggs0js1vnr12ep7eioh9quhq-uid-582947.png"
                  url="" action="app" a_actionData="com.specher.qqrobot" /></msg>";
    }*/
    // exclude_start



    public String toString() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>" +//Msg开始
                "<msg serviceID='2'" +
//                "<msg serviceID='1'" +
                " templateID='1'" +
                " action='web' " +
                "brief='" + titlebrief + "'" +
                " sourcePublicUin=\"\" sourceMsgId='0'" +
                " url='" + url + "'" +
                " flag='0' adverSign='0' adverKey=\"\">" +
                "<item layout='2'>" +
                "<audio cover='" + audioCover + "' src=\"" + audioFile + "\" /><title>" + musictitle + "</title>" +
                "<summary>" + getsummary() + "</summary></item>" +
//                "<item layout='2'><picture cover='http://url.cn/417bdos' w='0' h='0' /><title>这是一首歌啊</title><summary>附加描述</summary></item>" +
                "<source name=\"" + sharesource + "\" icon=\"" + icon + "\" action=\"app\" a_actionData=\"" + actionData + "\" /></msg>";//Msg结束
    }

    // exclude_end



    /*




                                                                       ====
     */
/*
    public String toString() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID='1' templateID='1' action='web' brief='[分享] 免费领取超级会员' sourceMsgId='0'" +
                " url='mqqapi://card/show_pslcard?uin=153016267&amp;account_flag=8460565&amp;s=1487557867&amp;card_type=public_account&amp;_wv=3' flag='0'
                adverSign='0' multiMsgFlag='0'>" +
                "<item layout='2'><picture cover='http://url.cn/417bdos' w='0' h='0' /><title>免费领取超级会员</title><summary>关注后领取</summary></item>" +
                "<source name=\"情迁聊天机器人\" icon=\"\" action=\"app\" a_actionData=\"cn.qssq666.robot\" /></msg>";
    }
*/


    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getsummary(){
        if(duration>0){

        return " 时长:" + DateUtils.getGagTime(duration);
        }else{
            return extraStr;
        }
    }
}
