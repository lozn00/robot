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

package cn.qssq666.robot.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/7. 虽然能解决左右平均分配问题但是不能解决4个item 中每一个item 宽度一致问题。
 * 因此开发者还需要 写一个东西就是
 * jingxuanHeadBinding.recyclerviewSinger.setPadding(spacingInPixels/2, 0, spacingInPixels/2, spacingInPixels/2);
 */

public class EqualDivierDecoration extends SpacesItemDecoration {
    private boolean needTop;

    /**
     * 不包括顶部
     * @param recyclerView
     * @param space
     */
    public EqualDivierDecoration(RecyclerView recyclerView, int space) {
        super(space);
        /**
         * 顶部 下面实现了
         */
        recyclerView.setPadding(getSpace() / 2, 0, getSpace() / 2, getSpace() / 2);
    }

    public EqualDivierDecoration(RecyclerView recyclerView, int space, boolean needLeft, boolean needRight) {
        this(recyclerView, space, needLeft, needRight, true);
        /**
         * 顶部 下面实现了
         */
    }

    public EqualDivierDecoration(RecyclerView recyclerView, int space, boolean needLeft, boolean needRight, boolean isneddTop) {
        super(space);
        /**
         * 顶部 下面实现了
         */
        this.needTop = isneddTop;
        recyclerView.setPadding(needLeft ? getSpace() / 2 : 0, isneddTop ? getSpace() / 2 : 0, needRight ? getSpace() / 2 : 0, getSpace() / 2);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int itemCount = parent.getLayoutManager().getItemCount();
//        int childAdapterPosition = parent.getChildAdapterPosition(view);
//     GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();

//        int spanCount = layoutManager.getSpanCount();
//        int result = childAdapterPosition % spanCount;
        //左右必须等分 上下没关系。
        if (needTop) {
            outRect.top = getSpace() / 2;
        }
        outRect.left = getSpace() / 2;
        outRect.right = getSpace() / 2;
        outRect.bottom = getSpace() / 2;//底部的diy不给提供，只负责顶部

    }
}
