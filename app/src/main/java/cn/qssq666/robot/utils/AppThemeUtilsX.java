package cn.qssq666.robot.utils;

import androidx.recyclerview.widget.RecyclerView;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.ui.EqualDivierDecoration;
import cn.qssq666.robot.ui.ListDividerItemDecoration;

/**
 * Created by qssq on 2018/1/18 qssq666@foxmail.com
 */

public class AppThemeUtilsX {




    /**
     * 不包括顶部
     *
     * @param recyclerView
     */
    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView) {
        int spacingInPixels = AppContext.getInstance().getResources().getDimensionPixelOffset(R.dimen.theme_margin_distance);
        EqualDivierDecoration decor = new EqualDivierDecoration(recyclerView, spacingInPixels);
        recyclerView.addItemDecoration(decor);
        return decor;
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight) {
        return setGridLayoutDividerItem(recyclerView, isNeedLeft, isNeedRight, true);
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight, boolean isNeedTop) {
        int spacingInPixels = AppContext.getInstance().getResources().getDimensionPixelOffset(R.dimen.theme_margin_distance);
        return setGridLayoutDividerItem(recyclerView, isNeedLeft, isNeedRight, isNeedTop, spacingInPixels);
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight, boolean isNeedTop, int size) {

        EqualDivierDecoration decor = new EqualDivierDecoration(recyclerView, size, isNeedLeft, isNeedRight, isNeedTop);
        recyclerView.addItemDecoration(decor);
        return decor;
    }

    public static void setLineLayoutDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new ListDividerItemDecoration(AppContext.getInstance(), R.drawable.shape_divider));
    }

}
