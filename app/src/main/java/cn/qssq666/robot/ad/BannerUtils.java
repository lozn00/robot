package cn.qssq666.robot.ad;
import cn.qssq666.CoreLibrary0;import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import cn.qssq666.robot.app.AppContext;

/**
 * Created by qssq on 2018/5/14 qssq666@foxmail.com
 */
public class BannerUtils {
    private static final String TAG = "BannerUtils";

    public static  BannerUtils getInstance() {
        return new BannerUtils();
    }

    public BannerUtils show(AdView adView) {


        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
//        adView = binding.adView;
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (onListener != null) {
                    onListener.onLoaded();
                }
                // Code to be executed when an ad finishes loading.

                Log.w(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (onListener != null) {

                    onListener.onFailed(errorCode);
                }
                Log.w(TAG, "banner  onAdFailedToLoad code" + errorCode);
                // Code to be executed when an ad request fails.
                AppContext.showToast("您的网络服务访问谷歌,您将不能间接或者直接赞助作者啦...");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.w(TAG, "onAdOpened");
                AppContext.showToast("谢谢你,希望你把这打开广告的截图发给我，我很开心的,但是你的网络能访问谷歌吗？不然也是白搭 呜呜呜");
            }

            @Override
            public void onAdLeftApplication() {
                Log.w(TAG, "onAdLeftApplication");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                Log.w(TAG, "onAdClosed");

                AppContext.showToast("关闭无所谓,但是我好可怜的 希望多多支持我嘛");
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        adView.loadAd(adRequest);
        return this;
    }

    public OnListener getOnListener() {
        return onListener;
    }

    public BannerUtils setOnListener(OnListener onListener) {
        this.onListener = onListener;
        return this;
    }

    OnListener onListener;

    public interface OnListener {
        void onLoaded();

        void onFailed(int code);
    }
}
