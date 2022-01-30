package cn.qssq666.robot.ui;
import cn.qssq666.CoreLibrary0;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.ide.LanguageCode;
import cn.qssq666.robot.ide.interfaces.IDEApi;
import cn.qssq666.robot.ide.mycode.android.FreeScrollingTextField;
import cn.qssq666.robot.ide.mycode.common.ColorScheme;
import cn.qssq666.robot.ide.mycode.common.ColorSchemeDark;
import cn.qssq666.robot.ide.mycode.common.ColorSchemeLight;
import cn.qssq666.robot.ide.mycode.common.Document;
import cn.qssq666.robot.ide.mycode.common.DocumentProvider;
import cn.qssq666.robot.ide.mycode.common.Language;
import cn.qssq666.robot.ide.mycode.common.Lexer;
import cn.qssq666.robot.ide.mycode.common.language.LanguageJava;
import cn.qssq666.robot.ide.mycode.common.language.LanguageJavascript;
import cn.qssq666.robot.ide.mycode.common.language.LanguageLua;
import cn.qssq666.robot.ide.mycode.common.language.LanguageMarkDown;
import cn.qssq666.robot.ide.mycode.common.language.LanguageShell;
import cn.qssq666.robot.ide.mycode.common.language.LanguageSmali;
import cn.qssq666.robot.plugin.js.util.JSUtil;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;

public class CodeView extends FreeScrollingTextField implements IDEApi {

    private boolean firstSetText = true;

    public CodeView(Context context) {
        super(context);
        init();
    }

    public CodeView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        init();
    }

    public CodeView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        init();
    }

    private void init() {
        setLang(LanguageJavascript.getInstance());
        setFocusable(true);
        setZoom(2.2f);
        setAutoIndent(true);//自动排版??
//        setTypeface(Typeface.MONOSPACE);
//        setHorizontalScrollBarEnabled(true);
//        setWordWrap(true);
//        setChirality(false);//手势？
        setShowLineNumbers(true);
//		setColorScheme(new AideLikeColorScheme());
//        scrollTo(0, 0);


    }

    public DocumentProvider getDocumentProvider() {
        return createDocumentProvider();
    }


    @Override
    public void setLang(int instance) {
        switch (instance) {
            case LanguageCode.JAVA:
                Language instance1 = LanguageJava.getInstance();
                setLang(instance1);
                break;
            case LanguageCode.JAVASCRIPT:
                setLang(LanguageLua.getInstance());
                break;
            case LanguageCode.LUA:
                setLang(LanguageJavascript.getInstance());
                break;
            case LanguageCode.SMALI:
                setLang(LanguageSmali.getInstance());
                break;
            case LanguageCode.MARKDOWN:
                setLang(LanguageMarkDown.getInstance());
                break;
            case LanguageCode.SHELL:
                setLang(LanguageShell.getInstance());
                break;
        }

    }

    public void setTextCode(String text) {
        System.gc();
        Document doc = new Document(this);
        doc.insert(text.toCharArray(), 0, 0, false);

        DocumentProvider dp = new DocumentProvider(doc);
        setDocumentProvider(dp);

        if (!firstSetText) {
            setEdited(true);
        } else {
            setEdited(false);
        }

        scrollTo(0, 0);
    }

    public String getTextCode() {
        DocumentProvider dp = getDocumentProvider();

        CharSequence charSequence = dp.subSequence(0, dp.docLength() - 1);
        return charSequence + "";
    }

    public void setLang(Language l) {
        _autoCompletePanel.setLanguage(l);

        Lexer.setLanguage(l);
    }

