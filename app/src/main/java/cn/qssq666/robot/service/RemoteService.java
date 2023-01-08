package cn.qssq666.robot.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.ICallBack;
import cn.qssq666.robot.RobotCallBinder;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.config.CmdConfig;
import cn.qssq666.robot.constants.ServiceExecCode;
import cn.qssq666.robot.remote.RemoteFlag;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.CookieLocalFilePool;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    private static final String CHANNEL_ONE_ID = "back_service";
    private Notification notification;

    public static String getProcessName() {
        if (!isIsInit()) {

            return "尚未连接";
        }
        return AppUtils.queryProcessNameByPid(AppContext.getContext(), Binder.getCallingPid());
    }

    public static String revokeMsg(String frienduin, String account, int revokeCount, long messageID) {
        return revokeMsg(frienduin,account,revokeCount+"",messageID);
    }
    public static String revokeMsg(String frienduin, String account, String revokeCountOrEmpty, long messageID) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryData(ServiceExecCode.REVOKE, (int) messageID, new String[]{account, frienduin,revokeCountOrEmpty+"",messageID+""});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
//        String sendqq, String group, String countStr
//        return QQEngine.doRevokeMsg(arg[0],arg[1],arg[2],Long.parseLong(arg[3]));
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onStartCommand()");
        Log.w(TAG, "service create");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w(TAG, "service start id=" + startId);
        callback(startId);
    }
    //DeadObjectException 表示绑定的服务以死了

    @Override
    public IBinder onBind(Intent t) {
        Log.w(TAG, "service on bind " + t.getAction() + "," + Thread.currentThread().getName());
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "service on destroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.w(TAG, "service on unbind");
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        Log.w(TAG, "service on rebind");
        super.onRebind(intent);
    }

    void callback(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).actionPerformed(val);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
    }

    private static ICallBack mCallback;
    public static final RobotCallBinder.Stub mBinder = new RobotCallBinder.Stub() {

        public void stopRunningTask() {

        }

        @Override
        public String queryConfig1(int flag, int what, String[] arg) throws RemoteException {
            return null;
        }

        @Override
        public String queryConfig2(int flag, int what, String[] arg) throws RemoteException {
            return null;
        }

        @Override
        public String queryConfig3(int flag, int what, String[] arg) throws RemoteException {
            return null;
        }

        @Override
        public boolean queryEnable1(int flag, int what, String[] arg) throws RemoteException {
            return false;
        }

        @Override
        public boolean queryEnable2(int flag, int what, String[] arg) throws RemoteException {
            return false;
        }

        @Override
        public boolean queryEnable3(int flag, int what, String[] arg) throws RemoteException {
            return false;
        }

        @Override
        public List queryData(int action, boolean flag1, String flag2) throws RemoteException {
            Log.w(TAG, "queryData" + action + ",flag:" + flag1 + "," + flag2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(action);
            return arrayList;
        }

        @Override
        public List queryDataStr(int action, boolean flag1, String[] flag) throws RemoteException {
            if (action == RemoteFlag.FLAG_QUERY_COOKIE) {
                List list = new ArrayList();
                String cookie = CookieLocalFilePool.getCookie(flag[0], flag[1]);
                list.add(cookie);
                return list;
            }
            return null;
        }

        @Override
        public Map queryMapData(int action, boolean flag1, String flag2) throws RemoteException {

            Log.w(TAG, "queryMapData" + action + ",flag:" + flag1 + "," + flag2);
            HashMap<String, String> map = new HashMap<>();
            map.put("action", action + "");
            map.put("flag1", flag1 + "");
            map.put("flag2", flag2 + "");
            return map;
        }

        @Override
        public String queryDataByMap(int flag, int what, String[] arg, int[] intarg, Map map) throws RemoteException {
            return null;
        }

        @Override
        public Map queryData2Map(int flag, int what, String[] arg, int[] intarg) throws RemoteException {
            return null;
        }

        @Override
        public List queryData2List(int flag, int what, String[] arg, int[] intarg, boolean[] boolflag) throws RemoteException {
            return null;
        }

        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public int getVersionCode() throws RemoteException {
            return BuildConfig.VERSION_CODE;
        }

        @Override
        public String getVersionName() throws RemoteException {
            return BuildConfig.VERSION_NAME;
        }

        @Override
        public String getLoginUser() throws RemoteException {
            return "默认账号";
        }

        @Override
        public String getLoginToken() throws RemoteException {
            return "已停用服务器";
        }

        @Override
        public boolean isAuthorUser() throws RemoteException {
            return true;
        }

        @Override
        public boolean isLogin() throws RemoteException {
            return true;
        }

        @Override
        public String getMenuStr() throws RemoteException {
            return CmdConfig.printSupportCmd();
        }

        @Override
        public String getHostInfo() throws RemoteException {
            return RobotContentProvider.getInstance() + "";
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.w(TAG, "basicTypes" + anInt + ",aLong:" + aLong + ",aBoolean " + aBoolean + ",aFloat:" + aFloat + ",aDouble:" + aDouble + ",aString:" + aString);
        }

        public boolean isTaskRunning() {
            Log.w(TAG, "isTaskRunning");
            return false;
        }

        @Override
        public void clearCallBack() {
            mCallback = null;
            int count = mCallbacks.beginBroadcast();
            mCallbacks.kill();
            mCallbacks.finishBroadcast();
        }

        @Override
        public void registerCallback(ICallBack cb) {

            mCallback = cb;
            if (cb != null) {
                mCallbacks.register(cb);
                Log.w(TAG, "注册了回调 " + cb.getClass().getName() + "," + Thread.currentThread() + ",");
            }
        }

        @Override
        public void unregisterCallback(ICallBack cb) {
            mCallback = null;
            if (cb != null) {
                mCallbacks.unregister(cb);
                Log.w(TAG, "取消了回调注册" + cb.getClass().getName());
            }
        }
    };


    public static void beginBroadcast() {
        mCallbacks.beginBroadcast();
    }

    public static void finishBroadcast() {
        mCallbacks.finishBroadcast();
    }

    public static ICallBack getClientICallBack() {
        int registeredCallbackCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            registeredCallbackCount = mCallbacks.getRegisteredCallbackCount();
        } else {
            return null;
        }
        if (registeredCallbackCount <= 0) {
            return null;
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "CallbackCount:" + registeredCallbackCount);

            }
            if (mCallback != null) {
                return mCallback;
            }
