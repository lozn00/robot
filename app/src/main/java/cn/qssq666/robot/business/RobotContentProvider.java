package cn.qssq666.robot.business;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.AdminBean;
import cn.qssq666.robot.bean.AtBean;
import cn.qssq666.robot.bean.DoWhileMsg;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.GroupAdaminBean;
import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.bean.RedPacketBeanFromServer;
import cn.qssq666.robot.bean.RedpacketBaseInfo;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.bean.RequestBean;
import cn.qssq666.robot.bean.ResultBean;
import cn.qssq666.robot.bean.TwoBean;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.bean.ViolationWordRecordBean;
import cn.qssq666.robot.business.module.TranslateQueryImpl;
import cn.qssq666.robot.config.CmdConfig;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.config.MemoryIGnoreConfig;
import cn.qssq666.robot.config.MiscConfig;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.constants.CardHelper;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.IPluginRequestCall;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.constants.ServiceExecCode;
import cn.qssq666.robot.constants.TuLingType;
import cn.qssq666.robot.constants.UpdateLog;
import cn.qssq666.robot.enums.GAGTYPE;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.event.BaseSettignEvent;
import cn.qssq666.robot.event.DelegateSendMsg;
import cn.qssq666.robot.event.ForbitUseAdvanceEvent;
import cn.qssq666.robot.event.ForceEvent;
import cn.qssq666.robot.event.GroupConfigEvent;
import cn.qssq666.robot.event.OnUpdateAccountListEvent;
import cn.qssq666.robot.event.WordEvent;
import cn.qssq666.robot.http.HttpUtilRetrofit;
import cn.qssq666.robot.http.api.MoLiAPI;
import cn.qssq666.robot.http.api.TuLingAPI;
import cn.qssq666.robot.http.newcache.HttpUtil;
import cn.qssq666.robot.http.newcache.MyCookieManager;
import cn.qssq666.robot.interfaces.DelegateSendMsgType;
import cn.qssq666.robot.interfaces.ICmdIntercept;
import cn.qssq666.robot.interfaces.IIntercept;
import cn.qssq666.robot.interfaces.INeedReplayLevel;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.interfaces.OnCareteNotify;
import cn.qssq666.robot.interfaces.RedPacketMessageType;
import cn.qssq666.robot.interfaces.RequestListener;
import cn.qssq666.robot.misc.SQLCns;
import cn.qssq666.robot.openai.OpenAIBiz;
import cn.qssq666.robot.plugin.PluginUtils;
import cn.qssq666.robot.plugin.js.util.JSPluginUtil;
import cn.qssq666.robot.plugin.lua.util.LuaPluginUtil;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IGroupConfig;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.sdk.myimpl.ConfigQueryImpl;
import cn.qssq666.robot.plugin.sdk.myimpl.PluginControlmpl;
import cn.qssq666.robot.plugin.util.QueryPluginModel;
import cn.qssq666.robot.receiver.CodeUpdateReceiver;
import cn.qssq666.robot.selfplugin.IHostControlApi;
import cn.qssq666.robot.selfplugin.IPluginHolder;
import cn.qssq666.robot.selfplugin.IRobotContentProvider;
import cn.qssq666.robot.service.DaemonService;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.AccountUtil;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.BatchUtil;
import cn.qssq666.robot.utils.CheckUtils;
import cn.qssq666.robot.utils.ClearUtil;
import cn.qssq666.robot.utils.ConfigUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.EncryptPassUtil;
import cn.qssq666.robot.utils.ErrorHelper;
import cn.qssq666.robot.utils.HttpUtilOld;
import cn.qssq666.robot.utils.InitUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.NetQuery;
import cn.qssq666.robot.utils.NickNameUtils;
import cn.qssq666.robot.utils.PairFix;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.RXUtil;
import cn.qssq666.robot.utils.RegexUtils;
import cn.qssq666.robot.utils.ProxySendAlertUtil;
import cn.qssq666.robot.utils.RobotFormatUtil;
import cn.qssq666.robot.utils.RobotUtil;
import cn.qssq666.robot.utils.SPUtils;
import cn.qssq666.robot.utils.ShellUtil;
import cn.qssq666.robot.utils.StringUtils;
import cn.qssq666.robot.utils.VarCastUtil;
import cn.qssq666.robot.utils.ZxingUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Callback;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static cn.qssq666.robot.utils.DateUtils.TYPE_MS;
import static cn.qssq666.robot.utils.DateUtils.TYPE_SECOND;
import static cn.qssq666.robot.utils.DateUtils.getTimeDistance;

/**
 * Created by luozheng on 2017/2/12.  qssq.space
 */

public class RobotContentProvider extends ContentProvider implements IRobotContentProvider {

    //IGNORE_START
    public static final boolean ENABLE_LOG = BuildConfig.DEBUG;
    private static RobotContentProvider instance;
    private String mLastError;
    private ClassLoader mProxyClassloader;
    public boolean mAllowPluginInterceptEndMsg = false;
    public boolean mAllowReponseSelfCommand = true;
    public IHostControlApi mHostControlApi;
    private boolean mUseChildThread = false;
    public int defaultReplyIndex;
    private MsgItem mItem;
    //    private boolean mCfBaseEnableOutProgramVoiceAlert;
//    private String mCfBaseEnableOutProgramVoiceKeyword;
    public MiscConfig _miscConfig = new MiscConfig();


    public PluginControlInterface getPluginControlInterface() {
        return pluginControlInterface;
    }

    private PluginControlInterface pluginControlInterface;

    public ConfigQueryImpl getConfigQueryImpl() {
        return configQueryImpl;
    }

    private ConfigQueryImpl configQueryImpl;

    public static RobotContentProvider getInstance() {
        return instance;
    }

    private String mRobotQQ = null;
    private SharedPreferences sharedPreferences;

    private static final String TAG = "RobotContentProvider";//
    public final static String ACTION_MSG = "insert/msg";//表示这目录下面所有
    public final static String ACTION_GAD = "insert/gad";//表示这目录下面所有
    private String mShortUrlTextApiUrl = "http://suo.im/api.php?url=";
    public final static String ACTION_KICK = "insert/kick";
    public final static String ACTION_UPDATE_KEY = "update/key";
    public final static String ACTION_UPDATE_MISC_CONFIG = "update/miscconfig";
    private static UriMatcher _uriMatcher;
    public String robotReplyKey = "";
    public String robotReplySecret = "";
    public String robotReplyRequestMark = "";
    private static final int CODE_MSG = 1;
    private static final int CODE_GAD = 2;
    private static final int CODE_TICK = 3;
    private static final int CODE_UPDATE_KEY = 4;
    private static final int CODE_UPDATE_MISC = 5;

    public boolean mCfeanbleGroupReply = true;
    public final boolean mCfOnlyReplyWhiteNameGroup = true;
    public boolean mCfNotReplyGroup = true;
    public String mCfOneReplyOneGroupStr = "";
    public String mCfNotReplyGroupStr = "";
    public boolean mCfBaseReplyShowNickName;
    public boolean mCFBaseEnablePlugin = true;
    @Deprecated
    public boolean mCfBaseReplyNeedAite;
    public boolean mCfBaseWhiteNameReplyNotNeedAite;
    public boolean mCfNotWhiteNameReplyIfAite;
    public boolean mCfprivateReply;
    public String mLocalRobotName;
    public String mCfBaseNoReplyNameStr;
    public static DBUtils _dbUtils;
    HashMap<String, String> mKeyWordMap = new HashMap<>();
    public List<GroupWhiteNameBean> mQQGroupWhiteNames = new ArrayList<>();
    public List<AccountBean> mIgnoreQQs = new ArrayList<>();
    public List<AccountBean> mIgnoreGagQQs = new ArrayList<>();
    public List<AdminBean> mSuperManagers = new ArrayList<>();
    public List<GagAccountBean> mGagKeyWords = new ArrayList<>();
    public boolean mCfBaseEnableNetRobotPrivate;
    public boolean mCfBaseEnableNetRobotGroup;
    public String mCfGroupJoinGroupReplyStr;
    public static int musicType;
    public static long mStatupTime;
    public boolean mForceUpdate;
    public boolean mStopUseAdvanceFunc;
    public boolean mCfBaseDisableAtFunction;
    public boolean mCfBaseDisableGag;
    public boolean mCfBaseNetReplyErrorNotWarn;
    public List<IPluginHolder> mPluginList = new ArrayList<>();
    public List<IPluginHolder> mLuaPluginList = new ArrayList<>();
    public List<IPluginHolder> mJSPluginList = new ArrayList<>();
    public boolean mCfBaseDisableStructMsg;
    public boolean mCfBaseEnableLocalWord;
    public boolean mCFBaseEnableCheckKeyWapGag = true;
    public static long mInsertTime;
    public String mReplyPostFix = "";
    /**
     * 管如果是 管理理员则无视规则.
     */
    private boolean mCfprivateReplyManagrIgnoreRule;
    private boolean interceptNotifyChanage;
    public static String mPackageName;
    private Context mPakcageContext;

    //IGNORE_END
    public static void setLastError(Throwable lastError) {
        RobotContentProvider.getInstance().mLastError = Log.getStackTraceString(lastError);
    }

    public void setProxyResources(Context context, Resources resources) {
        try {
            mPakcageContext = context.createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY);

//            mPakcageContext = (Context) EncryptUtilN.cxc(context);
            this.mProxyResources = mPakcageContext.getResources();
            LogUtil.writeLog(TAG, "set proxy from createPackageContext " + mProxyResources.getString(R.string.test_read));

        } catch (Throwable e) {
            LogUtil.writeLog(TAG, "may cannot sync modify info .c fuck error 228");
            this.mProxyResources = resources;
            e.printStackTrace();
        }
    }

    @Override
    public void testApi() {
        MsgReCallUtil.notifyTest(this, false, "测试私聊消息");
        MsgReCallUtil.notifyTest(this, true, "测试群消息");
    }

    @Override
    public List<IPluginHolder> getPluginList() {
        return mPluginList;
    }

    public List<IPluginHolder> getLuaPluginList() {
        return mLuaPluginList;
    }

    public List<IPluginHolder> getJSPluginList() {
        return mJSPluginList;
    }

    private Resources mProxyResources;

    public static DBUtils getDbUtils() {
        return _dbUtils;
    }

    @Subscribe
    public void onReceveWordData(WordEvent event) {
        if (event.isEdit()) {
            initWordMap();
        } else {
            doInsertNewKeyBean(event.getBean());
        }
    }

    public void initWordMap() {
        List<ReplyWordBean> list = DBHelper.getKeyWordDBUtil(_dbUtils).queryAll(ReplyWordBean.class);
        initKeyMap(list);
    }

    /**
     * 当收到了群白名单 或者QQ忽略列表的适合
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceveAccountListUpdateEvent(OnUpdateAccountListEvent event) {
        if (event.getType() == AccountType.TYPE_QQ_INOGRE_NAME) {
            mIgnoreQQs.clear();
            mIgnoreQQs.addAll(event.getList());
        }
        if (event.getType() == AccountType.TYPE_QQ_INOGRE_NAME_GAG) {
            mIgnoreGagQQs.clear();
            mIgnoreGagQQs.addAll(event.getList());
        } else if (event.getType() == AccountType.TYPE_QQGROUP_WHITE_NAME) {
            mQQGroupWhiteNames.clear();
            List listNew = event.getList();
            mQQGroupWhiteNames.addAll(listNew);
        }

    }

    @Subscribe
    public void onReceiveDelegateSendMsg(DelegateSendMsg msg) {

        if (BuildConfig.DEBUG) {
            LogUtil.writeLog(TAG, "收到代理消息" + msg);
        }
        if (msg.getType() == DelegateSendMsgType.KICK) {
            MsgReCallUtil.notifyKickPersonMsgNoJump(this, msg.getMsgItem(), (Boolean) msg.getObject());
        }
        if (msg.getType() == DelegateSendMsgType.CALL) {
            MsgReCallUtil.notifySendVoiceCall(this, msg.getMsgItem().getSenderuin(), msg.getMsgItem());
        } else if (msg.getType() == DelegateSendMsgType.GAG) {
            MsgReCallUtil.notifyGadPersonMsgNoJump(this, (Long) msg.getObject(), msg.getMsgItem());
        } else if (msg.getType() == DelegateSendMsgType.GROUP) {
            MsgReCallUtil.notifyJoinMsgNoJump(this, msg.getMsgItem(), true);
        } else if (msg.getType() == DelegateSendMsgType.PRIVATE) {
            MsgReCallUtil.notifyJoinMsgNoJump(this, msg.getMsgItem(), true);
        } else if (msg.getType() == DelegateSendMsgType.DEFAULT) {
            MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(this, msg.getMsgItem(), true);
        } else if (msg.getType() == DelegateSendMsgType.AITE) {
            MsgReCallUtil.notifyAtMsgJump(this, msg.getMsgItem().getSenderuin(), msg.getMsgItem().getNickname(), msg.getMsgItem().getMessage(), msg.getMsgItem(), true);
        } else if (msg.getType() == DelegateSendMsgType.SEND_PIC) {
            MsgReCallUtil.notifySendPicMsg(this, (String) msg.getObject(), msg.getMsgItem());
        } else {
            MsgReCallUtil.notifyUniversalMsg(this, msg.getType(), (String) msg.getObject(), msg.getMsgItem(), true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceveAccountDataInsertEvent(AccountAddOrChangeEvent event) {//不管是不是编辑模式 都 插入。
        LogUtil.writeLog("收到更新请求,类型:" + event.getType());
        if (event.getType() == AccountType.TYPE_QQGROUP_WHITE_NAME) {
            initGroupWhiteNamesFromDb();

        } else if (event.getType() == AccountType.TYPE_QQ_INOGRE_NAME) {
            List<AccountBean> list = DBHelper.getIgnoreQQDBUtil(_dbUtils).queryAll(AccountBean.class);
            iniQQIgnoresMap(list);
        } else if (event.getType() == AccountType.TYPE_QQ_INOGRE_NAME_GAG) {
            List<AccountBean> list = DBHelper.getIgnoreGagDBUtil(_dbUtils).queryAll(AccountBean.class);
            iniQQIgnoresGagMap(list);
        } else if (event.getType() == AccountType.TYPE_SUPER_MANAGER) {
            List<AdminBean> list = DBHelper.getSuperManager(_dbUtils).queryAll(AdminBean.class);
            initSuperManager(list);
        } else if (event.getType() == AccountType.TYPE_GAG) {
            List<GagAccountBean> list = DBHelper.getGagKeyWord(_dbUtils).queryAll(GagAccountBean.class);
            initGagWords(list);
        } else if (event.getType() == AccountType.TYPE_PLUGIN) {
            initJAVAPlugin();
            initLuaPlugin();
            initJavascriptSPlugin();
        } else if (event.getType() == AccountType.TYPE_VAR_MANAGER) {
            //不需要更新 因为 只有部分地方用到了。
        } else if (event.getType() == AccountType.TYPE_GROUP_ADMIN) {
            //不需要更新 因为 只有部分地方用到了。
        }
    }

    public void initGroupWhiteNamesFromDb() {
        List<GroupWhiteNameBean> list = DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).queryAll(GroupWhiteNameBean.class);
        iniQQGroupWhiteMap(list);
    }


    @Override
    public boolean onCreate() {
        doOnCreate();


        if (!isAsPluginLoad()) {
            Intent intent = new Intent(getContext(), RemoteService.class);
            getContext().startService(intent);

            try {
                DaemonService.startup(getContext());

            } catch (Throwable e) {
                Log.e(TAG, "multi_dex_error", e);
                //多dex,在其它子线程会挂。直接try,让他能够允许
            }

        }
        return true;
    }

    private void doOnCreate() {
        LogUtil.writeLog(TAG, "QSSQ Robot init VERSION CODE:" + BuildConfig.VERSION_CODE + ",as plugin load:" + isAsPluginLoad());
        _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        _uriMatcher.addURI(Cns.AUTHORITY, ACTION_MSG, CODE_MSG);
        _uriMatcher.addURI(Cns.AUTHORITY, ACTION_GAD, CODE_GAD);
        _uriMatcher.addURI(Cns.AUTHORITY, ACTION_KICK, CODE_TICK);
        _uriMatcher.addURI(Cns.AUTHORITY, ACTION_UPDATE_KEY, CODE_UPDATE_KEY);
        _uriMatcher.addURI(Cns.AUTHORITY, ACTION_UPDATE_MISC_CONFIG, CODE_UPDATE_MISC);
        LogUtil.importPackage();
        instance = RobotContentProvider.this;
        mPackageName = getProxyContext().getPackageName();
        long currentTime = new Date().getTime();
        mStatupTime = currentTime;
//        mStatupTime = SPUtils.getValue(getProxyContext(), AppConstants.CONFIG_STARTUPTIME, currentTime);
//        SPUtils.getValue(getProxyContext(), AppConstants.CONFIG_STARTUPTIME, currentTime);//这个逻辑代表下次启动读取的是这次启动的间隔时间。
        if (getProxyContext().getPackageName().equals(BuildConfig.APPLICATION_ID)) {
          /*  if(EventBus.getDefault().hasSubscriberForEvent(this)){sha

            }*/
            EventBus.getDefault().register(this);
        } else {
            AppContext.mStartupTime = System.currentTimeMillis();
        }
        if (!isAsPluginLoad()) {
            getContext().registerReceiver(new CodeUpdateReceiver(), new IntentFilter(Cns.UPDATE_CODE_BROADCAST));
//            System.loadLibrary("sqlite3core");//删除签名检测
        }
        OpenAIBiz.init();
        loadData();
    }

    //TODO 夜神模拟器5.1机制机器人出现错误。A/libc: Fatal signal 11 (SIGSEGV), code 1, fault addr 0x2829af in tid 5547 (thread_sp_Async)
    @Override
    public void reload() {
        loadData();
    }

    private void loadData() {

        if (isAsPluginLoad()) {
            try {
//                mPakcageContext = (Context) EncryptUtilN.cxc(getProxyContext());
                mPakcageContext = getProxyContext().createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (_dbUtils != null) {
            _dbUtils.close();

        }
        pluginControlInterface = PluginControlmpl.getInstance();
        configQueryImpl = new ConfigQueryImpl();
        _dbUtils = new DBUtils(getProxyContext());
        _dbUtils.setGetDeclared(true);
        InitUtils.initTableAndInsertDefaultValue(_dbUtils);
        LogUtil.writeLog("Robot服务OnCreate");//比appcontext早调用
        initKeyMap(DBHelper.getKeyWordDBUtil(_dbUtils).queryAll(ReplyWordBean.class));
        initGroupWhiteNamesDb();
        initIgnores();
        initSuperManager();
        initGagWords();
        initConfig();
        initJavascriptSPlugin();
        initJAVAPlugin();
        initLuaPlugin();

        String packageName = getProxyContext().getPackageName();
        NickNameUtils.initNicknames(_dbUtils);
        if (packageName.equals(BuildConfig.APPLICATION_ID)) {
            LogUtil.writeLog(TAG, packageName + "非插件化加载");
        } else {

            LogUtil.writeLog(TAG, packageName + "插件化加载");
        }
    }


    public void initGroupWhiteNamesDb() {
        iniQQGroupWhiteMap(DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).queryAll(GroupWhiteNameBean.class));
    }

    public void initIgnores() {
        iniQQIgnoresMap(DBHelper.getIgnoreQQDBUtil(_dbUtils).queryAll(AccountBean.class));
    }

    public void initSuperManager() {
        initSuperManager(DBHelper.getSuperManager(_dbUtils).queryAll(AdminBean.class));
    }

    public void initGagWords() {
        initGagWords(DBHelper.getGagKeyWord(_dbUtils).queryAll(GagAccountBean.class));
    }

    public void initLuaPlugin() {
        initLuaPlugin(null);
    }

    public void initLuaPlugin(final INotify iNotify) {
        ConfigQueryImpl.robotContentProvider = this;
        synchronized (PluginUtils.class) {
            if (mLuaPluginList != null) {
                for (IPluginHolder model : mLuaPluginList) {
                    try {
                        model.getPluginInterface().onDestory();

                        if (BuildConfig.DEBUG) {

                            LogUtil.writeLog(TAG, "卸载插件文件成功" + model.getPath());
                        }
                    } catch (Throwable e) {
                        LogUtil.writeLog(TAG, "卸载插件文件错误" + e.getMessage());

                    }
                }
            }
        }
        synchronized (LuaPluginUtil.class) {
            mLuaPluginList.clear();

        }

        new QssqTask<ArrayList<QueryPluginModel>>(new QssqTask.ICallBack<List<QueryPluginModel>>() {
            @Override
            public List<QueryPluginModel> onRunBackgroundThread() {
                List<QueryPluginModel> queryPluginModels = LuaPluginUtil.loadPlugin(getProxyContext(), getProxyClassloader(), new OnCareteNotify() {
                    @Override
                    public boolean onEach(PluginInterface pluginInterface) {
                        pluginInterface.onReceiveControlApi(pluginControlInterface);
                        pluginInterface.onReceiveRobotConfig(configQueryImpl);
                        pluginInterface.onCreate(getProxyContext());
                        return false;
                    }
                });
                return queryPluginModels;
            }

            @Override
            public void onRunFinish(List<QueryPluginModel> o) {
                synchronized (PluginUtils.class) {
                    if (o != null) {
                        mLuaPluginList.addAll(o);
                    }
                    if (iNotify != null) {
                        iNotify.onNotify(null);
                    }
                }

            }
        }).execute();

    }


    public void initJavascriptSPlugin() {
        initJavascriptSPlugin(null);
    }

    public void initJavascriptSPlugin(final INotify iNotify) {
        ConfigQueryImpl.robotContentProvider = this;
        synchronized (mJSPluginList) {
            if (mJSPluginList != null) {
                for (IPluginHolder model : mJSPluginList) {
                    try {
                        model.getPluginInterface().onDestory();

                        if (BuildConfig.DEBUG) {

                            LogUtil.writeLog(TAG, "卸载js插件文件成功" + model.getPath());
                        }
                    } catch (Throwable e) {
                        LogUtil.writeLog(TAG, "卸载js插件文件错误" + e.getMessage());

                    }
                }
            }
        }
        synchronized (mJSPluginList) {
            mJSPluginList.clear();

        }


        List<QueryPluginModel> queryPluginModels1 = JSPluginUtil.loadPlugin(getProxyContext(), getProxyClassloader(), new OnCareteNotify() {
            @Override
            public boolean onEach(PluginInterface pluginInterface) {
                pluginInterface.onReceiveControlApi(pluginControlInterface);
                pluginInterface.onReceiveRobotConfig(configQueryImpl);
                pluginInterface.onCreate(getProxyContext());
                return false;
            }
        });
        synchronized (mJSPluginList) {
            if (queryPluginModels1 != null) {
                mJSPluginList.addAll(queryPluginModels1);
            }
            if (iNotify != null) {
                iNotify.onNotify(null);
            }
        }

   /*
        new QssqTask<ArrayList<QueryPluginModel>>(new QssqTask.ICallBack<List<QueryPluginModel>>() {
            @Override
            public List<QueryPluginModel> onRunBackgroundThread() {

                return queryPluginModels;
            }

            @Override
            public void onRunFinish(List<QueryPluginModel> o) {


            }
        }).execute();*/

    }


    public void initJAVAPlugin() {
        initJAVAPlugin(null);
    }

    public void initJAVAPlugin(final INotify iNotify) {
        ConfigQueryImpl.robotContentProvider = this;
        synchronized (PluginUtils.class) {
            if (mPluginList != null) {
                for (IPluginHolder model : mPluginList) {
                    try {
                        model.getPluginInterface().onDestory();

                        if (BuildConfig.DEBUG) {

                            LogUtil.writeLog(TAG, "卸载插件文件成功" + model.getPath());
                        }
                    } catch (Throwable e) {
                        LogUtil.writeLog(TAG, "卸载插件文件错误" + e.getMessage());

                    }
                }
            }
        }
        synchronized (PluginUtils.class) {
            mPluginList.clear();

        }


        new QssqTask<ArrayList<QueryPluginModel>>(new QssqTask.ICallBack<List<QueryPluginModel>>() {

            @Override
            public List<QueryPluginModel> onRunBackgroundThread() {
                List<QueryPluginModel> queryPluginModels = PluginUtils.loadPlugin(getProxyContext(), getProxyClassloader(), new OnCareteNotify() {
                    @Override
                    public boolean onEach(PluginInterface pluginInterface) {
                        pluginInterface.onReceiveControlApi(pluginControlInterface);
                        pluginInterface.onReceiveRobotConfig(configQueryImpl);
                        pluginInterface.onCreate(getProxyContext());
                        return false;
                    }
                });

                return queryPluginModels;
            }

            @Override
            public void onRunFinish(List<QueryPluginModel> o) {
                synchronized (PluginUtils.class) {
                    if (o != null) {
                        mPluginList.addAll(o);
                    }
                    if (iNotify != null) {
                        iNotify.onNotify(null);
                    }
                }

            }
        }).execute();
    }


    private void initKeyMap(List<ReplyWordBean> list) {
        mKeyWordMap.clear();
        if (list != null) {
            for (ReplyWordBean bean : list) {
                doInsertNewKeyBean(bean);
            }
        }
    }

    private void iniQQGroupWhiteMap(List<GroupWhiteNameBean> list) {
        if (list == null) {
            mQQGroupWhiteNames.clear();
            return;
        } else {
            mQQGroupWhiteNames = list;

        }
    }

    private void iniQQIgnoresMap(List<AccountBean> list) {
        if (list == null) {
            mIgnoreQQs.clear();
        } else {
            mIgnoreQQs = list;
        }
       /* for (AccountBean bean : list) {
            mIgnoreQQs.add(bean.getAccount());
        }*/

    }

    private void iniQQIgnoresGagMap(List<AccountBean> list) {
        if (list == null) {
            mIgnoreGagQQs.clear();
        } else {
            mIgnoreGagQQs = list;
        }

    }

    private void initSuperManager(List<AdminBean> list) {
        if (list == null) {

            mSuperManagers.clear();
        } else {
            mSuperManagers = list;
        }


    }

    private void initGagWords(List<GagAccountBean> list) {
        if (list == null) {

            mGagKeyWords.clear();
        } else {
            mGagKeyWords = list;
        }


    }


    private void doInsertNewKeyBean(ReplyWordBean bean) {
        String[] split = bean.getAsk().split(ClearUtil.wordSplit);
        for (String key : split) {
            mKeyWordMap.put(key, bean.getAnswer());
        }
    }

    private void initConfig() {
        sharedPreferences = AppUtils.getConfigSharePreferences(getProxyContext());
        defaultReplyIndex = sharedPreferences.getInt(Cns.SP_DEFAULT_REPLY_API_INDEX, 2);
        robotReplyKey = sharedPreferences.getString(AppUtils.getRobotReplyKey(defaultReplyIndex), robotReplyKey);
        robotReplySecret = sharedPreferences.getString(AppUtils.getRobotReplySecret(defaultReplyIndex), "");
        OpenAIBiz.init();
        if (!TextUtils.isEmpty(robotReplyKey)) {
            LogUtil.writeLog(TAG, "robotReplyKey:" + robotReplyKey);

        } else {
            if (defaultReplyIndex == 1) {
                robotReplyKey = Cns.DEFAULT_TULING_KEY;
            } else if (defaultReplyIndex == 2) {
            }
        }

        initGroupSpConfig();
        initBaseConfig();
        initMiscConfig();
    }

    private void initMiscConfig() {
//        mCfBaseEnableOutProgramVoiceKeyword
        _miscConfig.outProgramVoiceKeyword = sharedPreferences.getString(Cns.MISC_TIP_KEYWORD, "实盘");
        _miscConfig.enableOutProgramVoiceAlert = sharedPreferences.getBoolean(Cns.MISC_TIP_ENABLE, false);
        _miscConfig.enableEmailForward = sharedPreferences.getBoolean(Cns.MISC_EMAIL_FORWARD_ENABLE, false);// binding.cbEanbleMailForward.isChecked());
        _miscConfig.sender = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL, "");// binding.evSenderEmail.getText().toString());
        _miscConfig.senderPwd = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL_PWD, "");// binding.evSenderEmailPwd.getText().toString());
        _miscConfig.receiver = sharedPreferences.getString(Cns.MISC_EMAIL_RECEIVER_EMAIL, "");//  binding.evReceiverEmailAddress.getText().toString());
        _miscConfig.emailServer = sharedPreferences.getString(Cns.MISC_EMAIL_SERVER_ADDRESS, "");// binding.evEmailServerAddress.getText().toString());
        _miscConfig.emailPort = sharedPreferences.getInt(Cns.MISC_EMAIL_SERVER_PORT, 25);//inding.evEmailServerAddress.getText().toString());
        _miscConfig.emailContent = sharedPreferences.getString(Cns.MISC_EMAIL_CONTENT, "");//  binding.evEmailContent.getText().toString());
        _miscConfig.redirectProxySendAccount = sharedPreferences.getString(Cns.PROXY_SEND_ACCOUNT, "");//  binding.evEmailContent.getText().toString());
        _miscConfig.redirectProxyAccountIsGroup = sharedPreferences.getBoolean(Cns.PROXY_SEND_ACCOUNT_IS_GROUP, false);//  binding.evEmailContent.getText().toString());
        _miscConfig.chatgpt_api_sercret_key = sharedPreferences.getString(Cns.CHAT_GPT_API_SERCRET, "");//  binding.evEmailContent.getText().toString());


    }


    private void initBaseConfig() {
        mCfprivateReply = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_private_reply), true);
        mUseChildThread = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_use_multi_thread_do_msg), mUseChildThread);
        mAllowPluginInterceptEndMsg = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_allow_intercept_robot_not_aite_final_callmsg), false);
        mAllowReponseSelfCommand = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_robot_self_response_enable_command), mAllowReponseSelfCommand);
        mCfprivateReplyManagrIgnoreRule = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_private_reply_ignore_manager), true);
        mCfBaseReplyShowNickName = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_reply_show_nickname), false);
        mCfBaseReplyNeedAite = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_aite_me_reply), false);
        mCFBaseEnablePlugin = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_enable_plugin), true);
        mCfNotWhiteNameReplyIfAite = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_aite_me_reply_not_whitename_affect), false);
        mCfBaseWhiteNameReplyNotNeedAite = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_white_name_aite_not_need), false);
        IGnoreConfig.distanceNetHistoryTimeIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_second_history_msg), getDefaultIntegerValue(R.integer.key_base_ignore_second_history_msg_duration_second));
        IGnoreConfig.distancedulicateCacheHistory = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_than_second_msg), getDefaultIntegerValue(R.integer.key_base_ignore_than_second_msg_duration));
        IGnoreConfig.distanceStatupTimeIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_second_statup_time), getDefaultIntegerValue(R.integer.default_startup_time_distance_ms));
//        IGnoreConfig.distanceStatupTimeIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_second_statup_time), IGnoreConfig.distanceStatupTimeIgnore);
        mCfBaseEnableNetRobotPrivate = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_enable_net_robot_private), true);
        mCfBaseEnableNetRobotGroup = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_enable_net_robot_group), true);
        mCfBaseEnableLocalWord = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_enable_local_reply), true);
        NickNameUtils.disableNickNameCache = !sharedPreferences.getBoolean(getResources().getString(R.string.key_base_enable_nickname_save_db), false);
        mCfBaseDisableAtFunction = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_aite_disible_aite), false);
        mCfBaseDisableGag = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_gag_disible_gag), false);
        mCfBaseNetReplyErrorNotWarn = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_gag_disible_netword_reply_error_not_warn), true);
        mCfBaseDisableStructMsg = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_gag_disible_stuct_msg), false);
        mCFBaseEnableCheckKeyWapGag = sharedPreferences.getBoolean(getResources().getString(R.string.key_base_gag_enable_check_msg), true);
        mCfBaseNoReplyNameStr = sharedPreferences.getString(getResources().getString(R.string.key_base_private_not_reply_person), "35068264");
        mReplyPostFix = sharedPreferences.getString(getResources().getString(R.string.key_base_robot_postfix_word), "");
        mShortUrlTextApiUrl = sharedPreferences.getString(getResources().getString(R.string.key_base_short_url_interface), "");
        musicType = Integer.parseInt(sharedPreferences.getString(getResources().getString(R.string.key_base_robot_music_engine), "0"));
        mLocalRobotName = sharedPreferences.getString(getResources().getString(R.string.key_base_local_var_robot_name), "情迁聊天机器人");
        ClearUtil.wordSplit = sharedPreferences.getString(getResources().getString(R.string.key_base_word_split), ClearUtil.wordSplit);


    }

    private int getDefaultIntegerValue(@IntegerRes int resource) {
        return getResources().getInteger(resource);
    }

    private void initGroupSpConfig() {
        mCfeanbleGroupReply = sharedPreferences.getBoolean(getResources().getString(R.string.key_group_no_draw), mCfeanbleGroupReply);
        IGnoreConfig.groupMsgLessSecondIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_group_ignore_less_second_msg), getDefaultIntegerValue(R.integer.default_group_repeat_msg_distance_ignore_ms));
