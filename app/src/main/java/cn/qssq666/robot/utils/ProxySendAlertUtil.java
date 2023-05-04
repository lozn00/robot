package cn.qssq666.robot.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.http.newcache.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProxySendAlertUtil {

    private static MediaPlayer mMediaPlayer;
    private static Vibrator vibrator;
    private static Runnable r;

    /**
     * //RingtoneManager.TYPE_NOTIFICATION;   通知声音
     * <p>
     * //RingtoneManager.TYPE_ALARM;  警告
     * <p>
     * //RingtoneManager.TYPE_RINGTONE; 铃声
     * huò qǔ de shì líng shēng de Uri
     *
     * @param ctx
     * @param type
     * @return
     */
    public static Uri getDefaultRingtoneUri(Context ctx, int type) {

        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);

    }

    public static void cancel() {
        cancelMedia();
        cancelVirbrate();
    }

    private static void cancelVirbrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.cancel();
            vibrator = null;
        }
    }

    public static void emailAlert(RobotContentProvider robotContentProvider, String message) {
        if (robotContentProvider._miscConfig.enableEmailForward) {

            String content;
            if (TextUtils.isEmpty(robotContentProvider._miscConfig.emailContent)) {
                content = message;
            } else if (robotContentProvider._miscConfig.emailContent.contains("--") || robotContentProvider._miscConfig.emailContent.contains("__")) {
                content = robotContentProvider._miscConfig.emailContent + "\n" + message;
            } else {
                content = robotContentProvider._miscConfig.emailContent;

            }
            try {
                sendEmail(robotContentProvider._miscConfig.emailServer, robotContentProvider._miscConfig.emailPort,
                        robotContentProvider._miscConfig.sender,
                        robotContentProvider._miscConfig.senderPwd,
                        robotContentProvider._miscConfig.receiver,
                        content
                );
            } catch (Throwable e) {
                LogUtil.writeLog("发送邮件失败" + e.getMessage());
            }
    /*    _miscConfig.enableEmailForward = sharedPreferences.getBoolean(Cns.MISC_EMAIL_FORWARD_ENABLE, false);// binding.cbEanbleMailForward.isChecked());
        _miscConfig.sender = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL, "");// binding.evSenderEmail.getText().toString());
        _miscConfig.senderPwd = sharedPreferences.getString(Cns.MISC_EMAIL_SENDER_EMAIL_PWD, "");// binding.evSenderEmailPwd.getText().toString());
        _miscConfig.receiver = sharedPreferences.getString(Cns.MISC_EMAIL_RECEIVER_EMAIL, "");//  binding.evReceiverEmailAddress.getText().toString());
        _miscConfig.emailServer = sharedPreferences.getString(Cns.MISC_EMAIL_SERVER_ADDRESS, "");// binding.evEmailServerAddress.getText().toString());
        _miscConfig.emailContent = sharedPreferences.getString(Cns.MISC_EMAIL_CONTENT, "");//  binding.evEmailContent.getText().toString());*/
        }

    }

    public static void sendEmail(String host, int port, String user, String password, String receiver, String content) throws MessagingException {
        //host跟port可以到：
        //http://service.mail.qq.com/cgi-bin/help?subtype=1&&no=1000557&&id=20010
//        String host ="smtp.qq.com";
//        int port = 25;
//        String user = "123456789@qq.com";//换成你自己的
//        String password = "";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        //
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
//        设置发件人
        message.setFrom(user);

        //
        MimeMultipart multipart = new MimeMultipart();
        //邮件内容
        MimeBodyPart text = new MimeBodyPart();
        text.setText(content, "UTF-8");
        multipart.addBodyPart(text);
    /*    //
        MimeBodyPart attachment = new MimeBodyPart();
        //附件地址
        attachment.attachFile(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test.docx");
        //附件名称，收件人收到附件显示的附件名称
        attachment.setFileName("test.docx");
        multipart.addBodyPart(attachment);*/

        //设置收件人
        message.addRecipients(Message.RecipientType.TO, receiver);//"987654321@qq.com");
        //邮件主题
        message.setSubject("消息提醒", "UTF-8");
        message.setContent(multipart);
        //发送时间
        message.setSentDate(new Date());
        //不传附件的话可以使用message.setText()设置邮件内容，否则message.setText()不起作用，接受者收不到内容
        //message.setText("窗前明月光，疑似地上霜，举头望明月，低头思故乡。", "UTF-8");
        //
        Transport transport = session.getTransport();
        transport.connect(host, port, user, password);
        //
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static boolean isAlerting() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public static void urlForward(String forwardUrl, String message) {
        int titleIndex = message.indexOf("标题");

        int startIndex = message.indexOf("内容");
        int enddex;
        String temp="";
        if (titleIndex >= 0 && startIndex > 0) {
            int  enddex1 = startIndex;
            int startIndex1 = titleIndex + 3;
            temp= message.substring(startIndex1, enddex1)+" ";
        }
        {
            if(startIndex<0){
                startIndex=0;
            }else{
                startIndex=startIndex+3;
            }
            enddex = message.lastIndexOf("时间");
            if (enddex < 0 || enddex < startIndex) {
                enddex = message.length();
            }
        }
        message =temp+ message.substring(startIndex, enddex);
        if (TextUtils.isEmpty(forwardUrl)) {
            LogUtil.writeLoge("转发请求失败,转发url不正确" + forwardUrl);
            return;
        }
        String replacement = AppUtils.encodeUrl(message);
        if (!forwardUrl.startsWith("http")) {
            forwardUrl = "http://" + forwardUrl;
        }
        String url;
        if (forwardUrl.contains("message")) {
            url = forwardUrl.replace("[message]", replacement);
            url = url.replace("$message", replacement);

        } else if (forwardUrl.contains("msg")) {
            url = forwardUrl.replace("[msg]", replacement);
            url = url.replace("msg", replacement);

        }else{
            url=forwardUrl.lastIndexOf("/")==forwardUrl.length()-1? forwardUrl+message :forwardUrl+"/"+message;
        }
        String finalUrl = url;
        HttpUtil.queryGetData(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtil.writeLoge("转发请求失败" + finalUrl, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    LogUtil.writeLog("转发请求结果::" + response.body().string());

                } catch (Throwable e) {

                    LogUtil.writeLoge("转发请求失败" + finalUrl, e);
                }
            }
        });
    }

    /**
     * huò qǔ de shì líng shēng xiāng yìng de Ringtone
     *
     * @param ctx
     * @param type
     */
    public Ringtone getDefaultRingtone(Context ctx, int type) {

        return RingtoneManager.getRingtone(ctx,
                RingtoneManager.getActualDefaultRingtoneUri(ctx, type));

    }

    /**
     * 播放铃声
     */

    public static void vibrate(Context context, long second) {
        try {

            // 震动效果的系统服务
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = { 200, 2000, 2000, 200, 200, 200 };

            vibrator.vibrate(second * 1000);//振动两秒
        } catch (Throwable e) {

        }

        // 下边是可以使震动有规律的震动 -1：表示不重复 0：循环的震动
//        long[] pattern = { 200, 2000, 2000, 200, 200, 200 };
//        vibrator.vibrate(pattern, -1);
//        vibrator.vibrate(pattern, -1);
    }

    public static void PlayRingTone(Context ctx, int type, long second) {
        cancelMedia();
        mMediaPlayer = MediaPlayer.create(ctx,
                getDefaultRingtoneUri(ctx, type));
        if (second <= 0) {

        } else {

            mMediaPlayer.setLooping(true);
            r = new Runnable() {
                @Override
                public void run() {
                    try {
                        mMediaPlayer.stop();
                        mMediaPlayer = null;
                    } catch (Throwable e) {

                    }
                }
            };
            AppContext.getHandler().postDelayed(r, second * 1000);
        }
        mMediaPlayer.start();

    }

    private static void cancelMedia() {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();

            } catch (Throwable e) {

            }


        }
        if (r != null) {
            AppContext.getHandler().removeCallbacks(r);
            r = null;
        }
    }
}
