package cn.qssq666.robot.interfaces;

public interface DownloadListener {
	void onStart(int value);
	void onLoading(int process);
	void onFail(String value);
	void onSuccess(String value);
}
