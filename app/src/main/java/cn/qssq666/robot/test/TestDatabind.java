package cn.qssq666.robot.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by qssq on 2018/1/20 qssq666@foxmail.com
 */

public class TestDatabind  extends androidx.appcompat.widget.AppCompatTextView{
    public TestDatabind(Context context) {
        super(context);
    }

    public TestDatabind(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestDatabind(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
/*

    public TestDatabind(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    String name;
}
