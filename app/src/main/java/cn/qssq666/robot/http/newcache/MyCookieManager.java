package cn.qssq666.robot.http.newcache;

import java.util.HashMap;
import java.util.Map;

public class MyCookieManager {

    public HashMap<String, String> getCookiesMap() {
        return cookiesMap;
    }

    public void setCookiesMap(HashMap<String, String> cookiesMap) {
        this.cookiesMap = cookiesMap;
    }

   public HashMap<String, String> cookiesMap = new HashMap<>();

    public void addCookie(String cookieName, String cookieValue) {
        cookiesMap.put(cookieName, cookieValue);
    }

    public void setCookies(String cookies) {
        cookiesMap.clear();
        addCookies(cookies);
    }

    /**
     * 更新合并
     *
     * @param cookies
     */
    public void addCookies(String cookies) {
        String[] split = cookies.split(";");
        for (String s : split) {
            if (s == null || s.length() == 0) {
                continue;
            }
            String[] split1 = s.split("=");

            if (split1.length == 2) {
                String keyName = split1[0].trim();
                String keyValue = split1[1].trim();
                String before = cookiesMap.get(keyName);
                if (before != null && keyValue != null && !before.equals(keyValue)) {
                    MyLog.Log("更新Cookie:" + keyName ,"从" + before + ",变更为:" + keyValue);
                }
                cookiesMap.put(keyName, keyValue);

            } else {
                cookiesMap.put(s, "");
//                System.err.println("error cookie length:" + s);
            }
        }
    }

    public String getCookies() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append(";");
        }
        if (cookiesMap.size() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }

    public void mergeCookies(String cookies) {

    }
}
