package com.example.administrator.comparision;

/**
 * Created by Administrator on 2017/11/4.
 */
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class DishInfo {

    private Drawable dishImage;
    private String dishName;
    private List<String> others;

    public DishInfo(Drawable dishImage, String dishName, List<String> others) {
        this.dishImage = dishImage;
        this.dishName = dishName;
        this.others = others;
    }
    public DishInfo(Drawable dishImage, String dishName, String str) {
        this.dishImage = dishImage;
        this.dishName = dishName;
        this.others = new ArrayList<String>();
        this.others.add(str);
    }
    public DishInfo() {

    }

    public int getSize() { return others.size(); }

    public Drawable getImage() {
        return dishImage;
    }

    public void setImage(Drawable dishImage) {
        this.dishImage = dishImage;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getOthers(int i) {
        if (i >= others.size()) return null;
        return others.get(i);
    }

    public void addOthers(String str) {
        others.add(str);
    }

}
