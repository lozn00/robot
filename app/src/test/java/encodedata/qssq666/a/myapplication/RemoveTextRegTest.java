package encodedata.qssq666.a.myapplication;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveTextRegTest {
    @Test
    public void keepNumberLetterChinese(String str){
        String regex = ".*?(\\d+).*?(\\w+).*?([\u4E00-\u9FA5]+).*";
        Pattern p = Pattern.compile(regex);
        Matcher m =p.matcher(str);
        if (m.find()) {
            int count = m.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println("["+m.group(i)+"]]");
            }
        }
    }
}
