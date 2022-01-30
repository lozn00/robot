package cn.qssq666.robot;
import java.util.List;
import java.util.Map;
interface ICallBack {
    void actionPerformed (int actionId);
//基本数据类型默认为in  can be an out type, so you must declare it as in, out or inout. 'out String flag2' can only be an in parameter.
     void onReceiveMsg( int flag, int what,boolean flag1,   String flag2,in Map map,in List list);
     Map queryClientData(int flag,int waht,boolean flag1,String flag2,in Map map,in List list);
     String queryData(int flag,int what,in String[] arg);
     String queryDataArr(int flag,int what,in String[] arg,in int[] intarg);
  String queryDataByMap(int flag,int what,in String[] arg,in int[] intarg,in Map map);
     Map queryData2Map(int flag,int what,in String[] arg,in int[] intarg);
     List queryData2List(int flag,int what,in String[] arg,in int[] intarg,in boolean[] boolflag);
     boolean execBooleanAction(int flag,int what,in String[] arg,in Map arg1);

}