/*
    public void addNames(String[] strArr) {
        LanguageLua languageLua = (LanguageLua) Lexer.getLanguage();
        Object names = languageLua.getNames();
        Object obj = new String[(names.length + strArr.length)];
        System.arraycopy(names, 0, obj, 0, names.length);
        System.arraycopy(strArr, 0, obj, names.length, strArr.length);
        languageLua.setNames(obj);
        Lexer.setLanguage(languageLua);
        respan();
        invalidate();
    }*/

   /* public boolean findNext(String str) {
        if (!str.equals(this.mKeyword)) {
            this.mKeyword = str;
            this.idx = 0;
        }
        this.finder = new LinearSearchStrategy();
        String str2 = this.mKeyword;
        if (str2.isEmpty()) {
            selectText(false);
            return false;
        }
        this.idx = this.finder.find(getText(), str2, this.idx, getText().length(), false, false);
        if (this.idx == -1) {
            selectText(false);
            Toast.makeText(this.mContext, "未找到", 500).show();
            this.idx = 0;
            return false;
        }
        setSelection(this.idx, this.mKeyword.length());
        this.idx += this.mKeyword.length();
        moveCaret(this.idx);
        return true;
    }*/

    public String getSelectedText() {
        return this._hDoc.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
    }
/*
    public DocumentProvider getText() {
        return createDocumentProvider();
    }*/

    public void gotoLine() {
//        startGotoMode();
    }

    public void gotoLine(int i) {
        if (i > this._hDoc.getRowCount()) {
            i = this._hDoc.getRowCount();
        }
        setSelection(_hDoc.getLineOffset(i - 1), 1);
//        getLin
//        setSelection(getText()).getLineOffset(i - 1));
    }

    public void insert(int position, String str) {
        selectText(false);
        moveCaretLeft();
        moveCaret(position);//移动符号
        paste(str);
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState() & -28673)) {
            switch (i) {
                case 29:
                    selectAll();
                    return true;
                case 31:
                    copy();
                    return true;
                case Lexer.SINGLE_SYMBOL_DELIMITED_A /*50*/:
                    paste();
                    return true;
                case 52:
                    cut();
                    return true;
            }
        }
        return super.onKeyShortcut(i, keyEvent);
    }

    /*   protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
           super.onLayout(z, i, i2, i3, i4);
           if (this._index != 0 && i3 > 0) {
               moveCaret(this._index);
               this._index = 0;
           }
       }

   */
    public void redo() {
        int redo = createDocumentProvider().redo();
        if (redo >= 0) {
            setEdited(true);
            respan();
            selectText(false);
            moveCaret(redo);
            invalidate();
        }
    }

    @Override
    public void requestCodeFocus() {
       /* if (this.hasFocus()) {
            this.requestFocus();
            focusCaret();

        }*/
        if (!makeCharVisible(_caretPosition)) {
            focusCaret();

        }
    }


    public void search() {
//        startFindMode();
    }

    public void setBackgoudColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.BACKGROUND, i);
    }
/*
    public void setBasewordColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.NAME, i);
    }*/

    public void setCommentColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.COMMENT, i);
    }

    public void setDark(boolean z) {
        if (z) {
            setColorScheme(new ColorSchemeDark());
        } else {
            setColorScheme(new ColorSchemeLight());
        }
    }

    public void setKeywordColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.KEYWORD, i);
    }

    public void setPanelBackgroundColor(int i) {
        this._autoCompletePanel.setBackgroundColor(i);
    }

    public void setPanelTextColor(int i) {
        this._autoCompletePanel.setTextColor(i);
    }

/*    public void setSelection(int i) {
        selectText(false);
        if (hasLayout()) {
            this._index = i;
        } else {
            moveCaret(i);
        }
    }*/

    public void setStringColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.STRING, i);
    }
