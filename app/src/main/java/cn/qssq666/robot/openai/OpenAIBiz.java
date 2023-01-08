package cn.qssq666.robot.openai;

import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.RequestBean;
import cn.qssq666.robot.bean.ResultBean;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.constants.TuLingType;
import cn.qssq666.robot.http.HttpUtilRetrofit;
import cn.qssq666.robot.http.api.OpenAI;
import cn.qssq666.robot.http.api.translate.OpenAIUtil;
import cn.qssq666.robot.http.newcache.CookieMemoryPool;
import cn.qssq666.robot.http.newcache.HttpUtil;
import cn.qssq666.robot.http.newcache.MyCookieManager;
import cn.qssq666.robot.http.newcache.MyLog;
import cn.qssq666.robot.interfaces.IIntercept;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.CookieLocalFilePool;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.ErrorHelper;
import cn.qssq666.robot.utils.LogUtil;
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
    public static String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

    //    static MapUrlCookie storeCookies = new MapUrlCookie();
    public static void updateOpenAICookie(String cookie) {

        SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(AppContext.getInstance());
        sharedPreferences.edit().putString(AppUtils.getRobotReplySecret(RobotContentProvider.getInstance().defaultReplyIndex), cookie).commit();
        RobotContentProvider.getInstance().robotReplySecret = cookie;
        CookieMemoryPool.saveCookie("https://chat.openai.com", "chat.openai.com", RobotContentProvider.getInstance().robotReplySecret);
        CookieLocalFilePool.saveCookie("https://chat.openai.com", "chat.openai.com", RobotContentProvider.getInstance().robotReplySecret);

    }

    public static MyCookieManager tempCoverMergeCookie(String token) {

        MyCookieManager myCookieManager = new MyCookieManager();
        String cookie = CookieMemoryPool.getCookie("https://chat.openai.com", "chat.openai.com");
        myCookieManager.addCookies(cookie);
        myCookieManager.addCookies(token);//覆盖之前的
        RobotContentProvider.getInstance().robotReplySecret = myCookieManager.getCookies();
        CookieLocalFilePool.saveCookie("https://chat.openai.com", "chat.openai.com", myCookieManager.getCookies());
        CookieMemoryPool.saveCookie("https://chat.openai.com", "chat.openai.com", myCookieManager.getCookies());
        return myCookieManager;

    }

    public static void updateRequestHeaderMark(String requestMark) {
        SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(AppContext.getInstance());
        RobotContentProvider.getInstance().robotReplyRequestMark = requestMark;
        sharedPreferences.edit().putString(AppUtils.getRobotReplyDefaultRequest(RobotContentProvider.getInstance().defaultReplyIndex), requestMark).commit();
    }

    public static okhttp3.Response fetchTokenByCookie() throws IOException {
        String cookie = CookieMemoryPool.getCookie("https://chat.openai.com", "chat.openai.com");
        if (TextUtils.isEmpty(cookie)) {
            cookie = CookieLocalFilePool.getCookie("https://chat.openai.com", "chat.openai.com");
            if (TextUtils.isEmpty(cookie)) {
                CookieMemoryPool.saveCookie("https://chat.openai.com", "chat.openai.com", RobotContentProvider.getInstance().robotReplySecret);
                CookieLocalFilePool.saveCookie("https://chat.openai.com", "chat.openai.com", RobotContentProvider.getInstance().robotReplySecret);
                LogUtil.writeLog("cookie为空,提交更新!");
            }
        }

        MyCookieManager myCookieManager = new MyCookieManager();
        myCookieManager.addCookies(cookie);
//        cf_clearance	wq_MgemFrIWrA9.8hhE0y1TAE73s4GUJbPrYCUTCoH0-1670833682-0-160
//        myCookieManager.cookiesMap.put("cf_clearance", "wq_MgemFrIWrA9.8hhE0y1TAE73s4GUJbPrYCUTCoH0-1670833682-0-160");
//        myCookieManager.cookiesMap.put("__Secure-next-auth.session-token", "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..3UbJXWOs6B13Hbwi.gBloMCaYzt726vYvSAIRu6rHBx89Afr4t0H0XOb5bCrMKWXjkG3FK9p8xyG0GdiJ87cvUwbNzqKqbVPDI6TokqkfyudTX4_OtWmScEvKjsu5yuKo61VqMeVGGaOikKzcNXOVKg8C2YzdlkOf90P44OdWA6YgNp-Mh02zWeCJ8npr_Cr9tQPeCKeuOlF4Hj4P9LOT7L0injuilMSudNp0vqcBn1y6OjKzj0DeL8z55EWlbcQKdVV67g320EaZP7a2Ck9xaqugN_1zWWqsaqCBN3HB8yc7yTgMGUnNcmqV_HdRuCfaJfFK89hCZBMBxQ3kViQcSIug0MIp9aSpFrNWjVOCzaDQsSC359FRgmARGUmQ-nkSltaeiTAgykqyxolbLbqCQ_q-xzr4FTQocz7eNxiG_ue4IuOHl94vStaTv38zk--sp1ByQ3cT43syZXzo-75Zdo9X243njFaf2e0nP2XAFV_CDntoRtW3G6Y7ZFlaQqaaPcfg96IAvHof69wA1kl2JxQQkJ_340U68wDFgZx-yygMwV6ThuHQUVJ15mKGvHRqOSZ9rrruOTsZmcV-LTh3JhJmtwxiR0ecQwgmWIl_jM5yJLReuO1cQb5wJ2nXqm2D8iPrYnOOs6ZmNujuArWgkuNj6gYS9NXkmZxPhS58cvaH_xp81tVQjDtrMdMoVDQV1gYa71a1UdC2hmDf0PL4o4rM0xh5ecAzwkkD7YL3iYivqOGESFGcC3EXLsTlFyRp0Y5h1tb9pZNQlcgxD422wjU4yKM2JrOyTTgj7JcZXxMSs231dY2Jgio4ZbATaE9PrMGZ9_xyLe0HYR1n_PNUECeX0uKpsMJ6RMghux8HomTt7eO2v-ATiIClufGIcRVTv_l5HGZ9hqkV7VXGSq6uXmmFc3FNTFxr9ulrEYTgi44goy3fRkd_zfvASHjGRRw3lM7amlEsLYb3azkOx3OdqUpZ8KZR0shLoTJPxmkMv5BdEdOsONG21g3rePe3Gf_2fhZx9oVSMzwJmWIuJGYXAOtykj2k6IU0Zy46MHk4ZaCtbehvARcl7bBYhnUqQuPFAwpRjxZGwFjiELT0oDMalYXmVAIWldFnzhLbX_28TgzkXFhncL8t7fz1NCnma2cy5Waz_qOdv6TW2M3qPTa3HGVHI93uP2wyO_2dZlbXiS_B5kaGd8WzjQTc8IAcOEKfR2ZF855Aff57cpoNx5beJtDU5tmMBnf69hPSfw4Btk3l_JbJ6s3MUe4aoZmz-EU15PS4Iv3Ta8mTc6yFpulv_1ZxBGBRqsG3yO8Z7Gi2CVWTJFJTZzEwPICX1sGevfCH4lZtSrjS_r975YQ5cyd3a3sxq6fUZ0rh2CDKlJsGaKeEEh8okXCRY0mn_JMpcIAUusjjfixBWisODL7azT8x7UVsjYFNim-9P4aYkHjRSD1EQU7J1jWlk4qUXsCMx0P8n94lUZRlE4E-LD-5NfI_7b21_cSZoVr_BgcolYobEh2yZXbFRLw1oxa6j-XzSUmyVG8jiE-JQVQ8L393XAPccws46r5vg65kf5BEyXuoO8dqhIU_GcuNLH0TvRSyflw0dIwl1lv9n05u2E-3WBLDumapgUwwLaN60HMBk6QVVk_pS5_9CXsY4Y6BxtMzg_ntTLv0cJId7I5CEcUTBRh2Ab88eX81SX83e-u4BSZK7uWQEHRnN_WMz8j0nWUJOX4CIXbySU6gL-7DS5qoIUjmWbqFSDZJwZoRmVDfugj6X4ST9hCTp8tPjV6mbr82u-cPyNdNKRj7DfMUqQfbeXVCx12vlUz502-r0pQRUfvcy8LxiFVLDX2P1lop7b03H38tbZx66OWa8vrcLW38x4DbSpaEU4Qk8FIwJ3k64OmJxRfL9kubnRC2c5liFgKS-Apru5XE-Ety--GKBl2W3z-pxkNJpjCULjDEvkn3YLM_qzzn0NATpnjdmbusfZ4s0NoxOOBJi7ltzHbslfYedvCu6oFSx3KcY8ozhr4hXWe4b7If4dZwJLVsli7ACjwPdvFwaw4KvJuWwFtbfcW3SvN_qUTfYEhs76siSnEnmfw1cASH3gQHnd6KbhO8-9qQgk-0FdPRKlrgd5EcXD5ZqR_L9bgZp4YmjHFovB2nhtS9j9XtFow2eX_SgPM7k4my2A8IlzbE-0Pf3j0FtoPq6vRS7lAU1IDAmpwdEqurgqOfs3Jhbi6xQQ2rwaIZ2qWXm_B99GxKabNKDWZN26E.Z9q6U3HXX-xhQyzZjZT43A");
        String sessionToken = myCookieManager.cookiesMap.get("__Secure-next-auth.session-token");
        String cf_clearance = myCookieManager.cookiesMap.get("cf_clearance");
        MyLog.Log("cf_clearance:" + cf_clearance);
        MyLog.Log("__Secure-next-auth.session-token:" + sessionToken);
        HashMap<String, String> header = OpenAIBiz.genereateBaseHeader();
        String format = String.format(Cns.REQUEST_OPENAI_TOKEN_URL);
        okhttp3.Response json = HttpUtil.querySyncGetData(format, header, null);
  /*
        okhttp3.Response json = OkHttpUtil.syncRequestGetBody(, new INotify<OkHttpClient.Builder>() {
            @Override
            public void onNotify(OkHttpClient.Builder okhttpclientBuilder) {
                okhttpclientBuilder.addInterceptor(new AddCookiesInterceptor()).addInterceptor(new SaveCookiesInterceptor());
                Interceptor setHeaderInterceptor = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Connection", "close")
                                .addHeader("Cache-Control", "max-age=0")
                                .addHeader("Accept-Charset", "UTF-8")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").build();


                        return chain.proceed(request);
                    }
                };
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
*/

        return json;

//    __Secure-next-auth.session-token
    }

    public static HashMap<String, String> genereateBaseHeader() {
        HashMap<String, String> header = new LinkedHashMap<>();
//        header.put("connection", "close");
        header.put("user-agent", OpenAIBiz.UserAgent);
        header.put("Accept", "text/event-stream");
        header.put("X-Openai-Assistant-App-Id", "");
        header.put("Referer", "https://chat.openai.com/chat");
        header.put("Connection", "close");
        header.put("Content-Type", "application/json");
        header.put("accept-language", "zh-CN,zh;q=0.9");
//        header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
//        header.put("accept-charset", "UTF-8");
        replaceRequestHeader(header);
        return header;
    }


    public static void doOpenAIByPayAPISercret(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {

        Observable.create(new ObservableOnSubscribe<ResultBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<ResultBean> emitter) throws Exception {


                        long start = System.currentTimeMillis();

                        String conversation_id = OpenAIUtil.conversation_id;
                        String ask = msgItem.getMessage();
                        HashMap<String, String> header = genereateBaseHeader();
                        Retrofit retrofit = HttpUtilRetrofit.buildLongTimeRetrofit(Cns.ROBOT_OPEN_AI_DOMAIN_API, RobotContentProvider.getInstance().isManager(msgItem), new INotify<OkHttpClient.Builder>() {
                            @Override
                            public void onNotify(OkHttpClient.Builder builder) {
                                if (header != null) {
                                    builder.addInterceptor(new Interceptor() {
                                        @NonNull
                                        @Override
                                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                                            Request request = chain.request();
                                            Request.Builder builder1 = request.newBuilder();

                                            for (Map.Entry<String, String> stringStringEntry : header.entrySet()) {

                                                builder1.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
                                            }
                                            Request request1 = builder1.build();
                                            return chain.proceed(request1);

                                        }
                                    });


                                }
                            }
                        });
                        OpenAI projectAPI = retrofit.create(OpenAI.class);
                        ResultBean resultBean = new ResultBean();
                        try {
                            String content = OpenAIUtil.GenereateBodyByTextUseApi(ask);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
                            Call<String> call1 = projectAPI.queryByAPI( "Bearer " +robotContentProvider._miscConfig.chatgpt_api_sercret_key, requestBody);
                            Response<String> response = call1.execute();
                            if (!response.isSuccessful()) {
                                if (!RobotContentProvider.getInstance().isManager(msgItem)) {
                                    resultBean.setCode(-1);
                                    emitter.onNext(resultBean);
                                    return;
                                } else {
                                    int code = response.code();
                                    String err = "";
                                    String errorResult = response.errorBody().string();
                                    if (errorResult != null && errorResult.contains("<title>")) {
                                        String strCenter = StringUtils.getStrCenter(errorResult, "<title>", "</body>");
                                        errorResult = RegexUtils.deleteHtmlLabel(TextUtils.isEmpty(strCenter) ? errorResult : strCenter);
                                    }
                                    err = "AI执行出错," + errorResult + "" + code;
                                    LogUtil.writeLog(err);
                                    resultBean.setText(err);
                                }
                            } else {


                                String str = response.body();
                                int i = str.lastIndexOf("text\"");
                                if (i < 0) {
                                    resultBean.setText(str);
                                } else {
                                    JSONObject jsonObjectResult = JSONObject.parseObject(str);
//                                    OpenAIUtil.conversation_id = jsonObjectResult.getString("id");
                                    JSONArray choices = jsonObjectResult.getJSONArray("choices");
                                    String text = choices.getJSONObject(0).getString("text");
                                    LogUtil.writeLog("AI回复内容:" + text);
                                    resultBean.setText(text);

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

    }

    public static void doOpenAI(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {
        if (TextUtils.isEmpty(robotContentProvider._miscConfig.chatgpt_api_sercret_key)) {
            OpenAIBiz.doOpenAIByWebChatGPT(robotContentProvider, msgItem, bean, whiteNameBean, intercept);

        } else {
            OpenAIBiz.doOpenAIByPayAPISercret(robotContentProvider, msgItem, bean, whiteNameBean, intercept);
        }
    }
    public static void   doOpenAIByWebChatGPT(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {
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
                        HashMap<String, String> header = genereateBaseHeader();
                        Retrofit retrofit = HttpUtilRetrofit.buildLongTimeRetrofit(Cns.ROBOT_OPEN_AI_DOMAIN, RobotContentProvider.getInstance().isManager(msgItem), new INotify<OkHttpClient.Builder>() {
                            @Override
                            public void onNotify(OkHttpClient.Builder builder) {
                                if (header != null) {
                                    builder.addInterceptor(new Interceptor() {
                                        @NonNull
                                        @Override
                                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                                            Request request = chain.request();
                                            Request.Builder builder1 = request.newBuilder();

                                            for (Map.Entry<String, String> stringStringEntry : header.entrySet()) {

                                                builder1.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
                                            }
                                            Request request1 = builder1.build();
                                            return chain.proceed(request1);

                                        }
                                    });


                                }
                            }
                        });
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
                            Call<String> call1 = projectAPI.query("application/json", OpenAIBiz.UserAgent, "Bearer " + RobotContentProvider.getInstance().robotReplyKey, requestBody);
                            Response<String> response = call1.execute();
                            if (!response.isSuccessful()) {
                                if (!RobotContentProvider.getInstance().isManager(msgItem)) {
                                    resultBean.setCode(-1);
                                    emitter.onNext(resultBean);
                                    return;
                                } else {
                                    int errcode = response.code();
                                    if (errcode == 401 || errcode == 403) {
                                        if (errcode == 403) {
                                            String err;
                                            String errorJSON = response.errorBody().string();
                                            okhttp3.Response response1 = OpenAIBiz.fetchTokenByCookie();
                                            if (response1.isSuccessful()) {
                                                String string = response1.body().string();
                                                JSONObject jsonObjectNew = JSON.parseObject(string);
                                                String accessToken = jsonObjectNew.getString("accessToken");
                                                RobotContentProvider.getInstance().robotReplyKey = accessToken;
                                                SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(RobotContentProvider.getInstance().getProxyContext());
                                                sharedPreferences.edit().putString(AppUtils.getRobotReplyKey(2), accessToken).commit();
                                                err = "AI执行出错403,accessToken已过期,本次已刷新成功!";
//                                                "accessToken": "ey
                                            } else {
                                                String string = StringUtils.getStrByLen(response1.body().string(), 40);
                                                err = "AI执行出错403,accessToken已过期,刷新出错," + string + "请登录后访问https://chat.openai.com/api/auth/session复制token进行更新.刷新token失败" + StringUtils.getStrByLen(RegexUtils.deleteHtmlLabel(errorJSON), 100);

                                            }
                                            LogUtil.writeLog(err);
                                            resultBean.setText(err);
                                        } else {
                                            String errorJSON = response.errorBody().string();
                                            JSONObject jsonObjecteRROR = JSONObject.parseObject(errorJSON);
                                            String message = jsonObjecteRROR.getJSONObject("detail").getString("message");
                                            String err = "";
                                            if (message.contains("has expired")) {

                                                if (TextUtils.isEmpty(RobotContentProvider.getInstance().robotReplySecret)) {
                                                    err = "AI执行出错" + errcode + ",accessToken已过期,sessionToken为空请登录后访问https://chat.openai.com/api/auth/session复制token进行更新";

                                                } else {
                                                    okhttp3.Response response1 = OpenAIBiz.fetchTokenByCookie();
                                                    if (response1.isSuccessful()) {
                                                        String string = response1.body().string();
                                                        JSONObject jsonObjectNew = JSON.parseObject(string);
                                                        String accessToken = jsonObjectNew.getString("accessToken");
                                                        RobotContentProvider.getInstance().robotReplyKey = accessToken;
                                                        SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(RobotContentProvider.getInstance().getProxyContext());
                                                        sharedPreferences.edit().putString(AppUtils.getRobotReplyKey(2), accessToken).commit();
                                                        err = "AI执行出错" + errcode + ",accessToken已过期,本次已刷新成功!";
//                                                "accessToken": "ey
                                                    } else {
                                                        errorJSON = response1.body().string();
                                                        err = "AI执行出错" + errcode + ",accessToken已过期,请登录后访问https://chat.openai.com/api/auth/session复制token进行更新.刷新token失败" + errorJSON;

                                                    }

                                                }

                                            } else {

                                                err = "AI执行出错," + message;
                                            }

                                            LogUtil.writeLog(err);
                                            resultBean.setText(err);
                                        }
                                    } else {
                                        int code = response.code();
                                        String err = "";
                                        String errorResult = response.errorBody().string();
                                        if (errorResult != null && errorResult.contains("<title>")) {
                                            String strCenter = StringUtils.getStrCenter(errorResult, "<title>", "</body>");
                                            errorResult = RegexUtils.deleteHtmlLabel(TextUtils.isEmpty(strCenter) ? errorResult : strCenter);
                                        }
                                        err = "AI执行出错," + errorResult + "" + code;
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

                                    OpenAIBiz.delayRefreshToken();

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

    }

    private static void delayRefreshToken() {
        AppContext.getHandler().removeCallbacks(runnable);
        AppContext.getHandler().postDelayed(runnable, 1000 * 60 * 5);
    }

    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            okhttp3.Response response1 = null;
            try {
                response1 = OpenAIBiz.fetchTokenByCookie();
                if (response1.isSuccessful()) {
                    String string = response1.body().string();
                    JSONObject jsonObjectNew = JSON.parseObject(string);
                    String accessToken = jsonObjectNew.getString("accessToken");
                    RobotContentProvider.getInstance().robotReplyKey = accessToken;
                    SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(RobotContentProvider.getInstance().getProxyContext());
                    sharedPreferences.edit().putString(AppUtils.getRobotReplyKey(2), accessToken).commit();
                    MyLog.Log("后台刷新成功");
                    delayRefreshToken();
//                                                "accessToken": "ey
                } else {
                    String string = StringUtils.getStrByLen(response1.body().string(), 40);
                    MyLog.Log("后台无法刷新成功" + string + ",停止刷新");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static void init() {
        SharedPreferences sharedPreferences = AppUtils.getConfigSharePreferences(AppContext.getContext());
        RobotContentProvider.getInstance().robotReplyRequestMark = sharedPreferences.getString(AppUtils.getRobotReplyDefaultRequest(RobotContentProvider.getInstance().defaultReplyIndex), "");
//        sharedPreferences.edit().putString(AppUtils.getRobotReplyDefaultRequest(RobotContentProvider.getInstance().defaultReplyIndex), requestMark).commit();
    }

    public static void replaceRequestHeader(HashMap<String, String> map) {
        String temp = RobotContentProvider.getInstance().robotReplyRequestMark;
        if (temp != null && temp.length() > 0) {
            parseRequestHeaderPutMap(map, temp);
            reqeustHeaderRemoveRepeat(map);
        }


    }

    public static void reqeustHeaderRemoveRepeat(HashMap<String, String> map) {
        if (map instanceof LinkedHashMap) {
            HashMap<String, String> saveKeys = new HashMap<>();
            List<String> needRemoveKeyList = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey().toUpperCase();
                String oldKey = saveKeys.get(key);
                if (oldKey != null) {
                    needRemoveKeyList.add(oldKey);
                    LogUtil.writeLog("清除比较老的key,重复请求头大小写不一致,之前老的" + oldKey + ",现在的:" + entry.getKey());
                }
                saveKeys.put(key, entry.getKey());

            }
            for (String s : needRemoveKeyList) {
                map.remove(s);
            }

        }
    }

    public static void parseRequestHeaderPutMap(HashMap<String, String> map, String temp) {
        if (temp == null || temp.length() == 0) {
            return;
        }
        if (temp.contains("{")) {
            JSONObject jsonObject = JSON.parseObject(temp);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                doPutHeader(map, key.trim(), value.trim());
            }
        } else if (temp.trim().startsWith("curl ") || temp.trim().startsWith(" curl ")) {
            String[] split = temp.trim().split("\n");
            String flag = "";
            if (temp.contains("curl \"") || temp.contains("curl  \"")) {
                flag = "\"";
            } else if (temp.contains("curl \'") || temp.contains("curl  \'")) {
                flag = "\'";
            }
            for (String s : split) {
                if (s.trim().contains("-H ") && s.contains(":")) {


                    String strCenter = StringUtils.getStrCenter(s, flag, flag, true);
                    if (TextUtils.isEmpty(strCenter)) {

                    }
                    if (!TextUtils.isEmpty(strCenter)) {
                        String[] strings = StringUtils.splitKeyValue(strCenter, ":");
                        if (strings.length == 2) {
                            String key = strings[0].trim();
                            String value = strings[1].trim();
                            doPutHeader(map, key, value);
                        }
                    }

                }
            }

        } else {
            boolean contains = temp.contains("|");
            boolean containsEqual = temp.contains("=");
            String[] split = contains ? StringUtils.splitKeyValue(temp, "|") : (containsEqual ? new String[]{temp} : new String[0]);
            for (String s : split) {
                if (!TextUtils.isEmpty(s)) {
                    String[] headers = StringUtils.splitKeyValue(s.trim(), "=");
                    if (headers.length == 2) {
                        String key = headers[0].trim();
                        String value = headers[1].trim();
                        doPutHeader(map, key, value);
                    }
                }
            }
        }
    }

    private static void doPutHeader(HashMap<String, String> map, String key, String value) {
        value = value.replace("text/hhtml", "/html");
        value = value.replace("text/web", "text/html");
        if (key.equalsIgnoreCase("User-Agent")) {
            OpenAIBiz.UserAgent = value;
            LogUtil.writeLog("存档UserAgent:" + key + ":" + value);
        } else if (key.equalsIgnoreCase("cookie")) {

            MyCookieManager myCookieManager = new MyCookieManager();
            myCookieManager.setCookies(value);
            String sessionToken = myCookieManager.cookiesMap.get("__Secure-next-auth.session-token");
            String cf_clearance = myCookieManager.cookiesMap.get("cf_clearance");
            String value1 = "__Secure-next-auth.session-token=" + sessionToken + ";cf_clearance=" + cf_clearance;
            OpenAIBiz.updateOpenAICookie(value1);
//            map.put(key + "_", "replace_");
            LogUtil.writeLog("存档Cookie:" + key + "->" + value);
        }
//        else if (key.equalsIgnoreCase("accept")&&key.contains("pplication/xhtm")&&!key.contains("/html")) {//qq格式转换问题
//            OpenAIBiz.updateOpenAICookie(value);
//            String strRight = StringUtils.getStrRight(key, "html,");
//            if(TextUtils.isEmpty(strRight)){
//                value="text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
//            }else{
//                value="text/html,"+strRight;
//            }
//            map.put(key, value);
//            LogUtil.writeLog("存档Cookie:" + key + "->" + value);
//        }
        else {
            if (map.containsKey(key)) {
                map.remove(key);
            }
            map.put(key, value);
            LogUtil.writeLog("替换请求头:" + key + ":" + value);
        }//cept: textm,application/xhtm
    }

    public static boolean isOpenApiuRL(String url) {
        String domainRemoveSchame = CookieLocalFilePool.getDomainRemoveSchame(url);
        if (domainRemoveSchame.equals("chat.openai.com")) {
            return true;
        }

        return false;
    }
}
