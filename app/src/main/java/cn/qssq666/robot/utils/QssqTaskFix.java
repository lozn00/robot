package cn.qssq666.robot.utils;

import android.os.AsyncTask;

/**
 * Created by luozheng on 2016/11/14.  qssq.space
 */

public class QssqTaskFix<P, T> extends AsyncTask<P, Object, T> {
    public QssqTaskFix(ICallBackImp iCallBackImp) {
        this.iCallBackImp = iCallBackImp;
    }

    ICallBackImp<P, T> iCallBackImp;

    @Override
    protected T doInBackground(P... params) {
        return iCallBackImp.onRunBackgroundThread(params);
    }


    public interface ICallBack<P, T> {
        T onRunBackgroundThread(P... params);

        void onRunFinish(T t);


        void onProgressUpdate(Object o);

        void onSetQssqTask(QssqTaskFix taskFix);
    }

    public static abstract class ICallBackImp<P, T> implements ICallBack<P, T> {
        public QssqTaskFix getQssqTask() {
            return taskFix;
        }

        private QssqTaskFix taskFix;

        @Override
        public void onProgressUpdate(Object o) {

        }

        @Override
        public void onSetQssqTask(QssqTaskFix taskFix) {

            this.taskFix=taskFix;

        }
    }


    @Override
    protected void onProgressUpdate(Object... values) {
        iCallBackImp.onProgressUpdate(values[0]);

    }

    @Override
    protected void onPreExecute() {
        iCallBackImp.onSetQssqTask(this);
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        iCallBackImp.onRunFinish(t);
    }

    public void publishProgressProxy(Object o) {
        publishProgress(o);
//        progressBar.setProgress(values[0]); //设置进度条进度值
//        textView.append("当前进度值:" + values[0] + "\n");
    }

    static public void executeTask(ICallBackImp iCallBackImp) {
        new QssqTaskFix<>(iCallBackImp).execute();
    }
}
