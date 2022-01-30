package cn.qssq666.robot.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.bean.CodeSymobolBean;

/**
 * Created by qssq on 2018/8/3 qssq666@foxmail.com
 */
public class CodeSymobolAdapter extends DefaultAdapter<CodeSymobolBean> {
    @Override
    public void onBindViewHolderFix(BaseViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.value, getList().get(position));

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        DataBindingUtil.inflate(, , , )
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_item_code_symobol, parent, false);
        return new BaseViewHolder(binding);
    }
}
