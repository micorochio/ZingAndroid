package com.bb.zing;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import java.util.Map;



public class BBApplication extends Application {

    private Map<String, String> userInfo;//登陆的用户信息


    public static long id;


    private static Context context;
    private static BBApplication self;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        self = this;

    }




    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 保存数据
     *
     * @param name
     * @param map
     */
    public void saveSharedInfo(String name, Map<String, String> map) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(name, Context.MODE_MULTI_PROCESS); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        for (String key : map.keySet()) {//遍历key
            editor.putString(key, map.get(key));
        }
        editor.commit();//提交
    }

    /**
     * 读取数据
     *
     * @param name
     * @return
     */
    public Map<String, String> getSharedInfo(String name) {
        SharedPreferences getSharedPreferences = this.getSharedPreferences(name, Context.MODE_MULTI_PROCESS); //读取数据
        return (Map<String, String>) getSharedPreferences.getAll();
    }

    /**
     * 清除数据
     *
     * @param name
     */
    public void cleanSharedInfo(String name) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(name, Context.MODE_MULTI_PROCESS); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.clear();
        editor.commit();//提交
    }

    public String getVersionName(){
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        String version = packInfo.versionName;

        return version;
    }

    public static Context getContext() {
        return context;
    }

    public static BBApplication getSelf() {
        return self;
    }
}
