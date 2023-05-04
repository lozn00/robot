package cn.qssq666.robot.config;

public class MiscConfig {
    public String chatgpt_api_sercret_key="";
    public String ignoreKeyword="";
    public String keyword_alert_hour ="";

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPwd() {
        return senderPwd;
    }

    public void setSenderPwd(String senderPwd) {
        this.senderPwd = senderPwd;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEmailServer() {
        return emailServer;
    }

    public void setEmailServer(String emailServer) {
        this.emailServer = emailServer;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
public  boolean enableEmailForward;
   public  String sender;
   public  String senderPwd;
   public  String receiver;

    public int getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(int emailPort) {
        this.emailPort = emailPort;
    }

    public  int emailPort;
   public  String emailServer;
   public  String emailContent;
    public  boolean enableForwardUrl;
   public  String forwardUrl;
   public  String forwardUrlKeyword;
  public   boolean enableOutProgramVoiceAlert;
    public boolean redirectProxyAccountIsGroup;
  public String redirectProxySendAccount;
    public  String outProgramVoiceKeyword;
    public String getOutProgramVoiceKeyword() {
        return outProgramVoiceKeyword;
    }

    public void setOutProgramVoiceKeyword(String outProgramVoiceKeyword) {
        this.outProgramVoiceKeyword = outProgramVoiceKeyword;
    }


    public boolean isEnableOutProgramVoiceAlert() {
        return enableOutProgramVoiceAlert;
    }

    public void setEnableOutProgramVoiceAlert(boolean enableOutProgramVoiceAlert) {
        this.enableOutProgramVoiceAlert = enableOutProgramVoiceAlert;
    }



    //    private boolean mCfBaseEnableOutProgramVoiceAlert;
//    private String mCfBaseEnableOutProgramVoiceKeyword;
}
