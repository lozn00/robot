package cn.qssq666.robot.selfplugin;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by qssq on 2018/5/9 qssq666@foxmail.com
 */
public interface IRobotContentProvider {

    boolean getBooleanConfig(String key);

    void reloadSharedPreferences();
    SharedPreferences getSharedPreferences();

    public boolean writeConfig(Object beforeObj,String key, String value) ;

    SQLiteDatabase getRobotDb();

    public void reloadPlugin(final Handler.Callback callback);


    void writeBooleanConfig(String key, boolean isChecked);

    boolean hasDisablePlugin(IPluginHolder holder);

    boolean disablePlugin(IPluginHolder  holder, boolean isChecked);

    interface IContentProviderNotify {
        void notifyChange(@NonNull Uri uri, @Nullable IRobotContentProvider observer);

    }

    void addObserver(IContentProviderNotify providerNotify);

    void clearObserver();

    boolean onAttachIHostControlApi(IHostControlApi hostControlApi);

    Context getProxyContext();
    ClassLoader getProxyClassloader();

    void setProxyContext(Context context);
    void setProxyClassloader(ClassLoader classloader);

    void interceptNotifyChanage(boolean intercept);


    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    public String getType(Uri uri);

    public Uri insert(Uri uri, ContentValues values);


    public boolean onCreate();

    public void notifyChange(@NonNull Uri uri, @Nullable IRobotContentProvider observer);


    public int delete(Uri uri, String selection, String[] selectionArgs);

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);


    public String getPluginInfo();
    public View showOperaUi(ViewGroup viewGroup);

    /**
     * 初始化context.createPakcageContext
     * @param context
     * @param resources
     */
    public void setProxyResources(Context context,Resources resources);
    public void reload();
    public void testApi();
    public List<IPluginHolder> getPluginList();

    public Resources getResources();
    public Context getRobotContext();

    public String getLastErrorMsg();


}


