package encodedata.qssq666.a.myapplication;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qssq on 2018/8/28 qssq666@foxmail.com
 */
public class JoinGroupRegTest {

    /*

     */

    //□ 加入了本群，点击修改TA的群名片                    ##**##2,5,0,1,0,2791864577,icon,0,0,color,0,19,8,18,2791864577,icon,0,0,color,0
    @Test

    public void regxJoin() {
        //left 5 right 2
        //末生人 加入了本群，点击修改TA的群名片                    ##**##2,5,0,3,0,2369830331,icon,0,0,color,0,19,10,20,2369830331,icon,0,0,color,0
        //情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,0,4,0,153016267,icon,0,0,color,0,19,11,21,153016267,icon,0,0,color,0
//        String msg="坐骑 加入了本群  3                  ##**##1,5,0,2,0,2932651186,icon,0,0,color,0";//邀请加入
        String msg = "   ##**##2,5,0,10,0,3482313589,icon,0,0,color,0,19,17,27,123456789,icon,0,0,color,0";//邀请加入


//                      msg="##**##2,5,0,15,0,153016267,icon,0,0,color,0,5,19,27,0,35068264,icon,0,0,color,0";
//        String msg="╲ぷ   L/F09 加入了本群，点击修改TA的群名片                    ##**##2,5,0,10,0,3482313589,icon,0,0,color,0,19,17,27,3482313589,icon,0,0,color,0";
//        String msg="离了婚的女人 加入了本群 3                   ##**##1,5,0,6,0,2168531679,icon,0,0,color,0(群474240677)";
//        String patternString = ".*?\\,[0-9]\\,[0-9],[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";
//        String patternString = ".*?\\,[0-9]\\,[0-9]\\,[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";
        //你邀请 情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,4,8,0,153016267,icon,0,0,color,0,19,15,25,153016267,icon,0,0,color,0
//        String patternString = ".*?\\,[0-9]\\,[0-9]\\,[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";// ignore_include
        String patternString = ".*?\\,?[0-9]{1,3}\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?([0-9]{5,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}\\,color\\,[0-9]{1,3}\\,[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,[0-9]{1,3}?\\,?([0-9]{4,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}.*?";// ignore_include


//        String patternString = ".*?\\,[0-9]+\\,[0-9]\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";//不匹配

//        String patternString = ;
//        String patternString = ".*?\\,[0-9]+\\,[0-9]+\\,[0-9]+\\,([0-9]{5,11})\\,icon.*?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(msg);
        String sendUin = "";
        if (matcher.find()) {
            sendUin = matcher.group(2);// 不
        } else {
            sendUin = "fail";
        }
        System.out.println("match:" + sendUin);

    }

    @Test
    public void testQQ() {
        String text = "33333f00";
        String regex = "[0-9]{5,12}";
//        String regex = "^[0-9]{5,12}$";//效果一样
        boolean matches = Pattern.matches(regex, text);
//            String regex = "^[0-9]{5,12}$";
        System.out.print("正则匹配" +matches);
    }

    @Test

    public void selfJoin() {
        //left 5 right 2
        //末生人 加入了本群，点击修改TA的群名片                    ##**##2,5,0,3,0,2369830331,icon,0,0,color,0,19,10,20,2369830331,icon,0,0,color,0
        //情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,0,4,0,153016267,icon,0,0,color,0,19,11,21,153016267,icon,0,0,color,0
//        String msg="坐骑 加入了本群  3                  ##**##1,5,0,2,0,2932651186,icon,0,0,color,0";//邀请加入
        String msg = "  N......N 加入了本群                    ##**##1,5,0,8,0,35068264,icon,0,0,color,0";//邀请加入

        msg = " ##**##2,5,0,1,0,2791864577,icon,0,0,color,0,19,8,18,2791864577,icon,0,0,color,0";

//        String patternString = ;
        String patternString = ".*?\\,[0-9]+\\,[0-9]+\\,[0-9]+\\,([0-9]{5,11})\\,icon.*?";

        patternString = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";//

//        patternString = ".*?\\,[0-9]{1,3}\\,([0-9]{4,12})\\,icon.*?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(msg);
        String sendUin = "";
        if (matcher.find()) {
            sendUin = matcher.group(matcher.groupCount());// 不
        } else {
            sendUin = "fail";
        }

        System.out.println("match:" + sendUin + ",count:" + matcher.groupCount());

    }


//    ら耀世灬.弑魂 加入了本群，点击修改TA的群名片                    ##**##2,5,0,7,0,892037539,icon,0,0,color,0,19,14,24,892037539,icon,0,0,color,0

    @Test

    public void regxJoinNew() {

     /*   String patternString=null;

        if(messageFilter.contains("邀请")){
            patternString     = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include
        }else{

            patternString     = ".*?,color\\,\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include

        }
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(messageFilter);
        String sendUin = "";
        if (matcher.find()) {
            sendUin = matcher.group(1);// 不
        } else {
            MsgReCallUtil.notifyJoinMsgNoJump(this, item.getMessage(), item);

*/


    }


}





