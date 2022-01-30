package cn.qssq666.robot.bean;

/**
 * Created by luozheng on 2017/3/8.  qssq.space
 */

public class RequestBean {
    /*

参数	是否必须	长度	示例	说明
key	必须	32	1ca8089********736b8ce41591426(32位)	注册之后在机器人接入页面获得（参见本文档第2部分）
info	必须	1-30	打招呼“你好”
查天气“北京今天天气”	请求内容，编码方式为UTF-8
userid	上下文
     */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String key;
    String info;
    String userid;
}
