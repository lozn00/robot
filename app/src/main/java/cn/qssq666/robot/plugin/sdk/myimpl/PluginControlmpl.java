package cn.qssq666.robot.plugin.sdk.myimpl;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.business.MsgTyeUtils;
import cn.qssq666.robot.business.ParamParseUtil;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.business.SearchPluginMainImpl;
import cn.qssq666.robot.constants.ControlCode;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.event.DelegateSendMsg;
import cn.qssq666.robot.interfaces.DelegateSendMsgType;
import cn.qssq666.robot.plugin.sdk.interfaces.IApiCallBack;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DensityUtil;
import cn.qssq666.robot.utils.HttpUtilOld;
import cn.qssq666.robot.utils.ImageUtil;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.RegexUtils;
import cn.qssq666.robot.utils.RobotUtil;
import cn.qssq666.robot.utils.VarCastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class PluginControlmpl implements PluginControlInterface {
    static PluginControlmpl pluginControl;

    public static PluginControlInterface getInstance() {

        if (pluginControl == null) {
            synchronized (PluginControlmpl.class) {
                if (pluginControl == null) {
                    pluginControl = new PluginControlmpl();
                }
            }
        }
        return pluginControl;
    }

    @Override
    public void sendGroupMsg(IMsgModel item) {
        if (item == null) {
            return;
        }
        if (!checkParamSucc(item)) {
            return;
        }
        item.setIstroop(1);
        DelegateSendMsg delegateMsg = DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.GROUP, null, item);
        doPost(delegateMsg);

    }

    private void doPost(final DelegateSendMsg delegateMsg) {


        if (RobotContentProvider.getInstance().isAsPluginLoad()) {
            if (Thread.currentThread() == RobotContentProvider.getInstance().getProxyContext().getMainLooper().getThread()) {

                RobotContentProvider.getInstance().onReceiveDelegateSendMsg(delegateMsg);
            } else {

                RobotContentProvider.getInstance().getHandler().post(new DelegateProxyRunnableMsg(delegateMsg));
            }

        } else {
            EventBus.getDefault().post(delegateMsg);
        }

    }

    static class DelegateProxyRunnableMsg implements Runnable {


        public DelegateProxyRunnableMsg(DelegateSendMsg delegateMsg) {
            this.delegateMsg = delegateMsg;
        }

        private DelegateSendMsg delegateMsg;

        @Override
        public void run() {
            RobotContentProvider.getInstance().onReceiveDelegateSendMsg(delegateMsg);
        }
    }

    @Override
    public void sendQQMsg(IMsgModel item) {
        if (item == null) {
            return;
        }
        if (!checkParamSucc(item)) {
            return;
        }
        item = item.clone();
        item.setIstroop(0);
        item.setFrienduin(item.getSenderuin());
        DelegateSendMsg delegateMsg = DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.PRIVATE, null, item);


        doPost(delegateMsg);

    }


    @Override
    public void sendMsg(IMsgModel item) {
        if (item == null) {
            return;
        }
//        item.setCode()
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }


        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));

    }

    @Override
    public void sendGroupPrivateMsg(IMsgModel item, String group, String qq, String message) {
        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }
        item.setFrienduin(qq);

        item.setSenderuin(group);
        item.setIstroop(MsgTypeConstant.MSG_ISTROOP__GROUP_PRIVATE_MSG);

        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));
