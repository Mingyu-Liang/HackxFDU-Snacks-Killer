package com.example.administrator.comparision;

/**
 * Created by Administrator on 2017/11/4.
 */
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class ResInfo {
    private Drawable resImage;
    private String resName;
    private List<String> others;

    public ResInfo(Drawable resImage, String resName, List<String> others) {
        this.resImage = resImage;
        this.resName = resName;
        this.others = others;
    }
    public ResInfo(Drawable resImage, String resName, String str) {
        this.resImage = resImage;
        this.resName = resName;
        this.others = new ArrayList<String>();
        this.others.add(str);
    }
    public ResInfo() {

    }

    public int getSize() { return others.size(); }

    public Drawable getImage() {
        return resImage;
    }

    public void setImage(Drawable resImage) {
        this.resImage = resImage;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getOthers(int i) {
        if (i >= others.size()) return null;
        return others.get(i);
    }

    public void addOthers(String str) {
        others.add(str);
    }

}