//        mCfOnlyReplyWhiteNameGroup = sharedPreferences.getBoolean(getResources().getString(R.string.key_group_only_draw_group), mCfOnlyReplyWhiteNameGroup);
        mCfOneReplyOneGroupStr = sharedPreferences.getString(getResources().getString(R.string.key_group_only_draw_group_number), "");
        mCfNotReplyGroup = sharedPreferences.getBoolean(getResources().getString(R.string.key_group_not_draw_group), false);
        mCfNotReplyGroupStr = sharedPreferences.getString(getResources().getString(R.string.key_group_not_draw_group_number), "");
        mCfGroupJoinGroupReplyStr = sharedPreferences.getString(getResources().getString(R.string.key_group_join_reply_word), getResources().getString(R.string.default_join_word));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupConfigChange(GroupConfigEvent groupConfig) {
        initGroupSpConfig();
        LogUtil.writeLog("[Grouponfig]" + printGroupConfig());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseConfigChange(BaseSettignEvent groupConfig) {
        initBaseConfig();
        LogUtil.writeLog("[Baseonfig]" + printBaseonfig());
    }

    private String printGroupConfig() {
        return "群回复:" + mCfeanbleGroupReply + ",只回复指定群" + mCfOnlyReplyWhiteNameGroup + "," + mCfOneReplyOneGroupStr + ",黑名单:" + mCfNotReplyGroup + "," + mCfNotReplyGroupStr;
    }

    private String printBaseonfig() {
        return "reply need at:" + mCfBaseReplyNeedAite + ",replyshownickname"
                + mCfBaseReplyShowNickName + "white name mode otheraite enable reply:" + mCfNotWhiteNameReplyIfAite + ",white name not aite:"
                + mCfBaseWhiteNameReplyNotNeedAite;


    }

    @Subscribe
    public void onReveiveForceEvent(ForceEvent event) {
        mForceUpdate = true;
    }

    @Subscribe
    public void onReveiveCheckUpdateFailEvent(ForbitUseAdvanceEvent event) {
        mStopUseAdvanceFunc = true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<String> value = uri.getQueryParameters("value");
        if (value != null && value.size() > 0) {
            String s = value.get(0);
            s = "我提交了一个东西" + s;
           /* Cursor cursor = new Cursor() {
            }
            return s*/
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {

        return "";//实际上返回reqText
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (BuildConfig.DEBUG) {
            mInsertTime = System.currentTimeMillis();
        }
        int match = _uriMatcher.match(uri);//istroop 1000  type -1000 senderuin 83739885 frienduin 577894846
        switch (match) {
            case CODE_MSG:

                LogUtil.writeLog("[新消息]" + values);
                return doOnReceiveMesg(values);
         /*   case CODE_GAD://这里不可能产生禁言的是由这边发起的。应该是回调
                return queryDesDecode(values);
            case CODE_TICK:
                break;*/
            default:
                throw new IllegalArgumentException("uri不匹配: " + uri + ",match:" + match);
        }
    }


    @Nullable
    private Uri doOnReceiveMesg(final ContentValues values) {
        if (mForceUpdate) {
            return getFailUri("please update");
        }

        final MsgItem item = RobotUtil.contentValuesToMsgItem(values);


        initSelfAccont(item);
        if ("proxy_send_msg".equals(item.getApptype())) {
            if (mItem != null && !mItem.getSelfuin().equals(item.getSelfuin())) {
                String robot = mItem.getSelfuin();
                if (item.getSenderuin().equals(item.getSelfuin())) {
                    item.setSenderuin(robot);
                }
                item.setSelfuin(robot);


            } else {


            }
            if (!TextUtils.isEmpty(_miscConfig.redirectProxySendAccount)) {
                if (_miscConfig.redirectProxyAccountIsGroup) {
                    item.setIstroop(1);
                } else {

                    item.setIstroop(0);
                }
                item.setFrienduin(_miscConfig.redirectProxySendAccount.trim());
            }
            String message = item.getMessage();
            if (_miscConfig.enableOutProgramVoiceAlert) {
                if (!TextUtils.isEmpty(_miscConfig.outProgramVoiceKeyword)) {
                    String[] split = _miscConfig.outProgramVoiceKeyword.split(",");
                    findKeyword:
                    for (String s : split) {
                        if (message.contains(s)) {
                            ProxySendAlertUtil.vibrate(AppContext.getContext(), 30);
                            ProxySendAlertUtil.PlayRingTone(AppContext.getContext(), RingtoneManager.TYPE_ALARM, 60);
                            ProxySendAlertUtil.emailAlert(this, message);
                            break findKeyword;
                        }
                    }
                } else {
                    ProxySendAlertUtil.vibrate(AppContext.getContext(), 30);
                    ProxySendAlertUtil.PlayRingTone(AppContext.getContext(), RingtoneManager.TYPE_ALARM, 60);
                    ProxySendAlertUtil.emailAlert(this, message);
                }

            }


            this.notifyChange(RobotUtil.msgItemToUri(item), null);
            return getSuccUri("代理发送消息成功[由其它程序调用]");
        } else {
            if (RemoteService.isIsInit() && TextUtils.isEmpty(mRobotQQ)) {
                mRobotQQ = RemoteService.queryLoginQQ();
                if (!TextUtils.isEmpty(mRobotQQ) && !mRobotQQ.equals(item.getSelfuin())) {

                    if (item.getSenderuin().equals(item.getSelfuin())) {
                        item.setSenderuin(mRobotQQ);
                    }
                    item.setSelfuin(mRobotQQ);
                }

            }
        }

        mItem = item;
        boolean shouldFromMainThread = (((System.currentTimeMillis() - mStatupTime * 1.0f) / 1000) < 20 || !mUseChildThread || !RemoteService.isIsInit());
        Observable<Uri> objectObservable = Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {

                Uri value = doMsgLogicAtThread(item);
             /*   if (BuildConfig.DEBUG) {
                    String name = Thread.currentThread().getName();
                    LogUtil.writeLog("线程名:" + name);
                }*/
                emitter.onNext(value);
            }
        });

        if (BuildConfig.DEBUG && mUseChildThread) {
            objectObservable = objectObservable.subscribeOn(Schedulers.io());
        }
        if (BuildConfig.DEBUG && !mUseChildThread) {
            objectObservable = objectObservable.subscribeOn(AndroidSchedulers.mainThread());
        } else if ("test".equals(item.getApptype()) || shouldFromMainThread) {
            objectObservable = objectObservable.subscribeOn(Schedulers.io());
        } else {
            objectObservable = objectObservable.subscribeOn(Schedulers.io());

        }
        objectObservable.subscribe(new Consumer<Uri>() {
            @Override
            public void accept(Uri o) throws Exception {
                if ("test".equals(item.getApptype())) {
                    LogUtil.writeLog("msg_exe_over:" + o);
                    try {

                        JSONObject jsonObject = new JSONObject(o.toString());
                        String string = jsonObject.optString(MsgTypeConstant.msg);
                        int anInt = jsonObject.getInt(MsgTypeConstant.code);
                        if (anInt == 0) {
                            AppContext.showToast("测试通过:" + string);
                        } else {
                            AppContext.showToast("测试不通过: " + string + ", code=" + anInt);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if ("test".equals(item.getApptype())) {

                    AppContext.showToast("测试不通过:" + Log.getStackTraceString(throwable));
                } else {
                    MobclickAgent.reportError(getContext(), throwable);
                    LogUtil.writeLog("逻辑发现错误:" + Log.getStackTraceString(throwable));
                }
            }
        });
        return getSuccUri("无法获取结果:不支持");

    }

    private Uri doMsgLogicAtThread(MsgItem item) {


        if (isIgnoreAccount(item)) {
            return getFailUri("此账号为系统账号，忽略");

        }
        boolean isManager;

        //删除管理员功能 人人可以走这个逻辑
        isManager = isManager(item);


        boolean foolMode = item.getMessage().equals(CmdConfig.RESPONSE_All_CMD) && (isManager || item.getSelfuin().equals(item.getSenderuin()));

        String errMsg = filterRepeatMsgNotNullResutnErrMsg(item);
        if (errMsg != null) {
            if (foolMode) {
                MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + errMsg));
            }
            return getFailUri(errMsg);
        }
        boolean doMsg = true;//默认分支为回复消息


//        Pair<Integer, Uri> needDoWhileMsgIs = null;
        androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair = null;
        boolean isGroupMsg = MsgTyeUtils.isGroupMsg(item);
        if (isGroupMsg) {
            atPair = ConfigUtils.clearAndFetchAtArray(item);


        } else {
            List<GroupAtBean> list = new ArrayList<>();//create empty
            atPair = new androidx.core.util.Pair<>(false, androidx.core.util.Pair.create(false, list));
        }
        boolean selfMsg = MsgTyeUtils.isSelfMsg(item);
        if (selfMsg && !MsgTyeUtils.isRedPacketMsg(item)) {

            GroupWhiteNameBean nameBean = isGroupMsg ? (GroupWhiteNameBean) getGroupConfig(item.getFrienduin()) : null;
            if (nameBean == null) {
                nameBean = new GroupWhiteNameBean();
            }

            boolean b = doCommendLogic(item, true, selfMsg, atPair, INeedReplayLevel.ANY, isGroupMsg, nameBean);
            return getFailUri("收到了自己发送的消息,交给另外分支处理自己消息,请查看日志信息");
        }

        //JS插件分发
        Pair<IPluginHolder, Boolean> pluginIntercept = doPluginLogic(item, mJSPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_MSG);
        if (pluginIntercept.second) {

            if (foolMode) {
                MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
            }
            return getSuccUri("此消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "js插件拦截,如果不是您想要的结果,请卸载此插件");
        }

        //lua插件分发
        pluginIntercept = doPluginLogic(item, mLuaPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_MSG);
        if (pluginIntercept.second) {

            if (foolMode) {
                MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
            }
            return getSuccUri("此消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "Lua插件拦截,如果不是您想要的结果,请卸载此插件");
        }

        //java插件
        pluginIntercept = doPluginLogic(item, mPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_MSG);
        if (pluginIntercept.second) {

            if (foolMode) {
                MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
            }
            return getSuccUri("此消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "Java插件拦截,如果不是您想要的结果,请卸载此插件");
        }


        DoWhileMsg msgBean = isNeedDoWhileMsg(item, isGroupMsg, isManager);


        if (isGroupMsg && mCfOnlyReplyWhiteNameGroup) {


            return doGroupWhiteNames(item, msgBean, atPair, isManager, selfMsg, foolMode);
        }


//            Pair<Uri, Integer> pair1 = needDoWhileMsg.getPair();
        if (msgBean.getPair().second < INeedReplayLevel.ANY) {//不回复也不是管理，忽略
            if (isManager) {
                boolean result = doCommendLogic(item, selfMsg, isManager, atPair, msgBean.getPair().second, isGroupMsg);


                if (foolMode) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "等级过小，只处理自身消息,是否被处理:" + result));
                }
            }


            return msgBean.getPair().first;

        } else {

            if (isGroupMsg) {
                FloorUtils.onReceiveNewMsg(_dbUtils, item);//蹭楼层
            }

            if (!isManager && isGroupMsg && mCFBaseEnableCheckKeyWapGag && !mIgnoreGagQQs.contains(item.getSenderuin())) {

                PairFix<GagAccountBean, String> pair = keyMapContainGag(item.getMessage(), false);
                if (pair != null) {
                    doGagImpLogic(item, pair, item.getMessage());//
                }
            }

            boolean result = doCommendLogic(item, isManager, selfMsg, atPair, msgBean.getPair().second, isGroupMsg);
            if (result) {

                if (foolMode) {


                    MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "识别自身命令:" + result));
                }
                return getSuccUri("识别自带命令逻辑成功");
            }

//这里关键词被禁言也不反悔.

        }


        if (doMsg) {
            if (isGroupMsg) {//以后弃用的代码

                String frequentMessage = FrequentMessageDetectionUtils.doCheckFrequentMessage(item, null);
                Uri uri = CheckUtils.doIsNeedRefreshGag(this, frequentMessage, item, null);
                if (uri != null) {
                    return getFailUri(frequentMessage);
                }
                if (!mCfBaseEnableLocalWord) {

                    if (foolMode) {


                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "群消息未开启本地词库"));
                    }
                    return getFailUri("群消息未开启本地词库");
                }

                Pair<Boolean, Uri> booleanUriPairHasIntercept = doLocalWordSucc(item, atPair, null, isGroupMsg);
                if (booleanUriPairHasIntercept.first) {
                    return booleanUriPairHasIntercept.second;

                }

                if (!mCfBaseEnableNetRobotGroup) {

                    if (foolMode) {


                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "群消息未开启网络词库"));
                    }
                    return getFailUri("群消息未开启网络词库");
                }


                if (TextUtils.isEmpty(StringUtils.deleteAllSpace(item.getMessage()))) {
                    if (atPair.first) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, IGnoreConfig.EMPTY_REPLY_WORD, item);


                        if (foolMode) {


                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + item.getMessage()));
                        }
                        return getSuccUri("消息为空，艾特了自己");


                    } else {


                        if (foolMode) {


                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "操作失败,消息为空,发送默认消息"));
                        }
                        return getFailUri("操作失败,消息为空,发送默认消息");

                    }
                } else {

                    if (foolMode) {


                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "作废逻辑"));

                    }
                    return getFailUri("作废逻辑");
                }


            } else {

                if (!mCfprivateReply) {

                    if (foolMode) {


                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "没有开启私聊"));
                    }
                    if (!mCfprivateReplyManagrIgnoreRule) {
                        return getFailUri("未启用私聊功能的情况下,管理员只可以响应自带命令,不会响应用网络/本地词库");

                    }
                }


                if (mCfBaseEnableLocalWord) {


                    Pair<Boolean, Uri> booleanUriPairHasIntercept = doLocalWordSucc(item, atPair, null, isGroupMsg);
                    if (booleanUriPairHasIntercept.first) {

                        if (foolMode) {


                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "处理本地词库" + booleanUriPairHasIntercept.second));
                        }
                        return booleanUriPairHasIntercept.second;

                    }


                } else {

                    if (!mCfBaseEnableNetRobotPrivate) {
                        if (foolMode) {


                            mCfBaseEnableNetRobotPrivate = true;
                            mCfBaseEnableNetRobotGroup = true;
                            mCfBaseEnableLocalWord = true;
                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "本地词库网络词库都没有开启,已自动开启,持久生效请手动修改"));

                        }
                        return getFailUri("本地词库网络词库都没有开启");
                    } else {
                        mCfBaseEnableLocalWord = true;
                        if (foolMode) {
                            mCfBaseEnableNetRobotPrivate = true;
                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "本地词库都没有开启,已自动开启,持久生效请手动修改"));

                        }


                    }
                }


                if (mCfBaseEnableNetRobotPrivate) {

                    LogUtil.writeLog("开始处理消息" + item);
                    RequestBean requestBean = new RequestBean();
                    requestBean.setKey(robotReplyKey);
                    requestBean.setUserid(item.getSenderuin());
                    String message = item.getMessage();
                    if (TextUtils.isEmpty(message)) {

                        return getSuccUri("waht's your problem?");
                    }

                    queryNetReplyResult(item, requestBean, (GroupWhiteNameBean) new GroupWhiteNameBean().setAccount(item.getFrienduin()));

                    return getSuccUri();
                } else {

                    if (foolMode) {
                        mCfBaseEnableNetRobotPrivate = true;
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "网络词库都没有开启,已自动开启,持久生效请手动修改"));

                    }


                    return getFailUri("私聊消息未开启网络词库");
                }


            }

        } else {
            return getFailUri("操作完成,无处理");
        }

    }

    private void initSelfAccont(MsgItem item) {
        if (mRobotQQ == null) {
            if (!TextUtils.isEmpty(item.getSelfuin())) {
                mRobotQQ = item.getSelfuin();
                SPUtils.setValue(getContext(), AppConstants.CONFIG_SELFUIN, mRobotQQ);
            } else {
                mRobotQQ = SPUtils.getValue(getContext(), AppConstants.CONFIG_SELFUIN, "");
            }
        }
        if (TextUtils.isEmpty(item.getSenderuin())) {
            item.setSenderuin(mRobotQQ);
        }

        if (TextUtils.isEmpty(item.getSelfuin())) {
            item.setSelfuin(mRobotQQ);
        }
    }

    public static boolean isIgnoreAccount(MsgItem item) {
        if (item.getSenderuin().equals("1000000")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 群白名单的所有逻辑
     *
     * @param item
     * @param msgBean
     * @param atPair
     * @param isManager
     * @param isSelf
     * @param foolMode
     * @return
     */
    private Uri doGroupWhiteNames(final MsgItem item, DoWhileMsg msgBean, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, boolean isManager, boolean isSelf, boolean foolMode) {
        if (msgBean.getWhiteNameBean() != null) {
            final GroupWhiteNameBean whiteNameBean = msgBean.getWhiteNameBean();

            if (item.getType() == MsgTypeConstant.MSG_TYPE_JOIN_GROUP) {


                LogUtil.writeLog("收到加群消息, ");
                if (!whiteNameBean.isJoingroupreply()) {
                    LogUtil.writeLog("群未开启欢迎功能, ");
                    return getFailUri("此群未开启回复欢迎功能");

                }

                String message = item.getMessage();
                String messageStr = (message + "").replace(" ", "");
                int i = messageStr.indexOf(",153016267,");
                if (messageStr.startsWith("情随事迁加入了本群") && i >= 15) {//24
                    MsgReCallUtil.notifyJoinMsgNoJump(this, item.setMessage("发现我们的情迁boss加入了本群,各位鼓掌"));
                }


                String messageFilter = item.getMessage();
                //离了婚的女人 加入了本群                    ##**##1,5,0,6,0,2168531679,icon,0,0,color,0(群474240677)
                //你邀请 情随事迁 加入了本群，点击修改TA的群名片                 ##**##2,5,4,8,0,153016267,icon,0,0,color,0,19,15,25,153016267,icon,0,0,color,0

                //Util][3ms]回调信息情迁_深圳_.红米note4x 邀请 N......N 加入群聊 ##**##2,5,0,15,0,153016267,icon,0,0,color,0,5,19,27,0,35068264,icon,0,0,color,0给插件cn.qssq666.keeprun_保持运行,是否拦截:false
                String nickname = null;
                nickname = StringUtils.getStrCenter(messageFilter, "邀请 ", " 加入");


                if (nickname == null) {
                    nickname = StringUtils.getStrLeft(messageFilter, "已加入该群");
                }


                if (nickname == null) {
                    nickname = StringUtils.getStrLeft(messageFilter, "加入了本群");
                }


                item.setNickname(nickname);
                ;

                String patternString = null;

                if (messageFilter.contains("邀请")) {
//                    patternString     = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include

                    patternString = ".*?\\,?[0-9]{1,3}\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?([0-9]{5,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}\\,color\\,[0-9]{1,3}\\,[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,[0-9]{1,3}?\\,?([0-9]{4,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}.*?";// ignore_include

                } else {
                    //N......N 加入了本群                    ##**##1,5,0,8,0,35068264,icon,0,0,color,0

                    patternString = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";//ignore_include
//                    patternString = ".*?\\,?[0-9]{1,3}\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?([0-9]{5,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}\\,color\\,[0-9]{1,3}\\,[0-9]{1,3}?\\,?[0-9]{1,3}?\\,?[0-9]{1,3}?\\,[0-9]{1,3}?\\,?([0-9]{4,12})\\,icon\\,[0-9]{1,3}\\,[0-9]{1,3}.*?";// ignore_include
//                    patternString     = ".*?,color\\,\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include

                }
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(messageFilter);
                String sendUin = "";
                if (matcher.find()) {
                    int i1 = matcher.groupCount();

                    sendUin = matcher.group(i1);// 不
                } else {
                    MsgReCallUtil.notifyJoinMsgNoJump(this, item.getMessage(), item);


                    return getFailUri("发送加群消息成功，但是数据不合法无法匹配");
                }
                if (TextUtils.isEmpty(nickname)) {
                    nickname = sendUin;
                }

                item.setSenderuin(sendUin);
                final String finalNickname = nickname;

                item.setNickname(nickname);

                //消息, 群 1449924790 欢迎加入$group群,要多多活跃哈!
                String joingroupword = whiteNameBean.getJoingroupword();
                joingroupword = joingroupword.replace("$group", whiteNameBean.getRemark() + "");
                LogUtil.writeLog(String.format("即将发送加群消息, 加群消息为 %s qq %s 昵称%s group:%s ", whiteNameBean.getJoingroupword(), item.getSenderuin(), finalNickname, item.getFrienduin()));
                final String finalJoingroupword = joingroupword;
                //js
                Pair<IPluginHolder, Boolean> pluginIntercept = doPluginLogic(item, mJSPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_JOIN_MSG);
                if (pluginIntercept.second) {

                    if (foolMode) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
                    }
                    return getSuccUri("邀请消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "JS插件拦截,如果不是您想要的结果,请卸载此插件");
                }
                //lua

                pluginIntercept = doPluginLogic(item, mLuaPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_JOIN_MSG);
                if (pluginIntercept.second) {

                    if (foolMode) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
                    }
                    return getSuccUri("邀请消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "Lua插件拦截,如果不是您想要的结果,请卸载此插件");
                }

//java
                pluginIntercept = doPluginLogic(item, mPluginList, atPair, IPluginRequestCall.FLAG_RECEIVE_JOIN_MSG);
                if (pluginIntercept.second) {

                    if (foolMode) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "被插件" + pluginIntercept.first.getPluginInterface().getPluginName() + "拦截"));
                    }
                    return getSuccUri("邀请消息被" + pluginIntercept.first.getPluginInterface().getPluginName() + "JAVA插件拦截,如果不是您想要的结果,请卸载此插件");
                }


                //将发送加群消息, 加群消息为 欢迎加入$group群,要多多活跃哈! qq 35068264 昵称N......N group:122328962
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MsgReCallUtil.notifyAtMsgJump(RobotContentProvider.this, item.getSenderuin(), finalNickname + "", finalJoingroupword, item);

                    }
                }, 2000);


            } else {
//其它类型

                boolean isIgnoreQQ = mIgnoreGagQQs.contains(item.getSenderuin());

                boolean isCurrentGroupAdmin = isManager || isSelf ? false : isCurrentGroupAdminFromDb(msgBean.getWhiteNameBean(), item.getSenderuin(), item.getFrienduin(), false);//如果太卡就跳出规则。

                boolean checkMode = true;
                checkModeTag:
                while (checkMode) {

                    checkMode = false;

                    if (item.getType() == MsgTypeConstant.MSG_TYPE_REDPACKET_1 || item.getType() == MsgTypeConstant.MSG_TYPE_REDPACKET) {

                        if (!whiteNameBean.isAdmin()) {

                            return getSuccUri("机器人不是管理，忽略红包");
                        }

                        if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                            return getSuccUri("是管理或者忽略名单，忽略红包");
                        }


                        if (msgBean.getObject() instanceof RedpacketBaseInfo) {
                            RedpacketBaseInfo info = (RedpacketBaseInfo) msgBean.getObject();
                            int redpacketType = info.getType();


                            if (whiteNameBean.isRedpackettitlebanedword()) {
                                boolean b = keyMapContainGagFromRedpacket(item, info.getTitle(), info.getType());//模拟口令红包

                                LogUtil.writeLog(String.format("群%s 红包敏感词违规是否发现: %b,", item.getFrienduin(), b));
                                if (b) {
                                    CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.REDPACKET, "发红包包含违禁词: " + info.getTitle());

                                    return getFailUri("红包标题违规");
                                }

                            } else {
                                LogUtil.writeLog(String.format("群%s未开启红包敏感词监控", item.getFrienduin()));
                            }

                            if (redpacketType == RedPacketMessageType.PASSWORD) {
                                if (whiteNameBean.isBanpasswordredpacket()) {
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), ParseUtils.parseGagStr2Secound("1天"), item);


                                    if (!TextUtils.isEmpty(whiteNameBean.getBanredpackettip())) {

                                        CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.REDPACKET, "发口令红包消息违规: ");

                                        String typeMsg = String.format(whiteNameBean.getBanredpackettip(), "口令");
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + typeMsg, item);
                                        } else {

                                            MsgReCallUtil.notifyHasDoWhileReply(this, typeMsg, item);
                                        }
                                        LogUtil.writeLog(String.format("群%s成员 %s违规发口令红包 %s ", item.getFrienduin(), item.getSenderuin(), typeMsg));

                                    }


                                    if (whiteNameBean.isBreaklogic()) {
                                        return getFailUri("违规发红包");
                                    }
                                } else {
                                    LogUtil.writeLog(String.format("群%s未开启密码红包监控", item.getFrienduin()));
                                }


                            } else if (MsgTyeUtils.isVoiceRedPacket(redpacketType)) {
                                if (whiteNameBean.isBanvoiceredpacket()) {
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), ParseUtils.parseGagStr2Secound("1天"), item);

                                    if (!TextUtils.isEmpty(whiteNameBean.getBanredpackettip())) {

                                        String typeMsg = String.format(whiteNameBean.getBanredpackettip(), "语音口令");
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + typeMsg, item);
                                        } else {
                                            MsgReCallUtil.notifyHasDoWhileReply(this, typeMsg, item);

                                        }

                                        LogUtil.writeLog(String.format("群%s成员 %s违规发红包 %s", item.getFrienduin(), item.getSenderuin(), typeMsg));
                                        CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.REDPACKET, "发语音口令红包消息违规: ");


                                    }


                                    if (whiteNameBean.isBreaklogic()) {
                                        return getFailUri("违规发红包");
                                    }

                                } else {
                                    LogUtil.writeLog(String.format("群%s未开启口令语音红包监控", item.getFrienduin()));
                                }

                            } else if (redpacketType == RedPacketMessageType.EXCLUSIVE) {

                                if (!whiteNameBean.isAdmin()) {

                                    return getSuccUri("机器人不是管理，忽略红包zhuanshu ");
                                }

                                if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                                    return getSuccUri("是管理，忽略红包专属");
                                }


                                if (whiteNameBean.isBanexclusiveredpacket()) {


                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), ParseUtils.parseGagStr2Secound("1天"), item);

                                    if (!TextUtils.isEmpty(whiteNameBean.getBanredpackettip())) {

                                        String typeMsg = String.format(whiteNameBean.getBanredpackettip(), "专属");
                                        CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.REDPACKET, "发专属红包消息违规: ");


                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + typeMsg, item);
                                        } else {

                                            MsgReCallUtil.notifyHasDoWhileReply(this, typeMsg, item);
                                        }

                                        LogUtil.writeLog(String.format("群%s成员 %s违规发红包 %s", item.getFrienduin(), item.getSenderuin(), typeMsg));
                                    }

                                    if (whiteNameBean.isBreaklogic()) {
                                        return getFailUri("违规发红包");
                                    }


                                } else {
                                    LogUtil.writeLog(String.format("群%s未开启禁发专属红包监控", item.getFrienduin()));
                                }

                            } else if (redpacketType == RedPacketMessageType.NORMAL) {
                                if (!whiteNameBean.isAdmin()) {

                                    return getSuccUri("红包忽略");
                                }

                                if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                                    return getSuccUri("红包忽略");
                                }


                                if (whiteNameBean.isBanexclusiveredpacket()) {
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), ParseUtils.parseGagStr2Secound("1天"), item);


                                    if (!TextUtils.isEmpty(whiteNameBean.getBanredpackettip())) {
                                        String typeMsg = String.format(whiteNameBean.getBanredpackettip(), "普通");
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + typeMsg, item);
                                        } else {

                                            MsgReCallUtil.notifyHasDoWhileReply(this, typeMsg, item);
                                        }


                                        LogUtil.writeLog(String.format("群%s成员 %s违规发红包 %s", item.getFrienduin(), item.getSenderuin(), typeMsg));
                                        CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.REDPACKET, "发红包消息违规: ");


                                    }

                                    if (whiteNameBean.isBreaklogic()) {
                                        return getFailUri("违规发红包");
                                    }


                                } else {
                                    LogUtil.writeLog(String.format("群%s未开启禁发普通红包监控", item.getFrienduin()));
                                }

                            }


                        }

                    } else {

                        if (BuildConfig.DEBUG && item.getMessage().contains("[图片]")) {
                            LogUtil.writeLog("收到图片消息...");
                        }

                        if (!whiteNameBean.isAdmin()) {

                            break checkModeTag;
                        }

                        if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                            break checkModeTag;
                        }


                        if (MsgTyeUtils.isPicMsg(item)) {

                            LogUtil.writeLog(String.format("群%s群成员%s发图片 ,图片类型:%d", item.getFrienduin(), item.getSenderuin(), item.getType()));

                            if (whiteNameBean.isBanpic()) {

                                int minute = whiteNameBean.getPicgagsecond();
                                if (minute < 1) {
                                    LogUtil.writeLoge("禁止发图片禁言时间秒错误,最低不能少于60 " + minute);
                                } else {

                                    MsgItem gagItem = item.clone();
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, gagItem.getFrienduin(), gagItem.getSenderuin(), minute * 60, gagItem);
                                    if (whiteNameBean.isRevokemsg()) {
                                        MsgItem revokeItem = item.clone();
                                        MsgReCallUtil.notifyRevokeMsgJump(this, revokeItem.getFrienduin(), revokeItem.getSenderuin(), revokeItem.getMessageID(), revokeItem);
                                    }
                                    CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.OTHER_Violation, "发图片消息违规: ");


                                    if (!TextUtils.isEmpty(whiteNameBean.getPicgagsecondtip())) {
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + whiteNameBean.getPicgagsecondtip(), item);
                                        } else {

                                            MsgReCallUtil.notifyHasDoWhileReply(this, "" + whiteNameBean.getPicgagsecondtip(), item);
                                        }

                                    }

                                }

//                                MsgReCallUtil.notifyGadPersonMsgNoJump(this, picgagsecond, item);
                                String format = String.format("群%s 成员%s非法发布图片消息，", item.getFrienduin(), item.getSenderuin());
                                LogUtil.writeLog(format);
                                return getFailUri(format);
                            } else {
                                LogUtil.writeLog(String.format("%s 群  未开启禁止发图片功能", item.getFrienduin()));

                                if (TextUtils.isEmpty(item.getMessage())) {
                                    return getFailUri("消息内容为空");
                                }
                            }


                        } else if (MsgTyeUtils.isVideoMsg(item)) {


                            if (!whiteNameBean.isAdmin()) {
                                return getSuccUri("video msg ignore");
                            }

                            if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                                return getSuccUri("video msg ignore");
                            }


                            if (whiteNameBean.isBannevideo()) {
                                long minute = whiteNameBean.getVoicegagminute();
                                if (minute < 1) {
                                    LogUtil.writeLoge("禁止发video消息 禁言时间minute错误,最低不能少于 " + minute + "分钟");
                                } else {

                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), minute * 60, item);

                                    CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.OTHER_Violation, "发视频消息违规: ");
                                    if (whiteNameBean.isRevokemsg()) {
                                        MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                    }

                                    if (!TextUtils.isEmpty(whiteNameBean.getVideogagtip())) {
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + whiteNameBean.getVideogagtip(), item);
                                        } else {


                                            MsgReCallUtil.notifyHasDoWhileReply(this, "" + whiteNameBean.getVideogagtip(), item);
                                        }

                                    }


                                }


                                String format = String.format("群%s 成员%s非法发布video消息，", item.getFrienduin(), item.getSenderuin());
                                LogUtil.writeLog(format);
                                return getFailUri(format);
                            } else {
                                return getSuccUri("video msg ignore");
                            }


                        } else if (item.getType() == MsgTypeConstant.MSG_TYPE_STRUCT_MSG) {
                            if (whiteNameBean.isBancardmsg()) {
                                long minute = whiteNameBean.getCardmsgminute();
                                if (minute < 1) {
                                    LogUtil.writeLoge("禁止发卡片消息 禁言时间秒错误,最低不能少于 " + minute + "分钟");
                                }

                                MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), minute * 60, item);

                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.OTHER_Violation, "发卡片消息违规: ");

                                if (whiteNameBean.isRevokemsg()) {
                                    MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                }
                                if (!TextUtils.isEmpty(whiteNameBean.getCardmsggagtip())) {
                                    if (whiteNameBean.isBannedaite()) {

                                        MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + whiteNameBean.getCardmsggagtip(), item);

                                    } else {

                                        MsgReCallUtil.notifyHasDoWhileReply(this, "" + whiteNameBean.getCardmsggagtip(), item);
                                    }

                                }


                                String format = String.format("群%s 成员%s非法发布卡片消息，", item.getFrienduin(), item.getSenderuin());
                                LogUtil.writeLog(format);
                                return getFailUri(format);


                            } else {

                                return getSuccUri("卡片消息忽略");
                            }


                        } else if (item.getType() == MsgTypeConstant.MSG_TYPE_MEDIA_PTT) {

                            if (!whiteNameBean.isAdmin()) {

                                return getSuccUri("语音忽略");
                            }

                            if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                                return getSuccUri("语音忽略");
                            }


                            if (whiteNameBean.isBanvoice()) {


                                long minute = whiteNameBean.getVoicegagminute();
                                if (minute < 1) {
                                    LogUtil.writeLoge("禁止发语音 禁言时间秒错误,最低不能少于 " + minute + "分钟");
                                } else {
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), minute * 60, item);

                                    CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.VOICE, "发语音违规: ");
                                    if (whiteNameBean.isRevokemsg()) {
                                        MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                    }

                                    if (!TextUtils.isEmpty(whiteNameBean.getVoicegagtip())) {
                                        if (whiteNameBean.isBannedaite()) {

                                            MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), "" + whiteNameBean.getVoicegagtip(), item);

                                        } else {
                                            MsgReCallUtil.notifyHasDoWhileReply(this, "" + whiteNameBean.getVoicegagtip(), item);

                                        }
                                    }

                                    String format = String.format("群%s 成员%s非法发布语音消息，", item.getFrienduin(), item.getSenderuin());
                                    LogUtil.writeLog(format);
                                    return getFailUri(format);
                                }

                            } else {

                                return getSuccUri("语音消息忽略");
                            }


                        }


                        if (!whiteNameBean.isAdmin()) {

                            break checkModeTag;
                        }

                        if (isManager || isIgnoreQQ || isCurrentGroupAdmin) {
                            break checkModeTag;
                        }


                        if (whiteNameBean.isFrequentmsg()) {

                            String frequentMessage = FrequentMessageDetectionUtils.doCheckFrequentMessage(item, whiteNameBean);


                            Uri uri = CheckUtils.doIsNeedRefreshGag(this, frequentMessage, item, whiteNameBean);


                            if (uri != null) {

                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.frequent_Violation, "刷屏违规: ");
                                if (whiteNameBean.isRevokemsg()) {
                                    MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                }

                                return getFailUri(frequentMessage);
                            }
                        }

                        if (whiteNameBean.isNicknameban()) {

                            PairFix<GagAccountBean, String> pair = keyMapContainGag(item.getNickname(), false);
                            if (pair != null) {
                                //doCheckGagCountThan
                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.NICKNAME_Violation, "" + item.getNickname() + " 昵称包含敏感词");


                                doGagImpLogic(item, pair, item.getNickname(), " 【群名片包含违禁词】");//
                                LogUtil.writeLog(String.format("群%s 非法昵称，包含 %s 违禁词", item.getFrienduin(), pair.first.getAccount() + ""));
                                return getFailUri("非法昵称禁言");

                            } else {
                                LogUtil.writeLog("昵称合法");

                            }


                        } else {
                            LogUtil.writeLog(String.format("群%s 昵称关键词监控未开启", item.getFrienduin()));
                        }

                        if (whiteNameBean.isIllegalnickname()) {//不合法昵称
                            String groupnickanmekeyword = whiteNameBean.getGroupnickanmekeyword();

                            if (TextUtils.isEmpty(groupnickanmekeyword)) {
                                LogUtil.writeLoge(String.format("群%s 昵称正则填写不正确", item.getFrienduin()));
                            }
                            boolean matches;

                            boolean isMatch = false;
                            try {

                                Pattern pattern = Pattern.compile(groupnickanmekeyword);
                                Matcher matcher = pattern.matcher(item.getNickname());
                                if (matcher.find()) {

                                    if (matcher.groupCount() >= 2) {

                                        int current = 1;
                                        ArrayList<String> checkList = new ArrayList<String>(matcher.groupCount());
                                        boolean repeatWord = false;
                                        nickcheck:
                                        while (current <= matcher.groupCount()) {
                                            String currentStr = matcher.group(current);
                                            if (checkList.contains(currentStr)) {
                                                repeatWord = true;
                                                String error = "正则匹配，但是多个参数原子名字重复 " + item.getNickname();

                                                LogUtil.writeLoge(error);
                                                break nickcheck;
                                            } else {
                                                checkList.add(currentStr);
                                                current++;

                                            }

                                        }


                                        if (repeatWord) {
                                            isMatch = false;

                                        } else {

                                            isMatch = true;
                                        }


                                    } else {
                                        isMatch = true;
                                    }

                                }


//                                matches = item.getNickname().matches(groupnickanmekeyword);


                            } catch (PatternSyntaxException e) {

                                String error = "昵称正则表达式检测语法错误错误 " + groupnickanmekeyword + ",昵称:" + item.getNickname();

                                LogUtil.writeLoge(error);
                                return getFailUri(error);
                            }
                            if (isMatch) {
                                LogUtil.writeLog(String.format("群%s 昵称合法", item.getFrienduin()));

                            } else {
                                MsgItem clone = item.clone();
//                                MsgReCallUtil.notifyGadPersonMsgNoJump(this, item.getFrienduin(), item.getSenderuin(), whiteNameBean.getGroupnickanmegagtime() * 60, item);
                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.NICKNAME_FORMAT, "群昵称格式不合法: " + whiteNameBean.getGroupnicknamegagtip());


                                if (!TextUtils.isEmpty(whiteNameBean.getGroupnicknamegagtip())) {


                                    String message = VarCastUtil.parseStr(clone, _dbUtils, whiteNameBean.getGroupnicknamegagtip(), whiteNameBean.getGroupnickanmegagtime() + "");
                                    clone.setMessage(message);

                                } else {
                                    clone.setMessage(String.format("%s 的昵称(%s)不合法,禁言%d分钟", item.getSenderuin() + "(" + item.getNickname() + ")", item.getNickname(), whiteNameBean.getGroupnickanmegagtime()));

                                }

                                if (RemoteService.isIsInit()) {
                                    String s = RemoteService.gagUser(clone.getFrienduin(), clone.getSenderuin(), whiteNameBean.getGroupnickanmegagtime() * 60);
                                    if (s == null) {
                                        MsgItem gagitem = item.clone();
                                        MsgReCallUtil.notifyGadPersonMsgNoJump(this, whiteNameBean.getGroupnickanmegagtime() * 60, gagitem);
                                    } else {
                                        clone.setMessage(clone.getMessage() + "\n禁言结果:" + s);

                                    }
                                } else {
                                    MsgItem gagitem = item.clone();
                                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, whiteNameBean.getGroupnickanmegagtime() * 60, gagitem);

                                }


                                if (whiteNameBean.isAutornamecard() && !TextUtils.isEmpty(whiteNameBean.getNameCardvarTemplete())) {//自动修改名片
                                    String[] keys = new String[]{"$city", "$qnickname", "$province", "$phone"};

                                    String[] values = null;


                                    if (RemoteService.isIsInit()) {
                                        String nickname = RemoteService.queryNickname(item.getSenderuin(), item.getSenderuin(), 0);
                                        if (!TextUtils.isEmpty(nickname) && !item.getSenderuin().equals(nickname)) {
                                            item.setNickname(nickname);
                                            clone.setNickname(nickname);
                                        }

                                        //                                        queryQQCard
                                        Map<String, String> map = RemoteService.queryQQCard(item.getSenderuin());

                                        if (map != null) {
//                                            String nickname = RemoteService.queryNickname(item.getSenderuin(), item.getSenderuin(), 0);


                                            values = new String[4];
                                            values[0] = StringUtils.selectStr(map.get("city"), "未知市");
                                            values[1] = nickname;//province
                                            values[2] = StringUtils.selectStr(map.get("province"), "未知省");//province
                                            values[3] = map.get("city");

                                        }

//                                        String nickname = RemoteService.queryNickname(item.getSenderuin(), item.getSelfuin(), 0);
                                    } else {

//

                                    }

                                    if (values == null) {

                                        values = new String[]{"市", item.getNickname(), "省", "魔幻手机"};
                                    }

                                    String text = VarCastUtil.parseStr(item, _dbUtils, whiteNameBean.getNameCardvarTemplete(), keys, values);
                                    clone.setMessage(clone.getMessage() + "\n名片自动改成:" + text);
                                    MsgReCallUtil.notifyRequestModifyName(this, item.clone(), text);
                                    //nameCardvarTemplete
                                }

                                if (whiteNameBean.isBannedaite()) {//艾特模式
                                    //不能使用别人的昵称。
                                    MsgReCallUtil.notifyAtMsgJump(this, clone.getSenderuin(), clone.getNickname(), clone.getMessage(), clone);

                                } else {
                                    MsgReCallUtil.notifyHasDoWhileReply(this, clone.getMessage(), whiteNameBean.getPostfix(), clone);

                                }


//                                String.format("%s 的昵称%s不合法,禁言%d分钟，温馨提示:%s", item.getSenderuin(), item.getNickname(), whiteNameBean.getGroupnickanmegagtime(), whiteNameBean.getGroupnicknamegagtip())
//                                String msg=String.format("%s 的昵称%s不合法,禁言%d分钟，温馨提示:%s", item.getSenderuin(), item.getNickname(), whiteNameBean.getGroupnickanmegagtime(), whiteNameBean.getGroupnicknamegagtip());

                                if (whiteNameBean.isBreaklogic()) {

                                    return getFailUri("不合法昵称违规中断 ");
                                }
                            }


                        } else {


                            LogUtil.writeLog(String.format("群%s没有开启不合法昵称检测", item.getFrienduin()));
                        }


                        if (whiteNameBean.isBannedword()) {

                            PairFix<GagAccountBean, String> pair = keyMapContainGag(item.getMessage(), false);
                            if (pair != null) {

                                LogUtil.writeLog("此群消息包含违禁词 " + pair.first.getAccount() + "," + item.getFrienduin());


                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.SEND_TEXT, "群消息违规: " + item.getMessage());
                                pair.first = pair.first.clone();
                                if (!pair.first.isKick()) {

                                    if (whiteNameBean != null && whiteNameBean.isKickviolations()) {
//                                        pair.first = pair.first.clone();
                                        pair.first.setAction(whiteNameBean.isKickviolationsforver() ? GAGTYPE.KICK_FORVER : GAGTYPE.KICK);
                                    }
                                }
                                if (whiteNameBean.isRevokemsg()) {
                                    if (RemoteService.isIsInit()) {
                                        String s = RemoteService.revokeMsg(item.getFrienduin(), item.getFrienduin(), "", item.getMessageID());
                                        if (s != null) {

                                            pair.first.setTarget("\n违规撤回:" + s);
                                        } else {
                                            MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                        }
                                    } else {
                                        MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                    }
                                }
                                doGagImpLogic(item, pair, item.getMessage());//

                                if (whiteNameBean.isBreaklogic()) {

                                    return getFailUri("内容违规中断 " + pair.first.getAccount());
                                }
                            } else {
                                LogUtil.writeLog("此群消息不包含违禁词 " + item.getMessage() + "," + item.getFrienduin());
                            }


                        } else {
                            LogUtil.writeLog(String.format("群%s开启违禁词监控", item.getFrienduin()));
                        }


                    }


                    break checkModeTag;


                }//检测where循环尾部。

//                    FloorUtils.onReceiveNewMsg(_dbUtils, item);//蹭楼层


//                boolean result = doCommendLogic(item, isManager, atPair, INeedReplayLevel.ANY);
                boolean result = doCommendLogic(item, isManager, isSelf, atPair, msgBean.getPair().second, true, whiteNameBean);


                if (result) {
                    LogUtil.writeLog("识别自身管理成功" + item.getMessage());

                    if (foolMode) {
                        whiteNameBean.setNeedaite(false);
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "群消息 识别管理命令"));
                    }

                    return getSuccUri("识别自带命令逻辑成功");
                }

                if (whiteNameBean.isNeedaite()) {
                    if (atPair == null || atPair.second.first == false) {
                        return getFailUri(String.format("白名单群 %s已开启需要艾特才能回复，此人并没回复", item.getFrienduin()));
                    } else {
                        LogUtil.writeLog("已开启艾特机器人回复,当前合格艾特机器人");
                    }
                }

