package cn.qssq666.robot.activity.datamanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.qssq666.robot.R;
import cn.qssq666.robot.activity.SuperActivity;
import cn.qssq666.robot.adapter.AccountAdapter;
import cn.qssq666.robot.adapter.DefaultAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.databinding.ListActivityBinding;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.ui.ListDividerItemDecoration;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public abstract class BaseAccountManagerActivity<T extends AccountBean> extends SuperActivity {

    private ListActivityBinding binding;

    public AccountAdapter<T> getAdapter() {
        return adapter;
    }

    protected AccountAdapter<T> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.list_activity);
        setSupportActionBar(binding.toolbar);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ;
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.addItemDecoration(new ListDividerItemDecoration(this, R.drawable.divider));
        adapter = new AccountAdapter();
        adapter.setType(getAdapterType());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new DefaultAdapter.OnItemLongClickListener<T>() {
            @Override
            public boolean onItemClick(T model, View view, final int itemPosition) {
                if (needInterceptLongClick(model, itemPosition)) {
                    return true;
                }

                DialogUtils.showMenuDialog(BaseAccountManagerActivity.this, "操作", getLongPressMenu(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int menuWhich) {
                        switch (menuWhich) {
                            case 0: {
                                onLongEditClick(itemPosition);
                            }
                            break;
                            case 1:
                                onLongDeleteClick(itemPosition);
                                break;
                            default:
                                onLongOtherClick(itemPosition, menuWhich);
                                break;
                        }
                    }
                });
                return true;
            }
        });

        adapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(T model, View view, final int position) {


                DialogUtils.showMenuDialog(BaseAccountManagerActivity.this, "操作", getLongPressMenu(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                onLongEditClick(position);
                            }
                            break;
                            case 1:
                                onLongDeleteClick(position);
                                break;
                            default:
                                onLongOtherClick(position, which);
                                break;
                        }
                    }
                });

            }
        });
        binding.swiperefreshlayout.setRefreshing(true);
        binding.swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                doOnRefresh();

            }
        });

        queryData();

    }

    protected boolean needInterceptLongClick(T model, int position) {
        return false;
    }

    protected void doOnRefresh() {
        queryData();
    }

    /**
     * @param position
     * @param which    菜单位置
     */

    protected void onLongOtherClick(int position, int which) {

    }


    protected void onLongEditClick(final int position) {
        final T bean = adapter.getList().get(position);
        List<String> tiplist = getEditTitleTipList();
        List defaultvaluelist = getEditValueList(bean);
        DialogUtils.showEditDialog(this, getEditTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List param) {
                String first = (String) param.get(0);
                if (!onInsertOrAddDialogReceiveData(bean, param)) {
                    return;
                }
                if (TextUtils.isEmpty(first)) {
                    Toast.makeText(BaseAccountManagerActivity.this, "禁止输入空值", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    bean.setAccount(first);
                    int update = onEditToDb(bean);
                    if (update == -2) {
                        Toast.makeText(BaseAccountManagerActivity.this, "抱歉,暂不支持修改", Toast.LENGTH_SHORT).show();

                    } else if (update == 0) {

                        Toast.makeText(BaseAccountManagerActivity.this, "更新失败!建议使用数据库管理工具查看id是否已设置，如果没有设置则建议直接删除此表,然后重启QQ机器人。", Toast.LENGTH_SHORT).show();


                    } else {
                        doDataChanageSuccEvent();

                        adapter.notifyItemChanged(position);
                        Toast.makeText(BaseAccountManagerActivity.this, "更新成功,影响记录总数:" + update, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    public void queryData() {
        new QssqTask<List<T>>(new QssqTask.ICallBack<List<T>>() {
            @Override
            public List<T> onRunBackgroundThread() {
                return queryDataFromDb();
            }

            @Override
            public void onRunFinish(List<T> list) {
                doOnQueryFinish(list);
            }
        }).execute();
    }

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
        getMenuInflater().inflate(R.menu.menu_add_activity, menu);
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
        int result = deleteAllFromDb();

        Toast.makeText(this, "删除所有结果:" + (result > 0 ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
        if (result > 0) {
            List<T> list = adapter.getList();
            if (list != null) {
                list.clear();
            }
        }
        adapter.notifyDataSetChanged();
    }

    protected abstract int deleteAllFromDb();


    /**
     * 改良扩展，支持
     */
    protected void onAddItemClick() {
        List<String> tiplist = getInsertTitleTipList();
        List defaultvaluelist = getInsertValelist();
        DialogUtils.showEditDialog(this, getInsertTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String value = param.get(0);


                if (TextUtils.isEmpty(value)) {
                    return;
                } else {
                    if (onQueryExist(value)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    T object = onCreateBean(value);
                    if (!onInsertOrAddDialogReceiveData(object, param)) {
                        return;
                    }
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(BaseAccountManagerActivity.this, "添加是否成功 " + (insert > 0));
                    if (insert > 0) {
                        object.setId((int) insert);
                        if (adapter.getList() == null) {
                            ArrayList list = new ArrayList<AccountBean>();
                            list.add(object);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();


                        } else {
                            adapter.getList().add(0, object);
                            adapter.notifyItemInserted(0);
                        }
                        doSubmitInsertSuccEvent(object);


                    } else {
                        AppContext.showToast("添加失败，如果删除所有后重新添加数据,依然无法使用请进入高级管理删除当前表.");
                    }
                }

            }
        });
    }



/*
    protected void onAddItemClick() {
        DialogUtils.showEditDialog(this, getInsertTipTitle(), null, getInsertDefaultValue(), new INotify<String>() {
            @Override
            public void onNotify(String param) {

                if (TextUtils.isEmpty(param)) {
                    return;
                } else {
                    if (onQueryExist(param)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    T object = onCreateBean(param);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(BaseAccountManagerActivity.this, "添加是否成功 " + (insert > 0));
                    if (insert > 0) {
                        object.setId((int) insert);
                        if (adapter.getList() == null) {
                            ArrayList<T> list = new ArrayList<T>();
                            list.add(object);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();


                        } else {
                            adapter.getList().add(0, object);
                            adapter.notifyItemInserted(0);
                        }
                        doSubmitInsertSuccEvent(object);
                    } else {
                        AppContext.showToast("添加失败!");
                    }
                }
            }
        });
    }
    */


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
        t.setAccount(value);
        return t;
    }

    protected boolean onQueryExist(String account) {

        return DBHelper.getIgnoreQQDBUtil(AppContext.dbUtils).queryColumnExist(AccountBean.class, Fields.FIELD_ACCOUNT, account);

    }

    protected String getInsertTipTitle() {
        return "请添加数据";
    }

    protected String getInsertDefaultValue() {
        return "153016267";
    }

    protected String getEditTipTitle() {
        return "153016267";
    }


    protected List<String> getInsertTitleTipList() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("账号");
        return tiplist;
    }

    protected List getInsertValelist() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("" + getInsertDefaultValue());
        return tiplist;
    }


    public List<String> getEditTitleTipList() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("添加数据");
        return tiplist;
    }

    /**
     * 编辑多列的时候兼容任何模型，只需要用户自己对模型的编辑框内容进行赋值即可。 赋值操作在弹出编辑框的 onInsertOrAddDialogReceiveData 方法进行回调，
     *
     * @param bean
     * @return
     */
    public List getEditValueList(T bean) {
        List tiplist = new ArrayList<>();
        tiplist.add("" + bean);
        return tiplist;

    }


    protected void onLongDeleteClick(int position) {

        {
            T bean = adapter.getList().get(position);
            int i = onDeleteToDb(bean);
            if (i <= 0) {
                DialogUtils.showDialog(BaseAccountManagerActivity.this, "删除失败");
            } else {
                adapter.getList().remove(position);
                adapter.notifyDataSetChanged();
                doDataChanageSuccEvent();
            }
        }

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

    protected abstract void doSubmitInsertSuccEvent(T bean);

    protected abstract long onInsertToDb(T bean);

    protected abstract void doDataChanageSuccEvent();

    protected abstract int onDeleteToDb(T bean);

    protected abstract List<T> queryDataFromDb();


    protected String[] getLongPressMenu() {
        return Cns.OPERA_MENU;
    }
}
