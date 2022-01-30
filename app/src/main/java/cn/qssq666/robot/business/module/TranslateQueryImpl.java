package cn.qssq666.robot.business.module;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import androidx.core.util.Pair;
import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.BaseQueryImpl;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.http.HttpUtilRetrofit;
import cn.qssq666.robot.http.api.translate.TranslateInterface;
import cn.qssq666.robot.utils.AppUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 、
 * http://open.iciba.com/dsapi 每次词语
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public class TranslateQueryImpl extends BaseQueryImpl {

    /*
    中文翻译
    {
    status: 1,
    content: {
    from: "zh-CN",
    to: "en-US",
    out: "you",
    vendor: "ciba",
    err_no: 0
    }
    }


    {
    status: 0,
    content: {
    ph_en: "",
    ph_am: "",
    ph_en_mp3: "",
    ph_am_mp3: "",
    ph_tts_mp3: "http://res-tts.iciba.com/e/6/c/e6c151d449e1db05b1ffb5ad5ec656cf.mp3",
    word_mean: [
    "abbr. Nicaragua 尼加拉瓜;"
    ]
    }
    }


    日语翻译为中文
    {
    status: 1,
    content: {
    from: "ja-JP",
    to: "zh-CN",
    vendor: "xinyi",
    out: "你是",
    ciba_use: "来自机器翻译。",
    ciba_out: "",
    err_no: 0
    }
    }
    一般都是0

     */
    @Override
    public void doAction(MsgItem item, boolean isgroupMsg, GroupWhiteNameBean nameBean, String[] args, Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> atPair, String text) {
        HashMap<String,String> requestHeadedr=new HashMap<>();

        requestHeadedr.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

        Call<String> callByWord = HttpUtilRetrofit.buildRetrofit(Cns.TRANSLATE_URL).create(TranslateInterface.class).getCallByWord(requestHeadedr,text);

        //        Call<String> callByWord = HttpUtilRetrofit.buildRetrofit(Cns.TRANSLATE_URL).create(TranslateInterface.class).getCallByWord(requestHeadedr,text);
        callByWord.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {

                    ResponseBody responseBody = response.errorBody();
                    String string = "";
                    try {
                        string = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MsgReCallUtil.smartReplyMsg("翻译失败,服务器错误" + AppUtils.getSimpleInfomation(string) + ",code:" + response.code(), isgroupMsg, nameBean, item);

                    return;
                }

                String body = response.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(body);
                    StringBuilder sb = new StringBuilder();
                     {
                        int errorNo = jsonObject.optInt("errorCode", -5);
                        if(errorNo==0){
                            JSONArray translateResult = jsonObject.getJSONArray("translateResult");
                            if(translateResult.length()>0&&translateResult.getJSONArray(0).length()>0){
                                JSONObject current = translateResult.getJSONArray(0).getJSONObject(0);
                                sb.append(current.getString("tgt"));
                            }else{
                              sb.append(body);
                            }
                        }else{
                                sb.append(jsonObject.toString());
                        }
                            //http://fanyi.youdao.com/translate?&doctype=json&type=txt&i=helloi
//                            {"type":"EN2ZH_CN","errorCode":0,"elapsedTime":56,"translateResult":[[{"src":"hello","tgt":"你好"}]]}

                    }

                    MsgReCallUtil.smartReplyMsg("." + sb.toString(), isgroupMsg, nameBean, item);
                } catch (JSONException e) {
                    e.printStackTrace();
                    MsgReCallUtil.smartReplyMsg("抱歉,翻译失败,数据错误" + e.getMessage(), isgroupMsg, nameBean, item);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MsgReCallUtil.smartReplyMsg("抱歉,翻译失败,服务器错误" + t.getMessage(), isgroupMsg, nameBean, item);

            }
        });
    }
}
