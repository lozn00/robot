package cn.qssq666.robot.utils;

/**
 * Created by qssq on 2018/8/31 qssq666@foxmail.com
 */
public class HtmlEncodeUtil {

    public static String htmlStrDecode(String str) {
        str = str.replace("&ldquo;", "“");
        str = str.replace("&rdquo;", "”");
        str = str.replace("&nbsp;", " ");
        str = str.replace("&", "&amp;");
        str = str.replace("&#39;", "'");
        str = str.replace("&rsquo;", "’");
        str = str.replace("&mdash;", "—");
        str = str.replace("&ndash;", "–");
        return str;
    }

    public static String htmlCodeEncode(String str) {
        str = str.replace("“","&ldquo;");
        str = str.replace( "”","&rdquo;");
        str = str.replace(" ","&nbsp;");
        str = str.replace("&amp;","&");
        str = str.replace( "'","&#39;");
        str = str.replace( "’","&rsquo;");
        str = str.replace( "—","&mdash;");
        str = str.replace("–","&ndash;");
        return str;
    }
}