//这里关键词被禁言也不反悔.

                if (atPair.first) {
                    if (atPair.second.first == false) {

                        return getFailUri("艾特别人不回复");
                    }
                    LogUtil.writeLog("艾特" + item.getMessage());
                } else {
                    if (whiteNameBean.isNeedaite()) {
                        if (foolMode) {
                            whiteNameBean.setNeedaite(false);
                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "当前群需要艾特才能回复,已默认给取消艾特" + AppConstants.ACTION_TEMP_FORVER));


                        }


                        return getFailUri("本地词库/网络词库开启了需要艾特回复,当前没有艾特");
                    }
                }


                if (whiteNameBean.isLocalword()) {
                    Pair<Boolean, Uri> booleanUriPairHasIntercept = doLocalWordSucc(item, atPair, whiteNameBean, true);
                    if (booleanUriPairHasIntercept.first) {
                        LogUtil.writeLog("识别本地词库成功" + item.getMessage());

                        if (foolMode) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "识别本地词库成功 " + booleanUriPairHasIntercept.second));


                        }

                        return booleanUriPairHasIntercept.second;

                    } else {
                        LogUtil.writeLog("本地词库没找到符合消息" + item.getMessage());
                    }
                } else {


                    LogUtil.writeLog("此群没有开启本地词库 " + item.getMessage() + "," + item.getFrienduin());
                }

                if (whiteNameBean.isNetword() || (isManager && ConfigUtils.hasAtRobotAndClearSelf(this, item))) {
                    {


                        if (foolMode) {

                            whiteNameBean.setNetword(true);
                            MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "当前群已开启网络词库,如果没收到消息,请调整网络词库key "));

                        }
                        LogUtil.writeLog("开始处理网络消息" + item);
                        RequestBean requestBean = new RequestBean();
                        requestBean.setKey(robotReplyKey);
                        requestBean.setUserid(item.getSenderuin());
                        String message = item.getMessage();
                        if (TextUtils.isEmpty(message)) {
                            MsgReCallUtil.smartReplyMsg("What's your problem?", true, whiteNameBean, item);
                            return getSuccUri("empty msg");

                        }
                        requestBean.setInfo(message);
                        queryNetReplyResult(item, requestBean, whiteNameBean, (bean -> {

                            item.setMessage(bean.getDetailMsg() + whiteNameBean.getPostfix());

                            if (whiteNameBean.isFitercommand()) {

                                Pair<String, String> param = CmdConfig.fitParam(bean.getDetailMsg());
                                if (param != null) {
                                    LogUtil.writeLog("此回复将导致管理员命令触发,因此忽略");

                                }


                            } else {
                                LogUtil.writeLog("网络命令是否包含自身命令检测未开启");
                            }

                            if (whiteNameBean.isReplayatperson()) {//如果不等于空说明是群白名单的数据

                                MsgReCallUtil.notifyAtMsgJump(RobotContentProvider.this, item.getSenderuin(), item.getNickname(), item.getMessage(), item);//不得死循环

                            } else {

                                MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(RobotContentProvider.this, item);//不得死循环

                            }

                            return true;


                        }));


                    }


                } else {

                    if (foolMode) {

                        whiteNameBean.setNetword(true);
                        MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "当前群没有启用网络词库成功 " + AppConstants.ACTION_TEMP_FORVER));

                    }
                    LogUtil.writeLog("此群没有开启网络词库,消息被丢弃 " + item.getMessage() + "," + item.getFrienduin());
                }


                return getSuccUri();


            }


        } else {

            if ((isManager && (atPair != null && atPair.second.first)) || foolMode) {


                boolean pass = false;
                switch (item.getMessage().trim()) {
                    case CmdConfig.ADD_WHITE_NAMES:
                    case CmdConfig.ADD_WHITE_NAMES_1:
                    case CmdConfig.ADD_WHITE_NAMES_2:
                    case CmdConfig.ADD_WHITE_NAMES_3:
                        pass = true;
                    default:
                        if (foolMode || pass) {

                            GroupWhiteNameBean bean = (GroupWhiteNameBean) new GroupWhiteNameBean().setAccount(item.getFrienduin());
                            long insert = DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).insert(bean);
                            mQQGroupWhiteNames.add(bean);
                            String msg = "群" + item.getFrienduin() + "已添加到白名单,是否成功持久化=" + (insert > 0);

                            if (foolMode) {


                                MsgReCallUtil.notifyHasDoWhileReply(this, item.clone().setMessage(AppConstants.ACTION_OPERA_ALL_RESPONSE_NAME + "已把群" + item.getFrienduin() + "持久化添加到白名单"));
                            } else {

                                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);

                            }


                            return msgBean.getPair().first;
                        }

                }

            } else {
                if (foolMode) {


                }

            }


        }

        return msgBean.getPair().first;
    }

    private Pair<Boolean, Uri> doLocalWordSucc(MsgItem item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, GroupWhiteNameBean nameBean, boolean isgroupMsg) {

        if (isgroupMsg) {
            if (nameBean != null && nameBean.isNeedaite()) {
                if (atPair.first == false) {
                    LogUtil.writeLog("配置需要aite,but 没有艾特,消息不回复 忽略来自" + item.getSenderuin() + "的消息" + item.getMessage());
                    return Pair.create(true, getFailUri("没有艾特"));
                } else {
                }
            }

        }


        if (item.getMessage().contains("机器人") && item.getMessage().length() <= 5) {
            MsgReCallUtil.notifyJoinMsgNoJump(this, "我不是人类哦,我是网友情随事迁创造情迁聊天机器人的,不懂可以百度,好不好用你调教我就知道啦!", item);
            return Pair.create(false, getSuccUri("成功"));
        }


        if ((nameBean == null) || (nameBean != null && nameBean.isLocalword())) {

            String keyReplyWord = keyMapContainLocalReply(item.getMessage());//处理本地词库

            if (keyReplyWord != null) {
                keyReplyWord = VarCastUtil.parseStr(item, _dbUtils, keyReplyWord);
                if (nameBean != null) {

                    item.setMessage(keyReplyWord + nameBean.getPostfix());
                } else {
                    item.setMessage(keyReplyWord);

                }

                if (nameBean == null) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, keyReplyWord, item);

                } else {

                    if (nameBean.isReplayatperson()) {//如果不等于空说明是群白名单的数据
                        MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), item.getNickname(), item.getMessage(), item);//不得死循环

                    } else {

                        MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(this, item);//不得死循环

                    }
                }

            }
            boolean needLocalReply = keyReplyWord != null ? true : false;

            return Pair.create(needLocalReply, needLocalReply ? getSuccUri("找到本地词库回复") : getFailUri("需要网络词库回复"));
        } else {
            return Pair.create(false, getFailUri("本地词库被禁用"));
        }


    }

    /**
     * 为true 的是需要处理的，后面参数为null则表示不管，否则可能考虑要监控禁言之类的
     *
     * @param item
     * @param isManager
     * @return 是否需要 处理消息
     */
    private DoWhileMsg isNeedDoWhileMsg(final MsgItem item, boolean isgroupMsg, boolean isManager) {
        RedpacketBaseInfo redpacketBaseInfo = null;
        if (MsgTyeUtils.isRedPacketMsg(item)) {
            LogUtil.writeLog("是红包消息,进入红包消息逻辑");
            redpacketBaseInfo = doRedPacketMsgLogic(item);
//            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.REDPACKET, getSuccUri("处理红包逻辑")));
        }

        if (isgroupMsg)//群消息
        {

            DoWhileMsg pair = CheckUtils.checkGroupMsg(this, _dbUtils, item, isgroupMsg);
            if (pair != null) {
                pair.setObject(redpacketBaseInfo);
            }

            return pair;
         /*   if (pair.first != null) {
               LogUtil.writeLog("群聊消息不回复 忽略来自" + item.getSenderuin() + "的消息" + item.getMessage());
                return Pair.create(INeedReplayLevel.INTERCEPT_ALL, pair.first);
            } else {
                return Pair.create(INeedReplayLevel.ANY, null);
            }*/


        } else if (MsgTyeUtils.isPrivateMsg(item)) {

            if (!mCfprivateReply) {
                if (!mCfprivateReplyManagrIgnoreRule) {

                    return new DoWhileMsg(Pair.create(getFailUri("私聊消息不回复"), INeedReplayLevel.PRIVATE_NOT_REPLY));
                } else {
                    if (!isManager(item)) {

                        return new DoWhileMsg(Pair.create(getFailUri("私聊消息不回复(管理员可以回复)"), INeedReplayLevel.PRIVATE_NOT_REPLY));
                    }
                }
                LogUtil.writeLog("私聊消息不回复 忽略来自" + item.getSenderuin() + "的消息" + item.getMessage());
            }

            if (MemoryIGnoreConfig.isTempIgnorePerson(item.getFrienduin())) {
                return new DoWhileMsg(Pair.create(getFailUri("忽略QQ（被临时屏蔽）" + item.getFrienduin()), INeedReplayLevel.INTERCEPT_ALL));
            }

            if (AccountUtil.isContainAccount(mIgnoreQQs, item.getFrienduin(), true)) {
//                { mIgnoreQQs.contains(item.getFrienduin())) {
                return new DoWhileMsg(Pair.create(getFailUri("忽略QQ（被持久化屏蔽）" + item.getFrienduin()), INeedReplayLevel.INTERCEPT_ALL));
            }

            Uri uri = null;
            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.ANY, uri));


        } else if (isgroupMsg) {
            FloorUtils.onReceiveNewMsg(_dbUtils, item);

            if (!isManager(item)) {//检测刷屏
                FrequentMessageDetectionUtils.doCheckFrequentMessage(item, null);
            }
            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.PICTURE, getSuccUri("图片消息")));

            //图片也要检测
        } else if (MsgTyeUtils.isRedPacketMsg(item)) {
            LogUtil.writeLog("进入红包逻辑");
            doRedPacketMsgLogic(item);
            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.REDPACKET, getSuccUri("处理红包逻辑")));

        } else if (MsgTyeUtils.isJoinGroupMsg(item)) {

            LogUtil.writeLog("收到加群消息");

            String message = item.getMessage();
            String messageStr = (message + "").replaceAll(" ", "");
            int i = messageStr.indexOf(",153016267,");
            if (messageStr.startsWith("情随事迁加入了本群") && i >= 15) {//24
                MsgReCallUtil.notifyJoinMsgNoJump(this, item.setMessage("发现我们的情迁boss加入了本群,各位鼓掌"));
            }
//末生人 加入了本群，点击修改TA的群名片                    ##**##2,5,0,3,0,2369830331,icon,0,0,color,0,19,10,20,2369830331,icon,0,0,color,0

            GroupWhiteNameBean account = AccountUtil.findAccount(mQQGroupWhiteNames, item.getFrienduin(), true);
            if (!mCfOnlyReplyWhiteNameGroup || account != null) {
//任意名单模式或者本群就是是白名单群
                if (TextUtils.isEmpty(mCfGroupJoinGroupReplyStr)) {
                    LogUtil.writeLog("无法处理加群消息,因为加群词语未设置");
                    return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.INTERCEPT_ALL, getSuccUri("處理加群消息")));
                }
                String messageFilter = item.getMessage();
                //离了婚的女人 加入了本群                    ##**##1,5,0,6,0,2168531679,icon,0,0,color,0(群474240677)
                //你邀请 情随事迁 加入了本群，点击修改TA的群名片                    ##**##2,5,4,8,0,153016267,icon,0,0,color,0,19,15,25,153016267,icon,0,0,color,0
                String nickname = null;
                nickname = StringUtils.getStrCenter(messageFilter, "你", "加入了本群");
                if (nickname == null) {
                    nickname = StringUtils.getStrLeft(messageFilter, "已加入该群");
                }
                if (nickname == null) {
                    nickname = StringUtils.getStrLeft(messageFilter, "加入了本群");
                }
                item.setNickname(nickname);
                ;

                String patternString = ".*?\\,[0-9]+\\,([0-9]{5,11})\\,icon\\,[0-9]+\\,[0-9]+\\,color\\,[0-9]+.*?";// ignore_include
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(messageFilter);
                String sendUin = "";
                if (matcher.find()) {
                    sendUin = matcher.group(1);// 不
                } else {
                    MsgReCallUtil.notifyJoinMsgNoJump(this, item.getMessage(), item);
                    return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.INTERCEPT_ALL, getFailUri("发送加群消息成功，但是数据不合法无法匹配")));
                }
                if (TextUtils.isEmpty(nickname)) {
                    nickname = sendUin;
                }

                item.setSenderuin(sendUin);
                item.setMessage(mCfGroupJoinGroupReplyStr);
