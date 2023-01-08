package cn.qssq666.tencent5x;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.ValueCallback;

import static android.app.Activity.RESULT_OK;

import cn.qssq666.robot.R;


/**
 * Created by jingbin on 2019/1/15.
 * - 播放网络视频配置
 * - 上传图片(兼容)
 */
public class MyX5WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public static int FILECHOOSER_RESULTCODE = 1;
    public static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private View mXProgressVideo;
    private X5WebViewActivity mActivity;
    private IX5WebPageView mIWebPageView;
    private View mXCustomView;
    private IX5WebChromeClient.CustomViewCallback mXCustomViewCallback;

    public MyX5WebChromeClient(IX5WebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        this.mActivity = (X5WebViewActivity) mIWebPageView;
    }

    /**
     * 播放网络视频时全屏会被调用的方法
     */
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        mIWebPageView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mIWebPageView.hindWebView();
        // 如果一个视图已经存在，那么立刻终止并新建一个
        if (mXCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mIWebPageView.fullViewAddView(view);
        mXCustomView = view;
        mXCustomViewCallback = callback;
        mIWebPageView.showVideoFullView();
    }

    /**
     * 视频播放退出全屏会被调用的
     */
    @Override
    public void onHideCustomView() {
        // 不是全屏播放状态
        if (mXCustomView == null) {
            return;
        }
        mIWebPageView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mXCustomView.setVisibility(View.GONE);
        if (mIWebPageView.getVideoFullView() != null) {
            mIWebPageView.getVideoFullView().removeView(mXCustomView);
        }
        mXCustomView = null;
        mIWebPageView.hindVideoFullView();
        mXCustomViewCallback.onCustomViewHidden();
        mIWebPageView.showWebView();
    }

    /**
     * 视频加载时loading
     */
    @Override
    public View getVideoLoadingProgressView() {
        if (mXProgressVideo == null) {
            mXProgressVideo = mIWebPageView.getVideoLoadingProgressView();
        }
        return mXProgressVideo;
    }

    @Override
    public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mIWebPageView.startProgress(newProgress);
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (mXCustomView != null);
    }

    @Override
    public void onReceivedTitle(com.tencent.smtt.sdk.WebView view, String title) {
        super.onReceivedTitle(view, title);
        // 设置title
        mIWebPageView.onReceivedTitle(view, title);
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mIWebPageView.startFileChooserForResult(Intent.createChooser(intent, "文件选择"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");

        mIWebPageView.startFileChooserForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    /**
     * 5.0以下 上传图片成功后的回调
     */
    public void mUploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    public void mUploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5) {
            return;
        }
        Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        if (super.getDefaultVideoPoster() == null) {
            return BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_menu_continue);
        } else {
            return super.getDefaultVideoPoster();
        }
    }
}
