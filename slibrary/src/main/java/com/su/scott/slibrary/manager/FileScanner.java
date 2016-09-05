package com.su.scott.slibrary.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class FileScanner {
    private ArrayList<String> mDirPath;

    private static FileScanner instance;

    private FileScanner(Context context) {
        initSDPath(context);
    }

    public static FileScanner getInstance(Context context) {
        if (instance == null) {
            synchronized (FileScanner.class) {
                if (instance == null) {
                    instance = new FileScanner(context);
                }
            }
        }
        return instance;
    }

    public void scanFile(final String[] fileExtensions,@NonNull final FileScanCallback callback ) {
        new AsyncTask<Void, List<File>, List<File>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onPreScan();
            }

            @Override
            protected List<File> doInBackground(Void... params) {
                List<File> result = new ArrayList<File>();
                SystemClock.sleep(2000);//等待2S
                for (int i = 0; i < mDirPath.size(); i++) {
                    findFilePath4(mDirPath.get(i), fileExtensions, result);
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<File> result) {
                super.onPostExecute(result);
                callback.onScanComplete(result);
            }
        }.execute();
    }

    /**
     * 在指定根目录下递归查找文件maskrom.bin
     * path 查找根路径
     * Environment.getExternalStorageDirectory()
     **/
    @SuppressWarnings("unused")
    private static void findFilePath4(String path, String[] extensions, List<File> result) {
        try {

//        	Log.e("TAG","----------> " + path);

            File a = new File(path);
            if (a == null) {
                return;
            }
            String[] file = a.list();
            File temp = null;
            String path2;
            String paths = new String();
            for (int i = 0; i < file.length; i++) {
                if (path.endsWith(File.separator)) {
                    path2 = path + file[i];
                } else {
                    path2 = path + File.separator + file[i];
                }
                temp = new File(path2);
                String name = (temp.getName()).toString();
                if (name.equals(".")
                        || name.equals("..")
                        || name.equalsIgnoreCase("Android")
                        || name.equalsIgnoreCase("LOST.DIR")
                        || name.equalsIgnoreCase("UCDownloads")
                        || name.equalsIgnoreCase("Tencent")
                        || name.equalsIgnoreCase("system")
                        || name.equalsIgnoreCase("wandoujia")
                        || name.equalsIgnoreCase("DCIM")
                        || name.equalsIgnoreCase("media")
                        || name.equalsIgnoreCase("music")
                        || name.equalsIgnoreCase("movies")
                        || name.equalsIgnoreCase("wangxin")
                        || name.equalsIgnoreCase("tencentmapsdk")
                        || name.equalsIgnoreCase("taobao")
                        || name.equalsIgnoreCase("qqmusic")
                        || name.equalsIgnoreCase("alipay")
                        || name.startsWith(".")
                        || name.startsWith("com.")) {
                    continue;  //排除系统的目录不用遍历，节省时间
                }
                if (temp.isFile()) {
                    for (String extension : extensions) {
                        if (name.endsWith(extension)) {
                            result.add(temp);
                            break;
                        }
                    }
                }
                //如果是文件夹，那么就递归
                if (temp.isDirectory()) {
                    findFilePath4(path2, extensions, result);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*
     * 获取本地存储的根目录
     * paths数组中[0]保存外置存储器的路径,[1]中保存内置SD卡路径
     * 由于android不支持直接获取外置存储卡路径,所以通过反射机制获取
     */
    @SuppressLint("NewApi")
    private void initSDPath(Context context) {
        try {
            mDirPath = new ArrayList<String>();
            File file = context.getFilesDir();                                  // 获得/data/data/files的目录
            if (file != null) {
                mDirPath.add(file.getAbsolutePath());
            }

            Class<?>[] paramClasses = {};
            Object[] params = {};
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, params);
            //首先判断外置SD是否存在;
            for (String path : paths) {
                if (getStorageState(path, context).equals(Environment.MEDIA_MOUNTED)) {
//                        String newPath = path + "/Android/data/com.harison.adver/Program";
//                        mDirPath.add(newPath);
                    mDirPath.add(path);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description :  映射
     */
    @SuppressLint("NewApi")
    private String getStorageState(String path, Context context) {
        try {
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", new Class[]{String.class});
            String state = (String) getVolumeStateMethod.invoke(sm, path);
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface FileScanCallback {
        void onPreScan();

        void onScanComplete(List<File> result);
    }


}
