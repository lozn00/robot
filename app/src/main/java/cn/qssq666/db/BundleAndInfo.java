package cn.qssq666.db;

import android.os.Bundle;

/**
 * Created by luozheng on 2017/4/28.  qssq.space
 */

public class BundleAndInfo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(String activityClass) {
        this.activityClass = activityClass;
    }

    String activityClass;
    String name;

    public String getDescript() {
        return descript;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    int id;
    String descript;
    Bundle bundle;
}
