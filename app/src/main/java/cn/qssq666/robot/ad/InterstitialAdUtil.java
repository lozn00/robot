package cn.qssq666.robot.ad;
import cn.qssq666.CoreLibrary0;import android.content.Context;
import android.util.Log;
/*

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
*/

import cn.qssq666.robot.R;

/**
 * Created by qssq on 2018/5/14 qssq666@foxmail.com
 */
public class InterstitialAdUtil {

   /* private static final String TAG = "InterstitialAdUtil";
    private InterstitialAd interstitialAd;
    private int succCount;

    public int getRequestCount() {
        return requestCount;
    }

    private int requestCount;

    public int getHasFailCount() {
        return hasFailCount;
    }

    private int hasFailCount;

    public int getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(int freeCount) {
        if(freeCount<0){
            freeCount=0;
        }
        this.freeCount = freeCount;
    }

    private  int freeCount=0;
    public void init(Context context) {

        init(context, context.getString(R.string.insertscreen_ad_unit_id1));
    }

    public InterstitialAdUtil init(Context context, String interstiltialadId) {

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(context);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(interstiltialadId);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.w(TAG, "onAdClosed ");
                if(onListener!=null){
                    onListener.onClose();
                }
//                startRequest();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                hasFailCount=0;
                freeCount++;
                Log.w(TAG, "onAdLoaded ");
                if(onListener!=null){
                    onListener.onLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if(onListener!=null){
                    onListener.onFailLoad(i);
                }
                hasFailCount++;
                Log.w(TAG, "ad load fail " + i);
            }
        });
        startRequest();
        return this;


    }

    public InterstitialAdUtil setOnListener(OnListener onListener) {
        this.onListener = onListener;
        return this;
    }

    OnListener onListener;

    public int loadSuccCount() {
        return succCount;
    }

    public interface  OnListener{
        void onSHow();
        void onClose();
        void onFailLoad(int i);

        void onLoaded();
    }


    public boolean startRequest() {
        requestCount++;
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);

            Log.w(TAG,"开始请求广告");
            return true;
        }
        else{
            Log.w(TAG,"请求广告失败，可能已经laod或者正在load");
            return false;
        }

    }

    public boolean show() {
        Log.w(TAG, "show ");
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            succCount++;
            interstitialAd.show();
            Show=true;
            return true;
        } else {
            startRequest();
            return false;
        }
    }

        boolean Show;


    public boolean isLoadding(){
        return interstitialAd!=null&&interstitialAd.isLoading();
    }*/
}
