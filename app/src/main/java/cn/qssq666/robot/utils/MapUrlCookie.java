package cn.qssq666.robot.utils;

import java.util.HashMap;

public class MapUrlCookie {
    private HashMap<String, String> hashMap = new HashMap();

    /**
     *
     * @param str x=x的形式
     * @return
     */
    public boolean putKeyAndValue(String str) {
        if (!str.contains("=")) {
            return false;
        }
        int indexOf = str.indexOf("=");
        String replaceAll = str.substring(0, indexOf).replaceAll(" ", "");
        String substring = str.substring(indexOf + 1, str.indexOf(";"));
        if (substring.equals("")) {
            return false;
        }
        this.hashMap.put(replaceAll, substring);
        return true;
    }

    public String toString() {
        String str = "";
        for (String str2 : this.hashMap.keySet()) {
            str = new StringBuilder(String.valueOf(str)).append(str2).append("=").append((String) this.hashMap.get(str2)).append(";").toString();
        }
        return str;
    }
    
}
