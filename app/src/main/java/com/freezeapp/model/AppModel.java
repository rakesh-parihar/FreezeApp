package com.freezeapp.model;

import android.graphics.drawable.Drawable;

/**
 * Model class for Apps
 *
 * @author Rakesh
 * @since 10/11/2017.
 */

public class AppModel {
    String appname, pkg;
    boolean isDisabled;
    Drawable icon;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
