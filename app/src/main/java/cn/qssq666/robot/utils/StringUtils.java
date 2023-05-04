package cn.qssq666.robot.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.qssq666.robot.business.ParamParseUtil;

/**
 * Created by qssq on 2017/12/4 qssq666@foxmail.com
 */

public class StringUtils {


    private static final String TAG = "StringUtils";

    public static int getStrSignCount(String src, String find) {
        int o = 0;
        int index = -1;
        while ((index = src.indexOf(find, index)) > -1) {
            ++index;
            ++o;
        }
        return o;
    }
    public static String  removeZeroStart(String src) {
        while (src.startsWith("0") && src.length() > 1) {
            src = src.substring(1);
        }
        return src;
    }

    /**
     * 可以去掉全角的首尾空格
     *
     * @param textContent
     * @return
     */
    public static String trim(String textContent) {
        while (textContent.startsWith("　")) {//这里判断是不是全角空格
            textContent = textContent.substring(1, textContent.length());
        }

        while (textContent.endsWith("　")) {
            textContent = textContent.substring(0, textContent.length() - 1);

        }

        return textContent.trim();
    }

    //一个一个替换可以删除所有
    public static String deleteAllSpace(String textContent) {
        textContent = textContent.replace("　", "");//这里判断是不是全角空格
        textContent = textContent.replace(" ", "");//这里判断是不是全角空格


        return textContent;
    }


