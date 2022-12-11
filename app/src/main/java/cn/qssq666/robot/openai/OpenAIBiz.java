package cn.qssq666.robot.openai;

import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.text.TextUtils;

import androidx.core.util.TimeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashSet;

import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.RequestBean;
import cn.qssq666.robot.bean.ResultBean;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.business.OkHttpUtil;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.constants.TuLingType;
import cn.qssq666.robot.http.HttpUtilRetrofit;
import cn.qssq666.robot.http.api.OpenAI;
import cn.qssq666.robot.http.api.translate.OpenAIUtil;
import cn.qssq666.robot.interfaces.IIntercept;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.ErrorHelper;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.MapUrlCookie;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.RegexUtils;
import cn.qssq666.robot.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OpenAIBiz {
    static MapUrlCookie storeCookies = new MapUrlCookie();

    public static okhttp3.Response fetchTokenByCookie(String sessionToken) throws IOException {
        storeCookies.putKeyAndValue("__Secure-next-auth.session-token=" + sessionToken);

        okhttp3.Response json = OkHttpUtil.syncRequestGetBody(String.format(Cns.REQUEST_OPENAI_TOKEN_URL), new INotify<OkHttpClient.Builder>() {
            @Override
            public void onNotify(OkHttpClient.Builder okhttpclientBuilder) {

                Interceptor setHeaderInterceptor = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Connection", "close")
                                .addHeader("Cache-Control", "max-age=0")
                                .addHeader("Cookie", storeCookies.toString())
                                .addHeader("Accept-Charset", "UTF-8")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").build();
//                                addHeader("User-Agent", "Mozilla/5.0(Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.3506.8264 Safari/537.36").build();


                        return chain.proceed(request);
                    }
                };
//        at java.util.Collections$UnmodifiableCollection.add(Collections.java:928)
                okhttpclientBuilder.addInterceptor(setHeaderInterceptor);
                okhttpclientBuilder.addInterceptor(new OkHttpUtil.ReceivedCookiesInterceptor(new INotify<HashSet<String>>() {
                    @Override
                    public void onNotify(HashSet<String> param) {
                        for (String s : param) {
                            storeCookies.putKeyAndValue(s);
                        }
                    }
                }));
            }
        });
        return json;

