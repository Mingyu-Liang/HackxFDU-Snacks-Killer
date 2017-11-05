package com.example.administrator.comparision;
/**
 * Created by Administrator on 2017/11/4.
 */

import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.content.res.AssetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import android.util.Log;

public class ResActivity extends AppCompatActivity {
    private ListView lv_res_list;
    private TextView resTV;
    private ResAdapter mResAdapter;
    private ArrayList<String> appNames;
    private List<ResInfo> resInfos = new ArrayList<ResInfo>();
    private List<String> resItems;
    public Handler mHandler = new Handler();
    public AssetManager am;
    public InputStream inputStream = null;
    public InputStreamReader inputStreamReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reslist_main);

        am = this.getAssets();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        appNames = bundle.getStringArrayList("appNames");
        String appName = new String();
        appName += appNames.get(0);
        for (int i = 1; i < appNames.size(); ++i)
            appName += "、" + appNames.get(i);

        resTV = (TextView) findViewById(R.id.resTV);
        resTV.setText(appName);
        lv_res_list = (ListView) findViewById(R.id.lv_res_list);

        mResAdapter = new ResAdapter();
        lv_res_list.setAdapter(mResAdapter);
        initResList();
        lv_res_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<String> resInforms = new ArrayList<String>();
                ResInfo resInfo = resInfos.get(position);
                resInforms.add(resInfo.getResName());
                for (int i = 0; i < resInfo.getSize(); ++i)
                    resInforms.add(resInfo.getOthers(i));

                Intent intent = new Intent();
                intent.setClass(ResActivity.this, Dish2Activity.class);
                intent.putStringArrayListExtra("resInfos", resInforms);
                startActivity(intent);
                //System.out.println("what ........." + position);
                //startActivity(new Intent(ResActivity.this, Dish2Activity.class));
            }
        });
    }

    private void initResList() {
        String appName, strLine;
        for (int i = 0; i < appNames.size(); ++i) {
            appName = appNames.get(i);
            try {
                inputStream = am.open(appName + ".txt");
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            } catch(Exception e) {
                Toast.makeText(ResActivity.this, "InputStreamReader Error", Toast.LENGTH_SHORT).show();
                continue;
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            //Toast.makeText(ResActivity.this, appName + ".txt", Toast.LENGTH_SHORT).show();
            try {
                int j = 0, k = 0;
                String []strs = new String[3];
                String str;
                while ((strs[0] = reader.readLine()) != null) {
                    strs[2] = appName;
                    for (k = 1; k < 5; ++k) {
                        if (k < 2) strs[k] = reader.readLine();
                        else strs[2] += " " + reader.readLine();
                    }
                    j = searchEqualName(strs[0]);
                    if (j == -1) {
                        Drawable drawable = new BitmapDrawable(urlToImage(strs[1]));
                        ResInfo resInfo = new ResInfo(drawable, strs[0], strs[2]);
                        resInfos.add(resInfo);
                    } else {
                        resInfos.get(j).addOthers(strs[2]);
                    }

                    k = Integer.parseInt(reader.readLine());
                    while (k > 0) {
                        str = reader.readLine();
                        str = reader.readLine();
                        str = reader.readLine();
                        --k;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ResActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            try {
                inputStream.close();
                inputStreamReader.close();
            } catch(Exception e) {
                Toast.makeText(ResActivity.this, "Close Filed!s", Toast.LENGTH_SHORT).show();
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResAdapter.setData(resInfos);
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
        for (int i = 0; i < resInfos.size(); ++i)
            if (str.equals(resInfos.get(i).getResName())) return i;
        return -1;
    }

    class ResAdapter extends BaseAdapter {

        List<ResInfo> resInfos = new ArrayList<ResInfo>();

        public void setData(List<ResInfo> resInfos) {
            this.resInfos = resInfos;
            notifyDataSetChanged();
        }

        public List<ResInfo> getData() {
            return resInfos;
        }

        @Override
        public int getCount() {
            if (resInfos != null && resInfos.size() > 0) {
                return resInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (resInfos != null && resInfos.size() > 0) {
                return resInfos.get(position);
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
            ResInfo resInfo = resInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_res_info, null);
                mViewHolder.resImage = (ImageView) convertView.findViewById(R.id.resImage);
                mViewHolder.resName = (TextView) convertView.findViewById(R.id.resName);
                mViewHolder.firstRes = (TextView) convertView.findViewById(R.id.firstRes);
                mViewHolder.secondRes = (TextView) convertView.findViewById(R.id.secondRes);
                mViewHolder.thirdRes = (TextView) convertView.findViewById(R.id.thirdRes);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.resImage.setImageDrawable(resInfo.getImage());
            mViewHolder.resName.setText(resInfo.getResName());
            if (resInfo.getSize() >= 0) mViewHolder.firstRes.setText(resInfo.getOthers(0));
            if (resInfo.getSize() >= 1) mViewHolder.secondRes.setText(resInfo.getOthers(1));
            if (resInfo.getSize() >= 2) mViewHolder.thirdRes.setText(resInfo.getOthers(2));
            return convertView;
        }

        class ViewHolder {
            ImageView resImage;
            TextView resName;
            TextView firstRes;
            TextView secondRes;
            TextView thirdRes;
        }
    }
}