//        RobotContentProvider.getInstance().getConfigQueryImpl().isManager()
//        RobotContentProvider.getInstance().getPluginControlInterface().isGroupMsg()

    }

    @Override
    public void sendDiscussisonPrivateMsg(IMsgModel item, String group, String qq, String message) {
        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }


        item.setFrienduin(qq);
        item.setSenderuin(group);
        item.setIstroop(MsgTypeConstant.MSG_ISTROOP_DISCUSSION_GROUP_PRIVATE_MSG);

        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));
    }

    @Override
    public void sendGroupMsg(IMsgModel item, String group, String qq, String message) {

        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }

        item.setIstroop(1);
        item.setFrienduin(group);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));

    }


    @Override
    public void sendDiscussisonMsg(IMsgModel item, String group, String message) {

        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }

        item.setIstroop(MsgTypeConstant.MSG_ISTROOP_DISCUSSION_GROUP_MSG);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));

    }


    @Override
    public void sendPrivateMsg(IMsgModel item, String qq, String message) {

        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item)) {
            return;
        }

        item.setIstroop(0);
        item.setFrienduin(qq);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, null, item));


    }


    @Override
    public void sendRequestExitGroupMsg(IMsgModel item, String group) {

        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item, false)) {
            return;
        }
        item.setFrienduin(group);
        doPost(DelegateSendMsg.createDelegateMsg(ControlCode.QUIT_GROUP, null, item));


    }

    @Override
    public void sendRequestExitDiscussionMsg(IMsgModel item, String group) {

        if (item == null) {
            return;
        }
        item = item.clone();
        if (!checkParamSucc(item, false)) {
            return;
        }
        item.setFrienduin(group);
        doPost(DelegateSendMsg.createDelegateMsg(ControlCode.QUIT_DISCUSSION, null, item));

    }

    @Override
    public void sendGagMsg(IMsgModel item, long gagduration) {
        if (item == null) {
            return;
        }
        IMsgModel clone = item.clone();
        clone.setIstroop(1);
        clone.setFrienduin(item.getFrienduin());

        if (!checkParamSucc(clone)) {
            return;
        }


        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.GAG, gagduration, clone));


    }

    private boolean checkParamSucc(IMsgModel item) {
        return checkParamSucc(item, true);
    }

    private boolean checkParamSucc(IMsgModel item, boolean checkMsg) {
        if (item == null) {
            return false;
        }
        if (TextUtils.isEmpty(item.getSelfuin())) {
            return false;
        }
        if (TextUtils.isEmpty(item.getFrienduin())) {
            return false;
        }
        if (TextUtils.isEmpty(item.getSenderuin())) {
            return false;
        }

        if (checkMsg) {
            if (item.getMessage() == null) {
                return false;
            }

        }

        if (!RegexUtils.checkNoSignDigit(item.getSelfuin())) {
            return false;
        }
        if (!RegexUtils.checkNoSignDigit(item.getFrienduin())) {
            return false;
        }
        if (!RegexUtils.checkNoSignDigit(item.getSenderuin())) {
            return false;
        }
        return true;


    }

    @Override
    public void sendGagMsg(IMsgModel item, String group, String qq, long gagDuration) {
        if (item == null) {
            return;
        }
        IMsgModel clone = item.clone();
        clone.setIstroop(1);
        clone.setFrienduin(group);
        clone.setSenderuin(qq);


        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.GAG, gagDuration, clone));

    }

    @Override
    public void sendAtMsg(IMsgModel item, String group, String qq) {
        if (!checkParamSucc(item)) {
            return;
        }


        IMsgModel clone = item.clone();
//        clone.getFrienduin()
        clone.setFrienduin(group);
        clone.setSenderuin(qq);
        clone.setIstroop(1);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.AITE, clone));


    }

    @Override

    public void modifyGroupMemberCard(String group, String qq, String name) {

        MsgItem msgItem = new MsgItem();
        msgItem.setNickname("");
        msgItem.setCode(ControlCode.MODIFY_GROUP_MEMBER_CARD_NAME);
        msgItem.setSenderuin(qq);
        msgItem.setFrienduin(group);
        MsgReCallUtil.notifyRequestModifyName(RobotContentProvider.getInstance(), msgItem, name);

    }

    @Override
    public void zanPerson(String qq) {
        zanPerson(qq, 1);
    }

    @Override
    public void zanPerson(String qq, int count) {
        MsgItem msgItem = new MsgItem();
        msgItem.setNickname("");
        msgItem.setCode(ControlCode.ADD_LIKE);
        msgItem.setSenderuin(qq);
        msgItem.setFrienduin(qq);
        MsgReCallUtil.notifyZanPerson(RobotContentProvider.getInstance(), msgItem, qq, count);
    }

    @Override
    public JSONObject queryQQ(String qq) {
        Map<String, String> stringStringMap = RemoteService.queryQQCard(qq);
        if (stringStringMap != null) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                String key = stringStringEntry.getKey();
                try {
                    jsonObject.put(key, stringStringEntry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
        return null;
    }

    @Override
    public JSONObject queryQQGroup(String group) {

        Map<String, String> stringStringMap = RemoteService.queryGroupInfo(group);
        if (stringStringMap != null) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                String key = stringStringEntry.getKey();
                try {
                    jsonObject.put(key, stringStringEntry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
        return null;
    }

    @Override
    public String queryRobotNickName() {
        return null;
    }

    @Override
    public String queryRobotAccount() {
        return null;
    }

    @Override
    public JSONArray queryGroupsList() {
        return null;
    }

    @Override
    public String queryGroupField(String group, String field) {
        return null;
    }

    @Override
    public JSONArray queryGroupMemberList(String group) {
        return null;
    }

    @Override
    public JSONArray queryQQMemberList() {
        return null;
    }

    @Override
    public boolean revokeMsg(long id, Object arg) {
        return false;
    }

    @Override
    public boolean addQQGroupManager(String group, String qq) {
        return false;
    }

    @Override
    public boolean removeQQGroupManager(String group, String qq) {
        return false;
    }


    @Override
    public void shieldPerson(String qq) {

    }


    @Override
    public void modifyGroupName(String groupName) {

    }

    @Override
    public void modifyGroupNameInfo(JSONObject arg) {

    }

    @Override
    public void modifyQQInfo(JSONObject arg) {

    }


    @Override
    public void setGroupSpecialTitle(IMsgModel item, String group, String qq, String name) {

    }

    @Override
    public void setFriendAddRequest(IMsgModel item, String qq) {

    }

    @Override
    public void setGroupAddRequest(IMsgModel item, String qqgroup) {

    }


    @Override
    public void sendGroupVoiceMsg(IMsgModel item, String group, String qq, String voicePath) {

    }

    @Override
    public void sendVoiceMsg(IMsgModel item, String voicePath) {

    }

    @Override
    public void callVoice(IMsgModel iMsgModel, String s, String s1) {
        iMsgModel.setNickname(s1);
        MsgReCallUtil.notifySendVoiceCall(RobotContentProvider.getInstance(),s,iMsgModel);
    }

    @Override
    public void callVideo(IMsgModel iMsgModel, String s) {

    }

    @Override
    public void sendPrivateVoiceMsg(IMsgModel item, String group, String qq, String voicePath) {

    }


    @Override
    public void sendKickMsg(IMsgModel item, boolean forverKick) {
        if (!checkParamSucc(item)) {
            return;
        }

        IMsgModel clone = item.clone();
        clone.setIstroop(1);
        clone.setFrienduin(item.getSenderuin());
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.KICK, forverKick, clone));
    }

    @Override
    public void sendMsgCardMsg(IMsgModel item, String group, String qq, String xmlMsg) {
        if (!checkParamSucc(item)) {
            return;
        }

        IMsgModel clone = item.clone();
        clone.setIstroop(1);
        clone.setFrienduin(group);
        clone.setSenderuin(qq);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.CARDMSG, xmlMsg, clone));
    }

    @Override
    public void sendKickMsg(IMsgModel item, String group, String qq, boolean forverKick) {
        if (!checkParamSucc(item)) {
            return;
        }

        IMsgModel clone = item.clone();
        clone.setIstroop(1);
        clone.setFrienduin(group);
        clone.setSenderuin(qq);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.KICK, forverKick, clone));
    }

    @Override
    public boolean isGroupMsg(IMsgModel item) {
        return MsgTyeUtils.isGroupMsg(item);
    }

    @Override
    public boolean isPrivateMsg(IMsgModel item) {
        return MsgTyeUtils.isPrivateMsg(item);
    }

    @Override
    public boolean isPicMsg(IMsgModel item) {
        return MsgTyeUtils.isGroupPicMsg(item);
    }

    @Override
    public void sendPicMsg(IMsgModel item, String group, String qq, String picPath) {

        IMsgModel clone = item.clone();
//        clone.setIstroop(item);
        clone.setFrienduin(group);
        clone.setSenderuin(qq);
        clone.setMessage(picPath);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.SEND_PIC, picPath, clone));
    }

    @Override
    public void sendPicAndTextMsg(IMsgModel item, String group, String qq, String picPath, String message) {


           IMsgModel clone = item.clone();
//        clone.setIstroop(item);
        clone.setFrienduin(group);
        clone.setSenderuin(qq);
        clone.setCode(ControlCode.SEND_MIX_MSG);
        clone.setMessage(picPath);
        clone.setExtstr(message);
        doPost(DelegateSendMsg.createDelegateMsg(DelegateSendMsgType.DEFAULT, picPath, clone));
    }

    @Override
    public void sendUniversalMsg(IMsgModel item, String extra, int type) {

        IMsgModel clone = item.clone();
//        clone.setIstroop(item);
        clone.setMessage(extra);
        doPost(DelegateSendMsg.createDelegateMsg(type, extra, clone));
    }


    @Override
    public void sendAsyncGetRequest(String url, final IApiCallBack<byte[]> iCallBack) {
        sendAsyncGetRequest(url, null, iCallBack);

    }

    public void sendAsyncGetRequest(String url, HashMap<String, String> header, final IApiCallBack<byte[]> iCallBack) {

        HttpUtilOld.queryGetData(url, header, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (iCallBack != null) {
                    if (iCallBack != null) {
                        iCallBack.onFail(500, e);
                    }
                }

//cn.qssq666.robot.plugin.sdk.interfaces.IApiCallBack
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (iCallBack != null) {
//                        String str = response.body().string();
                    try {
                        byte[] bytes = response.body().bytes();
//                        new String(bytes)
                        iCallBack.onSucc(bytes);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        iCallBack.onFail(501, e instanceof Exception ? (Exception) e : new Exception(e));


                    }
                }

            }
        });

    }

    /*

    //获取要访问资源的byte数组
			byte[] arr = response.body().bytes();
			FileOutputStream fos = new FileOutputStream("d:/bd.jpg");
			fos.write(arr);
			fos.close();
			System.out.println("图片下载成功!!!!!");
     */


    @Override
    public void sendAsyncPostRequest(String url, final IApiCallBack<byte[]> iCallBack, HashMap<String, String> args) {
        sendAsyncPostRequest(url, iCallBack, null, args);
    }

    @Override
    public void sendAsyncPostRequest(String url, final IApiCallBack<byte[]> iCallBack, HashMap<String, String> header, HashMap<String, String> args) {

        FormBody.Builder builder = getBuilderFromArgsMap(args);


        RequestBody requestBody = builder.build();
        HttpUtilOld.queryPostData(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (iCallBack != null) {
                    if (iCallBack != null) {
                        iCallBack.onFail(500, e);
                    }
                }


            }

            @Override
            public void onResponse(Call call, Response response) {
                if (iCallBack != null) {
//                        String str = response.body().string();
                    try {
                        byte[] bytes = response.body().bytes();
                        iCallBack.onSucc(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                        iCallBack.onFail(501, e);


                    }
                    /*
                    	byte[] arr = response.body().bytes();
			FileOutputStream fos = new FileOutputStream("d:/bd.jpg");
			fos.write(arr);
			fos.close();
			System.out.print
                     */
                }

            }
        });
    }

    @NonNull
    private FormBody.Builder getBuilderFromArgsMap(HashMap<String, String> args) {
        FormBody.Builder builder = new FormBody.Builder();
        if (args != null) {
            for (Map.Entry<String, String> entry : args.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

        }
        return builder;
    }


    @Override
    public void sendAsyncPostJSONRequest(String url, final IApiCallBack<byte[]> iCallBack, HashMap<String, String> header, String jsonpostargm) {


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonpostargm);

        HttpUtilOld.queryPostData(url, header, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (iCallBack != null) {
                    if (iCallBack != null) {
                        iCallBack.onFail(500, e);
                    }
                }


            }

            @Override
            public void onResponse(Call call, Response response) {
                if (iCallBack != null) {
//                        String str = response.body().string();
                    try {
                        byte[] bytes = response.body().bytes();
                        iCallBack.onSucc(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                        iCallBack.onFail(501, e);


                    }
                    /*
                    	byte[] arr = response.body().bytes();
			FileOutputStream fos = new FileOutputStream("d:/bd.jpg");
			fos.write(arr);
			fos.close();
			System.out.print
                     */
                }

            }
        });

    }

    /**
     * 发送异步下载请求
     *
     * @param url
     * @param type
     * @param callback
     * @param args
     */
    @Deprecated
    @Override
    public void sendAnsyncDownloadUniversal(String url, int type, Handler.Callback callback, Object... args) {

    }

    /**
     * 发送下载异步请求
     *
     * @param url
     * @param savePath
     * @param iCallBack
     */

    public void sendAnsyncDownload(String url, final String savePath, final IApiCallBack<Boolean> iCallBack) {
        sendAnsyncDownload(url, savePath, null, iCallBack);

    }

    /**
     * 异步下载
     *
     * @param url
     * @param savePath
     * @param header
     * @param iCallBack
     */

    @Override
    public void sendAnsyncDownload(String url, final String savePath, HashMap<String, String> header, final IApiCallBack<Boolean> iCallBack) {


        sendAsyncGetRequest(url, header, new IApiCallBack<byte[]>() {
            @Override
            public void onSucc(byte[] bytes) {
                try {
                    File file = bytes2File(bytes, savePath);
                    iCallBack.onSucc(file.exists());
                } catch (IOException e) {
                    if (iCallBack != null) {
                        iCallBack.onFail(502, e);
                    }


                }

            }

            @Override
            public void onFail(int code, Exception e) {

                if (iCallBack != null) {
                    iCallBack.onFail(code, e);
                }

            }
        });

    }

    /**
     * 字节流写入文件
     *
     * @param bytes
     * @param savePath
     * @return
     * @throws IOException
     */
    public static File bytes2File(byte[] bytes, String savePath) throws IOException {
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(savePath);
        fos.write(bytes);
        fos.close();
        return file;
    }

    /**
     * 同步下载
     *
     * @param url
     * @param header
     */
    @Override
    public byte[] sendSyncRequest(String url, HashMap<String, String> header) throws IOException {
        Response request = HttpUtilOld.querySyncGetData(url, header);
//        String string = request.body().string();
        return request.body().bytes();
    }

    @Override
    public byte[] sendSyncDownRequest(String url, HashMap<String, String> header, String savePath) throws IOException {
        byte[] bytes = sendSyncRequest(url, header);
        File file = bytes2File(bytes, savePath);
        return bytes;
    }


    @Override
    public void sendSyncPostRequest(String url, HashMap<String, String> headers, HashMap<String, String> args) throws IOException {

        FormBody.Builder builder = getBuilderFromArgsMap(args);
        HttpUtilOld.querySyncPostData(url, headers, builder.build());
    }

   @Override
    public boolean writeConfig(String name, String defaultValue) {

        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putString(name, defaultValue).commit();

    }
    public boolean writeConfig(String name, Long defaultValue) {

        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putLong(name, defaultValue).commit();

    }

    @Override
    public boolean writeConfig(String name, int defaultValue) {
        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putInt(name, defaultValue).commit();
    }

    @Override
    public boolean writeConfig(String name, boolean defaultValue) {
        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putBoolean(name, defaultValue).commit();
    }

    @Override
    public boolean writeConfig(String name, float defaultValue) {
        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putFloat(name, defaultValue).commit();
    }

    @Override
    public boolean writeConfig(String name, long defaultValue) {
        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putLong(name, defaultValue).commit();
    }

    @Override
    public SharedPreferences.Editor writeConfig(String name, Set<String> defaultValue) {

        SharedPreferences.Editor edit = RobotContentProvider.getInstance().getSharedPreferences().edit();
        return edit.putStringSet(name, defaultValue);
    }

    @Override
    public String readStringConfig(String name, String defaultValue) {
        return RobotContentProvider.getInstance().getSharedPreferences().getString(name, defaultValue);
    }


    @Override
    public boolean readBooleanConfig(String name, boolean defaultValue) {
        return RobotContentProvider.getInstance().getSharedPreferences().getBoolean(name, defaultValue);

    }

    @Override
    public float readFloatConfig(String name, float defaultValue) {
        return RobotContentProvider.getInstance().getSharedPreferences().getFloat(name, defaultValue);
    }
