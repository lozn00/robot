package cn.qssq666.robot.activity.msg;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.activity.SuperActivity;
import cn.qssq666.robot.adapter.DefaultAdapter;
import cn.qssq666.robot.adapter.MsgAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.databinding.ListActivityBinding;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.ui.ListDividerItemDecoration;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;


public abstract class BaseMessageManagerActivity<T extends MsgItem> extends SuperActivity {

    protected ListActivityBinding binding;

    public MsgAdapter<T> getAdapter() {
        return adapter;
    }

    protected MsgAdapter<T> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.list_activity);
        setSupportActionBar(binding.toolbar);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ;
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.addItemDecoration(new ListDividerItemDecoration(this, R.drawable.divider));
        adapter = new MsgAdapter<>();
        adapter.setType(getAdapterType());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new DefaultAdapter.OnItemLongClickListener<T>() {
            @Override
            public boolean onItemClick(T model, View view, final int itemPosition) {
                if (needInterceptLongClick(model, itemPosition)) {
                    return true;
                }

                DialogUtils.showMenuDialog(BaseMessageManagerActivity.this, "操作", getClickMenu(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int menuWhich) {
                        onLongClickItem(itemPosition, menuWhich);
                    }
                });
                return true;
            }
        });

        adapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(T model, View view, final int position) {
                if (needIntercepClick(model, position)) {
                    return ;
                }
                DialogUtils.showMenuDialog(BaseMessageManagerActivity.this, "操作", getLongPressMenu(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onClickItem(position, which);
                    }
                });

            }
        });
        binding.swiperefreshlayout.setRefreshing(true);
        binding.swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doOnRefresh();

            }
        });

        queryData();

    }

    protected boolean needIntercepClick(T model, int position) {
        return false;
    }

    protected void onClickItem(int itemPosition, int menuWhich) {
    }


    protected void onLongClickItem(int position, int which) {
    }


    protected boolean needInterceptLongClick(T model, int position) {
        return false;
    }

    protected void doOnRefresh() {
        queryData();
    }


    public void queryData() {
        new QssqTask<List<T>>(new QssqTask.ICallBack<List<T>>() {
            @Override
            public List<T> onRunBackgroundThread() {
                return queryDataFrom();
            }

            @Override
            public void onRunFinish(List<T> list) {
                doOnQueryFinish(list);
            }
        }).execute();
    }

    protected abstract List<T> queryDataFrom();

    protected void doOnQueryFinish(List<T> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        binding.swiperefreshlayout.setRefreshing(false);
        onQueryFinish();
    }

    protected void onQueryFinish() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            onAddItemClick();
            return true;
        } else if (id == R.id.action_delete_all) {
            DialogUtils.showConfirmDialog(this, "是否删除?(没有后悔药哦)", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {

                    onDeleteAllClick();
                }
            });
            return true;
        } else if (id == R.id.action_help) {
            return onClickHelpMenu(item);

        } else {
            return onClickMenu(item, id);
        }

    }

    protected boolean onClickHelpMenu(MenuItem item) {
        Toast.makeText(this, "暂时没有添加帮助信息!", Toast.LENGTH_SHORT).show();
        return false;
    }

    protected boolean onClickMenu(MenuItem item, int id) {
        return false;
    }

    void onDeleteAllClick() {
/*        int result = deleteAllFromDb();

        Toast.makeText(this, "删除所有结果:" + (result > 0 ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
        if (result > 0) {
            List<T> list = adapter.getList();
            if (list != null) {
                list.clear();
            }
        }
        adapter.notifyDataSetChanged();*/
    }


    /**
     * 改良扩展，支持
     */
    protected void onAddItemClick() {
    }


    protected T onCreateBean(String value) {
        //https://www.cnblogs.com/onlysun/p/4539472.html
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        T t = null;
        if (actualTypeArguments.length < 0) {
            return t;
        } else {
            Class<T> className = (Class<T>) actualTypeArguments[0];

            try {
                t = className.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }

        }
        return t;
    }

    protected boolean onQueryExist(String account) {

        return DBHelper.getIgnoreQQDBUtil(AppContext.dbUtils).queryColumnExist(AccountBean.class, Fields.FIELD_ACCOUNT, account);

    }


    protected void onLongDeleteClick(int position) {

     /*   {
            T bean = adapter.getList().get(position);
            int i = onDeleteToDb(bean);
            if (i <= 0) {
                DialogUtils.showDialog(BaseMessageManagerActivity.this, "删除失败");
            } else {
                adapter.getList().remove(position);
                adapter.notifyDataSetChanged();
                doDataChanageSuccEvent();
            }
        }*/

    }


    /**
     * 检查数据是否合法 以及 用于编辑的时候提供给继承类，让其操作 给模型赋值，返回true,则表示 合法，否则还可以返回false,
     *
     * @param t
     * @param param
     * @return
     */
    protected boolean onInsertOrAddDialogReceiveData(T t, List param) {
        return true;

    }

    /**
     * 应该用户自己去实现逻辑!!!
     *
     * @param t
     * @return
     */
    protected int onEditToDb(T t) {

        return -2;

    }


    protected abstract int getAdapterType();

    /*
        protected abstract void doSubmitInsertSuccEvent(T bean);

        protected abstract long onInsertToDb(T bean);

        protected abstract void doDataChanageSuccEvent();

        protected abstract int onDeleteToDb(T bean);

        protected abstract List<T> queryDataFromDb();

    */
    protected String[] getClickMenu() {
        return new String[]{"查看"};
    }

    protected String[] getLongPressMenu() {
        return new String[]{"查看"};
    }
}
