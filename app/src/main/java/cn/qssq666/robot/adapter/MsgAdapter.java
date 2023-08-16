package cn.qssq666.robot.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.bean.MsgItem;

public class MsgAdapter<T extends MsgItem> extends DefaultAdapter<T> {
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
            layoutType = R.layout.view_item_recent_msg;
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutType, parent, false);
        return new BaseViewHolder(binding);
    }


    @Override
    public void onBindViewHolderFix(BaseViewHolder holder, final int position) {
        holder.getBinding().setVariable(BR.model, getList().get(position));
        holder.getBinding().executePendingBindings();
    }
}