//    @Override
    public long readLongConfig(String name, long defaultValue) {
        return RobotContentProvider.getInstance().getSharedPreferences().getLong(name, defaultValue);
    }

    /**
     * 暂未实现
     *
     * @param type
     * @param arg
     */

    @Override
    public void executeAction(int type, Object... arg) {

    }

    /**
     * 暂未实现 自己写吧
     *
     * @param sql
     */
    @Override
    public void executeSql(String sql) {

    }

    /**
     * 暂未实现 自己写吧
     *
     * @param shell
     */
    @Override
    public void executeShell(String shell) {

    }

    @Override
    public Set<String> readStringSetConfig(String name, Set<String> defaultValue) {

        return RobotContentProvider.getInstance().getSharedPreferences().getStringSet(name, defaultValue);
    }

    @Override
    public int readIntConfig(String name, int defaultValue) {
        return RobotContentProvider.getInstance().getSharedPreferences().getInt(name, defaultValue);
    }

    @Override
    public List<HashMap<String, String>> queryDataBySql(String sql) throws Exception {
        DBUtils.HashMapDBInfo hashMapDBInfo = RobotContentProvider.getDbUtils().queryAllSaveCollections(sql);
        List<LinkedHashMap<String, String>> list = hashMapDBInfo.getList();
        List list1 = list;
        return list1;
    }

    @Override
    public int executeBySql(String sql) throws Exception {
//	db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{person.name, person.age})
        RobotContentProvider.getInstance().getRobotDb().execSQL(sql);
        return 0;
    }

    @Override
    public void executeBySql(String sql, Object[] bindArgs) throws Exception {
//	db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{person.name, person.age})
        RobotContentProvider.getInstance().getRobotDb().execSQL(sql, bindArgs);
    }


    @Override
    public int executeScript(String name) {
        return -1;
    }

    @Override
    public int deleteScript(String name) {
        return -1;
    }

    @Override
    public int addScript(String name, String scriptContent) {
        return -1;
    }

    @Override
    public long addVar(String varname, String value) {


        VarBean varBean = DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).queryByColumn(VarBean.class, "name", varname);


        if (varBean != null) {
            return -2;
        }

        varBean.setName(varname);
        varBean.setValue(value);


        return DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).insert(varBean);
    }

    @Override
    public long deleteVar(String varname) {

        VarBean varBean = DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).queryByColumn(VarBean.class, "name", varname);


        if (varBean == null) {
            return -2;
        }


        return DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).deleteByColumn(VarBean.class, "name", varname);
    }

    @Override
    public String fetchVar(String varname) {
        VarBean varBean = DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).queryByColumn(VarBean.class, "name", varname);
        if (varBean == null) {
            return null;
        }
        return varBean.getValue();
    }

    @Override
    public long modifyVar(String varname, String value) {


        VarBean varBean = DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).queryByColumn(VarBean.class, "name", varname);


        if (varBean == null) {
            return -2;
        }

        varBean.setValue(value);

        return DBHelper.getVarTableUtil(RobotContentProvider.getDbUtils()).update(varBean);
    }

    @Override
    public boolean isRunAtMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    @Override
    public boolean post(Runnable callBack) {
        return RobotContentProvider.getInstance().getHandler().post(callBack);
    }

    @Override
    public boolean post(Runnable runnable, long delayMs) {
        return RobotContentProvider.getInstance().getHandler().postDelayed(runnable, delayMs);
    }

    @Override
    public void clearPost() {
        RobotContentProvider.getInstance().getHandler().removeCallbacksAndMessages(null);
    }

    @Override
    public Handler getHandler() {
        return RobotContentProvider.getInstance().getHandler();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return RobotContentProvider.getInstance().getSharedPreferences();
    }

    @Override
    public Context getContext() {
        return RobotContentProvider.getInstance().getProxyContext();
    }

    @Override
    public void showDebugToast(final String message) {
        if (isRunAtMainThread()) {
            Toast.makeText(RobotContentProvider.getInstance().getProxyContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            RobotContentProvider.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RobotContentProvider.getInstance().getProxyContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public String parseVar(IMsgModel iMsgModel, String var) {
        return VarCastUtil.parseStr(iMsgModel, RobotContentProvider.getDbUtils(), var);
    }

    @Override

    public int dp2px(int dp) {
        return DensityUtil.dip2px(getContext(), dp);
    }

    @Override
    public int sp2px(int dp) {
        return DensityUtil.sp2px(getContext(), dp);
    }

    @Override
    public long parseGagTime(String gagtime) {
        return ParseUtils.parseGagStr2Secound(gagtime);
    }


    @Override
    public String getArgsFrom(String[] args, int position) {
        return ParamParseUtil.getArgByArgArr(args, position);
    }


    @Override
    public boolean text2Pic(String text, int width, int height, int fontSize, int color, int bgColor, String savePath) {


        if (text.contains(" ") && width == 0) {

            boolean b = SearchPluginMainImpl.text2bitmapfile(text.split(" "), savePath);
            return b;
        } else if (text.contains("\n") && width == 0) {

            boolean b = SearchPluginMainImpl.text2bitmapfile(text.split("\n"), savePath);
            return b;
        } else {


        }


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(bgColor);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(DensityUtil.sp2px(getContext(), 10));
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.width();
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int y = height / 2;
        int x = width / 2;
        float centerLine = y + (rect.height() / 2);
        float baseLineY = centerLine + (fm.bottom - fm.top) / 2 - fm.bottom;
        canvas.drawText(text, width / 2, baseLineY, paint);


        return ImageUtil.saveBitmap(bitmap, savePath);
    }

    @Override
    public boolean text2Pic(String[] text, String savePath) {
        return SearchPluginMainImpl.text2bitmapfile(text, savePath);
    }

    @Override
    public boolean text2Pic(String[] text, String fontColor, String backgroundCOlor, String savePath) {
        return SearchPluginMainImpl.text2bitmapfile(text, fontColor, backgroundCOlor, savePath);
    }


    @Override
    public boolean bitmap2File(Bitmap bitmap, String savePath) {
        return ImageUtil.saveBitmap(bitmap, savePath);
    }

    @Override
    public File getBaseDir() {
        return RobotUtil.getBaseDir();
    }

    @Override
    public boolean isAsPluginLoad() {
        return RobotContentProvider.getInstance().isAsPluginLoad();
    }


    public static boolean saveBitmapToSDCardByJPG(String filepath, Bitmap bitmap) {
        return saveBitmapToSDCardBy(filepath, bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    public static boolean saveBitmapToSDCardBy(String filepath, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            bitmap.compress(compressFormat, 100, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 可以解决着色模式问题。
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2BitmapAny(Drawable drawable) {
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
