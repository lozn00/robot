package cn.qssq666.robot.business;

import java.util.List;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.ViolationRecordBean;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.utils.DBHelper;

/**
 * Created by qssq on 2018/2/25 qssq666@foxmail.com
 */

public class ViolationRecordUtil {
    public static int getViolationCount(DBUtils dbUtils, String group, String qq) {
        List<ViolationRecordBean> violationRecordBeans = DBHelper.getViolationRecordUtil(dbUtils).queryByColumnArr(ViolationRecordBean.class,
                ViolationRecordBean.class, new String[]{FieldCns.FIELD_GROUP, FieldCns.FIELD_QQ},
                new String[]{group, qq});

        if (violationRecordBeans == null || violationRecordBeans.isEmpty()) {
            DBHelper.getViolationRecordUtil(dbUtils).insert(new ViolationRecordBean().setGroups(group).setQq(qq));
            return 0;

        } else {
            return violationRecordBeans.get(0).getCount();
        }

    }


    public static int addViolationCount(DBUtils dbUtils, String group, String qq) {
        List<ViolationRecordBean> violationRecordBeans = DBHelper.getViolationRecordUtil(dbUtils).queryByColumnArr(ViolationRecordBean.class,
                ViolationRecordBean.class, new String[]{FieldCns.FIELD_GROUP, FieldCns.FIELD_QQ}, new String[]{group, qq});

        if (violationRecordBeans == null || violationRecordBeans.isEmpty()) {
            return (int) DBHelper.getViolationRecordUtil(dbUtils).insert(new ViolationRecordBean().setGroups(group).setQq(qq).setCount(1));


        } else {

            int count = 0;

            int index = 0;
            for (ViolationRecordBean violationRecordBean : violationRecordBeans) {
                violationRecordBean.setCount(1 + violationRecordBean.getCount());

                if (index > 0) {

                    DBHelper.getViolationRecordUtil(dbUtils).deleteById(ViolationRecordBean.class, violationRecordBean.getId());
                } else {
                    count = DBHelper.getViolationRecordUtil(dbUtils).update(violationRecordBean);

                }
                index++;


            }

            return count;
        }


    }

    public static int resetViolationCount(DBUtils dbUtils, String group, String qq) {


        return DBHelper.getViolationRecordUtil(dbUtils).deleteByColumn(ViolationRecordBean.class, new String[]{FieldCns.FIELD_GROUP, FieldCns.FIELD_QQ}, new String[]{
                group, qq
        });


    }


    public static int resetViolationCount(DBUtils dbUtils, String group) {


        return DBHelper.getViolationRecordUtil(dbUtils).deleteByColumn(ViolationRecordBean.class, new String[]{FieldCns.FIELD_GROUP}, new String[]{
                group
        });


    }


    public static int clearAll(DBUtils dbUtils) {
        int i = DBHelper.getViolationRecordUtil(dbUtils).deleteAll(ViolationRecordBean.class);
        return i;
    }
}
