package cn.qssq666.robot.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.base.BaseRecyclervdapter;
import cn.qssq666.robot.viewholder.GenericDataBindViewHolder;

/**
 * Created by qssq on 2017/10/31 qssq666@foxmail.com
 */

public abstract class GenericModelAdapter<T> extends BaseRecyclervdapter<T, GenericDataBindViewHolder> {
    @Override
    public GenericDataBindViewHolder onCreateViewHolderByExtend(ViewGroup parent, int viewType) {
        ViewDataBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutID(viewType), parent, false);
        return new GenericDataBindViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(GenericDataBindViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.model, getData().get(position));
    }

    public abstract int getLayoutID(int viewType);


}
