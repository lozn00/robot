package cn.qssq666.robot.asynctask;

import android.os.AsyncTask;

/**
 * Created by luozheng on 2016/11/14.  qssq.space
 */

public class QssqTask<T> extends AsyncTask<Object, Object, T> {
    public QssqTask(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    ICallBack<T> iCallBack;

    @Override
    protected T doInBackground(Object... params) {
        return iCallBack.onRunBackgroundThread();
    }

    public interface ICallBack<T> {
        T onRunBackgroundThread();

        void onRunFinish(T t);
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        iCallBack.onRunFinish(t);
    }

    static public void executeTask(ICallBack iCallBack) {
        new QssqTask<>(iCallBack).execute();
    }
}
