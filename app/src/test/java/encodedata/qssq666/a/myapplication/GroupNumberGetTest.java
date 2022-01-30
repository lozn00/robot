package encodedata.qssq666.a.myapplication;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.NetQuery;

/**
 * Created by luozheng on 2017/3/2.  qssq.space
 */

public class GroupNumberGetTest {

    /*
    Respose
Access-Control-Allow-Methods:POST GET
Access-Control-Allow-Origin:http://qun.qq.com
Cache-Control:max-age=0, must-revalidate
Connection:keep-alive
Content-Encoding:gzip
Content-Length:1322
Content-Type:text/html; charset=utf-8
Date:Fri, 19 May 2017 15:09:08 GMT
Server:tws
     */
/*

    Accept:application/json, text/javascript, **
/*; q=0.01
Accept-Encoding:gzip, deflate
Accept-Language:zh-CN,zh;q=0.8,en;q=0.6
Connection:keep-alive
Content-Length:47
Content-Type:application/x-www-form-urlencoded; charset=UTF-8
Cookie:pac_uid=1_153016267; pgv_pvi=2902239232; RK=4Q9ydWIade; tvfe_boss_uuid=c4c9e5f1909ec930; mobileUV=1_15b381a5468_8fc43; eas_sid=K1h4b9R1e4g7S8b9S2P1O4N3Z0; pgv_flv=25.0 r0; ptui_loginuin=35068264; sd_userid=23331494853844556; sd_cookie_crttime=1494853844556; luin=o0153016267; lskey=000100007951b29d96a31c1e359e39baccd65ae0b78847bb222d765b137229262519cf23a8b74f0e4aefdeb4; pgv_si=s6862272512; pgv_info=ssid=s5363234986; pgv_pvid=7913595717; o_cookie=153016267; _qpsvr_localtk=0.21503502123950402; ptisp=ctc; ptcz=fa3f681131911c5b352d14b5bad58232f4caa5e62efd02294263e39d935d1298; pt2gguin=o0153016267; uin=o0153016267; skey=@34AUNBtsw; p_uin=o0153016267; p_skey=feRxaXvhe7hZI8DZUOkYsbN57E4z5R9Y1S9w8KpSBg8_; pt4_token=KTJTTXhS4ZJrs6c5xXvqL-Idpe6it7F7Wc8-HeDdzEk_
Host:qun.qq.com
Origin:http://qun.qq.com
Referer:http://qun.qq.com/member.html
User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36
X-Requested-With:XMLHttpRequest
*/

