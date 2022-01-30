package com.myopicmobile.textwarrior.android;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/1/3.
 */

public class AutoActivityClass extends AppCompatActivity {
    private String TAG;
    private Activity activity;
    private boolean run = false;
    private View view;
    public void setOnErrListener(Object back){
    }
    public void addOnErrListener(Object back){
    }
    public void removeOnErrListener(Object back){
    }
    public void removeAllOnErrListener(){
    }
    public void sendErr(Throwable e){
    }
    public void sendWoring(String e){
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.view = view;
    }
    public View getView(){
        return view;
    }

    public Object Msuper(String TAG, Object... object){
        return null;
    }
    public A NewOne(String TAG){
        return new A(TAG);
    }
    public class A{

        public A(String TAG){
        }
        public A with(String TAG,double value){
            return this;
        }
        public A with(String TAG,String value){
            return this;
        }
        public void start(){
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {

    }
}
