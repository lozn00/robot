package cn.qssq666.robot.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    ViewDataBinding binding;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

}

