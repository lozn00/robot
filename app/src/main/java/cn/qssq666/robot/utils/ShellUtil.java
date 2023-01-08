package cn.qssq666.robot.utils;

import android.os.Build;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import cn.qssq666.robot.interfaces.ICmdIntercept;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
//ignore_start

/**
 * Created by qssq on 2018/7/8 qssq666@foxmail.com
 */
//接个界面不会引入xposed包
public class ShellUtil {
    /**
     * 免root机的virtual xposed就可以直接返回false
     *
     * @return
     */
    public static boolean hasRoot() {
        String[] strArr = new String[]{"/system/bin/", "/system/xbin/", "/su/bin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        while (i < strArr.length) {
            try {
                if (new File(strArr[i] + "su").exists()) {
                    return true;
                }
                i++;
            } catch (Exception e) {

                return false;
            }
        }
        return false;
    }


    public static boolean hasRoot1() {

/**
 * 方式二
 * */
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean hasRoot2() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            int i = process.waitFor();
            if (0 == i) {
                process = Runtime.getRuntime().exec("su");
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public static boolean execute(String str) {
//        Log.w("EXECUTE", Log.getStackTraceString(new Throwable()) + str);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"su", "-c", str});
            processBuilder.redirectErrorStream(true);
            Process start = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String readLine = bufferedReader.readLine();
            if (readLine == null || readLine.compareTo("Permission denied") != 0) {
                start.waitFor();
                return true;
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public static String executeAndFetchResult(String str) {

        Pair<String, Exception> stringExceptionPair = executeAndFetchResultPair(str, null);
        return stringExceptionPair.first;
    }

    /**
     * @param str
     * @param iIntercept 是否继续
     * @return
     */
    public static Pair<String, Exception> executeAndFetchResultPair(String str, ICmdIntercept<String> iIntercept) {
        return executeAndFetchResultPair(new String[]{str}, false,15, iIntercept);
    }

    public static Pair<String, Exception> executeAndFetchResultPair(String str,boolean atMainThread,long waitTime, ICmdIntercept<String> iIntercept) {
        return executeAndFetchResultPair(new String[]{str},  atMainThread,waitTime,iIntercept);
    }

    public static Pair<String, Exception> executeAndFetchResultPair(String[] str,ICmdIntercept<String> iIntercept) {
        return executeAndFetchResultPair(str,  false,15,iIntercept);
    }

    public static Pair<String, Exception> executeAndFetchResultPair(String str[],  boolean atMainThread,long waitTime,ICmdIntercept<String> iIntercept) {
//        Log.w("EXECUTE", Log.getStackTraceString(new Throwable()) + str);
        int[] isEnd = {0, 0, 0, 0, 0};
        try {
            String[] exestrs;
            if (hasRoot()) {
                String[] strings = new String[str.length + 2];
                System.arraycopy(str, 0, strings, 2, str.length);
                strings[0] = "su";
                strings[1] = "-c";
                exestrs = strings;
            } else {
                exestrs = str;

            }
            ProcessBuilder processBuilder = new ProcessBuilder(exestrs);
            processBuilder.redirectErrorStream(true);
            java.lang.Process process = processBuilder.start();
            StringBuffer sb = new StringBuffer();
            Observable<String> stand = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    ShellUtil.loopPrint(sb, process.getInputStream(), iIntercept, "输入流");
                    isEnd[1] = 1;
                    int sleep = 100;
              /*      while (isEnd[0]!=1) {
                        isEnd[3] = 50 + isEnd[3];

                        if (isEnd[0] == 1 || isEnd[3] >= sleep * 25) {//>2500
                            break;
                        }

                        Thread.sleep(sleep);//避免先删除正确信息又输出错误信息


                    }*/
                    emitter.onNext(sb.toString());

                }
            });

            stand = stand.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            stand.subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {

                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
            Observable<String> errorStream = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    ShellUtil.loopPrint(sb, process.getErrorStream(), iIntercept, "错误流");
        /*            isEnd[2] = 1;
                    while ( isEnd[0]!=1) {
                        Thread.sleep(50);//避免先删除正确信息又输出错误信息
                        isEnd[4] = 50 + isEnd[4];

                    }*/
                    emitter.onNext(sb.toString());
                }
            });
            errorStream = errorStream.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
            errorStream.subscribe(new Consumer<String>() {
                                      @Override
                                      public void accept(String s) throws Exception {

                                      }
                                  }
                    , new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                process.waitFor(waitTime, TimeUnit.SECONDS);
            } else {
                ShellUtil.waitFor(process, waitTime, TimeUnit.SECONDS);

            }
            isEnd[0] = 1;

            return Pair.create(sb.toString(), null);
        } catch (Exception e) {
            isEnd[0] = 1;
            return Pair.create(e.getMessage(), e);
        }

    }

    public static void loopPrint(StringBuffer sb, InputStream stream, ICmdIntercept<String> iIntercept) throws IOException {
        loopPrint(sb, stream, iIntercept, "");
    }

    public static void loopPrint(StringBuffer sb, InputStream stream, ICmdIntercept<String> iIntercept, String name) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));


        int count = 100;


        int totalcurrent = 0;
        int currentline = 0;//表示每次流失到最大次次数里面的总工室

        while (true) {


            String readLine = bufferedReader.readLine();
            if (readLine == null) {

                break;
            } else {
                if (totalcurrent != 0) {


                    if (totalcurrent == 1) {
                        sb.insert(0, "[0]");

                    }
                    sb.append("\n[" + totalcurrent + "]");
                }

                sb.append(readLine);

            }

            currentline++;
            totalcurrent++;

            if (currentline > count) {

                if (iIntercept == null || iIntercept.isNeedIntercept(sb.toString())) {
                    sb.append("\n(超过" + count + "行忽略)");
                    break;

                } else {
                    sb.setLength(0);
                    currentline = -1;
                }
            }

        }
        bufferedReader.close();
        if (iIntercept != null) {
            iIntercept.onComplete(name);
        }
    }

    public static boolean waitFor(java.lang.Process process, long timeout, TimeUnit unit)
            throws InterruptedException {
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);

        do {
            try {
                process.exitValue();
                return true;
            } catch (IllegalThreadStateException ex) {
                if (rem > 0) {

                    Thread.sleep(
                            Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
                }
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);
        return false;
    }

    //ignore_end
}
