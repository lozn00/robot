package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.NickNameBean;
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

public class NickNameManagerActivity extends BaseAccountManagerActivity<NickNameBean> {
    protected boolean onQueryExist(String account) {
        return DBHelper.getNickNameUtil(AppContext.dbUtils).queryColumnExist(NickNameBean.class, Fields.FIELD_ACCOUNT, account);
    }

    protected String getInsertTipTitle() {
        return "请添加昵称QQ数据";
    }

    @Override
    protected String getEditTipTitle() {
        return "请修改昵称QQ数据";
    }

    @Override
    protected boolean onInsertOrAddDialogReceiveData(NickNameBean NickNameBean, List param) {
        Object o = param.get(1);
        NickNameBean.setNickname(ParseUtils.parseString(o));
        return true;
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_QQ;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_NICKNAME;
    }

    protected void doSubmitInsertSuccEvent(NickNameBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_NICKNAME);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(NickNameBean bean) {
        if (TextUtils.isEmpty(bean.getTroopno())) {
            bean.setTroopno(bean.getAccount());
        }
        return DBHelper.getNickNameUtil(AppContext.dbUtils).insert(bean);

    }


    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getNickNameUtil(AppContext.dbUtils).deleteAll(NickNameBean.class);
    }


    @Override
    protected List<String> getInsertTitleTipList() {
        List<String> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add("QQ");
        insertTitleTipList.add("昵称");
        insertTitleTipList.add("群号\\可补填写");
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
        insertTitleTipList.add("");
        return insertTitleTipList;
    }

    @Override
    public List<Object> getEditValueList(NickNameBean bean) {
        List<Object> insertTitleTipList = new ArrayList<>();
        insertTitleTipList.add(bean.getAccount());
        insertTitleTipList.add(bean.getNickname());
        return insertTitleTipList;
    }


    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_NICKNAME);
        EventBus.getDefault().post(event);
    }

    @Override
    protected int onEditToDb(NickNameBean NickNameBean) {
        int i = DBHelper.getNickNameUtil(AppContext.getDbUtils()).update(NickNameBean);
        return i;
    }

    protected int onDeleteToDb(NickNameBean bean) {
        int i = DBHelper.getNickNameUtil(AppContext.getDbUtils()).deleteById(NickNameBean.class, bean.getId());
        return i;
    }

    protected List<NickNameBean> queryDataFromDb() {
        List<NickNameBean> list = DBHelper.getNickNameUtil(AppContext.getDbUtils()).queryAllIsDesc(NickNameBean.class, true, FieldCns.ID);
        return list;
    }


}
