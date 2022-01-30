package com.myopicmobile.textwarrior.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingsatuo.editor.R;
import com.myopicmobile.textwarrior.common.ProjectAutoTip;
import com.myopicmobile.textwarrior.language.AndroidLanguage;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class AutoCompleteView {
    public static final int TYPE_KEYWORD = 1;
    public static final int TYPE_FUNCTION = 2;
    public static final int TYPE_VAR = 3;
    public static final int Android = 4;
    public static final int JSProject = 5;
    public static final int JSVALUE = 6;
    public int type;
    public String content;
    public Object data;
    public static String name;

    public AutoCompleteView(int type) {
        this(type, null, null);
    }

    public AutoCompleteView(int type, String content) {
        this(type, content, null);
    }

    public AutoCompleteView(int type, String content, Object data) {
        this.type = type;
        this.content = content;
        this.data = data;
    }

    public View getView(Context cx) {
        switch (type) {
            case TYPE_KEYWORD: {
                if (content == null) return null;
                LinearLayout s = new LinearLayout(cx);
                int dp = dip2px(cx, 15);
                s.setPadding(dp, dp, dp, dp);
                TextView t = new TextView(cx);
                t.setText(content);
                t.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                t.setTextColor(Color.RED);
                s.addView(t);
                s.setTag(content);
                return s;
            }
            case TYPE_FUNCTION: {
                if (content == null) return null;
                LinearLayout layout = new LinearLayout(cx);
                final int dp = dip2px(cx, 12);
                layout.setPadding(dp, dp, dp, dp);
                final ImageView iv = new ImageView(cx);
                iv.setImageResource(R.drawable.function);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dip2px(cx, 24), 0);
                p.height = p.width;
                p.rightMargin = dp;
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.addView(iv, p);
                TextView tv = new TextView(cx);
                tv.setText(content);
                tv.setTextColor(Color.RED);
                layout.addView(tv);
                layout.setTag(content + "(");
                return layout;
            }
            case TYPE_VAR: {
                if (content == null) return null;
                LinearLayout layout = new LinearLayout(cx);
                final int dp = dip2px(cx, 12);
                layout.setPadding(dp, dp, dp, dp);
                final ImageView iv = new ImageView(cx);
                iv.setImageResource(R.drawable.variety);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dip2px(cx, 24), 0);
                p.height = p.width;
                p.rightMargin = dp;
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.addView(iv, p);
                TextView tv = new TextView(cx);
                tv.setText(content);
                tv.setTextColor(Color.RED);
                layout.addView(tv);
                layout.setTag(content);
                return layout;
            }
            case Android: {
                if (content == null) return null;
                LinearLayout layout = new LinearLayout(cx);
                final int dp = dip2px(cx, 12);
                layout.setPadding(dp, dp, dp, dp);
                final ImageView iv = new ImageView(cx);
                iv.setImageResource(R.drawable.function);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dip2px(cx, 24), 0);
                p.height = p.width;
                p.rightMargin = dp;
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.addView(iv, p);
                TextView tv = new TextView(cx);
                tv.setText(content);
                tv.setTextColor(Color.parseColor("#FF818080"));
                layout.addView(tv);
                layout.setTag(getFunction(content));
                return layout;
            }
            case JSProject: {
                if (content == null) return null;
                LinearLayout s = new LinearLayout(cx);
                int dp = dip2px(cx, 15);
                s.setPadding(dp, dp, dp, dp);
                TextView t = new TextView(cx);
                t.setText(content);
                t.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                t.setTextColor(Color.WHITE);
                s.addView(t);
                s.setTag(getTag());
                return s;
            }
            case JSVALUE: {
                if (content == null) return null;
                LinearLayout layout = new LinearLayout(cx);
                final int dp = dip2px(cx, 12);
                layout.setPadding(dp, dp, dp, dp);
                final ImageView iv = new ImageView(cx);
                iv.setImageResource(R.drawable.function);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dip2px(cx, 24), 0);
                p.height = p.width;
                p.rightMargin = dp;
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.addView(iv, p);
                TextView tv = new TextView(cx);
                tv.setText(content);
                tv.setTextColor(Color.parseColor("#FF818080"));
                layout.addView(tv);
                layout.setTag(getJSvalue(content));
                return layout;
            }
        }
        return null;
    }

    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    private String getFunction(String function) {
        if (function.equals("NewOne")){
            return "NewOne(\"TAG\")\n   .with(\"name\",\"value\")\n    .start()";
        }
        String back = function;
        for (String s: AndroidLanguage.funtions){
            if (s.equals(function))return function+"(";
        }
        String paraName=" ";
        try {
            Class clazz = Class.forName("com.myopicmobile.textwarrior.android.AutoActivityClass");
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();//获取方法名字
                if (function.equals(methodName)){
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    back = "function "+function+" (";
                    int dex = 0;
                    for (Class<?> clas : parameterTypes) {
                        String[] parameterName = clas.getName().split("\\.");
                        paraName+=("par"+dex+parameterName[parameterName.length-1]+",");
                        dex++;
                    }
                    if (function.equals("onCreate"))paraName="savedInstanceState";
                    if (paraName.charAt(paraName.length()-1)==',')
                    paraName = paraName.substring(0,paraName.length()-1);
                    back+=paraName+")";
                    Type type=method.getGenericReturnType();
                    if (!type.toString().equals("void")){
                        if (paraName.length()>1)
                        back+=("{\n // TUDO......\n    return activity.Msuper(\""+function+"\","+paraName+")\n}");
                        else{
                            back+=("{\n // TUDO......\n    return activity.Msuper(\""+function+"\",null"+")\n}");
                        }
                    }else{
                        if (function.equals("onCreate")){
                            back += ("{\n // TUDO......\n}");
                        }else {
                            if (paraName.length() > 1)
                                back += ("{\n // TUDO......\n    activity.Msuper(\"" + function + "\"," + paraName + ")\n}");
                            else {
                                back += ("{\n // TUDO......\n    activity.Msuper(\"" + function + "\"" + ")\n}");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return back;
        }
    }
    public String getTag(){
        for (int a = 0 ; a < ProjectAutoTip.getSize(name);a ++) {
            ProjectAutoTip.Tip tip = ProjectAutoTip.get(name,a);
            if (content.equals(tip.name)){
                return tip.value.equals("")?content:tip.value;
            }
        }
        return content;
    }
    public String getJSvalue(String key){
        return ProjectAutoTip.getMmaps().get(key);
    }
}
