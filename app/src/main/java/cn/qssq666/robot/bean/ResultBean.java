package cn.qssq666.robot.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by luozheng on 2017/3/8.  qssq.space
 */

public class ResultBean {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {


        /*
        100000	文本类
200000	链接类
302000	新闻类
308000	菜谱类
313000（儿童版）	儿歌类
314000（儿童版）	诗词类
         */
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    int code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
    String text;

    public boolean isNeedTranslate() {
        return needTranslate;
    }

    public void setNeedTranslate(boolean needTranslate) {
        this.needTranslate = needTranslate;
    }

    boolean needTranslate=true;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getDetailMsg() {
        return text + " " + (url == null ? "" : " " + url);
    }



    /*

    public static String tulingReply(String json) {
        TulingResponse response = new Gson().fromJson(json, TulingResponse.class);
        if (response == null) return "";
        if (response.getCode().equals("100000")) {
            return response.getText();
        }
        if (response.getCode().equals("200000 ")) {
            return response.getText() + "\n" + response.getUrl();
        }
        return "";
    }
     */
}
