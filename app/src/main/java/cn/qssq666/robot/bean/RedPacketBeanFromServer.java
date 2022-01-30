package cn.qssq666.robot.bean;
import cn.qssq666.CoreLibrary0;import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qssq on 2017/9/23 qssq666@foxmail.com
 * 这是qq插入到message 的bean结构
 */

public class RedPacketBeanFromServer {
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

  /*  public String getQqgroup() {
        return qqgroup;
    }

    public void setQqgroup(String qqgroup) {
        this.qqgroup = qqgroup;
    }*/

    public String getMoney() {
        return money;
    }

    public RedPacketBeanFromServer setMoney(String money) {
        this.money = money;
        return this;
    }


    public RedPacketBeanFromServer setResult(int result) {
        this.result = result;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RedPacketBeanFromServer setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    private String msg;
    private String qq;
    //    private String qqgroup;
    private String money;

    public int getResult() {
        return result;
    }

    private int result;

    public String getNickname() {
        return nickname;
    }

    public RedPacketBeanFromServer setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getGroupnickname() {
        return groupnickname;

    }

    public RedPacketBeanFromServer setGroupnickname(String groupnickname) {
        this.groupnickname = groupnickname;
        return this;
    }

    private String nickname;
    private String groupnickname;

    public int getType() {
        return type;
    }

    public RedPacketBeanFromServer setType(int type) {
        this.type = type;
        return this;
    }

    private int type;
    public String toJSONString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("qq", qq + "");
//            jsonObject.put("qqgroup", qqgroup + "");
            jsonObject.put("money", money + "");
            jsonObject.put("nickname", nickname + "");
            jsonObject.put("groupnickname", groupnickname + "");
            jsonObject.put("qq", qq + "");
            jsonObject.put("type", type + "");
            jsonObject.put("msg", msg + "");
            jsonObject.put("flag", flag + "");
            jsonObject.put("title", title + "");
            jsonObject.put("result", result + "");
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;

    }

    public static RedPacketBeanFromServer generateMsg(String money, int resultCode, String msg) {
        return new RedPacketBeanFromServer().setMoney(money).setResult(resultCode).setMsg(msg);
    }
}
