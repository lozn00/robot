package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.FloorBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.RegexUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class FloorManagerActivity extends BaseAccountManagerActivity<FloorBean> {


    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getFloorUtil(AppContext.dbUtils).deleteAll(FloorBean.class);
    }

    protected boolean onQueryExist(String account) {
        return DBHelper.getFloorUtil(AppContext.dbUtils).queryColumnExist(FloorBean.class, Fields.FIELD_ACCOUNT, account);

    }

    protected String getInsertTipTitle() {
        return "请输入要添加的楼层数据";
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_QQ;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_FLOOR;
    }

    @Override
    protected void doSubmitInsertSuccEvent(FloorBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_FLOOR);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }

    @Override
    protected long onInsertToDb(FloorBean accountBean) {
        return DBHelper.getFloorUtil(AppContext.dbUtils).insert(accountBean);

    }

/*
    protected void onAddItemClick() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("群");
        tiplist.add("QQ(多个逗号隔开)");
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("1234567");
        defaultvaluelist.add("35068264,153016267");
        DialogUtils.showEditDialog(this, "请输入要添加的楼层", tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String arg0 = param.get(0);
                String arg1 = param.get(1);


                if (TextUtils.isEmpty(arg0)) {
                    return;
                } else {
                    if (onQueryExist(arg0)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    FloorBean object = new FloorBean(arg0, arg1);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(FloorManagerActivity.this, "添加是否成功 " + (insert > 0));
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
                        AppContext.showToast("添加失败!");
                    }
                }

            }
        });
    }*/


    @Override
    protected List<String> getInsertTitleTipList() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("群");
        tiplist.add("QQ(多个逗号隔开)");
        return tiplist;
    }


    @Override
    protected List<String> getInsertValelist() {
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("1234567");
        defaultvaluelist.add("35068264,153016267");
        return defaultvaluelist;

    }

    @Override
    public List<String> getEditTitleTipList() {
        return getInsertTitleTipList();
    }


    @Override
    public List<String> getEditValueList(FloorBean bean) {
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("" + bean.getAccount());
        defaultvaluelist.add(bean.getData() + "");
        return defaultvaluelist;

    }

/*
    @Override
    protected void onLongEditClick(int position) {
        final FloorBean bean = (FloorBean) adapter.getList().get(position);
        List<String> tiplist = new ArrayList<>();
        tiplist.add("楼层群");
        tiplist.add("数据");
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("" + bean.getAccount());
        defaultvaluelist.add(bean.getData() + "");
        DialogUtils.showEditDialog(this, "修改楼层(修改在重启后还是会覆盖的)", tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {

                String first = param.get(0);
                String second = param.get(1);
                if (TextUtils.isEmpty(first)) {
                    Toast.makeText(FloorManagerActivity.this, "禁止输入空值", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!RegexUtils.checkDigit(second)) {
                    Toast.makeText(FloorManagerActivity.this, "输入的值不是数字", Toast.LENGTH_SHORT).show();

                } else {

                    bean.setAccount(first);
                    bean.setData(second);
                    int update = DBHelper.getFloorUtil(AppContext.getDbUtils()).update(bean);
                    Toast.makeText(FloorManagerActivity.this, "更新成功,影响记录总数:" + update, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }*/


    @Override
    protected String getEditTipTitle() {
        return "修改楼层(修改在重启后还是会覆盖的)";
    }


    @Override
    protected boolean onInsertOrAddDialogReceiveData(FloorBean floorBean, List param) {

        String first = (String) param.get(0);
        String second = (String) param.get(1);
        if (TextUtils.isEmpty(first)) {
            Toast.makeText(FloorManagerActivity.this, "禁止输入空值", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!RegexUtils.checkDigit(second)) {

            Toast.makeText(FloorManagerActivity.this, "输入的值不是数字", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            floorBean.setAccount(first);
            floorBean.setData(second);

        }
        return true;
    }

    @Override
    protected int onEditToDb(FloorBean floorBean) {
        int update = DBHelper.getFloorUtil(AppContext.getDbUtils()).update(floorBean);
        return update;
    }

    @Override
    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_FLOOR);
        EventBus.getDefault().post(event);
    }

    @Override
    protected int onDeleteToDb(FloorBean bean) {
        int i = DBHelper.getFloorUtil(AppContext.getDbUtils()).deleteById(FloorBean.class, bean.getId());
        return i;
    }

    @Override
    protected List<FloorBean> queryDataFromDb() {
        List list = DBHelper.getFloorUtil(AppContext.getDbUtils()).queryAllIsDesc(FloorBean.class, true, FieldCns.FIELD_ACCOUNT);
        return list;
    }


}
