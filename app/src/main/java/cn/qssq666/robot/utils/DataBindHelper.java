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

package cn.qssq666.robot.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.enums.GAGTYPE;




public class DataBindHelper {





    @BindingAdapter({"kickduration"}) //ignore_include
    public static void setKickDuration(final TextView textView, GagAccountBean bean) {

//        app:text="@{`禁言/踢出倒计时时间:`+DateUtils.getGagTime(model.duration)}"
        if (bean.getAction() == GAGTYPE.GAG) {
            textView.setText("禁言时间:" + DateUtils.getGagTime(bean.getDuration()));
        } else {
            textView.setText("多少分钟后踢出:" + (bean.getDuration() == 0 ? "马上" : DateUtils.getGagTime(bean.getDuration())));
        }
    }

    @BindingAdapter({"kick"})  //ignore_include
    public static void setKickType(final TextView textView, int action) {

        textView.setText( MiscUtil.getGagAction(action));


    }



    //ignore_start


    @BindingAdapter(value = {"beforeTextChanged", "onTextChanged",
            "afterTextChanged", "textAttrChanged"}, requireAll = false)
    public static void setTextWatcher(TextView view, final TextViewBindingAdapter.BeforeTextChanged before,
                                      final TextViewBindingAdapter.OnTextChanged on, final TextViewBindingAdapter.AfterTextChanged after,
                                      final InverseBindingListener textAttrChanged) {

        final String text = view.getText().toString();
        final TextWatcher newValue;
        if (before == null && after == null && on == null && textAttrChanged == null) {
            newValue = null;
        } else {
            newValue = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (before != null) {
                        before.beforeTextChanged(s, start, count, after);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (text != null) {

                        Log.w(TAG, "text:" + text + ",s:" + s);
                    }

                    if (on != null) {
                        on.onTextChanged(s, start, before, count);
                    }


                    if (textAttrChanged != null) {
                        textAttrChanged.onChange();
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (after != null) {
                        after.afterTextChanged(s);
                    }
                }
            };
        }
        final TextWatcher oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher);
        if (oldValue != null) {
            view.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            view.addTextChangedListener(newValue);
        }
    }


    @BindingAdapter("android:text")
    public static void setText(TextView view, int value) {
        view.setText(Integer.toString(value));
    }


    @BindingAdapter("text")
    public static void setTextApp(TextView view, int value) {
        view.setText(Integer.toString(value));
    }

//    @InverseBindingAdapter(attribute = "android:text")

    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static int getText(TextView view) {
        return Integer.parseInt(view.getText().toString());
    }


    @BindingAdapter("android:text")
    public static void setTextLong(TextView view, long value) {
        view.setText(Long.toString(value));
    }

    @BindingAdapter("text")
    public static void setTextLongApp(TextView view, long value) {
        setTextLong(view, value);
    }


    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static long getTextLong(TextView view) {
        String s = view.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return 0;
        } else {

            return Long.parseLong(s);
        }
    }

    @InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
    public static long getTextLongApp(TextView view) {
        return getTextLong(view);
    }


    @InverseBindingAdapter(attribute = "text", event = "textAttrChanged")
    public static int getTextXX(TextView view) {

        String s = view.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        {

            try {
                return Integer.parseInt(s);

            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }


/*
    @InverseBindingAdapter(attribute = "text")
    public static int getTextFromApp(TextView view) {
        return getTextXX(view);
    }
*/


    @InverseBindingAdapter(attribute = "text")
    public static String getTextFromAppX(TextView view) {
        return view.getText().toString();//
    }


    /**
     * 解决预览的奇葩问题
     *
     * @param textView
     * @param text
     */
    @BindingAdapter({"text"})
    public static void setTextValue(final TextView textView, String text) {
        textView.setText(text);

    }

/*    @BindingAdapter({"text"})
    public static void setTextValue(final EditText textView, String text) {
        textView.setText(text);

    }*/


    @BindingAdapter(value = {"onCheckedChanged", "checkedAttrChanged"}, requireAll = false)
    public static void setListeners(CompoundButton view, final CompoundButton.OnCheckedChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener);
        } else {
            view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener != null) {
                        listener.onCheckedChanged(buttonView, isChecked);
                    }
                    attrChange.onChange();
                }
            });
        }
    }


    // ignore_start
    public final static String TAG = "DataBindHelper";


    @BindingAdapter({"drawableid"})
    public static void setImageResource(final ImageView imageView, @DrawableRes int resourcesId) {
        imageView.setImageResource(resourcesId);
    }


    @BindingAdapter({"textColor"})
    public static void setTextColor(TextView textView, int textColor) {
        textView.setTextColor(textColor);
    }


    /**
     * 这种写法会导致第二次进入的时候 还是使用了之前的信息。 一直走emptyValue，那么 是否是isEmpty判断错误导致的一系列问题呢？ 果然 卧槽 fuck
     *
     * @param textView
     * @param content
     * @param emptyValue
     */
    @SuppressWarnings("tip")
    @BindingAdapter({"content", "empty"})
//Error:(241) No resource identifier found for attribute 'error' in package 'com.buyao.tv' 如果 没有用@{}包括起来就会报错 这个
    public static void imageLoaderTest(final TextView textView, String content, String emptyValue) {
        if (TextUtils.isEmpty(content)) {
            textView.setText(emptyValue);
        } else {
            textView.setText(content);

        }
    }

    @BindingAdapter({"visibility"})
    public static void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
                /*
                   android:visibility="@{TextUtils.isEmpty(bean.image)?View.GONE:View.VISIBLE}"
                 */
    }


    @BindingAdapter({"src"})
    public static void imageLoader(final ImageView imageView, int resourceId) {
        imageView.setImageResource(resourceId);
    }


    @BindingAdapter({"background"})
    public static void setBackground(final ImageView imageView, Drawable resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(resourceId);
        } else {
            imageView.setBackgroundDrawable(resourceId);

        }
    }


    @BindingAdapter({"background"})
    public static void setBackgroundView(final View imageView, int resourceId) {
        imageView.setBackgroundColor(ContextCompat.getColor(AppContext.getInstance(), resourceId));
    }

    @BindingAdapter({"background"})


    public static void setBackground(final ImageView imageView, int resourceId) {
        imageView.setBackgroundResource(resourceId);
    }


    @BindingAdapter({"drawableLeft"})
    public static void imageLoader(final TextView textView, Drawable drawableLeft) {

        textView.setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft, null, null, null);
    }


    @BindingAdapter("paddingLeft")
    public static void setPaddingLeft(View view, int padding) {
        view.setPadding(padding,
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    @BindingAdapter({"src"})
    public static void imageLoader(final ImageView imageView, Drawable resourceId) {
        imageView.setImageDrawable(resourceId);
    }

/*    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:text")
    public static void setSrc(TextView view, int resId) {
        view.setText(resId);
    }*/

    @BindingConversion
    public static String convertDate(Date date) {
        /*
        用法:
          android:text="@{time}" 只要@{}变量是 这个日期类型类型的就会自动查找
         */
        java.text.SimpleDateFormat sdf = null;
        sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }


    private static boolean haveContentsChanged(CharSequence str1, CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            return true;
        } else if (str1 == null) {
            return false;
        }
        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }



    //ignore_end



}
