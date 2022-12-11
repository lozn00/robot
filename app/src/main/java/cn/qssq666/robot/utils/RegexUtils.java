/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.robot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


//ignore_start

/**
 * @author cevencheng <cevencheng@gmail.com> www.zuidaima.com
 * @project baidamei
 * @create 2012-11-15 下午4:54:42
 */
public class RegexUtils {

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>  170 虚拟号码网段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }
    public static boolean isEnglishWord(String str){
        String regex="^(?!.*?[\\u4e00-\\u9fa5].*?$)(?![\\u4e00-\\u9fa5]+$)(?![0-9]+$)(?![0-9]+\\s+$)[a-zA-Z0-9\\W_]{3,}$";
        boolean result= Pattern.matches(regex, str);
        return result;
    }
    public static boolean checkPhoneFix(String phone) {
        String regex = "(^[1][34578]\\d{9}$)";
        return Pattern.matches(regex, phone);
    }

    /**
     * 必须 是正数 正 小数 。
     *
     * @param phone
     * @return
     */
    public static boolean checkRechargeMoney(String phone) {
        String regex = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$|^[1-9]\\d*$";
        return Pattern.matches(regex, phone);
    }

    /**
     * ^[1-9]\d*$  不能有小数点
     *
     * @param phone
     * @return
     */
    public static boolean checkRechargeMoneyNoPoint(String phone) {
        String regex = "^[1-9]\\d*$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "^-?[0-9]\\d*$";//\-?[1-9](\d+)?
//        String regex = "\\-?[0-9](\\d+)?";//\-?[1-9](\d+)?
        return Pattern.matches(regex, digit);
    }

    /**
     * @param digit 必须是不是小数的数字 也不能是负数 正整数
     * @return
     */
    public static boolean checkNoSignDigit(String digit) {
        String regex = "^[0-9]\\d*$";
//        String regex = "^[1-9]\\d*|0$";
//        String regex = "\\[1-9](\\d+)?";
        return Pattern.matches(regex, digit);
    }


    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证json
     *
     * @param json json
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkJson(String json) {
        if (json != null && (json.trim().startsWith("{") && json.trim().endsWith("}"))) {
            return true;
        }
        return false;
//        String regex = "^\\{(.*?)\\}$";//数据太多竟然匹配不到。
//        return Pattern.matches(regex,json);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    public static boolean checkIsEnglishReg(String str) {
        return str.matches("^[a-zA-Z]*");
    }


    public static boolean checkIsContainEnglish(String str) {
        return str.matches(".*?[a-zA-Z].*?");
    }

    public static boolean checkIsContainNumber(String str) {
        return str.matches(".*?[0-9].*?");
    }

    public static boolean checkIsContainChinese(String str) {
        return str.matches(".*?[\u4E00-\u9FA5].*?");
    }

    public static boolean checkIsChinese(String str) {
        return str.matches("^[\u4E00-\u9FA5]+$");
    }

    //判断表示是否全为英文
    public static boolean checkIsEnglish(String word) {
        boolean sign = true; // 初始化标志为为'true'
        for (int i = 0; i < word.length(); i++) {
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z')) {
                return false;
            }
        }
        return true;
    }

    //正则


    /**
     * 处理特殊字符
     *
     * @param str
     * @return
     */
    public static String deleteSpecialcharacter(String str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll(" ").trim();
    }

    private static final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    public static boolean isContainSpecialcharacter(String str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域名
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com
     * </pre>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|i|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }


    /**
     * 删除标点符号
     *
     * @param s
     * @return
     */
    public static String removePunctuation(String s) {
        String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }

    /**
     * 第一组判断 是 完全整整数 前有且有一个。后面的是可以有可以没有 0-9  如 1 10 233 33333333 支持无穷大
     * 第二组 是 1.2可以有可以没有.2   正小数 但是只能有1位小数 1.0 1.9 2200.9  1.11 1.01
     * 第三组是 小数 为0.1后面可以有可以没有 第一位小数 必须为1 也就是至少1毛钱 第三组 0.10=0.19
     * 第三组 是用于检测 是否是0.01-0.09  0.01 -0.09
     * 支持输入 0.1 0.01
     * <p/>
     * ^[1-9]\d*$|    ^[1-9]\d*\.\d?$|^0\.[1-9]?\d?$|0\.[0-9]\d$
     *
     * @return
     */
    public static boolean checkIsAlipayMoney(String money) {
        String regex = "^[1-9]\\d*$|^[1-9]\\d*\\.\\d{0,2}$|^0\\.[1-9]?\\d?$|0\\.[0-9]\\d$";
//        String regex = "^[1-9]\\d*$|^[1-9]\\d*\\.\\d?\\d?$|^0\\.[1-9]?\\d?$|0\\.[0-9]\\d$";
        return Pattern.matches(regex, money);
    }

    public static boolean isContaineQQOrPhone(String text) {

        if (text != null) {
            String regex = ".*?[0-9]{5,12}.*?";
            return regex.matches(regex);
        }
        return false;
    }

    public static boolean iseQQ(String text) {

        if (text != null) {
//            String regex = "[0-9]{5,12}";
//            String regex = "^[0-9]{5,12}$";
//            return regex.matches(regex);
            //java 用string.mathes必须 用.*? 收尾，否则无法匹配成功 而Pattern类则加不加 都能正常。

            String regex = "[0-9]{5,12}";
            return Pattern.matches(regex, text);
        }
        return false;
    }

    public static String deleteHtmlLabel(String strCenter) {
        String regex = "<[^>]*>"; // 正则表达式，用于匹配所有 HTML 标签
        String result = strCenter.replaceAll(regex, "");
        return result;
    }

    ////ignore_end
}

