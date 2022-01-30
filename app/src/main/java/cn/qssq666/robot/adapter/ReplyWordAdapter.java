package cn.qssq666.robot.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.bean.ReplyWordBean;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class ReplyWordAdapter extends DefaultAdapter<ReplyWordBean> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_item_chat, parent, false);
        return new BaseViewHolder(binding);
    }


    @Override
    public void onBindViewHolderFix(BaseViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.model, getList().get(position));
    }
}