    /**
     * @param s
     * @param startPosition 字符串开始位置,
     * @param length        截取的长度
     * @return
     */
    public static String bSubstringByChar(String s, int startPosition, int length) {
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        System.out.println("chars:" + chars.length);
        sb.append(chars, startPosition, length);



     /*   String ios88591 = null;
        try {
            ios88591 = new String(s.getBytes(),"iso-8859-1");
            System.out.println("iso "+ios88591);
        String fixSubStr = ios88591.substring(startPosition, length);
        return new String(fixSubStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return sb.toString();
    }


    public static String getStrRight(String src, String findKey) {
        int i = src.indexOf(findKey);
        if (i != -1 && i < src.length() - 1) {
            return src.substring(i + findKey.length(), src.length());
        }
        return null;
    }


    public static String getStrLeft(String src, String findKey) {
        int i = src.indexOf(findKey);
        if (i != -1 && i > 0) {
            return src.substring(0, i);//表示自己的真是结束为止 不包含
        }
        return null;
    }

    public static String getStrCenter(String src, String startStr, String endStr) {
        return getStrCenter(src,startStr,endStr,false);
    }
    public static String getStrCenter(String src, String startStr, String endStr,boolean fromLast) {
        int startPosition = src.indexOf(startStr);
        if (startPosition == -1||startPosition==src.length()-1) {
            return null;
        }
        int endPosition =fromLast?src.lastIndexOf(endStr): src.indexOf(endStr, startPosition);
        if (endPosition == -1) {
            return null;
        }
        if (startPosition >= endPosition) {
            return null;
        }
        return src.substring(startPosition + startStr.length(), endPosition);
    }

    /**
     * @param src
     * @param startStr
     * @param endIndex 结尾位置
     * @return
     */
    public static String getStrCenterByIndex(String src, String startStr, int endIndex) {
        int startPosition = src.indexOf(startStr);
        if (startPosition == -1) {
            return null;
        }
        int endPosition = endIndex;
        if (endPosition == -1 || endPosition > startStr.length()) {
            return null;
        }
        return src.substring(startPosition + startStr.length(), endPosition);
    }

    /**
     * @param src        原文本
     * @param startStr   开始文本
     * @param endStr     结束文本
     * @param replaceStr 开始和结束之间用作替换的文本
     * @return
     */
    public static String replaceCenterStr(String src, String startStr, String endStr, String replaceStr) {
        int startPosition = src.indexOf(startStr);
        if (startPosition == -1) {
            return null;
        }
        int endPosition = src.indexOf(endStr);
        if (endPosition == -1) {
            return null;
        }
        String centerStr = src.substring(startPosition + startStr.length(), endPosition);
        return src.replaceAll(centerStr, replaceStr);
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static long parseLong(String str) {
        /*
        for(int i=0;i<str.length();i++){
if(str.charAt(i)>=48 && str.charAt(i)<=57){
str2+=str.charAt(i);
}
         */
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String result = m.replaceAll("").trim();
        if (isEmpty(str)) {
            return 0;
        } else {
            return Long.parseLong(result);
        }
//        System.out.println( m.replaceAll("").trim());
    }


    /**
     * 只保留一个的话需要多删除几次
     *
     * @param str
     * @return
     */
    public static String deleteMulitiSpace(String str) {

        //boolean result1=Pattern.matches(".*?\\s{1,}.*?", str);
//        boolean result1=Pattern.matches(".*?\\s{1,}.*?", str);

        int maxReplaceCount = 1090;
        int current = 0;
        String reg = ".*?\\s{2,}.*?|";

  /*      String reg = ".*?\\s{2,}.*?|" +//ignore include
                "qssqdie153016267";*/
        while (str.matches(reg)) {  //ignore_include
            str = str.replaceAll("\\s{2,}", " ");
            if (current > 30) {
                System.out.println("replace  result:" + str + "," + current);

            }
            current++;
            if (current > maxReplaceCount) {
                System.out.println("Replace muliti err count err still match " + str.matches(reg) + ",do while str is:" + str);
                break;
//                break;
            }
        }
        return str;

    }


    public static String replaceAllByStr(String str, String findStr, String replaceWord) {
        StringBuilder stringBuilder = new StringBuilder(str);
        replaceAll(stringBuilder, findStr, replaceWord);
        return stringBuilder.toString();
    }

    public static boolean replaceAllParseVar(StringBuilder stringBuilder, String findStr, String replaceWord) {

        int findStrIndex = -1;
        boolean succ = false;
        while ((findStrIndex = stringBuilder.indexOf(findStr)) != -1) {  //ignore_include

            if (findStrIndex < stringBuilder.length() - 3) {//至少要小于3位 才算成立
//                char[] chars = new char[1];
//                stringBuilder.getChars(index + findStr.length(), index + findStr.length() + 1, chars, 0);
//                Log.w(TAG, "chars" + String.valueOf(chars));


                if (replaceWord.contains("%s") || replaceWord.contains("select(") || replaceWord.contains("{arg")) {


                    int leftKuoIndex = stringBuilder.indexOf("(", findStrIndex + findStr.length());
                    int rightKuoHao = stringBuilder.indexOf(")", findStrIndex + findStr.length() + 1);
                    boolean isMatchLeft = leftKuoIndex <= findStrIndex + findStr.length();


                    if (isMatchLeft && rightKuoHao != -1) {


                        String vars = stringBuilder.substring(findStrIndex + findStr.length() + 1, rightKuoHao);
                        if (vars == null || vars.contains("$")) {
                            stringBuilder.replace(findStrIndex, findStrIndex + findStr.length(), replaceWord);
//                            parseSelectOrArgRule(stringBuilder, replaceWord, 0, new String []{});
                            continue;
                        } else {


                            int paramCount = StringUtils.getStrSignCount(replaceWord, "%s");
                            String[] split = vars.split(",");
                            Object[] allowEmptyVar = null;


                            try {
                                stringBuilder.replace(findStrIndex, findStrIndex + rightKuoHao + 1, String.format(replaceWord, (Object[]) split));//这里的end有点可疑

                            } catch (Exception e) {
                                stringBuilder.replace(findStrIndex, findStrIndex + rightKuoHao + 1, replaceWord + "(期望的参数总数为" + paramCount + " 参数错误)");

                            }
//                                if (split.length > strSignCount) {//一个字符串只允许一个变量，


                            parseSelectOrArgRule(stringBuilder, replaceWord, paramCount, split);
//                                }


                        }


                    } else {
                        stringBuilder.replace(findStrIndex, findStrIndex + findStr.length(), replaceWord);
                        parseSelectOrArgRule(stringBuilder, replaceWord, 0, new String[]{});

                    }

                } else {

                    stringBuilder.replace(findStrIndex, findStrIndex + findStr.length(), replaceWord);
                }


            } else {
                stringBuilder.replace(findStrIndex, findStrIndex + findStr.length(), replaceWord);

            }


            succ = true;

        }
        return succ;
    }

    /**
     * 不管有没有 定义都应该处理  比如$xxx()也是可以替换里面的sleect()的。
     *
     * @param stringBuilder
     * @param replaceWord
     * @param paramCount
     * @param split
     */
    private static void parseSelectOrArgRule(StringBuilder stringBuilder, String replaceWord, int paramCount, String[] split) {
        try {

            int enableEmptyStart = -1;
            int startPosition = 0;
            int startArg = 0;//这里表示取第一个变量进行替换
            while ((enableEmptyStart = stringBuilder.indexOf("select(", startPosition)) != -1) {

                startPosition = enableEmptyStart + 1;//避免死循环
                if (enableEmptyStart != -1) {
                    int enableEmptyEnd = stringBuilder.indexOf(")", enableEmptyStart + 1);
                    if (enableEmptyEnd != -1) {


                        CharSequence charSequence = stringBuilder.subSequence(enableEmptyStart + ("select(").length(), enableEmptyEnd);//整个替换，但是取值得时候要从(后面开始

                        LogUtil.writeLog(TAG, "可空变量识别 " + charSequence);
                        if (split.length > paramCount) {
                            paramCount = paramCount + StringUtils.getStrSignCount(replaceWord, "{arg");//这里永远是用到最后面的。
                            String[] strings = ParamParseUtil.subArr(split, paramCount, split.length - 1);//把seelct(xxx)改为传递的xxx

                            if (startArg >= strings.length) {
                                stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, charSequence + "");//出现了错误，直接忽略替换默认值
                            } else {
                                String currentArg = strings[startArg];
                                if (TextUtils.isEmpty(currentArg.trim()) || currentArg.equals("null") || currentArg.equals("default")) {
                                    stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, charSequence + "");    //数据为空，用之前的。
                                } else {
                                    stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, currentArg);//首先是0 然后是1 ，每次可空变量是一次从里面取，一次性替换完毕了。 但是如果是空就忽略。

                                }
                            }


                        } else {
                            stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, charSequence + "");//把select(xxx)改为xxx

                            //没有传递 ，默认值

                        }

                    }
                }

                startArg++;

            }
        } catch (Exception e) {


        }


        try {

            int enableEmptyStart = -1;
            int startPosition = 0;
            int startArg = 0;//这里表示取第一个变量进行替换
            while ((enableEmptyStart = stringBuilder.indexOf("{arg", startPosition)) != -1) {

                startPosition = enableEmptyStart + 1;//避免死循环
                if (enableEmptyStart != -1) {
                    int enableEmptyEnd = stringBuilder.indexOf("}", enableEmptyStart + 1);
                    if (enableEmptyEnd != -1) {


                        CharSequence charSequence = stringBuilder.subSequence(enableEmptyStart + ("{arg").length(), enableEmptyEnd);//整个替换，但是取值得时候要从(后面开始
                        String defaultValue = "";
                        int fromArgIndex = 0;
                        if (TextUtils.isEmpty(charSequence)) {
                            fromArgIndex = 0;


                        } else {
                            int iSplitDefaultValue = charSequence.toString().indexOf(",");

                            if (iSplitDefaultValue == -1 || iSplitDefaultValue == charSequence.toString().length() - 1) {//后面没参数分割个毛线?
                                fromArgIndex = ParseUtils.parseInt(charSequence.toString());//取出第一个 没有必要。。。

                            } else {

                                defaultValue = charSequence.toString().substring(iSplitDefaultValue + 1, charSequence.toString().length());//取出第一个 没有必要。。。
                                fromArgIndex = ParseUtils.parseInt(charSequence.toString().substring(0, iSplitDefaultValue));//取出第一个 没有必要。。。


                            }

                        }


                        if (fromArgIndex >= split.length || fromArgIndex < 0 || split.length == 0 || ("".equals(split[0]) && split.length == 1)) {
                            stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, defaultValue);//超出的直接设置为空或者默认值
                        } else {
                            String str = split[fromArgIndex];
                            if (str == null || str.equals("null") || str.equals("default")) {
                                stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, defaultValue);
                            } else {

                                stringBuilder.replace(enableEmptyStart, enableEmptyEnd + 1, str);
                            }

                        }


                    }
                }

                startArg++;

            }
        } catch (Exception e) {


        }
    }

    public static boolean replaceAll(StringBuilder stringBuilder, String findStr, String
            replaceWord) {


        int index = -1;
        boolean succ = false;
        while ((index = stringBuilder.indexOf(findStr)) != -1) {  //ignore_include
            stringBuilder.replace(index, index + findStr.length(), replaceWord);


            succ = true;

        }
        return succ;

    }

    public static boolean isJSON(String json) {
        if (json == null) {
            return false;
        }
        int startIndexSign = json.indexOf("{");
        if (startIndexSign == -1) {

            return false;
        }

        int endIndexSign = json.lastIndexOf("}");
        if (endIndexSign == -1) {

            return false;

        }

        if (startIndexSign >= endIndexSign) {
            return false;
        }

        return true;
    }

    public static boolean isEqualStr(String beforeArg, String cardMsg) {
        if (beforeArg == null && cardMsg == null) {
            return true;
        }
        if (beforeArg == null) {
            return false;
        }
        if (beforeArg.trim().equals(cardMsg.trim())) {

            return true;
        }
        return false;
    }

    public static String selectStr(String str, String str1) {
        if (TextUtils.isEmpty(str)) {
            return str1;
        } else {
            return str;
        }
    }

    public static String getStrByLen(String deleteHtmlLabel, int len) {
        int length = deleteHtmlLabel.length();
        if (len >= length) {
            return deleteHtmlLabel;
        }
        return deleteHtmlLabel.substring(0, len);

    }

    public static String[] splitKeyValue(String content, String split) {
        int i = content.indexOf(split);
        if (i > 0&&i<content.length()-1) {
            String key = content.substring(0, i);
            String value = content.substring(i+1, content.length());
            return new String[]{key,value};
        }
        return new String[]{};

    }
}
