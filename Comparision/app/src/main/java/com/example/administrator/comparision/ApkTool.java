package com.example.administrator.comparision;
/**
 * Created by Administrator on 2017/11/4.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class ApkTool {
    static  String TAG = "ApkTool";
    public static List<AppInfo> mLocalInstallApps = null;

    public static List<AppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName((String)packageManager.getApplicationLabel(packageInfo.applicationInfo));
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                appInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                appInfos.add(appInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return appInfos;
    }
}
