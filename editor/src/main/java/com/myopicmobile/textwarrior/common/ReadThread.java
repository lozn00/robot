package com.myopicmobile.textwarrior.common;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zyw on 2017/10/2.
 */
public class ReadThread extends Thread
{
	public static final int MSG_READ_OK =0x101;
	public static final   int MSG_READ_FAIL =0x102;
	private  Handler handler;
	private  String path;
	private  To to;
	public ReadThread(String path, Handler handler)
	{
		this(path,handler,null);
	}
	public ReadThread(String path, Handler handler,To to)
	{
		this.path=path;
		this.handler=handler;
		this.to = to;
	}
	@Override
	public void run() {
		readFile(path);
	}

	private  void readFile(String file)
	{
		FileInputStream fileInputStream = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		StringBuilder stringBuilder=new StringBuilder();
		try {
			fileInputStream=new FileInputStream(file);
			byte[] buf=new byte[1024];
//			int len=0;
//			while ((len=fileInputStream.read(buf))!=-1){
//				stringBuilder.append(new String(buf,0,len));
//			}
			isr = new InputStreamReader(fileInputStream);
			 br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line+"\n");
			}
			handler.sendMessage(Message.obtain(handler, MSG_READ_OK,stringBuilder.toString()));
			if (to!=null){
				to.T("ok");
			}
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendMessage(Message.obtain(handler, MSG_READ_FAIL));
			if (to!=null){
				to.T("fail");
			}
		}finally {
			if(fileInputStream!=null)
			{
				try {
					isr.close();
					br.close();
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
