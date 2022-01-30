package cn.qssq666.robot.ide;
import cn.qssq666.CoreLibrary0;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.ide.interfaces.IDEApi;
import cn.qssq666.robot.plugin.js.util.JSUtil;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;
import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;

/**
 * Created by qssq on 2018/12/25 qssq666@foxmail.com
 */
public class JSEditorN extends CodeView implements IDEApi {

    private int language;

    public JSEditorN(@NotNull Context context) {
        super(context);
    }

    public JSEditorN(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setLang(int instance) {
//        codeView.setCode(, String.);
        this.language = instance;
    }

    @Override
    public void paste(String content) {

    }

    @Override
    protected void doInit(Context context, AttributeSet attributeSet) {
        super.doInit(context, attributeSet);
        this.setOptions(Options.Default.get(context)
                .withLanguage("javascript")
                .disableHighlightAnimation()

                .withTheme(ColorTheme.DEFAULT));

    }

    @Override
    public void setTextCode(String s) {

        this.setCode(s, "js");
    }

    @Override
    public String getTextCode() {
//        return codeView.ge;
        return getOptionsOrDefault().getCode();
    }

    @Override
    public void undo() {

    }


    @Override
    public void redo() {

    }

    @Override
    public void requestCodeFocus() {

    }


    @Override
    public void formatCode() {

        if (language == LanguageCode.JAVASCRIPT) {

            final ProgressDialog progressDialog = DialogUtils.getProgressDialog((Activity) getContext());
            progressDialog.setMessage("美化中");
            progressDialog.show();
            new QssqTaskFix<Void, Object>(new QssqTaskFix.ICallBackImp() {
                @Override
                public Object onRunBackgroundThread(Object[] params) {
                    String textCode = getTextCode();
                    try {
                        String string = JSUtil.formatCode(textCode);
                        return string;
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return e;
                    }
                }

                @Override
                public void onRunFinish(Object o) {
                    if (o instanceof Throwable) {
                        DialogUtils.showDialog(getContext(), "格式化失败!" + Log.getStackTraceString((Throwable) o));
                    } else {
                        setTextCode(o + "");

                    }
                    progressDialog.dismiss();
                }
            }).execute();
            return;
        } else {
            AppContext.showToast("暂不支持!");

        }
    }
}
