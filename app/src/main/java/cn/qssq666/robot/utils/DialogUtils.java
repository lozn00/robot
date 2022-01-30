package cn.qssq666.robot.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.interfaces.INotify;

/**
 * Created by luozheng on 15/12/8.
 */
public class DialogUtils {
    public static void delaydismissDialog(final Dialog dialog, String message) {
        delaydismissDialog(dialog, message, 1500);
    }

    /**
     * 延迟关闭对话框 也就是说延时小时一个标题 如果是未知对话框 那么 message信息不进行操作
     *
     * @param dialog
     * @param message  支持 的对话框 设置 有  @see {@link ProgressDialog,AlertDialog}
     * @param duration
     */
    public static void delaydismissDialog(final Dialog dialog, String message, long duration) {
        //SingleChoiceDialog
        if (dialog instanceof ProgressDialog) {
            ((ProgressDialog) dialog).setMessage("" + message);
        } else if (dialog instanceof AlertDialog) {
            ((AlertDialog) dialog).setMessage("" + message);
        }
       /* else if (dialog instanceof SingleChoiceDialog) {
            ((SingleChoiceDialog) dialog).setMessage("" + message);
        }*/
        AppContext.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, duration);

    }

    public static ProgressDialog getProgressDialog(Activity activity, String title, String content, boolean canncelable, DialogInterface.OnCancelListener onCancelListener) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setCanceledOnTouchOutside(canncelable);
        progressDialog.setCancelable(canncelable);
        progressDialog.setOnCancelListener(onCancelListener);
        progressDialog.setTitle(title == null ? "温馨提示" : title);
        progressDialog.setMessage((content == null ? "加载中" : content));
        return progressDialog;
    }

    public static ProgressDialog getProgressDialog(Activity activity, String content, boolean canncelable, DialogInterface.OnCancelListener onCancelListener) {
        return getProgressDialog(activity, null, content, canncelable, onCancelListener);
    }

    /**
     * 默认不能取消
     *
     * @param activity
     * @param content
     * @param onCancelListener
     * @return
     */
    public static ProgressDialog getProgressDialog(Activity activity, String content, DialogInterface.OnCancelListener onCancelListener) {
        return getProgressDialog(activity, null, content, false, onCancelListener);
    }

    public static ProgressDialog getProgressDialog(Activity activity, String content) {
        return getProgressDialog(activity, null, content, false, null);
    }

    /**
     * 默认不能取消
     *
     * @param activity
     * @return
     */
    public static ProgressDialog getProgressDialog(Activity activity) {
        return getProgressDialog(activity, null, null, false, null);
    }

    /**
     * @param dialog
     * @param close  能dis不? true表示不饿能够 也是
     */
    public static void modifyDialogAttr(DialogInterface dialog, boolean close) {
        try {
            //不关闭
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, close);//false 则是不关闭
//            if(close){
//                dialog.cancel();
//            }


        } catch (Exception e) {
        }


    }


    public static void showToast(Context context, String str) {
        showToast(context, str, Toast.LENGTH_SHORT);
    }

    public static void showToast(String str) {
        showToast(AppContext.getInstance(), str, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String str) {
        showToast(AppContext.getInstance(), str, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String str, int value) {
        Toast.makeText(context, str, value).show();
    }

    public static AlertDialog showDialog(Context context, String content) {
        return showDialog(context, content, null);
    }

    public static AlertDialog showDialog(Context context, String content, String title) {
        return showDialog(context, content, title, null);
    }

    public static AlertDialog showEditDialog(Context context, String title, List<String> tipList, List defaultValueList, int editCount, boolean isSystemDialog, final INotify<List<String>> onClickListener) {
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < editCount; i++) {
            final EditText editText = new EditText(context);
            editText.setTextColor(Color.parseColor("#000000"));
            if (tipList != null && i < tipList.size()) {
                editText.setHint(tipList.get(i));
                editText.setHintTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
            if (defaultValueList != null && i < defaultValueList.size()) {
                Object text = defaultValueList.get(i);
                if (text == null) {

                } else {

                    if (text instanceof String) {
                    } else if (text instanceof Integer || text instanceof Short) {
                        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
//                        editText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
                    } else if (text instanceof Double || text instanceof Float) {
//                        editText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                    }

                    editText.setText(text + "");

                }


            }
            linearLayout.addView(editText);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(linearLayout);
        builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modifyDialogAttr(dialog, true);
                dialog.dismiss();
                ArrayList<String> strings = new ArrayList<String>();
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    EditText tv = (EditText) linearLayout.getChildAt(i);
                    strings.add(tv.getText().toString());
                }

                onClickListener.onNotify(strings);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modifyDialogAttr(dialog, true);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        if (isSystemDialog) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
        modifyDialogAttr(dialog, false);
        return dialog;
    }


    public static AlertDialog showEditDialog(Context context, String content, String title, final INotify<String> onClickListener) {
        return showEditDialog(context, content, title, "", false, false, onClickListener);
    }

    public static AlertDialog showEditDialog(Context context, String content, String title, String defaultValue, final INotify<String> onClickListener) {
        return showEditDialog(context, content, title, defaultValue, false, false, onClickListener);
    }


    public static AlertDialog showEditDialog(Context context, String content, String title, String defaultValue, final boolean allowEmpty, boolean isSystemDialog, final INotify<String> onClickListener) {
        final EditText editText = new EditText(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            editText.setTextAppearance(R.style.editStyle);
        }


        editText.setTextColor(Color.parseColor("#ff0000"));
        if (defaultValue != null) {
            editText.setText(defaultValue);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, dialogBtnStyle);
        builder.setView(editText);
        if (title != null) {
            builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        }
        builder.setMessage(content);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!allowEmpty && TextUtils.isEmpty(editText.getText().toString())) {
                    return;
                }
                modifyDialogAttr(dialog, true);
                dialog.dismiss();
                onClickListener.onNotify(editText.getText().toString());
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modifyDialogAttr(dialog, true);
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        if (isSystemDialog) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
        modifyDialogAttr(dialog, false);
        return dialog;
    }


    public static void showSystemDialog(Context context, String content, String title, DialogInterface.OnClickListener onClickListener) {
//        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(content);
        builder.setNegativeButton("确定", onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
//        WindowManager.LayoutParams para = new WindowManager.LayoutParams();
//        para.height = -1;
//        para.width = -1;
//        para.format = 1;
//        para.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        para.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().getDecorView().setLayoutParams(para);
//        builder.updateTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
//        builder.setIcon(R.drawable.ic_launcher);
//        builder.setMessage(content);
//        builder.setNegativeButton("确定", onClickListener);
//        builder.createAndShow();
    }

    public static AlertDialog showDialog(Context context, String content, String title, final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(content);
        builder.setNegativeButton("确定", null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onClickListener != null) {
                        onClickListener.onClick(dialog, 0);

                    }
                }
            });
        } else {
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (onClickListener != null) {
                        onClickListener.onClick(dialog, 0);

                    }
                }
            });
        }
        AlertDialog show = builder.show();
        return show;
    }


    public static AlertDialog showCustromHtmlDialog(Context context, String content, String title, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        builder.setIcon(R.mipmap.ic_launcher);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_html, null, false);
        TextView tvTitle = (TextView) inflate.findViewById(android.R.id.title);
        if (title != null) {
            tvTitle.setText(title);
        }
        TextView viewById = (TextView) inflate.findViewById(android.R.id.text1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewById.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            viewById.setText(Html.fromHtml(content));

        }
        builder.setView(inflate);
