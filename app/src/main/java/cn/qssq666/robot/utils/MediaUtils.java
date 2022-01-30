package cn.qssq666.robot.utils;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.app.AppContext;


/**
 * Created by luozheng on 16/1/27.
 */
public class MediaUtils {
    private static final String TAG = "MediaUtils";


    /*
        public  static  void initVideoPlayer(MediaController mediaController, VideoView videoView, String path) {
            videoView.setVideoPath(path);
            videoView.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoView);
            // 让VideoView获取焦点
            videoView.requestFocus();
            // 开始播放
            mediaController.show(0);
        }

    */
    public static File getSdcardpApkFileName(String file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(AppContext.getInstance().getExternalCacheDir(), crashStr + File.separator + file);//放到crashStr目录管理
        } else {
//            return null;
            throw new RuntimeException("请插入内存卡再进行更新!");
        }
    }

    /**
     * 从临时缓存目录中生成一个随机图片文件
     *
     * @return
     */
    public static File getTempCachePictureFileName() {
        return new File(getCachePath(), getPhotoFileName());
    }

    /**
     * 格式为 20160128115859
     *
     * @return
     */
    public static File getTempCacheAudioFileName() {
        return new File(getCachePath(), getAudioFileName());
    }

    public static File getTempCacheVideoFileName() {
        return new File(getCachePath(), getVideoFileName());
    }

    public static File getMusicPath() {
        return getMeimiPath(mediaStr);
    }

    public static File getDataPath() {
        return getMeimiPath(dataStr);
    }

    public static File getBackupPath() {
        return getMeimiPath(backupDir);
    }

    public static File getCachePath() {

        return getMeimiPath(cacheStr);
    }

    /**
     * 对于崩溃日志 还是 优先内存卡处理,
     *
     * @return
     */

    public static File getCrashPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && readSDCardAvailableBlocks() > 1024) {
            return new File(AppContext.getInstance().getExternalCacheDir(), crashStr);
        }
        return getMeimiPath(crashStr);
    }

    public static File getMeimiRootPath() {
        return getMeimiPath(BuildConfig.APPLICATION_ID);
    }

    public static long readSDCardAvailableBlocks() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            return getFolderAvailableSpace(sdcardDir.getPath());
/*            Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
            Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");*/
        } else {
            return -1;
        }
    }

    public static long getFolderAvailableSpace(String path) {
        StatFs sf = new StatFs(path);
        long blockSize;
//        long blockCount;
        long availCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
//            blockCount = sf.getBlockCountLong();
            availCount = sf.getAvailableBlocksLong();
        } else {
            blockSize = sf.getBlockSize();
//            blockCount = sf.getBlockCount();
            availCount = sf.getAvailableBlocks();
        }
        return availCount * blockSize / 1024;
    }

    public static long readSystemAvailableBlocks() {
        File dataDirectory = Environment.getDataDirectory();
        return getFolderAvailableSpace(dataDirectory.getPath());
/*            Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
            Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");*/
    }

    public static File getMeimiPath(String path) {
/*        Log.d(TAG, "系统空间:" + FileUtils.readSystemAvailableBlocks() + "k," + Formatter.formatFileSize(AppContext.getInstance(), FileUtils.getAvailableInternalMemorySize()));
        Log.d(TAG, "磁盘空间:" + FileUtils.readSDCardAvailableBlocks() + "k,," + Formatter.formatFileSize(AppContext.getInstance(), FileUtils.getAvailableExternalMemorySize()));*/
        long availableInternalMemorySize = readSystemAvailableBlocks();
       /* long availableExternalMemorySize = -1;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            availableExternalMemorySize = readSDCardAvailableBlocks();
        }*/


/*        if (availableInternalMemorySize == availableExternalMemorySize) {
//            Log.d(TAG, "您的内存卡就是内部存储卡");
            File file = MediaUtils.productSystemCacheFolder(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }*/
        File dir = null;
        //如果没有 内存卡 或者 有内存卡 但是控件空间不足了 || FileUtils.readSDCardAvailableBlocks() < 1024
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && readSDCardAvailableBlocks() < 1024) {
            Log.d(TAG, "存储卡空间不足！");
            AppContext.showToast("警告,您的手机系统存储空间和内存卡磁盘空间均内存不足了,请马上清理磁盘,否则将导致程序异常崩溃,许多功能无法正常使用！");
            dir = MediaUtils.productSystemCacheFolder(path);
        } else {
            dir = new File(AppContext.getInstance().getExternalCacheDir(), path);
        }

    /*    } else {
            dir = new File(AppContext.getInstance().getExternalCacheDir(), path);
//            dir = MediaUtils.productSystemCacheFolder(path);
        }*/
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public final static String meimiDir = "" + BuildConfig.APPLICATION_ID;
    public final static String cacheStr = meimiDir + "/temp";
    public final static String mediaStr = meimiDir + "/media";

    private static String backupDir = meimiDir + "/backup";
    public final static String crashStr = meimiDir + "/crash";
    public final static String dataStr = meimiDir + "/data";

    private static String productFileName(String postfix) {
        Date date = new Date(System.currentTimeMillis()); //2016-01-28 12:02:28  14位年月日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + MediaUtils.getRandom(3) + postfix;
    }

    public static String getPhotoFileName() {
        return productFileName(".jpg");
    }

    public static String getAudioFileName() {
        return productFileName(".amr");
    }

    public static String getVideoFileName() {
        return productFileName(".mp4");
    }

    public static int getRandom(int n) {
        int ans = 0;
        while (Math.log10(ans) + 1 < n)
            ans = (int) (Math.random() * Math.pow(10, n));
        return ans;
    }

    /**
     * 得到视频缩略图
     *
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static boolean clearCache() {
        return delAllFile(getCachePath().getAbsolutePath());
    }

    public static boolean clearAllCache() {
        File externalCacheDir = AppContext.getInstance().getExternalCacheDir();
        File cacheDir = AppContext.getInstance().getCacheDir();
        boolean result = false;
        if (cacheDir != null) {
            result = delAllFile(cacheDir.getAbsolutePath());
        }
        if (externalCacheDir != null) {

            result = delAllFile(externalCacheDir.getAbsolutePath()) || result ? true : false;//原生是真 或者现在是true就是true
        }
        return result;
    }

    public static boolean clearCrash() {
        return delAllFile(getCrashPath().getAbsolutePath());
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径
    private static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static File getVolleyCacheFile() {
        return productSystemCacheFolder("volley");
    }

    public static File getImageUniversalCacheFile() {
        return productSystemCacheFolder("uil-images");
    }

    public static File productSystemCacheFolder(String file) {
        File cacheDir = AppContext.getInstance().getCacheDir();
        return new File(cacheDir, file);
    }
}
