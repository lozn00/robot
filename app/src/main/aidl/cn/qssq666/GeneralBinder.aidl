// RobotCall.aidl
package cn.qssq666;
import cn.qssq666.robot.ICallback;


interface GeneralBinder {
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
      void clearCallBack( );
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
        int getInt();
        int getIntVargs(int arg,int arg2,int arg3,int arg4);
        String getStr();
        String getStrVars(String arg,String arg1,String arg2,String Arg3);


        void setIntVargs(int arg,int arg2,int arg3,int arg4);
        String setStrVars(String arg,String arg1,String arg2,String Arg3);



}