//            mCallbacks.beginBroadcast();
            ICallBack broadcastItem = mCallbacks.getBroadcastItem(registeredCallbackCount - 1);
//            mCallbacks.finishBroadcast();
            ;
            return broadcastItem;

        }

    }

    public static boolean isIsInit() {
        return mCallback != null;
    }

    public static final RemoteCallbackList<ICallBack> mCallbacks = new RemoteCallbackList<ICallBack>();

    public static String queryNickname(String qq, String group, int istroop) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryData(ServiceExecCode.QUERY_NICKNAME, istroop, new String[]{qq, group});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String queryGroupName(String group) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryData(ServiceExecCode.QUERY_GROUP_NAME, 0, new String[]{group});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String queryGroupField(String group, String fieldName) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryData(ServiceExecCode.QUERY_GROUP_INFO_FIELD, 0, new String[]{group, fieldName});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询登陆信息
     * QUERY_LOGIN_INFO
     *
     * @return
     */
    public static Map<String, String> queryLoginInfo(boolean onlyqueryQ) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            Map map = clientICallBack.queryClientData(ServiceExecCode.QUERY_LOGIN_INFO, onlyqueryQ ? 1 : 0, false, null, null, null);
            return map;
        } catch (RemoteException e) {

            e.printStackTrace();
        }

        return null;


    }

    public static Map<String, String> queryQQCard(String senderuin) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack != null) {
                Map map = clientICallBack.queryClientData(ServiceExecCode.QUERY_USER_INFO, 1, false, senderuin, null, null);
                return map;
            }

            return null;
        } catch (RemoteException e) {

            e.printStackTrace();
        }

        return null;
    }

    public static String queryLoginQQ() {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String result = clientICallBack.queryData(ServiceExecCode.QUERY_CURRENT_LOGIN_QQ, 0, null);
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String queryLoginNickname() {
        try {
//            beginBroadcast();
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String result = clientICallBack.queryData(ServiceExecCode.QUERY_CURRENT_LOGIN_NICKNAME, 0, null);
            return result;

        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        } finally {
//            finishBroadcast();
        }
    }

    public static String addLike(int count, String[] strings) {
        try {

            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryDataArr(ServiceExecCode.ADD_VOTE, count, strings, new int[]{1});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map queryGroupInfo(String group) {
        try {

            return RemoteService.getClientICallBack().queryClientData(ServiceExecCode.QUERY_GROUP_INFO, 1, false, group, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * (String group, String qq, long gagtime) {
     * @param group
     * @param account
     * @param gagDuration
     * @return
     */
    public static String gagUser(String group, String account, long gagDuration) {
        try {
            ICallBack clientICallBack = RemoteService.getClientICallBack();
            if (clientICallBack == null) {
                return null;
            }
            String s = clientICallBack.queryData(ServiceExecCode.CMD_GAG_USER, (int) gagDuration, new String[]{group, account});
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}
