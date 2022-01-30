package cn.qssq666.robot.activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.adapter.DefaultAdapter;
import cn.qssq666.robot.adapter.RedPacketAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.databinding.ActivityWordManagerBinding;
import cn.qssq666.robot.event.RedPacketEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.ui.ListDividerItemDecoration;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.SPUtils;

import static cn.qssq666.robot.R.id.action_month_record;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class RedPacketRecordActivity extends SuperActivity {

    private ActivityWordManagerBinding binding;
    private String TAG = "RedPacketRecordActivity";

    public RedPacketAdapter getAdapter() {
        return adapter;
    }

    private RedPacketAdapter adapter;

    @Subscribe
    public void onReceveData(RedPacketEvent event) {
        if (event.isEdit()) {
            adapter.getList().set(event.getPosition(), event.getBean());

            adapter.notifyItemChanged(event.getPosition());
        } else {
            List<RedPacketBean> list = adapter.getList();
            if (list == null) {
                list = new ArrayList<>();
                list.add(event.getBean());
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            } else {
                list.add(0, event.getBean());
                adapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_word_manager);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("关键词回复");
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        ;
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.addItemDecoration(new ListDividerItemDecoration(this, R.drawable.divider));
        adapter = new RedPacketAdapter();
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener<RedPacketBean>() {
            @Override
            public void onItemClick(RedPacketBean model, View view, int position) {
                AppUtils.copy(view.getContext(), JSON.toJSONString(model));
                AppContext.showToast(model.getMessage());
            }
        });
        adapter.setOnItemLongClickListener(new DefaultAdapter.OnItemLongClickListener<RedPacketBean>() {
            @Override
            public boolean onItemClick(RedPacketBean model, View view, final int position) {
                DialogUtils.showMenuDialog(RedPacketRecordActivity.this, "操作", Cns.OPERA_MENU_EDIT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                AppContext.showToast("不支持的操作");
                            }
                            break;
                            case 1: {
                                RedPacketBean bean = adapter.getList().get(position);
                                int i = DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).deleteById(RedPacketBean.class, bean.getId());
                                if (i <= 0) {
                                    DialogUtils.showDialog(RedPacketRecordActivity.this, "删除失败");
                                } else {
                                    adapter.getList().remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                            }
                            break;
                        }
                    }
                });
                return true;
            }
        });

        binding.swiperefreshlayout.setRefreshing(true);
        binding.swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                queryData();
            }
        });

        queryData();

    }

    public void queryData() {
      /*  new QssqTask<List<RedPacketBean>>(new QssqTask.ICallBack<List<RedPacketBean>>() {
            @Override
            public List<RedPacketBean> onRunBackgroundThread() {
                List<RedPacketBean> list = DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).queryAllIsDesc(RedPacketBean.class, true, FieldValue.ID);
                return list;
            }

            @Override
            public void onRunFinish(List<RedPacketBean> list) {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                binding.swiperefreshlayout.setRefreshing(false);
            }
        }).execute();*/


        Calendar candenCar = getTodayCandenCar();
        candenCar.add(Calendar.DAY_OF_MONTH, -29);//包括自身一天
        //LogUtil.writeLog(String.format("查询%s以及之后的红包流水", DateUtils.getTime(candenCar.getTimeInMillis())));

        binding.swiperefreshlayout.setRefreshing(true);
        executeQuery("select * from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + " where createdAt>=" + candenCar.getTimeInMillis() + " order by createdAt desc");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_redpacket, menu);
        return true;
    }


    public Calendar getTodayCandenCar() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTimeInMillis(new Date().getTime());
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help_install_plugin) {
            DialogUtils.showDialog(this, "欲使用红包记录,必先下载情迁内置1.2.14以及以上,若要执行sql增删改查,你可能需要这些信息,如 字段支持下面这些" + JSON.toJSONString(new RedPacketBean()) + ",表名为:" + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class));

            return true;
        } else if (id == action_month_record) {
            queryData();

        } else if (id == R.id.action_total_record) {
            executeQuery("select * from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + "");
//
        } else if (id == R.id.action_total_money) {
            executeQuery("select sum(money) as money from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + " where result=200");
        } else if (id == R.id.action_weekday_record) {
            Calendar candenCar = getTodayCandenCar();
            candenCar.add(Calendar.DAY_OF_WEEK, -6);//包括自身一天
            //LogUtil.writeLog("时间:" + DateUtils.getTime(candenCar.getTimeInMillis()));
            executeQuery("select * from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + " where createdAt>=" + candenCar.getTimeInMillis() + " and result=200");


        } else if (id == R.id.action_today_record) {


            Calendar todayCandenCar = getTodayCandenCar();
            //LogUtil.writeLog("时间:" + DateUtils.getTime(todayCandenCar.getTimeInMillis()));
            executeQuery("select * from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + " where createdAt>=" + todayCandenCar.getTimeInMillis() + " and result=200");


        } else if (id == R.id.action_total_succ_count) {

            executeQuery("select count(*) as qq, sum(money) as money,result  from " + DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).getTableName(RedPacketBean.class) + " where result=200");
        } else if (id == R.id.action_delete) {
            DialogUtils.showConfirmDialog(this, "你真的不需要这些红包数据了吗?", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).deleteAll(RedPacketBean.class);
                    queryData();
                }
            });
            return true;
        } else if (id == R.id.action_query) {
            String defaultSql = SPUtils.getValue(RedPacketRecordActivity.this, AppConstants.CONFIG_LAST_EXECUTE_QUERY_SQL, AppConstants.CONFIG_LAST_EXECUTE_QUERY_SQL_DEFAULT_VALUE);
            DialogUtils.showEditDialog(this, "请输入要执行的查询sql执行结果将在列表展示", "", defaultSql, new INotify<String>() {
                @Override
                public void onNotify(final String param) {


                    executeQuery(param);


                }
            });

        } else if (id == R.id.action_d_u_execute) {

            String defaultSql = SPUtils.getValue(RedPacketRecordActivity.this, AppConstants.CONFIG_LAST_EXECUTE_SQL_COMMEND, AppConstants.CONFIG_LAST_EXECUTE_SQL_DEFAULT_VALUE);
            DialogUtils.showEditDialog(this, "请输入要执行的sql命令,不懂的朋友自己百度!", "", defaultSql, new INotify<String>() {
                @Override
                public void onNotify(String param) {
                    SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_LAST_EXECUTE_SQL_COMMEND, param);
                    try {

                        DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).execSQL(param);
                        AppContext.showToast("执行成功!请下拉刷新测试!");
                    } catch (Exception e) {
                        AppContext.showToast("执行出错" + e.toString());
                    }


                }
            });


        } else if (id == R.id.action_d_u_timestamp) {

            String value = SPUtils.getValue(RedPacketRecordActivity.this, AppConstants.CONFIG_LAST_EXECUTE_TIMESTAMP, AppConstants.CONFIG_LAST_EXECUTE_TIMESTAMP_VALUE);
            DialogUtils.showEditDialog(this, "输入2017-9-23 17:37:50格式时间将获取到查询红包间隔时间戳将生成查询大于此时间的时间戳并生成sql查询语句到剪辑版", "", value, new INotify<String>() {
                @Override
                public void onNotify(String param) {
                    try {
                        Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(param);// ignore_include
                        String sql = "select * from redpacket where createdAt>" + parse.getTime();
                        AppUtils.copy(RedPacketRecordActivity.this, sql);
                        AppContext.showToast("已生成查询大于此时间的红包记录sql语句,请尝试粘贴执行查询!");

                        SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_LAST_EXECUTE_TIMESTAMP, param);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        AppContext.showToast("输入格式有误!" + e.toString());
                    }


                }
            });


        }


        return super.onOptionsItemSelected(item);
    }

    public void executeQuery(String param) {
        if (param.startsWith("select") && !param.contains("order") && param.contains("from")) {
            param = param + " order by createdAt desc";

        }
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "SQL:" + param);
        }
        SPUtils.setValue(AppContext.getInstance(), AppConstants.CONFIG_LAST_EXECUTE_QUERY_SQL, param);

        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(RedPacketRecordActivity.this);
        progressDialog.show();
        progressDialog.setMessage("正在执行查询");
        final String finalParam = param;
        new QssqTask<Object>(new QssqTask.ICallBack() {
            @Override
            public Object onRunBackgroundThread() {
                try {
                    return DBHelper.getRedPacketBUtil(AppContext.getDbUtils()).rawQuery(RedPacketBean.class, finalParam);

                } catch (Exception e) {

                    return e;
                }
            }

            @Override
            public void onRunFinish(Object list) {
                progressDialog.dismiss();
                if (list instanceof Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, list.toString());
                    }
                    DialogUtils.showDialog(RedPacketRecordActivity.this, "执行出错" + list.toString());
                } else {
                    adapter.setList((List<RedPacketBean>) list);
                    adapter.notifyDataSetChanged();

                }
                binding.swiperefreshlayout.setRefreshing(false);
            }
        }).execute();


    }

    static {
        LogUtil.importPackage();

    }
}
