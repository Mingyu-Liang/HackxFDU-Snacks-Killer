package com.example.administrator.comparision;
/**
 * Created by Administrator on 2017/11/4.
 */

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

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class DisplayAppListActivity extends AppCompatActivity {
    private ListView lv_app_list;
    private TextView hintView;
    private Button determineBtn;
    private AppAdapter mAppAdapter;
    private ArrayList<String> chosenApp = new ArrayList<String>();
    public Handler mHandler = new Handler();

    private Intent result = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;

        setContentView(R.layout.applist_main);

        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        hintView = (TextView) findViewById(R.id.hintView);
        determineBtn = (Button) findViewById(R.id.determineBtn);

        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        initAppList();
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String appName = mAppAdapter.appInfos.get(position).getAppName();
                int j = -1;
                for (int i = 0; i < chosenApp.size(); ++i)
                    if (chosenApp.get(i) == appName) j = i;
                if (j == -1) {
                    chosenApp.add(appName);
                    Toast.makeText(DisplayAppListActivity.this, "你添加了 " + appName, Toast.LENGTH_SHORT).show();
                }
                else {
                    chosenApp.remove(appName);
                    Toast.makeText(DisplayAppListActivity.this, "你删除了 " + appName, Toast.LENGTH_SHORT).show();
                }
                if (chosenApp.size() == 0) hintView.setText("空");
                else {
                    String str = new String();
                    str += chosenApp.get(0);
                    for (int i = 1; i < chosenApp.size(); ++i)
                        str += "、" + chosenApp.get(i);
                    hintView.setText(str);
                }
            }
        });

        determineBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //String []result = new String[chosenApp.size()];
                //for (int i = 0; i < chosenApp.size(); ++i)
                    //result[i] = chosenApp.get(i);
                Intent intent = new Intent();
                intent.putStringArrayListExtra("result", chosenApp);
                setResult(2000, intent);
                finish();
            }
        });
    }

    private void initAppList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<AppInfo> appInfos = ApkTool.scanLocalInstallAppList(DisplayAppListActivity.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }

    class AppAdapter extends BaseAdapter {

        List<AppInfo> appInfos = new ArrayList<AppInfo>();

        public void setData(List<AppInfo> appInfos) {
            this.appInfos = appInfos;
            notifyDataSetChanged();
        }

        public List<AppInfo> getData() {
            return appInfos;
        }

        @Override
        public int getCount() {
            if (appInfos != null && appInfos.size() > 0) {
                return appInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (appInfos != null && appInfos.size() > 0) {
                return appInfos.get(position);
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
            AppInfo appInfo = appInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(appInfo.getImage());
            mViewHolder.tx_app_name.setText(appInfo.getAppName());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }
}