//        builder.setMessage(content);
        builder.setNegativeButton("确定", onClickListener);
        AlertDialog show = builder.show();
        return show;
    }

    /**
     * 只有确定回调的对话框
     *
     * @param context
     * @param content
     * @param title
     * @param iNotify
     */
    public static AlertDialog showConfirmDialog(Context context, String content, String title, final INotify<Void> iNotify) {
        return showConfirmDialog(context, content, title, iNotify, null);
    }

    /**
     * @param context
     * @param content
     * @param yesiNotify 确定按钮的回调
     */
    public static AlertDialog showConfirmDialog(Context context, String content, final INotify<Void> yesiNotify) {
        return showConfirmDialog(context, content, "", yesiNotify);
    }

    /**
     * @param context
     * @param content
     */
    public static AlertDialog showConfirmDialog(Context context, String content, final INotify<Void> yesiNotify, final INotify<Void> noiNotify) {
        return showConfirmDialog(context, content, null, yesiNotify, noiNotify);
    }

    /**
     * @param context
     * @param content
     * @param title      标题为null则为温馨提示 为 “"为信息
     * @param yesiNotify 确定回调
     * @param iNotify    取消回调
     */
    public static AlertDialog showConfirmDialog(Context context, String content, String title, final INotify<Void> yesiNotify, final INotify<Void> iNotify) {
        AlertDialog yesOrNoDialog = getYesOrNoDialog(context, content, title, yesiNotify, iNotify);

        yesOrNoDialog.show();
        return yesOrNoDialog;
    }

    public static AlertDialog showConfirmDialog(Context context, String content, String yesBtn, String noBtn, final INotify<Void> yesiNotify) {
        AlertDialog yesOrNoDialog = getYesOrNoDialog(context, content, null, yesBtn, noBtn, yesiNotify, null);
        yesOrNoDialog.show();
        return yesOrNoDialog;
    }

    public static AlertDialog showConfirmDialog(Context context, String content, String yesBtn, String noBtn, final INotify<Void> yesiNotify, final INotify<Void> notify) {
        AlertDialog yesOrNoDialog = getYesOrNoDialog(context, content, null, yesBtn, noBtn, yesiNotify, notify);
        yesOrNoDialog.show();
        return yesOrNoDialog;
    }

    public static void showYesOrSystemNoDialog(Context context, String content, final INotify<Void> yesiNotify, final INotify<Void> noiNotify) {
        AlertDialog yesOrNoDialog = getYesOrNoDialog(context, content, null, yesiNotify, noiNotify);
        yesOrNoDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        yesOrNoDialog.show();
    }


    public static AlertDialog getYesOrNoDialog(Context context, String content, String title, final INotify<Void> yesiNotify, final INotify<Void> noiNotify) {
        return getYesOrNoDialog(context, content, title, null, null, yesiNotify, noiNotify);

    }

    public static AlertDialog getYesOrNoDialog(Context context, String content, String title, String yesBtn, String noBtn, final INotify<Void> yesiNotify, final INotify<Void> noiNotify) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(TextUtils.isEmpty(title) || "".equals(title) ? "温馨提示" : title + "");
//        builder.updateTitle(TextUtils.isEmpty(title) || "".equals(title) ? AppContext.getInstance().getString(R.string.Prompt)  : title + "");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(content);
        builder.setPositiveButton(noBtn == null ? AppContext.getInstance().getResources().getString(android.R.string.cancel) : noBtn, noiNotify == null ? null : new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                noiNotify.onNotify(null);
            }
        });
        builder.setNegativeButton(yesBtn == null ? AppContext.getInstance().getResources().getString(android.R.string.ok) : yesBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yesiNotify.onNotify(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;

    }

    public static AlertDialog showMenuDialog(Context context, String title, String[] items, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, clickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    /*  *//**
     * 随机产生订单号
     *
     * @return
     *//*
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }*/

}
