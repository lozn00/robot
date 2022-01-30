package cn.qssq666.robot.activity;
import cn.qssq666.CoreLibrary0;import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.CheckSplitInfo;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.databinding.ActivityImportWordBinding;
import cn.qssq666.robot.event.WordEvent;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.EncodingDetect;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.SPUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class ImportWordActivity extends SuperActivity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_FILE = 2;
    private ActivityImportWordBinding binding;
    private AutoCompleteTextView evWordPath;
    private TextView tvLogView;
    private boolean mCanncel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_word);
        setSupportActionBar(binding.toolbar);
        binding.btnSubmit.setText("导入词库");
        binding.btnSubmit.setOnClickListener(this);
        binding.getRoot().findViewById(R.id.btn_tab_path).setOnClickListener(this);
        binding.btnSelectPath.setOnClickListener(this);
        binding.btnSmartReadFlag.setOnClickListener(this);
        evWordPath = ((AutoCompleteTextView) findViewById(R.id.ev_wordtext_path));

        evWordPath.setText(SPUtils.getString(this, AppConstants.CONFIG_WORD_IMPORT_PATH));
        binding.evSplitFlag.setText(SPUtils.getString(this, AppConstants.CONFIG_WORDS_SPLIT_FLAG));
        binding.evAskSplitFlag.setText(SPUtils.getString(this, AppConstants.CONFIG_ASK_AND_ANASWER_SPLIT_FLAG));
        evWordPath.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                File file = new File(s + "");
                SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_WORD_IMPORT_PATH, file.getAbsolutePath());
                updateFileExistFlag(file.getAbsoluteFile(), evWordPath);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        binding.evSplitFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_WORDS_SPLIT_FLAG,s+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        binding.evAskSplitFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_ASK_AND_ANASWER_SPLIT_FLAG, ""+s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tvLogView = ((TextView) binding.getRoot().findViewById(R.id.tv_log_view));


    }

    public static String formatVar(String str) {

        String current;
        current = "[rnrn]";
        if (str.contains(current)) {

            return str.replace(current, "\r\n\r\n");
        }
        current = "[rn]";
        if (str.contains(current)) {
            return str.replace(current, "\r\n");
        }
        current = "[n]";

        if (str.contains(current)) {
            return str.replace(current, "\n");
        }
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:


            {
                tvLogView.setText("");

                final String wordSplit = formatVar(binding.evSplitFlag.getText().toString());
                if (TextUtils.isEmpty(wordSplit)) {
                    AppContext.showToast("问答与问答之间分隔符未填写");
                    return;
                }

                final String askAndAnswerSplit = formatVar(binding.evAskSplitFlag.getText().toString());
                if (TextUtils.isEmpty(askAndAnswerSplit)) {
                    AppContext.showToast("问与答之间分隔符未填写");
                    return;
                }
                final File file = new File(evWordPath.getText().toString());
                if (!file.exists()) {
                    AppContext.showToast("无法自动补全文件夹,因为文件/文件夹" + file.getName() + "不存在");
                    return;
                } else {

                    final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this);
                    progressDialog.show();
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mCanncel = true;
                        }
                    });


                    new QssqTaskFix<Object, Object>(new QssqTaskFix.ICallBackImp() {

                        @Override
                        public void onSetQssqTask(QssqTaskFix taskFix) {
                            super.onSetQssqTask(taskFix);
                        }


                        @Override
                        public Object onRunBackgroundThread(Object[] params) {


                            List<ReplyWordBean> wordBeans = new ArrayList<>();
                            try {


                                String javaEncode = EncodingDetect.getJavaEncode(file.getAbsolutePath());
                                if (javaEncode != null) {
                                    javaEncode = "gbk";
                                }


                                BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(file), javaEncode));//此时获取到的bre就是整个文件的缓存流

                                String str;
                                boolean isMultiMode = (binding.evSplitFlag.getText().toString().equals("[rnrn]") && binding.evAskSplitFlag.getText().toString().equals("[rn]"))
                                        || (binding.evSplitFlag.getText().toString().equals("[nn]") && binding.evAskSplitFlag.getText().toString().equals("[n]"));


                                if (isMultiMode || binding.evSplitFlag.getText().toString().endsWith("n]")) {

                                    ReplyWordBean replyWordBean = null;
                                    int line = 1;
                                    while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环


                                    {


                                        if (isMultiMode) {
                                            if (TextUtils.isEmpty(str)) {

                                                replyWordBean = new ReplyWordBean();
                                                wordBeans.add(replyWordBean);

                                            } else {


                                                if (line == 1) {
                                                    replyWordBean = new ReplyWordBean();
                                                    wordBeans.add(replyWordBean);
                                                }

                                                if (TextUtils.isEmpty(replyWordBean.getAsk())) {
                                                    replyWordBean.setAsk(str);
                                                } else if (TextUtils.isEmpty(replyWordBean.getAnswer())) {
                                                    replyWordBean.setAnswer(str);
                                                } else {
                                                    LogUtil.writeLog("词库错误" + "忽略行" + line + ":" + str);
                                                }
                                            }
                                        } else if (binding.evSplitFlag.getText().toString().equals("[n]") || binding.evSplitFlag.getText().toString().equals("[rn]")) {
                                            replyWordBean = new ReplyWordBean();

                                            String split = formatVar(binding.evAskSplitFlag.getText().toString());
                                            if (TextUtils.isEmpty(str)) {
                                                LogUtil.writeLog("忽略行" + str);
                                                continue;
                                            }
                                            String[] words = str.split(split);
                                            if (words != null && words.length == 2) {

                                                replyWordBean.setAsk(words[0]);
                                                replyWordBean.setAnswer(words[1]);


                                            } else {
                                                if (words == null) {
                                                    throw new RuntimeException("无法读取行" + line + ":" + str + ",分割词条为空");
                                                } else {
                                                    throw new RuntimeException("无法读取行" + line + ":" + str + ",分割词条总数不匹配，期望为2，当前为" + words.length);
                                                }
                                            }


                                        }

                                        LogUtil.writeLog("read line " + line + ":" + str);
                                        line++;


                                        getQssqTask().publishProgressProxy("读入" + str);


                                        if (mCanncel) {
                                            return null;
                                        }
                                    }


                                } else {


                                    getQssqTask().publishProgressProxy("由于词库不是换行符分割,因此一次读入再进行分割,可能时间较长");
                                    String strs = FileUtils.readFileToString(file, javaEncode);


                                    String[] splitWord = strs.split(wordSplit);
                                    getQssqTask().publishProgressProxy("识别到词条总数" + splitWord + "个");
                                    for (int i = 0; i < splitWord.length; i++) {

                                        String currentWord = splitWord[i];
                                        getQssqTask().publishProgressProxy("处理第" + (1 + i) + "行\n" + currentWord + "");
                                        String[] askAndAswerSplit = currentWord.split(askAndAnswerSplit);

                                        if (askAndAswerSplit != null && askAndAswerSplit.length == 2) {
                                            ReplyWordBean replyWordBean = new ReplyWordBean();
                                            replyWordBean.setAsk(askAndAswerSplit[0]);
                                            replyWordBean.setAnswer(askAndAswerSplit[1]);
                                        } else {
                                            if (askAndAswerSplit == null) {
                                                throw new RuntimeException("无法读取行" + (1 + i) + ":" + currentWord + ",分割词条为空");
                                            } else {
                                                throw new RuntimeException("无法读取行" + (1 + i) + ":" + currentWord + ",分割词条总数不匹配，期望为2，当前为" + askAndAswerSplit.length);
                                            }
                                        }

                                    }
                                }


                            } catch (Exception e) {

                                return e;

                            }
                            StringBuilder sb = new StringBuilder();

                            if (wordBeans != null) {


                                getQssqTask().publishProgressProxy("开始插入数据库");

                                try {

                                    for (int i = 0; i < wordBeans.size(); i++) {

                                        ReplyWordBean wordBean = wordBeans.get(i);

                                        if(wordBean==null ||wordBean.getAnswer()==null || wordBean.getAsk()==null){
                                            sb.append("[忽略数据]第 "+(1+i)+"条数据->"+wordBean+"\n");
                                            continue;
                                        }
                                        ReplyWordBean dbBean = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumn(ReplyWordBean.class, FieldCns.ASK, wordBean.getAsk().toString());

                                        if (dbBean != null) {

                                            if (dbBean.getAnswer().equals(dbBean.getAnswer())) {
                                                getQssqTask().publishProgressProxy("第" + (1 + i) + "条数据已存在");
                                                sb.append("[已存在]问:" + wordBean.getAsk() + "对应的答案" + dbBean.getAnswer() + "已存在\n");

                                            } else {
                                                dbBean.setAnswer(wordBean.getAnswer() + "," + wordBean.getAnswer());
                                                long result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(dbBean);

                                                if (result > 0) {
                                                getQssqTask().publishProgressProxy("更新第" + (1 + i) + "条数据成功");
                                                    sb.append("[更新成功]问:" + wordBean.getAsk() + "已存在,更新答案为" + dbBean.getAnswer() + "\n");

                                                } else {

                                                getQssqTask().publishProgressProxy("更新第" + (1 + i) + "条数据失败");
                                                    sb.append("[更新失败]问:" + wordBean.getAsk() + "已存在,更新答案为" + dbBean.getAnswer() + "\n");
                                                }
                                            }

                                        } else {

                                            long result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).insert(wordBean);

                                            if (result > 0) {
                                            getQssqTask().publishProgressProxy("插入第" + (1 + i) + "条数据成功");
                                                sb.append("[插入成功]问:" + wordBean.getAsk() + ",答" + wordBean.getAnswer() + "已存在\n");

                                            } else {

                                            getQssqTask().publishProgressProxy("插入第" + (1 + i) + "条数据失败");
                                                sb.append("[插入失败]问:" + wordBean.getAsk() + ",答" + wordBean.getAnswer() + "已存在\n");
                                            }
                                        }


                                    }

                                    return sb.toString();
                                } catch (Exception e) {
                                    Log.e("插入任务", "插入数据失败", e);
                                    return e;
                                }


                            } else {
                                return "没有数据";
                            }


                        }

                        @Override
                        public void onRunFinish(Object o) {


                            if (o instanceof String) {
                                tvLogView.setText(o+"");


                                WordEvent wordEvent=new WordEvent();
                                wordEvent.setAll(true);
                                EventBus.getDefault().post(wordEvent);
                                DialogUtils.showDialog(ImportWordActivity.this, o + "");



                            }
                            if (o instanceof List) {

                                List<ReplyWordBean> wordBeans = (List<ReplyWordBean>) o;
                                for (ReplyWordBean wordBean : wordBeans) {
                                    LogUtil.writeLog("dump词库", wordBean + "");

                                }

                            } else if (o instanceof Exception) {
                                tvLogView.setText("执行错误"+Log.getStackTraceString((Throwable) o));
                                DialogUtils.showDialog(ImportWordActivity.this, "出现错误\n" + ((Exception) o).getMessage());
                            }
                            progressDialog.dismiss();

                        }


                        @Override
                        public void onProgressUpdate(Object o) {
                            progressDialog.setMessage(o + "");

                        }
                    }).execute();


                }

            }
            break;


            case R.id.btn_smart_read_flag:

            {


                final File file = new File(evWordPath.getText().toString());
                if (!file.exists()) {
                    AppContext.showToast("无法自动补全文件夹,因为文件/文件夹" + file.getName() + "不存在");
                    return;
                } else {


                    final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this);
                    progressDialog.show();
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mCanncel = true;
                        }
                    });


                    new QssqTask<>(new QssqTask.ICallBack<CheckSplitInfo>() {
                        @Override
                        public CheckSplitInfo onRunBackgroundThread() {
                            final CheckSplitInfo checkSplitInfo = new CheckSplitInfo();
                            int line = 1;

                            try {

                                //{"_id":{"$oid":"593a63cc38b976fd719e15fe"},"account":"994957859","info":"a860046034431553","count":0,"createdAt":{"$date":"2017-06-09T09:01:00.317Z"},"updatedAt":{"$date":"2017-06-09T09:01:00.317Z"}}
                                // {"_id":{"$oid":"593a63cc45b2d9b83c3ba0f2"},"account":"994957859","info":"a860046034431553","count":0,"createdAt":{"$date":"2017-06-09T09:01:00.726Z"},"updatedAt":{"$date":"2017-06-09T09:01:00.726Z"}}


                                String javaEncode = EncodingDetect.getJavaEncode(file.getAbsolutePath());
                                if (javaEncode != null) {
                                    javaEncode = "gbk";
                                }
                                BufferedReader breFirst = new BufferedReader(new InputStreamReader(new FileInputStream(file), javaEncode));//此时获取到的bre就是整个文件的缓存流

                                long readLength = file.length() > 6000 ? 6000 : file.length();
                                char[] tt = new char[(int) readLength];
                                int read = breFirst.read(tt, 0, (int) readLength);
                                String readTmpStr = "";
                                if (read != -1) {
                                    readTmpStr = String.valueOf(tt);

                                }

                                LogUtil.writeLog("读取临时字符串" + readTmpStr);


                                BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(file), javaEncode));
                                //new BufferedReader(new FileInputStream(file), "UTF-8"));//此时获取到的bre就是整个文件的缓存流
//                                BufferedReader bre = new BufferedReader(new FileReader(file));//此时获取到的bre就是整个文件的缓存流

                                String str;


                                boolean lastLineIsEmpty = false;
                                while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
                                {


                                    if (mCanncel) {
                                        break;
                                    }

                                    LogUtil.writeLog("line" + line + ":" + str);

//                                    FileUtils.read


                                    if (lastLineIsEmpty) {


                                        {
                                            int rnIndex = readTmpStr.indexOf("\r\n\r\n");

                                            if (rnIndex == -1) {

                                                rnIndex = readTmpStr.indexOf("\r\n");
                                                if (rnIndex != -1) {
                                                    checkSplitInfo.wordSplit = "[rn]";

                                                }

                                            } else {

                                                checkSplitInfo.wordSplit = "[rnrn]";
                                            }


                                        }


                                        {


                                            int rnIndex = readTmpStr.indexOf("\r\n");

                                            if (rnIndex == -1) {

                                                rnIndex = readTmpStr.indexOf("\n");
                                                if (rnIndex != -1) {
                                                    checkSplitInfo.askanswerSplit = "[n]";

                                                }

                                            } else {

                                                checkSplitInfo.askanswerSplit = "[rn]";
                                            }
                                            if (checkSplitInfo.wordSplit != null) {

                                                break;
                                            }
                                        }


                                    } else {

                                        if (str.indexOf(",") != -1) {
                                            checkSplitInfo.askanswerSplit = ",";
                                        } else if (str.indexOf(",") != -1) {
                                            checkSplitInfo.askanswerSplit = "|";
                                        } else if (str.indexOf("-") != -1) {
                                            checkSplitInfo.askanswerSplit = "-";
                                        } else {


                                        }


                                        int i = str.indexOf("\n");
                                        if (i != -1) {

                                            if (i < readTmpStr.indexOf(str)) {

                                                checkSplitInfo.wordSplit = "[n]";
                                            }


                                        }

                                        i = str.indexOf("\r\n");
                                        if (i != -1) {

                                            if (i < readTmpStr.indexOf(str)) {

                                                checkSplitInfo.wordSplit = "[rn]";
                                            }


                                        }

                                    }


                                    if (TextUtils.isEmpty(str)) {
                                        lastLineIsEmpty = true;
                                    }

                                    line++;

                                    if (line > 8) {
                                        break;
                                    }
                                }
                                ;
                            } catch (IOException e) {
                            }
                            return checkSplitInfo;


                        }

                        @Override
                        public void onRunFinish(CheckSplitInfo o) {

                            progressDialog.dismiss();
                            if (o.wordSplit == null) {
                                AppContext.showToast("无法识别词条分隔符");
                            } else if (o.askanswerSplit == null) {
                                AppContext.showToast("无法识别问题与答案分隔符");
                            } else {
                                binding.evSplitFlag.setText(o.wordSplit);
                                binding.evAskSplitFlag.setText(o.askanswerSplit);

                                AppContext.showToast("检测完成,问答与问答分隔符" + o.wordSplit + "\n问与答之间分隔符:" + o.askanswerSplit);
                            }


                        }

                    }).execute();


                }

            }
            break;


            case R.id.btn_tab_path:

            {

                File file = new File(evWordPath.getText().toString());
                if (!file.exists()) {
                    AppContext.showToast("无法自动补全文件夹,因为文件/文件夹" + file.getName() + "不存在");
                } else {
                    File parentFile = file.isFile() ? file.getParentFile() : file;

                    File[] list = parentFile.listFiles();
                    if (list != null && list.length > 0) {
                        if (lastTabPosition < list.length) {


                        } else {
                            if (list.length == 1) {
                                evWordPath.setError("只有一个文件");
                            }
                            lastTabPosition = 0;
                        }
                    } else {
                        AppContext.showToast("目录" + parentFile.getName() + "没有其他文件了");
                        ;
                        return;
                    }

                    evWordPath.setText(list[lastTabPosition].getAbsolutePath());
                    lastTabPosition++;

                }
            }
            break;
            case R.id.btn_select_path:
                AppUtils.chooseFile(this, REQUEST_CHOOSE_FILE);
                break;

        }

    }

    private static int lastTabPosition = 0;

    protected static void updateFileExistFlag(File file, AutoCompleteTextView completeTextView) {

        if (file != null && file.exists()) {
            if (file.isFile()) {
                completeTextView.setError(null);

            } else {
                completeTextView.setError("不是文件");

            }
        } else {
            completeTextView.setError("路径错误");
        }
    }


    public static String parsePath(Context context, Uri uri) {
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
//        }else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    String string = cursor.getString(column_index);
                    cursor.close();
                    return string;
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                if (uri == null) {
                    AppContext.showToast("无法获取选择的文件,请联系作者");
                    return;
                }

                String path = parsePath(this, uri);
                if (path == null) {
                    AppContext.showToast("获取的数据有问题:Uri: " + uri + "");
                    return;
                }


                lastTabPosition = 0;
                evWordPath.setText(path);

            } else {
            }


            super.onActivityResult(requestCode, resultCode, data);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_import_help, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help_install_plugin) {
            DialogUtils.showDialog(this,"目前分隔符栏目中填写换行符 \\r\\n应该用[rn]表示,而\\n则用[n]表示,建议使用 智能识别功能,对于网络上不支持识别的的词库格式,请记得联系我处理哦!\n本地词库回复语会自动替换某些变量,用户可以用配置添加变量的方式添加一些变量,以便词库更智能亲民,比如问 我的qq是多少 回答你的qq为 $u ,另外机器人名字默认变量为$robotname 用户昵称为$username 目前昵称可能存在一些问题,暂时还没时间去处理排查问题。" );
            return true;
        }else if(id==R.id.action_suggest) {
            DialogUtils.showDialog(this,"目前词库是单线程执行的,可能过多词库会导致出现问题,之前也是为了方便点测试直接处理返回结果未做优化,目前正在考虑之中。" );
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
