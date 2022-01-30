package cn.qssq666;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by luozheng on 2017/2/25.  qssq.space
 * <p>
 * static JNINativeMethod gMethods[] = {//绑定，注意，V,Z签名的返回值不能有分号“;” 以及 基本类型如数组数组不能有分号 不能 有分号 后面的是地址屌用地址
 * {"a1qssq",                  "(Ljava/lang/String;)Ljava/lang/String;",                                                        (void *) test},
 * {"a2 callStaticMethod",      "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;",  (void *) callStaticMethod},
 * {"a3 callStaticMultiMethod", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", (void *) callStaticMultiMethod},
 * {"a4 callObjectMethod",      "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;",  (void *) callObjectMethod},
 * {"a5 findClass",             "(Ljava/lang/String;)Ljava/lang/Class;",                                                         (void *) findClassz},
 * {"a5 encode",                "([I)V",                                                                                         (void *) edcodearr},
 * {"a7 decode",                "([I)Ljava/lang/String;",                                                                        (void *) b_1},
 * <p>
 * };
 */


//ignore_start

public class CoreLibrary0 {

    static {
        System.loadLibrary("qssqcore");
    }

    private static String PACKAGENAME = "";//com.tencent.mobileqq.zhengl.EncryptUtil


    public static boolean isCrash;
    private static boolean alreadyopen;
    public static String mErrorMsg;

    public native static void callParentMethodV(Object obj, Object methodName, Object parentClassName);

    public static boolean hasInitSucc() {
        return isCrash == false;
    }

    /**
     * TODO
     *
     * @param troopManager
     * @param methodName
     * @param returnType
     * @param paramType
     */
    public static void findMethod(Object troopManager, String methodName, String returnType, String paramType) {

    }

    //q666.EncryptUtilN.callPpp:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/Object;)V
    public static native void callPpp(Object baseClassloader, Object appinterface, Object context, Object sSelfuin, Object sFrienduin, Object sSenderuin, int sIsStropGroup, Object picPath);


    public static String pluginSoPath = null;
    public static String currentSoPath = null;

    private static final String TAG = "EncryptUtilN";

    public static native void shabiwatch(Object mPluginClassloader, Object baseApplicationImp,
                                         Object mTicketManager, Object mTroopGagMgr,
                                         Object mTroopManager, Object sbReq_text,
                                         Object frienduin, Object senderuin,
                                         Object selfuin, Object istroop);

    public static native String a1(String name);

    public static String qssq(String name) {
        return a1(name);
    }

    public static native Object a2(String className, String method, String signName, Object o);


    public static native Object a3(String className, String method, String signName, Object[] o);


    public static native Object a4(Object o, String method, String signName, Object o1);

    public static native Class a5(String className);

    public static native void a6(int[] arr);//([I)V

    public static native String getEStr(int random, String appendStr, Object classloader, Object context);

    //goout123.(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;JLjava/util/List;Z)V"
    public static native void goout123(Object qqinterface, Object mTroopManager, Object mTroopGagMgr, long group, Object list, boolean forver, int versionCode);

    /**
     * @param qqinterface   之后的2个参数是用来混淆概念实际上并没有用到，和踢人的方法不同，这里用的是 a方法，不过原理应该是一样的
     * @param mTroopManager
     * @param mTroopGagMgr
     * @param group
     */
    public static native void tuid(Object qqinterface, Object mTroopManager, Object mTroopGagMgr, long group);

    /**
     * 退群
     *
     * @param qqinterface
     * @param mTroopManager
     * @param mTroopGagMgr
     */
//        EncryptUtilN.goout123(mQQAppInterface, getTroopManager(), mTroopGagMgr, group,QQVersionConfig.mCurrentVersionCode);
    public static native void tuig(Object qqinterface, Object mTroopManager, Object mTroopGagMgr, String obj, int versionCode);

    //cn.qssq666.EncryptUtilN.mcd:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    public static native Object mcd(Object qqinterface, Object mTroopManager, Object classloader, String group, String qq, String remark, int versionCode);

    //addZan
    public static native String addZan(Object qqinterface, Object manager, Object classloader, Object arg1, String arg2, String arg3, int arg4, int versionCode);


    /**
     * 查询卡片用户信息
     *
     * @param qqinterface
     * @param manager     friendermanaer
     * @param classloader I接啊的
     * @param arg1        假的
     * @param qq
     * @param arg3        假的
     * @param
     * @param versionCode
     * @return
     */
    public static native Object queryC(Object qqinterface, Object manager, Object classloader, Object arg1, String qq, String arg3, int type, int versionCode);


    //    public static native void goout123(Object mTroopHandler, Object mTroopManager, Object mTroopGagMgr,long group,List  qqList,boolean forver);
    //Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;J)Z
    public static native boolean shutdown(Object o, String group, String qq, long duration);


    /**
     * tencent.com.cftutils
     *
     * @param random
     * @param appendStr
     * @param classloader
     * @param context
     * @return
     */

    public static native String getEStr1(int random, String appendStr, Object classloader, Object context);

    public static native String getDStr1(int random, String appendStr, Object classloader, Object context);


    public static native String getDStr(String random, String appendStr, Object classloader, Object context);

    public static native String a7(int[] arr);

    /**
     * xlistview id
     *
     * @param context
     * @return
     */
    public static native int getXid(Context context);

    /**
     * setting id
     *
     * @param context
     * @return
     */
    public static native int getSid(Context context);

    /**
     * 寻找id通过name
     * @param context
     * @param name
     * @return
     */
//    public static native int getIByN(Context context,String name);

    /**
     * 寻找viewid
     *
     * @param context
     * @param id
     * @return
     */
    public static native Object fd(Object context, int id);

    /**
     * 自定义寻找view id
     *
     * @param context
     * @param name
     * @return
     */
    public static native int getCustom(Context context, String name);

    /**
     * @param context
     * @param type    如 id, layout drawable
     * @param name
     * @return
     */
    public static native int getCustomFomType(Context context, String type, String name);


    //    @BWN
    public static native void a8(int i);


    //    @BWN
    public static native boolean a9(Context context);

    public static native void a10(int i);

    public static native boolean a11(Context context);

    /**
     * 加载插件classloader
     *
     * @param o            classloader的根源 通过它获取它的classloader
     * @param file         插件的目录
     * @param dataCacheDir 缓存目录
     * @return 返回classloader
     */
    public static native Object lbrb(Object o, String file, String dataCacheDir);

    /**
     * @param pluginInterface 插件接口 ，
     * @param parentView      parentview
     * @param layoutStr       布局名
     * @return 返回这个布局的view
     */
    public static native Object gV(Object classloaderFromObj, Object pluginInterface, Object parentView, String layoutStr);

    /**
     * 创建机器人的上下文.
     *
     * @param context
     * @return
     */
    public static native Object cxc(Object context);

    /**
     * 检查qq签名和破解签名方法，必须调用，否则将在1分钟后anr ,可以参考/data/amr
     */
    public static native void a12(Context context);

    public static native int a13(Context context);

    //Landroid.widget/TextView;[I)V
    public static native void a14(TextView textView, int[] chars);

    public static native void a15(TextView textView, CharSequence chars);

    public static native boolean a16(String s, String s1);

    public static native boolean a17(String s, String s1);

    public static native void a18(Object s, int[] temp);

    public static native void a19(TextView textView, CharSequence chars);

    //检查微信签名和破解签名
    public static native void a20(Context context);

    /**
     * 检查是否包含黑名单QQ
     *
     * @param s1
     * @return
     */
    public static native boolean a20(String s1);

    /**
     * 完成contextprovider 的初始化
     *
     * @param context
     * @param contentProvider
     */
    public native static Object cCAndEnsureResource(Context context, Object contentProvider, String path);

    public static native void findMethodFromJNI(Object troopManager, String methodName, String sign);


    public static native void hookLZ(Object sessionInfo, String senderUin, String friendUin, int isGroup);


    public static void encode(int[] arr) {
        a6(arr);
        ;
    }

    public static Object callStaticMethod(String className, String method, String signName, Object o) {
        return a2(className, method, signName, o);
    }

    public static void exitApp1() {
        a10(0);
    }

    public static Object callStaticMultiMethod(String className, String method, String signName, Object[] o) {
        return a3(className, method, signName, o);
    }

    public static int checkD(Context context) {
        return a13(context);
    }

    public static void setText(TextView textView, int[] chars) {
        a14(textView, chars);

    }


    //,jbyte flag,jshort startPosition,jshort textLen,jlong uin
    public static native Object callyou(Object o, byte flag, short startPosition, short textLen, long uin);

    //bject
/*    sendAtMsg(JNIEnv *env, jobject thiz, jobject classloader, jobject appinterface, jobject context,
              jstring content, jobject arrayList,jstring  frienduin,jint  istroop, jstring  senderuin,jbyte flag, jshort startPosition, jshort textLen,
              jlong uin*/
    public static native Object ainimama(Object classloader, Object appInterface, Object context, String content, Object list, String frienduin,


                                         int istroop, String senderuin);


//    public static native Object ainimama(Object classloader, Object appInterface, Object context, String content, Object list,String frienduin,int istroop,String senderuin,byte flag, short startPosition, short textLen, long uin);


    public static native void setfieldx(Object classObj, String methdName, Object obj, Object methodValue);


    public static native void bug(Object qqAppInterface, String frienduin, String senderuin, int istroop, String message, Object extra, Object classloader);

    public static native void destory();

    public static native void setObj(Object obj, String fieldName, String fieldClassNameSign, Object value);

    public static native boolean callUniversal(int type, String frienduin, String senderuin, int istroop, String message, ClassLoader classloader, Object qqinterface, Object mTroopManager, Object mTroopGagMgr, Object friendsManagerObject);

    public static native Object cnmObj(Object obj, String fieldName, String fieldClassNameSign);


    public static Object findObj(Object obj, String fieldName, String fieldClassNameSign) {
        return cnmObj(obj, fieldName, fieldClassNameSign);
    }


    public static boolean isContainCrackQQ(String qq) {
        return a20(qq);
    }

    public static Object createAtObject(ClassLoader classLoader, byte flag, short startPosition, short textLen, long uin) {
        return callyou(classLoader, flag, startPosition, textLen, uin);
    }


    public static void setVerifyTextStr(TextView textView, String chars) {
        a15(textView, chars);
    }

    public static void setNormalextStr(TextView textView, String chars) {
        a19(textView, chars);
    }

    public static boolean isContainsStr(String chars, String strs) {
        return a16(chars, chars);
    }

    public static boolean isContainsStr1(String chars, String strs) {
        return a17(chars, chars);
    }

    public static void setLeftTextStr(Object object, int[] leftTextStr) {
        a18(object, leftTextStr);
    }

    /**
     * @param obj            寻找的对象
     * @param fieldName      字段名
     * @param fieldTypeClass 字段签名class类型
     * @param value          设置的值
     */

    public static void setObj(Object obj, String fieldName, Class fieldTypeClass, Object value) {
        StringBuffer sb = getClassJniSign(fieldTypeClass.getName());
        setObj(obj, fieldName, sb.toString(), value);

    }

    public static Object findObjByClass(Object obj, String fieldName, Class classs) {
        String name = classs.getName();
        return findObjByClass(obj, fieldName, name);
    }

    /**
     * @param obj
     * @param fieldName
     * @param name      字段签名
     * @return
     */
    private static Object findObjByClass(Object obj, String fieldName, String name) {
        StringBuffer sb = getClassJniSign(name);
        return findObj(obj, fieldName, sb.toString());
    }

    private static StringBuffer getClassJniSign(String name) {
        String[] split = name.split("\\.");
        StringBuffer sb = new StringBuffer();
        sb.append("L");
        for (int i = 0; i < split.length; i++) {
            String current = split[i];
            sb.append(current);
            if (i != split.length - 1) {
                sb.append("/");

            }
        }

        sb.append(";");
        return sb;
    }


    public static String decode(int[] arr) {
        return a7(arr);
    }

    public static void exitApp() {

        a8(0);
    }


    //ignore_end

    //04-22 22:36:48.214 16447-16447/? E/dalvikvm: Could not find class 'com.tencent.mm.plugin.luckymoney.c.ab', referenced from method qqproguard.wechat.a.a
}
