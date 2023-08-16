package cn.qssq666.robot.activity.msg;

import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.activity.DevToolActivity;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.MsgTypeConstant;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.RobotUtil;

public class RecentMsgAct extends BaseMessageManagerActivity<MsgItem> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.recent_msg);
        if(AppUtils.getConfigSharePreferences(getApplicationContext()).getInt(Cns.SP_RECENT_MSG_RECORD_COUNT, 0)<=0){
            Toast.makeText(this, "你没有开启记录历史消息，请进入杂项设置设置记录历史消息总数大于0", Toast.LENGTH_SHORT).show();
        }
        

        AppContext.getContext().getContentResolver().registerContentObserver(Uri.parse(MsgTypeConstant.AUTHORITY_CONTENT), true, myobserver);
    }

    ContentObserver myobserver = new ContentObserver(AppContext.getHandler()) {
        @Override
        public boolean deliverSelfNotifications() {
//            Toast.makeText(RecentMsgAct.this, "deliverSelfNotifications", Toast.LENGTH_SHORT).show();
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //LogUtil.writeLog("测试主界面:onReceive " + uri);
            MsgItem msgItem = RobotUtil.uriToMsgItem(uri);
            LogUtil.writeLog("[最近消息]添加新消息" + msgItem);
            if (adapter.getItemCount() == 0) {
                ArrayList<MsgItem> list = new ArrayList<>();
                list.add(msgItem);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                binding.recyclerview.scrollToPosition(0);
            } else {
                List<MsgItem> list = adapter.getList();
                list.add(0, msgItem);
                adapter.notifyItemInserted(0);
                binding.recyclerview.scrollToPosition(0);
            }

        }
    };

    @Override
    protected boolean needIntercepClick(MsgItem model, int position) {
        Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected boolean needInterceptLongClick(MsgItem model, int position) {
        DialogUtils.showDialog(this,model.toString());
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppContext.getContext().getContentResolver().unregisterContentObserver(myobserver);
    }

    @Override
    protected List<MsgItem> queryDataFrom() {
        List<MsgItem> recentMsgs = RobotContentProvider.getInstance().getRecentMsgs();
        return recentMsgs;
    }

    @Override
    protected int getAdapterType() {
        return 0;
    }

}
