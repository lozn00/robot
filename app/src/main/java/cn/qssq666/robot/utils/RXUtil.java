package cn.qssq666.robot.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.ResultBean;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.http.HttpUtilRetrofit;
import cn.qssq666.robot.http.api.translate.TranslateInterface;
import io.reactivex.functions.Function;

/**
 * Created by qssq on 2019/1/14 qssq666@foxmail.com
 */
public class RXUtil {
    public static Function<ResultBean, ResultBean> mapTranslateFunction(GroupWhiteNameBean whiteNameBean) {
        Function<ResultBean, ResultBean> transMap = new Function<ResultBean, ResultBean>() {
            @Override
            public ResultBean apply(ResultBean o) throws Exception {
                if (whiteNameBean.isReplyTranslate() && o.isNeedTranslate() && o.getText().length() < 50 && !o.getText().startsWith("{") && !o.getText().startsWith("[")) {


                    HashMap<String, String> requestHeadedr = new HashMap<>();

                    requestHeadedr.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

                    retrofit2.Call<String> call = HttpUtilRetrofit.buildRetrofit(Cns.TRANSLATE_URL).create(TranslateInterface.class).getCallByWord(requestHeadedr, o.getText());
                    retrofit2.Response<String> execute = call.execute();
                    String body = execute.body();
                    if (body == null) {

                        return o;
                    }
                    JSONObject jsonObject = new JSONObject(body);
                    StringBuilder sb = new StringBuilder();
                    int errorNo = jsonObject.optInt("errorCode", -5);
                    if (errorNo == 0) {
                        JSONArray translateResult = jsonObject.getJSONArray("translateResult");
                        if(translateResult.length()>0&&translateResult.getJSONArray(0).length()>0){
                            JSONObject current = translateResult.getJSONArray(0).getJSONObject(0);
                            sb.append(current.getString("tgt"));
                        } else {
                            sb.append(body);
                        }
                    } else {
                        sb.append(jsonObject.toString());
                    }
                    o.setText(o.getText() + "\n-----------\n" + sb.toString());
                }
                return o;
            }
        };
        return transMap;
    }

    /**
     * 英语翻译成中文。
     * @return
     */
    public static Function<MsgItem, String> mapEnglish2ChineseTranslateFunctionWord() {
        Function<MsgItem, String> transMap = new Function<MsgItem, String>() {
            @Override
            public String apply(MsgItem o) throws Exception {

                HashMap<String, String> requestHeadedr = new HashMap<>();

                requestHeadedr.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

                retrofit2.Call<String> call = HttpUtilRetrofit.buildRetrofit(Cns.TRANSLATE_URL).create(TranslateInterface.class).getEnglish2Word(requestHeadedr, o.getMessage());
                retrofit2.Response<String> execute = call.execute();
                String body = execute.body();
                JSONObject jsonObject = new JSONObject(body);
                StringBuilder sb = new StringBuilder();
                int errorNo = jsonObject.optInt("errorCode", -5);
                if (errorNo == 0) {
                    JSONArray translateResult = jsonObject.getJSONArray("translateResult");
                    if(translateResult.length()>0&&translateResult.getJSONArray(0).length()>0){
                        JSONObject current = translateResult.getJSONArray(0).getJSONObject(0);
                        sb.append(current.getString("tgt"));
                        return sb.toString();
                    } else {
                    }
                } else {
                }
                return o.getMessage();
            }
        };
        return transMap;
    }
}
