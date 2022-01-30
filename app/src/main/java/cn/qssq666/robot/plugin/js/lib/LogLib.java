package cn.qssq666.robot.plugin.js.lib;
import cn.qssq666.CoreLibrary0;
import android.util.Log;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.plugin.js.cns.JSCns;

public class LogLib {
	public void w(String msg) {
		callback("w", msg);
		Log.w(JSCns.LOG_TAG, msg);
	}

	public void d(String msg) {
		callback("d", msg);
		Log.d(JSCns.LOG_TAG, msg);
	}

	public void e(String msg, Throwable e) {
		callback("e", msg, e);
		Log.e(JSCns.LOG_TAG, msg, e);
	}

	public void e(String msg) {
		callback("e", msg);
		Log.e(JSCns.LOG_TAG, msg);
	}

	public void i(String msg) {
		callback("i", msg);
		Log.i(JSCns.LOG_TAG, msg);
	}

	public void v(String msg) {
		callback("v", msg);
		Log.v(JSCns.LOG_TAG, msg);
	}

	public void callback(String string, String msg, Throwable... e) {

	}

	public void info(String msg) {
		i(msg);
	}

	public void wran(String msg) {
		w(msg);
	}

	public void debug(String msg) {
		d(msg);
	}

	public void error(String msg) {
		e(msg);
	}

	public void error(String msg, Throwable e) {
		e(msg, e);
	}

	public void toast(String toast) {
		callback("toast", toast);
		RobotContentProvider.getInstance().getPluginControlInterface().showDebugToast(toast);
	}
}
