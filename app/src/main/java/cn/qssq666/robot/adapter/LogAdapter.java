package cn.qssq666.robot.adapter;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import cn.qssq666.robot.BR;
import cn.qssq666.robot.R;
import cn.qssq666.robot.databinding.ViewItemLogBinding;
import cn.qssq666.robot.utils.TestUtil;

/**
 * Created by qssq on 2018/8/3 qssq666@foxmail.com
 */
public class LogAdapter extends DefaultAdapter<String> {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    @Override
    public void onBindViewHolderFix(BaseViewHolder holder, int position) {
        String value = getList().get(position);
        holder.getBinding().setVariable(BR.value, value);
        //    app:text="@{value}"
        int itemViewType = getItemViewType(position);
        if (type == TYPE_Verbose || itemViewType == type) {
            switch (itemViewType) {
                case TYPE_WARN:
                    value = TestUtil.warnYellowWrapFont(value, "");
                    break;
                case TYPE_ERROR:
                    value = TestUtil.redWrapFont(value, "");
                    break;
                case TYPE_DEBUG:
                    value = TestUtil.blueWrapFont(value, "");
                    break;
                case TYPE_INFO:
                    value = TestUtil.greenWrapFont(value, "");
                    break;
                case TYPE_Verbose:
                    value = TestUtil.blackWrapFont(value, "");
                    break;
                case TYPE_UNKNOWN:
                    value = TestUtil.grayWrapFont(value, "");
                    break;
                default:
                    value = TestUtil.qianYellowWrapFont(value, "");
                    break;

            }
            Spanned text = Html.fromHtml(value);
            ((ViewItemLogBinding) holder.getBinding()).tvLog.setText(text);

            ((ViewItemLogBinding) holder.getBinding()).tvLog.setVisibility(View.VISIBLE);
        } else {
            ((ViewItemLogBinding) holder.getBinding()).tvLog.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        DataBindingUtil.inflate(, , , )
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_item_log, parent, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        String s = getList().get(position);
        return getTypeByStr(s);
    }

    public static int getTypeByStr(String s) {
        int i;
        int start = 0;
        int count = 0;
        while ((i = s.indexOf("/", start)) != -1) {
            ;
            if (i == -1 || i == 0) {
                return TYPE_UNKNOWN;
            }
            String type = s.substring(i - 1, i);

//            if (type.matches("[WVDIE]")) {//某些日志司机。

            switch (type) {
                case "W":
                    return TYPE_WARN;
                case "V":
                    return TYPE_Verbose;
                case "D":
                    return TYPE_DEBUG;
                case "I":
                    return TYPE_INFO;
                case "E":
                    return TYPE_ERROR;
            }
//            }
            start = i;
            count++;
            if (count == 2) {
                return TYPE_Verbose;
            }

        }
        return TYPE_UNKNOWN;
    }

    public final static int TYPE_Verbose = 0;
    public final static int TYPE_DEBUG = 1;
    public final static int TYPE_INFO = 2;
    public final static int TYPE_WARN = 3;
    public final static int TYPE_ERROR = 4;
    public final static int TYPE_ASSERT = 5;
    public final static int TYPE_UNKNOWN = 6;


    public void setLogType(int type) {
        this.type = type;
        this.notifyDataSetChanged();
    }

    public String getTypeStr() {
        switch (type) {
            case TYPE_Verbose:
                return "无限制";
            case TYPE_DEBUG:
                return "debug类型";

            case TYPE_ERROR:
                return "error类型";
            case TYPE_INFO:
                return "info类型";
            case TYPE_WARN:
                return "警告类型";
            default:
                return "未知类型";


        }
    }
}
