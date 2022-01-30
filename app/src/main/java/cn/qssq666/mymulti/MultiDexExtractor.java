//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.qssq666.mymulti;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor {
    private static final String TAG = "MultiDex";
    private static final String DEX_PREFIX = "classes";
    private static final String DEX_SUFFIX = ".dex";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";
    private static final int MAX_EXTRACT_ATTEMPTS = 3;
    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final String KEY_CRC = "crc";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String KEY_DEX_CRC = "dex.crc.";
    private static final String KEY_DEX_TIME = "dex.time.";
    private static final int BUFFER_SIZE = 16384;
    private static final long NO_VALUE = -1L;
    private static final String LOCK_FILENAME = "MultiDex.lock";

    MultiDexExtractor() {
    }

    static List<? extends File> load(Context context, File sourceApk, File dexDir, String prefsKeyPrefix, boolean forceReload) throws IOException {
        Log.i("MultiDex", "MultiDexExtractor.load(" + sourceApk.getPath() + ", " + forceReload + ", " + prefsKeyPrefix + ")");
        long currentCrc = getZipCrc(sourceApk);
        File lockFile = new File(dexDir, "MultiDex.lock");
        RandomAccessFile lockRaf = new RandomAccessFile(lockFile, "rw");
        FileChannel lockChannel = null;
        FileLock cacheLock = null;
        IOException releaseLockException = null;

        List files;
        try {
            lockChannel = lockRaf.getChannel();
            Log.i("MultiDex", "Blocking on lock " + lockFile.getPath());
            cacheLock = lockChannel.lock();
            Log.i("MultiDex", lockFile.getPath() + " locked");
            if (!forceReload && !isModified(context, sourceApk, currentCrc, prefsKeyPrefix)) {
                try {
                    files = loadExistingExtractions(context, sourceApk, dexDir, prefsKeyPrefix);
                } catch (IOException var21) {
                    Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", var21);
                    files = performExtractions(sourceApk, dexDir);
                    putStoredApkInfo(context, prefsKeyPrefix, getTimeStamp(sourceApk), currentCrc, files);
                }
            } else {
                Log.i("MultiDex", "Detected that extraction must be performed.");
                files = performExtractions(sourceApk, dexDir);
                putStoredApkInfo(context, prefsKeyPrefix, getTimeStamp(sourceApk), currentCrc, files);
            }
        } finally {
            if (cacheLock != null) {
                try {
                    cacheLock.release();
                } catch (IOException var20) {
                    Log.e("MultiDex", "Failed to release lock on " + lockFile.getPath());
                    releaseLockException = var20;
                }
            }

            if (lockChannel != null) {
                closeQuietly(lockChannel);
            }

            closeQuietly(lockRaf);
        }

        if (releaseLockException != null) {
            throw releaseLockException;
        } else {
            Log.i("MultiDex", "load found " + files.size() + " secondary dex files");
            return files;
        }
    }

    private static List<MultiDexExtractor.ExtractedDex> loadExistingExtractions(Context context, File sourceApk, File dexDir, String prefsKeyPrefix) throws IOException {
        Log.i("MultiDex", "loading existing secondary dex files");
        String extractedFilePrefix = sourceApk.getName() + ".classes";
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        int totalDexNumber = multiDexPreferences.getInt(prefsKeyPrefix + "dex.number", 1);
        List<MultiDexExtractor.ExtractedDex> files = new ArrayList(totalDexNumber - 1);

        for(int secondaryNumber = 2; secondaryNumber <= totalDexNumber; ++secondaryNumber) {
            String fileName = extractedFilePrefix + secondaryNumber + ".zip";
            MultiDexExtractor.ExtractedDex extractedFile = new MultiDexExtractor.ExtractedDex(dexDir, fileName);
            if (!extractedFile.isFile()) {
                throw new IOException("Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
            }

            extractedFile.crc = getZipCrc(extractedFile);
            long expectedCrc = multiDexPreferences.getLong(prefsKeyPrefix + "dex.crc." + secondaryNumber, -1L);
            long expectedModTime = multiDexPreferences.getLong(prefsKeyPrefix + "dex.time." + secondaryNumber, -1L);
            long lastModified = extractedFile.lastModified();
            if (expectedModTime != lastModified || expectedCrc != extractedFile.crc) {
                throw new IOException("Invalid extracted dex: " + extractedFile + " (key \"" + prefsKeyPrefix + "\"), expected modification time: " + expectedModTime + ", modification time: " + lastModified + ", expected crc: " + expectedCrc + ", file crc: " + extractedFile.crc);
            }

            files.add(extractedFile);
        }

        return files;
    }

    private static boolean isModified(Context context, File archive, long currentCrc, String prefsKeyPrefix) {
        SharedPreferences prefs = getMultiDexPreferences(context);
        return prefs.getLong(prefsKeyPrefix + "timestamp", -1L) != getTimeStamp(archive) || prefs.getLong(prefsKeyPrefix + "crc", -1L) != currentCrc;
    }

    private static long getTimeStamp(File archive) {
        long timeStamp = archive.lastModified();
        if (timeStamp == -1L) {
            --timeStamp;
        }

        return timeStamp;
    }

    private static long getZipCrc(File archive) throws IOException {
        long computedValue = ZipUtil.getZipCrc(archive);
        if (computedValue == -1L) {
            --computedValue;
        }

        return computedValue;
    }

    private static List<MultiDexExtractor.ExtractedDex> performExtractions(File sourceApk, File dexDir) throws IOException {
        String extractedFilePrefix = sourceApk.getName() + ".classes";
        prepareDexDir(dexDir, extractedFilePrefix);
        List<MultiDexExtractor.ExtractedDex> files = new ArrayList();
        ZipFile apk = new ZipFile(sourceApk);

        try {
            int secondaryNumber = 2;

            for(ZipEntry dexFile = apk.getEntry("classes" + secondaryNumber + ".dex"); dexFile != null; dexFile = apk.getEntry("classes" + secondaryNumber + ".dex")) {
                String fileName = extractedFilePrefix + secondaryNumber + ".zip";
                MultiDexExtractor.ExtractedDex extractedFile = new MultiDexExtractor.ExtractedDex(dexDir, fileName);
                files.add(extractedFile);
                Log.i("MultiDex", "Extraction is needed for file " + extractedFile);
                int numAttempts = 0;
                boolean isExtractionSuccessful = false;

                while(numAttempts < 3 && !isExtractionSuccessful) {
                    ++numAttempts;
                    extract(apk, dexFile, extractedFile, extractedFilePrefix);

                    try {
                        extractedFile.crc = getZipCrc(extractedFile);
                        isExtractionSuccessful = true;
                    } catch (IOException var19) {
                        isExtractionSuccessful = false;
                        Log.w("MultiDex", "Failed to read crc from " + extractedFile.getAbsolutePath(), var19);
                    }

                    Log.i("MultiDex", "Extraction " + (isExtractionSuccessful ? "succeeded" : "failed") + " - length " + extractedFile.getAbsolutePath() + ": " + extractedFile.length() + " - crc: " + extractedFile.crc);
                    if (!isExtractionSuccessful) {
                        extractedFile.delete();
                        if (extractedFile.exists()) {
                            Log.w("MultiDex", "Failed to delete corrupted secondary dex '" + extractedFile.getPath() + "'");
                        }
                    }
                }

                if (!isExtractionSuccessful) {
                    throw new IOException("Could not create zip file " + extractedFile.getAbsolutePath() + " for secondary dex (" + secondaryNumber + ")");
                }

                ++secondaryNumber;
            }
        } finally {
            try {
                apk.close();
            } catch (IOException var18) {
                Log.w("MultiDex", "Failed to close resource", var18);
            }

        }

        return files;
    }

    private static void putStoredApkInfo(Context context, String keyPrefix, long timeStamp, long crc, List<MultiDexExtractor.ExtractedDex> extractedDexes) {
        SharedPreferences prefs = getMultiDexPreferences(context);
        Editor edit = prefs.edit();
        edit.putLong(keyPrefix + "timestamp", timeStamp);
        edit.putLong(keyPrefix + "crc", crc);
        edit.putInt(keyPrefix + "dex.number", extractedDexes.size() + 1);
        int extractedDexId = 2;

        for(Iterator var10 = extractedDexes.iterator(); var10.hasNext(); ++extractedDexId) {
            MultiDexExtractor.ExtractedDex dex = (MultiDexExtractor.ExtractedDex)var10.next();
            edit.putLong(keyPrefix + "dex.crc." + extractedDexId, dex.crc);
            edit.putLong(keyPrefix + "dex.time." + extractedDexId, dex.lastModified());
        }

        edit.commit();
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences("multidex.version", VERSION.SDK_INT < 11 ? 0 : 4);
    }

    private static void prepareDexDir(File dexDir, final String extractedFilePrefix) {
        FileFilter filter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return !name.startsWith(extractedFilePrefix) && !name.equals("MultiDex.lock");
            }
        };
        File[] files = dexDir.listFiles(filter);
        if (files == null) {
            Log.w("MultiDex", "Failed to list secondary dex dir content (" + dexDir.getPath() + ").");
        } else {
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File oldFile = var4[var6];
                Log.i("MultiDex", "Trying to delete old file " + oldFile.getPath() + " of size " + oldFile.length());
                if (!oldFile.delete()) {
                    Log.w("MultiDex", "Failed to delete old file " + oldFile.getPath());
                } else {
                    Log.i("MultiDex", "Deleted old file " + oldFile.getPath());
                }
            }

        }
    }

    private static void extract(ZipFile apk, ZipEntry dexFile, File extractTo, String extractedFilePrefix) throws IOException, FileNotFoundException {
        InputStream in = apk.getInputStream(dexFile);
        ZipOutputStream out = null;
        File tmp = File.createTempFile("tmp-" + extractedFilePrefix, ".zip", extractTo.getParentFile());
        Log.i("MultiDex", "Extracting " + tmp.getPath());

        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)));

            try {
                ZipEntry classesDex = new ZipEntry("classes.dex");
                classesDex.setTime(dexFile.getTime());
                out.putNextEntry(classesDex);
                byte[] buffer = new byte[16384];

                for(int length = in.read(buffer); length != -1; length = in.read(buffer)) {
                    out.write(buffer, 0, length);
                }

                out.closeEntry();
            } finally {
                out.close();
            }

            if (!tmp.setReadOnly()) {
                throw new IOException("Failed to mark readonly \"" + tmp.getAbsolutePath() + "\" (tmp of \"" + extractTo.getAbsolutePath() + "\")");
            }

            Log.i("MultiDex", "Renaming to " + extractTo.getPath());
            if (!tmp.renameTo(extractTo)) {
                throw new IOException("Failed to rename \"" + tmp.getAbsolutePath() + "\" to \"" + extractTo.getAbsolutePath() + "\"");
            }
        } finally {
            closeQuietly(in);
            tmp.delete();
        }

    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException var2) {
            Log.w("MultiDex", "Failed to close resource", var2);
        }

    }

    private static class ExtractedDex extends File {
        public long crc = -1L;

        public ExtractedDex(File dexDir, String fileName) {
            super(dexDir, fileName);
        }
    }
}
