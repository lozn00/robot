package cn.qssq666.robot.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.activity.datamanager.FloorManagerActivity;
import cn.qssq666.robot.activity.datamanager.GagKeyWordActivity;
import cn.qssq666.robot.activity.datamanager.GroupAdminActivity;
import cn.qssq666.robot.activity.datamanager.GroupWhiteNamesActivity;
import cn.qssq666.robot.activity.datamanager.NickNameManagerActivity;
import cn.qssq666.robot.activity.datamanager.QQIgnoresActivity;
import cn.qssq666.robot.activity.datamanager.QQIgnoresGagActivity;
import cn.qssq666.robot.activity.datamanager.QQSuperManagerActivity;
import cn.qssq666.robot.activity.datamanager.VarManagerActivity;
import cn.qssq666.robot.ad.BannerUtils;
import cn.qssq666.robot.ad.InterstitialAdUtil;
import cn.qssq666.robot.adapter.HomeMenuAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.UpdateBean;
import cn.qssq666.robot.business.FloorUtils;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.NetCns;
import cn.qssq666.robot.databinding.ActivityMainBinding;
import cn.qssq666.robot.event.ForbitUseAdvanceEvent;
import cn.qssq666.robot.event.ForceEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.interfaces.OnItemClickListener;
import cn.qssq666.robot.interfaces.RequestListener;
import cn.qssq666.robot.utils.AppThemeUtilsX;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.BatteryUtil;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.DensityUtil;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.DownloadUtils;
import cn.qssq666.robot.utils.HttpUtil;
import cn.qssq666.robot.utils.MediaUtils;
import cn.qssq666.robot.utils.PermissionUtil;
import cn.qssq666.robot.xbean.HomeMenu;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends SuperActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivityR";

    public static final String TEST = "\n" + "\n\n" + "\n\n" + "测试换行符\n\n你好世界 我擦\n擦擦擦" + "\n" + "===========\n\n\n后面是t\t然后还是t\t逼逼来了\b还有一个bb\b逼逼结束 rn了\r\n";
    private static final int REQUEST_PERMISSION = 1;
    private boolean mForceUpdate;
    private ActivityMainBinding binding;
    private ContentResolver resolver;
    private int istroop = 0;
    private int mProgress;
    private PowerManager.WakeLock wakeLock;
    private String mFrom;
    private AdView adView;
    private InterstitialAdUtil interstitialAdUtil;

    int clickId;
    private int _CurrentPermissionIndex;
    private int mIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null) {


            mFrom = intent.getStringExtra("from");


        }


        setTitleColor(Color.WHITE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVersion(BuildConfig.VERSION_NAME);
        binding.executePendingBindings();
        binding.radioGroup.setOnCheckedChangeListener(this);
        binding.rb1.setChecked(true);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(AppContext.getInstance(), 3));
        int size = DensityUtil.dip2px(AppContext.getInstance(), 5);
        AppThemeUtilsX.setGridLayoutDividerItem(binding.recyclerview, true, true, true, size);
        final HomeMenuAdapter homeMenuAdapter = new HomeMenuAdapter();
        List<HomeMenu> homeMenus = new ArrayList<>();
        homeMenus.add(new HomeMenu(R.id.btn_set, "总开关"));
        homeMenus.add(new HomeMenu(R.id.btn_group_white_names, "配置群"));
        homeMenus.add(new HomeMenu(R.id.btn_super_manager, "超级管理员"));
        homeMenus.add(new HomeMenu(R.id.btn_group_admin, "配置群管理"));
        homeMenus.add(new HomeMenu(R.id.btn_ignore_uncheck_qq, "免检账号"));
        homeMenus.add(new HomeMenu(R.id.btn_ignore_qq, "忽略账号"));
        homeMenus.add(new HomeMenu(R.id.btn_key_manager, "词库管理"));
        homeMenus.add(new HomeMenu(R.id.btn_gag_word, "违禁词管理"));
        homeMenus.add(new HomeMenu(R.id.btn_js_plugin_manager, "JS插件管理"));
        homeMenus.add(new HomeMenu(R.id.btn_lua_plugin_manager, "Lua插件管理"));
        homeMenus.add(new HomeMenu(R.id.btn_plugin_manager, "Java插件管理"));
        homeMenus.add(new HomeMenu(R.id.btn_var_manager, "变量管理"));
        homeMenus.add(new HomeMenu(R.id.btn_key_redpacket_record, "红包流水"));
