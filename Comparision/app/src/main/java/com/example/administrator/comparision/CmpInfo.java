package com.example.administrator.comparision;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class CmpInfo {
    private List<Drawable> images;
    private List<String> appNames;

    public CmpInfo(List<Drawable> images, List<String> appNames) {
        this.images = images;
        this.appNames = appNames;
    }

    public CmpInfo() {

    }

    public int getSize() {
        return images.size();
    }

    public Drawable getImage(int i) {
        if (i >= images.size()) return null;
        return images.get(i);
    }

    public List<String> getAppNames() {
        return appNames;
    }

    public String getAppName(int i) {
        if (i >= appNames.size()) return null;
        return appNames.get(i);
    }
}
