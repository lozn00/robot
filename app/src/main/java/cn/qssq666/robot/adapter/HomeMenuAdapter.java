package cn.qssq666.robot.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.base.BaseRecyclervdapter;
import cn.qssq666.robot.xbean.HomeMenu;
import cn.qssq666.robot.databinding.ViewItemHomeMenuBinding;
import cn.qssq666.robot.viewholder.GenericDataBindViewHolder;

/**
 * Created by qssq on 2018/1/18 qssq666@foxmail.com
 */

public class HomeMenuAdapter extends BaseRecyclervdapter<HomeMenu, GenericDataBindViewHolder<ViewItemHomeMenuBinding>> {


    @Override
    public GenericDataBindViewHolder onCreateViewHolderByExtend(ViewGroup parent, int viewType) {
        return new GenericDataBindViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_item_home_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(GenericDataBindViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.model, getData().get(position));
    }
}
