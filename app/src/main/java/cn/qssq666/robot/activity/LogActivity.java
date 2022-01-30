package cn.qssq666.robot.activity;

import android.app.PictureInPictureParams;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.qssq666.robot.R;
import cn.qssq666.robot.adapter.LogAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.databinding.ActivityLogBinding;
import cn.qssq666.robot.event.LogEvent;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;

/**
 * Created by luozheng on 2017/3/12.  qssq.space
 */

public class LogActivity extends SuperActivity implements SearchView.OnQueryTextListener {
    private boolean mPress;
    private boolean mPause;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LogActivity.this.isFinishing()) {
                return;
            }

            if (mPause) {
                return;
            }
            String log = intent.getStringExtra("log");

            if (_filterKey != null && !log.toUpperCase().contains(_filterKey.toUpperCase())) {
                return;
            }

            if (adapter.getType() != LogAdapter.TYPE_Verbose) {
                int logType = LogAdapter.getTypeByStr(log);
                if (logType != adapter.getType()) {//不是全部也不是指定的类型，就返回
                    return;
                }

            }

            if (adapter.getList() == null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(log);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                return;
            }

            if (adapter.getList().size() > 5950) {
                adapter.getList().remove(adapter.getList().size() - 1);
                adapter.notifyItemRemoved(adapter.getList().size());


            } else {


            }
//            adapter.setH
            adapter.getList().add(0, log);
//            adapter.
            adapter.notifyItemInserted(0);


            adapter.notifyDataSetChanged();


            if (!mPress) {
                recyclerView.smoothScrollToPosition(0);

            }


        }
    };
    private LogAdapter adapter;
    private RecyclerView recyclerView;
    private ActivityLogBinding binding;
    private SearchView searchViewContent;
    private SearchView searchViewTag;
    private String _filterKey;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onUserLeaveHint() {
//        if (mWantEnterPic) {
//            enterPictureInPictureMode();
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("情迁聊天机器人实时日志");
//         DataBindingUtil.setContentView(this,R.layout.activity_log);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log);

        EventBus.getDefault().register(this);
        LogUtil.startRecordLog();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new LogAdapter();
        recyclerView.setAdapter(adapter);

        //         LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(RobotContentProvider.getInstance().getProxyContext());
        //                        Intent intent = new Intent();
        //                        intent.putExtra("log", current);
        //                        intent.setAction(""+INTENT_RECEIVER_LOG);
        //                        localBroadcastManager.sendBroadcast(intent);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mPress = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPress = true;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mPress = false;
                        break;
                }
                return false;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LogUtil.INTENT_RECEIVER_LOG));

        setSupportActionBar(binding.toolbar);
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(AppConstants.INTENT_FORM_CODE_VIEW, false)) {
//            if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {//跳转的时候就判断了，我的荣耀8 8.0支持，7.0的小米只支持分屏。
            PictureInPictureParams pic = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    /*
                       // 设置宽高比例值，第一个参数表示分子，第二个参数表示分母
        // 下面的10/5=2，表示画中画窗口的宽度是高度的两倍 10 5

                     */
                //10分之7 左边是分子

                pic = new PictureInPictureParams.Builder().setAspectRatio(new Rational(90, 100)).build();
                enterPictureInPictureMode(pic);
                getSupportActionBar().hide();//这代码不能再回调里面写，台区怪了
//get
//                LogActivity.this.setSupportActionBar();
            }
//            }
        } else {

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLog(LogEvent logEvent) {
//        tvLog.setText(getSimpleTime(logEvent.getTime()) + ":" + logEvent.getLog() + "\n\n" + tvLog.getText());
    }

    private String getTime(long time) {
        return new SimpleDateFormat("mm-dd HH:mm:ss").format(new Date(time));
    }

    private String getSimpleTime(long time) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(time));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        LogUtil.stopRecordLog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log, menu);

