package com.su.scott.slibrary.manager;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * @类名 DbUtilHelper
 * @描述 基于xUtil3的数据库帮助类
 * @作者 Su
 * @时间 2016年7月
 */
public class DbUtilHelper {

    private DbUtilHelper() {
    }

    private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("default.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

    /**
     * 获取默认数据库操作类实例
     * 需在Application中初始化：
     * x.Ext.init(this);
     *
     * @return
     */
    public static DbManager getDefaultDbManager() {
        return x.getDb(daoConfig);
    }

}
