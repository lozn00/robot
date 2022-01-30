/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.robot.viewholder;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import cn.qssq666.robot.interfaces.BaseViewHolderI;


/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/9.
 */

public class GenericDataBindViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder implements BaseViewHolderI {
    private T binding;

    /**
     * `@Deprecated 如果用户直接从这里访问那么可能导致用户忘记什么databiing
     */
    public GenericDataBindViewHolder(View itemView) {
        super(itemView);
        this.binding = DataBindingUtil.findBinding(itemView);
    }

    public GenericDataBindViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void setBinding(T binding) {
        this.binding = binding;
    }

    public T getBinding() {
        return this.binding;
    }

    @Override
    public View getItemView() {
        return binding.getRoot();
    }
}
