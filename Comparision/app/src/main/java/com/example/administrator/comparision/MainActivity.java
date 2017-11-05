package com.example.administrator.comparision;

import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.PixelFormat;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView add_btn;
    private CmpAdapter mCmpAdapter;
    private ListView lv_cmp_list;
    private List<AppInfo> appInfos;
    private List<CmpInfo> cmpInfos;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAppList();
        lv_cmp_list = (ListView) findViewById(R.id.lv_cmp_list);
        mCmpAdapter = new CmpAdapter();
        lv_cmp_list.setAdapter(mCmpAdapter);
        initCmpList();

        add_btn = (ImageView) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, DisplayAppListActivity.class));
                startActivityForResult(new Intent(MainActivity.this, DisplayAppListActivity.class), 1000);
            }
        });

        lv_cmp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<String> chosenApp = new ArrayList<String>(cmpInfos.get(position).getAppNames());
                //List<String> appNames = cmpInfos.get(position).getAppNames();
                //for (int i = 0; i < appNames.size(); ++i)
                //    chosenApp.add(appNames.get(i));
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, ResActivity.class);
                intent.putStringArrayListExtra("appNames", chosenApp);
                startActivity(intent);
            }
        });
    }

    private void initAppList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(MainActivity.this.getPackageManager());
            }
        }.start();
    }

    private void initCmpList() {
        cmpInfos = new ArrayList<CmpInfo>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 2000)
        {
            ArrayList<String> result = data.getStringArrayListExtra("result");
            if (result.size() == 0) return;
            List<Drawable> pictureList = new ArrayList<Drawable>();
            for (int i = 0; i < result.size(); ++i) {
                String appName = new String(result.get(i));
                for (int j = 0; j < appInfos.size(); ++j) {
                    if (appName.equals(appInfos.get(j).getAppName())) {
                        pictureList.add(appInfos.get(j).getImage());
                        break;
                    }
                }
            }
            cmpInfos.add(new CmpInfo(pictureList, result));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCmpAdapter.setData(cmpInfos);
                }
            });
        }
    }

    class CmpAdapter extends BaseAdapter {

        List<CmpInfo> cmpInfos = new ArrayList<CmpInfo>();

        public void setData(List<CmpInfo> cmpInfos) {
            this.cmpInfos = cmpInfos;
            notifyDataSetChanged();
        }

        public List<CmpInfo> getData() {
            return cmpInfos;
        }

        @Override
        public int getCount() {
            if (cmpInfos != null && cmpInfos.size() > 0) {
                return cmpInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (cmpInfos != null && cmpInfos.size() > 0) {
                return cmpInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MainActivity.CmpAdapter.ViewHolder mViewHolder;
            CmpInfo cmpInfo = cmpInfos.get(position);
            if (convertView == null) {
                mViewHolder = new MainActivity.CmpAdapter.ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_cmp_info, null);
                mViewHolder.iv1_cmp_icon = (ImageView) convertView.findViewById(R.id.iv1_cmp_icon);
                mViewHolder.iv2_cmp_icon = (ImageView) convertView.findViewById(R.id.iv2_cmp_icon);
                mViewHolder.iv3_cmp_icon = (ImageView) convertView.findViewById(R.id.iv3_cmp_icon);
                mViewHolder.iv4_cmp_icon = (ImageView) convertView.findViewById(R.id.iv4_cmp_icon);
                mViewHolder.iv5_cmp_icon = (ImageView) convertView.findViewById(R.id.iv5_cmp_icon);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MainActivity.CmpAdapter.ViewHolder) convertView.getTag();
            }
            if (cmpInfo.getSize() > 0) mViewHolder.iv1_cmp_icon.setImageDrawable(cmpInfo.getImage(0));
            if (cmpInfo.getSize() > 1) mViewHolder.iv2_cmp_icon.setImageDrawable(cmpInfo.getImage(1));
            if (cmpInfo.getSize() > 2) mViewHolder.iv3_cmp_icon.setImageDrawable(cmpInfo.getImage(2));
            if (cmpInfo.getSize() > 3) mViewHolder.iv4_cmp_icon.setImageDrawable(cmpInfo.getImage(3));
            if (cmpInfo.getSize() > 4) mViewHolder.iv5_cmp_icon.setImageDrawable(cmpInfo.getImage(4));
            return convertView;
        }

        class ViewHolder {
            ImageView iv1_cmp_icon;
            ImageView iv2_cmp_icon;
            ImageView iv3_cmp_icon;
            ImageView iv4_cmp_icon;
            ImageView iv5_cmp_icon;
        }
    }
}
