package encodedata.qssq666.a.myapplication;

import org.junit.Test;

public class TestEnglishReg {
    @Test
    public void englisth(){
        String str="hello ";
        boolean result = str.matches("[a-zA-Z]+");//true:全文英文
        System.out.println(str.matches("[a-zA-Z0-9]+"));//判断英文和
    }
}
