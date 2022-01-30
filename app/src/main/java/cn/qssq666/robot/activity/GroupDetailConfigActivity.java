package cn.qssq666.robot.activity;

import cn.qssq666.CoreLibrary0;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.databinding.ActivityGroupDetailBinding;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.event.GroupDetailEvent;
import cn.qssq666.robot.handler.GroupConfigHandler;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;

/**
 * Created by qssq on 2017/12/20 qssq666@foxmail.com
 */

public class GroupDetailConfigActivity extends SuperActivity implements View.OnClickListener {

    private static final String TAG = "GroupDetailConfig";
    private ActivityGroupDetailBinding binding;
    private GroupConfigHandler configHandler;
    private String mTipDefaultValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTipDefaultValue = "";
        setTitle("群管详情");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_detail);
        EventBus.getDefault().register(this);
        binding.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.copy(AppContext.getInstance(), "" + configHandler.getWhiteNameBean().getAccount());
                Toast.makeText(GroupDetailConfigActivity.this, "已复制群号", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtils.showEditDialog(GroupDetailConfigActivity.this, "请输入昵称进行正则测试", "", mTipDefaultValue, new INotify<String>() {
                    @Override
                    public void onNotify(String param) {

                        mTipDefaultValue = param;
                        try {
                            String text = configHandler.getWhiteNameBean().getGroupnickanmekeyword();
                            boolean matches = param.matches(text);
                            Toast.makeText(GroupDetailConfigActivity.this, "是否正则匹配:" + matches, Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(GroupDetailConfigActivity.this, "正则 表达式有错误" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


//        EventBus.getDefault().register(this);


    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onReceive(GroupDetailEvent event) {
        GroupWhiteNameBean currentNameBean = event.getBean();
        configHandler = new GroupConfigHandler();
        binding.setHandler(configHandler);
/*        ClassLoader classLoader = src.getClass().getClassLoader();
        Class aClass = src.getClass();// cn.qssq666.robot.bean.GroupWhiteNameBean is not an interface
        Class[] interfaces = aClass.getInterfaces();
        Object o = Proxy.newProxyInstance(classLoader, interfaces, new ConfigProxy(src));
        GroupWhiteNameBean bean = (GroupWhiteNameBean) o;*/
        configHandler.setWhiteNameBean(currentNameBean);
        View viewBtnFetchGroupName = binding.getRoot().findViewById(R.id.btn_fetch_current_group_name);
        viewBtnFetchGroupName.setVisibility(RemoteService.isIsInit() ? View.VISIBLE : View.GONE);
        viewBtnFetchGroupName.setOnClickListener(this);


        binding.btnFetchCurrentGroupAdmins.setOnClickListener(this);
        binding.btnAdminsHelp.setOnClickListener(this);
        binding.setModel(currentNameBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (configHandler == null || configHandler.getWhiteNameBean() == null) {
            return;
        }
        Log.w(TAG, "MODIFYRESULT:" + configHandler.getWhiteNameBean());
        if (configHandler.isHasChange()) {
            DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).update(configHandler.getWhiteNameBean());
            //LogUtil.writeLog("改变...");
            if (configHandler.getWhiteNameBean().isSelfcmdnotneedaite()&&AppContext.isVip()) {
                AppContext.showToast("普通群员所有指令静默功能只有VIP才能开启!");
                configHandler.getWhiteNameBean().setSelfcmdnotneedaite(false);
            }
            if(configHandler.getWhiteNameBean().isRevokemsg()){

            }

            AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
            event.setBean(configHandler.getWhiteNameBean());
            event.setType(AccountType.TYPE_QQGROUP_WHITE_NAME);
            event.setPosition(0);
            EventBus.getDefault().post(event);


            AppContext.showToast("保存成功");
        } else {
            //LogUtil.writeLog("没有改变");
        }

    }

    static {

        LogUtil.importPackage();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fetch_current_group_name: {

                if (!RemoteService.isIsInit()) {
                    Toast.makeText(this, "需要绑定服务才能使用!", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    String s = RemoteService.queryGroupName(configHandler.getWhiteNameBean().getAccount());
                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(this, "获取失败!", Toast.LENGTH_SHORT).show();
                    } else {
                        //ev_group_name
                        binding.evGroupName.setText(s);
                    }
                }

            }
            break;

            case R.id.btn_admins_help: {

                DialogUtils.showDialog(this, "绑定机器人服务将具备更多功能,如任意昵称查询真实管理员查询,但是也意味着会影响执行效率,会导致机器人经常无响应，卡顿，因此每次收到消息不去检测是否是真实管理员，所以真实管理员依然会受检测规则限制 测规则包含如:(发言违规/敏感词违规/群名片违规/发红包 等)，因为每次都会触发,因此采用此办法尽量避免查询过多数据。 。");
//                DialogUtils.showDialog(this, "为了提升执行效率,因此只有超级管理员以及在本界面配置得管理员可以不受检测规则限制。检测规则包含如:(发言违规/敏感词违规/群名片违规/发红包 等)，因为每次都会触发,因此采用此办法尽量避免查询过多数据。");
            }

            break;

            case R.id.btn_fetch_current_group_admins: {
                {

                    if (!RemoteService.isIsInit()) {
                        Toast.makeText(this, "需要绑定服务才能使用,请打开Q++勾选绑定机器人服务并勾选死亡自动唤醒。!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        String owneruin = RemoteService.queryGroupField(configHandler.getWhiteNameBean().getAccount(), "troopowneruin");
                        String admins = RemoteService.queryGroupField(configHandler.getWhiteNameBean().getAccount(), "Administrator");

                        if (admins != null) {
                            admins = admins.replace("|", ",");

                        }
                        String result = owneruin == null && admins == null ? null : owneruin + "," + admins;
                        if (TextUtils.isEmpty(result)) {
                            Toast.makeText(this, "获取失败!", Toast.LENGTH_SHORT).show();
                        } else {
                            AppUtils.copy(this, "" + result);
                            Toast.makeText(this, "已拷贝到剪辑版,情选择输入框然后进行粘贴!", Toast.LENGTH_SHORT).show();
                        }
                        //ev_group_name
                    }
                }


            }


            break;
        }
    }
}
