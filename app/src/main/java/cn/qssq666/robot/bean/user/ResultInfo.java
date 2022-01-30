package cn.qssq666.robot.bean.user;

import com.alibaba.fastjson.JSONObject;

public class ResultInfo {
    public static boolean isSucc(JSONObject jsonObject) {
        if(jsonObject.getIntValue("code")==200){
            return true;
        }else{
            return false;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    String message;
    int code;
}
