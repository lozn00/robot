package cn.qssq666.robot.ui;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.databinding.InverseBindingListener;

/**
 * Created by qssq on 2018/1/20 qssq666@foxmail.com
 */

/*@InverseBindingMethods({
        @InverseBindingMethod(
                type = androidx.support.v4.widget.SwipeRefreshLayout.class,
                attribute = "customValue",
                event = "refreshingAttrChanged",
                method = "getCustomValue")})*/


@SuppressLint("AppCompatCustomView")
public class TwoBindEditView extends EditText {

    private static final String TAG = "TwoBindEditView";
    private static InverseBindingListener mInverseBindingListener;


    public TwoBindEditView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //触发反向数据的传递
                if (mInverseBindingListener != null) {
                    mInverseBindingListener.onChange();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public TwoBindEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TwoBindEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Object getOldType() {
        return oldType;
    }

    public void setOldType(Object oldType) {
        this.oldType = oldType;
    }

    Object oldType;

/*    @BindingAdapter(value = "twotext", requireAll = false)
    public static void onSetValue(TwoBindEditView editView, Object progress) {
        if (progress == null) {
            editView.setText("");
        } else {
            editView.setOldType(progress);
            if (!editView.getText().toString().equals(progress.toString())) {
                editView.setText(String.valueOf(progress));
            }
        }

    }

    @BindingAdapter(value = "twotext", requireAll = false)
    public static void onSetValue(TwoBindEditView editView, int progress) {
        onSetValue(editView, progress + "");
    }*/
/*
    @BindingAdapter(value = "twotext", requireAll = false)
    public static void onSetValue(TwoBindEditView editView, long progress) {
        onSetValue(editView, progress + "");
    }

    @BindingAdapter(value = "twotext", requireAll = false)
    public static void onSetValue(TwoBindEditView editView, String progress) {
        if (progress == null) {
            editView.setText("");
        } else {
            editView.setOldType(progress);
            if (editView.getText().toString().equals(progress.toString())) {
                editView.setText(String.valueOf(progress));
            }
        }

    }*/

/*    @InverseBindingAdapter(attribute = "twotext", event = "twotextAttrChanged")

    public static int getInnerTextInt(TwoBindEditView textview) {
        return ParseUtils.parseInt(textview.getText().toString());
    }
    */


 /*   @InverseBindingAdapter(attribute = "twotext", event = "twotextAttrChanged")

    public static double getInnerTextDouble(TwoBindEditView textview) {
        return ParseUtils.parseDouble(textview.getText().toString());

    }

    @InverseBindingAdapter(attribute = "twotext", event = "twotextAttrChanged")

    public static long getInnerTextLong(TwoBindEditView textview) {
        return ParseUtils.parseLong(textview.getText().toString());

    }

    @InverseBindingAdapter(attribute = "twotext", event = "twotextAttrChanged")

    public static String getInnerTextStr(TwoBindEditView textview) {
        return textview.getText().toString();

    }*/

/*
    */

    /**
     * 反向解析 必须准确返回类型，否则不识别
     *
     * @param textview
     * @return
     *//*

    @InverseBindingAdapter(attribute = "twotext", event = "twotextAttrChanged")
    public static Object getInnerText(TwoBindEditView textview) {
        if (textview.getOldType() == null) {
            Log.e(TAG, EncryptUtilN.a7(new int[]{182,574,586,590,626,638,586,310,582,570,646,570,310,602,642,310,586,618,630,646,666}));
            return ParseUtils.parseInt(textview.getText().toString());
        } else if (textview.getOldType() instanceof Integer) {
            return ParseUtils.parseInt(textview.getText().toString());
        } else if (textview.getOldType() instanceof Long) {
            return ParseUtils.parseLong(textview.getText().toString());
        } else if (textview.getOldType() instanceof Float) {
            return ParseUtils.parseFloat(textview.getText().toString());
        } else if (textview.getOldType() instanceof Double) {
            return ParseUtils.parseDouble(textview.getText().toString());
        } else if (textview.getOldType() instanceof Boolean) {
            return ParseUtils.parseBoolean(textview.getText().toString());
        } else if (textview.getOldType() instanceof Short) {
            return ParseUtils.parseInt(textview.getText().toString());
        } else if (textview.getOldType() instanceof String) {
            return textview.getText().toString();
        } else {
            return null;
        }
    }
*/
/*    @BindingAdapter(value = {"twotextAttrChanged"}, requireAll = false)
    public static void setPhilProgressAttrChanged(TwoBindEditView twoBindEditView, InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            Log.e(EncryptUtilN.a7(new int[]{6681,159357,149973,267805}), EncryptUtilN.a7(new int[]{3149,3441,3589,3621,3553,3605,3609,3553,3413,3569,3589,3549,3569,3589,3561,3453,3569,3609,3613,3553,3589,3553,3605,83253,128565,3281}));
        } else {
            mInverseBindingListener = inverseBindingListener;
        }
    }*/



    //http://blog.csdn.net/zhangphil/article/details/77839555


}
