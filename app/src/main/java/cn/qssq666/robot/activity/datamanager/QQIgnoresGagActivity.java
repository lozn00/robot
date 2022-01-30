package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.MenuUtil;

/**
 * Created by luozheng on 11.24 22点15分
 */

public class QQIgnoresGagActivity extends BaseAccountManagerActivity<AccountBean> {

    protected boolean onQueryExist(String account) {

        return DBHelper.getIgnoreGagDBUtil(AppContext.dbUtils).queryColumnExist(AccountBean.class, Fields.FIELD_ACCOUNT, account);

    }

    @Override
    protected String[] getLongPressMenu() {
        return MenuUtil.build(super.getLongPressMenu()).add("定位QQ");
    }

    @Override
    protected void onLongOtherClick(int position, int which) {
        switch (which) {
            case 2: {
                try {
                    AccountBean accountBean = adapter.getList().get(position);
                    AppUtils.openQQChat(this, accountBean.getAccount());

                } catch (Exception e) {
                    Toast.makeText(this, "启动失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
        super.onLongOtherClick(position, which);
    }

    protected String getInsertTipTitle() {
        return "请输入要忽略不回复的QQ";
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_QQ;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_QQ_INOGRE_NAME;
    }

    protected void doSubmitInsertSuccEvent(AccountBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_QQ_INOGRE_NAME);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(AccountBean accountBean) {
        return DBHelper.getIgnoreGagDBUtil(AppContext.dbUtils).insert(accountBean);

    }


    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getIgnoreGagDBUtil(AppContext.dbUtils).deleteAll(AccountBean.class);
    }

    protected void onLongDeleteClick(int position) {

        {
            AccountBean bean = adapter.getList().get(position);
            int i = onDeleteToDb(bean);
            if (i <= 0) {
                DialogUtils.showDialog(this, "删除失败");
            } else {
                adapter.getList().remove(position);
                adapter.notifyItemRemoved(position);
                doDataChanageSuccEvent();
            }
        }

    }

    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_QQ_INOGRE_NAME);
        EventBus.getDefault().post(event);
    }

    protected int onDeleteToDb(AccountBean bean) {
        int i = DBHelper.getIgnoreGagDBUtil(AppContext.getDbUtils()).deleteById(AccountBean.class, bean.getId());
        return i;
    }

    protected List<AccountBean> queryDataFromDb() {
        List<AccountBean> list = DBHelper.getIgnoreGagDBUtil(AppContext.getDbUtils()).queryAllIsDesc(AccountBean.class, true, FieldCns.FIELD_ACCOUNT);
        return list;
    }


}
