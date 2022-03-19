package cn.qssq666.robot.activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import androidx.annotation.Nullable;
import cn.qssq666.robot.R;
import cn.qssq666.robot.ad.BannerUtils;
import cn.qssq666.robot.ad.InterstitialAdUtil;
import cn.qssq666.robot.app.AppContext;

/**
 * Created by qssq on 2018/4/21 qssq666@foxmail.com
 */
@Deprecated
public class WatchAdActivity extends SuperActivity {


    AdView adView;

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("看广告也需要翻墙..");
        setContentView(R.layout.activity_watch_ad);


//        MobileAds.initialize(this, AppContext.getInstance().getString(R.string.google_app_id_));
        adView = findViewById(R.id.ad_view);
        final TextView textView = (TextView) findViewById(R.id.tv_tip);
        final TextView textViewBanner = (TextView) findViewById(R.id.tv_bannr_ad);

        BannerUtils.getInstance().setOnListener(new BannerUtils.OnListener() {
            @Override
            public void onLoaded() {
                textViewBanner.setText("banner加载完毕，没显示？锁屏再解锁");

            }

            @Override
            public void onFailed(int code) {
                textViewBanner.setText("banner广告加载失败 code="+code+",google封禁了我的账号，因为可能有人反复的点击广告。");

            }
        }).show(adView);


        final InterstitialAdUtil adUtil = new InterstitialAdUtil();

        adUtil.init(this, AppContext.getInstance().getString(R.string.insertscreen_ad_unit_id_watch_ad));
        adUtil.startRequest();

        adUtil.setOnListener(new InterstitialAdUtil.OnListener() {
            @Override
            public void onSHow() {
                textView.setText("插屏广告显示了");

            }

            @Override
            public void onClose() {
                textView.setText("插屏广告被关闭了");
                adUtil.startRequest();
            }

            @Override
            public void onFailLoad(int i) {

                textView.setText("插屏广告加载失败");
            }

            @Override
            public void onLoaded() {
                textView.setText("插屏广告加载完成");
            }
        });


        AppContext.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {


//                boolean show = adUtil.isLoadding();
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
        AppContext.getHandler().removeCallbacksAndMessages(null);
    }
}
