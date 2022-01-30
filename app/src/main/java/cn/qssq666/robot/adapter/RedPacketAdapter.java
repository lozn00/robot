package cn.qssq666.robot.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.bean.RedPacketBean;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class RedPacketAdapter extends DefaultAdapter<RedPacketBean> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_item_redpacket, parent, false);
        return new BaseViewHolder(binding);
    }


    @Override
    public void onBindViewHolderFix(final BaseViewHolder holder, int position) {
        final RedPacketBean value = getList().get(position);
        holder.getBinding().setVariable(BR.model, value);
    }
}
