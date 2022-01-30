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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.qssq666.robot.R;


/**
 * id 不生成 make moudle
 * Created by luozheng on 2016/5/20.  qssq.space
 * <p>
 * 默认保持水平方向
 */
public class SquareLayout extends FrameLayout {
    private int orientation = LinearLayout.HORIZONTAL;

    public SquareLayout(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout, defStyleAttr, 0);
        int index = typedArray.getInt(R.styleable.SquareLayout_square_orientation, -1);
//        int index = MyInputConnectionWrapper.getInt(com.android.internal.R.styleable.LinearLayout_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }

        typedArray.recycle();
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(orientation == LinearLayout.HORIZONTAL ? widthMeasureSpec : heightMeasureSpec, orientation == LinearLayout.HORIZONTAL ? widthMeasureSpec : heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //核心就是下面这块代码块啦
//        int width = orientation == LinearLayout.HORIZONTAL ? getMeasuredWidth() : getMeasuredHeight();
        if (orientation == LinearLayout.HORIZONTAL) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        } else {
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = orientation == LinearLayout.HORIZONTAL ? width : width;
        lp.width = orientation == LinearLayout.HORIZONTAL ? width : height;
        setLayoutParams(lp);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    //    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        if (getHeight() != getWidth()) {
//            ViewGroup.LayoutParams layoutParams = getLayoutParams();
//            if (layoutParams != null) {
//                layoutParams.width = getHeight();
//                setLayoutParams(layoutParams);
//            }
//        }
//    }
}
