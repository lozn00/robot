package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.GroupAdaminBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class GroupAdminActivity extends BaseAccountManagerActivity<GroupAdaminBean> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("群管理员");
    }

    protected boolean onQueryExist(String account) {

        return DBHelper.getGroupAdminTableUtil(AppContext.dbUtils).queryColumnExist(GroupAdaminBean.class, Fields.FIELD_NAME, account);

    }

    protected String getInsertTipTitle() {
        return "添加群管理";
    }

    protected String getInsertDefaultValue() {
        return "群1,群2";
    }


    @Override
    protected String getEditTipTitle() {
        return "1个qq对应多个群模式";
    }


    @Override
    protected List<String> getInsertTitleTipList() {
        List<String> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add("管理员QQ");
        insertTitleTipList.add("输入如 群1,群号2");
        return insertTitleTipList;
    }


    @Override
    public List<String> getEditTitleTipList() {
        return getInsertTitleTipList();
    }


    @Override
    protected List getInsertValelist() {

        List insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add(getInsertDefaultValue());
        insertTitleTipList.add(0);
        return insertTitleTipList;
    }


    protected int getAdapterType() {
        return AccountType.TYPE_GROUP_ADMIN;
    }


    protected void doSubmitInsertSuccEvent(GroupAdaminBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_GROUP_ADMIN);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(GroupAdaminBean GroupManagerBean) {
        return DBHelper.getGroupAdminTableUtil(AppContext.dbUtils).insert(GroupManagerBean);

    }


    @Override
    protected String[] getLongPressMenu() {
        String[] OPERA_MENU = {"编辑", "删除"};
        return OPERA_MENU;
    }

    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getGroupAdminTableUtil(AppContext.dbUtils).deleteAll(GroupAdaminBean.class);
    }

    protected void onLongDeleteClick(final int position) {
        DialogUtils.showConfirmDialog(this, "真的要删除么?", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {

                GroupAdaminBean bean = adapter.getList().get(position);
                int i = onDeleteToDb(bean);
                if (i <= 0) {
                    DialogUtils.showDialog(GroupAdminActivity.this, "删除失败");
                } else {
                    adapter.getList().remove(position);
                    adapter.notifyItemRemoved(position);
                    doDataChanageSuccEvent();
                }
            }
        });

    }


    protected void onAddItemClick() {
        List<String> tiplist = getInsertTitleTipList();
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("153016267");
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

                    GroupAdaminBean object = onCreateBean(name);
                    object.setName(name);
                    object.setGroups(value);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(GroupAdminActivity.this, "添加是否成功 " + (insert > 0));
                    if (insert > 0) {
                        object.setId((int) insert);
                        if (adapter.getList() == null) {
                            ArrayList list = new ArrayList<GroupAdaminBean>();
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
        event.setType(AccountType.TYPE_GROUP_ADMIN);
        EventBus.getDefault().post(event);
    }

    protected int onDeleteToDb(GroupAdaminBean bean) {
        int i = DBHelper.getGroupAdminTableUtil(AppContext.getDbUtils()).deleteById(GroupAdaminBean.class, bean.getId());
        return i;
    }

    protected List<GroupAdaminBean> queryDataFromDb() {
        List<GroupAdaminBean> list = DBHelper.getGroupAdminTableUtil(AppContext.getDbUtils()).queryAllIsDesc(GroupAdaminBean.class, true, null);
//        List<GroupManagerBean> list = DBHelper.getGroupAdminTableUtil(AppContext.getDbUtils()).queryAllIsDesc(GroupManagerBean.class, true, FieldCns.ID);
        return list;
    }

    /**
     * 处理编辑
     *
     * @param adminBean
     * @param param
     * @return
     */
    @Override
    protected boolean onInsertOrAddDialogReceiveData(GroupAdaminBean adminBean, List param) {
        Object o = param.get(1);
        adminBean.setGroups(o + "");
        return true;
    }

    @Override
    public List getEditValueList(GroupAdaminBean bean) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(bean.getAccount());
        objects.add(bean.getGroups());
        return objects;
    }

    @Override
    protected int onEditToDb(GroupAdaminBean groupAdaminBean) {
        return DBHelper.getGroupAdminTableUtil(AppContext.getDbUtils()).update(groupAdaminBean);
    }
}