/*
    public void setText(CharSequence charSequence) {
        Document document = new Document(this);
        document.setWordWrap(this._isWordWrap);
        document.setText(charSequence);
        setDocumentProvider(new DocumentProvider(document));
    }*/

    public void setText(CharSequence charSequence, boolean z) {
        replaceText(0, getLength() - 1, charSequence.toString());
    }

    public void setTextColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.FOREGROUND, i);
    }

    public void setTextHighlightColor(int i) {
        getColorScheme().setColor(ColorScheme.Colorable.SELECTION_BACKGROUND, i);
    }

    public void setUserwordColor(int i) {
//        getColorScheme().setColor(ColorScheme.Colorable.LITERAL, i);
    }

 /*   public void setWordWrap(boolean z) {
        this._isWordWrap = z;
        super.setWordWrap(z);
    }

    public void startFindMode() {
        startActionMode(new Callback() {
            private EditText edit;
            private LinearSearchStrategy finder;
            private int idx;

            private void findNext() {
                this.finder = new LinearSearchStrategy();
                String obj = this.edit.getText().toString();
                if (obj.isEmpty()) {
                    LuaEditor.this.selectText(false);
                    return;
                }
                this.idx = this.finder.find(LuaEditor.this.getText(), obj, this.idx, LuaEditor.this.getText().length(), false, false);
                if (this.idx == -1) {
                    LuaEditor.this.selectText(false);
                    Toast.makeText(LuaEditor.this.mContext, "未找到", 500).show();
                    this.idx = 0;
                    return;
                }
                LuaEditor.this.setSelection(this.idx, this.edit.getText().length());
                this.idx += this.edit.getText().length();
                LuaEditor.this.moveCaret(this.idx);
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 2:
                        findNext();
                        break;
                }
                return false;
            }

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle("搜索");
                actionMode.setSubtitle(null);
                this.edit = new EditText(LuaEditor.this.mContext) {
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (charSequence.length() > 0) {
                            AnonymousClass2.this.idx = 0;
                            AnonymousClass2.this.findNext();
                        }
                    }
                };
                this.edit.setSingleLine(true);
                this.edit.setImeOptions(3);
                this.edit.setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        AnonymousClass2.this.findNext();
                        return true;
                    }
                });
                this.edit.setLayoutParams(new LayoutParams(LuaEditor.this.getWidth() / 3, -1));
                menu.add(0, 1, 0, BuildConfig.FLAVOR).setActionView(this.edit);
                menu.add(0, 2, 0, LuaEditor.this.mContext.getString(17039372));
                this.edit.requestFocus();
                return true;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
        });
    }

    public void startGotoMode() {
        startActionMode(new Callback() {
            private EditText edit;
            private int idx;

            private void _gotoLine() {
                String obj = this.edit.getText().toString();
                if (!obj.isEmpty()) {
                    int intValue = Integer.valueOf(obj).intValue();
                    if (intValue > LuaEditor.this._hDoc.getRowCount()) {
                        intValue = LuaEditor.this._hDoc.getRowCount();
                    }
                    LuaEditor.this.gotoLine(intValue);
                }
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 2:
                        _gotoLine();
                        break;
                }
                return false;
            }

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle("转到");
                actionMode.setSubtitle(null);
                this.edit = new EditText(LuaEditor.this.mContext) {
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (charSequence.length() > 0) {
                            AnonymousClass1.this.idx = 0;
                            AnonymousClass1.this._gotoLine();
                        }
                    }
                };
                this.edit.setSingleLine(true);
                this.edit.setInputType(2);
                this.edit.setImeOptions(2);
                this.edit.setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        AnonymousClass1.this._gotoLine();
                        return true;
                    }
                });
                this.edit.setLayoutParams(new LayoutParams(LuaEditor.this.getWidth() / 3, -1));
                menu.add(0, 1, 0, BuildConfig.FLAVOR).setActionView(this.edit);
                menu.add(0, 2, 0, LuaEditor.this.mContext.getString(17039370));
                this.edit.requestFocus();
                return true;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
        });
    }*/

    @Override
    public void undo() {
        int undo = createDocumentProvider().undo();
        if (undo >= 0) {
            setEdited(true);
            respan();
            selectText(false);
            moveCaret(undo);
            invalidate();
        }
    }

    @Override
    public void formatCode() {

        if (cn.qssq666.robot.ide.mycode.common.Lexer.getLanguage() instanceof LanguageJavascript) {

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



  /*  @Override
    public void showIME(boolean show) {
        if (viewmode()) {
            return;
        }

        super.showIME(!show);
    }

    public void setViewOnly(boolean on) {
        super.viewmode(on);
    }*/

}