//        updateMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchViewContent = (SearchView) menu.findItem(R.id.action_search_content).getActionView();
        searchViewContent.setQueryHint("过滤关键词 *=所有]");


        try {
            Field mSearchButton = searchViewContent.getClass().getDeclaredField("mSearchButton");
            mSearchButton.setAccessible(true);
            ImageView iv = (ImageView) mSearchButton.get(searchViewContent);
            iv.setImageResource(R.drawable.ic_menu_search);

        } catch (Exception e) {
            e.printStackTrace();
        }

        searchViewContent.setOnQueryTextListener(this);
        searchViewContent.setSubmitButtonEnabled(true);

        searchViewTag = (SearchView) menu.findItem(R.id.action_search_tag).getActionView();
        searchViewTag.setQueryHint("过滤标签 *=取消");
        searchViewTag.setOnQueryTextListener(this);
        searchViewTag.setSubmitButtonEnabled(true);//打勾
        try {
            Field mSearchButton = searchViewTag.getClass().getDeclaredField("mSearchButton");
            mSearchButton.setAccessible(true);
            ImageView iv = (ImageView) mSearchButton.get(searchViewTag);
            iv.setImageResource(R.drawable.ic_menu_tag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchViewContent.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchViewTag.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItem item = menu.findItem(R.id.action_pause);
        if (mPause) {
            item.setTitle("继续");
            item.setIcon(R.drawable.ic_menu_continue);
        } else {
            if (mPause) {
                item.setTitle("暂停");
                item.setIcon(R.drawable.ic_menu_pause);
            }
        }

        MenuItem itemPicInPic = menu.findItem(R.id.action_switch_pic_center_pic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!this.isInPictureInPictureMode() && this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {

                itemPicInPic.setVisible(true);
            } else {
                itemPicInPic.setVisible(false);

            }
        } else {
            itemPicInPic.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.group_menu_item_info:
                adapter.setLogType(LogAdapter.TYPE_Verbose);
                break;
            case R.id.group_menu_item_debug:
                adapter.setLogType(LogAdapter.TYPE_DEBUG);
                break;
            case R.id.group_menu_item_all:
                adapter.setLogType(LogAdapter.TYPE_UNKNOWN);
                break;
            case R.id.group_menu_item_error:
                adapter.setLogType(LogAdapter.TYPE_ERROR);
                break;
            case R.id.group_menu_item_wran:
                adapter.setLogType(LogAdapter.TYPE_WARN);
                break;
            case R.id.action_pause:
                mPause = !mPause;
                if (mPause) {
                    item.setTitle("继续");
                    item.setIcon(R.drawable.ic_menu_continue);
                } else {
                    item.setTitle("暂停");
                    item.setIcon(R.drawable.ic_menu_pause);
                }
                return true;
            case R.id.action_info:
                StringBuffer sb = new StringBuffer();

                sb.append("当过滤的标签:" + LogUtil.filterTag + "\n");
                sb.append("当过滤的关键词:" + LogUtil.filterKey + "\n");
                sb.append("当前过滤的日志级别:" + adapter.getTypeStr() + "\n");
                sb.append("注意事项:\n");
                sb.append("1.如果设置过滤了标签,就算之后删掉了标签也不会再继续收到之前的标签信息\n");
                sb.append("2.如果设置过滤了关键词,就算之后删掉了关键词也不会再继续收到之前的关键词信息\n");
                sb.append("3.从非编辑代码界面进入,日志的标签和关键词过滤方式不会应该界面的关闭而被还原!\n");
                sb.append("3.设置显示级别不会删除之前存在的日志，可以通过调节显示级别还原。\n");
                sb.append("4.从LUA插件开发工具进入默认会设置为标签为LUAEngine,方便开发者调试查看日志!\n");
                sb.append("5.如果您的手机支持分屏或者支持画中画，在开发者工具控制台或者在代码编辑区域调试日志更方便哦!\n");
                DialogUtils.showDialog(this, sb.toString());
                return true;

            default:
            case R.id.action_switch_pic_center_pic:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    PictureInPictureParams pic = new PictureInPictureParams.Builder().setAspectRatio(new Rational(90, 100)).build();
                    this.enterPictureInPictureMode(pic);
                }
                return true;
        }

        item.setChecked(true);

        return false;

    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {

   /*     ObjectAnimator animator = ObjectAnimator.ofFloat(secondNavigationBar,
                View.TRANSLATION_Y, -secondNavigationBarHeight, 0);
        animator.setDuration(ANIMATOR_SPEED);
        animator.start();*/
        if (isInPictureInPictureMode) {
//            this.setSupportActionBar(null);
            setSupportActionBar(null);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar == null) {
                return;
            }
            if (supportActionBar.isShowing()) {
                AppContext.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogActivity.this.setSupportActionBar(binding.toolbar);
                        getSupportActionBar().hide();//走这里了。

                    }
                }, 3000);

            }
        } else {

            AppContext.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogActivity.this.setSupportActionBar(binding.toolbar);
                    getSupportActionBar().show();

                }
            }, 3000);
        }

        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    //todo 明天完善日志级别的过滤。。
    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.setList(new ArrayList<String>());
        adapter.notifyDataSetChanged();
        LogUtil.stopRecordLog();
        if (!searchViewContent.isIconified()) {
            _filterKey = query.equals("*") ? null : query;
//            searchViewContent.setQuery("", false);

        } else if (searchViewTag.isIconified() == false) {
//            searchViewTag.setQuery("", false);
            LogUtil.filterTag = query.equals("*") ? null : query;
        } else {
            Toast.makeText(this, "未知状态!", Toast.LENGTH_SHORT).show();
        }
        LogUtil.startRecordLog();
        return true;
//        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void onBackPressed() {
        if (searchViewTag != null && !searchViewTag.isIconified()) {
            searchViewTag.onActionViewCollapsed();
        } else if (searchViewContent != null && !searchViewContent.isIconified()) {
            searchViewContent.onActionViewCollapsed();
        } else {
            super.onBackPressed();

        }

    }
}
