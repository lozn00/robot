package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.AdminBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.ParseUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class QQSuperManagerActivity extends BaseAccountManagerActivity<AdminBean> {
    protected boolean onQueryExist(String account) {
        return DBHelper.getSuperManager(AppContext.dbUtils).queryColumnExist(AdminBean.class, Fields.FIELD_ACCOUNT, account);
    }

    protected String getInsertTipTitle() {
        return "请输入要添加的管理员";
    }

    @Override
    protected String getEditTipTitle() {
        return "等级权限越高权限越大";
    }


    @Override
    protected List<String> getInsertTitleTipList() {
        List<String> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add("管理员QQ");
        insertTitleTipList.add("权限等级");
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

    @Override
    protected boolean onInsertOrAddDialogReceiveData(AdminBean adminBean, List param) {
        Object o = param.get(1);
        adminBean.setLevel(ParseUtils.parseInt(o));
        return true;
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_QQ;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_SUPER_MANAGER;
    }

    protected void doSubmitInsertSuccEvent(AdminBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_SUPER_MANAGER);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(AdminBean AdminBean) {
        return DBHelper.getSuperManager(AppContext.dbUtils).insert(AdminBean);

    }


    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getSuperManager(AppContext.dbUtils).deleteAll(AdminBean.class);
    }


    @Override
    public List<Object> getEditValueList(AdminBean bean) {
        List<Object> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add(bean.getAccount());
        insertTitleTipList.add(bean.getLevel());
        return insertTitleTipList;
    }


    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_SUPER_MANAGER);
        EventBus.getDefault().post(event);
    }

    @Override
    protected int onEditToDb(AdminBean adminBean) {
        int i = DBHelper.getSuperManager(AppContext.getDbUtils()).update(adminBean);
        return i;
    }

    protected int onDeleteToDb(AdminBean bean) {
        int i = DBHelper.getSuperManager(AppContext.getDbUtils()).deleteById(AdminBean.class, bean.getId());
        return i;
    }

    protected List<AdminBean> queryDataFromDb() {
        List<AdminBean> list = DBHelper.getSuperManager(AppContext.getDbUtils()).queryAllIsDesc(AdminBean.class, true, FieldCns.FIELD_ACCOUNT);
        return list;
    }


}
