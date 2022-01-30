package cn.qssq666.robot.activity.intent;
import cn.qssq666.CoreLibrary0;import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cn.qssq666.robot.utils.AppUtils;

/**
 * Created by qssq on 2018/11/16 qssq666@foxmail.com
 */
public class BaseIntentActivity extends NotUiActivity {
    private static final String TAG = "RunLuaTAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TextView view = new TextView(this);
//        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
//        setContentView(view);
        onNewIntent(getIntent());
        setFinishOnTouchOutside(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            handleIntent(intent);
        } catch (Exception e) {
            Log.w(TAG, Log.getStackTraceString(e));
            Toast.makeText(this, "操作错误" + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
            finish();
        }
    }


    //URI:content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/qssq666/robot_plugin_lua/plugin.lua
    private void handleIntent(Intent intent) throws Exception {

        if (intent == null) {
            onNotIntentData();
            return;
        }
        Uri uri = intent.getData();
        String scheme = uri.getScheme();
        Log.w(TAG, "URI:" + uri + ",scheme:" + scheme);
        if (uri == null || !"content".equals(scheme)) {

            String path = intent.getData().getPath();
            if (!TextUtils.isEmpty(path)) {
                onReceivePath(path);
                return;
            }
            return;
        }
  /*      InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
            Log.w(TAG, "LINE:" + line);
        }
        inputStream.close();*/
        String fileOrContent = AppUtils.getStrContentByUri(this, uri);
        onReceiveStr(fileOrContent);
        return;


//        new ScriptOperations(this, null).importFile("", inputStream, ext).subscribe(new ImportIntentActivity$$Lambda$0(this));
    }

    private void onNotIntentData() {

    }

    protected void onReceiveStr(String fileOrContent) {
    }

    protected void onReceivePath(String path) {
    }

}