//        homeMenus.add(new HomeMenu(R.id.btn_floor_manager, "楼层数据查询"));
//        homeMenus.add(new HomeMenu(R.id.btn_nickname, "昵称管理"));
        homeMenus.add(new HomeMenu(R.id.btn_self_comd, "机器人命令"));
        homeMenus.add(new HomeMenu(R.id.btn_join_group, "申请加群"));
        homeMenus.add(new HomeMenu(R.id.btn_about, "关于本软件"));
        homeMenus.add(new HomeMenu(R.id.btn_view_Log, "调试日志"));
        homeMenus.add(new HomeMenu(R.id.btn_debug_robot, "高级调试"));
//        homeMenus.add(new HomeMenu(R.id.btn_clock_task, "定时任务(暂无)"));
//        homeMenus.add(new HomeMenu(R.id.btn_clock_task, "群发(不自己开发插件)"));
//        homeMenus.add(new HomeMenu(R.id.btn_group_illegal_record, "群员违规记录(无)"));
        homeMenus.add(new HomeMenu(R.id.watch_ad, "广告展示"));
//        homeMenus.add(new HomeMenu(R.id.plugin_to_install, "自身转插件"));
        homeMenus.add(new HomeMenu(R.id.btn_daemon_app, "提升稳定性"));
        homeMenus.add(new HomeMenu(R.id.btn_win_money, "撸羊毛"));
        homeMenus.add(new HomeMenu(R.id.btn_download_url, "更新入口"));
        homeMenuAdapter.setData(homeMenus);
        homeMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                HomeMenu homeMenu = homeMenuAdapter.getData().get(position);
                doMenuClick(homeMenu.getId());

            }
        });
        binding.recyclerview.setAdapter(homeMenuAdapter);
        setTitle(getString(R.string.app_name) + BuildConfig.VERSION_NAME + " build " + BuildConfig.VERSION_CODE);


        EventBus.getDefault().register(this);
        checkUpdate();


        initAd();

        if (!BatteryUtil.ignoreBatteryOptimization(this)) {


            DialogUtils.showConfirmDialog(this, "如果不添加到电量优化白名单，在机器人被系统杀死之后将会被宿主(如Q++,情迁红包插件)唤醒以保证机器人不死，但是严重丢失消息，因此建议同意。", "添加电量优化白名单", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    Pair<Boolean, String> add = BatteryUtil.add(MainActivity.this);
                    if (!add.first) {
                        Toast.makeText(MainActivity.this, "添加失败" + add.second, Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "" + add.second, Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }


        TextView tvAd = (TextView) findViewById(R.id.tv_top_ad);
        if (hasHook()) {
            tvAd.setText("机器人宿主软件已激活(注意勾选启用哦!)");
            tvAd.setTextColor(Color.parseColor("#006400"));//ignore_include
        }


        int i = initPermission();
        if (i != 0) {

        }
        Log.w(TAG, "数据库文件路径:" + RobotContentProvider.getDbUtils().getDbNameAbsolutePath() + ",i:" + i);

    }

    private int initPermission() {

        int from = 0;
        return callPermissionLogic(from);

    }

    private int callPermissionLogic(final int from) {
        int breakFlag = -1;
        for (int i = from; i < waitRequestPermissionList.size(); i++) {
            final String currentPermission = waitRequestPermissionList.get(i);
            _CurrentPermissionIndex = i;
            final int finalI = i;
            if (PermissionUtil.checkSelfPermission(this, currentPermission) != PackageManager.PERMISSION_GRANTED) {
                breakFlag = i;
                if (PermissionUtil.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                    Log.w(TAG, "shouldShowRequestPermissionRationale call");
                    DialogUtils.showConfirmDialog(this, "年轻人,权限不用轻易拒绝啊,再给你一次机会，我需要申请" + currentPermission + "权限,这个权限有利于机器人程序得问题哈!", new INotify<Void>() {
                        @Override
                        public void onNotify(Void param) {
                            PermissionUtil.requestPermissions(MainActivity.this, new String[]{currentPermission}, finalI);
                        }
                    }, new INotify<Void>() {
                        @Override
                        public void onNotify(Void param) {
                            callPermissionLogic(from + 1);
                        }
                    });

                } else {
                    PermissionUtil.requestPermissions(this, new String[]{currentPermission}, i);
                    if (BuildConfig.DEBUG) {

                        Log.w(TAG, "第一次或者最后一次权限申请 " + currentPermission);
                    }

                }
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "权限" + currentPermission + "已拥有...");

                }
                if (i == waitRequestPermissionList.size() - 1) {
//                    if (PermissionUtil.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED) {
                    registerWakeLock();//具备了这个权限了。
//                    }
                }
            }

        }
        return breakFlag;
    }


    List<String> waitRequestPermissionList = new ArrayList<>();

    {

        waitRequestPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        waitRequestPermissionList.add("android.permission.FOREGROUND_SERVICE");
        waitRequestPermissionList.add(Manifest.permission.WAKE_LOCK);
    }

    public static boolean hasHook() {
        return false;
    }

    private void initAd() {


        MobileAds.initialize(this, AppContext.getInstance().getResources().getString(R.string.google_app_id_));
        adView = findViewById(R.id.ad_view);


        BannerUtils.getInstance().show(adView);

        interstitialAdUtil = new InterstitialAdUtil();
        interstitialAdUtil.init(this);
        interstitialAdUtil.startRequest();

        interstitialAdUtil.setOnListener(new InterstitialAdUtil.OnListener() {
            @Override
            public void onSHow() {
                if (clickId > 0) {


                }
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onFailLoad(int i) {

            }

            @Override
            public void onLoaded() {
                if (!BuildConfig.DEBUG) {
                    interstitialAdUtil.show();
                }


            }
        });


    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        stopUpdateTime();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//onRequestPermissionsResult requestCode:1,permission:[android.permission.FOREGROUND_SERVICE],grantResult:[-1]
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "onRequestPermissionsResult requestCode:" + requestCode + ",permission:" + Arrays.toString(permissions) + ",grantResult:" + Arrays.toString(grantResults));
        }
        _CurrentPermissionIndex++;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            AppContext.getDbUtils().readLoad();
            DialogUtils.showConfirmDialog(this, "由于您同意了写入存储权限，外部存储目录，建议重新进行更新", "好", "不", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    AppUtils.restartApp(MainActivity.this);
                }
            }, new INotify<Void>() {
                @Override
                public void onNotify(Void param) {

                }
            });
        } else {
            callPermissionLogic(_CurrentPermissionIndex);


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @SuppressLint("InvalidWakeLockTag")
    private void registerWakeLock() {
//        PermissionUtil.showSystemDialog(this);


        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        wakeLock.acquire();//申请锁，这里会调用PowerManagerService里面acquireWakeLock()


    }


    private void checkUpdate() {
        HttpUtil.queryData(Cns.UPDATE_URL, new RequestListener() {
            @Override
            public void onSuccess(String str) {

                final UpdateBean updateInfo = JSON.parseObject(str, UpdateBean.class);
                NetCns.updateBean = updateInfo;
                if (!TextUtils.isEmpty(updateInfo.getQqgroup())) {
                    Cns.DEFAULT_GROUP = updateInfo.getQqgroup();
                }


//                UpdateInfo updateInfo = pullXmlTextToUpdateInfo(str);
                if (updateInfo != null && updateInfo.getVersioncode() > BuildConfig.VERSION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppUtils.showUpdateDialog(updateInfo, MainActivity.this);
                        }
                    });

                    if (updateInfo.isForce()) {
                        EventBus.getDefault().post(new ForceEvent());
                    }

                }
            }

            @Override
            public void onFail(String str) {
                Cns.DEFAULT_GROUP = "";
                EventBus.getDefault().post(new ForbitUseAdvanceEvent());//网络失败的概率还是有的，这种情况不允许使用禁言指令
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void doUpdateDialogClick(Pair<Boolean, UpdateBean> resultPair) {
        if (resultPair.second.getUrl() == null) {
            AppUtils.openWebView(AppContext.getInstance(), Cns.UPDATE_ABOUT_URL);
        } else if (resultPair.second.getUrl().endsWith("apk")) {
            AppContext.showToast("正在下载,请注意查收通知栏结果");
            requestDownloadFile(resultPair.second);

        } else {
            AppUtils.openWebView(AppContext.getInstance(), resultPair.second.getUrl());

        }
    }


    private void requestDownloadFile(final UpdateBean model) {


        final int NOTIFY_ID_DOWNLOAD = 1;
        final NotificationManager mNotifyManager = (NotificationManager) AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "1");
        mBuilder.setAutoCancel(false);
        mBuilder.setSubText(model.getDescription());
        mBuilder.setContentTitle("正在升级更新情迁聊天机器人" + model.getVersion()).setContentText("下载中，请稍等");

        final File sdcardpApkFileName = MediaUtils.getSdcardpApkFileName("temp_" + model.getVersion() + " build " + model.getVersioncode() + ".apk");
        if (!sdcardpApkFileName.exists()) {


            DownloadUtils.downloadFile(model.getUrl(), sdcardpApkFileName, new DownloadUtils.DownloadListener() {
                @Override
                public void onStart(int value) {//http://www.jianshu.com/p/914cd8350f21
                    mBuilder.setProgress(100, 0, false);
                    mBuilder.setOngoing(true);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    mNotifyManager.notify(NOTIFY_ID_DOWNLOAD, mBuilder.build());
                }

                @Override
                public void onProgress(int process) {
                    if (process % 2 == 0) {
                        if (process == mProgress) {//解决卡ui问题
                            return;
                        }
                        mProgress = process;
                        if (BuildConfig.DEBUG) {

                        }
                        mBuilder.setProgress(100, process, false);
                        mNotifyManager.notify(NOTIFY_ID_DOWNLOAD, mBuilder.build());

                    }


                }

                @Override
                public void onFail(String value) {
                    AppContext.showToast("自动下载最新版APK失败！" + value);
                    mNotifyManager.cancel(NOTIFY_ID_DOWNLOAD);
                }

                @Override
                public void onSuccess(String value) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "DownloadFinish");

                    }
                    //没有意图 似乎没法自动消失
                    AppContext.showToast("最新版APK下载完成,请安装！");
                    mNotifyManager.cancel(NOTIFY_ID_DOWNLOAD);

                    AppUtils.installApkFile(MainActivity.this, sdcardpApkFileName);

                }
            });


        } else {
            AppContext.showToast("新版本已经下载完成,请安装");
            AppUtils.installApkFile(MainActivity.this, sdcardpApkFileName);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }

        if (adView != null) {
            adView.destroy();
        }
    }

    @Subscribe
    public void onReveiveForceEvent(ForceEvent event) {
        mForceUpdate = true;
    }

    public void checkUpdate1() {


        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://121.42.29.143/startup/index.php/Home/ItemComment/getAll/12899")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.toString();
                UpdateBean updateBean = JSON.parseObject(s, UpdateBean.class);

            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        doMenuClick(id);

    }


    public void doMenuClick(int id) {


            if (!interstitialAdUtil.isLoadding()) {

                if (interstitialAdUtil.loadSuccCount() < 3) {

                    interstitialAdUtil.startRequest();
                }
        }


        if (mForceUpdate) {
            finish();
        }


        switch (id) {
            case R.id.btn_debug_robot: {
                if (true) {

                }
                Intent intent = new Intent(this, DevToolActivity.class);
                startActivity(intent);

            }
            break;

            case R.id.btn_win_money: {
                AppUtils.openWebView(this, "https://qssq666.gitee.io/software/s.html");
//                AppUtils.openAlipaySHnag(MainActivity.this);
                break;
            }
            case R.id.plugin_to_install:
//                startInstall();

                DialogUtils.showDialog(this, "(如果崩溃先关闭插件化加载,尽量确保都是最新的,因为插件化加载需要同时更新插件和机器人主程序 但凡一个程序版本不一致都会导致崩溃.如1.7.0机器人配合插件1.6.4的插件)此功能情迁红包插件(1.3.8) 1.6.1以及以上勾选作为插件加载机器人即可,那么本app所有ui设置将无效了(数据库最新版统一移动到了/sdcard/qssq666文件夹,在这里修改需要先彻底关闭QQ)机器人主程序设置里面的首选项修改无效,不过可以手动复制robot_pe***到,具体参考简书.在情迁插件激活后在QQ设置-情迁红包设置-情迁聊天机器人管理中心进入简书入口");
                break;
            case R.id.btn_group_admin: {
                Intent intent = new Intent(this, GroupAdminActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_super_manager: {
                Intent intent = new Intent(this, QQSuperManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_about: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_nickname: {
                Intent intent = new Intent(this, NickNameManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_join_group: {
                AppUtils.toQQGroup(this);


            }
            break;
            case R.id.btn_gag_word: {
                Intent intent = new Intent(this, GagKeyWordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_floor_manager: {
                Intent intent = new Intent(this, FloorManagerActivity.class);
                startActivity(intent);
                FloorUtils.saveAllGroupFloorToDb();
            }
            break;


            case R.id.btn_group_white_names: {
                Intent intent = new Intent(this, GroupWhiteNamesActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_ignore_uncheck_qq: {
                Intent intent = new Intent(this, QQIgnoresGagActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.btn_download_url: {
                AppUtils.openWebView(this, "https://lozn.top/update");
                Toast.makeText(this, "如果无法打开网址,建议开启VPN", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.btn_ignore_qq: {
                Intent intent = new Intent(this, QQIgnoresActivity.class);
                startActivity(intent);
            }
            break;


            case R.id.btn_self_comd: {
                Intent intent = new Intent(this, CmdHelpActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_key_manager: {
                Intent intent = new Intent(this, KeyWordActivity.class);
                startActivity(intent);
            }
            break;


            case R.id.btn_key_redpacket_record: {
                Intent intent = new Intent(this, RedPacketRecordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_view_Log: {
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
            }
            break;


            case R.id.btn_set: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_plugin_manager: {
                Intent intent = new Intent(this, PluginManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_lua_plugin_manager: {

                Intent intent = new Intent(this, LuaPluginManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_js_plugin_manager: {
                Intent intent = new Intent(this, JSPluginManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_group_illegal_record: {
         /*       Intent intent = new Intent(this, PluginManagerActivity.class);
                startActivity(intent);*/
            }
            break;
            case R.id.btn_clock_task: {
         /*       Intent intent = new Intent(this, PluginManagerActivity.class);
                startActivity(intent);*/
            }
            break;
            case R.id.btn_daemon_app: {
                Intent intent = new Intent(this, DaemonAppActivity.class);
                startActivity(intent);


            }
            break;
            case R.id.watch_ad: {

                Intent intent = new Intent(this, WatchAdActivity.class);
                startActivity(intent);
            }

            break;
            case R.id.btn_var_manager: {
                Intent intent = new Intent(this, VarManagerActivity.class);
                startActivity(intent);

            }

            break;
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        moveTaskToBack(true);

        FloorUtils.saveAllGroupFloorToDb();
        AlertDialog alertDialog = DialogUtils.showConfirmDialog(this, "真的要退出吗?(退出除非关闭宿主机器人监听)", "退出", "后台", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
                finish();
            }
        }, new INotify<Void>() {
            @Override
            public void onNotify(Void param) {


                binding.linelayout.requestFocusFromTouch();
                binding.linelayout.requestFocus();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        if (mIndex == 0) {
            startUpdateTime();
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long distance = System.currentTimeMillis() - AppContext.getInstance().getStartupTime();
            binding.tvRunTime.setText("本软件已稳定运行:" + DateUtils.generateTimeDetail(distance) + ""
                    + (mFrom != null ? "  本程序被异常或者后台内存不足时杀死过," +
                    "被第三方程序" + mFrom + "调用激活了,如果本软件反复弹出,您要么放弃使用机器人 在" + mFrom + "设置里面关闭启用机器人," +
                    "要么就忍受要么就去系统电池优化里面给本软件添加后台白名单避免被反复杀死又被" + mFrom + "反复启动激活" : ""));
            AppContext.getHandler().postDelayed(runnable, 1000);
        }
    };

    public void stopUpdateTime() {
        AppContext.getHandler().removeCallbacks(runnable);
    }

    public void startUpdateTime() {
        AppContext.getHandler().removeCallbacks(runnable);
        AppContext.getHandler().postDelayed(runnable, 1000);
    }

    @Override
    public void finish() {
        EventBus.getDefault().unregister(this);
        super.finish();

    }

    private void startInstall() {
        try {
            PackageInfo packageInfo = MainActivity.this.getApplicationContext().getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);


            File pluginDirs = new File("/data/data/" + "com.tencent.mobileqq" + "/app_installed_plugin");
            if (!pluginDirs.isDirectory()) {
                pluginDirs.mkdirs();
                Log.w(TAG, "目录不存在 " + pluginDirs.getAbsolutePath());
            }
            File sourceDir = new File(packageInfo.applicationInfo.sourceDir);
            String installlName = "";
            if (Build.VERSION.SDK_INT < 21) {
                installlName = getApplication().getPackageName() + "_" + BuildConfig.BUILD_TIME;
                executeSu(null, "chmod 777 " + pluginDirs.getAbsolutePath());
                File[] files = pluginDirs.listFiles();

                if (files != null) {

                    for (File file : files) {
                        if (file.getName().startsWith(getApplication().getPackageName())) {
                            file.delete();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "无法卸载原来插件", Toast.LENGTH_SHORT).show();
                }

            } else {

                installlName = getApplication().getPackageName();

            }


            File installApkFile = new File(pluginDirs, installlName + ".apk");
            try {

                Process proc = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(proc.getOutputStream());
                os.writeBytes(String.format("chmod 777 %s %s\n", sourceDir.getAbsolutePath(), installApkFile.getAbsolutePath()));
                os.writeBytes(String.format("cp -p %s %s\n", sourceDir.getAbsolutePath(), installApkFile.getAbsolutePath()));
                os.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "utf-8"));

                os.close();
                String line = bufferedReader.readLine();
                if (TextUtils.isEmpty(line)) {
                    Toast.makeText(MainActivity.this, "安装成功,插件名为:" + installApkFile.getName(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "安装完成，无法确定是否成功!" + bufferedReader.readLine(), Toast.LENGTH_SHORT).show();

                }
                bufferedReader.close();


            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "请手动把apk文件复制到" + installApkFile + "中去,并使用刷新命令进行刷新!", Toast.LENGTH_SHORT).show();
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "安装失败,找不到自身apk文件路径!", Toast.LENGTH_SHORT).show();
        }
    }

    public static DataOutputStream executeSu(DataOutputStream os, String cmd) {
        try {
            Process proc;
            if (os == null) {
                proc = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(proc.getOutputStream());
            }

            os.writeBytes(cmd);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "执行写入权限失败" + e.getMessage());
        }
        return os;

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int checkIndex = getCheckIndex(group, checkedId);
        if (mIndex == 0 && checkIndex == 0) {
            return;
        }
        mIndex = checkIndex;
        FrameLayout fragmentContainer = binding.fragmentContainer;
        int childCount = fragmentContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = fragmentContainer.getChildAt(i);
            childAt.setVisibility(i == mIndex ? View.VISIBLE : View.GONE);
        }
        if (mIndex == 0) {
            startUpdateTime();
        } else {
            stopUpdateTime();
        }
    }


    private int getCheckIndex(RadioGroup group, int checkedId) {
        View viewById = group.findViewById(checkedId);
        return group.indexOfChild(viewById);
    }

}
