package cn.qssq666.robot.proguardx.user;

import android.text.TextUtils;

import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.utils.CookieLocalFilePool;

public class UserInfo {
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String phone;
    String username;
    String nickname;

    public String getToken() {
        if(TextUtils.isEmpty(token)){
            String cookie = CookieLocalFilePool.getCookie(Cns.ROBOT_DOMAIN,Cns.ROBOT_DOMAIN);
            return cookie;
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;
    public long vipendtime;
    public double score;
    int id;
    String email;
   public int vip;
    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int  getx(){
        return vip;
    }

    public long getVipendtime() {
        return vipendtime;
    }

    public void setVipendtime(long vipendtime) {
        this.vipendtime = vipendtime*60*1000;//分钟转换为毫秒

    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
