package com.example.administrator.comparision;
/**
 * Created by Administrator on 2017/11/5.
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class Dish2Activity extends AppCompatActivity {
    private ListView lv_dish_list;
    private TextView firstResInfo;
    private TextView secondResInfo;
    private TextView thirdResInfo;
    private DishAdapter mDishAdapter;
    private ArrayList<String> resInfos;
    private List<DishInfo> dishInfos;
    public Handler mHandler = new Handler();
    public AssetManager am;
    public InputStream inputStream = null;
    public InputStreamReader inputStreamReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishlist_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resInfos = bundle.getStringArrayList("resInfos");

        am = this.getAssets();
        dishInfos = new ArrayList<DishInfo>();
        firstResInfo = (TextView) findViewById(R.id.firstResInfo);
        secondResInfo = (TextView) findViewById(R.id.secondResInfo);
        thirdResInfo = (TextView) findViewById(R.id.thirdResInfo);
        lv_dish_list = (ListView) findViewById(R.id.lv_dish_list);
        initDishList();

        mDishAdapter = new DishAdapter();
        lv_dish_list.setAdapter(mDishAdapter);
        lv_dish_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }

    private void initDishList() {
        String resName = resInfos.get(0);
        List<String> appNames = new ArrayList<String>();
        for (int i = 1; i < resInfos.size(); ++i) {
            String []strs = resInfos.get(i).split(" ");
            System.out.println(strs[0]);
            if (appNames.size() < 3) {
                appNames.add(strs[0]);
                String str = new String();
                str+=strs[0]+"\n起送价"+strs[1]+"配送费"+strs[2]+"满减" + strs[3];
                if (appNames.size() == 1) firstResInfo.setText(str);
                if (appNames.size() == 2) secondResInfo.setText(str);
                if (appNames.size() == 3) thirdResInfo.setText(str);
            }
        }
        String appName;
        for (int i = 0; i < appNames.size(); ++i) {
            appName = appNames.get(i);
            System.out.println("what ...............-1" + appName + " " + appNames.size());
            try {
                inputStream = am.open(appName + ".txt");
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            } catch(Exception e) {
                Toast.makeText(Dish2Activity.this, appName + ".txt " + "InputStreamReader Error", Toast.LENGTH_SHORT).show();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);

            try {
                int j = 0, k = 0;
                String []strs = new String[3];
                String str;
                while ((str = reader.readLine()) != null) {
                    if (str.equals(resName)) break;
                }
                if (str == null) continue;
                for (k = 1; k < 5; ++k) str = reader.readLine();
                System.out.println("what ..............-2 " + str);
                k = Integer.parseInt(reader.readLine());
                System.out.println("what ..............-2 " + str);
                while (k > 0) {
                    strs[1] = appName;
                    for (j = 0; j < 3; ++j)
                        if (j != 1) strs[j] = reader.readLine();
                        else strs[1] += " " + reader.readLine();
                    System.out.println("what ..............1 " + strs[0]);
                    j = searchEqualName(strs[0]);
                    System.out.println("what ..............1 " + j);
                    if (j == -1) {
                        Drawable drawable;
                        if (strs[2] != null) drawable = new BitmapDrawable(urlToImage(strs[2]));
                        else drawable = null;
                        DishInfo dishInfo = new DishInfo(drawable, strs[0], strs[1]);
                        dishInfos.add(dishInfo);
                    } else {
                        DishInfo dishInfo = dishInfos.get(j);
                        if (strs[1].equals(dishInfo.getOthers(0)) || strs[1].equals(dishInfo.getOthers(1))) continue;
                        else dishInfos.get(j).addOthers(strs[1]);
                    }
                    --k;
                }
            } catch (Exception e) {
                Toast.makeText(Dish2Activity.this, "StringBuffer Error", Toast.LENGTH_SHORT).show();
            }
            try {
                inputStream.close();
                inputStreamReader.close();
                //Toast.makeText(Dish2Activity.this, "Close Success!s", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(Dish2Activity.this, "Close Filed!s", Toast.LENGTH_SHORT).show();
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDishAdapter.setData(dishInfos);
            }
        });
    }

    public Bitmap urlToImage(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    public int searchEqualName(String str) {
        for (int i = 0; i < dishInfos.size(); ++i)
            if (str.equals(dishInfos.get(i).getDishName())) return i;
        return -1;
    }

    class DishAdapter extends BaseAdapter {

        List<DishInfo> dishInfos = new ArrayList<DishInfo>();

        public void setData(List<DishInfo> dishInfos) {
            this.dishInfos = dishInfos;
            notifyDataSetChanged();
        }

        public List<DishInfo> getData() {
            return dishInfos;
        }

        @Override
        public int getCount() {
            if (dishInfos != null && dishInfos.size() > 0) {
                return dishInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (dishInfos != null && dishInfos.size() > 0) {
                return dishInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            DishInfo dishInfo = dishInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_dish_info, null);
                mViewHolder.dishImage = (ImageView) convertView.findViewById(R.id.dishImage);
                mViewHolder.dishName = (TextView) convertView.findViewById(R.id.dishName);
                mViewHolder.dishInFirstRes = (TextView) convertView.findViewById(R.id.dishInFirstRes);
                mViewHolder.dishInSecondRes = (TextView) convertView.findViewById(R.id.dishInSecondRes);
                mViewHolder.dishInThirdRes = (TextView) convertView.findViewById(R.id.dishInThirdRes);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.dishImage.setImageDrawable(dishInfo.getImage());
            mViewHolder.dishName.setText(dishInfo.getDishName());
            if (dishInfo.getSize() >= 0) mViewHolder.dishInFirstRes.setText(dishInfo.getOthers(0));
            if (dishInfo.getSize() >= 1) mViewHolder.dishInSecondRes.setText(dishInfo.getOthers(1));
            if (dishInfo.getSize() >= 2) mViewHolder.dishInThirdRes.setText(dishInfo.getOthers(2));
            return convertView;
        }

        class ViewHolder {
            ImageView dishImage;
            TextView dishName;
            TextView dishInFirstRes;
            TextView dishInSecondRes;
            TextView dishInThirdRes;
        }
    }
}