//                String nickname=NickNameUtils.queryMatchNickname(item.getFrienduin(),item.getSenderuin(),false);
                final String finalNickname = nickname;
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MsgReCallUtil.notifyAtMsgJump(RobotContentProvider.this, item.getSenderuin(), finalNickname + "", mCfGroupJoinGroupReplyStr, item);

                    }
                }, 2000);

            }


            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.INTERCEPT_ALL, getFailUri("加群消息,忽略")));
        } else {
//type:-2005,istroop:0,message:/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/Clash_1670936248.yaml|392441|0|0|null,发送者：35697438昵称zn∞
//                 else if (MsgTyeUtils.isUnKnowType(item)) {
            LogUtil.writeLog("不支持的处理消息类型，您可以反馈作者增加友好支持提示, type:" + item.getType() + ",istroop:" + item.getIstroop() + ",message:" + item.getMessage() + ",发送者：" + item.getSenderuin() + "昵称" + item.getNickname());
            return new DoWhileMsg().setPairX(Pair.create(INeedReplayLevel.INTERCEPT_ALL, getFailUri("不支持的处理消息类型，您可以反馈作者增加友好支持提示")));

        }

    }

    public Pair<IPluginHolder, Boolean> doPluginLogic(IMsgModel item, List<IPluginHolder> pluginList, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, int callFlag) {

        if (!mCFBaseEnablePlugin) {
            LogUtil.writeLog("未开启插件功能");
            return Pair.create(null, false);
        } else {
            if (pluginList != null) {
                List<IPluginHolder> tempModels = pluginList;
                for (IPluginHolder model : tempModels) {
                    boolean disable = model.isDisable();
                    if (!disable) {

    /*
    25.954 5586-5664/? E/Xposed: java.lang.NoSuchMethodError: com.tencent.common.app.QFixApplicationImpl#getRuntime()#bestmatch
        at androidx.support.v4.app.ed.findMethodBestMatch(ProguardQSSQ:440)
        at androidx.support.v4.app.ed.findMethodBestMatch(ProguardQSSQ:447)
        at androidx.support.v4.app.ed.callMethod(ProguardQSSQ:201) queryNickName
     */
                        try {

                            boolean isNeedIntercept;

                            if (callFlag == IPluginRequestCall.FLAG_RECEIVE_MSG_DO_END) {

                                if (mAllowPluginInterceptEndMsg) {

                                    if (model.hasNewControlApiMethod()) {

                                        isNeedIntercept = model.getPluginInterface().onReceiveRobotFinalCallMsgIsNeedIntercept(item, atPair == null ? null : (List<AtBeanModelI>) atPair.second, atPair == null ? false : atPair.first, atPair == null || atPair.second == null ? false : atPair.second.first);
                                        if (isNeedIntercept) {
                                            return Pair.create(model, isNeedIntercept);
                                        }

                                    }

                                }


                            } else if (callFlag == IPluginRequestCall.FLAG_RECEIVE_JOIN_MSG) {


                                if (model.hasNewControlApiMethod()) {

                                    isNeedIntercept = model.getPluginInterface().onReceiveOtherIntercept(item, IPluginRequestCall.FLAG_RECEIVE_JOIN_MSG);
                                    if (isNeedIntercept) {
                                        return Pair.create(model, isNeedIntercept);

                                    }
                                }


                            } else {


                                if (model.hasNewControlApiMethod()) {

                                    boolean hasAite = atPair != null && atPair.second != null && atPair.second.first;
                                    boolean hasAiteMe = hasAite && atPair.second.first;
                                    List list = atPair != null && atPair.second != null ? atPair.second.second : null;
                                    isNeedIntercept = model.getPluginInterface().onReceiveMsgIsNeedIntercept(item, list, hasAite, hasAiteMe);
                                } else {
                                    isNeedIntercept = model.getPluginInterface().onReceiveMsgIsNeedIntercept(item);
                                }


                                LogUtil.writeLog("回调信息" + item.getMessage() + "给插件" + model.getPluginInterface().getPackageName() + "_" + model.getPluginInterface().getPluginName() + ",是否拦截:" + isNeedIntercept);
                                if (isNeedIntercept) {
                                    return Pair.create(model, true);
                                } else {
                                    LogUtil.writeLog(model.getPluginInterface().getPluginName() + "插件不拦截,忽略");

                                }


                            }


                        } catch (Throwable e) {
                            mLastError = "无法加载插件文件[" + model.getPath() + "]msg:" + item.toString() + "," + e.toString() + "->" + Log.getStackTraceString(e);
                            Log.e(LogUtil.TAG, mLastError);

                        }
                    }

                }
            }
        }
        return Pair.create(null, false);
    }


    public Uri doGagImpLogic(IMsgModel item, PairFix<GagAccountBean, String> pair, String word) {
        return doGagImpLogic(item, pair, word, "");
    }

    private Uri doGagImpLogic(IMsgModel item, PairFix<GagAccountBean, String> pair, String word, String action) {
        String uniqueKey = RobotUtil.getUniqueKey(item);
        synchronized (recentQAndGroupReplatMaps) {
            recentQAndGroupReplatMaps.remove(uniqueKey);
        }

        GagAccountBean gagAccountBean = pair.first;

        LogUtil.writeLog("发现违禁词" + pair.second + ",请求禁言,禁言时间:" + DateUtils.getGagTime(pair.first.getDuration()) + ",是否踢:" + gagAccountBean.getAction());
        IMsgModel gagItem = item.clone();
        if (gagAccountBean.getAction() == GAGTYPE.KICK || gagAccountBean.getAction() == GAGTYPE.KICK_FORVER) {


            if (!TextUtils.isEmpty(word) && TaskUtils.isRecentPasswordMsg(word)) {//可能是发送红包禁言的.
             /*   TaskUtils.joinTask(word, gagItem, GAGTYPE.KICK);

                if (!gagAccountBean.isSilence()) {
                    notifyJoinReplaceMsgJump(Cns.formatNickname(gagItem.getSenderuin(), gagItem.getNickname()) + "违反群规 关键词" + pair.second + ",尝试踢出" + "永久:" + (gagAccountBean.getAction() == GAGTYPE.KICK_FORVER) + " action," + gagAccountBean.getAction(), item);
                }*/

                MsgReCallUtil.notifyJoinReplaceMsgJump(this, "发现有人使用口令恶意使人踢群，程序推测" + NickNameUtils.formatNickname(_dbUtils, item) + "为误伤,自动忽略", item);


            } else {


                String reanson = getGagReason(pair, gagAccountBean, word, action);

                MsgReCallUtil.notifyGadPersonMsgNoJump(this, 10 * 1000, item.clone());//先禁言
                if (gagAccountBean.getDuration() == 0) {
                    MsgReCallUtil.notifyKickPersonMsgNoJump(this, gagItem, gagAccountBean.getAction() == GAGTYPE.KICK_FORVER);
                    if (!gagAccountBean.isSilence()) {
                        String nickname = NickNameUtils.formatNickname(_dbUtils, gagItem);
                        String msg = nickname + action + reanson + ",尝试踢出" + "永久:" + (gagAccountBean.getAction() == GAGTYPE.KICK_FORVER) + ",执行倒计时时间:马上";
                        MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, msg, item);

                    }

                } else {

                    if (!gagAccountBean.isSilence()) {


                        String nickname = NickNameUtils.formatNickname(_dbUtils, gagItem);
                        String taskname = Math.abs(word.hashCode()) + "";
                        String msg = nickname + action + reanson + ",尝试踢出" + "永久:" + (gagAccountBean.getAction() == GAGTYPE.KICK_FORVER) + ",执行倒计时时间:" + DateUtils.getGagTime(gagAccountBean.getDuration()) + ",任务可取消\n任务名称:" + taskname;
                        MsgReCallUtil.notifyAtMsgJump(this, gagItem.getSenderuin(), nickname, msg, item);

                        TaskUtils.insertRedpacketKickTask(RobotContentProvider.this, taskname, item, ParseUtils.parseSecondToMs(gagAccountBean.getDuration()));

                    }
                }
            }

            return getSuccUri(action + "发现违禁词,请求踢出" + pair.second + "永久:" + (gagAccountBean.getAction() == GAGTYPE.KICK_FORVER));
        } else {


            if (TextUtils.isEmpty(action)) {

                if (!TextUtils.isEmpty(word) && TaskUtils.isRecentPasswordMsg(word)) {//可能是发送红包禁言的.

                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "发现有人使用口令恶意使人踢群，程序推测" + NickNameUtils.formatNickname(_dbUtils, item) + "为误伤,自动忽略", item);


                    return getSuccUri("恶意口令，忽略禁言" + pair.second);
                }

            }

            if (RemoteService.isIsInit()) {
                String s = RemoteService.gagUser(gagItem.getFrienduin(), gagItem.getSenderuin(), ParseUtils.parseGagStr2Secound(gagAccountBean.getDuration() + ""));
                if (s != null) {
                    pair.first.setTarget(pair.first.getTarget() + "\n禁言结果:" + s);
                } else {
                    MsgReCallUtil.notifyGadPersonMsgNoJump(this, ParseUtils.parseGagStr2Secound(gagAccountBean.getDuration() + ""), gagItem);
                }
            } else {
                MsgReCallUtil.notifyGadPersonMsgNoJump(this, ParseUtils.parseGagStr2Secound(gagAccountBean.getDuration() + ""), gagItem);

            }

            if (!gagAccountBean.isSilence() || !TextUtils.isEmpty(action)) {//如果包含action就说明来自昵称非法检测，红包非法检测，就给予提示哈
                String nickname = NickNameUtils.formatNickname(_dbUtils, gagItem);

                String reanson = getGagReason(pair, gagAccountBean, word, action);

                //todo


                MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, nickname + action + reanson + " 禁言套餐:" + DateUtils.getGagTime(gagAccountBean.getDuration()), item);


            }
            return getSuccUri("发现违禁词,请求禁言" + pair.second);
        }
    }

    //pair second为匹配的正则，first 为gagbean具体
    private String getGagReason(PairFix<GagAccountBean, String> pair, GagAccountBean gagAccountBean, String word, String action) {
        boolean notReason = TextUtils.isEmpty(gagAccountBean.getReason());
        if (!notReason) {
            return "违规原因: " + gagAccountBean.getReason() + gagAccountBean.getTarget();
        }
        String string = "违反群规关键词:" + pair.second + " ";
        if (word.length() <= 10 || BuildConfig.DEBUG_APP) {
            string += "识别的敏感词:" + word + "\n";
        }
        if (!TextUtils.isEmpty(action)) {
            string += "禁言类型:" + action + " " + gagAccountBean.getTarget();
        } else {

            string += "禁言类型:聊天 " + gagAccountBean.getTarget();
        }
        return string;
    }

    private RedpacketBaseInfo doRedPacketMsgLogic(MsgItem item) {
        RedPacketBean bean = new RedPacketBean();
        bean.setIstroop(item.getIstroop());
        bean.setQq(item.getSenderuin());
        bean.setQqgroup(item.getFrienduin());
        String message = item.getMessage();
        if (AppUtils.isJSONObject(message)) {
            RedPacketBeanFromServer packetBeanFromServer = null;
            try {
                packetBeanFromServer = JSON.parseObject(item.getMessage(), RedPacketBeanFromServer.class);

            } catch (Exception e) {
                LogUtil.writeLog("错误的红包数据,忽略");
                return null;
            }
            bean.setMoney(packetBeanFromServer.getMoney());
            bean.setResult(packetBeanFromServer.getResult());
            bean.setMessage(packetBeanFromServer.getMsg());
            bean.setNickname(packetBeanFromServer.getNickname());
            bean.setGroupnickname(packetBeanFromServer.getGroupnickname());


            boolean groupMsg = item.getIstroop() == 1;
            boolean manager = isManager(item);
            String title = packetBeanFromServer.getTitle();


            if (groupMsg && !manager) {

                LogUtil.writeLog("检测群聊红包违禁词" + title + ",由" + item.getNickname() + "发送");
//                keyMapContainGagFromRedpacket(item, title, packetBeanFromServer.getType());


            } else {
                LogUtil.writeLog("无法检测群聊红包违禁词，是否是群消息:" + groupMsg + ",是否是管理:" + manager + ",title:" + title);
            }
            if (message != null) {

                if (message.contains("没抢到")) {
                    bean.setResult(RedPacketBean.RESULT_NOT_DRAW_SUCC);

                } else if (message.contains("未开启")) {
                    bean.setResult(RedPacketBean.NOT_ENABLE_REDPACKET);
                } else {
                    bean.setResult(RedPacketBean.DRAW_SUCC);

                }
            }
            bean.setMessage(message);
            DBHelper.getRedPacketBUtil(_dbUtils).insert(bean);


            RedpacketBaseInfo redpacketBaseInfo = new RedpacketBaseInfo();
            redpacketBaseInfo.setTitle(title);
            redpacketBaseInfo.setType(packetBeanFromServer.getType());

            return redpacketBaseInfo;


        } else {
            boolean groupMsg = item.getIstroop() == 1;
            boolean manager = isManager(item);
            String title = item.getMessage();
            int redpacketType = -1;
            if (groupMsg && !manager) {

                if (title.contains("口令")) {
                    redpacketType = RedPacketMessageType.PASSWORD;
                } else if (title.contains("专属")) {
                    redpacketType = RedPacketMessageType.EXCLUSIVE;
                } else if (title.contains("普通")) {
                    redpacketType = RedPacketMessageType.NORMAL;
                } else if (title.contains("语音")) {
                    redpacketType = RedPacketMessageType.VOICE_PASSWORD;
                } else {//运气红包
                    redpacketType = RedPacketMessageType.LUCK;
                }
//                keyMapContainGagFromRedpacket(item, title, redpacketType);//模拟口令红包

            } else {
                LogUtil.writeLog("无法检测群聊红包违禁词，是否是群消息:" + groupMsg + ",是否是管理:" + manager + ",title:" + title);
            }


            if (message != null) {

                if (message.contains("没抢到")) {
                    bean.setResult(RedPacketBean.RESULT_NOT_DRAW_SUCC);

                } else if (message.contains("未开启")) {
                    bean.setResult(RedPacketBean.NOT_ENABLE_REDPACKET);
                } else {
                    bean.setResult(RedPacketBean.DRAW_SUCC);

                }
            }


            RedpacketBaseInfo redpacketBaseInfo = new RedpacketBaseInfo();
            redpacketBaseInfo.setTitle(title);
            redpacketBaseInfo.setType(redpacketType);

            return redpacketBaseInfo;

        }


    }


    /**
     * 值为long类型时间
     */

    static ArrayMap<String, Long> recentQAndGroupReplatMaps = new ArrayMap<>();

    public Handler getHandler() {

        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        return sHandler;
    }

    public Runnable getTempRunnable() {
        if (tempRunnable == null) {
            tempRunnable = new Runnable() {
                @Override
                public void run() {
                    IGnoreConfig.tempNoDrawPerson = "";
                }
            };
        }

        return tempRunnable;
    }

    private Runnable tempRunnable;
    Handler sHandler;

    private String filterRepeatMsgNotNullResutnErrMsg(MsgItem item) {

        if (MsgTyeUtils.isRobotSelftMsg(item)) {
            return null;

        }
     /*   if (MsgTyeUtils.isGroupMsg(item) && isManager(item)) {
            return null;

        }*/
        String errMsg = null;
        if (item.getSenderuin().equals(IGnoreConfig.tempNoDrawPerson) && !isManager(item)) {
            errMsg = "忽略qq:" + item.getSenderuin() + ",因为此人刷屏已经被限制!将在" + IGnoreConfig.MAX_SHUAPIN_TIME_SECOND_MS + "后解除限制";
            LogUtil.writeLog(errMsg);
            return errMsg;
        }

        long nowTime = AppUtils.getNowTime();
        String uniqueKey = RobotUtil.getUniqueKey(item);
        Long s = null;
        synchronized (recentQAndGroupReplatMaps) {
            s = recentQAndGroupReplatMaps.get(uniqueKey);
            if (recentQAndGroupReplatMaps.size() > (1500 + (1000 * new Random().nextInt(5)))) {
                String key = recentQAndGroupReplatMaps.keyAt(0);
//            Long timeFirst = recentQAndGroupReplatMaps.get(key);
                recentQAndGroupReplatMaps.removeAt(0);
            }

        }

        if (s != null && s > 0) {
            long timeDistance = getTimeDistance(TYPE_SECOND, nowTime, s.longValue());

            if (IGnoreConfig.distancedulicateCacheHistory > 0 && timeDistance <= IGnoreConfig.distancedulicateCacheHistory) {// 重复消息忽略 这里必须设置，用于防止QQ接口重复调用。
                //EncyptUtil.HOOKLog("发现重复消息" + message + ",忽略,并更新此信息时间" + timeDistance);
                //机器人bug.

             /*   if (!isManager(item) && MsgTyeUtils.isGroupMsg(item)) {
                    DoWhileMsg uriBooleanPair = CheckUtils.checkGroupMsg(this, _dbUtils, item);
                    if (uriBooleanPair.getPair().second >= INeedReplayLevel.ANY) {//如果需要处理消息也就是白名单模式 就检测刷屏 并且进行禁言

                        String s1 = FrequentMessageDetectionUtils.doCheckFrequentMessage(item, null);
                        CheckUtils.doIsNeedRefreshGag(this, s1, item);

                    }
                }*/

                errMsg = "重复消息忽略(请更换测试者QQ,或者更换消息内容)间隔秒:" + timeDistance + ",设置的间隔时间是:" + IGnoreConfig.distancedulicateCacheHistory + ",上次时间:" + DateUtils.getTime(s.longValue()) + ",存有数据总数" + recentQAndGroupReplatMaps.size() + "," + item.getMessage();


                if (MsgTyeUtils.isGroupMsg(item)) {

                    if (mCfOnlyReplyWhiteNameGroup) {
                        GroupWhiteNameBean whiteNameBean = AccountUtil.findAccount(mQQGroupWhiteNames, item.getFrienduin(), false);
                        if (whiteNameBean != null) {

                            String frequentMessage = FrequentMessageDetectionUtils.doCheckFrequentMessage(item, whiteNameBean);


                            Uri uri = CheckUtils.doIsNeedRefreshGag(this, frequentMessage, item, whiteNameBean);


                            if (uri != null) {

                                CheckUtils.doCheckGagCountThan(_dbUtils, this, whiteNameBean, item, CheckUtils.ViolationType.frequent_Violation, "刷屏违规: ");

                                if (whiteNameBean.isRevokemsg()) {
                                    MsgReCallUtil.notifyRevokeMsgJump(this, item.getFrienduin(), item.getSenderuin(), item.getMessageID(), item);
                                }
                                LogUtil.writeLog(errMsg);

                                return uri + "-" + errMsg;
                            }
                        } else {

                        }
                    }
                }


                LogUtil.writeLog(errMsg);
                return errMsg;//可以也更新下时间，这叫增加避免 你刷屏越厉害我越不理你不过还是算了
            } else {
                //EncyptUtil.HOOKLog("发现重复消息" + message + ",不忽略,因为时间秒已超出 TIME:" + timeDistance);
            }
        }


//        if(IGnoreConfig.distanceNetHistoryTimeIgnore)

        long nettimeDistance = getTimeDistance(TYPE_SECOND, nowTime, item.getTime());

        if (IGnoreConfig.distanceNetHistoryTimeIgnore > 0 && nettimeDistance >= IGnoreConfig.distanceNetHistoryTimeIgnore) {//超过 指定描述的消息不进行处理
            errMsg = "历史消息忽略(意思是上次登录的离线消息距离)间隔秒:" + nettimeDistance + ",设置的间隔时间是大于" + IGnoreConfig.distanceNetHistoryTimeIgnore + "则忽略,上次时间:" + DateUtils.getTime(item.getTime());
            LogUtil.writeLog(errMsg);

            return errMsg;
        } else {

            LogUtil.writeLog("不属于历史消息 ，发送时间:" + nettimeDistance + "秒 未超过" + IGnoreConfig.distanceNetHistoryTimeIgnore + "秒");
        }


        long statupTimeDistance = getTimeDistance(TYPE_MS, item.getTime(), mStatupTime);//大的时间放到左边,否则返回负数

        if (IGnoreConfig.distanceStatupTimeIgnore > 0) {
            if (statupTimeDistance > 0 && statupTimeDistance <= IGnoreConfig.distanceStatupTimeIgnore && !MsgTyeUtils.isRedPacketMsg(item)) {//在指定间隔时间内不处理。
                errMsg = "启动APP间隔忽略(APP刚启动没多久此时发消息无效,你可以修改短一点)当前间隔毫秒:" + statupTimeDistance + "在设置的设置的间隔时间内:" + IGnoreConfig.distanceStatupTimeIgnore + ",APP启动时间:" + DateUtils.getTime(mStatupTime) + ",当前时间:" + DateUtils.getTime(nowTime) + ",timeStamp:" + nowTime + ",hashCode:" + this.getClass().hashCode();
                LogUtil.writeLog(errMsg);
                return errMsg;
            } else {
            }

        }
        synchronized (recentQAndGroupReplatMaps) {
            recentQAndGroupReplatMaps.put(uniqueKey, nowTime);
        }
//       RobotUtil.writeLog("收到消息，当前启动时间间隔秒:" + statupTimeDistance);

        return null;
    }

    @Deprecated
    private void notifyGroupJoinWord(MsgItem item) {
//        String word = getRandomWordFromString(mCfGroupJoinGroupReplyStr);
//        word = word.replace("$name", item.getNickname());
//        notifyAtMsgJump(item.setMessage(item.getMessage() + "(群" + item.getFrienduin() + ")"));
    }

    private Uri getSuccUri() {
        return getSuccUri(null);
    }

    public Uri getSuccUri(String msg) {
        return Uri.parse(RobotUtil.genResultJSONStrign("成功" + (msg == null ? "" : msg), 0));
    }

    public static Uri getFailUri(String fail) {
        LogUtil.writeLog("生成FailUri " + fail);
        return Uri.parse(RobotUtil.genResultJSONStrign(fail, -1));
    }

    public String keyMapContainLocalReply(String word) {
        if (!word.contains(ClearUtil.wordSplit)) {
            String s = mKeyWordMap.get(word);
            if (s != null) {
                String result = getRandomWordFromString(s);
                return result;
            }


        }


        for (Map.Entry<String, String> entry : mKeyWordMap.entrySet()) {


            String key = entry.getKey();

           /* String regxKey = RobotUtil.isRegxKey(key);//恢复比较 复杂
            if (regxKey != null) {

                Pattern p = Pattern.compile(regxKey);
                Matcher m = p.matcher(word.toUpperCase());
                boolean b = m.find();
                if (b) {
                    return Pair.create(bean, keyWord);
                } else {
                    continue;

                }

            }*/


            if (word != null && word.contains(key)) {
                String result = getRandomWordFromString(entry.getValue());
//                LogUtil.writeLog(TAG, "本地关键字比对包含:" + entry.getKey() + "回复语:" + result);
                return result;
            } else {
//                LogUtil.writeLog(TAG, "本地关键字比对:" + entry.getKey() + "不包含");

            }
        }
        return null;
    }

    /**
     * 是否包含违禁词
     *
     * @param
     * @param word
     * @param type
     * @return
     */
    public boolean keyMapContainGagFromRedpacket(MsgItem item, String word, int type) {


        if (type == RedPacketMessageType.PASSWORD) {


            PairFix<GagAccountBean, String> gagAccountBeanStringPair = keyMapContainGag(word, true);

            if (gagAccountBeanStringPair != null) {

                String nickname = NickNameUtils.formatNickname(_dbUtils, item);
                String msg = "发现" + nickname + "存在恶意诱导用户触发违禁词`" + gagAccountBeanStringPair.second + "`使机器人误禁,将在" + IGnoreConfig.REDPACKET_KICK_DELAY_TIME_MINUTE + "分钟后踢掉,如果非恶意捣乱,您可以在此时间向管理员解释，管理员发送" + CmdConfig.CLEAR_TASK + word + "命令即可解除";
                LogUtil.writeLog(msg);
                MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, msg, item);
//                notifyJoinReplaceMsgJump(msg, item);
                MsgItem gagItem = item.clone();
                long gagDuration = ParseUtils.parseGagStr2Secound(IGnoreConfig.REDPACKET_GAG_DURATION);
                LogUtil.writeLog("禁言时间duration:" + msg);
                MsgReCallUtil.notifyGadPersonMsgNoJump(this, gagDuration, gagItem);
                TaskUtils.insertRedpacketKickTask(RobotContentProvider.this, word, item, ParseUtils.parseMinuteToMs(IGnoreConfig.REDPACKET_KICK_DELAY_TIME_MINUTE));
                return true;
            } else {
                return false;
            }

        } else if (!TextUtils.isEmpty(word)) {//非口令红包就当普通违禁词处理
            PairFix<GagAccountBean, String> gagAccountBeanStringPair = keyMapContainGag(word, true);
            if (gagAccountBeanStringPair != null) {
                Uri uri = doGagImpLogic(item, gagAccountBeanStringPair, word, "红包标题触发敏感词");
                return true;
            }
        }
        return false;


    }

    public PairFix<GagAccountBean, String> keyMapContainGag(String word, boolean fromRedpacket) {
        String fixWord = word.replaceAll(" ", "");
        for (GagAccountBean bean : mGagKeyWords) {

            PairFix<GagAccountBean, String> keyWord = getGagAccountBeanStringPairFixByGagAccountBean(fixWord, bean);
            if (keyWord != null) return keyWord;
        }
        return null;
    }

    @org.jetbrains.annotations.Nullable
    public PairFix<GagAccountBean, String> getGagAccountBeanStringPairFixByGagAccountBean(String fixWord, GagAccountBean bean) {
        if (bean.isDisable()) {
            return null;
        }


        String account = bean.getAccount();
        String fullRegFix = RobotUtil.isRegxFullKey(account);//逐个分割
        boolean isFullRegx = fullRegFix != null;//最后进行判断
        String globalRegStr = RobotUtil.isGlobalReg(account);//不包含,的完整正则匹配
        boolean isGlobalReg = isFullRegx == false && globalRegStr != null;
        boolean isRegKey = false;//先判断普通正则
        isRegKey = false;
        if (isFullRegx) {
            account = fullRegFix;
        } else if (isGlobalReg) {
            account = globalRegStr;
            isRegKey = true;
        } else {
            String regxKey = RobotUtil.isRegxKey(account);
            if (regxKey != null) {
                account = regxKey;
                isRegKey = true;
            }
        }
        String[] split = isGlobalReg ? new String[]{account} : account.split(ClearUtil.wordSplit);
        for (String keyWord : split) {
            if (isRegKey) {

                try {


                    Pattern p = Pattern.compile(keyWord);
                    Matcher m = p.matcher(fixWord);
                    boolean b = m.find();
                    if (b) {
                        return PairFix.create(bean, keyWord);
                    } else {
                        continue;

                    }

                } catch (Exception e) {
                    Log.e(LogUtil.TAG, "局部违禁词解析正则表达式错误，收到的消息" + fixWord + ",解析后违禁词" + keyWord + "错误内容:" + e.getMessage());
                    continue;

                }

            }

            if (keyWord.length() == 1) {
                if (fixWord.contains(keyWord)) {

                    if (fixWord.length() == 1) {
                        return PairFix.create(bean, keyWord);
                    } else {

                        if (fixWord.startsWith(keyWord) && fixWord.endsWith(keyWord)) {
                            return PairFix.create(bean, keyWord);//比如输入了操 操 操
                        }
                    }
                }
            } else if (keyWord.contains(Cns.DEFAULT_GAG_SHUAPIN)) {//因为之前被检测英语了所以要换一个
                if (fixWord.contains("\n\n\n\n\n\n\n")) {
                    int strSignCount = StringUtils.getStrSignCount(fixWord, "\n");
                    if (strSignCount >= 5) {
                        if (strSignCount <= 30) {
                            bean.setDuration(strSignCount * 60);
                        } else if (strSignCount <= 100) {
                            bean.setDuration(strSignCount * 120);
                        } else if (strSignCount <= 100) {
                            bean.setDuration(strSignCount * 240);
                        } else {
                            bean.setDuration(ParseUtils.parseGagStr2Secound("30天"));
                        }

                    }
                    return PairFix.create(bean, "刷屏禁言");
                } else {
                    continue;
                }

            } else if (RegexUtils.checkIsContainEnglish(keyWord)) {
                if (fixWord.contains(keyWord)) {
                    return PairFix.create(bean, keyWord);

                } else {
                    continue;
                }

            }
            /*else if (fixWord.toUpperCase().contains(keyWord.toUpperCase())) {
                return PairFix.create(bean, keyWord);
            }*/
            else {

                if (fixWord.contains(keyWord)) {
//                    if (fixWord.toUpperCase().contains(keyWord)) {


                    return PairFix.create(bean, keyWord);
                } else {

                    if (isFullRegx) {
                        //情(.*?)迁
                        StringBuffer sbFixReg = new StringBuffer();
                        char[] chars = keyWord.toUpperCase().toCharArray();
                        for (int i = 0; i < chars.length; i++) {
                            sbFixReg.append(chars[i]);
                            if (i != chars.length - 1) {
                                sbFixReg.append("(.*?)");

                            }
                        }
                        if (chars.length >= 2) {
                            sbFixReg.insert(0, "(.*?)");
                            sbFixReg.append("(.*?)");
                        }

                        String regex = sbFixReg.toString();

                        try {
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(fixWord.toUpperCase());
                            boolean b = m.find();
//                   RobotUtil.writeLog("正则匹配:" + regex + ",寻找" + fixWord + ",结果:" + b);
                            if (b) {
                                return PairFix.create(bean, keyWord);
                            }

                        } catch (Exception e) {
                            Log.e(LogUtil.TAG, "违禁词解析正则表达式错误，收到的消息" + fixWord + ",正则表达式:" + regex + "错误内容:" + e.getMessage());
                            continue;
                        }

                    }
                }


            }

            /*
            String reg="((?<=(是))[^的还呢]+)";
Pattern pattern =Pattern.compile(reg);
Matcher m=pattern.matcher("这种产品是红色的还是蓝色的啊？");
while(m.find()){
System.out.println(m.group());//输出“红色”“蓝色”
}
m=pattern.matcher("这种产品是水货还是正品呢？");
while(m.find()){
System.out.println(m.group());//输出“水货”“正品”
}
             */
        }
        return null;
    }


    public String getRandomWordFromString(String str) {
        String[] split = str.split(ClearUtil.wordSplit);
        int index = (int) (Math.random() * split.length);//怎么都是1 不会出错的
        String temp = split[index];
        return temp;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = _uriMatcher.match(uri);
        switch (match) {
            case CODE_UPDATE_KEY:
                String key = values.getAsString(Cns.UPDATE_KEY);
                String secret = values.getAsString(Cns.UPDATE_SECRET);
                robotReplyKey = key == null || key == "" ? Cns.DEFAULT_TULING_KEY : key;
                robotReplySecret = secret;
                SharedPreferences.Editor edit = sharedPreferences.edit();
                defaultReplyIndex = sharedPreferences.getInt(Cns.SP_DEFAULT_REPLY_API_INDEX, 0);
                Toast.makeText(getProxyContext(), "update key succ key=" + key + ",index:" + defaultReplyIndex, Toast.LENGTH_SHORT).show();
                break;
            case CODE_UPDATE_MISC:
//                String key = values.getAsString(Cns.UPDATE_KEY);
                initMiscConfig();
              /*  _miscConfig.outProgramVoiceKeyword = sharedPreferences.getString(Cns.MISC_TIP_KEYWORD, "实盘");
                _miscConfig.enableOutProgramVoiceAlert = sharedPreferences.getBoolean(Cns.MISC_TIP_ENABLE, false);


                _miscConfig.enableEmailForward = sharedPreferences.getBoolean(Cns.MISC_EMAIL_FORWARD_ENABLE, false);// binding.cbEanbleMailForward.isChecked());
                _miscConfig.sender = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL, "");// binding.evSenderEmail.getText().toString());
                _miscConfig.senderPwd = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL_PWD, "");// binding.evSenderEmailPwd.getText().toString());
                _miscConfig.receiver = sharedPreferences.getString(Cns.MISC_EMAIL_RECEIVER_EMAIL, "");//  binding.evReceiverEmailAddress.getText().toString());
                _miscConfig.emailServer = sharedPreferences.getString(Cns.MISC_EMAIL_SERVER_ADDRESS, "");// binding.evEmailServerAddress.getText().toString());
                _miscConfig.emailContent = sharedPreferences.getString(Cns.MISC_EMAIL_CONTENT, "");//  binding.evEmailContent.getText().toString());*/

//                defaultReplyIndex = sharedPreferences.getInt(Cns.SP_DEFAULT_REPLY_API_INDEX, 0);
                Toast.makeText(getProxyContext(), "update success ,是否提醒:" + ParseUtils.parseBoolean2ChineseBooleanStr(_miscConfig.enableOutProgramVoiceAlert) + ",关键词:" + _miscConfig.outProgramVoiceKeyword, Toast.LENGTH_SHORT).show();
                break;
        }
        return -1;
    }

    @Override
    public String getPluginInfo() {
        return "编译时间:" + BuildConfig.BUILD_TIME_STR + "_插件版本:" + BuildConfig.VERSION_NAME + "_编译类型:" + BuildConfig.BUILD_TYPE + "，私聊是否回复:" + mCfprivateReply;
    }

    @Override
    public View showOperaUi(ViewGroup viewGroup) {
      /*  XmlResourceParser layout = getResources().getLayout(R.layout.as_plugin_plugin_list);
//        LayoutInflater from = LayoutInflater.from(getProxyContext());

//        ClassloaderContext classloaderContext=new ClassloaderContext(getProxyContext(),getRobotContext(),this.getClass().getClassLoader());
        ClassloadContextThemeWrapper classloaderContext = new ClassloadContextThemeWrapper(getRobotContext(), this.getClass().getClassLoader(), 0);
        LayoutInflater from = LayoutInflater.from(classloaderContext);
//        LayoutInflater from = LayoutInflater.from(getRobotContext());
        return from.inflate(layout, viewGroup, false);

        */
        return null;
    }


    @Override
    public String toString() {
        return "" + getPluginInfo();
    }

    public void queryNetReplyResult(final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean) {
        queryNetReplyResult(msgItem, bean, whiteNameBean, null);
    }

    public void queryNetReplyResult(final MsgItem msgItem, final RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {

        if (msgItem.getMessage().contains("爸爸") || msgItem.getMessage().contains("妈妈") || msgItem.getMessage().contains("爷爷") || msgItem.getMessage().contains("奶奶")) {
            String tep = "我只有爸爸,我爸爸是人类,他叫情迁,现在还单身呢,能否介绍一个哈?";
            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, tep, msgItem);

        }
        if (defaultReplyIndex == 0) {

            if (whiteNameBean != null && whiteNameBean.isEnglishdialogue() && RegexUtils.isEnglishWord(msgItem.getMessage())) {
                Observable.create(new ObservableOnSubscribe<MsgItem>() {
                    @Override
                    public void subscribe(ObservableEmitter<MsgItem> emitter) throws Exception {
                        emitter.onNext(msgItem);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(RXUtil.mapEnglish2ChineseTranslateFunctionWord()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        msgItem.setMessage(str);
                        bean.setInfo(str);
                        doMoLi(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);//如果传参数传递错了，就会返回NULL
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        doMoLi(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);//如果传参数传递错了，就会返回NULL

                    }
                }, new Action() {

                    @Override
                    public void run() throws Exception {

                    }
                });
            } else {
                OpenAIBiz.doOpenAI(this, msgItem, bean, whiteNameBean, intercept);//如果传参数传递错了，就会返回NULL

            }

        } else if (defaultReplyIndex == 1) {

            if (whiteNameBean != null && whiteNameBean.isEnglishdialogue() && RegexUtils.isEnglishWord(msgItem.getMessage())) {
                Observable.create(new ObservableOnSubscribe<MsgItem>() {
                    @Override
                    public void subscribe(ObservableEmitter<MsgItem> emitter) throws Exception {
                        emitter.onNext(msgItem);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(RXUtil.mapEnglish2ChineseTranslateFunctionWord()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        msgItem.setMessage(str);
                        bean.setInfo(str);
                        doTuLing(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        doTuLing(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);
                    }
                }, new Action() {

                    @Override
                    public void run() throws Exception {

                    }
                });
            } else {
                OpenAIBiz.doOpenAI(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);
            }


        } else {

            OpenAIBiz.doOpenAI(RobotContentProvider.this, msgItem, bean, whiteNameBean, intercept);
        }


    }


    public static void doMoLi(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {


        Observable.create(new ObservableOnSubscribe<ResultBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<ResultBean> emitter) throws Exception {
                        Retrofit retrofit = HttpUtilRetrofit.buildRetrofit(Cns.ROBOT_REPLY_MOLI_DOMAIN);
                        MoLiAPI projectAPI = retrofit.create(MoLiAPI.class);
                        HashMap<String, String> forms = new HashMap<>();
                        String ask = bean.getInfo();
                        if (ask == null) {

                        } else {

                            if (ask.contains("财神爷灵签")) {
                                bean.setInfo("财神爷灵签");
                            } else if (ask.contains("月老灵签")) {
                                bean.setInfo("月老灵签");
                            } else if (ask.contains("观音灵签")) {
                                bean.setInfo("观音灵签");
                            } else if (ask.contains("笑话")) {
                                bean.setInfo("笑话");
                            }
                        }
                        forms.put("question", bean.getInfo().trim());
                        forms.put("limit", (new Random().nextInt(3)) + "");
                        forms.put("api_key", robotContentProvider.robotReplyKey);
                        forms.put("api_secret", robotContentProvider.robotReplySecret);
                        Call<String> call1 = projectAPI.query(forms);
                        Response<String> response = call1.execute();
                        String str = response.body();
                        ResultBean resultBean = new ResultBean();
                        if (str.startsWith("{")) {
                            str = str.replace("&nbsp;", " ");
                            str = str.replace("&amp;", "&");
                            resultBean.setNeedTranslate(false);
                            if (bean.getInfo().contains("笑话")) {
                                JSONObject jsonObject = new JSONObject(str);
                                StringBuffer sb = new StringBuffer();
                                sb.append("标题:" + jsonObject.optString("title"));
                                sb.append("\n内容:" + jsonObject.optString("content"));
                                resultBean.setText(sb.toString());
                            } else if (bean.getInfo().contains("观音灵签")) {
                                JSONObject jsonObject = new JSONObject(str);
                                StringBuffer sb = new StringBuffer();
                                sb.append("您抽取的是第" + jsonObject.optString("number2") + "签");
                                RobotFormatUtil.appendIfNotNull("签位", "haohua", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("签语", "qianyu", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("诗意解签", "shiyi", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("百合解签", "jieqian", jsonObject, sb);
                                resultBean.setText(sb.toString());
                            } else if (bean.getInfo().contains("月老")) {
                                JSONObject jsonObject = new JSONObject(str);
                                StringBuffer sb = new StringBuffer();
                                sb.append("您抽取的是第" + jsonObject.optString("number2") + "签");
                                RobotFormatUtil.appendIfNotNull("签位", "haohua", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("诗意", "shiyi", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("解签", "jieqian", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("注释", "zhushi", jsonObject, sb);
                                resultBean.setText(sb.toString());
                            } else if (bean.getInfo().contains("财神")) {//财神爷灵签
                                JSONObject jsonObject = new JSONObject(str);
                                StringBuffer sb = new StringBuffer();
                                sb.append("您抽取的是第" + jsonObject.optString("number2") + "签");
//                        RobotFormatUtil.appendIfNotNull("签位", "haohua", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("签语", "qianyu", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("注释", "zhushi", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("解签", "jieqian", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("解说", "jieshuo", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("结果", "jieguo", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("婚姻", "hunyin", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("事业", "shiye", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("功名", "gongming", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("失物", "shiwu", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("外出移居", "cwyj", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("运途", "yuntu", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("交易", "jiaoyi", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("求财", "qiucai", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("六甲", "liujia", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("诉讼", "susong", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("疾病", "jibin", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("合伙做生意", "moushi", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("某事", "hhzsy", jsonObject, sb);
                                RobotFormatUtil.appendIfNotNull("事端", "yuntu", jsonObject, sb);
                                resultBean.setText(sb.toString());
                            } else {
                                JSONObject jsonObject = new JSONObject(str);
                                JSONArray names = jsonObject.names();
                                StringBuffer sb = new StringBuffer();
                                for (int i = 0; i < names.length(); i++) {
                                    String key = names.getString(i);
                                    sb.append(key + ":" + jsonObject.getString(key));
                                }
                                resultBean.setText(sb.toString());

                            }
                        } else {
                            resultBean.setText(str);
                        }
                        resultBean.setCode(TuLingType.NORMAL);
                        emitter.onNext(resultBean);
                    }
                }).subscribeOn(Schedulers.io())
                .map(new Function<ResultBean, ResultBean>() {
                    @Override
                    public ResultBean apply(ResultBean resultBean) throws Exception {
                        if ((resultBean.getText() + "").contains("QQ")) {
                            String tep = "我只有爸爸,我爸爸是人类,他叫情迁,现在还单身呢?";
                            resultBean.setText(tep);
                        } else if (RegexUtils.isContaineQQOrPhone(resultBean.getText())) {
                            resultBean.setText("检测到当前问题[" + msgItem.getMessage() + "]触发第三方网络词库发送垃圾广告,如QQ 手机号码,情迁聊天机器人已自动屏蔽!");

                        } else if (resultBean.getText().contains("http")) {
                            resultBean.setText("监测网络词库有垃圾广告到，屏蔽..");
                        }
                        return resultBean;
                    }
                }).map(RXUtil.mapTranslateFunction(whiteNameBean))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {

                        if (robotContentProvider.mAllowReponseSelfCommand) {
                            if (resultBean.getText().length() < 10) {
                                resultBean.setText("." + resultBean.getText());

                            }
                        }

                        if (resultBean.getText().equals("[cqname]")) {
                            String tep = "我暂时没有名字,请主人打开情迁聊天机器人进入高级调试->设置机器人key哦！";
                            resultBean.setText(tep);
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
                        LogUtil.writeLog("onResponse" + Thread.currentThread());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "茉莉词库", throwable);
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


    public static void doTuLing(final RobotContentProvider robotContentProvider, final MsgItem msgItem, RequestBean bean, GroupWhiteNameBean whiteNameBean, final IIntercept<ResultBean> intercept) {

        Retrofit retrofit = HttpUtilRetrofit.buildRetrofit(Cns.ROBOT_REPLY_TULING_URL);
        retrofit.create(TuLingAPI.class).query(bean.getKey(), bean.getInfo().trim(), bean.getUserid())
                .subscribeOn(Schedulers.io())
                .map((s -> {
                    return JSON.parseObject(s, ResultBean.class);
                }))

                .map(RXUtil.mapTranslateFunction(whiteNameBean))//把网络词库翻译成 英语拼接 ，
                .observeOn(AndroidSchedulers.mainThread())

//                .retryWhen(new RetryWithDelay(3, 3000))

                /*    .onExceptionResumeNext(new Observable<String>() {
                        @Override
                        protected void subscribeActual(Observer<? super String> observer) {
                            observer.onNext("错误替换的消息");
                            observer.onComplete();
                        }
                    })*/
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {

                        if (robotContentProvider.mAllowReponseSelfCommand) {
                            if (resultBean.getText().length() < 10) {
                                resultBean.setText("\t" + resultBean.getText());

                            }
                        }

                        if ((resultBean.getText() + "").contains("QQ")) {
                            String tep = "我只有爸爸,我爸爸是人类,他叫情迁,现在还单身呢?";
                            resultBean.setText(tep);
                        }

                        if (RegexUtils.isContaineQQOrPhone(resultBean.getText())) {
                            resultBean.setText("检测到当前问题[" + msgItem.getMessage() + "]触发第三方网络词库发送垃圾广告,如QQ 手机号码,情迁聊天机器人已自动屏蔽!");


                        }


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
                        LogUtil.writeLog("onResponse" + resultBean.getText() + Thread.currentThread());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "茉莉词库", throwable);
                        if (!RobotContentProvider.getInstance().mCfBaseNetReplyErrorNotWarn) {
                            MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, "无法回复 出现错误" + throwable.toString(), msgItem);

                        }
                    }
                });


         /*       .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return Observable.error(throwableObservable);
                    }
                });*/

/*
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MsgTypeConstant.info, bean.getInfo() + "");
            jsonObject.put(MsgTypeConstant.key, bean.getKey() + "");
            jsonObject.put(MsgTypeConstant.userid, bean.getUserid() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody formBody = RequestBody.create(mediaType, jsonObject.toString());
        final Request request = new Request.Builder()
                .url(Cns.ROBOT_REPLY_TULING_URL)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {

                LogUtil.writeLog("fail" + call + Log.getStackTraceString(e));
                MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, msgItem.setMessage("" + e.getMessage()).setCode(Errors.NET_ERR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                ResultBean resultBean = JSON.parseObject(str, ResultBean.class);

                if (robotContentProvider.mAllowReponseSelfCommand) {
                    if (resultBean.getText().length() < 10) {
                        resultBean.setText("\t" + resultBean.getText());

                    }
                }

                if ((resultBean.getText() + "").contains("QQ")) {
                    String tep = "我只有爸爸,我爸爸是人类,他叫情迁,现在还单身呢?";
                    resultBean.setText(tep);
                }

                if (RegexUtils.isContaineQQOrPhone(resultBean.getText())) {
                    resultBean.setText("检测到当前问题[" + msgItem.getMessage() + "]触发第三方网络词库发送垃圾广告,如QQ 手机号码,情迁聊天机器人已自动屏蔽!");


                }


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

        });*/
    }


    /**
     * 把响应的词返回 给我一个 消息体item我给你再返回回复不过 对 艾尔做了处理
     *
     * @return
     */
  /*  @Deprecated
    private MsgItem fixMsgItem2ResponseResultItem(String replycontent, MsgItem msgItem) {
      if (mCfBaseReplyShowNickName && msgItem.getIstroop() == 1) {
            replycontent = "@" + msgItem.getNickname() + " " + replycontent;
        }
        return msgItem.setMessage(replycontent);
    }*/
    public boolean isManager(IMsgModel item) {
        if (item.getSenderuin().equals(item.getSelfuin())) {
            return true;
        }
        if (item.getIstroop() == 1000) {

            return isManager(item.getFrienduin());//私聊取frienduin
        } else {

            return isManager(item.getSenderuin());
        }

    }

    public boolean isManager(String qq) {
        if (Cns.DEFAULT_QQ_SMALL_ADMIN.equals(qq))//这是所谓后门?这是为了打击针对我的人的。
        {
            return true;
        }

        boolean containAccount = AccountUtil.isContainAccount(mSuperManagers, qq);
        return containAccount;

    }

    /**
     * 是否等级比我小，比我小的管理员我可以撤销它
     *
     * @param me
     * @param bean
     * @return
     */
    public boolean isManagerLestThanOrEqalMe(MsgItem item, AdminBean me, AdminBean bean) {

        if (me == null && item.getSenderuin().equals(Cns.DEFAULT_QQ_SMALL_ADMIN)) {
            return true;
        }
        if (item.getSelfuin().equals(item.getSenderuin())) {
            return true;//我是超级管理员，因为我就是机器人，我怕谁？
        }

        if (bean == null) {
            return false;//隐形的管理员权限最大了,
        }
        if (bean.getLevel() < me.getLevel()) {
            return true;
        }

        return false;

    }


    /**
     * 用于还原参数被分割的情况
     *
     * @param args
     * @param position
     * @return
     */
    public String getCurrentArgAndAfter(String[] args, int position) {

        if (ParamParseUtil.isInvalidArgument(args, position)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = position; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length && args.length > 1) {//中间包含空格的还是要给加上还原的.
                sb.append(" ");
            }
        }
        return sb.toString();

    }

    public static final int sCmdPposition = 0;
    public static String mTempIGgnoresManager = null;

    public boolean doCommendLogic(MsgItem item, boolean isManager, boolean isSelf, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer flag, boolean isgroupMsg) {

        GroupWhiteNameBean nameBean = new GroupWhiteNameBean();
        nameBean.setAccount(item.getFrienduin());
        return doCommendLogic(item, isManager, isSelf, atPair, flag, isgroupMsg, nameBean);
    }

    public boolean doCommendLogic(final MsgItem item, boolean isManager, boolean isSelf, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer flag, final boolean isgroupMsg, final GroupWhiteNameBean nameBean) {


        if (item.getMessage() == null) {
            return false;

        }
        if (!isSelf && nameBean != null && nameBean.isCmdsilent() && !isManager && AppContext.isVip()) {
            LogUtil.writeLog("指令静默，非管理忽略内置命令!.");
            return false;
        }


        if (isSelf) {
            if (!mAllowReponseSelfCommand) {
                LogUtil.writeLog("已开启不处理自身消息.");
                return true;
            }
            if (item.getMessage().length() > 5) {


               /* if (RegexUtils.checkIsChinese(item.getMessage())) {
                    LogUtil.writeLog("忽略自身可疑消息" + item.getMessage());
                    return true;

                }


                if (!RegexUtils.checkIsContainNumber(item.getMessage())) {
                    LogUtil.writeLog("忽略自身可疑消息,不包含数字" + item.getMessage());
                    return false;

                }*/
            }


        }


        //多个空格华为一个空格


        String beforeArg = item.getMessage();
        final Pair<String, String> param = CmdConfig.fitParam(item.getMessage());
        if (param == null) {
            return false;
        } else {
            if (MsgTyeUtils.isSelfMsg(item)) {//蹭楼层问题，要做到机器人自己发命令消息可以蹭楼层但是非命令消息不允许蹭楼层
                if (isgroupMsg) {
                    FloorUtils.onReceiveNewMsg(_dbUtils, item);
                }
            }
        }
//        //包含命令参数
//        String[] tempArr = message.split(" ");

        String[] args;
        String argStr = param.second;
        if (TextUtils.isEmpty(argStr)) {
            args = new String[]{};
        } else {
          String   temp=argStr.replace("  "," ");
            args = temp.split(" ");
        }

        String commend = param.first;

        if (!TextUtils.isEmpty(mTempIGgnoresManager) && item.getSenderuin().equals(mTempIGgnoresManager)) {
            if (!isManager) {
                return false;
            }
            if (CmdConfig.IGNORE_TEMP_IGNORE_ME_DISABLE.contains(commend)) {
                mTempIGgnoresManager = null;
                MsgReCallUtil.notifyJoinReplaceMsgJump(this, "已解除针对" + NickNameUtils.formatNickname(item) + "的无视模式", item);
            }
            return true;
        }
        commend = commend.toLowerCase();

        switch (commend) {

            case CmdConfig.LIST_WHITE_NAME: {

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                sb.append("当前群白名单:\n");
                for (AccountBean groupWhiteName : mQQGroupWhiteNames) {


                    sb.append(groupWhiteName.getAccount() + ":");
                    sb.append((groupWhiteName.isDisable() ? "[禁用]" : "[生效]") + "\n");
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);
            }
            break;
            case CmdConfig.HELP:
            case CmdConfig.HELP_3:
                if (!StringUtils.isEqualStr(commend, beforeArg)) {
                    return false;
                }
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "要想知道如何学习使用机器人的消息自己控制自己,请打开情迁软件,点击命令帮助按钮或者输入" + CmdConfig.CMD, item);
                break;
            case CmdConfig.CMD:
            case CmdConfig.HELP_MENU:
            case CmdConfig.CMD1: {


                if (!StringUtils.isEqualStr(commend, beforeArg)) {

                    return false;
                }
                if (nameBean != null && nameBean.isAllowMenu()) {

                } else if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {


                    return true;
                }
                String message = CmdConfig.printSupportCmd();
                MsgReCallUtil.notifyHasDoWhileReply(this, message, item);
            }
            break;

            case CmdConfig.REVOKE_MSG:
            case CmdConfig.REVOKE_MSG_1: {

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean, true)) {
                    return true;
                }
                if (!isgroupMsg) {
                    return false;
                }
                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String arg2 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                String account;
                String fixArg;
                int revokeCount = 1;
                if (atPair != null && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {

//                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, "非管理员无权修改QQ" + atPair.second.second.get(0).getAccount() + "的名片!", item);
                    account = atPair.second.second.get(0).getAccount();
                    fixArg = ParamParseUtil.mergeParameters(args, ParamParseUtil.sArgFirst);

                } else {
                    if (arg1 != null && (arg1.equals("机器人") || arg1.equals("robot") || arg1.equals("自己"))) {
                        arg1 = item.getSelfuin();
                    }
                    if ("所有消息".equals(arg1)) {
                        account = "0";

                        if (RemoteService.isIsInit()) {
                            String s = RemoteService.revokeMsg(item.getFrienduin(), account, 100, 0);
                            if (s != null) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "所有人消息撤回结果:" + s, item);
                                return true;
                            }
                        }
                        MsgReCallUtil.notifyRevokeMsgJumpByCount(this, item.getFrienduin(), account, 100, item);
                        MsgReCallUtil.notifyHasDoWhileReply(this, "操作成功,撤回所有人的的最近白条消息", item);
                        return true;
                    } else if ("所有人".equals(arg1)) {
                        account = "0";

                        if (RemoteService.isIsInit()) {
                            int revokecount = 1;
                            revokeCount = ParseUtils.parseInt(arg2, revokeCount);
                            String s = RemoteService.revokeMsg(item.getFrienduin(), account, revokeCount, 0);
                            if (s != null) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "最近发言的人消息共" + revokeCount + "条,撤回结果:" + s, item);
                                return true;
                            }
                        }
                        MsgReCallUtil.notifyRevokeMsgJumpByCount(this, item.getFrienduin(), account, revokeCount, item);
                        MsgReCallUtil.notifyHasDoWhileReply(this, "操作成功,撤回最近发言人" + revokeCount + "条消息", item);
                        return true;
                    }
                    if ("全部".equals(arg1)) {
                        account = FloorUtils.getFloorQQ(item.getFrienduin());
                        fixArg = arg1;
                    } else if (TextUtils.isEmpty(arg1)) {

                        account = FloorUtils.getFloorQQ(item.getFrienduin());
                        fixArg = "";
                    } else if (RegexUtils.checkNoSignDigit(arg1)) {
                        if (FloorUtils.isFloorData(arg1)) {
                            account = FloorUtils.getQQByMsgItemFromValue1(item, arg1);
                        } else {
                            account = arg1;
                        }
                        fixArg = arg2;
                    } else {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "请指定要撤回的账号,而非其他字符 支持的参数\n[被艾特人 撤回总数/全部]\n[自己/机器人 撤回总数/全部]\n[楼层 撤回总数/全部]\n[QQ 撤回总数/全部]\n[所有消息（所有人近百条消息）]\n[所有人 消息总数]", item);
                        return true;

                    }


                }

                if (TextUtils.isEmpty(account)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "你真的想撤回吗?我都不知道撤回谁的消息呢", item);
                    return true;
                }

                String tip = "";


                if (fixArg.equals("all") || fixArg.equals("全部") || fixArg.contains("所有")) {
                    revokeCount = 500;
                    tip = "全部";
                } else if (RegexUtils.checkNoSignDigit(fixArg)) {//纯数字。
                    revokeCount = ParseUtils.parseInt(fixArg);
                    tip = "最近" + revokeCount + "条";
                } else {
                    if (!TextUtils.isEmpty(fixArg)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "无法完成您的撤回请求,参数'" + fixArg + "'不支持", item);
                        return true;

                    }
                }
                if (RemoteService.isIsInit()) {
                    String s = RemoteService.revokeMsg(item.getFrienduin(), account, revokeCount, item.getMessageID());
                    if (s != null) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "" + NickNameUtils.formatNickname(item.getFrienduin(), account) + "的" + tip + "消息撤回结果:" + s, item);
                        return true;
                    }
                }

                MsgReCallUtil.notifyRevokeMsgJumpByCount(this, item.getFrienduin(), account, revokeCount, item);
                MsgReCallUtil.notifyHasDoWhileReply(this, "操作成功,撤回" + NickNameUtils.formatNickname(item.getFrienduin(), account) + "的" + tip + "消息", item);

                return true;

            }
            case CmdConfig.ADD_MANAGER: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean, true)) {
                    return true;
                }

                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);

                if (TextUtils.isEmpty(arg1)) {


                    if (atPair != null && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {

//                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, "非管理员无权修改QQ" + atPair.second.second.get(0).getAccount() + "的名片!", item);
                        arg1 = atPair.second.second.get(0).getAccount();


                    } else {

                        MsgReCallUtil.notifyHasDoWhileReply(this, "要添加管理员请指定账号", item);
                        return true;
                    }


                } else if (RegexUtils.checkNoSignDigit(arg1)) {
                    arg1 = FloorUtils.getQQByMsgItemFromValue1(item, arg1);

                    if (TextUtils.isEmpty(arg1)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "无法锁定账号!", item);
                        return true;
                    }

                } else {

                    MsgReCallUtil.notifyHasDoWhileReply(this, "要添加管理员请指定账号,而非其他字符!", item);
                }


                AdminBean bean = new AdminBean();
                AdminBean accountMe = (AdminBean) AccountUtil.findAccount(mSuperManagers, item.getSenderuin(), false);
                int level = 0;
                if (accountMe == null) {
                    if (!item.getSenderuin().equals(Cns.DEFAULT_QQ_SMALL_ADMIN)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "无法查询你的管理员权限,因此不能撤销其他人管理", item);
                        return true;

                    } else {
                        level = -2;
                    }
                } else {
                    bean.setLevel(accountMe.getLevel() - 1);
                }

                bean.setLevel(level);
                bean.setAccount(arg1);
                long insert = DBHelper.getSuperManager(AppContext.getDbUtils()).insert(bean);
                if (insert > 0) {
                    mSuperManagers.add(bean);
                    MsgReCallUtil.notifyHasDoWhileReply(this, "操作成功,已授予" + NickNameUtils.formatNickname(item.getFrienduin(), arg1) + "管理员", item);
                    ;//回复的消息里面不能用命令词否则死循环下去
                } else {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "给" + NickNameUtils.formatNickname(item.getFrienduin(), arg1) + "添加管理员失败,可能已存在", item);
                    ;
                }
                return true;


            }

            case CmdConfig.DELETE_MANAGER: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean, true)) {
                    return true;
                }

                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);


                arg1 = FloorUtils.getQQByMsgItemFromValue1(item, arg1);


                if (TextUtils.isEmpty(arg1)) {


                    if (atPair != null && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {

//                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, "非管理员无权修改QQ" + atPair.second.second.get(0).getAccount() + "的名片!", item);
                        arg1 = atPair.second.second.get(0).getAccount();


                    } else {

                        MsgReCallUtil.notifyHasDoWhileReply(this, "要添加管理员请指定账号", item);
                    }

                    return true;

                } else {

                    if (!RegexUtils.checkNoSignDigit(arg1)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "必须添加数字", item);
                        return true;

                    }

                }


                AdminBean accountDelte = (AdminBean) AccountUtil.findAccount(mSuperManagers, arg1, false);
                if (accountDelte == null) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "无法删除管理员" + NickNameUtils.formatNickname(item.getFrienduin(), arg1) + ",因为数据库中并不存在", item);
                    return true;
                }

                AdminBean accountMe = (AdminBean) AccountUtil.findAccount(mSuperManagers, item.getSenderuin(), false);
                if (accountMe == null && !item.getSenderuin().equals(Cns.DEFAULT_QQ_SMALL_ADMIN)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "无法查询你的管理员权限,因此不能撤销其他人管理", item);
                    return true;
                }
                if (item.getSenderuin().equals(accountDelte.getAccount())) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, NickNameUtils.formatNickname(item) + " 你无法撤销自己的管理员权限", item);

                }

                if (!isManagerLestThanOrEqalMe(item, accountMe, accountDelte)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + NickNameUtils.formatNickname(item) + " 很抱歉,你无法撤销管理" + NickNameUtils.formatNickname(item.getFrienduin(), accountDelte.getAccount()) + ",他的等级和你相当或更高,你的权限等级是" + accountMe.getLevel() + ",他的权限等级是:" + accountDelte.getLevel() + "(机器人自身管理权限最大)", item)
                    ;
                    return true;
                }
                int i = DBHelper.getSuperManager(AppContext.getDbUtils()).deleteByColumn(AdminBean.class, FieldCns.FIELD_ACCOUNT, arg1);
                if (i > 0) {
                    AccountUtil.removeAccount(mSuperManagers, arg1);
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请求删除管理员" + NickNameUtils.formatNickname(item.getFrienduin(), arg1) + "成功", item)
                    ;

                } else {

                    MsgReCallUtil.notifyHasDoWhileReply(this, "请求删除管理员" + NickNameUtils.formatNickname(item.getFrienduin(), arg1) + "失败,不存在或已经删除", item);
                    ;
                }
                return true;
            }

