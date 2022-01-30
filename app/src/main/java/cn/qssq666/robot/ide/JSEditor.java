package cn.qssq666.robot.ide;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;

import com.myopicmobile.textwarrior.android.FreeScrollingTextField;
import com.myopicmobile.textwarrior.common.ColorScheme;
import com.myopicmobile.textwarrior.common.ColorSchemeDark;
import com.myopicmobile.textwarrior.common.ColorSchemeLight;
import com.myopicmobile.textwarrior.common.Document;
import com.myopicmobile.textwarrior.common.DocumentProvider;
import com.myopicmobile.textwarrior.common.Language;
import com.myopicmobile.textwarrior.common.LanguageJavascript;
import com.myopicmobile.textwarrior.common.LanguageNonProg;
import com.myopicmobile.textwarrior.common.Lexer;
import com.myopicmobile.textwarrior.common.ReadThread;
import com.myopicmobile.textwarrior.common.ThemeLanguage;
import com.myopicmobile.textwarrior.common.To;
import com.myopicmobile.textwarrior.common.WriteThread;
import com.myopicmobile.textwarrior.language.AndroidLanguage;

import java.io.File;

import cn.qssq666.CoreLibrary0;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.ide.interfaces.IDEApi;
import cn.qssq666.robot.plugin.js.util.JSUtil;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;

/**
 * Created by Administrator on 2017/11/19.
 */

public class JSEditor extends FreeScrollingTextField implements IDEApi {
    private boolean _isWordWrap;
    private String _lastSelectFile;
    private FunctionCallBACK callBACK;
    private Document _inputtingDoc;
    private int _index;
    private To to;

    public JSEditor(Context context) {
        this(context, null);
    }