//    __Secure-next-auth.session-token
    }

    public static void doOpenAI(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {
        Observable.create(new ObservableOnSubscribe<ResultBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<ResultBean> emitter) throws Exception {


                        long start = System.currentTimeMillis();

                        String conversation_id = OpenAIUtil.conversation_id;
                        String ask = msgItem.getMessage();
                        boolean newConversation = false;
                        if (ask.trim().toUpperCase().startsWith("R:") || TextUtils.isEmpty(conversation_id)) {
                            ask = ask.replace("R:", "");
                            conversation_id = "";
                            newConversation = true;
                        }
                        Retrofit retrofit = HttpUtilRetrofit.buildLongTimeRetrofit(Cns.ROBOT_OPEN_AI_DOMAIN, RobotContentProvider.getInstance().isManager(msgItem));
                        OpenAI projectAPI = retrofit.create(OpenAI.class);

                        ResultBean resultBean = new ResultBean();
                        try {
//                            "conversation_id": "be8d325d-85f8-43f7-a5d2-a9b3d5201dad",

                            String content = OpenAIUtil.GenereateBodyByText(conversation_id, OpenAIUtil.parentMessageID, "测试");
                            JSONObject jsonObject = JSON.parseObject(content);

                            if (newConversation) {
                                jsonObject.put("parent_message_id", "");
                            }
                            JSONArray jsonArray = jsonObject.getJSONArray("messages").getJSONObject(0).getJSONObject("content").getJSONArray("parts");
                            jsonArray.remove(0);
                            jsonArray.add(ask);
                            // bean.getInfo().trim());
                            content = jsonObject.toJSONString();
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
                            Call<String> call1 = projectAPI.query("application/json", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36", "Bearer " + RobotContentProvider.getInstance().robotReplyKey, requestBody);
                            Response<String> response = call1.execute();
                            if (!response.isSuccessful()) {
                                if (!RobotContentProvider.getInstance().isManager(msgItem)) {
                                    resultBean.setCode(-1);
                                    emitter.onNext(resultBean);
                                    return;
                                } else {
                                    if (response.code() == 401) {
                                        String errorJSON = response.errorBody().string();
                                        JSONObject jsonObjecteRROR = JSONObject.parseObject(errorJSON);
                                        String message = jsonObjecteRROR.getJSONObject("detail").getString("message");
                                        String err = "";
                                        if (message.contains("has expired")) {

                                            if (TextUtils.isEmpty(RobotContentProvider.getInstance().robotReplySecret)) {
                                                err = "AI执行出错,accessToken已过期,sessionToken为空请登录后访问https://chat.openai.com/api/auth/session复制token进行更新";

                                            } else {
                                                okhttp3.Response response1 = OpenAIBiz.fetchTokenByCookie(RobotContentProvider.getInstance().robotReplySecret);
                                                if (response1.isSuccessful()) {
                                                    String string = response1.body().string();
                                                    JSONObject jsonObjectNew = JSON.parseObject(string);
                                                    String accessToken = jsonObjectNew.getString("accessToken");
                                                    RobotContentProvider.getInstance().robotReplyKey = accessToken;
                                                    SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(RobotContentProvider.getInstance().getProxyContext());
                                                    sharedPreferences.edit().putString(AppUtils.getRobotReplyKey(2), accessToken).commit();
                                                    err = "AI执行出错,accessToken已过期,本次已刷新成功!";
//                                                "accessToken": "ey
                                                } else {
                                                    errorJSON = response1.body().string();
                                                    err = "AI执行出错,accessToken已过期,请登录后访问https://chat.openai.com/api/auth/session复制token进行更新.刷新token失败" + errorJSON;

                                                }

                                            }

                                        } else {

                                            err = "AI执行出错," + message;
                                        }
                                        LogUtil.writeLog(err);
                                        resultBean.setText(err);
                                    } else {
                                        int code = response.code();
                                        String err = "";
                                        String errorResult = response.errorBody().string();
                                        if (errorResult != null && errorResult.contains("<title>")) {
                                            String strCenter = StringUtils.getStrCenter(errorResult, "<title>", "</body>");
                                            errorResult = RegexUtils.deleteHtmlLabel(TextUtils.isEmpty(strCenter) ? errorResult : strCenter);
                                        }
                                        err = "AI执行出错," + errorResult + "" + response.code();
                                        LogUtil.writeLog(err);
                                        resultBean.setText(err);
                                    }
                                }
                            } else {

                                String str = response.body();


                                int i = str.lastIndexOf("{\"message\":");

//                    {"message": {"id": "2cafe753-4847-46bf-a3f3-cae98b88a7f2", "role": "assistant", "user": null, "create_time": null, "update_time": null, "content": {"content_type": "text", "parts": ["Yes, I can speak English. Is there something you would like to talk about?"]}, "end_turn": null, "weight": 1.0, "metadata": {}, "recipient": "all"}, "conversation_id": "3bed8d67-9312-4396-bdbe-687c63f18b89", "error": null}

                                int i1 = str.lastIndexOf("data: [DONE]");
                                if (i < 0 || i1 < 0) {
                                    resultBean.setText(str);
                                } else {
                                    String body = str.substring(i, i1 - 1);
                                    JSONObject jsonObjectResult = JSONObject.parseObject(body);
                                    OpenAIUtil.conversation_id = jsonObjectResult.getString("conversation_id");
                                    JSONObject messageObj = jsonObjectResult.getJSONObject("message");
                                    OpenAIUtil.parentMessageID = messageObj.getString("id");//随机请求
                                    String replyContent = messageObj.getJSONObject("content").getJSONArray("parts").getString(0);
                                    LogUtil.writeLog("AI回复内容:" + replyContent);
                                    resultBean.setText(replyContent);

                                }

                            }


                        } catch (Throwable e) {
                            if (RobotContentProvider.getInstance().isManager(msgItem)) {
                                resultBean.setText("[AI回复故障]" + e.getMessage());
                            } else {
                                resultBean.setCode(-1);
                                emitter.onNext(resultBean);
                                return;
                            }
                        }

                      /*  {
                            "action": "next",
                                "messages": [
                            {
                                "id": "d6352238-637f-48cb-9e71-f6dcf343c2f4",
                                    "role": "user",
                                    "content": {
                                "content_type": "text",
                                        "parts": [
                                "R"
                ]
                            }
                            }
    ],
                            "model": "text-davinci-002-render",
                                "parent_message_id": "d6252238-637f-48cb-9e71-f6dcf343c5f4"
                        }*/
//                        jsonObject.getJSONObject("messages").
                        long end = System.currentTimeMillis();
                        long times = (end - start);
                        String consumed = DateUtils.formatTimeDistance(times);
                        resultBean.setText(resultBean.getText() + "[" + consumed + "]");//    sb.append("[" + name + "]" + "查询[耗时" + (end - start) + "ms]\n");
                        resultBean.setCode(TuLingType.NORMAL);
                        emitter.onNext(resultBean);
                    }
                }).subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {

                        if (robotContentProvider.mAllowReponseSelfCommand && (resultBean.getCode() == TuLingType.NORMAL || resultBean.getCode() == TuLingType.LINK)) {
                            if (resultBean.getText().length() < 10) {
                                resultBean.setText("." + resultBean.getText());

                            }
                        }


/*
                        resultBean.setText(resultBean.getText().replace("[cqname]", RobotContentProvider.getInstance().mLocalRobotName));
                        resultBean.setText(resultBean.getText().replace("[name]", NickNameUtils.formatNickname(msgItem)));*/

                        if (resultBean.getCode() == TuLingType.NORMAL || resultBean.getCode() == TuLingType.LINK) {
                            String message = resultBean.getDetailMsg();


                            if (intercept != null && intercept.isNeedIntercept(resultBean)) {

                                return;
                            }
                            if (MsgTypeConstant.MSG_ISTROP_GROUP_PRIVATE_MSG_1 == msgItem.getIstroop() || MsgTypeConstant.MSG_ISTROOP__GROUP_PRIVATE_MSG == msgItem.getIstroop() || 0 == msgItem.getIstroop()) {
                                if (!msgItem.getSenderuin().equals(msgItem.getSelfuin()) && !msgItem.getFrienduin().equals(msgItem.getSelfuin())) {
                                    //
                                    msgItem.setExtrajson(msgItem.getSelfuin());//避免反反复复的回复消息。

                                }
                            }
                            MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, message, msgItem);


                        } else if (ErrorHelper.isNotSupportMsgType(resultBean.getCode())) {
//                    MsgItem msgItem1 = msgItem.setMessage("网络词库无法处理消息" + msgItem.getMessage() + "" + resultBean.getText()).setCode(-1);
//                    notifyHasDoWhileReply(msgItem1);
                            LogUtil.writeLog("出现错误" + resultBean);

                        } else {
//                    String msg = msgItem.setMessage("网络词库无法处理消息" + msgItem.getMessage() + ",type:" + msgItem.getType() + "  " + msgItem.getMessage() + ErrorHelper.codeToMessage(resultBean.getCode())).setCode(-1;
                            LogUtil.writeLog("出现错误 -e" + resultBean);
//                    notifyHasDoWhileReply());
                        }
                        LogUtil.writeLog("onResponse" + Thread.currentThread());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.writeLog("OpenAI错误", throwable.getMessage());
                        if (!RobotContentProvider.getInstance().mCfBaseNetReplyErrorNotWarn) {
                            MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, "无法回复 出现错误" + throwable.toString(), msgItem);

                        }

                    }
                });


