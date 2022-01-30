package cn.qssq666.robot.handler;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import cn.qssq666.robot.adapter.AccountAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.event.OnUpdateAccountListEvent;
import cn.qssq666.robot.utils.DBHelper;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public class AccountClickHandler {
    AccountAdapter adapter;

    public AccountAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AccountAdapter adapter) {
        this.adapter = adapter;
    }

    public void onClickVoid(View v) {

    }

    public void onClickX(View v, Object x, AccountBean bean) {
        onClick(v, bean);

    }

    public void onClick(View v, AccountBean bean) {


//        AccountBean bean = adapter.getList().get(position);
        bean.setDisable(!bean.isDisable() );

        switch (adapter.getType()) {
            case AccountType.TYPE_QQ_INOGRE_NAME: {

                DBHelper.getIgnoreQQDBUtil(AppContext.getDbUtils()).update(bean);
                OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                event.setType(AccountType.TYPE_QQ_INOGRE_NAME);
                event.setList(adapter.getList());
                EventBus.getDefault().post(event);
            }
            break;
            case AccountType.TYPE_VAR_MANAGER: {

                DBHelper.getVarTableUtil(AppContext.getDbUtils()).update(bean);
                OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                event.setType(adapter.getType());
                event.setList(adapter.getList());
                EventBus.getDefault().post(event);
            }
            break;
            case AccountType.TYPE_QQGROUP_WHITE_NAME: {
                DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).update(bean);

                OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                event.setList(adapter.getList());
                event.setType(AccountType.TYPE_QQGROUP_WHITE_NAME);
                EventBus.getDefault().post(event);
            }
            break;

            case AccountType.TYPE_SUPER_MANAGER: {
                DBHelper.getSuperManager(AppContext.getDbUtils()).update(bean);
                OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                event.setList(adapter.getList());
                event.setType(AccountType.TYPE_SUPER_MANAGER);
                EventBus.getDefault().post(event);
            }
            break;
            case AccountType.TYPE_GAG: {
                DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(((GagAccountBean) bean));
                OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                event.setList(adapter.getList());
                event.setType(AccountType.TYPE_GAG);
                EventBus.getDefault().post(event);
            }
            break;
            case AccountType.TYPE_PLUGIN: {


//                ((PluginBean) bean).setDisable();
                AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
                event.setType(AccountType.TYPE_PLUGIN);
                EventBus.getDefault().post(event);

            }
            break;
            default:
                break;
        }

    }
}