//            case CmdConfig.SUPER_MAANGER:

            case CmdConfig.MAANGER_ALL: {
                if (args.length > 1 && isSelf) {//避免词库触发.
                    return false;
                }


                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }

                StringBuffer sb = new StringBuffer();
                sb.append("[所有管理员信息]\n超级管理员:\n");
                for (AccountBean bean : mSuperManagers) {
                    sb.append(bean.getAccount() + ":");
                    sb.append((bean.isDisable() ? "[禁用]" : "[生效]") + "\n");
                }

                if (isgroupMsg && nameBean != null) {
                    sb.append("本群管理员(数据库):\n");
                    if (nameBean.getAdmins() != null) {
                        String[] split = nameBean.getAdmins().split(",");
                        if (split.length > 0) {
                            for (String s : split) {
                                sb.append(s + ",昵称:" + NickNameUtils.queryMatchNickname(nameBean.getAccount(), s, true) + "\n");
                            }

                        } else {

                            sb.append("无\n");
                        }
                    } else {
                        sb.append("无\n");
                    }
                }
                if (isgroupMsg && nameBean != null) {
                    sb.append("本群管理员(根据群管理名单反查):\n");

                    List<GroupAdaminBean> groupAdaminBeans = DBHelper.getGroupAdminTableUtil(_dbUtils).queryAllByFieldLike(GroupAdaminBean.class, FieldCns.FIELD_GROUP, "" + nameBean.getAccount());
                    if (groupAdaminBeans != null && groupAdaminBeans.size() > 0) {
                        for (int i = 0; i < groupAdaminBeans.size(); i++) {
                            String account = groupAdaminBeans.get(i).getAccount();
                            sb.append(account + ",昵称:" + NickNameUtils.queryMatchNickname(nameBean.getAccount(), account, true) + "\n");
                        }
                    } else {
                        sb.append("无\n");
                    }

                }
                sb.append("本群管理员(真实):\n");
                if (isgroupMsg && nameBean != null && RemoteService.isIsInit()) {
                    String troopowneruin = RemoteService.queryGroupField(nameBean.getAccount(), "troopowneruin");
                    if (troopowneruin != null) {
                        sb.append(troopowneruin + ",昵称:" + NickNameUtils.queryMatchNickname(nameBean.getAccount(), troopowneruin, true) + "\n");
                    }
                    String administrator = RemoteService.queryGroupField(nameBean.getAccount(), "Administrator");
                    if (administrator != null) {
                        String[] split = administrator.split("\\|");//ignore_include
                        if (split.length > 0) {
                            for (String s : split) {
                                sb.append(s + ",昵称:" + NickNameUtils.queryMatchNickname(nameBean.getAccount(), s, true) + "\n");
                            }
                        }
                    }
                    sb.append("\n");

                } else {
                    sb.append("需要绑定机器人服务方可查询\n");
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);

                return true;
            }
            case CmdConfig.SUPER_MAANGER_CMD1: {


                if (args.length > 1 && isSelf) {//避免词库触发.
                    return false;
                }

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }

                StringBuffer sb = new StringBuffer();
                sb.append("[超级管理员]\n");
                for (AccountBean bean : mSuperManagers) {
                    sb.append(bean.getAccount() + ":");
                    sb.append((bean.isDisable() ? "[禁用]" : "[生效]") + "\n");
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);


            }

            break;

            case CmdConfig.ADD_CURRENT_GROUP_MAANAGER: {


                if (args.length > 1 && isSelf) {//避免词库触发.
                    return false;
                }

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                if (!isgroupMsg) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, Cns.TIP_PLEASE_GROUP_CHAT, item);
                    return true;


                }
                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(arg1)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请填写要添加的管理员", item);
                    return true;
                }

                if (!RegexUtils.iseQQ(arg1)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请填写正确的QQ", item);
                    return true;
                }
                String admins = nameBean.getAdmins();
                boolean containByArray = AccountUtil.isContainByArray(arg1, ",", admins);
                if (containByArray) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,管理员" + NickNameUtils.queryMatchNickname(item.getFrienduin(), arg1, false) + "已存在!", item);
                    return true;
                } else {
                    nameBean.setAdmins(AccountUtil.addValueByArray(arg1, ",", admins));
                    int update = DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).update(nameBean);
                    MsgReCallUtil.notifyHasDoWhileReply(this, "操作结果:" + ((update > 0) ? "成功" : "失败"), item)
                    ;
                    return true;
                }

            }


            case CmdConfig.REMOVE_CURRENT_GROUP_MAANAGER: {


                if (args.length > 1 && isSelf) {//避免词库触发.
                    return false;
                }

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                if (!isgroupMsg) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, Cns.TIP_PLEASE_GROUP_CHAT, item);
                    return true;


                }
                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(arg1)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请填写要添加的管理员", item);
                    return true;
                }

                if (!RegexUtils.iseQQ(arg1)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请填写正确的QQ", item);
                    return true;
                }
                String admins = nameBean.getAdmins();
                boolean containByArray = AccountUtil.isContainByArray(arg1, ",", admins);
                if (!containByArray) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,管理员" + NickNameUtils.queryMatchNickname(item.getFrienduin(), arg1, false) + "不存在!", item);
                    return true;
                } else {
                    nameBean.setAdmins(AccountUtil.removeValueByArray(arg1, ",", admins));
                    int update = DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).update(nameBean);
                    MsgReCallUtil.notifyHasDoWhileReply(this, "操作结果:" + ((update > 0) ? "成功" : "失败"), item);
                    return true;
                }

            }

            case CmdConfig.TEST_ACCESS_URL: {
                if (!isManager) {
                    return false;
                }
                String arg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);

                if (TextUtils.isEmpty(arg1)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请填写地址", item);
                } else {
                    HttpUtilOld.queryData(arg1, new RequestListener() {
                        @Override
                        public void onSuccess(String str) {

                            if (str.length() < 150) {

                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "打开成功:" + str, item);
                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "打开成功,前50/" + str.length() + "个字符串:" + str.substring(0, 150), item);

                            }
                        }

                        @Override
                        public void onFail(String str) {
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "无法打开:" + str, item);
                        }
                    });
                }

            }
            return true;
            case CmdConfig.FECTCH_MUSIC:
            case CmdConfig.FECTCH_MUSIC1:
            case CmdConfig.FECTCH_MUSIC2: {


                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowmusic()) {
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;

                        }
                        return false;
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理
                        }
                    }
                }

                MusicMoudle.onReceiveMusic(this, item, argStr, atPair, args, isManager);


            }
            return true;

            case CmdConfig.SEARCH_2:

            case CmdConfig.SEARCH_1:
            case CmdConfig.SEARCH: {
                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowsearchpic()) {
                        LogUtil.writeLog("搜图片功能被禁用");
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;

                        }
                        if (!isManager) {
                            return false;

                        }
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }


                    if (isgroupMsg && nameBean != null) {
                        boolean isCurrentGroupAdmin = isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                        nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                    }
                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("694")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }

                String text = ParamParseUtil.mergeParameters(args, ParamParseUtil.sArgFirst);
                if (CmdConfig.SEARCH_2.equals(commend)) {
                    SearchPluginMainImpl.doSendCacheDir(item, text);
                    return true;

                }
                if (TextUtils.isEmpty(text)) {
                    MsgReCallUtil.smartReplyMsg("请输入要搜索的图,输入" + CmdConfig.SEARCH_2 + "则随机取出本地已经下载好的图片", isgroupMsg, nameBean, item);
                } else {

                    SearchPluginMainImpl.doSearchPicLogic(item, text);
                }


            }
            return true;

            case CmdConfig.TRANSLATE: {
                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowTranslate()) {
                        LogUtil.writeLog("翻译功能被禁用");
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;
                        }
                        if (!isManager) {
                            return false;

                        }
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }
                    if (isgroupMsg && nameBean != null) {
                        boolean isCurrentGroupAdmin = isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                        nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                    }
                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。
                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("694")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }


                String text = ParamParseUtil.mergeParameters(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(text)) {
                    MsgReCallUtil.smartReplyMsg("请输入要翻译的内容", isgroupMsg, nameBean, item);
                } else {
                    BaseQueryImpl.getInstance(TranslateQueryImpl.class).doAction(item, isgroupMsg, nameBean, args, atPair, text);
                }


            }
            return true;
            case CmdConfig.CARD_MSG: {
                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowGenerateCardMsg()) {
                        LogUtil.writeLog("卡片功能被禁用");
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;

                        }
                        if (!isManager) {
                            return false;

                        }

                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("694")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }


                String text = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);

                doCardLogic(item, args, text, 0);


            }
            return true;
            case CmdConfig.TEXT_2PIC:
            case CmdConfig.TEXT_2PIC_1: {


                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowtext2pic()) {
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;

                        }
                        LogUtil.writeLog("文字转图片功能被禁用");
                        if (!isManager) {
                            return false;

                        }
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("694")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }


                String fontColor = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String bgColor = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);

                int megreePosition = ParamParseUtil.sArgFirst;
                if (bgColor != null && bgColor.startsWith("#")) {
                    megreePosition = ParamParseUtil.sArgThrid;
                } else if (fontColor != null && fontColor.startsWith("#")) {
                    megreePosition = ParamParseUtil.sArgSecond;
                }


                String text = ParamParseUtil.mergeParameters(args, megreePosition);


                if (TextUtils.isEmpty(text)) {
                    MsgReCallUtil.smartReplyMsg("请输入内容,多个用换行隔开,如果包含要修改文字文字颜色和背景颜色，请在先传递16进制颜色参数,如输入[" + CmdConfig.TEXT_2PIC + "#ff0000 #000000 问世间情为何物 直教人身死相许]其中背景颜色也可以单独省略，即默认背景色白色。", isgroupMsg, nameBean, item);
                } else {
                    SearchPluginMainImpl.doText2PicLogic(item, fontColor, bgColor, text);
                }


            }

            return true;
            case CmdConfig.QRCODE:
            case CmdConfig.QRCODE_1: {


                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowqrcode()) {
                        if (StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                            return true;

                        }
                        if (!isManager) {
                            return false;

                        }
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
                if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("350")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }
           /*     String qrcodeColor = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String bgColor = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);


                int megreePosition = ParamParseUtil.sArgFirst;
                if (bgColor != null && bgColor.startsWith("#")) {
                    megreePosition = ParamParseUtil.sArgThrid;
                } else if (qrcodeColor != null && qrcodeColor.startsWith("#")) {
                    megreePosition = ParamParseUtil.sArgSecond;
                }


                String text =null;
                if(qrcodeColor!=null&&bgColor!=null){
                    text=    ParamParseUtil.mergeParameters(args, megreePosition);
                }else{
                    qrcodeColor="#ff0000";
                    bgColor="#ffffff";
                    text=ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                }
            */
                String text = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(text)) {
                    MsgReCallUtil.smartReplyMsg("请输入内容方可转换二维码", isgroupMsg, nameBean, item);
                } else {

                    new QssqTaskFix<Void, Void>(new QssqTaskFix.ICallBackImp() {
                        @Override
                        public Object onRunBackgroundThread(Object[] params) {
                            try {
                                String path = ZxingUtil.bitmap2qrcodeFile(text);
                                return path;

                            } catch (Throwable e) {
                                return e;
                            }
                        }

                        @Override
                        public void onRunFinish(Object o) {
                            if (o instanceof Throwable) {

                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "无法生成" + o, item);
                            } else {

                                MsgReCallUtil.notifySendPicMsg(RobotContentProvider.getInstance(), o + "", item);
                            }

                        }
                    }).execute();
                }

            }

            return true;

            case CmdConfig.ADD_LIKE: {
                boolean isCurrentGroupAdmin = false;

                if (isgroupMsg && nameBean != null) {
                    isCurrentGroupAdmin = isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                    nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                }
                if (isgroupMsg && nameBean != null) {
                    if (!nameBean.isAllowzan()) {
                        if (!isManager && StringUtils.isEqualStr(beforeArg, commend)) {
                            MsgReCallUtil.smartReplyMsg(
                                    AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);

                            return true;

                        }


                        LogUtil.writeLog("点赞功能被禁用");
                        if (!isManager && !isCurrentGroupAdmin) {
                            return false;

                        }
                    } else {

                    }
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {

                        return true;
                    }
                }
        /*        if (atPair != null && atPair.first && atPair.second.first == false) {
                    List<GroupAtBean> atBeans = atPair.second.second;
                    for (GroupAtBean atBean : atBeans) {

                        if (isManager(atBean.getAccount())) {
                            if (atBean.getAccount().startsWith("694")) {

                            } else {
                                return true;//艾特了别人 而且是管理员，如果2个都是机器人咋办,所以不处理

                            }
                        }
                    }
                }*/


                String argZan = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String qq;
                int count = 1;


                if (atPair != null && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {
                    if (!isManager && !isCurrentGroupAdmin) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "非管理员不允许赞其他人QQ", item);
                        return true;
                    }
                    if (atPair.second.second.size() == 1) {
                        qq = atPair.second.second.get(0).getAccount();//只允许赞一个，
                        if (argZan != null && RegexUtils.checkNoSignDigit(argZan)) {
                            count = ParseUtils.parseInt(argZan);

                        }

                    } else {


                        count = ParseUtils.parseInt(ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst), 1);

                        final int finalCount = count;
                        FloorMultiUtils.doMultiFloorEachLogicFromAt(this, item, atPair.second.second, item.getFrienduin(), new FloorMultiUtils.IMultiEachCallBack<MsgItem>() {
                            @Override
                            public boolean onEachDoAndisIgnore(MsgItem bean) {

                                if (MsgTyeUtils.isSelfMsg(bean)) {
                                    return true;
                                }
                                MsgReCallUtil.notifyZanPerson(RobotContentProvider.getInstance(), bean, bean.getSenderuin(), finalCount);
                                return false;
                            }

                            @Override
                            public void onEnd(List<AtBean> atBeanList, String info) {

                                info = info + " \n管理员" + NickNameUtils.queryMatchNicknameAndNullReturnDefault(item.getFrienduin(), item.getSenderuin(), item.getNickname()) + "给你们点了" + finalCount + "个赞，请注意查收哦!";
                                if (ConfigUtils.isDisableAtFunction(RobotContentProvider.getInstance())) {
                                    MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(RobotContentProvider.getInstance(), info, item);
                                } else {

                                    MsgReCallUtil.notifyAtMsgJumpB(RobotContentProvider.getInstance(), info, atBeanList, item);
                                }

                            }

                            @Override
                            public void onFailEnd() {
                                MsgReCallUtil.smartReplyMsg("抱歉，无法点赞,Nobody!!!!没有符要求的数据!", isgroupMsg, nameBean, item);
                            }

                        });

                        return true;

                    }


                } else {

                    if (TextUtils.isEmpty(argZan)) {

                        if (isgroupMsg) {
                            qq = isSelf ? FloorUtils.getFloorQQ(item.getFrienduin(), 1) : item.getSenderuin();

                        } else {

                            if (isSelf) {
                                MsgReCallUtil.smartReplyMsg("请指定要点赞的人", isgroupMsg, nameBean, item);
                                return true;
                            } else {
                                qq = item.getSenderuin();

                            }
                        }
                    } else if (RegexUtils.checkNoSignDigit(argZan)) {
                        int value = ParseUtils.parseInt(argZan);
                        if (value <= 20) {
                            count = value;
                            qq = item.getSenderuin();
                        } else {

                            if (!isManager && !isCurrentGroupAdmin) {
                                MsgReCallUtil.smartReplyMsg("非管理员不允许操作大于20个赞,或赞其他人QQ", isgroupMsg, nameBean, item);
                                return true;
                            } else {

                                if (argZan.length() > 4) {
                                    qq = argZan;

                                    count = ParseUtils.parseInt(ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond), 1);

                                } else {


                                    MsgReCallUtil.smartReplyMsg("最多允许赞20次!", isgroupMsg, nameBean, item);
                                    return true;
                                }

                            }


                        }


                    } else {

                        if ((isManager || isCurrentGroupAdmin) && isgroupMsg) {
                            Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(argZan);
                            if (pair != null) {
                                List<MsgItem> floors = FloorUtils.getFloors(item.getFrienduin(), pair.first, pair.second);

                                if (floors == null) {
                                    MsgReCallUtil.smartReplyMsg("楼层数据有误!!", isgroupMsg, nameBean, item);
                                    return false;
                                } else {
                                    final int finalCount = ParseUtils.parseInt(ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond), 1);
                                    FloorMultiUtils.doMultiFloorEachLogic(this, item, floors, item.getFrienduin(), new FloorMultiUtils.IMultiEachCallBack<MsgItem>() {
                                        @Override
                                        public boolean onEachDoAndisIgnore(MsgItem bean) {

                                            if (MsgTyeUtils.isSelfMsg(bean)) {
                                                return true;
                                            }
                                            MsgReCallUtil.notifyZanPerson(RobotContentProvider.getInstance(), bean, bean.getSenderuin(), finalCount);
                                            return false;
                                        }

                                        @Override
                                        public void onEnd(List<AtBean> atBeanList, String info) {


                                            info = info + " \n管理员" + NickNameUtils.queryMatchNicknameAndNullReturnDefault(item.getFrienduin(), item.getSenderuin(), item.getNickname()) +
                                                    "给你们点了" + finalCount + "个赞,请注意查收哦!";

                                            if (ConfigUtils.isDisableAtFunction(RobotContentProvider.getInstance())) {
                                                MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(RobotContentProvider.getInstance(), info, item);
                                            } else {

                                                MsgReCallUtil.notifyAtMsgJumpB(RobotContentProvider.getInstance(), info, atBeanList, item);
                                            }

                                        }

                                        @Override
                                        public void onFailEnd() {
                                            MsgReCallUtil.smartReplyMsg("抱歉，无法点赞,Nobody!!!!没有符要求的数据!", isgroupMsg, nameBean, item);
                                        }
                                    });
                                    return true;

                                }
                            }
                        }
                        MsgReCallUtil.smartReplyMsg("你输入的点赞参数不合法!!", isgroupMsg, nameBean, item);
                        return true;
                    }


                }

                if ((count < 0 || count > 20) && !isManager && !isCurrentGroupAdmin) {
                    count = 1;
                }


                boolean hasVote = false;
                String msg = "";
                if (RemoteService.isIsInit()) {
                    String s = RemoteService.addLike(count, new String[]{item.getSelfuin(), qq});
                    if (s != null) {

                        com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                        int code = object.getIntValue("code");
                        msg = object.getString("msg");
                        if (code != 0) {
                            MsgReCallUtil.smartReplyMsg(" \n" + msg, isgroupMsg, nameBean, item);
                            return true;

                        } else {
                            msg = "{" + msg + "}";
                        }
                        hasVote = true;

                    } else {

                    }

                }

                if (!hasVote) {
                    MsgReCallUtil.notifyZanPerson(this, item.clone(), qq, count);

                }
                String s = NickNameUtils.queryMatchNickname(item.getFrienduin(), qq, false);
                if (qq.equals(item.getSenderuin())) {
                    MsgReCallUtil.smartReplyMsg(" 给你" + count + "个赞!请查收!\n" + msg, isgroupMsg, nameBean, item);

                } else {
                    MsgReCallUtil.smartReplyMsg("给" + s + " 打赏了" + count + "个赞!\n" + msg, isgroupMsg, nameBean, item);

                }


            }

            return true;
            case CmdConfig.MODIFY_CARD_NAME:
            case CmdConfig.MODIFY_CARD_NAME1:
            case CmdConfig.MODIFY_CARD_NAME2:
            case CmdConfig.MODIFY_CARD_NAME3: {
                if (!nameBean.isAllowModifyCard()) {
                    LogUtil.writeLog(commend + "功能被禁用");


                    if (StringUtils.isEqualStr(beforeArg, commend)) {
                        MsgReCallUtil.smartReplyMsg(AppConstants.ACTION_OPERA_TIP + commend + AppConstants.FUNC_IS_DISABLE_TIP, isgroupMsg, nameBean, item);
                        return true;

                    }

                    return false;
                } else {

                }


                if (isgroupMsg && nameBean != null) {
                } else {
                    if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
                        return true;
                    }

                    if (isNeedIgnoreNormalCommand(item, atPair, flag, true, isSelf, isgroupMsg, nameBean)) {
                        if (ConfigUtils.IsNeedAt(nameBean) && atPair.second.first == false) {//允许自己和别人艾特 . 但是艾特2个都不是机器人自己就。

                            return false;
                        }
                    }
                }

                if (nameBean != null && nameBean.isNeedaite()) {
                    if (nameBean.isSelfcmdnotneedaite() || atPair.second.first) {
                        //如果自身不需要艾特那么可以点歌，或者呢如果已经艾特了也可以点歌。
                    } else {


                        return true;
                    }
                }
                if (!isgroupMsg) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "不支持私聊修改群名片!", item);
                    return true;
                }


                String modifyName = "";
                String mondifyQQ = "";
                String beforeNickname = "";

                String arg1Str = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(arg1Str)) {
                    MsgReCallUtil.smartReplyMsg("请输入如[要改的名字 |qq 要改的名字]", isgroupMsg, nameBean, item);
                    return true;

                } else {


                    String secondArg = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                    if (TextUtils.isEmpty(secondArg) || atPair != null) {


                        if (atPair != null && atPair.second != null && atPair.second.second != null && atPair.second.second.size() > 0) {

                            if (!isManager) {
                                MsgReCallUtil.notifyJoinReplaceMsgJump(this, "非管理员无权修改QQ" + atPair.second.second.get(0).getAccount() + "的名片!", item);
                                return true;
                            } else {
                                if (atPair.second.second.size() > 1) {
                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "最多只能艾特一个人进行修改!", item);
                                    return true;

                                } else {
                                    GroupAtBean groupAtBean = atPair.second.second.get(0);
                                    mondifyQQ = groupAtBean.getAccount();
                                    beforeNickname = groupAtBean.getNickname();
                                    modifyName = ParamParseUtil.mergeParameters(args, ParamParseUtil.sArgFirst);
                                }
                            }


                        } else {


                            beforeNickname = item.getNickname();
                            mondifyQQ = item.getSenderuin();
                            modifyName = arg1Str;


                        }

                    } else {
                        if (!isManager) {

                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, "非管理员无权修改他人名片!", item);
                            return true;
                        }
                        mondifyQQ = arg1Str;

                        if (!RegexUtils.checkDigit(mondifyQQ)) {

                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, mondifyQQ + "不是数字账号！", item);
                            return true;
                        } else if (FloorUtils.isFloorData(mondifyQQ)) {//允许空参数
                            String floorQQ = FloorUtils.getFloorQQ(item.getFrienduin(), mondifyQQ);
                            if (floorQQ != null) {
                                mondifyQQ = floorQQ;

                            } else {
                                MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.getFloorInputDataInValidMsg(mondifyQQ), item);
                                return true;
                            }

                        }


                        modifyName = secondArg;
                        beforeNickname = NickNameUtils.queryMatchNickname(item.getFrienduin(), mondifyQQ);

                    }


                    if (!isManager && nameBean != null && nameBean.getGroupnickanmekeyword() != null) {

                        try {

                            boolean matches = item.getNickname().matches(nameBean.getGroupnickanmekeyword());
                        } catch (PatternSyntaxException e) {

                            MsgReCallUtil.notifyJoinReplaceMsgJump(this, "无法修改，因为名片格式不正确!", item);
                            return true;
                        }
                    }

                    if (!item.getSenderuin().equals(mondifyQQ)) {
                        AdminBean accountMe = (AdminBean) AccountUtil.findAccount(mSuperManagers, item.getSenderuin(), false);
                        AdminBean accountHe = (AdminBean) AccountUtil.findAccount(mSuperManagers, mondifyQQ, false);

                        if (accountHe != null && accountMe != null) {

                            if (accountMe.getLevel() <= accountHe.getLevel()) {

                                MsgReCallUtil.notifyJoinReplaceMsgJump(this, "无法修改管理员" + mondifyQQ + "的名片,因为他的权限等级比你大!", item);
                                return true;
                            }
                        }

                    }


                    MsgItem clone = item.clone();
                    clone.setSenderuin(mondifyQQ);


                    MsgReCallUtil.notifyRequestModifyName(this, clone, modifyName);
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME + "修改群名片\n" + "[账号]" + mondifyQQ + "\n[曾经名片]" + beforeNickname + "\n[当前名片]" + modifyName, item);

                }


            }

            return true;

            case CmdConfig.LIST_QQ_IGNORES: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }

                if (!isManager) {
                    MsgReCallUtil.notifyNotManagerMsg(this, item);
                    return true;
                }
                StringBuffer sb = new StringBuffer();
                sb.append("当前忽略的QQ:\n");
                for (AccountBean mIgnoreQQ : mIgnoreQQs) {

                    sb.append(NickNameUtils.formatNickname(mIgnoreQQ.getAccount(), mIgnoreQQ.getAccount()) + ":");
                    sb.append((mIgnoreQQ.isDisable() ? "[禁用]" : "[生效]") + "\n");
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);
            }
            return true;
            case CmdConfig.IGNORE_TEMP_IGNORE_ME: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String arg1RobotQQ = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String qqSenderUin = getCurrentArgAndAfter(args, ParamParseUtil.sArgSecond);
                qqSenderUin = TextUtils.isEmpty(qqSenderUin) ? item.getSenderuin() : qqSenderUin;
                mTempIGgnoresManager = qqSenderUin;
                String nickname = item.getSenderuin().equals(qqSenderUin) ? item.getNickname() : qqSenderUin + "";

                if (TextUtils.isEmpty(arg1RobotQQ)) {
                    arg1RobotQQ = item.getSelfuin();
                }
                if (item.getSelfuin().equals(arg1RobotQQ) || item.getNickname().equals(arg1RobotQQ)) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "本机器人将临时忽略" + nickname + "的命令,直至输入" + CmdConfig.IGNORE_TEMP_IGNORE_ME_DISABLE, item);
                } else {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "本机器人无法无视" + nickname + "的命令(如果艾特其他机器人发送管理命令也可以自动忽略命令),因为您输入的非机器人QQ，也就是不是我的QQ,请输入如:" + CmdConfig.IGNORE_TEMP_IGNORE_ME + "" + item.getSelfuin() + " " + item.getSenderuin() + ",如果是无视发送命令这，后自己的QQ可以不填写", item);
                }
            }
            return true;
            case CmdConfig.CLEAR_TASK: {
                if (flag < INeedReplayLevel.ANY) {
                    return false;
                }
                if (!isManager) {
                    MsgReCallUtil.notifyNotManagerMsg(this, item);
                    return true;
                }
                String first = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);

                if (TextUtils.isEmpty(first)) {
                    int i = TaskUtils.clrearAllTask();
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "已清除所有群所有命令,总数:" + i, item);
                } else {
                    int count = TaskUtils.clrearTaskByTitleAndMatchAllKey(first);
                    if (count <= 0) {
                        count = TaskUtils.clrearTaskByTitleAndFetchCount(first);
                    }
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "已清除匹配" + first + "的QQ 群号 任务关键词所有任务,清除总数:" + count, item);
                }

            }

            return true;
            case CmdConfig.IGNORE_TEMP_IGNORE_ME_DISABLE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String temp = mTempIGgnoresManager;
                if (temp == null) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "并没有管理的命令被无视", item);
                } else {

                    mTempIGgnoresManager = null;
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "已解除针对管理" + NickNameUtils.formatNickname(temp, temp) + "的无视模式", item);
                }
            }
            break;
            case CmdConfig.IGNORE_PINGBI:


                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                if (isgroupMsg) {
                    if (args.length > 0) {
                        return false;
                    }
                    MemoryIGnoreConfig.addIgnoreGroupNo(item.getFrienduin(), item);
                    String msg = "群" + item.getFrienduin() + "已屏蔽 本次有效";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                } else {
                    String account = args.length > 0 ? args[0] : item.getFrienduin();
                    if (RegexUtils.checkIsContainNumber(account)) {
                        return false;
                    }

                    MemoryIGnoreConfig.addIgnoreGroupNo(account, item);
                    String msg = "QQ_IGNORES" + account + "已屏蔽 本次有效";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                }
                break;


            case CmdConfig.ADD_GAG: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                //           GagAccountBean object = new GagAccountBean(keyWord, duration, silence, action);
                String ask = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String answer = getCurrentArgAndAfter(args, ParamParseUtil.sArgSecond);
                // return DBHelper.getGagKeyWord(AppContext.dbUtils).insert(accountBean);
                if (ask == null) {

                    String msg = "无法添加敏感词,问与答之间用空格隔开哦!";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else if (answer == null) {


                    String msg = "无法添加敏感词,不能识别禁言时间";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else {

                    List<GagAccountBean> list = DBHelper.getGagKeyWord(AppContext.getDbUtils()).queryAllIsDesc(GagAccountBean.class, true, FieldCns.FIELD_ACCOUNT);
                    for (GagAccountBean accountBean : list) {
                        if (accountBean.getAccount().contains(ask)) {
                            String msg = "重复添加|" + accountBean.getAccount();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                            return true;
                        }
                    }
                    long l = ParseUtils.parseGagStr2Secound(answer);
                    GagAccountBean object = new GagAccountBean(ask, l, false, GAGTYPE.GAG);
                    long result = DBHelper.getGagKeyWord(AppContext.dbUtils).insert(object);
                    String msg = "";
                    if (result > 0) {
                        object.setId((int) result);
                        doInsertNewGagBean(object);
                        msg = String.format(AppConstants.ACTION_OPERA_NAME + "添加成功\n敏感词:%s\n禁言时间:%s", ask, answer);
                    } else {
                        msg = AppConstants.ACTION_OPERA_NAME + "添加败,数据库错误 inser effect count:" + result;
                    }
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                }

            }
            break;
            case CmdConfig.DEL_GAG: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String ask = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                // return DBHelper.getGagKeyWord(AppContext.dbUtils).insert(accountBean);
                if (ask == null) {

                    String msg = "无法删除敏感词!";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else {

                    List<GagAccountBean> list = DBHelper.getGagKeyWord(AppContext.getDbUtils()).queryAllIsDesc(GagAccountBean.class, true, FieldCns.FIELD_ACCOUNT);
                    ListIterator<GagAccountBean> gagAccountBeanListIterator = list.listIterator();
                    StringBuilder sb = new StringBuilder();
                    sb.append("删除" + ask + "|");
                    boolean find = false;
                    while (gagAccountBeanListIterator.hasNext()) {
                        GagAccountBean accountBean = gagAccountBeanListIterator.next();
                        if (accountBean.getAccount().contains(ask)) {
                            find = true;
                            if (accountBean.getAccount().equals(ask)) {

                                long result = DBHelper.getGagKeyWord(AppContext.dbUtils).deleteById(GagAccountBean.class, accountBean.getId());
                                String msg = "";
                                sb.append("删除相等关键词,id:" + accountBean.getId() + ",禁言时间:" + DateUtils.getGagTime(accountBean.getDuration()));
                                if (result > 0) {
                                    gagAccountBeanListIterator.remove();
                                    msg = "成功";
                                } else {
                                    msg = "失败,status:" + result;
                                }
                                sb.append(msg);
                            } else {

                                String replace = accountBean.getAccount().replace(ClearUtil.wordSplit + ask, "");
                                replace = accountBean.getAccount().replace(ask + ClearUtil.wordSplit, "");
                                replace = accountBean.getAccount().replace(ClearUtil.wordSplit + ClearUtil.wordSplit, ClearUtil.wordSplit);
                                sb.append(",从多条分歌词里面移除关键词" + ask + ",id:" + accountBean.getId() + ",禁言时间:" + DateUtils.getGagTime(accountBean.getDuration()) + ",移除后的词条内容:" + replace);

                                accountBean.setAccount(replace);
                                String msg;
                                long result = DBHelper.getGagKeyWord(AppContext.dbUtils).update(accountBean);
                                if (result > 0) {
//                                    gagAccountBeanListIterator.remove();
                                    msg = "";
                                } else {
                                    msg = "额,移除失败,status:" + result;
                                }
                                sb.append(msg);

                            }


                        }
                    }
                    if (!find) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "没找到相关敏感词" + ask, item);

                    } else {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);

                    }

                }

            }
            break;
            case CmdConfig.ADB_AUTH: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.ADB_AUTH, "");
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请从电脑中提取adbkey.pub内容然后作为参数传递", item);
                    return true;
                }
                value = StringUtils.replaceAllByStr(value, "斜杠", "/");
                {
                    String finalCmd = value.trim();
                    long start = System.currentTimeMillis();
                    StringBuffer sb = new StringBuffer();
                    new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                        @Override
                        public String onRunBackgroundThread(StringBuffer[] params) {
                            StringBuffer sb = params[0];
                            boolean[] waiting = {true, true};
                            Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo " + finalCmd + ">>/data/misc/adb/adb_keys"}, new ICmdIntercept<String>() {
                                //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                                @Override
                                public boolean isNeedIntercept(String bean) {
                                    sb.append(bean);
                                    return false;
                                }

                                @Override
                                public void onComplete(String name) {
                                    if (name != null && name.contains("错误")) {

                                        waiting[0] = false;
                                    } else {
                                        waiting[1] = false;

                                    }
                                    long end = System.currentTimeMillis();

                                    sb.append("[" + name + "]" + "执行[耗时" + (end - start) + "ms]\n");

                                }
                            });

                            try {
                                long waitTime = 0;
                                while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                    waitTime += 10;
                                    Thread.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sb.insert(0, stringExceptionPair.first);
                            return sb.toString();

                        }

                        @Override
                        public void onRunFinish(String o) {
                            if (o != null) {
                                item.setMessage(o);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                            }

                        }
                    }).execute(sb);
                    return true;
                }
            }
            case CmdConfig.WRITE_FILE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.WRITE_FILE, "");
                int douHaoIndex = value.indexOf(",");
                if (TextUtils.isEmpty(value) || douHaoIndex < 1) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递要写入的文件以及内容,用逗号分割", item);
                    return true;
                }
                String file = value.substring(0, douHaoIndex);
                String writeContent = value.substring(douHaoIndex + 1);
                writeContent = StringUtils.replaceAllByStr(writeContent, "斜杠", "/");
                {
                    String finalCmd = writeContent.trim();
                    long start = System.currentTimeMillis();
                    StringBuffer sb = new StringBuffer();
                    sb.append("文件路径:" + file);
                    sb.append("写入内容:" + writeContent);
                    new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                        @Override
                        public String onRunBackgroundThread(StringBuffer[] params) {
                            StringBuffer sb = params[0];
                            boolean[] waiting = {true, true};
                            Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo " + finalCmd + ">>" + file}, new ICmdIntercept<String>() {
                                //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                                @Override
                                public boolean isNeedIntercept(String bean) {
                                    sb.append(bean);
                                    return false;
                                }

                                @Override
                                public void onComplete(String name) {
                                    if (name != null && name.contains("错误")) {

                                        waiting[0] = false;
                                    } else {
                                        waiting[1] = false;

                                    }
                                    long end = System.currentTimeMillis();

                                    sb.append("[" + name + "]" + "执行[耗时" + (end - start) + "ms]\n");

                                }
                            });

                            try {
                                long waitTime = 0;
                                while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                    waitTime += 10;
                                    Thread.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sb.insert(0, stringExceptionPair.first);
                            return sb.toString();

                        }

                        @Override
                        public void onRunFinish(String o) {
                            if (o != null) {
                                item.setMessage(o);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                            }

                        }
                    }).execute(sb);
                    return true;
                }
            }
            case CmdConfig.TOUCH_OPERA: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.TOUCH_OPERA, "");

                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "支持的操作如下:\nkeyevent 66\ntap 1 1\ntext aa\nswipe 100 100 100 100 1000", item);
                    return true;
                }

                {
                    String finalCmd = value.trim();
                    long start = System.currentTimeMillis();
                    StringBuffer sb = new StringBuffer();
                    new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                        @Override
                        public String onRunBackgroundThread(StringBuffer[] params) {
                            StringBuffer sb = params[0];
                            boolean[] waiting = {true, true};
                            Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"input " + finalCmd}, new ICmdIntercept<String>() {
                                //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                                @Override
                                public boolean isNeedIntercept(String bean) {
                                    sb.append(bean);
                                    return false;
                                }

                                @Override
                                public void onComplete(String name) {
                                    if (name != null && name.contains("错误")) {

                                        waiting[0] = false;
                                    } else {
                                        waiting[1] = false;

                                    }
                                    long end = System.currentTimeMillis();

                                    sb.append("[" + name + "]" + "执行[耗时" + (end - start) + "ms]\n");

                                }
                            });

                            try {
                                long waitTime = 0;
                                while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                    waitTime += 10;
                                    Thread.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sb.insert(0, stringExceptionPair.first);
                            return sb.toString();

                        }

                        @Override
                        public void onRunFinish(String o) {
                            if (o != null) {
                                item.setMessage(o);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                            }

                        }
                    }).execute(sb);
                    return true;
                }


            }

            case CmdConfig.REPLACE_FILE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.REPLACE_FILE, "");

                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "支持的操作如下:\nA B file\n提示\n shell命令也可以完成替换操作\n awk '{gsub(/old/,\"new\")}1' filename \n sed -i 's/old/new/g' filename", item);
                    return true;
                }
                String waitReplace = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String replaceContent = getCurrentArgAndAfter(args, ParamParseUtil.sArgSecond);
                String file = getCurrentArgAndAfter(args, ParamParseUtil.sArgThrid);

                {
                    long start = System.currentTimeMillis();
                    StringBuffer sb = new StringBuffer();
                    new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                        @Override
                        public String onRunBackgroundThread(StringBuffer[] params) {
                            StringBuffer sb = params[0];
                            boolean[] waiting = {true, true};
                            String cmd="awk '{gsub(/"+waitReplace+"/,\""+replaceContent+"\")}1' "+file+"";
                            sb.append("\n执行命令:"+cmd);
                            sb.append("\n被操作的文件:"+file);
                            sb.append("\n把:"+waitReplace+"替换为"+replaceContent+"\n");
                            Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{cmd}, new ICmdIntercept<String>() {
                                //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                                @Override
                                public boolean isNeedIntercept(String bean) {
                                    sb.append(bean);
                                    return false;
                                }

                                @Override
                                public void onComplete(String name) {
                                    if (name != null && name.contains("错误")) {

                                        waiting[0] = false;
                                    } else {
                                        waiting[1] = false;

                                    }
                                    long end = System.currentTimeMillis();

                                    sb.append("[" + name + "]" + "执行[耗时" + (end - start) + "ms]\n");

                                }
                            });

                            try {
                                long waitTime = 0;
                                while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                    waitTime += 10;
                                    Thread.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sb.insert(0, stringExceptionPair.first);
                            return sb.toString();

                        }

                        @Override
                        public void onRunFinish(String o) {
                            if (o != null) {
                                item.setMessage(o);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                            }

                        }
                    }).execute(sb);
                    return true;
                }


            }
            case CmdConfig.EXEC_SHELL: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.EXEC_SHELL, "");
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请输入要执行的shell命令,如echo hello", item);
                    return true;
                }

                {
                    String finalCmd = value.trim();
                    long start = System.currentTimeMillis();
                    StringBuffer sb = new StringBuffer();
                    new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                        @Override
                        public String onRunBackgroundThread(StringBuffer[] params) {
                            StringBuffer sb = params[0];
                            boolean[] waiting = {true, true};
                            Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{finalCmd}, new ICmdIntercept<String>() {
                                //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                                @Override
                                public boolean isNeedIntercept(String bean) {
                                    sb.append(bean);
                                    return false;
                                }

                                @Override
                                public void onComplete(String name) {
                                    if (name != null && name.contains("错误")) {

                                        waiting[0] = false;
                                    } else {
                                        waiting[1] = false;

                                    }
                                    long end = System.currentTimeMillis();

                                    sb.append("[" + name + "]" + "执行shell完成[耗时" + (end - start) + "ms]\n");

                                }
                            });

                            try {
                                long waitTime = 0;
                                while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                    waitTime += 10;
                                    Thread.sleep(10);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sb.insert(0, stringExceptionPair.first);
                            return sb.toString();

                        }

                        @Override
                        public void onRunFinish(String o) {
                            if (o != null) {
                                item.setMessage(o);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                            }

                        }
                    }).execute(sb);
                    return true;
                }


            }

            case CmdConfig.UPDATE_COOKIE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.UPDATE_COOKIE, "");
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递参数cookies", item);
                    return true;
                }
                OpenAIBiz.updateOpenAICookie(value);
                MyCookieManager myCookieManager = new MyCookieManager();
                myCookieManager.addCookies(value);
                String sessionToken = myCookieManager.cookiesMap.get("__Secure-next-auth.session-token");
                String cf_clearance = myCookieManager.cookiesMap.get("cf_clearance");
                MsgReCallUtil.notifyHasDoWhileReply(this, "更新机器人Cookies完成,\n__Secure-next-auth.session-token=" + sessionToken + ";cf_clearance=" + cf_clearance + ",cookie总数:" + myCookieManager.cookiesMap.size() + "\n" + myCookieManager.cookiesMap.keySet(), item);
                return true;
            }
            case CmdConfig.UPDATE_REQEUST_HEADER: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.UPDATE_REQEUST_HEADER, "");
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递参数请求头参数json数据,或者curl  开头包含-H的数据 或key=value|key=value格式数据", item);
                    return true;
                }
                OpenAIBiz.updateRequestHeaderMark(value);
                HashMap<String, String> map = OpenAIBiz.genereateBaseHeader();
                int size = map.size();
                HashMap<String, String> mapParse = map;
                OpenAIBiz.parseRequestHeaderPutMap(mapParse, value);
                OpenAIBiz.reqeustHeaderRemoveRepeat(map);
                MsgReCallUtil.notifyHasDoWhileReply(this, "请求头提交数量:" + size + "\n结果:\n" + ParseUtils.formatMap2KeyValue(map), item);
                return true;
            }
            case CmdConfig.COOKIE_COVER: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.COOKIE_COVER, "").trim();
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递参数cookies", item);
                    return true;
                }
                MyCookieManager myCookieManager = OpenAIBiz.tempCoverMergeCookie(value);
                Set<String> strings = myCookieManager.getCookiesMap().keySet();
                MsgReCallUtil.notifyHasDoWhileReply(this, "覆盖cookie,cookie总数:" + myCookieManager.cookiesMap.size() + "," + strings, item);
                return true;
            }
            case CmdConfig.SERCERT_UPDATE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.SERCERT_UPDATE, "").trim();
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递 Chat gpt apisercet,获取网址为:https://beta.openai.com/,当前sercretKey为:"+  _miscConfig.chatgpt_api_sercret_key, item);

                    return true;
                }
                String beforeValue=_miscConfig.chatgpt_api_sercret_key;
                _miscConfig.chatgpt_api_sercret_key=value;
                sharedPreferences.edit().putString(Cns.CHAT_GPT_API_SERCRET,value);
                MsgReCallUtil.notifyHasDoWhileReply(this, "更新完成\n之前值:"+ beforeValue+"\n现在值:"+value, item);
                return true;
            }
            case CmdConfig.BROWSER_ACCESS_INNER: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.BROWSER_ACCESS, "").trim();
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请输入要模拟访问的网址", item);
                    return true;
                }
                AppUtils.toWebView(AppContext.getContext(), value);
                MsgReCallUtil.notifyHasDoWhileReply(this, "已访问" + value, item);
                return true;
            }
            case CmdConfig.BROWSER_ACCESS: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.BROWSER_ACCESS, "").trim();
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请输入要模拟访问的网址", item);
                    return true;
                }
                AppUtils.toWebViewTX(AppContext.getContext(), value, "");
                MsgReCallUtil.notifyHasDoWhileReply(this, "已访问" + value, item);
                return true;
            }
            case CmdConfig.ACCESS_NET: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String value = item.getMessage().replace(CmdConfig.ACCESS_NET, "").trim();
                if (TextUtils.isEmpty(value)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请传递要测试的网址", item);
                    return true;
                }
                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "访问+" + value + "中,请稍后", item);
                HashMap<String, String> map = OpenAIBiz.genereateBaseHeader();
                try {
                    HttpUtil.queryGetData(value, map, new Callback() {
                        @Override
                        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                            String str = "访问" + value + "失败" + e.getMessage();
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), str, item);

                        }

                        @Override
                        public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();
                                    MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "访问" + value + "成功\n" + RegexUtils.deleteHtmlLabelAndFindBody(str), item);

                                } else {
                                    String str = response.body().string();
                                    MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "访问+" + value + "失败,code:" + str + "," + RegexUtils.deleteHtmlLabelAndFindBody(str), item);
                                }

                            } catch (Throwable e) {
                                LogUtil.writeLoge("访问网络失败", e);
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "访问故障,url:" + value + "," + e.getMessage(), item);

                            }
                        }
                    });
                } catch (Throwable e) {
                    LogUtil.writeLoge("请求异常", e);
                    MsgReCallUtil.notifyHasDoWhileReply(this, "请求异常" + e.toString(), item);
                }

                return true;
            }

