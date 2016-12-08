package com.bb.zinglibrary.common;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by zing on 2016/12/5.
 */

public class SDCardUtil {
    /**
     * 获取缓存路径
     */
    public static String getCacheDir(Context context) {
        String cacheDir;
        if (context.getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = context.getExternalCacheDir().toString();
        } else {
            cacheDir = context.getCacheDir().toString();
        }
        return cacheDir;
    }

    /**
     * SD卡检查
     * 在SDCard中创建与删除文件权限
     * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
     * <p>
     * 往SDCard写入数据权限
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @return
     */

    private static boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        StatFs sf = getStatFs();
        //获取单个数据块的大小(Byte) sf.getAvailableBlocks()
        //空闲的数据块的数量 sf.getAvailableBlocks()

        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (sf.getAvailableBlocks() * sf.getBlockSize()) / 1024 / 1024; //单位MB
    }

    /**
     * SD卡总容量
     *
     * @return
     */
    public long getSDAllSize() {
        StatFs sf = getStatFs();
        //获取单个数据块的大小(Byte)
        // long blockSize = sf.getBlockSize();
        //获取所有数据块数
        // long allBlocks = sf.getBlockCount();

        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (sf.getBlockCount() * sf.getBlockSize()) / 1024 / 1024; //单位MB
    }

    private static StatFs getStatFs() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        return new StatFs(path.getPath());
    }


}
