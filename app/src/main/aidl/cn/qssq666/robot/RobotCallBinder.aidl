// RobotCall.aidl
package cn.qssq666.robot;
import cn.qssq666.robot.ICallback;

// Declare any non-default types here with import statements

interface RobotCallBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
      void clearCallBack( );
    void registerCallback(in ICallBack cb);
    void unregisterCallback( in ICallBack cb);
     boolean isTaskRunning();
      void stopRunningTask();
        String queryConfig1(int flag,int what,in String[] arg);
        String queryConfig2(int flag,int what,in String[] arg);
        String queryConfig3(int flag,int what,in String[] arg);
        boolean queryEnable1(int flag,int what,in String[] arg);
        boolean queryEnable2(int flag,int what,in String[] arg);
        boolean queryEnable3(int flag,int what,in String[] arg);
        List queryData(int action,boolean flag1,String flag2);
        List queryDataStr(int action,boolean flag1,in String[] flag);
        Map queryMapData(int action,boolean flag1,String flag2);
        String queryDataByMap(int flag,int what,in String[] arg,in int[] intarg,in Map map);
        Map queryData2Map(int flag,int what,in String[] arg,in int[] intarg);
        List queryData2List(int flag,int what,in String[] arg,in int[] intarg,in boolean[] boolflag);

        int getPid();
        int getVersionCode();
        String getVersionName();
        String getLoginUser();
        String getLoginToken();
        boolean isAuthorUser();
        boolean isLogin();
        String getMenuStr();
        String getHostInfo();

}
