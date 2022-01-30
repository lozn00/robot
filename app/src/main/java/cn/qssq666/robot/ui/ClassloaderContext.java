package cn.qssq666.robot.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by qssq on 2018/5/17 qssq666@foxmail.com
 */
public class ClassloaderContext extends Context {

    private final Context context;
    private final ClassLoader classLoader;
    private final Context onlyReadContext;

    public ClassloaderContext(Context context, Context onlyReadContext, ClassLoader classLoader) {
        this.onlyReadContext = onlyReadContext;
        this.context = context;
        this.classLoader = classLoader;
    }

    @Override
    public AssetManager getAssets() {
        return context.getAssets();
    }

    @Override
    public Resources getResources() {
        return context.getResources();
    }

    @Override
    public PackageManager getPackageManager() {
        return context.getPackageManager();
    }

    @Override
    public ContentResolver getContentResolver() {
        return context.getContentResolver();
    }

    @Override
    public Looper getMainLooper() {
        return context.getMainLooper();
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void setTheme(int resid) {
        context.setTheme(resid);
    }

    @Override
    public Resources.Theme getTheme() {
        return context.getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public String getPackageName() {
        return context.getPackageName();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return context.getApplicationInfo();
    }

    @Override
    public String getPackageResourcePath() {
        return context.getPackageResourcePath();
    }

    @Override
    public String getPackageCodePath() {
        return context.getPackageCodePath();
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return context.getSharedPreferences(name,mode);
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean deleteSharedPreferences(String name) {
        return context.deleteSharedPreferences(name);
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return context.openFileInput(name);
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return context.openFileOutput(name,MODE_APPEND);
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable String type) {
        return context.getExternalFilesDir(type);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public File[] getExternalFilesDirs(String type) {
        return context.getExternalFilesDirs(type);
    }

    @Override
    public File getObbDir() {
        return context.getObbDir();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public File[] getObbDirs() {
        return context.getObbDirs();
    }

    @Override
    public File getCacheDir() {
        return context.getCacheDir();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public File getCodeCacheDir() {
        return context.getCodeCacheDir();
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return context.getExternalCacheDir();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public File[] getExternalCacheDirs() {
        return context.getExternalCacheDirs();
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return context.fileList();
    }

    @Override
    public File getDir(String name, int mode) {
        return context.getDir(name,mode);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return context.openOrCreateDatabase(name,mode,factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        return context.openOrCreateDatabase(name,mode,factory,errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        return context.moveDatabaseFrom(sourceContext,name);
    }

    @Override
    public boolean deleteDatabase(String name) {
        return context.deleteDatabase(name);
    }

    @Override
    public File getDatabasePath(String name) {
        return context.getDatabasePath(name);
    }

    @Override
    public String[] databaseList() {
        return context.databaseList();
    }

    @Override
    public Drawable getWallpaper() {
        return context.getWallpaper();
    }

    @Override
    public Drawable peekWallpaper() {
        return context.peekWallpaper();
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return context.getWallpaperDesiredMinimumWidth();
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return context.getWallpaperDesiredMinimumHeight();
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {
        context.setWallpaper(bitmap);

    }

    @Override
    public void setWallpaper(InputStream data) throws IOException {
        context.setWallpaper(data);
    }

    @Override
    public void clearWallpaper() throws IOException {
        context.clearWallpaper();
    }

    @Override
    public void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        context.startActivity(intent,options);
    }

    @Override
    public void startActivities(Intent[] intents) {
        context.startActivities(intents);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        context.startActivities(intents,options);
    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        context.startIntentSender(intent,fillInIntent,flagsMask,flagsValues,extraFlags);
    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {
        context.sendBroadcast(intent);
    }

    @Override
    public void sendBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String receiverPermission, @Nullable BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler, int flags) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return null;
    }

    @Override
    public boolean stopService(Intent service) {
        return false;
    }

    @Override
    public boolean bindService(Intent service, @NonNull ServiceConnection conn, int flags) {
        return false;
    }

    @Override
    public void unbindService(@NonNull ServiceConnection conn) {

    }

    @Override
    public boolean startInstrumentation(@NonNull ComponentName className, @Nullable String profileFile, @Nullable Bundle arguments) {
        return false;
    }

    @Nullable
    @Override
    public Object getSystemService(@NonNull String name) {
        return context.getSystemService(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public String getSystemServiceName(@NonNull Class<?> serviceClass) {
        return (String) context.getSystemService(serviceClass);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkPermission(@NonNull String permission, int pid, int uid) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingPermission(@NonNull String permission) {
        return context.checkSelfPermission(permission);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingOrSelfPermission(@NonNull String permission) {
        return context.checkCallingOrSelfPermission(permission);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    public int checkSelfPermission(@NonNull String permission) {
        return context.checkSelfPermission(permission);
    }

    @Override
    public void enforcePermission(@NonNull String permission, int pid, int uid, @Nullable String message) {

    }

    @Override
    public void enforceCallingPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        context.grantUriPermission(toPackage,uri,modeFlags);
    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int checkUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags, @Nullable String message) {


    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return context.createPackageContext(packageName,flags);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Context createContextForSplit(String splitName) throws PackageManager.NameNotFoundException {
        return context.createContextForSplit(splitName);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public Context createConfigurationContext(@NonNull Configuration overrideConfiguration) {
        return context.createConfigurationContext(overrideConfiguration);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public Context createDisplayContext(@NonNull Display display) {
        return context.createDisplayContext(display);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Context createDeviceProtectedStorageContext() {
        return context.createDeviceProtectedStorageContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean isDeviceProtectedStorage() {
        return context.isDeviceProtectedStorage();
    }
}