/*

        FormBody formBody = new FormBody.Builder()//这种表单就是  urlencoded
                .add("question", bean.getInfo())
                .add("limit", (new Random().nextInt(3)) + "")
                .add("api_key", robotContentProvider.robotReplyKey)
                .add("api_secret", robotContentProvider.robotReplySecret).build();
        final Request request = new Request.Builder()
                .url(Cns.ROBOT_REPLY_MOLI_URL)
                .post(formBody)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);


        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                LogUtil.writeLog("fail" + call + Log.getStackTraceString(e));
                MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, msgItem.setMessage("" + e.getMessage()).setCode(Errors.NET_ERR));
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                String str = response.body().string();

                ResultBean resultBean = new ResultBean();
                resultBean.setText(str);
                resultBean.setCode(TuLingType.NORMAL);

                if (robotContentProvider.mAllowReponseSelfCommand) {
                    if (resultBean.getText().length() < 10) {
                        resultBean.setText("." + resultBean.getText());

                    }
                }

                if ((resultBean.getText() + "").contains("QQ")) {
                    String tep = "我只有爸爸,我爸爸是人类,他叫情迁,现在还单身呢?";
                    resultBean.setText(tep);
                } else if (resultBean.getText().equals("[cqname]")) {
                    String tep = "我暂时没有名字,请主人打开情迁聊天机器人进入高级调试->设置机器人key哦！";
                    resultBean.setText(tep);
                }

                if (RegexUtils.isContaineQQOrPhone(resultBean.getText())) {
                    resultBean.setText("检测到当前问题[" + msgItem.getMessage() + "]触发第三方网络词库发送垃圾广告,如QQ 手机号码,情迁聊天机器人已自动屏蔽!");


                }
                resultBean.setText(resultBean.getText().replace("[cqname]", RobotContentProvider.getInstance().mLocalRobotName));
                resultBean.setText(resultBean.getText().replace("[name]", NickNameUtils.formatNickname(msgItem)));

                if (resultBean.getCode() == TuLingType.NORMAL || resultBean.getCode() == TuLingType.LINK) {
                    String message = resultBean.getDetailMsg();


                    if (intercept != null && intercept.isNeedIntercept(resultBean)) {

                        return;
                    }
                    if (MsgTypeConstant.MSG_ISTROP_GROUP_PRIVATE_MSG_1 == msgItem.getIstroop() || MsgTypeConstant.MSG_ISTROOP__GROUP_PRIVATE_MSG == msgItem.getIstroop() || 0 == msgItem.getIstroop()) {
                        if (!msgItem.getSenderuin().equals(msgItem.getSelfuin()) && !msgItem.getFrienduin().equals(msgItem.getSelfuin())) {
                            //
                            msgItem.setExtrajson(msgItem.getSelfuin());//避免反反复复的回复消息。

                        }
                    }
                    MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, message, msgItem);


                } else if (ErrorHelper.isNotSupportMsgType(resultBean.getCode())) {
//                    MsgItem msgItem1 = msgItem.setMessage("网络词库无法处理消息" + msgItem.getMessage() + "" + resultBean.getText()).setCode(-1);
//                    notifyHasDoWhileReply(msgItem1);
                    LogUtil.writeLog("网络词库不支持" + resultBean);

                } else {
//                    String msg = msgItem.setMessage("网络词库无法处理消息" + msgItem.getMessage() + ",type:" + msgItem.getType() + "  " + msgItem.getMessage() + ErrorHelper.codeToMessage(resultBean.getCode())).setCode(-1;
                    LogUtil.writeLog("网络词库不支持 -e" + resultBean);
//                    notifyHasDoWhileReply());
                }
                LogUtil.writeLog("onResponse" + str + Thread.currentThread());


            }

        });
*/
    }
}
