package cn.qssq666.robot.utils;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.constants.Cns;

/**
 * Created by qssq on 2017/6/4 qssq666@foxmail.com
 */

public class DBHelper {
    public static DBUtils getQQGroupWhiteNameDBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_WHTE_GROUP_NAME_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getViolationRecordUtil(DBUtils dbUtils) {
            synchronized (DBHelper.class) {
        dbUtils.setAlias("");

            }
        return dbUtils;
    }


    public static DBUtils getViolationWordHistoryRecordUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias("");

        }
        return dbUtils;
    }

    public static DBUtils getIgnoreQQDBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_QQIGNORE_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getIgnoreGagDBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_ignore_gag_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getKeyWordDBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_KEY_REPLY_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getRedPacketBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_REDPACKET_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getQQGroupManagerDBUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_QQ_GROUP_MANAGER_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getSuperManager(DBUtils dbUtils) {
        synchronized (DBHelper.class) {

            dbUtils.setAlias(Cns.TABLE_SUPER_MANAGER_TABLE);

        }
        return dbUtils;
    }

    public static DBUtils getGagKeyWord(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_GAG_KEYWORD);

        }
        return dbUtils;
    }

    public static DBUtils getNickNameUtil(DBUtils dbUtils) {
        dbUtils.setAlias("");

        return dbUtils;
    }


    public static DBUtils getVarTableUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias("");


        }

        return dbUtils;
    }

    public static DBUtils getGroupAdminTableUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {

            dbUtils.setAlias("");

        }

        return dbUtils;
    }

    public static DBUtils getFloorUtil(DBUtils dbUtils) {
        synchronized (DBHelper.class) {
            dbUtils.setAlias(Cns.TABLE_FLOOR);

        }
        return dbUtils;
    }


}
