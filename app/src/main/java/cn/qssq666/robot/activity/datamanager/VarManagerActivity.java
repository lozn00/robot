package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class VarManagerActivity extends BaseAccountManagerActivity<VarBean> {


    protected boolean onQueryExist(String account) {

        return DBHelper.getVarTableUtil(AppContext.dbUtils).queryColumnExist(VarBean.class, Fields.FIELD_NAME, account);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_activity, menu);

        return true;
    }


    protected int getAdapterType() {
        return AccountType.TYPE_VAR_MANAGER;
    }


    protected void doSubmitInsertSuccEvent(VarBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_VAR_MANAGER);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(VarBean VarBean) {
        return DBHelper.getVarTableUtil(AppContext.dbUtils).insert(VarBean);

    }

    @Override
    protected boolean onClickHelpMenu(MenuItem item) {
        DialogUtils.showDialog(this, "变量的意义:\n输入配置print $红包福利 或者输入配置SQL $变量sql 或者输入艾特全体 $变量名 ，现在懂了吧??除了ui可以添加变量外,也可以通过命令配置添加变量进行添加哦!");
        return true;

    }

    @Override
    protected int onEditToDb(VarBean varBean) {
        return DBHelper.getVarTableUtil(AppContext.dbUtils).update(varBean);
    }

    @Override
    protected String[] getLongPressMenu() {
        String[] OPERA_MENU = {"编辑", "删除"};
        return OPERA_MENU;
    }

    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getVarTableUtil(AppContext.dbUtils).deleteAll(VarBean.class);
    }

    protected void onLongDeleteClick(final int position) {
        DialogUtils.showConfirmDialog(this, "真的要删除么?", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {

                VarBean bean = adapter.getList().get(position);
                int i = onDeleteToDb(bean);
                if (i <= 0) {
                    DialogUtils.showDialog(VarManagerActivity.this, "删除失败" + (bean.getId() == 0 ? "因为没有id,建议请先删除所有" : "请直接删除所有."));
                } else {
                    adapter.getList().remove(position);
                    adapter.notifyItemRemoved(position);
                    doDataChanageSuccEvent();
                }
            }
        });

    }

    @Override

    protected void onAddItemClick() {
        List<String> tiplist = getInsertTitleTipList();
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("红包福利");
        defaultvaluelist.add("" + getInsertDefaultValue());

        DialogUtils.showEditDialog(this, getInsertTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String name = param.get(0);
                String value = param.get(1);
                if (TextUtils.isEmpty(name)) {
                    return;
                } else {
                    if (onQueryExist(name)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    VarBean object = onCreateBean(name);
                    object.setName(name);
                    object.setValue(value);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(VarManagerActivity.this, "添加是否成功 " + (insert > 0));
                    if (insert > 0) {
                        object.setId((int) insert);
                        if (adapter.getList() == null) {
                            ArrayList list = new ArrayList<VarBean>();
                            list.add(object);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();

                        } else {
                            adapter.getList().add(0, object);
//                            adapter.notifyDataSetChanged();
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

    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_VAR_MANAGER);
        EventBus.getDefault().post(event);

    }

    protected int onDeleteToDb(VarBean bean) {
        int i = DBHelper.getVarTableUtil(AppContext.getDbUtils()).deleteById(VarBean.class, bean.getId());
        return i;
    }

    protected List<VarBean> queryDataFromDb() {
        List<VarBean> list = DBHelper.getVarTableUtil(AppContext.getDbUtils()).queryAllIsDesc(VarBean.class, true, null);
//        List<VarBean> list = DBHelper.getVarTableUtil(AppContext.getDbUtils()).queryAllIsDesc(VarBean.class, true, FieldCns.ID);
        return list;
    }


    protected String getInsertTipTitle() {
        return "测试参数";
    }


    @Override
    protected List<String> getInsertTitleTipList() {
        List<String> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add("变量名");
        insertTitleTipList.add("变量值");
        return insertTitleTipList;
    }

    @Override
    public List<String> getEditTitleTipList() {
        return getInsertTitleTipList();
    }

    @Override
    public List getEditValueList(VarBean bean) {
        List tiplist = new ArrayList<>();
        tiplist.add("" + bean.getName());
        tiplist.add("" + bean.getValue());
        return tiplist;
    }

    @Override
    protected boolean onInsertOrAddDialogReceiveData(VarBean varBean, List param) {
        varBean.setName(param.get(0) + "");
        varBean.setValue(param.get(1) + "");
        return true;
    }

    @Override
    protected List getInsertValelist() {

        List insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add("测试1");
        insertTitleTipList.add("我是变量1 %s 我是变量2 %s ");
        return insertTitleTipList;
    }


}