//            break;
            case CmdConfig.ADD_WORD_CMD: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String ask = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String answer = getCurrentArgAndAfter(args, ParamParseUtil.sArgSecond);
                if (ask == null) {

                    String msg = "无法添加词库,不能识别词库[问]请检查语法语法是否正确,问与答之间用空格隔开哦!";

                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else if (answer == null) {


                    String msg = "无法添加词库,不能识别词库[答]请检查语法语法是否正确,问与答之间用空格隔开哦!";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else {


                    ReplyWordBean bean = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumn(ReplyWordBean.class, FieldCns.ASK, ask);
                    if (bean == null) {

                        List<ReplyWordBean> wordBeans = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryAllByFieldLike(ReplyWordBean.class, FieldCns.ASK, ask);

                        if (wordBeans != null) {
                            for (ReplyWordBean wordBeanLike : wordBeans) {

                                HashSet<String> strings = ClearUtil.word2HashSet(ClearUtil.wordSplit, wordBeanLike.getAsk());
                                if (strings != null) {
                                    boolean remove = strings.remove(ask);
                                    if (remove) {
                                        bean = wordBeanLike;
                                        break;
                                    }
                                }

                            }
                        }
                    }


                    String msg;
                    if (bean != null) {
                        msg = "词库已经存在 id" + bean.getId() + "问[" + bean.getAsk() + "]\n回复语[" + bean.getAnswer() + "]\n如果要添加多条回复语,请打开此词条，然后编辑用分隔符" + ClearUtil.wordSplit + "进行分割";
                    } else {

                        bean = new ReplyWordBean(ask, answer);
                        long result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).insert(bean);
                        if (result > 0) {
                            bean.setId((int) result);
                            doInsertNewKeyBean(bean);
                            msg = String.format(AppConstants.ACTION_OPERA_NAME + "添加词库成功\n问:%s\n答:%s", ask, answer);
                        } else {
                            msg = AppConstants.ACTION_OPERA_NAME + "添加词库失败,数据库错误 inser effect count:" + result;
                        }
                    }


                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                }

            }
            break;

            case CmdConfig.UPDATE_WORD_CMD: {

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }
                String ask = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                String answer = getCurrentArgAndAfter(args, ParamParseUtil.sArgSecond);
                if (ask == null) {

                    String msg = "无法更新词库,不能识别词库[问]请检查语法语法是否正确";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else if (answer == null) {


                    String msg = "无法更新词库,不能识别词库[答]请检查语法语法是否正确";


                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else {

                    ReplyWordBean bean = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumn(ReplyWordBean.class, FieldCns.ASK, ask);

                    String msg;
                    if (bean == null) {
                        msg = "此词库并未创建,建议改用添加词库命令";
                    } else {

                        bean.setAnswer(answer);
                        long result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(bean);

                        if (result > 0) {
                            bean.setId((int) result);
                            initWordMap();
                            msg = String.format("更新词库成功\n问:%s\n答:%s", ask, answer);
                        } else {
                            msg = "更新词库失败,数据库错误  effect count:" + result;
                        }
                    }


                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                }

            }
            break;

            case CmdConfig.FLOOR:
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }


                if (args.length == 0) {
                    if (isgroupMsg) {

                        MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.printFloorData(item.getFrienduin(), 20), item);
                    } else {
                        MsgReCallUtil.notifyJoinReplaceMsgJump(this, "私聊需要指定群号", item);

                    }

                } else {
                    String group = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.printFloorData(group, 20), item);
                }

                break;
            case CmdConfig.GAG:
            case CmdConfig.GAG1:
            case CmdConfig.BIZUI:
            case CmdConfig.GAG_SHUTUP: {


                boolean isCurrentGroupAdmin = false;
                if (isgroupMsg && nameBean != null) {
                    isCurrentGroupAdmin = isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                    nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                }

                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean, true)) {
                    return true;
                }
    /*            String arg0 = getArgByArgArr(args, sArgFirst);
                String arg1 = getArgByArgArr(args, sArgSecond);
                String arg2 = getArgByArgArr(args, sArgSecond);*/


                if (isgroupMsg) {
                    return doGagFromGroupMsgCmd(item, isManager, args, atPair, nameBean);
                } else {
                    return doGagCmdPrivateMsgCmd(item, isManager, args);
                }


            }
            case CmdConfig.TASK: {


                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean)) {
                    return true;
                }


                if (args.length == 0) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "如何使用任务命令？任务后面 加上踢人，发消息，禁言，以及时间参数\n举例:\n[禁言 QQ 1分钟 5分钟]表示5分钟之后禁言qq 1分钟\n [踢 QQ 0 5分钟]表示5分钟之后踢掉禁言某Q,非用就,\n[发消息 QQ 我爱你 5分钟]表示5分钟之后发送我爱你给指定QQ" +
                            "\n清除任务发送[" + CmdConfig.CLEAR_TASK + " 任务ID]", item);
                } else {
                    String firstCmd = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);//命令名称


                    if (firstCmd != null) {
                        if (CmdConfig.LIJI_EXECUTE.equals(firstCmd)) {

                            //immediate execution
                            if (TaskUtils.hasTask()) {
                                int i = TaskUtils.immediateExecute();
                                MsgReCallUtil.notifyHasDoWhileReply(this, "执行完成,工执行" + i + "个任务", item);

                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,没有任何任务", item);

                            }
                            return true;
                        }
                    }
                    String sendOrGroup;
                    String senderuin;
                    String currentArg1;
                    String currentArg2;
                    boolean multiOpera = false;
                    if (isgroupMsg && atPair.first && atPair.second.second != null && atPair.second.second.size() > 0) {
                        sendOrGroup = item.getFrienduin();
                        senderuin = atPair.second.second.get(0).getSenderuin();
                        currentArg1 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);// 群号
                        currentArg2 = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgThrid);
                        multiOpera = true;

                    } else {


                        sendOrGroup = isgroupMsg ? item.getFrienduin() : ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                        senderuin = ParamParseUtil.getArgByArgArr(args, isgroupMsg ? ParamParseUtil.sArgSecond : ParamParseUtil.sArgThrid);
                        currentArg1 = ParamParseUtil.getArgByArgArr(args, isgroupMsg ? ParamParseUtil.sArgThrid : ParamParseUtil.sArgFourth);// 群号
                        currentArg2 = ParamParseUtil.getArgByArgArr(args, isgroupMsg ? ParamParseUtil.sArgFourth : ParamParseUtil.sArgFifth);

                        if (isgroupMsg) {
                            if (FloorUtils.isFloorData(senderuin)) {
                                senderuin = FloorUtils.getFloorQQ(sendOrGroup, senderuin);
                            }
                        }


                    }


                    if (TextUtils.isEmpty(sendOrGroup)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,任务参数错误,无法识别群号", item);
                        return true;
                    }
                    if (TextUtils.isEmpty(senderuin)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,任务参数错误,无法识别QQ", item);
                        return true;
                    }
                    if (TextUtils.isEmpty(currentArg1)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,任务参数错误,缺少附加参数 (禁言 则缺少禁言分钟,发消息则表示缺少消息体，踢人则表示缺少是否踢出的操作)", item);

                        return true;

                    }

                    if (TextUtils.isEmpty(currentArg2) && !firstCmd.equals(CmdConfig.SEND_MSG)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "抱歉,任务参数错误,缺少执行时间,加入任务当然是不能马上执行的,最低时间也不能低于一分钟", item);

                        return true;

                    }

                    long exeucuteTimeSecond = ParseUtils.parseGagStr2Secound(currentArg2);
                    MsgItem clone = item.clone();

                    clone.setIstroop(senderuin.equals(sendOrGroup) ? 0 : 1);
                    clone.setFrienduin(sendOrGroup);
                    clone.setNickname(" ");
                    clone.setSenderuin(senderuin);
                    String cancelCmd = (senderuin + sendOrGroup + currentArg1 + currentArg2 + item.getIstroop()).hashCode() + "";
                    cancelCmd = cancelCmd.replace("-", "");

                    innergag:
                    switch (firstCmd) {
                        case CmdConfig.GAG:
                        case CmdConfig.GAG1:
                        case CmdConfig.GAG_SHUTUP: {
                            long gagTime = ParseUtils.parseGagStr2Secound(currentArg1);


                            if (gagTime < 0) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "没有指定禁言时间", item);
                            }


                            if (exeucuteTimeSecond < 60) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "执行任务时间过短,当前设置为" + exeucuteTimeSecond + "秒后执行", item);
                            }


                            if (multiOpera) {

                                TaskUtils.insertGagMultiTaskFromAtMsg(this, cancelCmd, clone, atPair.second.second, exeucuteTimeSecond * 1000, gagTime);

                                MsgReCallUtil.notifyHasDoWhileReply(this, "加入任务成功" +
                                        "\n执行时间:" + DateUtils.getGagTime(exeucuteTimeSecond) + "后" +
                                        (isgroupMsg ? "" : "\n操作群:" + sendOrGroup) +
                                        "\n操作:禁言多个群成员(总数:" + atPair.second.second.size() + ")" +
                                        "\n提示:发送[" + CmdConfig.CLEAR_TASK + cancelCmd + "]可取消此任务", item);
                            } else {


                                TaskUtils.insertGagTask(this, cancelCmd, clone, exeucuteTimeSecond * 1000, gagTime);


                                MsgReCallUtil.notifyHasDoWhileReply(this, "加入任务成功" +
                                        "\n执行时间:" + DateUtils.getGagTime(exeucuteTimeSecond) + "后" +
                                        (isgroupMsg ? "" : "\n操作群:" + sendOrGroup) +
                                        "\n操作:禁言QQ" + senderuin + " " + DateUtils.getGagTime(gagTime) + "  " +
                                        "\n提示:发送[" + CmdConfig.CLEAR_TASK + cancelCmd + "]可取消此任务", item);

                            }


                        }

                        break innergag;


                        case CmdConfig.KICK:
                        case CmdConfig.KICK_1:
                        case CmdConfig.KICK_2: {


                            boolean forver = ParseUtils.parseBoolean(currentArg1);

                            TaskUtils.insertRedpacketKickTask(this, cancelCmd, clone, exeucuteTimeSecond * 1000, forver);

                            MsgReCallUtil.notifyHasDoWhileReply(this, "加入任务成功" +
                                    "\n执行时间:" + DateUtils.getGagTime(exeucuteTimeSecond) + "后" +
                                    "\n操作:踢出" + sendOrGroup + "群成员" + senderuin + "" +
                                    "\n是否永久:" + forver + "" +
                                    "\n提示:发送" + CmdConfig.CLEAR_TASK + cancelCmd + "可取消此任务", item);


                        }
                        break innergag;
                        case CmdConfig.SEND_MSG: {
                            String messageContent = "";
                            if (senderuin != null) {
                                if (clone.getIstroop() == 1) {

                                    messageContent = currentArg1;
                                    clone.setIstroop(0);
                                    clone.setSenderuin(senderuin);
                                    clone.setFrienduin(senderuin);

                                } else {
                                    clone.setSenderuin(sendOrGroup);
                                    clone.setFrienduin(sendOrGroup);
                                    exeucuteTimeSecond = ParseUtils.parseGagStr2Secound(currentArg2);

//                                    senderuin=senderuin;
                                    messageContent = senderuin;

                                }


                            }


                            TaskUtils.insertSendMsgKickTask(this, cancelCmd, clone, exeucuteTimeSecond * 1000, messageContent);

                            if (clone.getIstroop() == 1) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "加入发消息任务成功" +
                                        "\n执行时间:\n" + DateUtils.getGagTime(exeucuteTimeSecond) + "后" +
                                        "\n行为:发送消息" + messageContent + "给群" + sendOrGroup + "\n提示:发送" + CmdConfig.CLEAR_TASK + cancelCmd + "可取消此任务", item);


                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "加入发消息任务成功" +
                                        "\n执行时间:" +
                                        "\n" + DateUtils.getGagTime(exeucuteTimeSecond) + "后" +
                                        "\n行为:发送消息" + messageContent + "给好友" + senderuin + "" +
                                        "\n提示:发送" + CmdConfig.CLEAR_TASK + cancelCmd + "可取消此任务", item);

                            }


                        }
                        break innergag;
                        default:


                            if (isSelf && !RegexUtils.checkIsContainNumber(argStr)) {//命令输错很可能是因为网络词库自身触发导致的。
                                return false;

                            }

                            MsgReCallUtil.notifyHasDoWhileReply(this, "未知的任务类型" + firstCmd + " 仅支持 踢人，发消息，禁言倒计时任务", item);


                            return true;


                    }
                    return true;
                }
            }

            break;

            case CmdConfig.WEIGUI_: {

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }


                if (!isgroupMsg) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "此命令只支持在群聊操作", item);
                } else {


                    String childCmd = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                    if (TextUtils.isEmpty(childCmd)) {
                        MsgReCallUtil.notifyJoinReplaceMsgJump(this, "支持的子命令为 [查看 QQ] [清空 QQ|群|(不填写清空所有群)]", item);

                    } else {


                        GroupAtBean groupAtBean = ConfigUtils.fetchLastAtBean(item, atPair);
                        String qq = groupAtBean != null ? groupAtBean.getAccount() : ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                        child:
                        switch (childCmd) {

                            case "查看":

                                if (TextUtils.isEmpty(qq)) {
                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "账号必须指定", item);
                                } else {
                                    int violationCount = ViolationRecordUtil.getViolationCount(_dbUtils, item.getFrienduin(), qq);

                                    List<ViolationWordRecordBean> violationWordRecordBeans = null;
                                    if (violationCount >= 0) {
                                        violationWordRecordBeans = ViolationWordHistoryRecordUtil.queryRecord(_dbUtils, item.getFrienduin(), qq);
                                    }

                                    StringBuffer sb = new StringBuffer();
                                    if (groupAtBean != null) {

                                        sb.append("" + groupAtBean.getNickname());
                                    }
                                    sb.append(" QQ" + qq + "违规次数:" + violationCount + "\n");
                                    if (violationWordRecordBeans != null) {
                                        sb.append("违规记录前10条如下\n");

                                        int size = violationWordRecordBeans.size() > 10 ? 10 : violationWordRecordBeans.size();
                                        for (ViolationWordRecordBean bean : violationWordRecordBeans) {
                                            sb.append(DateUtils.getTime(bean.getTime()) + ":" + bean.getWord() + "\n");
                                        }

                                    }


                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "" + sb.toString(), item);

                                }

                                break child;
                            case "清空"://IGNORE_INCLUDE
                                if (TextUtils.isEmpty(qq)) {

//                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "账号码必须指定", item);
                                    ViolationRecordUtil.clearAll(_dbUtils);
                                    ViolationWordHistoryRecordUtil.clearAll(_dbUtils);
                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "清空所有违规总数记录 违规行为记录完成", item);


                                } else if (qq.equals("群")) {
                                    int i = ViolationRecordUtil.resetViolationCount(_dbUtils, item.getFrienduin());
                                    int i1 = ViolationWordHistoryRecordUtil.clearRecord(_dbUtils, item.getFrienduin());
                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "清空本群违规总数记录是否成功" + (i > 0) + " 违规详情记录是否成功" + (i1 > 0) + ",前者总数:" + i + ",后者总数:" + i1, item);


                                } else {
                                    int i = ViolationRecordUtil.resetViolationCount(_dbUtils, item.getFrienduin(), qq);
                                    int i1 = ViolationWordHistoryRecordUtil.clearRecord(_dbUtils, item.getFrienduin(), qq);
                                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, "清空QQ 违规总数记录是否成功" + (i > 0) + " 违规详情记录是否成功" + (i1 > 0) + ",前者总数:" + i + ",后者总数:" + i1, item);


                                }


                                break child;
                            default://正常的命令识别词库


                                MsgReCallUtil.notifyJoinReplaceMsgJump(this, "不支持的子命令[" + childCmd + "]是否忘记加空格了?", item);

                                return true;
                        }
                    }


                }


            }


            break;
            case CmdConfig.PLUGIN_INFO: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return true;
                }


                StringBuffer sb = new StringBuffer();
                sb.append("已安装java插件总数" + mPluginList.size() + "\n");
                for (int i = 0; i < mPluginList.size(); i++) {
                    IPluginHolder pluginModel = mPluginList.get(i);
                    PluginInterface pluginInterface = pluginModel.getPluginInterface();
                    sb.append("【" + (1 + i) + "】" + pluginInterface.getPluginName() + " " + pluginInterface.getVersionName() + "-" + pluginInterface.getVersionCode() + "\n");
                    sb.append("作者:" + pluginInterface.getAuthorName() + "\n编译时间:" + pluginInterface.getBuildTime() + "\n安装路径:" + pluginModel.getPath() + "\n是否禁用:" + (pluginModel.isDisable() ? "是" : "否"));
                    sb.append("\n");
                    sb.append("\n");

                }

                sb.append("已安装js插件总数" + mPluginList.size() + "\n");
                for (int i = 0; i < mJSPluginList.size(); i++) {
                    IPluginHolder pluginModel = mJSPluginList.get(i);
                    PluginInterface pluginInterface = pluginModel.getPluginInterface();
                    sb.append("【" + (1 + i) + "】" + pluginInterface.getPluginName() + " " + pluginInterface.getVersionName() + "-" + pluginInterface.getVersionCode() + "\n");
                    sb.append("作者:" + pluginInterface.getAuthorName() + "\n编译时间:" + pluginInterface.getBuildTime() + "\n安装路径:" + pluginModel.getPath() + "\n是否禁用:" + (pluginModel.isDisable() ? "是" : "否"));
                    sb.append("\n");
                    sb.append("\n");

                }
                sb.append("已安装lua插件总数" + mPluginList.size() + "\n");
                for (int i = 0; i < mLuaPluginList.size(); i++) {
                    IPluginHolder pluginModel = mLuaPluginList.get(i);
                    PluginInterface pluginInterface = pluginModel.getPluginInterface();
                    sb.append("【" + (1 + i) + "】" + pluginInterface.getPluginName() + " " + pluginInterface.getVersionName() + "-" + pluginInterface.getVersionCode() + "\n");
                    sb.append("作者:" + pluginInterface.getAuthorName() + "\n编译时间:" + pluginInterface.getBuildTime() + "\n安装路径:" + pluginModel.getPath() + "\n是否禁用:" + (pluginModel.isDisable() ? "是" : "否"));
                    sb.append("\n");
                    sb.append("\n");

                }
                MsgReCallUtil.notifyJoinReplaceMsgJump(this, sb.toString(), item);

            }
            break;
            case CmdConfig.AITE_CMD: {

                if (isgroupMsg && nameBean != null) {
                    boolean isCurrentGroupAdmin = isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                    nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                }
                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean, true)) {
                    return true;
                }


                if (isgroupMsg) {
                    String group = item.getFrienduin();
                    if (doAtCmdByGroupMsg(item, isManager, args, group)) {
                        return true;
                    }


                } else if (MsgTyeUtils.isPrivateMsg(item)) {
                    String group = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                    if (TextUtils.isEmpty(group)) {
                        MsgReCallUtil.notifyJoinReplaceMsgJump(this, "私聊发起艾特消息必须指定群号", item);
                        return true;


                    }
                    if (doAtCmdByPrivateMsg(item, isManager, args, group)) {
                        return true;
                    }


                }


            }
            break;
            case CmdConfig.CONFIG: {
                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean)) {
                    return true;
                }

                String argFirst = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(argFirst)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "支持修改的子命令有[" + String.format("%s %s %s %s|%s %s %s %s %s %s %s %s %s %s %s %s %s  %s(部分命令操作教程需要输入子命令方可查看)",
                            CmdConfig.ChildCmd.CONFIG_ADD_VAR,
                            CmdConfig.ChildCmd.CONFIG_DELETE_VAR,
                            CmdConfig.ChildCmd.CONFIG_MODIFY_VAR,
                            CmdConfig.ChildCmd.CONFIG_MODIFY,
                            CmdConfig.ChildCmd.CONFIG_GROUP_INFO,
                            CmdConfig.ChildCmd.CONFIG_USER_CARD,
                            CmdConfig.FECTCH_MUSIC,
                            CmdConfig.ChildCmd.CONFIG_PRINT,
                            CmdConfig.ChildCmd.CONFIG_RELOAD,
                            CmdConfig.ChildCmd.CONFIG_SHOW,
                            CmdConfig.ChildCmd.CONFIG_CARD,
                            CmdConfig.ChildCmd.CONFIG_USER_CARD,
                            CmdConfig.ChildCmd.CONFIG_EXIT_DISCUSSION,
                            CmdConfig.ChildCmd.CONFIG_EXIT_GROUP,
                            CmdConfig.ChildCmd.CONFIG_CAST_URI_DECODE,
                            CmdConfig.ChildCmd.CONFIG_CAST_URI_DECODE,
                            CmdConfig.ChildCmd.CONFIG_WEB_ENCODE,
                            "\n" + CmdConfig.CONFIG + "" + CmdConfig.ChildCmd.CONFIG_RELOAD +
                                    "\n" + CmdConfig.CONFIG + "" + CmdConfig.ChildCmd.CONFIG_RESTART +
                                    "\n" + CmdConfig.CONFIG + "" + CmdConfig.ChildCmd.CONFIG_MODIFY +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_SHOW +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_EXIT_GROUP +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_EXIT_DISCUSSION +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_EXECUTE + " shell命令" +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_OPEN + "  schame或url" +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_SCREENCAP + "  截图" +
                                    "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_LAUNCHER_APP + "  应用包名" +
                                    CmdConfig.ChildCmd.CONFIG_SQL) + "【可脱离ui修改任意设置】最新版插件支持载入本机器人软件作为插件进行加载提高稳定性,但是ui界面失效，那么本命令将变成一款神器了.]\n具体演示\n" +
                            "\n" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_SQL + " select * from $管理员 limit 0,1(更多命令输入`" + CmdConfig.CONFIG + "SQL`" + ")" +
                            "", item);


                    return true;
                }

                String argSecond = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                switch (argFirst) {
                    case CmdConfig.ChildCmd.CONFIG_KILL:
                        android.os.Process.killProcess(Process.myPid());
                        return true;
                    case CmdConfig.ChildCmd.CONFIG_RESTART:
                        AppUtils.restartApp(AppContext.getContext());
                        return true;
                    case CmdConfig.FECTCH_MUSIC:
                        if (TextUtils.isEmpty(argSecond)) {

                            MsgReCallUtil.notifyHasDoWhileReply(this, String.format("请输入如 %s%s酷狗,将修改引擎并且马上生效", CmdConfig.CONFIG, CmdConfig.FECTCH_MUSIC), item);


                            return true;

                        }

                        String[] array = getResources().getStringArray(R.array.musicoptions);
                        for (int i = 0; i < array.length; i++) {

                            int currentNo = i + 1;

                            String engine = array[i];
                            if (argSecond.equals(currentNo + "") || argSecond.equals(engine)) {
                                musicType = i;
                                ConfigUtils.saveAppUIPerference(getProxyContext(), getResources().getString(R.string.key_base_robot_music_engine), musicType + "");
                                MsgReCallUtil.notifyHasDoWhileReply(this, String.format("切换到%s点歌引擎成功,发送%s 老男孩 命令试试吧", engine, CmdConfig.FECTCH_MUSIC), item);
                                return true;
                            }
                        }

                        MsgReCallUtil.notifyHasDoWhileReply(this, String.format("参数指令错误只支持序号或者中文名来切换引擎 ,默认共%d个音乐引擎,请输入如 %s%s酷狗或1-%d其中的任何一个序号,将修改引擎并且马上生效", array.length, CmdConfig.CONFIG, CmdConfig.FECTCH_MUSIC, array.length), item);


                        break;
                    case CmdConfig.ChildCmd.CONFIG_USER_CARD:
                        if (!isAsPluginLoad() && !RemoteService.isIsInit()) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "本命令需要宿主插件化加载机器人或宿主绑定机器人服务方可使用", item);
                        } else {

                            if (isAsPluginLoad() && mHostControlApi == null) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "本命令需要Q++版本1.0.6以及以上方可使用", item);
                            } else {

                                String qq = ParamParseUtil.getArgByArgArr(args, 1);

                                if (TextUtils.isEmpty(qq)) {
                                    qq = item.getSenderuin();
                                }


                                if (!RegexUtils.checkIsContainNumber(qq)) {

                                    MsgReCallUtil.notifyHasDoWhileReply(this, "请输入qq", item);
                                    return true;
                                }


                                {


                                    StringBuilder sb = new StringBuilder();

                                    String nickname = NickNameUtils.queryNicknameFromHost(qq, item.getFrienduin(), 0);
                                    String robotnickname = NickNameUtils.queryNicknameFromHost(item.getSelfuin(), item.getFrienduin(), 0);

                                    if (isgroupMsg) {
                                        String groupnickname = NickNameUtils.queryNicknameFromHost(qq, item.getFrienduin(), 1);
                                        String groupnicknameRobot = NickNameUtils.queryNicknameFromHost(item.getSelfuin(), item.getFrienduin(), 1);
                                        sb.append("机器人群备注:" + groupnickname + "\n");
                                        sb.append("机器人昵称:" + robotnickname + "\n");
                                        sb.append("群备注:" + groupnickname + "\n");
                                        sb.append("昵称:" + nickname + "\n");


                                    } else {

                                        sb.append("机器人昵称:" + robotnickname + "\n");
                                        sb.append("昵称:" + nickname + "\n");


                                    }


                                    Map map = null;
                                    try {
                                        map = RemoteService.getClientICallBack().queryClientData(ServiceExecCode.QUERY_USER_INFO, 1, false, qq, null, null);
                                        if (map != null && ParseUtils.parseInt(map.get("code")) == 0) {


                                            /*

                                                 map.put("bAvailVoteCnt", bAvailVoteCnt + "");
                map.put("bHaveVotedCnt", bHaveVotedCnt + "");
                map.put("strSpaceName", strSpaceName + "");
                                             */
                                            if (ParseUtils.parseInt(map.get("code")) == 0) {
                                                sb.append("QQ等级:" + map.get("level") + "\n");
                                                sb.append("昵称:" + map.get("nickname") + "\n");
                                                sb.append("动态:" + map.get("dynamic") + "\n");
                                                sb.append("空间:" + map.get("strSpaceName") + "\n");
                                                sb.append("已点赞次数" + map.get("bHaveVotedCnt") + "次赞\n");//Voted投票
                                                sb.append("剩余点赞次数:" + map.get("bAvailVoteCnt") + "次赞\n");//Voted投票
//                                                sb.append("个人备注:" + map.get("remark") + "\n");

                                                String addressinfo = (String) map.get("addressinfo");
                                                if (!TextUtils.isEmpty(addressinfo)) {
                                                    sb.append("坐标:" + addressinfo + "\n");

                                                }
                                                String province = (String) map.get("province");
                                                if (!TextUtils.isEmpty(province)) {
                                                    sb.append("省:" + province + "\n");

                                                }
                                                String city = (String) map.get("city");

                                                if (!TextUtils.isEmpty(province)) {
                                                    sb.append("市:" + city + "\n");

                                                }


                                                MsgReCallUtil.notifyHasDoWhileReply(this, sb.toString(), item);

                                            } else {

                                                MsgReCallUtil.notifyHasDoWhileReply(this, "查询失败 " + map.get("msg"), item);
                                            }


                                        }


                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                    MsgReCallUtil.notifyHasDoWhileReply(this, sb.toString(), item);

                                }


                            }


                        }


                        return true;
                    case CmdConfig.ChildCmd.CONFIG_OPEN: {
                        String s1 = ParamParseUtil.mergeParameters(args, 1);
                        if (TextUtils.isEmpty(s1)) {

                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "请输入要执行的命令如scheme或者url!", item);
                            return true;
                        }
                        try {
                            AppUtils.openWebView(AppUtils.getApplication(), s1);
                        } catch (Exception e) {
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "启动失败!" + e.getMessage(), item);

                        }
                    }

                    return true;
                    case CmdConfig.ChildCmd.CONFIG_LAUNCHER_APP: {
                        String s1 = ParamParseUtil.mergeParameters(args, 1);
                        if (TextUtils.isEmpty(s1)) {

                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "请输入要启动的应用包名!", item);
                            return true;
                        }
                        try {
                            AppUtils.lauchApp(AppUtils.getApplication(), s1);
                        } catch (Exception e) {
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "启动失败!" + e.getMessage(), item);

                        }
                    }

                    return true;
                    case CmdConfig.ChildCmd.CONFIG_EXECUTE:
                        String s1 = ParamParseUtil.mergeParameters(args, 1);
                        if (TextUtils.isEmpty(s1)) {

                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "请输入要执行的命令!", item);
                            return true;
                        }

                        new QssqTaskFix<String, String>(new QssqTaskFix.ICallBackImp<String, String>() {
                            @Override
                            public String onRunBackgroundThread(String[] params) {
                                final Pair<String, Exception> s = ShellUtil.executeAndFetchResultPair(params[0], new ICmdIntercept<String>() {
                                    @Override
                                    public boolean isNeedIntercept(String str) {
                                        MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), str, item);
                                        return false;
                                    }

                                    @Override
                                    public void onComplete(String name) {

                                    }
                                });

                                if (s.first != null) {
                                    return s.first;
                                } else if (s.second != null) {
                                    return "[执行错误]" + s.second.getMessage();
                                }

                                return "未知错误";

                            }

                            @Override
                            public void onRunFinish(String o) {


                                if (o != null) {
                                    MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                                }


                            }
                        }).execute(s1);


                        return true;
                    case CmdConfig.ChildCmd.CONFIG_QQINFO: {

                        if (!RemoteService.isIsInit()) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "本命令需要宿主绑定机器人服务方可使用", item);
                        } else {
                            String account = ParamParseUtil.getArgByArgArr(args, 1);
                            if (TextUtils.isEmpty(account)) {
                                account = item.getSenderuin();
                            }

                            Map map = RemoteService.queryQQCard(account);
                            if (map == null) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "查询名片失败", item);
                            } else {

                                StringBuilder sb = new StringBuilder();
                                if (ParseUtils.parseInt(map.get("code")) == 0) {
                                    sb.append("昵称:" + map.get("nickname")); //strQzoneHeader 动态 空间有多少更新啥的。
                                    sb.append("\n等级:" + map.get("level"));
                                    sb.append("\n学校:" + map.get("school"));
                                    sb.append("\n手机:" + map.get("phone"));
                                    sb.append("\n备注:" + map.get("remark"));
                                    sb.append("\n动态:" + map.get("dynamic"));
                                    sb.append("\n地址:" + map.get("addressinfo"));
                                    sb.append("\n省:" + map.get("province"));
                                    sb.append("\n市:" + map.get("city"));
                                    sb.append("\nAvailVoteCnt:" + map.get("bAvailVoteCnt"));
                                    sb.append("\n可赞数:" + map.get("bHaveVotedCnt"));
                                    sb.append("\nSuperVIP:" + map.get("bSuperVipOpen"));
                                    sb.append("\n空间:" + map.get("strSpaceName"));
                                    MsgReCallUtil.notifyHasDoWhileReply(this, sb.toString(), item);

                                } else {

                                    MsgReCallUtil.notifyHasDoWhileReply(this, "查询失败 " + map.get("msg"), item);
                                }


                            }


                        }


                    }
                    return true;
                    case CmdConfig.ChildCmd.CONFIG_GROUP_INFO:
                        if (!RemoteService.isIsInit()) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "本命令需要宿主绑定机器人服务方可使用", item);
                        } else {


                            String group = ParamParseUtil.getArgByArgArr(args, 1);

                            if (TextUtils.isEmpty(group)) {
                                if (isgroupMsg) {
                                    group = item.getFrienduin();
                                } else {
                                    MsgReCallUtil.notifyHasDoWhileReply(this, "本命令需要指定群号", item);
                                    return true;
                                }

                            }


                            if (!RegexUtils.checkIsContainNumber(group)) {

                                MsgReCallUtil.notifyHasDoWhileReply(this, "请输入正确的群号", item);
                                return true;
                            }


                            Map map = RemoteService.queryGroupInfo(group);
                            if (map == null) {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "查询失败,群号不存在，或者服务终端断开了", item);
                            } else {

                                StringBuilder sb = new StringBuilder();
                                if (ParseUtils.parseInt(map.get("code")) == 0) {



                                        /*
                                          map.put("troopname", troopname + "");
                map.put("troopuin", troopuin + "");
                                         */
                                    sb.append("群名:" + map.get("troopname") + "\n");
                                    sb.append("群主QQ:" + map.get("troopowneruin") + "\n");
                                    sb.append("管理员:" + map.get("managers") + "\n");
                                    sb.append("群类型:" + map.get("groupclass") + "\n");
                                    sb.append("群介绍:" + (map.get("troopintro") + "").replaceAll("\n", " ") + "\n");
                                    sb.append("本群最大人数:" + map.get("membermaxnum") + "\n");
                                    sb.append("本群当前人数:" + map.get("membernum") + "\n");
                                    sb.append("群星等级:" + map.get("troopstartlevel") + "\n");
                                    sb.append("加群问题:" + map.get("jsontroopproblem") + "\n");
                                    sb.append("加群答案:" + map.get("joinTroopAnswer") + "\n");
                                    sb.append("群创建时间:" + DateUtils.getTimeYmd(Long.parseLong(map.get("troopcreatetime") + "") * 1000l) + "\n");
                                    sb.append("群标签:" + (map.get("tags") + "").replaceAll("\n", " ") + "\n");
                                    sb.append("付费入群金额:" + map.get("paymoney") + "\n");
                                    sb.append("群位置:" + map.get("groupLocation") + "\n");


                                        /*
                 map.put("jsontroopproblem", jsontroopproblem + "");
                map.put("joinTroopAnswer", joinTroopAnswer + "");
                map.put("troopname", troopname + "");
                map.put("managers", managers);
                map.put("hassettroopname", hassettroopname + "");
                map.put("hassettroophead", hassettroophead + "");
                map.put("troopowneruin", troopowneruin + "");//群主qq
                map.put("groupLocation", groupLocation + "");//群位置
                map.put("troopintro", figertroopmemo + "");//群介绍
                map.put("membernum", wMemberNum + "");//群介绍
                map.put("membermaxnum", wMemberMaxNum + "");//群介绍
                map.put("paymoney", paymoney + "");//群介绍
                map.put("troopuin", troopuin + "");
                map.put("tags", tags + "");
                map.put("groupclass", groupclass + "");
                map.put("troopstartlevel", startlevel + "");
                                         */

                                    MsgReCallUtil.notifyHasDoWhileReply(this, sb.toString(), item);

                                } else {

                                    MsgReCallUtil.notifyHasDoWhileReply(this, "查询失败 " + map.get("msg"), item);
                                }


                            }


                        }


                        return true;
                    case CmdConfig.ChildCmd.CONFIG_CARD: {

                        doCardLogic(item, args, argSecond, 1);


                    }
                    return true;
                    case CmdConfig.ChildCmd.CONFIG_EXIT_DISCUSSION:
                        if (MsgTyeUtils.isGroupMsg(item)) {


                            {

                                String msgCurrent = ParamParseUtil.getArgByArgArr(args, 1);

                                MsgReCallUtil.notifyHasDoWhileReply(this, TextUtils.isEmpty(msgCurrent) ? " 什么破玩意,我退了!" : msgCurrent, item);

                                getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MsgReCallUtil.notifyRequestExitDiscussionJump(RobotContentProvider.this, item.clone(), item.getFrienduin());

                                    }
                                }, 5000);


                            }


                        } else {

                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法退出讨论组,私聊消息", item);

                        }
                        return true;
                    case CmdConfig.ChildCmd.CONFIG_EXIT_GROUP:
                        if (MsgTyeUtils.isGroupMsg(item)) {


                            {

                                String msgCurrent = ParamParseUtil.getArgByArgArr(args, 1);

                                MsgReCallUtil.notifyHasDoWhileReply(this, TextUtils.isEmpty(msgCurrent) ? " 什么破群,我退了!" : msgCurrent, item);

                                getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MsgReCallUtil.notifyRequestExitGroupJump(RobotContentProvider.this, item.clone(), item.getFrienduin());

                                    }
                                }, 5000);


                            }


                        } else {

                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法退出群,私聊消息", item);

                        }
                        return true;

                    case CmdConfig.ChildCmd.CONFIG_CAST_URI_DECODE: {
                        String second = ParamParseUtil.getArgByArgArr(args, 2);
                        String encode = second == null ? Charset.defaultCharset().name() : argSecond;
                        String value = second == null ? argSecond : second;

                        try {
                            String msg = URLDecoder.decode(value, encode);
                            MsgReCallUtil.notifyHasDoWhileReply(this, "转换后结果:" + msg + ",编码:" + encode, item);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法转换 要转换的编码:" + encode + ",转换的内容:" + value, item);

                        }

                        return true;
                    }

                    case CmdConfig.ChildCmd.CONFIG_CAST_URI_ENCODE: {
                        String second = ParamParseUtil.mergeParameters(args, 2);
                        String encode = second == null ? Charset.defaultCharset().name() : argSecond;
                        String value = second == null ? argSecond : second;
                        try {
                            String msg = URLEncoder.encode(value, encode);
                            MsgReCallUtil.notifyHasDoWhileReply(this, "转换后结果:" + msg + ",编码:" + encode, item);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法转换 要转换的编码:" + encode + ",要转换的内容:" + value, item);

                        }

                        return true;
                    }
                    case CmdConfig.ChildCmd.CONFIG_WEB_ENCODE: {
                        String second = ParamParseUtil.mergeParameters(args, 1);

                        try {
                            String msg = StringEscapeUtils.escapeHtml4(second);
                            MsgReCallUtil.notifyHasDoWhileReply(this, "转换后结果:" + msg, item);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法转换 " + e.getMessage(), item);

                        }

                        return true;
                    }
                    case CmdConfig.ChildCmd.CONFIG_WEB_DECODE: {
                        String second = ParamParseUtil.mergeParameters(args, 1);

                        try {
                            String msg = StringEscapeUtils.unescapeHtml4(second);
                            MsgReCallUtil.notifyHasDoWhileReply(this, "转换后结果:" + msg, item);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法转换 " + e.getMessage(), item);

                        }

                        return true;
                    }
                    case CmdConfig.ChildCmd.CONFIG_JSON_ENCODE: {
                        String second = ParamParseUtil.mergeParameters(args, 1);

                        try {
                            String msg = StringEscapeUtils.escapeJson(second);
                            MsgReCallUtil.notifyHasDoWhileReply(this, "转换后结果:" + msg, item);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MsgReCallUtil.notifyHasDoWhileReply(this, "无法转换 " + e.getMessage(), item);

                        }

                        return true;
                    }
                    case CmdConfig.ChildCmd.CONFIG_DELETE_VAR: {

                        String name = ParamParseUtil.getArgByArgArr(args, 1);

                        if (TextUtils.isEmpty(name)) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "请输入要删除的变量名", item);


                        } else {

                            int result = DBHelper.getVarTableUtil(_dbUtils).deleteByColumn(VarBean.class, "name", name);
                            MsgReCallUtil.notifyHasDoWhileReply(this, result > 0 ? "删除成功,总数:" + result + "条" : "删除失败,可能不存在或者已被删除!", item);

                        }

                    }


                    return true;
                    case CmdConfig.ChildCmd.CONFIG_MODIFY_VAR: {

//                        String name = ParamParseUtil.getArgByArgArr(args, 1);
                        String name = ParamParseUtil.mergeParameters(args, 1, args.length - 1);

                        if (TextUtils.isEmpty(name)) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "请输入变量名以及变量值,用|分割", item);


                        } else {

                            String[] split = name.split("\\|");//ignore_include

                            if (split.length == 2) {


                                String varName = split[0];
                                String varValue = split[1];

                                List<VarBean> names = DBHelper.getVarTableUtil(_dbUtils).queryAllByField(VarBean.class, "name", varName);

                                if (names != null && names.size() > 0) {

                                    int succcount = 0;
                                    for (VarBean bean : names) {
                                        bean.setValue(varValue);


                                        long id = DBHelper.getVarTableUtil(_dbUtils).update(bean);
                                        if (id > 0) {
                                            succcount++;
                                        }
                                    }
                                    if (succcount > 0) {
                                        MsgReCallUtil.notifyHasDoWhileReply(this, "修改变量成功!!\n变量名" + varName + "\n变量值:" + varValue + "\n成功总数:" + succcount + "\n变量总数:" + names.size(), item);

                                    } else {

                                        MsgReCallUtil.notifyHasDoWhileReply(this, "修改变量失败!!\n变量名" + varName + "\n变量值" + varValue + "\n操作结果:失败", item);
                                    }

                                } else {


                                    MsgReCallUtil.notifyHasDoWhileReply(this, "变量名" + varName + "不存在", item);
                                }


                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "变量传参错误,识别的键名 键值应该有2个,当前传递了" + split.length + "个 是否忘记添加|号了？请输入如:" + CmdConfig.CONFIG + " " + CmdConfig.ChildCmd.CONFIG_ADD_VAR + " 作者|情随事迁 那么输入" + CmdConfig.CONFIG + " " + CmdConfig.ChildCmd.CONFIG_PRINT + " $作者 就可以输出变量了!", item);

                            }
                        }

                    }


                    return true;
                    case CmdConfig.ChildCmd.CONFIG_ADD_VAR: {

//                        String name = ParamParseUtil.getArgByArgArr(args, 1);
                        String name = ParamParseUtil.mergeParameters(args, 1, args.length - 1);

                        if (TextUtils.isEmpty(name)) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "请输入变量名以及变量值,用|分割", item);


                        } else {

                            String[] split = name.split("\\|");//ignore_include

                            if (split.length == 2) {


                                String varName = split[0];
                                String varValue = split[1];

                                List<VarBean> names = DBHelper.getVarTableUtil(_dbUtils).queryAllByField(VarBean.class, "name", varName);

                                if (names != null && names.size() > 0) {
                                    MsgReCallUtil.notifyHasDoWhileReply(this, "变量名" + varName + "已存在!总数:" + names.size() + "个.", item);

                                } else {
                                    VarBean varBean = new VarBean();
                                    varBean.setName(varName);
                                    varBean.setValue(varValue);
                                    long count = DBHelper.getVarTableUtil(_dbUtils).insert(varBean);

                                    if (count > 0) {
                                        MsgReCallUtil.notifyHasDoWhileReply(this, "添加变量成功!!\n变量名" + varName + "\n变量值:" + varValue + "\n插入后ID:" + count, item);

                                    } else {

                                        MsgReCallUtil.notifyHasDoWhileReply(this, "添加变量失败!!\n变量名" + varName + "\n变量值" + varValue + "\n操作结果:失败", item);
                                    }

                                }


                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(this, "变量传参错误,识别的键名 键值应该有2个,当前传递了" + split.length + "个 是否忘记添加|号了？请输入如:" + CmdConfig.CONFIG + " " + CmdConfig.ChildCmd.CONFIG_ADD_VAR + " 作者|情随事迁 那么输入" + CmdConfig.CONFIG + " " + CmdConfig.ChildCmd.CONFIG_PRINT + " $作者 就可以输出变量了!", item);

                            }
                        }

                    }


                    return true;
                    case CmdConfig.ChildCmd.CONFIG_PRINT:
                        String printContent = ParamParseUtil.mergeParameters(args, 1, args.length - 1);

                        if (TextUtils.isEmpty(printContent)) {
                            MsgReCallUtil.notifyHasDoWhileReply(this, "请输入要打印的内容,对于变量将自动进行转换输出! \n变量的格式:$变量名 或者$变量名(格式化参数0,参数1) \n如变量a定义的内容为 a(变量1=%s 变量2=%s)那么得到的结果输入" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_PRINT + " a(你好,我好)得到的结果是 变量1=你好 变量2=我好 \n注意:%s的总数一定要匹配，否则会出现异常.", item);
                        } else {
                            printContent = VarCastUtil.parseStr(item, _dbUtils, printContent);
                            MsgReCallUtil.notifyHasDoWhileReply(this, printContent, item);
                        }
                        return true;
                    case CmdConfig.ChildCmd.CONFIG_SQL:

                        if (TextUtils.isEmpty(argSecond)) {

                            MsgReCallUtil.notifyHasDoWhileReply(this, "sql语句帮助:\n支持变量$g=群号 $u=QQ $s=机器人自身QQ " +
                                            "\n表[群配置:groupconfig,luckmoney:收到的红包,var:表量表,vr:违规记录,vwr:群友触发的违规词记录,floor:楼层数据]" +
                                            "[" + DBHelper.getSuperManager(getDbUtils()).getTableName(AdminBean.class) + ":超管表," +
                                            "[" + DBHelper.getQQGroupManagerDBUtil(getDbUtils()).getTableName(TwoBean.class) + ":群管表," +
                                            "," + DBHelper.getKeyWordDBUtil(getDbUtils()).getTableName(ReplyWordBean.class) + ":词库表," +
                                            "," + DBHelper.getVarTableUtil(getDbUtils()).getTableName(VarBean.class) + ":变量表]" +
                                            "\n查询当前群配置" +
                                            "\n" + CmdConfig.CONFIG + "SQL select * from groupconfig where account=" + "\"$g\"" +// ignore_include
                                            "\n查询管理员" +
                                            "\n" + CmdConfig.CONFIG + "SQL select * from admin" +// ignore_include
                                            "\n禁用网络词库" +
                                            "\n" + CmdConfig.CONFIG + "SQL " + SQLCns.SQL_CONSTANT_DISENABLE_NET_WORK +

                                            "\n修改所有违规词禁言时长" +
                                            "\n" + CmdConfig.CONFIG + "SQL update " + DBHelper.getGagKeyWord(getDbUtils()).getTableName(GagAccountBean.class) + " set duration=60" +
                                            "\n群违规强制中断逻辑" +
                                            "\n" + CmdConfig.CONFIG + "SQL update groupconfig set breaklogic=1" +

                                            "\n删除词库" +
                                            "\n" + CmdConfig.CONFIG + "SQL delete from " + DBHelper.getKeyWordDBUtil(getDbUtils()).getTableName(ReplyWordBean.class) + " where answer like %红包% or ask like %红包%" +
                                            "\n添加变量name" +
                                            "\n" + CmdConfig.CONFIG + "SQL insert into " + DBHelper.getVarTableUtil(getDbUtils()).getTableName(VarBean.class) + "(name,value) values('你好" + new Random().nextInt(500) + "','测试变量数据')" +
                                            " \n如果列数据太多,您可以输入参数指定格式\n" +
                                            "-width 10表示每个单元格宽度为10" +
                                            "\n -fontlength 表示每个单元格字体不得超过多少的长度 " +
                                            "\n-format web 表示用网页来浏览 如果使用网页浏览，默认字体限制会自动调整足够大,避免挤在一坨",
                                    "\n更新生效需要执行:" + CmdConfig.CONFIG + CmdConfig.ChildCmd.CONFIG_RELOAD,
                                    item);


                            return true;
                        }

                        try {
                            int maxSpaceWidth = 100;
                            int fontMaxWidth = 100;

                            int startPosition = 1;
                            boolean byNetPrint = false;
                            boolean findFormat = false;
                            boolean ignoreVar = false;

                            argloop:
                            for (int i = startPosition; i < args.length; i++) {

                                String current = args[i];
                                if (current.trim().equals("-width")) {
                                    String value = ParamParseUtil.getArgByArgArr(args, i + 1);
                                    maxSpaceWidth = ParseUtils.parseInt(value);
                                    startPosition = i + 2;
                                    findFormat = true;
                                } else if (current.trim().equals("-igvar")) {//加速
                                    String value = ParamParseUtil.getArgByArgArr(args, i + 1);
                                    ignoreVar = ParseUtils.parseBoolean(value);
                                    startPosition = i + 2;
                                    findFormat = true;
                                } else if (current.trim().equals("-fontlength")) {
                                    String value = ParamParseUtil.getArgByArgArr(args, i + 1);
                                    fontMaxWidth = ParseUtils.parseInt(value);
                                    startPosition = i + 2;
                                    findFormat = true;
                                } else if (current.trim().equals("-format")) {
                                    String value = ParamParseUtil.getArgByArgArr(args, i + 1);
                                    if ("web".equals(value)) {
                                        byNetPrint = true;

                                        if (maxSpaceWidth <= 10) {
                                            maxSpaceWidth = 50;
                                        }

                                        if (fontMaxWidth <= 8) {
                                            fontMaxWidth = 30;
                                        }
                                    }
                                    findFormat = true;

                                    startPosition = i + 2;//值也算在里面也不包含当前所以才+2
                                }

                                if (i >= startPosition + 3) {//如果等于找到的索引+2 这下一波进入没找到， findFormat 不需要也可以断定
                                    break argloop;
                                }

                            }

                            if (byNetPrint) {

                            }

                            argSecond = ParamParseUtil.mergeParameters(args, startPosition, args.length - 1);

                            if (!ignoreVar) {
                                // ignore_start
                                argSecond = VarCastUtil.parseStr(item, _dbUtils, argSecond);

                                // ignore_end
                            }


                            if (argSecond.contains("select")) {

                                if (BuildConfig.DEBUG) {
                                    LogUtil.writeLog(TAG, "SQL:" + param);
                                }


                                final int finalMaxSpaceWidth = maxSpaceWidth;
                                final boolean finalByNetPrint = byNetPrint;
                                final int finalFontMaxWidth = fontMaxWidth;
                                final String finalArgSecond = argSecond;
                                new QssqTask<Object>(new QssqTask.ICallBack() {
                                    @Override
                                    public Object onRunBackgroundThread() {

                                        try {

                                            DBUtils.HashMapDBInfo info = AppContext.getDbUtils().queryAllSaveCollections(finalArgSecond);

                                            // 列名 ，列值
                                            StringBuffer sbMapHeader = new StringBuffer();
                                            if (finalByNetPrint) {
                                                sbMapHeader.append("<table border=\'1\' width=\'50%\' align=\"center\">");// ignore_include

                                                sbMapHeader.append("<caption>"
                                                        //ignore-end
                                                        + "情迁聊天机器人数据库查询"
                                                        + "</caption>");
                                            }

                                            //多上行也就是多少map 就会有多少buffer
                                            List<StringBuffer> lines = new ArrayList<>();//把stringbuffer保存起来，下次直接根据，index找。这个行号 也叫做一个map. 第一次是找不到的，只有第二次才能找到


                                            int columnIndex = 0;


                                            HashMap<String, String> rowKeys = info.getMaxRow();//values是没有作用不需要了的。
                                            if (rowKeys != null) {


                                                rowloop:
                                                for (Map.Entry<String, String> entry : rowKeys.entrySet()) {

                                                    String key = entry.getKey();

                                                    //处理表头
                                                    if (finalByNetPrint) {

                                                        if (columnIndex == 0) {
                                                            sbMapHeader.append("<tr>");//第一列
                                                        } else {

                                                        }


                                                        sbMapHeader.append("<th>");
                                                        sbMapHeader.append(key);//头部完成

                                                        sbMapHeader.append("</th>");//头部完成


                                                        if (columnIndex == info.getMaxRow().size() - 1) {//最后一列

                                                            sbMapHeader.append("</tr>");
                                                        }

                                                    } else if (info.getList().size() <= 5 && info.getMaxRow().size() > 3) {
                                                        sbMapHeader.append("" + entry.getKey() + ":\n");
                                                        sbMapHeader.append("[");
                                                        boolean beforeHasContain = false;
                                                        for (int i = 0; i < info.getList().size(); i++) {
                                                            LinkedHashMap<String, String> map = info.getList().get(i);
                                                            String s = map.get(entry.getKey());
                                                            if (!TextUtils.isEmpty(s)) {
                                                                if (beforeHasContain) {
                                                                    sbMapHeader.append(",");
                                                                    beforeHasContain = true;
                                                                }
                                                                sbMapHeader.append(s);
                                                            }


                                                        }
                                                        sbMapHeader.append("]");
                                                        sbMapHeader.append("\n");

                                                        continue;

                                                    } else {

                                                        String format = String.format("%-" + finalMaxSpaceWidth + "s", ParseUtils.parseMaxLengStr(key, finalFontMaxWidth));

                                                        sbMapHeader.append(format);//头部完成
                                                    }


                                                    int row = 0;
                                                    //处理当前columnIndex列 从上往下然后第二排 一次从上往下

                                                    for (HashMap<String, String> map : info.getList()) { //第一次 大循环，完成一列的查找 和填充表格  那么第二次的时候要拿到第到对应的好好的stringbunffer继续往右边拼接自己的行.
                                                        //拼接方式是 从上往下 以此 一列一列完成，完成第一列后第二列 需要拿到  自己的行号的stringbuffer继续拼接

                                                        StringBuffer sbCurrent = null;
                                                        if (columnIndex == 0) {//第一次外围循环 会全部添加到这里add

                                                            sbCurrent = new StringBuffer();

                                                            lines.add(sbCurrent);//第一行

                                                        } else {

                                                            sbCurrent = lines.get(row); //第2列 第2行要怎么取？  那么直接取 第2列。

                                                        }

                                                        String value = map.get(key);

                                                        if (finalByNetPrint) {
                                                            if (columnIndex == 0) {

                                                                sbCurrent.append("<tr>");
                                                            }
                                                            sbCurrent.append("<td>");
                                                            sbCurrent.append(value);
                                                            sbCurrent.append("</td>");
                                                            if (columnIndex == info.getMaxRow().size() - 1) {

                                                                sbCurrent.append("</tr>");
                                                            }


                                                        } else {

                                                            String valueFormat = String.format("%-" + finalMaxSpaceWidth + "s", ParseUtils.parseMaxLengStr(value, finalFontMaxWidth));
                                                            sbCurrent.append(valueFormat);

                                                        }

                                                        row++;
                                                        LogUtil.writeLog(TAG, "处理第" + columnIndex + "第" + row + "行完毕值:" + sbCurrent.toString());
                                                    }

                                                    columnIndex++;


                                                }

                                            }


                                            if (finalByNetPrint) {

//                                                sbMapHeader.append("<br/>");//头部表格完成
                                            } else {

                                                sbMapHeader.append("\n");
                                            }


                                            for (StringBuffer line : lines) {


                                                if (finalByNetPrint) {

                                                    sbMapHeader.append(line);//不需要进行br换行

//                                                    sbMapHeader.append("<br/>");//头部表格完成
                                                } else {
                                                    sbMapHeader.append(line + "\n");

                                                }

                                            }


                                            if (finalByNetPrint) {

                                                sbMapHeader.append("</table>");

                                            }

                                            String s = sbMapHeader.toString();
                                            if (finalByNetPrint) {
//https://blog.csdn.net/frankcheng5143/article/details/52939082
                                                NetQuery netQuery = new NetQuery();
                                                String urlMy = Cns.DOMAIN + "/robot/sql.html?info=" + AppUtils.encodeUrl(EncryptPassUtil.encryption("" + s, "lozn.top_luozheng"));
                                                ;
//                                                String urlMy = Cns.DOMAIN + "/robot/sql.html?info=" + EncryptPassUtil.encryption(""+s, "lozn.top_luozheng");;
                                                //   String urlMy = Cns.DOMAIN + "/robot/sql.html?info=" + AppUtils.encodeUrl(s);
                                                String urlRequest = mShortUrlTextApiUrl = "" + urlMy;
                                                //   String urlRequest="http://suo.im/api.php?url=" + urlMy;

                                                if (TextUtils.isEmpty(mShortUrlTextApiUrl)) {
                                                    String result = netQuery.sendGet(urlRequest);

                                                    return "查询完成,请打开" + result + "查看结果";
                                                } else {
                                                    return "查询完成,请打开" + urlMy + "查看结果";
                                                }

                                            } else {

                                                return "查询结果:\n" + s;
                                            }


                                        } catch (Exception e) {

                                            if (BuildConfig.DEBUG) {
                                                LogUtil.writeLog(TAG, "exception:" + Log.getStackTraceString(e));
                                            }
                                            return e;
                                        }

                                    }

                                    @Override
                                    public void onRunFinish(Object list) {
                                        if (list instanceof Exception) {
                                            if (BuildConfig.DEBUG) {
                                                LogUtil.writeLog(TAG, list.toString());
                                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "" + ((Exception) list).getMessage() + ",识别的sql语句:" + finalArgSecond, item.clone());
                                            }
                                        } else {


                                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "" + list, item);

                                        }
                                    }
                                }).execute();


                                // MsgReCallUtil.notifyHasDoWhileReply(this, "查询语句稍后才能出结果", item);

                            } else {

                                _dbUtils.execSQL(argSecond);
                                MsgReCallUtil.notifyHasDoWhileReply(this, "执行成功,无法得知结果,转义之后的sql: " + argSecond + " 如果是修改群配置信息,请输入重载命令进行刷新", item);
                            }


                        } catch (Exception e) {

                            MsgReCallUtil.notifyHasDoWhileReply(this, "sql:" + argSecond + "\n错误原因," + e.getMessage(), item);
                        }
                        return true;

                    case CmdConfig.ChildCmd.CONFIG_RELOAD:
                        initGroupSpConfig();
                        initBaseConfig();
                        initGroupWhiteNamesFromDb();
                        initIgnores();
                        initSuperManager();
                        initJAVAPlugin();
                        initJavascriptSPlugin();
                        initLuaPlugin();
                        NickNameUtils.clearFromMemory();
                        initGagWords();
                        MsgReCallUtil.notifyHasDoWhileReply(this, "执行完成,包含重载了插件，清空了昵称缓存，初始化类内存信息，重新读取了配置信息。", item);


                        return true;
                    case CmdConfig.ChildCmd.CONFIG_VIEW_PIC: {

                        String arg = ParamParseUtil.mergeParameters(args, 1);
                        if (TextUtils.isEmpty(arg)) {
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "请输入你想要看的本地图片路径", item);
                        } else {
                            if (new File(arg).exists()) {
                                MsgReCallUtil.notifySendPicMsg(RobotContentProvider.getInstance(), arg + "", item);

                            } else {
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "图片文件" + arg + "不存在", item);

                            }

                        }
                        return true;
                    }
                    case CmdConfig.ChildCmd.CONFIG_SCREENCAP: {

                        String arg = ParamParseUtil.mergeParameters(args, 1);

                        new QssqTaskFix<String, String>(new QssqTaskFix.ICallBackImp<String, String>() {
                            @Override
                            public String onRunBackgroundThread(String[] params) {
                                String path = TextUtils.isEmpty(params[0] + "") ? "/sdcard/robot.jpg" : params[0] + "";
                                File picFile = new File(path);
                                if (picFile.exists()) {
                                    picFile.delete();
                                }
                                String cmd = "screencap -p " + path;
                                final Pair<String, Exception> s = ShellUtil.executeAndFetchResultPair(cmd, new ICmdIntercept<String>() {
                                    @Override
                                    public boolean isNeedIntercept(String str) {
                                        MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), str, item);
                                        return false;
                                    }

                                    @Override
                                    public void onComplete(String name) {
                                        if (picFile.exists() && "输入流".equals(name)) {
                                            MsgReCallUtil.notifySendPicMsg(RobotContentProvider.getInstance(), picFile.getAbsolutePath() + "", item);

                                        }

                                    }
                                });

                                if (s.first != null) {
                                    return s.first;
                                } else if (s.second != null) {
                                    return "[执行错误]" + s.second.getMessage();
                                }

                                return "未知错误";

                            }

                            @Override
                            public void onRunFinish(String o) {


                                if (o != null) {
                                    MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                                }


                            }
                        }).execute(arg);
                    }
                    return true;
