package cn.qssq666.robot.bean;

/**
 * Created by luozheng on 2017/3/12.  qssq.space
 */


public class UpdateBean {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    private String version;//EncryptUtilN.a7(new int[]{2916,3112,3100,3108,3100,3140}),
    private int versioncode;//EncryptUtilN.a7(new int[]{2897,3093,3093}),
    private boolean force;//false
    private String description;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String group;

    public String getQqgroup() {
        return qqgroup;
    }

    public void setQqgroup(String qqgroup) {
        this.qqgroup = qqgroup;
    }

    private String qqgroup;
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
