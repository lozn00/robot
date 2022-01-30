package cn.qssq666.robot.ui;
import cn.qssq666.CoreLibrary0;import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.qssq666.robot.R;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 */

public class NumberPerference extends Preference {
    int layoutResouce = R.layout.number_perference;
    private View mContentView;
    private EditText evText;
    private long mText;
    private AlertDialog mDialog;
    private TextView tv;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    private int minValue =0 ;
    public static final String TAG = "NumberPerference";

    public NumberPerference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutResource(layoutResouce);
    }

    public NumberPerference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumberPerference(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Long.parseLong(a.getString(index));
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return true;
    }


    @Override

    protected View onCreateView(ViewGroup parent) {
        mContentView = super.onCreateView(parent);
        Context context = getContext();


        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.view_edit, null, false);


        evText = (EditText) dialogView.findViewById(android.R.id.edit);

        evText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s)) {
                    mText = 0;

                    persistLong(mText);
//                    Log.w(TAG, "onTextChanged Value 3:" + mText);
                } else {
                    String valueStr = s.toString();
                    try {
                        mText = Long.parseLong(valueStr);

                    } catch (NumberFormatException e) {
                        mText = 100;
                    }
                    if (mText < minValue) {
                        mText = minValue;
                    }
//                    Log.w(TAG, "onTextChanged Value:" + mText + "  thisObject:" + this);
                    persistLong(mText);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context)

                .setTitle("请输入值")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


                    }
                }).setView(dialogView);
        ;


        mDialog = mBuilder.create();

//        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tv.setText(""+mText);

            }
        });
        tv = ((TextView) mContentView.findViewById(android.R.id.text1));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evText.setText(""+mText);
                mDialog.show();
            }
        });

//        (() getContext());

        return mContentView;
    }

    @Override
    public boolean shouldDisableDependents() {
        return mText == -1 | super.shouldDisableDependents();
    }

    public int getLayoutResouce() {
        return layoutResouce;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        Log.w(TAG, "defaultValue:" + defaultValue);
        long defaultValueFix = defaultValue == null ? getMinValue() : (Long) defaultValue;
        if (defaultValueFix < getMinValue()) {
            defaultValueFix = getMinValue();
        }
        setValue(restorePersistedValue ? getPersistedLong(mText) : defaultValueFix);

    }

    public void setValue(long longValue) {
        mText = longValue;
        final boolean changed = mText != longValue;
        if (changed || !mTextSet) {
            mText = longValue;
            mTextSet = true;
            persistLong(longValue);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        Log.w(TAG, "onBindView");
        if (tv != null) {
            tv.setText("" + mText);

        }


    }

    public long getText() {
        return mText;
    }

    private boolean mTextSet;
}