    /**
     * 批量查询QQ号码
     */
    @Test
    public void GroupMemberPullTest() {

        NetQuery netQuery = new NetQuery();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Accept", "application/json,text/javascript, */*; q=0.01");
            map.put("Content-Type", "pplication/x-www-form-urlencoded; charset=UTF-8");
            map.put("Referer", "http://qun.qq.com/member.html");
            //下面是必须得。
            String insertUrl;
            insertUrl = "http://qun.qq.com/cgi-bin/qun_mgr/search_group_members";
            System.out.println("request url:" + insertUrl);

            String cookie = "pgv_pvi=1487813632; RK=oR9yYUIKYc; pac_uid=1_153016267; tvfe_boss_uuid=f33fac9115609638; uid=32378841; pgv_pvid=6550889042; o_cookie=153016267; _qpsvr_localtk=tk5304; pgv_info=ssid=s8719925476; pgv_si=s1763521536; qqmusic_uin=; qqmusic_key=; qqmusic_fromtag=; ptisp=ctc; ptcz=c0423465a8d5a5da13c8be06e23d77c18e4f2798801f6005e16e035de1e02bc3; pt2gguin=o0153016267; uin=o0153016267; skey=@dDVfGf37t; p_uin=o0153016267; p_skey=57u7yBJ2HoSlhFWjYaEozSEAcGXk6bop9FGd*VDg6JI_; pt4_token=CSCQZ6cVudRwIayPieJq50L0QERilGZcpFvVr-CRBq0_";
            String qqgroup = "286804469";// 129103467 618725848
            String bkn="1235684244";//一次登录之后不会变化
            String count = "20000";
            String remark="";
            String postData = "gc=" + qqgroup + "&st=0&end=" + count + "&sort=0&bkn="+bkn;
            String result = netQuery.sendPost(insertUrl, map, postData, cookie, true);
            System.out.println("result:" + result);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
            String mems1 = jsonObject.getString("mems");
            if(mems1==null || mems1.trim().equals("")){
                System.err.println("此群没有加入,"+result);
                return ;
            }
            List<QQMenberBean> mems = JSON.parseArray(mems1, QQMenberBean.class);
            StringBuffer sb = new StringBuffer();
            for (QQMenberBean mem : mems) {
                sb.append("" + mem.getUin() + "," + mem.getNick() + "," + DateUtils.getTimeEightFormatStr(new Date(mem.getJoin_time()*1000))+ "," + DateUtils.getTimeEightFormatStr(new Date(mem.getLast_speak_time()*1000)) + "\n");
            }
            File file = new File(remark+"group_member" + qqgroup +"_"+ DateUtils.getTimeEightFormatStr(new Date())+"_count_"+mems.size()+".txt");
            FileUtils.writeStringToFile(file, sb.toString(), "utf-8");
            System.out.println("写入成功:\n" + mems.size() + "个qq,写入路径:" + file.getAbsolutePath());
//            System.out.println("result:\n" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //{"calllog":"CMLB_39998_1#oidb_0x561_2:0","ec":7,"em":""} 没有加入的情况。

    }

    @Test
    public void drawKuaiBo(){

        NetQuery netQuery = new NetQuery();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Accept", "application/json,text/javascript, */*; q=0.01");
//            map.put("Content-Type", "text/html; charset=UTF-8");
            map.put("Accept-Encoding", "gzip, deflate, br");
            map.put("Accept-Language", "zh-CN,zh;q=0.8");
            map.put("Connection", "keep-alive");
            map.put("Origin", "https://www.lltoken.com");
            map.put("Referer", "https://www.lltoken.com/user/exchange/product");
            map.put(" User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            map.put("X-Requested-With", "XMLHttpRequest");


            //下面是必须得。
            String insertUrl = "https://www.baidu.com";
//            String insertUrl = "https://www.lltoken.com/user/exchange/qa";
            System.out.println("request url:" + insertUrl);

            String cookie = "_uab_collina=151417650005229983391983; aliyungf_tc=AQAAAMEN9w9XHwIA7dFbcbljWRlYfISa; acw_tc=AQAAAAGaHVA4VQMA7dFbcYXXR755C2MA; UM_distinctid=1608bf3d0a98a5-06e7f6a9dfa61c-6a191178-232800-1608bf3d0aa497; clicaptcha_text=%E6%B7%B7%2C%E6%8A%98%2C%E7%A0%94%2C%E5%AF%86; CNZZDATA30087897=cnzz_eid%3D1536902383-1514172398-%26ntime%3D1514172398; u_asec=099%23KAFEnYEKEioEhGTLEEEEEpEQz0yFD6t1Sc9MD6PhSr%2FEW60cDrBFa6gJDt7TEEiStEE7lYFETKxqAjHhE7Eht3BlluZdAYFEv71kbbZdCwUQrjDt9235rAoZ6bu6%2FtUnHshnwBZgSOIVNsR7%2BOaf6scCNdVcrtuWbiUSadu4DAZBNsr3Llek6LenP7P6uz8nPzXWLYr8%2Bi2nbsj6Y2ywqVQScblczQI08z8YuasgSCcxh0Uu7W35rAoZ6bu6%2FtUnHshnw9%2F8SA4LrO4Ri%2FJw8g4LbP1p%2F6Snbspx3%2Fl8UwYVPaI3%2BewrE7Eht%2FMFE6%2BIBEFE13iSEJD8xhK0sEFEp3iSlldx7GCqt37MlXZddpyStTLtsyaG73iSh3nP%2F3TEt37MlXZddFwUE7Tx1IsbEH63c%2FNVWwInwqSvUZxd0xEqa8%2BSXREYq5CcXyTlUJ6%2F1KGZbHwvBwlqwwlLqXGVRKnyMOFOViP%2Bk6ArtiOvcv60JBWv09ma3mvkcQYSQmQTEE7EERpC; user_token=36ccba051c61d5d018aef6c8d926aefc7bb82f37";

            String postData = "";
//            String result = netQuery.sendGet(insertUrl);
            String result = netQuery.sendPost(insertUrl, map, postData, cookie, false);
            System.out.println("KUAIBO:result:" + new String(result.getBytes("gbk"),"utf-8"));
//            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);

            //            System.out.println("result:\n" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