    public JSEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.MONOSPACE);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, BASE_TEXT_SIZE_PIXELS, dm);
        setTextSize((int) size);
        Lexer.setLanguage(ThemeLanguage.getInstance());
        setShowLineNumbers(true);
        setHorizontalScrollBarEnabled(true);
        setHighlightCurrentRow(true);
        setWordWrap(false);
        setAutoIndentWidth(2);

        setTextColor(Color.BLACK);
    }

    public void setLanguage(Language language) {
        Lexer.setLanguage(language);
        _autoCompletePanel.setLanguage(Lexer.getLanguage());
    }

    public JSEditor addBasePackage(String key, String[] values) {
        Language languageJavascript = Lexer.getLanguage();
        languageJavascript.addBasePackage(key, values);
        return this;
    }

    public JSEditor addUserWord(String key) {
        Language languageJavascript = Lexer.getLanguage();
        languageJavascript.addUserWord(key);
        return this;
    }

    public JSEditor updateUserWord() {
        Language languageJavascript = Lexer.getLanguage();
        languageJavascript.updateUserWord();
        return this;
    }

    public JSEditor setDark(boolean isDark) {
        if (isDark)
            setColorScheme(new ColorSchemeDark());
        else
            setColorScheme(new ColorSchemeLight());
        return this;
    }

    public void setKeywordColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.KEYWORD, color);
    }

    public char[] getOperators() {
        return Lexer.getLanguage().getOperatorlist();
    }

    public void setStringColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.STRING, color);
    }

    public void setCommentColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.COMMENT, color);
    }

    public void setBackgroundColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.BACKGROUND, color);
    }

    public void setTextColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.FOREGROUND, color);
    }

    public void setTextHighlightColor(int color) {
        getColorScheme().setColor(ColorScheme.Colorable.SELECTION_BACKGROUND, color);
    }

    public String getSelectedText() {
        // TODO: Implement this method
        return _hDoc.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
    }


    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        final int filteredMetaState = event.getMetaState() & ~KeyEvent.META_CTRL_MASK;
        if (KeyEvent.metaStateHasNoModifiers(filteredMetaState)) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    selectAll();
                    return true;
                case KeyEvent.KEYCODE_X:
                    cut();
                    return true;
                case KeyEvent.KEYCODE_C:
                    copy();
                    return true;
                case KeyEvent.KEYCODE_V:
                    paste();
                    return true;
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public void setWordWrap(boolean enable) {
        // TODO: Implement this method
        _isWordWrap = enable;
        super.setWordWrap(enable);
    }


    public DocumentProvider getText() {
        return createDocumentProvider();
    }

    public void insert(int idx, String text) {
        selectText(false);
        moveCaret(idx);
        paste(text);
    }

    public void setText(CharSequence c, boolean isRep) {
        replaceText(0, getLength() - 1, c.toString());
    }

    public void setText(CharSequence c) {
        Document doc = new Document(this);
        doc.setWordWrap(_isWordWrap);
        doc.setText(c);
        setDocumentProvider(new DocumentProvider(doc));
    }

    public void setSelection(int index) {
        selectText(false);
        if (!hasLayout())
            moveCaret(index);
        else
            _index = index;
    }

    @Override
    public void undo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.undo();

        if (newPosition >= 0) {
            //TODO editor.setEdited(false); if reached original condition of file
            setEdited(true);

            respan();
            selectText(false);


            moveCaret(newPosition);
            invalidate();
        }

    }

    @Override
    public void formatCode() {
        if (Lexer.getLanguage() instanceof LanguageJavascript) {


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

    @Override
    public void redo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.redo();

        if (newPosition >= 0) {
            setEdited(true);

            respan();
            selectText(false);
            moveCaret(newPosition);
            invalidate();
        }
    }

    @Override
    public void requestCodeFocus() {
//        if(!this.hasFocus()){
//            this.requestFocus();
         if (!makeCharVisible(_caretPosition)) {
            focusCaret();

         }
//        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO: Implement this method
        super.onLayout(changed, left, top, right, bottom);
        if (_index != 0 && right > 0) {
            moveCaret(_index);
            _index = 0;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ReadThread.MSG_READ_OK:
                    setTextCode(msg.obj.toString());
                    if (callBACK != null) callBACK.T("open_ok");//打开成功
                    break;
                case ReadThread.MSG_READ_FAIL:
                    if (callBACK != null) callBACK.T("open_fail");//打开失败
                    break;
                case WriteThread.MSG_WRITE_OK:
                    if (callBACK != null) callBACK.T("save_ok");//保存成功
                    if (to != null) {
                        to.T("ok");
                    }
                    break;
                case WriteThread.MSG_WRITE_FAIL:
                    if (callBACK != null) callBACK.T("save_fail");//保存失败
                    if (to != null) {
                        to.T("fail");
                    }
                    break;
            }
        }
    };

    public void open(String filename) {
        open(filename, null);
    }

    public void open(String filename, To to) {
        if (to != null) {
            if (isEdited()) {
                to.T("fail");
                return;
            }
        }
        _lastSelectFile = filename;

        File inputFile = new File(filename);
        _inputtingDoc = new Document(this);
        _inputtingDoc.setWordWrap(this.isWordWrap());
        ReadThread readThread = new ReadThread(inputFile.getAbsolutePath(), handler, to);
        readThread.start();

    }

    public void setCallback(FunctionCallBACK callback) {
        callBACK = callback;
    }

    /**
     * 保存文件
     * * @param file
     */
    public void save(String file) {
        save(file, null);
    }

    /**
     * 保存文件
     * * @param file
     */
    public void save(String file, To toBeContinue) {
        this.to = toBeContinue;
        WriteThread writeThread = new WriteThread(getTextCode().toString(), file, handler);
        writeThread.start();
    }

    public File getOpenedFile() {
        if (_lastSelectFile != null)
            return new File(_lastSelectFile);

        return null;
    }

    public void setOpenedFile(String file) {
        _lastSelectFile = file;
    }

    public void setLang(Language instance) {
        com.myopicmobile.textwarrior.common.Lexer.setLanguage(instance);
//          Lexer.setLanguage(ThemeLanguage.getInstance());
    }

    public String getTextCode() {

        DocumentProvider dp = getText();

        CharSequence charSequence = dp.subSequence(0, dp.docLength() - 1);
        return charSequence + "";
    }

    @Override
    public void setLang(int instance) {
        setLang(LanguageJavascript.getInstance());
        if (true) {
            return;
        }
        switch (instance) {
            case LanguageCode.JAVA:
                setLang(AndroidLanguage.getInstance());
                break;
            case LanguageCode.JAVASCRIPT:
                setLang(LanguageJavascript.getInstance());
                break;
            default:
                setLang(LanguageNonProg.getInstance());
                break;

        }
    }

    @Override
    public void setTextCode(String text) {

        Document doc = new Document(this);
        doc.insert(text.toCharArray(), 0, 0, false);

        DocumentProvider dp = new DocumentProvider(doc);
        setDocumentProvider(dp);

//        if (!firstSetText) {
        setEdited(true);
      /*  } else {
            setEdited(false);
        }*/


        scrollTo(0, 0);
    }
}
