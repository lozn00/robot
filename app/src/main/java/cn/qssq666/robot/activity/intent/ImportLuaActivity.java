package cn.qssq666.robot.activity.intent;
import cn.qssq666.CoreLibrary0;import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.plugin.lua.util.LuaPluginUtil;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;

/**
 * Created by qssq on 2018/11/16 qssq666@foxmail.com
 */
public class ImportLuaActivity extends BaseIntentActivity {


    protected void onReceiveStr(final String fileOrContent) {

        AlertDialog alertDialog = DialogUtils.showEditDialog(this, "请输入文件名", "脚本命名", new INotify<String>() {
            @Override
            public void onNotify(String fileName) {
                File defaultLuaPath = LuaPluginUtil.getDefaultPath(ImportLuaActivity.this);
                File saveFile = new File(defaultLuaPath, fileName + ".lua");
                if (saveFile.exists()) {
                    DialogUtils.showDialog(ImportLuaActivity.this, "请更换其它名字，此文件已存在!", "提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onReceiveStr(fileOrContent);
                        }
                    });
                } else {

                    executeImportFile(fileOrContent, saveFile, false);
                }
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void executeImportFile(final String fileOrStr, final File saveFile, final boolean isFile) {
        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this);
        progressDialog.setMessage("导入中...");
        progressDialog.show();
        new QssqTaskFix<String, Object>(new QssqTaskFix.ICallBackImp<String, Object>() {
            @Override
            public Object onRunBackgroundThread(String... params) {
                String current = params[0];

                if (isFile) {
                    try {
                        FileUtils.copyFile(new File(fileOrStr), saveFile);
                        return saveFile;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e;
                    }
                } else {
                    try {
                        FileUtils.writeStringToFile(saveFile, fileOrStr, "utf-8");
                        return saveFile;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e;
                    }

                }
            }

            @Override
            public void onRunFinish(Object o) {

                progressDialog.dismiss();
                if (o instanceof File) {
                    DialogUtils.showDialog(ImportLuaActivity.this, "导入成功,文件名:" + ((File) o).getAbsolutePath(), "提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                } else {
                    DialogUtils.showDialog(ImportLuaActivity.this, "导入失败!" + Log.getStackTraceString(((Exception) o)), "错误", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }

            }
        }).execute(fileOrStr);
    }

    protected void onReceivePath(final String sourcePath) {
        File defaultLuaPath = LuaPluginUtil.getDefaultPath(ImportLuaActivity.this);
        File savePath = new File(defaultLuaPath, new File(sourcePath).getName());
        if (savePath.exists()) {
            DialogUtils.showDialog(ImportLuaActivity.this, "重复导入,此文件导入了!", "提示", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else {

            executeImportFile(sourcePath, savePath, true);
        }
    }
}
