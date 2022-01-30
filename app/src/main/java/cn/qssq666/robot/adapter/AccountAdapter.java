package cn.qssq666.robot.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.handler.AccountClickHandler;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class AccountAdapter<T extends AccountBean> extends DefaultAdapter<T> {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int type;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutType;
        if (type == AccountType.TYPE_GAG) {
            layoutType = R.layout.view_item_gag_word;
        } else if (type == AccountType.TYPE_QQGROUP_WHITE_NAME) {
            layoutType = R.layout.view_item_two_data;
        } else if (type == AccountType.TYPE_VAR_MANAGER) {
            layoutType = R.layout.view_item_two_data;
        } else if (type == AccountType.TYPE_GROUP_ADMIN) {
            layoutType = R.layout.view_item_two_data;
        } else if (type == AccountType.TYPE_FLOOR) {
            layoutType = R.layout.view_item_floor;
        } else if (type == AccountType.TYPE_SUPER_MANAGER) {
            layoutType = R.layout.view_item_two_data;
        } else if (type == AccountType.TYPE_NICKNAME) {
            layoutType = R.layout.view_item_tree_data;
        } else if (type == AccountType.TYPE_PLUGIN) {
            layoutType = R.layout.view_item_plugin_data;
        } else if (type == AccountType.TYPE_QQ_INOGRE_NAME) {//TYPE_QQ_INOGRE_NAME
            layoutType = R.layout.view_item_white_name;
        } else if (type == AccountType.TYPE_QQ_INOGRE_NAME_GAG) {//TYPE_QQ_INOGRE_NAME
            layoutType = R.layout.view_item_white_name;
        } else {
            layoutType = R.layout.view_item_white_name;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutType, parent, false);
        return new BaseViewHolder(binding);
    }


    @Override
    public void onBindViewHolderFix(BaseViewHolder holder, final int position) {
        holder.getBinding().setVariable(BR.model, getList().get(position));
        AccountClickHandler clickHandler = new AccountClickHandler();
        holder.getBinding().setVariable(BR.handler, clickHandler);
        clickHandler.setAdapter(this);

  /*      ((ViewItemWhiteNameBinding) holder.getBinding()).btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountBean bean = getList().get(position);
                bean.setDisable(bean.isDisable() ? false : true);

                switch (type) {
                    case AccountType.TYPE_QQ_INOGRE_NAME: {

                        DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).update(bean);
                        OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                        event.setType(AccountType.TYPE_QQ_INOGRE_NAME);
                        event.setList(getList());
                        EventBus.getDefault().post(event);
                    }
                    break;
                    case AccountType.TYPE_QQGROUP_WHITE_NAME: {
                        DBHelper.getIgnoreQQDBUtil(AppContext.getDbUtils()).update(bean);

                        OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                        event.setList(getList());
                        event.setType(AccountType.TYPE_QQGROUP_WHITE_NAME);
                        EventBus.getDefault().post(event);
                    }
                    break;

                    case AccountType.TYPE_SUPER_MANAGER: {
                        DBHelper.getSuperManager(AppContext.getDbUtils()).update(bean);
                        OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                        event.setList(getList());
                        event.setType(AccountType.TYPE_SUPER_MANAGER);
                        EventBus.getDefault().post(event);
                    }
                    break;
                    case AccountType.TYPE_GAG: {
                        DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(((GagAccountBean) bean));
                        OnUpdateAccountListEvent event = new OnUpdateAccountListEvent();
                        event.setList(getList());
                        event.setType(AccountType.TYPE_GAG);
                        EventBus.getDefault().post(event);
                    }
                    break;
                    default:
                        break;
                }

            }
        });*/
    }
}
