package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;import java.util.Date;
import java.util.List;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.ViolationWordRecordBean;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.utils.DBHelper;

/**
 * Created by qssq on 2018/2/25 qssq666@foxmail.com
 */

public class ViolationWordHistoryRecordUtil {
    public static List<ViolationWordRecordBean> queryRecord(DBUtils dbUtils, String group, String qq) {
        List<ViolationWordRecordBean> list = DBHelper.getViolationWordHistoryRecordUtil(dbUtils).query(ViolationWordRecordBean.class, new String[]{FieldCns.FIELD_GROUP, FieldCns.FIELD_QQ,FieldCns.FIELD_WORD,FieldCns.FIELD_TIME},
                "" + FieldCns.FIELD_GROUP + "=? and " + FieldCns.FIELD_QQ + "=?",
                new String[]{group, qq}, FieldCns.FIELD_TIME+" desc");

        return list;

    }


    public static int addRecord(DBUtils dbUtils, String group, String qq, String msg) {


        return (int) DBHelper.getViolationWordHistoryRecordUtil(dbUtils).insert(new ViolationWordRecordBean().setGroups(group).setQq(qq).setWord(msg).setTime(new Date().getTime()));

    }

    public static int clearRecord(DBUtils dbUtils, String group, String qq) {
        return DBHelper.getViolationWordHistoryRecordUtil(dbUtils).deleteByColumn(ViolationWordRecordBean.class, new String[]{FieldCns.FIELD_GROUP, FieldCns.FIELD_QQ}, new String[]{
                group, qq
        });

    }
    public static int clearRecord(DBUtils dbUtils, String group) {
        return DBHelper.getViolationWordHistoryRecordUtil(dbUtils).deleteByColumn(ViolationWordRecordBean.class, new String[]{FieldCns.FIELD_GROUP}, new String[]{
                group
        });

    }

    public static int clearAll(DBUtils dbUtils) {
            int i = DBHelper.getViolationWordHistoryRecordUtil(dbUtils).deleteAll(ViolationWordRecordBean.class);
            return i;
        }
}
