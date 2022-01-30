package cn.qssq666.robot.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.activity.datamanager.BaseAccountManagerActivity;
import cn.qssq666.robot.adapter.ErrorPluginInterface;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.PluginBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.IntentCns;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.event.RefreshEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.plugin.lua.util.LuaPluginUtil;
import cn.qssq666.robot.plugin.lua.util.LuaUtil;
import cn.qssq666.robot.plugin.util.QueryPluginModel;
import cn.qssq666.robot.selfplugin.IPluginHolder;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.PermissionUtil;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class LuaPluginManagerActivity extends BaseAccountManagerActivity<PluginBean> {

    private static final int REQUEST_CODE_SELECT_PLUGIN = 1;
    private boolean mChanage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        callPermissionLogic(this,0);



    }

    private int _CurrentPermissionIndex;

    List<String> waitRequestPermissionList = new ArrayList<>();

    {    waitRequestPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        waitRequestPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private int callPermissionLogic(Activity activity,final int from) {
        int breakFlag = -1;
        for (int i = from; i < waitRequestPermissionList.size(); i++) {
            final String currentPermission = waitRequestPermissionList.get(i);
            _CurrentPermissionIndex = i;
            final int finalI = i;
            if (PermissionUtil.checkSelfPermission(this, currentPermission) != PackageManager.PERMISSION_GRANTED) {
                breakFlag = i;
                if (PermissionUtil.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                    Log.w("PermissionRequest", "shouldShowRequestPermissionRationale call");
                    DialogUtils.showConfirmDialog(this, "年轻人,权限不用轻易拒绝啊,再给你一次机会，我需要申请" + currentPermission + "权限,这个权限有利于机器人程序得问题哈!", new INotify<Void>() {
                        @Override
                        public void onNotify(Void param) {
                            PermissionUtil.requestPermissions(activity, new String[]{currentPermission}, finalI);
                        }
                    }, new INotify<Void>() {
                        @Override
                        public void onNotify(Void param) {
                            callPermissionLogic(activity,from + 1);
                        }
                    });

                } else {
                    PermissionUtil.requestPermissions(this, new String[]{currentPermission}, i);
                    if (BuildConfig.DEBUG) {

                        Log.w("PermissionRequest", "第一次或者最后一次权限申请 " + currentPermission);
                    }

                }
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w("PermissionRequest", "权限" + currentPermission + "已拥有...");

                }
            }

        }
        return breakFlag;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//onRequestPermissionsResult requestCode:1,permission:[android.permission.FOREGROUND_SERVICE],grantResult:[-1]
        if (BuildConfig.DEBUG) {
            Log.w("PermissionRequest", "onRequestPermissionsResult requestCode:" + requestCode + ",permission:" + Arrays.toString(permissions) + ",grantResult:" + Arrays.toString(grantResults));
        }
        _CurrentPermissionIndex++;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            AppContext.getDbUtils().readLoad();
            DialogUtils.showConfirmDialog(this, "由于您同意了写入存储权限，外部存储目录，建议重新进行更新", "好", "不", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    AppUtils.restartApp(LuaPluginManagerActivity.this);
                }
            }, new INotify<Void>() {
                @Override
                public void onNotify(Void param) {

                }
            });
        } else {
            callPermissionLogic(LuaPluginManagerActivity.this,_CurrentPermissionIndex);


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveChangeNotification(RefreshEvent event) {
        mChanage = true;
        if (!event.isUpdateUi()) {
            Log.w("LuaManager", "insert-event");
            RobotContentProvider.getInstance().initLuaPlugin();
        } else {
            Log.w("LuaManager", "等待更新ui-event");
            delayUpdate();
        }
    }

    private void delayUpdate() {
        AppContext.getHandler().removeCallbacks(runnable);
        AppContext.getHandler().postDelayed(runnable, 150);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doOnRefresh();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (mChanage) {
            Log.w("LuaManager", "等待更新ui-onResume");
            delayUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plugin_activity_lua, menu);
        return true;
    }

    @Override
    protected int deleteAllFromDb() {
        return LuaPluginUtil.deleteAllPlugin(AppContext.getInstance());
    }

    boolean mNeedUpdate = false;

    @Override
    protected boolean onClickMenu(MenuItem item, int id) {
        if (id == R.id.action_refresh) {
//            queryData();
            doDataChanageSuccEvent();
            AppContext.showToast("操作成功");
        } else if (id == R.id.action_sdk_help) {//
            AppUtils.openWebView(LuaPluginManagerActivity.this, Cns.SDK_DEVELOPER_URL_LRA);

        } else if (id == R.id.action_help_install_plugin) {
            AppUtils.openWebView(LuaPluginManagerActivity.this, Cns.HELPER_URL);

        } else if (id == R.id.action_download_plugin_sdk) {
            AppUtils.openWebView(LuaPluginManagerActivity.this, Cns.SDK_DOWNLOAD_URL);
            DialogUtils.showDialog(this, "选择数字最大的版本下载,下载选择doc字样的jar,下载后改后缀为zip解压打开html查看文档，可以根据java sdk举一反三。");

        } else if (id == R.id.action_download_plugin_lua) {
            AppUtils.openWebView(LuaPluginManagerActivity.this, Cns.LUA_PLUGIN_MARKET_URL);

        } else if (id == R.id.action_download_new_file) {
            final int templeteId = R.string.templete_code_new_file_lua;
            createFileByTemplete(templeteId);
        } else if (id == R.id.action_download_new_file_contain_code) {
            final int templeteId = R.string.templete_code_insert_reply_msg_code_lua;
            createFileByTemplete(templeteId);
        }
        return true;
    }

    private void createFileByTemplete(final int templeteId) {
        DialogUtils.showEditDialog(this, "输入插件名(不要包含后缀名或者特殊符号)", "新建一个Lua脚本插件", new INotify<String>() {
            @Override
            public void onNotify(String param) {
                if (!param.endsWith(".lua")) {
                    param = param + ".lua";
                }
                final File file = new File(LuaPluginUtil.getDefaultPath(LuaPluginManagerActivity.this), param);
                boolean exists = file.exists();
                if (exists) {
                    DialogUtils.showConfirmDialog(LuaPluginManagerActivity.this, "插件目录已定义此文件，是否直接打开", new INotify<Void>() {
                        @Override
                        public void onNotify(Void param) {
                            enterPluginEditPage(file.getAbsolutePath());
                        }
                    });
                } else {
                    try
                    {

//                            boolean newFile = file.createNewFile();
                        FileUtils.writeStringToFile(file, AppContext.getInstance().getString(templeteId), "utf-8");
                        enterPluginEditPage(file.getAbsolutePath());
                        mChanage = true;
                        queryData();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LuaPluginManagerActivity.this, "创建文件失败,请检查存储设备空间是否已满?" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onQueryFinish() {
        super.onQueryFinish();
    }

    @Override
    protected int getAdapterType() {
        return AccountType.TYPE_PLUGIN;
    }


    @Override
    protected void doSubmitInsertSuccEvent(PluginBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_PLUGIN);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    @Override
    protected boolean needInterceptLongClick(PluginBean bean, int position) {

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_PLUGIN) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String path = null;
                try {
                    path = AppUtils.getPath(this, uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "无法选择文件,可能此文件不允许访问", Toast.LENGTH_SHORT).show();
                    return;
                }
                final File file = new File(path);
                if (!file.exists()) {
                    AppContext.showToast("你选择的文件不存在");

                } else {
                    if (!file.getAbsolutePath().endsWith("apk")
                            && !file.getAbsolutePath().endsWith("zip")
                            && !file.getAbsolutePath().endsWith("jar")
                            && !file.getAbsolutePath().endsWith("qssq")
                            && !file.getAbsolutePath().endsWith("dex")) {

                        AppContext.showToast("你选择的文件不是情迁机器人插件文件,目前支持apk,jar,zip,qssq.dex后缀文件");
                    } else if (!file.exists()) {
                        AppContext.showToast("文件不存在");
                    } else {
                        final File targetFile = new File(LuaPluginUtil.getDefaultPath(AppContext.getInstance()), file.getName());
                        if (targetFile.exists()) {
                            targetFile.delete();
                            AppContext.showToast("插件目录已经安装此文件,强制覆盖");
                        }
                        new QssqTask<Object>(new QssqTask.ICallBack() {
                            @Override
                            public Object onRunBackgroundThread() {
                                try {
                                    FileUtils.copyFile(file, targetFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return e;
                                }
                                return true;
                            }

                            @Override
                            public void onRunFinish(Object o) {
                                if (o instanceof Exception) {
                                    AppContext.showToast("安装失败 " + ((Exception) o).getMessage());
                                } else {
                                    mChanage = true;
                                    queryData();
                                }
                            }
                        }).execute();
                    }

                }
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onAddItemClick() {
        AppUtils.chooseFile(this, REQUEST_CODE_SELECT_PLUGIN);

    }

    @Override
    protected String[] getLongPressMenu() {
        return new String[]{"编辑",
                "删除",
                "分享",
                "运行"
                , "运行Lua界面GUI"
        };
    }


    public boolean showError(PluginBean bean) {

        if (bean.getPluginHolder() != null && bean.getPluginHolder().getPluginInterface() instanceof ErrorPluginInterface) {
            final ErrorPluginInterface errorPluginInterface = ((ErrorPluginInterface) bean.getPluginHolder().getPluginInterface());

            final String msg = errorPluginInterface.getErrorMsg();
            AlertDialog alertDialog = DialogUtils.showConfirmDialog(this, "此插件加载错误,通常是插件的ｓｄｋ版本和机器人版本不一致导致的(已复制到剪辑版,若不是你认为的错误，请粘贴剪辑版内容发给作者)\n错误文件路径:" + errorPluginInterface.getErrorFile() + "\n" + msg, "发送错误文件", "复制到剪辑版", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    try {
                        Intent shareFileIntent = AppUtils.getShareFileIntent(errorPluginInterface.getErrorFile());
                        LuaPluginManagerActivity.this.startActivity(shareFileIntent);


                    } catch (Exception e) {
                        AppContext.showToast("无法分享文件" + e.getMessage());
                    }
                }
            }, new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    AppUtils.copy(LuaPluginManagerActivity.this, msg);
                    Toast.makeText(LuaPluginManagerActivity.this, "复制完成！", Toast.LENGTH_SHORT).show();

                }
            });
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            return true;
        }
        return false;
    }


    @Override
    protected void onLongEditClick(int position) {
        PluginBean bean = adapter.getList().get(position);
        String path = bean.getPath();
        enterPluginEditPage(path);

    }

    private void enterPluginEditPage(String path) {
        Intent intent = new Intent(this, LuaEditCodeActivity.class);
        intent.putExtra(IntentCns.INTENT_FILE_PATH, path);
        startActivity(intent);
    }

    @Override
    protected void onLongOtherClick(int position, int which) {
        PluginBean bean = adapter.getList().get(position);


        switch (which) {
            case 2:

                Intent shareFileIntent = AppUtils.getShareFileIntent(new File(bean.getPath()));
                try {
                    this.startActivity(shareFileIntent);

                } catch (Exception e) {
                    AppContext.showToast(e.getMessage() + "无法分享此插件,请手动操作");
                }
                break;
            case 3:

                if (showError(bean)) {
                    return;
                }

                LuaUtil.runByGUI(bean.getPath(), true, this);
                break;
            case 4: {

                if (showError(bean)) {
                    return;
                }
                FrameLayout rootView = new FrameLayout(this);
                try {
                    View ui = bean.getPluginInterface().onConfigUi(rootView);
                    if (ui == null) {
                        AppContext.showToast(this, "该插件不支持显示UI界面哦！");
                        return;
                    }


                    Dialog dialog = new Dialog(this);
                    rootView.setBackgroundColor(Color.WHITE);
                    rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //处理宽度高度问题
                    Window dialogWindow = dialog.getWindow();


                    dialogWindow.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(android.R.color.transparent)));
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                    lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.TOP;

                    dialogWindow.setAttributes(lp);
                    //创建标题栏
                    rootView.addView(ui, 0);//标题栏当然要添加到顶部
                    dialog.setContentView(rootView);
                    dialog.show();

                } catch (Throwable e) {

                    DialogUtils.showDialog(LuaPluginManagerActivity.this, "启动失败！可能是插件ｓｄｋ和机器人需要的ｓｄｋ不一致！请联系插件作者升级插件,错误详情\n " + Log.getStackTraceString(e));
                }
            }

            break;
        }
    }


    @Override
    protected long onInsertToDb(PluginBean bean) {
        return 0;
    }


    @Override
    protected void doDataChanageSuccEvent() {
       /* AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_PLUGIN);
        EventBus.getDefault().post(event);*/
    }

    @Override
    protected int onDeleteToDb(PluginBean bean) {
        return LuaPluginUtil.deletePlugin(bean.getPluginInterface(), bean.getPath());
    }

    protected void doOnRefresh() {
        mChanage = true;
        queryData();
    }

    @Override
    public void queryData() {
        if (mChanage) {

            mChanage = false;
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this);
            progressDialog.setTitle("查询中");
            progressDialog.show();
            RobotContentProvider.getInstance().initLuaPlugin(new INotify() {
                @Override
                public void onNotify(Object param) {
                    progressDialog.dismiss();
                    reLoadPlugin();
                }
            });
        } else {

            reLoadPlugin();
        }
    }

    private void reLoadPlugin() {
        List<IPluginHolder> pluginList = RobotContentProvider.getInstance().getLuaPluginList();
        List<PluginBean> list = new ArrayList<>();
        for (IPluginHolder pluginModelQuery : pluginList) {
            PluginBean bean = new PluginBean();
            bean.setPath(pluginModelQuery.getPath());
            bean.setPluginHolder(pluginModelQuery);
            list.add(bean);
        }

        File defaultPluginApkPath = LuaPluginUtil.getDefaultPath(AppContext.getInstance());
        File[] files = defaultPluginApkPath.listFiles(new LogFileFilter());
        if (files != null) {

            for (File file : files) {
                try {
                    String json = FileUtils.readFileToString(file, "utf-8");
                    if (json.startsWith("{")) {
                        ErrorPluginInterface pluginInterface = new ErrorPluginInterface(file, json);
                        QueryPluginModel pluginHolder = new QueryPluginModel();
                        String path = pluginInterface.getPath();
                        if (path != null && new File(path).exists()) {

                            pluginHolder.setPath(path);
                            pluginHolder.setOfficial(false);
                            pluginHolder.setDisableFlag(true);
                            pluginHolder.setPluginInterface(pluginInterface);


                            PluginBean bean = new PluginBean();
                            bean.setPath(pluginInterface.getPath());
                            bean.setPluginHolder(pluginHolder);
                            list.add(bean);

                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        doOnQueryFinish(list);
    }

    public class LogFileFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            String absolutePath = pathname.getAbsolutePath();
            return absolutePath.endsWith(".log");
        }
    }


    @Override
    protected List<PluginBean> queryDataFromDb() {
        throw new RuntimeException("作废的操作");


    }


}
