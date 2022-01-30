package encodedata.qssq666.a.myapplication;

import org.junit.Test;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.qssq666.robot.utils.StringUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {
    @Test
    public  void test(){
        LinkedList<String> pairs = new  LinkedList<String>();
        pairs.add("1");
        pairs.add("2");
        pairs.add("3");
        pairs.add("4");
        pairs.add("5");
        ListIterator<String> stringListIterator = pairs.listIterator(pairs.size() );
        while (stringListIterator.hasPrevious()){
            String previous = stringListIterator.previous();
            System.out.println("prepre:"+previous);
        }

    }

    public static String printTestTime(long j) {
//        Context applicationContext = this.a.getApplication().getApplicationContext();
        String secondStr = "分钟";//applicationContext.getString(2131430448);
        String hourStr = "小时";//""+ applicationContext.getString(2131430449);
        String dayStr = "天";//applicationContext.getString(2131430450);
        if (j < 60) {
            return 1 + "秒";
        }
        long second = 59 + j;
        long day = second / 86400;//86400 等于1天 3600 等于60分钟 1小时
        long hour = (second - (86400 * day)) / 3600;
        second = ((second - (86400 * day)) - (3600 * hour)) / 60;
        String str = "";
        if (day > 0) {
            str = str + day + dayStr;
        }
        if (hour > 0) {
            str = str + hour + hourStr;
        }
        if (second > 0) {
            return str + second + secondStr;
        }
        return str;
    }

    @Test
    public void subFix() {
        String source = "123456一二三四五流33方法";
        String str = StringUtils.bSubstringByChar(source, 4, 5);
        String str1 = source.substring(2, 2 + 1);
        System.out.println("str:" + str + ",str1:" + str1);
    }

    @Test
    public void testMatch() {
        String mess = "^[\\u4e00-\\u9fa5]{1,5}\\-[\\u4e00-\\u9fa5]{1,5}\\-R|NR$";
        mess = "^[\\u4e00-\\u9fa5]{1,5}\\-[\\u4e00-\\u9fa5]{1,5}\\-R|NR$";
        mess = "^[\\u4e00-\\u9fa5]{1,5}\\-[\\u4e00-\\u9fa5]{1,5}\\-R|NR$";
        boolean matches = "情迁-深圳-R".matches(mess);
        System.out.println("是否匹配:" + matches);
    }

     @Test
    public void testMatchNew() {
        String mess = "";
        mess = "^([\\u4e00-\\u9fa5_a-zA-Z0-9]{1,5})[\\_\\-\\--]([\\u4e00-\\u9fa5]{1,5})" +
                "[\\_\\-\\--]([\\u4e00-\\u9fa5_a-zA-Z0-9]{1,12})[\\_\\-\\--](?=.*?[N|R|NR])";
        boolean matches = "情迁-深圳-R".matches(mess);
        System.out.println("是否匹配:" + matches);
    }

    @Test

    public void regxJoin() {
        //left 5 right 2
        //末生人 加入了本群，点击修改TA的群名片                    ##**##2,5,0,3,0,2369830331,icon,0,0,color,0,19,10,20,2369830331,icon,0,0,color,0
        //情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,0,4,0,153016267,icon,0,0,color,0,19,11,21,153016267,icon,0,0,color,0
//        String msg="坐骑 加入了本群  3                  ##**##1,5,0,2,0,2932651186,icon,0,0,color,0";
        String msg = "   ##**##2,5,0,10,0,3482313589,icon,0,0,color,0,19,17,27,3482313589,icon,0,0,color,0";
//        String msg="╲ぷ   L/F09 加入了本群，点击修改TA的群名片                    ##**##2,5,0,10,0,3482313589,icon,0,0,color,0,19,17,27,3482313589,icon,0,0,color,0";
//        String msg="离了婚的女人 加入了本群 3                   ##**##1,5,0,6,0,2168531679,icon,0,0,color,0(群474240677)";
//        String patternString = ".*?\\,[0-9]\\,[0-9],[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";
//        String patternString = ".*?\\,[0-9]\\,[0-9]\\,[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";
        //你邀请 情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,4,8,0,153016267,icon,0,0,color,0,19,15,25,153016267,icon,0,0,color,0
//        String patternString = ".*?\\,[0-9]\\,[0-9]\\,[0-9]\\,([0-9]{5,11})\\,icon\\,[0-9]\\,[0-9]\\,color\\,[0-9].*?";// ignore_include
        String patternString = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include
//        String patternString = ".*?\\,[0-9]+\\,[0-9]\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";//不匹配

//        String patternString = ;
//        String patternString = ".*?\\,[0-9]+\\,[0-9]+\\,[0-9]+\\,([0-9]{5,11})\\,icon.*?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(msg);
        String sendUin = "";
        if (matcher.find()) {
            sendUin = matcher.group(1);// 不
        } else {
            sendUin = "fail";
        }
        System.out.println("match:" + sendUin);

    }

    @Test

    public void testischinese() {
//        public static boolean checkIsChinese(String str) {
        System.out.println("是否是中文" + "辅导费辅导费".matches("^[\u4E00-\u9FA5]+$"));
//        }

    }

    @Test
    public void testP() throws Exception {//如果遇到了3个 这替换2个为1个 还是有一个。
        String str = ":        f     f  ff方     法  ";


//     boolean result1=Pattern.matches(".*?\\s{1,}.*?", str);
//        System.out.println("result1:"+result1);

  /*      int maxReplaceCount = 1090;
        int current = 0;
        while (str.matches(".*?\\s{2,}.*?")) {  //ignore_include
            str = str.replaceAll("\\s{2,}", " ");
            System.out.println("result result:"+str);
            current++;
            if (current > maxReplaceCount) {
                System.out.println(  "Replace muliti err count err still match "+str.matches(".*?\\s{2,}.*?")+",result:"+str);
                break;
//                break;
            }
        }*/
     /*   while (str.contains("  ")) {
            str = str.replaceAll("  ", " ");
            str = str.replaceAll("\\s", " ");
            str = str.replaceAll("　", " ");

        }*/
        System.out.println("str:" + StringUtils.deleteMulitiSpace(str));
    }

    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        String str = "中国人很棒哦";
        String split = StringUtils.getStrRight(str, "中国");
        System.out.println("split取中国右边:" + split);
        split = StringUtils.getStrLeft(str, "国");
        System.out.println("spli去国左边:" + split);
        split = StringUtils.getStrLeft(str, "中");
        System.out.println("中的左边 split:" + split);
        split = StringUtils.getStrCenter(str, "中", "人");
        System.out.println("中 人center split:" + split);
        String temp = "abcdefg";
        split = StringUtils.getStrCenter(temp, "e", "g");
        System.out.println("a center split:" + split);
        System.out.println("中人之间的替换:" + StringUtils.replaceCenterStr(str, "中", "人", "部"));
        System.out.println("getInt:" + StringUtils.parseLong("30分钟"));


    }

    @Test
    public void list() {
        LinkedList<String> list = new LinkedList<>();
        list.add("35068264");
        list.add("11");
        list.add("3333");
        list.addFirst("first");
        list.addFirst("first1");
        list.add("xxx");
        list.addLast("endxxxbyAddLast");
        list.add("xxxbbbbend");
        list.push("新的");//将插入到第一个
        list.offer("旧的");//插入到最后面 add
        list.offer("旧的");//插入到最后面



        /*
         添加元素、删除元素：当做栈使用：push添加      pop删除
                                   当做队列使用：offer添加    poll删除
         */

                /*

                   // 在指定的位置增加数据
        list.add(3, "b");
        System.out.println(list);// [a, A, B, b, C, D, E]
        // poll使用方法,获取并删除列表的第一个元素
        String str1 = list.poll();
        System.out.println(str1 + "##" + list);// a##[A, B, b, C, D, E]

        // peek使用方法,获取并不删除列表的第一个元素
        String str2 = list.peek();
        System.out.println(str2 + "##" + list);// A##[A, B, b, C, D, E]
        // pop取堆栈中取出元素，并出栈
        String str3 = list.pop();
        System.out.println(str3 + "##" + list);// A##[B, b, C, D, E]
        //Linkedlist与数组的转换
        String arrstr[]=new String[list.size()];
        list.toArray(arrstr);
        for(String str:arrstr){
            System.out.print(str+"\t");//B    b    C    D    E

        }
        //************************
        //第二种构造linkedList的方法
        LinkedList<String> list2 = new LinkedList<String>(list);
        System.out.println("\n第二种构造方法："+list2);//第二种构造方法：[B, b, C, D, E]
                 */

//        System.out.println("list:" + list + "," + list.size());

        System.out.println("format" + printTestTime(1));//1秒
        System.out.println("format" + printTestTime(60));//1分钟
        System.out.println("format" + printTestTime(60 * 60));
        System.out.println("format" + printTestTime(60 * 60 * 24));
        System.out.println("checkEnglish" + checkIsContainEnglish("ab"));
        System.out.println("checkEnglish" + checkIsContainEnglish("a发b"));
        System.out.println("checkEnglish" + checkIsContainEnglish("方法"));
//        list:[end, first1, first, 35068264, 11, 3333, xxx, xxxbbbbend]

    }


    public boolean checkIsContainEnglish(String str) {
        return str.matches(".*?[a-zA-Z].*?");
    }
    /*

    队列方法       等效方法
add(e)        addLast(e)
offer(e)      offerLast(e)
remove()      removeFirst()
poll()        pollFirst()
element()     getFirst()
peek()        peekFirst()

    https://www.cnblogs.com/skywang12345/p/3308807.html
     */
}