//                        screencap -p /sdcard/screenshots/01.png
                    case CmdConfig.ChildCmd.CONFIG_MODIFY:
                        String argValue = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgThrid);
                        String argByArgArrType = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFourth);
                        Object writeValue = null;
                        if (TextUtils.isEmpty(argSecond) || TextUtils.isEmpty(argValue)) {

                            MsgReCallUtil.notifyHasDoWhileReply(this, "需要携带2个参数， 键名和键值，(键值boolean只能为true何false,否则为字符串 但是如果不存在的字符串需要继续补参数为类型[string,strings(,分割),float,int,boolean,long],另外如果需要写入int 则需要在值后面编写int,不知道有哪些配置可以输入子命令`" + CmdConfig.ChildCmd.CONFIG_SHOW + "`查看)修改后需要调用重载命令进行重载,当前键值:" + argSecond, item);
                            return true;

                        }


//                            String argSecond = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
                        if (argValue.equalsIgnoreCase("null")) {
                            argValue = "";
                        }
                        Object beforeObj = sharedPreferences.getAll().get(argSecond);
                        boolean containKey = beforeObj != null;
                        String type = "未知";
                        boolean result = false;
                        if (argValue.toLowerCase().equals("true") || argValue.toLowerCase().equals("false") || (argByArgArrType != null && argByArgArrType.equals("boolean"))) {
                            sharedPreferences.edit().apply();

                            writeValue = ParseUtils.parseBoolean(argValue);
                            result = sharedPreferences.edit().putBoolean(argSecond, (Boolean) writeValue).commit();
                            type = "boolean";

                        } else if (argByArgArrType != null && argByArgArrType.equals("int")) {
                            sharedPreferences.edit().apply();
                            writeValue = ParseUtils.parseInt(argValue);
                            result = sharedPreferences.edit().putInt(argSecond, (Integer) writeValue).commit();
                            type = "int";

                        } else if (argByArgArrType != null && argByArgArrType.toLowerCase().equals("strings")) {
                            sharedPreferences.edit().apply();
                            Set<String> set = new TreeSet();
                            String[] split = argValue != null ? argValue.split(",") : null;
                            if (split != null) {
                                for (String s : split) {
                                    set.add(s);
                                }

                            }

                            writeValue = set;
                            result = sharedPreferences.edit().putStringSet(argSecond, (Set<String>) writeValue).commit();
                            type = "strings";

                        } else if (argByArgArrType != null && argByArgArrType.equals("long")) {
                            sharedPreferences.edit().apply();
                            writeValue = ParseUtils.parseLong(argValue);
                            result = sharedPreferences.edit().putLong(argSecond, (Long) writeValue).commit();
                            type = "long";

                        } else if (argByArgArrType != null && argByArgArrType.equals("float")) {
                            sharedPreferences.edit().apply();
                            writeValue = ParseUtils.parseFloat(argValue);
                            result = sharedPreferences.edit().putFloat(argSecond, (Float) writeValue).commit();
                            type = "float";

                        } else {

                            if (containKey || argByArgArrType != null && argByArgArrType.toLowerCase().equals("string")) {//根据之前的类型推算
                                try {
                                    type = "string";

                                    writeValue = argValue;
                                    boolean writeConfig = writeConfig(beforeObj, argSecond, (String) writeValue);


                                    if (writeConfig) {


                                    } else {

                                        MsgReCallUtil.notifyHasDoWhileReply(this, "操作失败\n未知的类型:" + beforeObj.getClass() + ",如果您觉得不支持此配置是bug请联系作者", item);
                                        return true;
                                    }

                                } catch (Exception e) {
                                    MsgReCallUtil.notifyHasDoWhileReply(this, "操作失败\n类型无法转换,错误信息：" + e.getMessage() + "", item);

                                }


                            } else {

                                MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(this, "不包含的值" + argSecond + "暂时不支持存放,避免出现严重错误，您可以继续加上参数 string或者boolean强制写入", item);
                                return true;
                            }
                        }

                        MsgReCallUtil.notifyHasDoWhileReply(this, "操作完成\n是否首次提交:" + containKey
                                + "\n是否成功:" + result
                                + "\n键名 :" + argSecond
                                + "\n键值 :" + writeValue
                                + "\n数据类型:" + type + "\n上次的值:" + beforeObj, item);


                        return true;
                    case CmdConfig.ChildCmd.CONFIG_SHOW:
                        StringBuffer sb = new StringBuffer();


                        Set<? extends Map.Entry<String, ?>> set = sharedPreferences.getAll().entrySet();
                        sb.append("总共有" + set.size() + "个数据(不是所有都是机器人的)\n");
                        for (Map.Entry<String, ?> entry : set) {

                            if (!entry.getKey().startsWith("key_")) {
                                continue;
                            }
                            String typeCurrent = "";
                            if (entry.getValue() == null) {
                                typeCurrent = "未知";
                            } else {
                                typeCurrent = entry.getValue().getClass().getSimpleName();
                            }
                            sb.append(String.format("键名:%s\n键值:%s\n类型:%s\n==========\n", entry.getKey(), entry.getValue(), typeCurrent));
                        }

                        MsgReCallUtil.notifyHasDoWhileReply(this, "" + sb.toString(), item);

                        return true;
                }


                MsgReCallUtil.notifyHasDoWhileReply(this, "填写错误,是否忘记填写空格了??识别到的子命令为" + argFirst + ",参数为:" + argSecond, item);
            }

            return true;

            case CmdConfig.KICK:
            case CmdConfig.KICK_1:
            case CmdConfig.KICK_2: {
                if (atPair != null && atPair.first == false) {

                    String first = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                    if (!TextUtils.isEmpty(first) && !RegexUtils.checkIsContainNumber(first)) {
                        return false;
                    }
                }

                if (isgroupMsg && nameBean != null) {
                    boolean isCurrentGroupAdmin = isSelf || isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                    nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                }
                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean, true)) {
                    return false;
                }
                if (isgroupMsg) {

                    doKickFromGroupMsgCmd(item, isManager, nameBean, args, atPair);
                } else {
                    doKickCmdPrivateMsgCmd(item, isManager, nameBean, args);
                }
                return true;

            }
            case CmdConfig.VOICE_CALL: {
                if (atPair != null && atPair.first == false) {

                    String first = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                    if (!TextUtils.isEmpty(first) && !RegexUtils.checkIsContainNumber(first)) {
                        return false;
                    }
                }

                if (isgroupMsg && nameBean != null) {
                    boolean isCurrentGroupAdmin = isSelf || isCurrentGroupAdminFromDb(nameBean, item.getSenderuin(), item.getFrienduin());
                    nameBean.setIsCurrentGroupAdmin(isCurrentGroupAdmin);

                }
                if (isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean, true)) {
                    return false;
                }
                String qq = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (TextUtils.isEmpty(qq)) {

                    String msg = "让机器人拨打电话请填写qq号码";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                    return true;
                }
                if (!RegexUtils.iseQQ(qq)) {
                    String msg = "请填写qq号。";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                    return true;
                }
                MsgReCallUtil.notifyHasDoWhileReply(this, "已尝试呼叫" + qq + "，请确保宿主版本支持!", item);
                MsgReCallUtil.notifySendVoiceCall(this, qq, item);

                return true;

            }

            case CmdConfig.DELETE_WORD_CMD:
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }

                String ask = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (ask == null) {

                    String msg = "无法添加词库,不能识别词库[问]请检查语法语法是否正确";
                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

                } else {
                    ReplyWordBean bean = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumn(ReplyWordBean.class, FieldCns.ASK, ask);
                    String msg;
                    if (bean == null) {
                        msg = "词库不存在 无法删除";
                    } else {

                        int result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).deleteById(ReplyWordBean.class, bean.getId());
                        if (result > 0) {
                            bean.setId((int) result);
                            msg = String.format("删除词库成功\n问:%s\n答:%s", ask, bean.getAnswer());
                            initWordMap();
                        } else {
                            msg = "添加删除词库失败,数据库错误 inser effect count:" + result;
                        }
                    }

                    MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                }


                break;
            case CmdConfig.IGNORE_QUXIAO_PIGNBI:
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                if (isgroupMsg) {
                    String account = args.length > 0 ? args[0] : item.getFrienduin();
                    MemoryIGnoreConfig.removeIgnoreGroupNo(account);
                    String msg = "群" + account + "已取消屏蔽 本次有效";
                    MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);
                } else {
                    String account = args.length > 0 ? args[0] : item.getFrienduin();
                    MemoryIGnoreConfig.removeIgnorePerson(account);
                    String msg = "QQ_IGNORES" + account + "已取消屏蔽 本次有效";
                    MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);
                }
                break;

            case CmdConfig.SHOW_JIANRONG: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }

                String apptype = item.getApptype();
                StringBuffer sb = new StringBuffer();
                if (TextUtils.isEmpty(apptype)) {
                    sb.append("无法查询宿主信息(您的宿主软件[内置或插件版本过老])\n");
                } else {
                    if (apptype.contains("plugin")) {
                        sb.append("宿主软件:插件");
                        int index = apptype.lastIndexOf("_");
                        boolean supportMusic = false;
                        int code = Integer.parseInt(apptype.substring(index + 1, apptype.length()));
                        if (code >= 70) {
                            supportMusic = true;
                            sb.append("\n");
                            sb.append("是否支持点歌:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "支持");
                        } else if (code == 69) {
                            if (apptype.contains("1.5.2"))
                                sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持点歌:" + "支持");

                        } else if (code >= 68) {

                            sb.append("是否支持点歌:" + "不支持");
                            sb.append("\n");
                            sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持点歌:" + "支持");

                        } else if (code == 56) {
                            sb.append("宿主软件:内置(bug)");
                            sb.append("\n");
                            sb.append("是否支持点歌:" + "不支持");
                            sb.append("\n");
                            sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "支持");
                        } else {
                            sb.append("是否支持点歌:" + "不支持");
                            sb.append("\n");
                            sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "支持");


                        }
                    } else if (apptype.contains("insert")) {

                        sb.append("宿主软件:内置");
                        sb.append("\n");
                        int index = apptype.lastIndexOf("_");
                        boolean supportMusic = false;
                        int code = Integer.parseInt(apptype.substring(index, apptype.length()));
                        if (code > 56) {
                            sb.append("是否支持点歌:" + "支持");
                            sb.append("\n");

                            sb.append("是否支持禁言:" + "支持");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "支持");
                        } else {

                            sb.append("是否支持点歌:" + "未知");
                            sb.append("\n");
                            sb.append("是否支持禁言:" + "未知");
                            sb.append("\n");
                            sb.append("是否支持踢人:" + "未知");

                        }

                        return true;


                    }
                }
                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + sb.toString(), item);
                return true;
            }

            case CmdConfig.VERSION_UPDATE: {

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                String argByArgArr = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (!TextUtils.isEmpty(argByArgArr)) {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                sb.append("机器人软件版本:" + BuildConfig.VERSION_NAME + " build " + BuildConfig.VERSION_CODE + "\n");
                sb.append("更新内容:" + UpdateLog.getLastLog() + "\n");
                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + sb.toString(), item);
                break;
            }
            case CmdConfig.QUERY_iP: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                String cmdContent = item.getMessage().replace(CmdConfig.QUERY_iP, "").trim();
                String url = "http://ip-api.com/json/?lang=zh-CN";
                HttpUtilOld.queryData(url, new RequestListener() {
                    @Override
                    public void onSuccess(String str) {

                        if (str.length() <= 200) {

                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "打开成功:" + str, item);
                        } else {
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "打开成功,前50/" + str.length() + "个字符串:" + str.substring(0, 200), item);

                        }
                    }

                    @Override
                    public void onFail(String str) {
                        MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.this, "访问网址:" + url + "失败," + str, item);
                    }
                });
                return true;
            }
            case CmdConfig.NAT_TRAVERSE: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }

                StringBuffer sb = new StringBuffer();
                String wifiIP = AppUtils.getWifiIP();
                sb.append("\nIP地址" + wifiIP);

                String cmdContent = item.getMessage().replace(CmdConfig.NAT_TRAVERSE, "").trim();
                String cmd;
                if (TextUtils.isEmpty(cmdContent)) {
                    cmd = "";
                } else {
                    if (cmdContent.length() < 3) {
                        cmd = " " + sharedPreferences.getString(Cns.NAT_TRAVERSE_CMD, "_not_define_needtoken");
                    } else {
                        sharedPreferences.edit().putString(Cns.NAT_TRAVERSE_CMD, "qty437ofhff8ekungnpbi824d4hb5qfs:5899853").commit();
                        cmd = " " + cmdContent;
                    }
                }
                String finalCmd = cmd;
                long start = System.currentTimeMillis();
                new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                    @Override
                    public String onRunBackgroundThread(StringBuffer[] params) {
                        StringBuffer sb = params[0];
                        boolean[] waiting = {true, true};
                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;setprop service.adb.tcp.port 5555;frpc.sh" + finalCmd}, new ICmdIntercept<String>() {
                            //                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(new String[]{"echo before adb.tcp.port;getprop service.adb.tcp.port;frpc.sh"}, new ICmdIntercept<String>() {
                            @Override
                            public boolean isNeedIntercept(String bean) {
                                sb.append(bean);
                                return false;
                            }

                            @Override
                            public void onComplete(String name) {
                                if (name != null && name.contains("错误")) {

                                    waiting[0] = false;
                                } else {
                                    waiting[1] = false;

                                }
                                long end = System.currentTimeMillis();

                                sb.append("[" + name + "]" + "查询[耗时" + (end - start) + "ms]\n");

                            }
                        });

                        try {
                            long waitTime = 0;
                            while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                waitTime += 10;
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sb.insert(0, stringExceptionPair.first);
                        return sb.toString();

                    }

                    @Override
                    public void onRunFinish(String o) {
                        if (o != null) {
                            item.setMessage(o);
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                        }

                    }
                }).execute(sb);
                break;
            }
            case CmdConfig.WIFI_ADB: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                String wifiIP = AppUtils.getWifiIP();
                sb.append("\nIP地址" + wifiIP);
                sb.append("\nWIFI名:" + AppUtils.getSSID() + "");
                sb.append("\n电脑输入:adb connect " + wifiIP + ":5555\n");
                long start = System.currentTimeMillis();
                new QssqTaskFix<StringBuffer, String>(new QssqTaskFix.ICallBackImp<StringBuffer, String>() {
                    @Override
                    public String onRunBackgroundThread(StringBuffer[] params) {
                        StringBuffer sb = params[0];
                        boolean[] waiting = {true, true};
                        String[] strings = {"echo before adb.tcp.port;getprop service.adb.tcp.port;setprop service.adb.tcp.port 5555;echo setport over;stop adbd;echo stop adb over;start adbd;echo start adb over!"};
                        ICmdIntercept<String> stringExceptionPair1 = new ICmdIntercept<String>() {
                            @Override
                            public boolean isNeedIntercept(String bean) {
                                sb.append(bean);
                                return false;
                            }

                            @Override
                            public void onComplete(String name) {
                                if (name != null && name.contains("错误")) {

                                    waiting[0] = false;
                                } else {
                                    waiting[1] = false;

                                }
                                long end = System.currentTimeMillis();

                                sb.append("[" + name + "]" + "查询[耗时" + (end - start) + "ms]\n");

                            }
                        };
                        Pair<String, Exception> stringExceptionPair = ShellUtil.executeAndFetchResultPair(strings, stringExceptionPair1);

                        try {
                            long waitTime = 0;
                            while ((waiting[0] || waiting[1]) && waitTime < 10000) {
                                waitTime += 10;
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sb.insert(0, stringExceptionPair.first);
                        return sb.toString();

                    }

                    @Override
                    public void onRunFinish(String o) {


                        if (o != null) {
                            item.setMessage(o);
                            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), o, item);

                        }

                    }
                }).execute(sb);
                break;
            }
            case CmdConfig.STATE_INFO: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                String argByArgArr = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (!TextUtils.isEmpty(argByArgArr)) {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                sb.append("机器人软件版本:" + BuildConfig.VERSION_NAME + " build " + BuildConfig.VERSION_CODE + "\n");
                sb.append("编译时间:" + BuildConfig.BUILD_TIME_STR + "\n");
                long distance = System.currentTimeMillis() - AppContext.getStartupTime();
                sb.append("本软件已稳定运行:" + DateUtils.generateTimeDetail(distance) + "\n");
                sb.append("宿主信息:" + item.getApptype() + "\n");
                sb.append("是否插件化加载:" + ParseUtils.parseBoolean2ChineseBooleanStr(isAsPluginLoad()) + "\n");
                sb.append("宿主是否已绑定服务:" + ParseUtils.parseBoolean2ChineseBooleanStr(RemoteService.isIsInit()) + "\n");
                sb.append("宿主进程:" + RemoteService.getProcessName() + "\n");
                sb.append("消息运行线程:" + Thread.currentThread().getName() + "\n");
                sb.append("宿主版本号:" + item.getVersion() + "\n");
                sb.append("IP地址" + AppUtils.getWifiIP() + "\n");
                sb.append("WIFI名:" + AppUtils.getSSID() + "\n");
                sb.append("型号:" + " " + Build.MODEL + "\nSDK_INT:" + Build.VERSION.SDK_INT);


                new QssqTaskFix<String, String>(new QssqTaskFix.ICallBackImp<String, String>() {
                    @Override
                    public String onRunBackgroundThread(String[] params) {
                        final Pair<String, Exception> s = ShellUtil.executeAndFetchResultPair(params[0], new ICmdIntercept<String>() {
                            @Override
                            public boolean isNeedIntercept(String str) {
                                MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), str, item);
                                return false;
                            }

                            @Override
                            public void onComplete(String name) {

                            }
                        });

                        if (s.first != null) {
                            return s.first;
                        } else if (s.second != null) {
                            return "[执行错误]" + s.second.getMessage();
                        }

                        return "未知错误";

                    }

                    @Override
                    public void onRunFinish(String o) {

                        MsgReCallUtil.notifyJoinMsgNoJump(RobotContentProvider.getInstance(), "" + sb.toString() + "\nCPU温度:" + o, item);


                    }
                }).execute("cat /sys/class/thermal/thermal_zone0/temp");


                //                配置运行 cat /sys/class/thermal/thermal_zone0/temp


                break;
            }
            case CmdConfig.VERSION:

                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                String argByArgArr = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
                if (!TextUtils.isEmpty(argByArgArr)) {
                    return false;
                }

                StringBuffer sb = new StringBuffer();
                sb.append("(BWN)机器人软件版本:" + BuildConfig.VERSION_NAME + " build " + BuildConfig.VERSION_CODE + "\n");
                sb.append("编译时间:" + BuildConfig.BUILD_TIME_STR + "\n");
                long distance = System.currentTimeMillis() - AppContext.getStartupTime();
                sb.append("本软件已稳定运行:" + DateUtils.generateTimeDetail(distance) + "\n");
                sb.append("宿主信息:" + item.getApptype() + "\n");
                sb.append("是否插件化加载:" + ParseUtils.parseBoolean2ChineseBooleanStr(isAsPluginLoad()) + "\n");
                sb.append("宿主是否已绑定服务:" + ParseUtils.parseBoolean2ChineseBooleanStr(RemoteService.isIsInit()) + "\n");
                sb.append("宿主进程:" + RemoteService.getProcessName() + "\n");
                sb.append("消息运行线程:" + Thread.currentThread().getName() + "\n");
                sb.append("Q/TIM版本号:" + item.getVersion() + "\n");

                sb.append("是否已开启私聊自动回复:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfprivateReply) + "\n");
                sb.append("私聊模式被屏蔽下管理员依然可以回复:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfprivateReplyManagrIgnoreRule) + "\n");
                sb.append("是否已开启私聊本地词库没找到启用网络词库回复:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfBaseEnableNetRobotPrivate) + "\n");
                sb.append("是否开启本地词库回复:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfBaseEnableLocalWord));
                sb.append("\n");
                sb.append("是否全局禁用点歌:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfBaseDisableStructMsg));
                sb.append("\n");
                String[] stringArray = getResources().getStringArray(R.array.musicoptions);
                if (musicType <= stringArray.length - 1) {

                    sb.append("点歌引擎:" + stringArray[musicType]);
                } else {
                    sb.append("音乐数组越界 :" + musicType + ",maxLength:" + stringArray.length);
                }
                sb.append("\n");
                sb.append("艾特模式模式白名单是否无需艾特:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfBaseWhiteNameReplyNotNeedAite));
                sb.append("\n");
/*                sb.append("非白名单艾特是否可以触发聊天:" + mCfNotWhiteNameReplyIfAite);
                sb.append("\n");*/
/*                sb.append("回复自动携带发言者昵称(艾特):" + mCfBaseReplyShowNickName);
                sb.append("\n");*/
                sb.append("是否已开启是群聊自动回复:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfeanbleGroupReply));
                sb.append("\n");
       /*         sb.append("是否已开启是群聊网络词库回复:" + mCfBaseEnableNetRobotGroup);
                sb.append("\n");*/
   /*             sb.append("是否禁用本地词库回复:" + mCfBaseEnableNetRobotGroup);
                sb.append("\n");*/
                sb.append("是否只回复指定群白名单:" + ParseUtils.parseBoolean2ChineseBooleanStr(mCfOnlyReplyWhiteNameGroup) + "\n");
/*                sb.append("是否开启群聊需要艾特:" + ConfigUtils.replyNeedAt(this));
                sb.append("\n");*/
//                sb.append("是否开启群内发言监控(刷屏/违规词/违规红包禁言):" + mCFBaseEnableCheckKeyWapGag + "\n");

                if (isgroupMsg) {

                    sb.append("当前群是否已临时屏蔽:" + ParseUtils.parseBoolean2ChineseBooleanStr(MemoryIGnoreConfig.isIgnoreGroupNo(item.getFrienduin())) + "\n");
                    //groupMsgLessSecondIgnore
                    sb.append("本群是否在白名单:" + ParseUtils.parseBoolean2ChineseBooleanStr(AccountUtil.isContainAccount(mQQGroupWhiteNames, item.getFrienduin(), true)) + "\n");

                    if (mCfOnlyReplyWhiteNameGroup) {
                        GroupWhiteNameBean bean = AccountUtil.findAccount(mQQGroupWhiteNames, item.getFrienduin(), false);
                        if (bean != null) {
                            sb.append("┌┈┈┈┈┈┈┈┐\n" + bean.getConfig());
                            sb.append("└┈┈┈┈┈┈┈┘\n");

                        }
                    }
//                            mQQGroupWhiteNames.contains(item.getFrienduin()));
                } else if (MsgTyeUtils.isPrivateMsg(item)) {
                    sb.append("当前QQ是否已临时屏蔽:" + ParseUtils.parseBoolean2ChineseBooleanStr(MemoryIGnoreConfig.isTempIgnorePerson(item.getFrienduin())) + "\n");
                    sb.append("当前QQ是否被永久屏蔽:" + ParseUtils.parseBoolean2ChineseBooleanStr(AccountUtil.isContainAccount(mIgnoreQQs, item.getFrienduin(), true)) + "\n");
//                    sb.append("当前QQ是否被永久屏蔽:" + mIgnoreQQs.contains(item.getFrienduin()));
                }


                sb.append("忽略历史消息间隔毫秒:" + IGnoreConfig.distanceNetHistoryTimeIgnore + "\n");
                sb.append("重复消息忽略秒:" + IGnoreConfig.distancedulicateCacheHistory + "\n");
                sb.append("重复群消息忽略毫秒:" + IGnoreConfig.groupMsgLessSecondIgnore + "\n");
                sb.append("启动时间间隔忽略毫秒:" + IGnoreConfig.distanceStatupTimeIgnore + "\n");
                sb.append("\n");

           /*     IGnoreConfig.distanceNetHistoryTimeIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_second_history_msg), IGnoreConfig.distanceNetHistoryTimeIgnore);
                IGnoreConfig.distancedulicateCacheHistory = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_than_second_msg), getDefaultIntegerValue(R.integer.key_base_ignore_than_second_msg_duration));
                IGnoreConfig.distanceStatupTimeIgnore = sharedPreferences.getLong(getResources().getString(R.string.key_base_ignore_second_statup_time), getDefaultIntegerValue(R.integer.default_startup_time_distance_ms));

*/

                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + sb.toString(), item);
                break;
            case CmdConfig.CLEAR_PINBI:
            case CmdConfig.CLEAR_PINBI_1: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                int sizePserson = MemoryIGnoreConfig.getIgnorePersonMap().size();
                int sizeGroup = MemoryIGnoreConfig.getIgnoreGroupMap().size();
                MemoryIGnoreConfig.getIgnoreGroupMap().clear();
                MemoryIGnoreConfig.getIgnorePersonMap().clear();
                String msg = "已清除屏蔽,群" + sizeGroup + "个,QQ_IGNORES" + sizePserson + "个";
                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);
            }
            break;
            case CmdConfig.ADD_WHITE_NAMES:
            case CmdConfig.ADD_WHITE_NAMES_1:
            case CmdConfig.ADD_WHITE_NAMES_2:
            case CmdConfig.ADD_WHITE_NAMES_3: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                if (MsgTyeUtils.isPrivateMsg(item)) {
                    {
                        if (args.length == 0) {
                            String msg = Cns.PRIVATE_MSG_MUST_INCLUDE_QQGROUP;
                            MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                            return false;
                        }
                    }
                }
                String account = args.length > 0 ? args[0] : item.getFrienduin();

                if (AccountUtil.isContainAccount(mQQGroupWhiteNames, account, false)) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "无法把群" + account + "加入白名单, 因为已经存在!", item);
                    return false;
                } else {
                    GroupWhiteNameBean bean = (GroupWhiteNameBean) new GroupWhiteNameBean().setAccount(account);
                    long insert = DBHelper.getQQGroupWhiteNameDBUtil(_dbUtils).insert(bean);
                    mQQGroupWhiteNames.add(bean);
                    String msg = "群" + account + "已添加到白名单,是否成功持久化=" + ParseUtils.parseBoolean2ChineseBooleanStr((insert > 0));
                    MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);

                }
            }
            break;
            case CmdConfig.REMOVE_WHITE_NAMES:
            case CmdConfig.REMOVE_WHITE_NAMES_1:
            case CmdConfig.REMOVE_WHITE_NAMES_2:
            case CmdConfig.REMOVE_WHITE_NAMES_3:
            case CmdConfig.REMOVE_WHITE_NAMES_4:
            case CmdConfig.REMOVE_WHITE_NAMES_5:
            case CmdConfig.REMOVE_WHITE_NAMES_6: {
                if (isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isgroupMsg, nameBean)) {
                    return false;
                }
                if (MsgTyeUtils.isPrivateMsg(item)) {
                    if (args.length == 0) {
                        String msg = Cns.PRIVATE_MSG_MUST_INCLUDE_QQGROUP;
                        MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);
                        return false;
                    }
                }
                String account = args.length > 0 ? args[0] : item.getFrienduin();

                boolean remove = AccountUtil.removeAccount(mQQGroupWhiteNames, account);
                String msg = null;
                if (remove) {

                    int i = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).deleteByColumn(GroupWhiteNameBean.class, FieldCns.FIELD_ACCOUNT, account);
                    msg = "群" + account + "已从白名单中移除成功,永久生效,结果:" + (i > 0);


                } else {
                    msg = "" + account + "没有在白名单中 无法移除";

                }
                MsgReCallUtil.notifyJoinMsgNoJump(this, "" + msg, item);

            }
            break;
            default:
                if (BuildConfig.DEBUG) {
                    LogUtil.writeLog("忽略自己的消息,因为非命令消息");
                }
                return false;
        }

        return true;
    }

    private void doInsertNewGagBean(GagAccountBean object) {
        mGagKeyWords.add(object);
    }

    private boolean doCardLogic(MsgItem item, String[] args, String firstArg, int startPosition) {
        boolean notSafeCheck = false;
        if (TextUtils.isEmpty(firstArg)) {


        /*    String msg = String.format(CardHelper.demo, "输入配置 卡片 xml内容",
                    "输入配置举例",
                    "输入配置 卡片 不安全 xml内容",
                    "多种方式"
                    , "供您选择"
                    , "赶快试试吧");*/
            MsgReCallUtil.notifyHasDoWhileReply(this, "请传递要转换为卡片消息的xml内容，手机端群消息一般可以看到效果，注意卡片消息发频繁会被系统屏蔽哦。 \n" + AppConstants.EXAMPLE_FORMAT, item);

            return true;
        } else if (firstArg.equals("举例")) {


       /*     MusicCardInfo cardInfo=new MusicCardInfo();
            cardInfo.setActionData("alipays://platformapi/startapp?saId=10000007&lientVersion=3.7.0.0718&qrcode=https://qr.alipay.com/c1x09104vwt0xobp1vmrr63");
            cardInfo.setAudioCover(Cns.DEFAULT_ROBOT_ICON);
            cardInfo.setDuration(3000);
            cardInfo.setAuthor("情随事迁");
            cardInfo.setMusictitle("这是标题");
            cardInfo.setSharesource("情迁聊天机器人");
            cardInfo.setActionData("http://fs.open.kugou.com/d9a0a78fb63f6bd82831395bf18f35bd/5b892e90/G052/M00/1A/15/1IYBAFaeC92AVl3UAEU5UA5cCgc155.mp3");
            cardInfo.setUrl("http://lozn.top");;
            cardInfo.setTitlebrief("测试");

            cardInfo.setExtraStr("notbody");*/
            String msg = String.format(CardHelper.demo, "情迁聊天机器人", "http://lozn.top", "永久免费/跨平台", "我是情迁", "我为自己代言", "赶快下载吧");
            MsgReCallUtil.notifyHasDoWhileReply(this, "" + msg, item);

            return true;
        } else if (firstArg.equals("不安全")) {
            notSafeCheck = true;

        }


        String infos = ParamParseUtil.mergeParameters(args, notSafeCheck ? startPosition + 1 : startPosition);

        infos = VarCastUtil.parseStr(item, _dbUtils, infos);
        if (infos == null || infos.indexOf(">") == -1 || infos.indexOf("version") == -1 || infos.indexOf("<") == -1 || infos.indexOf("<?xml") == -1) {

            String tip = "";
            if (infos != null && infos.indexOf("$") != -1) {
                tip = "(检测到你传递的是变量转xml格式,解析之后的文本是:﹛" + infos + "﹜请检查是否成功解析)";
            }
            MsgReCallUtil.notifyHasDoWhileReply(this, "你输入的并不是卡片xml字符串,\n" + AppConstants.EXAMPLE_FORMAT + "\n\n" + tip, item);
            return true;
        }

        if (!notSafeCheck) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //创建一个DocumentBuilder的对象
            //创建DocumentBuilder对象
            try {
                DocumentBuilder db = null;
                db = dbf.newDocumentBuilder();
                Document document = db.parse(new InputSource(new ByteArrayInputStream(infos.getBytes(), 0, infos.getBytes().length)));

                NodeList nodelist = document.getElementsByTagName("msg");
                if (nodelist == null) {
                    MsgReCallUtil.notifyHasDoWhileReply(this, "没有找到msg节点", item);
                    return true;
                } else {
                    MsgReCallUtil.notifMusicCardJump(RobotContentProvider.this, item, infos);
                }

            } catch (Exception e) {
                String message = e.getMessage();

                MsgReCallUtil.notifyHasDoWhileReply(this, "执行失败" + message
                        , item);


            }
        } else {
            MsgReCallUtil.notifMusicCardJump(RobotContentProvider.this, item, infos);


        }
        return false;
    }

    public boolean isNeedIgnoreManagerCommand(MsgItem item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer flag, boolean isManager, boolean isGroupMsg, GroupWhiteNameBean nameBean) {


        return isNeedIgnoreManagerCommand(item, atPair, flag, isManager, isGroupMsg, nameBean, false);
    }

    public boolean isNeedIgnoreManagerCommand(MsgItem
                                                      item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer
                                                      flag, boolean isManager, boolean isGroupMsg, GroupWhiteNameBean nameBean, boolean allowAt) {


        if (flag < INeedReplayLevel.ANY) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
            if (!isGroupMsg) {

                if (isManager) {
                    return false;//私聊消息是管理员可以操作
                } else {
                    return true;//是不是管理员的私聊消息进行拦截中断，
                }
            }
            if (atPair.first && atPair.second.first) {
            } else {

                return true;
            }

        }

        if (!isManager) {
            if (item != null) {

                boolean needAt = ConfigUtils.IsNeedAt(nameBean);
                if ((needAt && atPair.second.first) || !needAt) {//如果需要艾特，且不是管理员,但是艾特机器人或者不需要艾特都,就理睬告诉他 这里无视自身命令是否不需要艾特。
                    MsgReCallUtil.notifyNotManagerMsg(this, item);
                }
            }
            return true;
        } else {


            if (atPair.first && atPair.second.first == false) {//如果没有艾特自己但是艾特了别人忽略 除非艾特了自己
                if (allowAt) {
                    return false;
                }
                return true;
            } else {
                if (ConfigUtils.IsNeedAt(nameBean)) {

                    if (nameBean != null && nameBean.isSelfcmdnotneedaite() == false) {
                        if (!item.getSenderuin().equals(item.getSelfuin())) {//是管理员又如何，除非是自己
                            return true;
                        }
                    }
                }
            }

        }

        return false;


    }

    /**
     * 这不是非常危险的权限，因此允许普通管理也可以操作
     *
     * @param item
     * @param atPair
     * @param flag
     * @param isManager
     * @param selfMsg
     * @param isgroupMsg
     * @param nameBean
     * @return
     */

    public boolean isNeedIgnoreNormalCommand(MsgItem
                                                     item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer
                                                     flag, boolean isManager, boolean selfMsg, boolean isgroupMsg, GroupWhiteNameBean nameBean) {


        if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
            if (!isgroupMsg) {//说明没开启这个功能
                return true;
            }
            if (atPair.first && atPair.second.first) {
                if (!isManager && !(nameBean != null && nameBean.isCurrentGroupAdmin())) {
                    return true;//正常命令非管理不允许发送
                } else {
                }
            } else {

                return true;
            }

        } else {
            if (!isgroupMsg) {
                return false;//不判断直接允许  因为私聊没有这个说法
            }
        }


        if (ConfigUtils.IsNeedAt(nameBean)) {
            if (atPair.second.first == false) {

                if (nameBean != null && nameBean.isSelfcmdnotneedaite() == false) {//需要艾特 也没开启自身命令忽视。
                    if (!selfMsg) {//是管理员又如何，除非是自己
                        return true;
                    }
                }
            }

        }
        if (atPair.first && atPair.second.first == false) {//如果没有艾特自己但是艾特了别人忽略 除非艾特了自己

            if (selfMsg) {
                return false;
            }
            return true;
        } else {

            return false;
        }


    }

    public boolean isNeedIgnoreXManagerCommand(MsgItem
                                                       item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer
                                                       flag, boolean isManager, GroupWhiteNameBean nameBean) {

        return isNeedIgnoreXManagerCommand(item, atPair, flag, isManager, nameBean, false);

    }

    /**
     * 可以艾特也可以不爱特的模式，
     *
     * @param item
     * @param atPair
     * @param flag
     * @param isManager
     * @param nameBean
     * @return
     */
    public boolean isNeedIgnoreXManagerCommand(MsgItem
                                                       item, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, Integer
                                                       flag, boolean isManager, GroupWhiteNameBean nameBean, boolean checkLocalManager) {

        if (flag < INeedReplayLevel.INTERCEPT_ALL_HEIGHT) {//如果是屏蔽本群指令的除非艾特了否则不想要，也就是说会返回true通知忽略
            if (atPair.first && atPair.second.first) {
                if (!isManager && !(checkLocalManager && nameBean != null && nameBean.isCurrentGroupAdmin())) {
                    return true;//正常命令非管理不允许发送
                } else {

                }
            } else {

                return true;
            }

        } else {
            if (!isManager && !(checkLocalManager && nameBean != null && nameBean.isCurrentGroupAdmin())) {
                if (item != null) {

                    boolean needAt = ConfigUtils.IsNeedAt(nameBean);
                    if ((needAt && atPair.second.first) || !needAt) {//如果需要艾特，且不是管理员,但是艾特机器人或者不需要艾特都,就理睬告诉他 这里无视自身命令是否不需要艾特。
                        MsgReCallUtil.notifyNotManagerMsg(this, item);
                    }
                }
                return true;
            } else {

            }
        }


        if (ConfigUtils.IsNeedAt(nameBean)) {
            if (atPair.second.first == false && nameBean != null && nameBean.isSelfcmdnotneedaite() == false) {

                //如果没有 且 也没开启自身命令无需艾特功能，那么还是忽略
                return true;
            }

        }
        return false;


    }


    private boolean doGagCmdPrivateMsgCmd(MsgItem item, boolean isManager, String[] args) {

        String group = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
        if (!verifyPrivateMsgGroupParam(group)) {
            return true;
        }

        return doGagCmd(item, isManager, args, group, ParamParseUtil.sArgSecond, ParamParseUtil.sArgThrid, false, null, null);
    }

    private boolean doGagFromGroupMsgCmd(MsgItem item, boolean isManager, String[]
            args, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, GroupWhiteNameBean
                                                 nameBean) {
        return doGagCmd(item, isManager, args, item.getFrienduin(), ParamParseUtil.sArgFirst, ParamParseUtil.sArgSecond, true, atPair, nameBean);
    }

    private boolean doKickCmdPrivateMsgCmd(MsgItem item, boolean isManager, GroupWhiteNameBean nameBean, String[] args) {

        String group = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgFirst);
        if (!verifyPrivateMsgGroupParam(group)) {
            return false;
        }

        return doKickCmd(item, isManager, nameBean, args, group, ParamParseUtil.sArgSecond, ParamParseUtil.sArgThrid, null);
    }

    private boolean verifyPrivateMsgGroupParam(String group) {
        if (TextUtils.isEmpty(group) || !RegexUtils.checkNoSignDigit(group)) {
            return false;
        }
        return true;
    }

    private boolean doKickFromGroupMsgCmd(MsgItem item, boolean isManager, GroupWhiteNameBean nameBean, String[]
            args, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair) {
        return doKickCmd(item, isManager, nameBean, args, item.getFrienduin(), ParamParseUtil.sArgFirst, ParamParseUtil.sArgSecond, atPair);

    }

    private boolean doKickCmd(MsgItem item, boolean isManager, GroupWhiteNameBean nameBean, String[] args, String group, int accountIndex,
                              int forverIndex, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair) {


        String account = ParamParseUtil.getArgByArgArr(args, accountIndex);
        if (args.length == 1) {


        }
        boolean forver = ParseUtils.parseBoolean(ParamParseUtil.getArgByArgArr(args, forverIndex));


        if (atPair != null && atPair.first) {//是群艾特数据再包含账号码参数了
            forver = ParseUtils.parseBoolean(account);
            boolean issucc = FloorMultiUtils.doMultiKickLogicByAt(RobotContentProvider.this, isManager, nameBean, item, atPair.second.second, group, forver);
            if (!issucc) {
                String nickname;
                if (ConfigUtils.isDisableAtFunction(this)) {
                    nickname = NickNameUtils.formatNicknameFromNickName(group, item.getNickname());
                } else {
                    nickname = item.getNickname();
                }


                if (atPair.second.second.size() == 1) {
                    forver = ParseUtils.parseBoolean(ParamParseUtil.getArgByArgArr(args, forverIndex));
                    account = atPair.second.second.get(0).getAccount();
                } else {
                    MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, "无法踢出,可能踢出的人是管理员", item);
                    return true;

                }
            } else {
                return true;
            }

        } else if (FloorUtils.isFloorData(account)) {//允许空参数
            String floorQQ = FloorUtils.getFloorQQ(group, account);
            if (floorQQ != null) {
                account = floorQQ;

            } else {
                MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.getFloorInputDataInValidMsg(account), item);
                return false;
            }

        } else {


            Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
            if (pair != null) {
                List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                FloorMultiUtils.doMultiKickLogic(RobotContentProvider.this, isManager, nameBean, item, floors, group, forver);
                return false;
            } else {

                if (!RegexUtils.checkNoSignDigit(account)) {//非法的参数.
                    return false;
                }
            }
        }

        if (TextUtils.isEmpty(account)) {
            MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",无法识别操作账号!", item);
            return true;
        }


        if (!item.getSenderuin().equals(account)) {

            AdminBean accountMe = (AdminBean) AccountUtil.findAccount(mSuperManagers, item.getSenderuin(), false);
            AdminBean accountHe = (AdminBean) AccountUtil.findAccount(mSuperManagers, account, false);

            if (accountHe != null && accountMe != null) {

                if (accountMe.getLevel() <= accountHe.getLevel()) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为他的权限大于等于你的权限!", item);
                    return true;
                }
            } else if (accountMe == null) {


                if (accountHe != null && isManager == false) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为他是超级管理员,而你只是此群的管理员!", item);
                    return true;
                } else if (isCurrentGroupAdminFromDb(nameBean, account, group)) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为你们两个都是本群管理员!", item);
                    return true;

                }


            }

        }


        MsgItem kickItem = item.clone();
        kickItem.setIstroop(1);
        kickItem.setSenderuin(account + "");
        kickItem.setFrienduin(group);
        kickItem.setMessage(forver + "");
        kickItem.setNickname("" + account);//先设置为它,
        MsgReCallUtil.notifyKickPersonMsgNoJump(this, kickItem, forver);
        kickItem.setNickname(NickNameUtils.formatNickname(kickItem));//避免变成自己
        String nickname;//这里不需要艾特被踢者
        if (ConfigUtils.isDisableAtFunction(this)) {
            nickname = NickNameUtils.formatNicknameFromNickName(account, kickItem.getNickname());
        } else {
            nickname = kickItem.getNickname();
        }
        MsgReCallUtil.notifyJoinReplaceMsgJump(this, "请求踢出群成员:" + nickname + ",是否永久:" + kickItem.getMessage(), item);
        return true;
    }

    public boolean isCurrentGroupAdminFromDb(GroupWhiteNameBean nameBean, String qq, String group) {
        return isCurrentGroupAdminFromDb(nameBean, qq, group, false);
    }

    public boolean isCurrentGroupAdminFromDb(GroupWhiteNameBean nameBean, String qq, String group, boolean checkService) {
        if (nameBean == null) {

            return false;
        }
        if (qq == null) {
            return false;
        }
        String admins = nameBean.getAdmins();
        if (admins != null && nameBean.getAccount().equals(group)) {
            LogUtil.writeLog("当前群所配置的管理员:" + admins);
            String[] split = admins.split(",");
            for (String currentQQ : split) {
                if (qq.equals(currentQQ)) {
                    return true;
                }
            }
        }
        GroupAdaminBean groupAdaminBean = DBHelper.getGroupAdminTableUtil(_dbUtils).queryByColumn(GroupAdaminBean.class, FieldCns.FIELD_ACCOUNT, qq);

        if (groupAdaminBean != null) {
            String groups = groupAdaminBean.getGroups();
            LogUtil.writeLog("当前QQ所能管理的群:" + groups);
            if (!TextUtils.isEmpty(groups)) {
                String[] split = groups.split(",");
                for (String currentGroup : split) {
                    if (currentGroup.equals(group)) {
                        return true;
                    }
                }
            }
        }


        if (checkService && RemoteService.isIsInit()) {//真实管理员可能不会马上刷新..
            String administrator = RemoteService.queryGroupField(group, "Administrator");
            if (!TextUtils.isEmpty(administrator)) {
                LogUtil.writeLog("当前群真实管理:" + administrator);
                String[] split = administrator.split("\\|");//ignore_include
                for (String currentAdmin : split) {

                    if (currentAdmin.equals(qq)) {
                        return true;
                    }

                }
            }
            String qunzhu = RemoteService.queryGroupField(group, "troopowneruin");
            if (!TextUtils.isEmpty(qunzhu) && qunzhu.equals(qq)) {

                return true;
            }
        }


        return false;
    }


    private boolean doGagCmd(MsgItem item, boolean isManager, String[] args, String group, int accountIndex,
                             int gagTimeIndex, boolean groupMsg, androidx.core.util.Pair<Boolean, androidx.core.util.Pair<Boolean, List<GroupAtBean>>> atPair, GroupWhiteNameBean
                                     nameBean) {


        String paramStr1 = ParamParseUtil.getArgByArgArr(args, accountIndex);


        //解决回路bug
        if (groupMsg && item.getSelfuin().equals(item.getSenderuin()) && args.length >= 1 && paramStr1 != null && paramStr1.length() > 3 && !RegexUtils.checkIsContainNumber(paramStr1)) {

            return false;

        }

        if (paramStr1 != null && isManager) {//非超级管理不允许使用此命令
            paramStr1 = paramStr1.replace("全体", AppConstants.ALL_PERSON_FLAG);
            paramStr1 = paramStr1.replace("所有人", AppConstants.ALL_PERSON_FLAG);


        }


        String paramStr2 = ParamParseUtil.getArgByArgArr(args, gagTimeIndex);

        if (paramStr1 != null && paramStr1.equals(AppConstants.ALL_PERSON_FLAG)) {
            if (TextUtils.isEmpty(paramStr2)) {
                paramStr2 = "153016267秒";
            }
        }
        String account = paramStr1;

        long gagTime;

        if (atPair != null && atPair.first) {//是群艾特数据再包含账号码参数了
            if (TextUtils.isEmpty(paramStr1) && nameBean != null) {
                gagTime = formatGagTime(nameBean.getNotparamgagminute() + "分钟");

            } else {

                gagTime = formatGagTime(paramStr1);
            }

            boolean issucc = FloorMultiUtils.doMultiAtGagLogic(RobotContentProvider.this, isManager, nameBean, item, atPair.second.second, group, gagTime);
            if (!issucc) {
                String nickname;
                if (ConfigUtils.isDisableAtFunction(this)) {
                    nickname = NickNameUtils.formatNicknameFromNickName(group, item.getNickname());
                } else {
                    nickname = item.getNickname();
                }
                if (atPair.second.second.size() == 1) {

                    account = atPair.second.second.get(0).getAccount();
                    if (TextUtils.isEmpty(paramStr1) && nameBean != null) {
                        gagTime = formatGagTime(nameBean.getNotparamgagminute() + "分钟");

                    } else {
                        gagTime = formatGagTime(paramStr1);

                    }
                } else {
                    MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, "无法禁言,可能你艾特的人是管理员", item);
                    return true;

                }
            } else {
                return true;
            }


        } else if (FloorUtils.isFloorData(account)) {//允许空参数
            if (TextUtils.isEmpty(paramStr2) && nameBean != null) {
                gagTime = formatGagTime(nameBean.getNotparamgagminute() + "分钟");

            } else {
                gagTime = formatGagTime(paramStr2);

            }
            String floorQQ = FloorUtils.getFloorQQ(group, account);
            if (floorQQ != null) {
                account = floorQQ;

            } else {
                MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.getFloorInputDataInValidMsg(account), item);
                return true;
            }

        } else {

            Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
            if (pair != null) {//是否是多个楼层

                if (TextUtils.isEmpty(paramStr2) && nameBean != null) {
                    gagTime = formatGagTime(nameBean.getNotparamgagminute() + "分钟");

                } else {

                    gagTime = formatGagTime(paramStr2);
                }
                List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                boolean b = FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, isManager, nameBean, item, floors, group, gagTime);
                if (!b) {
                    String nickname;
                    if (ConfigUtils.isDisableAtFunction(this)) {
                        nickname = NickNameUtils.formatNicknameFromNickName(group, item.getNickname());
                    } else {
                        nickname = item.getNickname();
                    }
                    MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, "无法禁言,你操作的列表都是管理员或是机器人自己", item);


                }

                return true;
            } else {
                //是否省略了禁言人直接填写分钟 天之类的

                if (!RegexUtils.checkNoSignDigit(account)) {//非法的参数.比如可能是分钟之类的
                    String arg = account;
                    gagTime = ParseUtils.parseGagStr2Secound(arg);
                    account = FloorUtils.getFloorQQ(group);
                    if (TextUtils.isEmpty(account)) {
                        MsgReCallUtil.notifyHasDoWhileReply(this, "无法禁言,无法识别楼层号,请精确填写账号", item);
                        return true;
                    }
                } else {//直接填写qq

                    if (TextUtils.isEmpty(paramStr2) && nameBean != null) {
                        gagTime = formatGagTime(nameBean.getNotparamgagminute() + "分钟");

                    } else {

                        gagTime = formatGagTime(paramStr2);
                    }
                }

            }
        }


        if (!item.getSenderuin().equals(account)) {
            AdminBean accountMe = (AdminBean) AccountUtil.findAccount(mSuperManagers, item.getSenderuin(), false);
            AdminBean accountHe = (AdminBean) AccountUtil.findAccount(mSuperManagers, account, false);

            if (accountHe != null && accountMe != null) {

                if (accountMe.getLevel() <= accountHe.getLevel()) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为他的权限大于等于你的权限!", item);
                    return true;
                }
            } else if (accountMe == null) {
                if (accountHe != null) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为他是超级管理员，而你只是本群管理员!", item);
                    return true;

                } else if (isCurrentGroupAdminFromDb(nameBean, account, group)) {
                    MsgReCallUtil.notifyJoinReplaceMsgJump(this, AppConstants.ACTION_OPERA_NAME_FORBID + "无法操控" + account + ",因为你们两个都是本群管理员!", item);
                    return true;

                }

            } else {

            }

        }


        MsgItem gagMsgItem = item.clone();
        gagMsgItem.setIstroop(1);
        gagMsgItem.setSenderuin(account);
        gagMsgItem.setFrienduin(group);
        gagMsgItem.setNickname(NickNameUtils.queryMatchNickname(group, account, false));
        gagMsgItem.setMessage(gagTime + "");
        item.setNickname(gagMsgItem.getNickname());
        //设置结果在群里触发
        item.setFrienduin(group);
        item.setIstroop(1);

        //
        String result = "";
        if (RemoteService.isIsInit()) {
            String s = RemoteService.gagUser(gagMsgItem.getFrienduin(), gagMsgItem.getSenderuin(), gagTime);
            if (s != null) {
                result = "结果:" + s;
            } else {
                MsgReCallUtil.notifyGadPersonMsgNoJump(this, gagTime, gagMsgItem);
            }
        } else {
            MsgReCallUtil.notifyGadPersonMsgNoJump(this, gagTime, gagMsgItem);
        }

        String nickname;
        if (ConfigUtils.isDisableAtFunction(this)) {
            nickname = NickNameUtils.formatNicknameFromNickName(gagMsgItem.getSenderuin(), gagMsgItem.getNickname());
        } else {
            nickname = gagMsgItem.getNickname();
            if (gagMsgItem.getSenderuin().equals(nickname)) {
                gagMsgItem.setNickname("qq" + nickname + "");
            }
        }
        String message;
        if (gagTime <= 0) {
            if (AppConstants.ALL_PERSON_FLAG.equals(gagMsgItem.getSenderuin())) {
                message = AppConstants.ACTION_OPERA_NAME + "关闭全群禁言";

            } else {
                message = AppConstants.ACTION_OPERA_NAME + "解除" + gagMsgItem.getNickname() + "禁言";

            }
            //            MsgReCallUtil.notifyAtMsgJump(this, gagMsgItem.getSenderuin(), gagMsgItem.getNickname(), , item);
        } else {
            if (AppConstants.ALL_PERSON_FLAG.equals(gagMsgItem.getSenderuin())) {
                message = AppConstants.ACTION_OPERA_NAME + "开启全群禁言";
            } else {
                message = AppConstants.ACTION_OPERA_NAME + "禁言" + gagMsgItem.getNickname() + ",禁言时间" + DateUtils.getGagTime(gagTime);

            }
        }
        if (!TextUtils.isEmpty(result)) {
            message = message + "\n" + result;
        }
        if (groupMsg && nameBean != null && nameBean.isReplayatperson()) {//如果不等于空说明是群白名单的数据

            MsgReCallUtil.notifyAtMsgJump(RobotContentProvider.this, gagMsgItem.getSenderuin(), gagMsgItem.getNickname(), message, item);

        } else {

            MsgItem msgItem = item.setSenderuin(gagMsgItem.getSenderuin());
            msgItem.setMessage(message);
            msgItem.setNickname(nickname);
            MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(RobotContentProvider.this, msgItem);

        }

        return true;
    }

    private long formatGagTime(String gagTimeStr) {
        long gagTime;
        if (TextUtils.isEmpty(gagTimeStr)) {
            gagTime = ParseUtils.parseGagStr2Secound(IGnoreConfig.DEFAULT_GAG_TIME_STR);
        } else {
            gagTime = ParseUtils.parseGagStr2Secound(gagTimeStr);
        }
        return gagTime;

    }


    public boolean doAtCmdByGroupMsg(MsgItem item, boolean isManager, String[] args, String group) {
        return doAtCmd(item, isManager, args, group, ParamParseUtil.sArgFirst, ParamParseUtil.sArgSecond);
    }

    public boolean doAtCmdByPrivateMsg(MsgItem item, boolean isManager, String[] args, String group) {


        if (!verifyPrivateMsgGroupParam(group)) {
            return false;
        }
        item.setFrienduin(group);//出现的问题是如果报错无法私聊提示出来
        item.setIstroop(1);//转换为群艾特消息，无回执

        return doAtCmd(item, isManager, args, group, ParamParseUtil.sArgSecond, ParamParseUtil.sArgThrid);
    }

    public boolean doAtCmd(MsgItem item, boolean isManager, String[] args, String group, int gagPersonArgPosition,
                           int gagMsgPosition) {
        String first;
        first = ParamParseUtil.getArgByArgArr(args, gagPersonArgPosition);
        String second = ParamParseUtil.getArgByArgArr(args, gagMsgPosition);
        second = TextUtils.isEmpty(second) ? "别问我为啥艾特你,是我的主人干的,你找他去" : VarCastUtil.parseStr(item, _dbUtils, second);
        if ("全体".equals(first)) {
            if (!isManager) {
                MsgReCallUtil.notifyJoinMsgNoJump(this, "你的权限被限制,不能操作此功能！", item);
                return true;
            }
            String nickname = "全体成员";
            MsgReCallUtil.notifyAtMsgJump(this, "0", nickname, second, item);

        } else if (FloorUtils.isFloorData(first)) {

            String tempQQ = FloorUtils.getFloorQQ(group, first);
            if (tempQQ != null) {
                String nickname = NickNameUtils.queryMatchNickname(group, tempQQ, false);
                MsgReCallUtil.notifyAtMsgJump(this, tempQQ, nickname, second, item);
            } else {
                MsgReCallUtil.notifyJoinReplaceMsgJump(this, FloorUtils.getFloorInputDataInValidMsg(tempQQ), item);
                return true;
            }


        } else {

            Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(first);
            if (pair != null) {
                List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);
                if (floors != null) {
                    BatchUtil.atFloorData(this, item, floors, second);
                } else {

                    MsgReCallUtil.notifyJoinMsgNoJump(this, "无法识别艾特楼层因为,没有楼层数据", item);
                }
                return true;
            } else {
                if (!RegexUtils.checkNoSignDigit(first)) {
                    String nickname = NickNameUtils.queryMatchNickname(group, item.getSenderuin(), false);
                    MsgReCallUtil.notifyAtMsgJump(this, item.getSenderuin(), nickname, "无法艾特制定人，数据不合法", item);
                } else {


                    String nickname = NickNameUtils.queryMatchNickname(group, first, false);
                    MsgReCallUtil.notifyAtMsgJump(this, first, nickname, second, item);
                }

            }

        }
        return false;
    }


    static {
        LogUtil.importPackage();

    }


    @Override
    public void notifyChange(@NonNull Uri uri, @Nullable IRobotContentProvider observer) {


        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).notifyChange(uri, observer);
        }
        if (interceptNotifyChanage) {
            return;
        }
        getProxyContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public boolean getBooleanConfig(String key) {
        if (sharedPreferences == null) {
            initConfig();
        }
        return sharedPreferences.getBoolean(key, false);
    }

    @Override
    public void reloadSharedPreferences() {

        initConfig();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public boolean writeConfig(Object beforeObj, String key, String value) {
        boolean result = false;
        if (beforeObj instanceof Integer) {
            result = sharedPreferences.edit().putInt(key, Integer.parseInt(value)).commit();

        } else if (beforeObj instanceof Long) {
            result = sharedPreferences.edit().putLong(key, Long.parseLong(value)).commit();

        } else if (beforeObj instanceof String) {
            result = sharedPreferences.edit().putString(key, value).commit();

        } else if (beforeObj instanceof Boolean) {
            result = sharedPreferences.edit().putBoolean(key, Boolean.parseBoolean(value)).commit();

        } else if (beforeObj instanceof Set) {
            String[] split = value.split(",");
            TreeSet<String> strings = new TreeSet<>();
            for (String s : split) {
                strings.add(s);
            }
            result = sharedPreferences.edit().putStringSet(key, strings).commit();


        }
        return result;

    }

    @Override
    public SQLiteDatabase getRobotDb() {
        return _dbUtils.getDb();
    }

    @Override
    public void reloadPlugin(final Handler.Callback callback) {
        initJAVAPlugin(new INotify() {
            @Override
            public void onNotify(Object param) {

                if (callback != null) {
                    Message message = getHandler().obtainMessage(0, mPluginList == null ? 0 : mPluginList.size(), 0);
                    callback.handleMessage(message);
                }

            }
        });
        initLuaPlugin();
        initJavascriptSPlugin();
    }

    @Override
    public void writeBooleanConfig(String key, boolean isChecked) {

        SharedPreferences.Editor edit = sharedPreferences.edit();

        boolean commit = edit.putBoolean(key, isChecked).commit();
        edit.apply();
        if (commit == false) {
            mLastError = "修改" + key + "信息失败";
        }
    }

    /**
     * 通过持久化查询判断是否已经禁用
     *
     * @return
     */
    @Override

    public boolean hasDisablePlugin(IPluginHolder holder) {
        File path = new File(holder.getPath());
//        return sharedPreferences.getBoolean("disable_plugin_"+pluginInterface.get);

        return new File(path.getParentFile(), "" + path.getName() + ".disable").exists();
    }


    public static boolean writePluginErrorLog(File pluginPath, String error) {


//        return sharedPreferences.getBoolean("disable_plugin_"+pluginInterface.get);

        File file = new File(pluginPath.getParentFile(), "" + pluginPath.getName() + ".log");

        if (error == null) {
            if (file.exists()) {
                return file.delete();
            }
        }

        try {
            FileUtils.write(file, error, "utf-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    public String readPluginErrorLog(File pluginPath, String error) {
//        return sharedPreferences.getBoolean("disable_plugin_"+pluginInterface.get);

        File file = new File(pluginPath.getParentFile(), "" + pluginPath.getName() + ".log");

        try {
            return FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean disablePlugin(IPluginHolder holder, boolean isChecked) {

        File path = new File(holder.getPath());
        File file = new File(path.getParentFile(), "" + path.getName() + ".disable");
        boolean result = false;
        if (isChecked) {

            try {
                file.createNewFile();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            if (file.exists()) {

                result = file.delete();
            } else {
                result = true;
            }
        }
        holder.setDisableFlag(result);
        return result;
    }

    @Override
    public void addObserver(IContentProviderNotify providerNotify) {
        observers.add(providerNotify);
    }

    @Override
    public void clearObserver() {
        observers.clear();


    }

    @Override
    public boolean onAttachIHostControlApi(IHostControlApi hostControlApi) {
        mHostControlApi = hostControlApi;
        return false;

    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String callingPackage = getCallingPackage();

        }
//        int callingPid = Binder.getCallingPid();
        return super.call(method, arg, extras);
    }

    List<IRobotContentProvider.IContentProviderNotify> observers = new ArrayList<>();

    @Override
    public Context getProxyContext() {

        if (mInnerProxyCoontext != null) {
            return mInnerProxyCoontext;
        }
        return this.getContext();
    }

    @Override
    public ClassLoader getProxyClassloader() {
        if (mProxyClassloader != null) {
            return mProxyClassloader;

        } else {
            return RobotContentProvider.class.getClassLoader();
        }
    }

    @Override
    public Context getRobotContext() {

        if (mPakcageContext != null) {
            return mPakcageContext;
        }
        if (mInnerProxyCoontext != null) {
            return mInnerProxyCoontext;
        }
        return this.getContext();
    }

    @Override
    public String getLastErrorMsg() {
        return mLastError;
    }


    public void setProxyContext(Context context) {
        this.mInnerProxyCoontext = context;

    }

    @Override
    public void setProxyClassloader(ClassLoader classloader) {
        this.mProxyClassloader = classloader;
    }

    @Override
    public void interceptNotifyChanage(boolean intercept) {
        this.interceptNotifyChanage = intercept;
    }


    private Context mInnerProxyCoontext;

    public boolean isDisableAtFunction() {
        return mCfBaseDisableAtFunction;
    }

    public boolean replyShowNickname() {
        return mCfBaseReplyShowNickName;
    }

    public boolean isDisableSuperFunction() {
        return mStopUseAdvanceFunc;
    }


    public Resources getResources() {

        if (getRobotContext() != null) {
            return getRobotContext().getResources();
        }

        if (mProxyResources == null || getProxyContext().getPackageName().equals(BuildConfig.APPLICATION_ID)) {
            return getProxyContext().getResources();
        }


        return mProxyResources;

    }


    public boolean isAsPluginLoad() {
        return !getProxyContext().getPackageName().equals(BuildConfig.APPLICATION_ID);
    }

    public IGroupConfig getGroupConfig(String group) {


        GroupWhiteNameBean account = AccountUtil.findAccount(mQQGroupWhiteNames, group, false);

        return account;
    }
}




    /*
        if (code == 100000) {

        } else if (code == 200000) {

        } else if (code == 200000) {

        } else if (code == 302000) {

        } else if (code == 308000) {

        } else if (code == 313000) {

        } else if (code == 314000) {

        } else {
           /* 40001	参数key错误
            40002	请求内容info
       8
            40004	当天请求次数已使用完
            40007	数据格式异常*/
/*

 */
