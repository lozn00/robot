package cn.qssq666.robot.adapter;

import android.view.View;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    List<T> list = null;


    @Override
    public final void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (onItemClickListener != null) {
            holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition == -1) {
                        adapterPosition = position;
                    }
                    onItemClickListener.onItemClick(getList().get(adapterPosition), v, adapterPosition);
                }
            });

        }
        if (onItemLongClickListener != null) {
            holder.getBinding().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition == -1) {
                        adapterPosition = position;
                    }
                    return onItemLongClickListener.onItemClick(getList().get(adapterPosition), v, adapterPosition);

                }
            });
        }
        onBindViewHolderFix(holder, position);
    }

    public abstract void onBindViewHolderFix(BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T model, View view, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemClick(T model, View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener<T> onItemClickListener;

    public OnItemLongClickListener<T> getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    OnItemLongClickListener<T> onItemLongClickListener;
}
