package cn.qssq666.robot.utils;

import cn.qssq666.db.DBUtils;
import cn.qssq666.db.ReflectUtils;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.AdminBean;
import cn.qssq666.robot.bean.FloorBean;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.GroupAdaminBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.bean.NickNameBean;
import cn.qssq666.robot.bean.RedPacketBean;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.bean.TwoBean;
import cn.qssq666.robot.bean.VarBean;
import cn.qssq666.robot.bean.ViolationRecordBean;
import cn.qssq666.robot.bean.ViolationWordRecordBean;
import cn.qssq666.robot.constants.Cns;

/**
 * Created by qssq on 2017/12/11 qssq666@foxmail.com
 */

public class InitUtils {

    public static void initTableAndInsertDefaultValue(DBUtils dbUtils) {
        //回复关键词
        boolean result = DBHelper.getKeyWordDBUtil(dbUtils).createTable(ReplyWordBean.class);
        if (result) {
            RobotUtil.insertDefaultWord(DBHelper.getKeyWordDBUtil(dbUtils));
            //LogUtil.writeLog("insert default replyword");
        }

        //群白名单 Cns.GROUP_CONFIG_TABLE
//TABLE_WHTE_NAME_TABLEgroupconfig
        String groupTableOld = "TABLE_WHTE_NAME_TABLEAccountBean";
//        String groupTableOld = DBUtils.getTableNameStatic("TABLE_WHTE_GROUP_NAME_TABLE", AccountBean.class);
        boolean exist = DBUtils.ToolHelper.tableExist(dbUtils.getDb(), groupTableOld);
        //LogUtil.writeLog("old table " + groupTableOld + " is exist:" + exist);

        if (exist) {//标存在,修改为指定new table
            result = false;
            String groupTableNew = DBUtils.getTableNameStatic(Cns.TABLE_WHTE_GROUP_NAME_TABLE, GroupWhiteNameBean.class);
            if (DBUtils.ToolHelper.tableExist(dbUtils.getDb(), groupTableNew)) {//如果有先删除 虽然是不可能发生的发生的事情.但是为了提高程序的健壮性
                DBUtils.ToolHelper.deleteTable(dbUtils.getSQLiteDatabaseObj(), groupTableNew);
            }
            DBUtils.ToolHelper.reNameTable(dbUtils.getDb(), groupTableOld, groupTableNew);
            DBUtils.insertNewCloumnFromClasss(DBHelper.getQQGroupWhiteNameDBUtil(dbUtils), GroupWhiteNameBean.class, dbUtils.getDb());
            //改表还需要添加字段,太麻烦了.
//            result = DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).createTable(GroupWhiteNameBean.class);//作废了
            //LogUtil.writeLog("发现老表" + groupTableOld + "存在，尝试导入老表数据到new new table " + groupTableNew + ",result:");
        } else {
            result = DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).createTable(GroupWhiteNameBean.class);//作废了
            DBUtils.insertNewCloumnFromClasss(DBHelper.getQQGroupWhiteNameDBUtil(dbUtils), GroupWhiteNameBean.class, dbUtils.getDb());//修复字段

        }
//        result = DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).createTable(AccountBean.class);

        if (result) {
            //LogUtil.writeLog("insert default whitenams");
            RobotUtil.insertDefaulWhiteNames(DBHelper.getQQGroupWhiteNameDBUtil(dbUtils));
        } else {
            if (!DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(GroupWhiteNameBean.class), Cns.FIELD_ENABLE)) {//image
                DBHelper.getQQGroupWhiteNameDBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(GroupWhiteNameBean.class), Cns.FIELD_ENABLE);
            }
        }
        //忽略回复的QQ
        result = DBHelper.getIgnoreQQDBUtil(dbUtils).createTable(AccountBean.class);
        if (result) {
            //LogUtil.writeLog("insert default ignores qq");
            RobotUtil.insertDefaulIgnores(DBHelper.getIgnoreQQDBUtil(dbUtils));
        } else {

            if (!DBHelper.getIgnoreQQDBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(AccountBean.class), Cns.FIELD_ENABLE)) {//image
                DBHelper.getIgnoreQQDBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(AccountBean.class), Cns.FIELD_ENABLE);
            }

        }

        //忽略不禁言的QQ
        result = DBHelper.getIgnoreGagDBUtil(dbUtils).createTable(AccountBean.class);
        if (result) {
            //LogUtil.writeLog("insert default ignores qq");
            RobotUtil.insertDefaulIgnores(DBHelper.getIgnoreGagDBUtil(dbUtils));
        } else {

            if (!DBHelper.getIgnoreGagDBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(AccountBean.class), Cns.FIELD_ENABLE)) {
                DBHelper.getIgnoreGagDBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(AccountBean.class), Cns.FIELD_ENABLE);
            }

        }
        //红包流水记录
        result = DBHelper.getRedPacketBUtil(dbUtils).createTable(RedPacketBean.class);
        if (result) {
            //LogUtil.writeLog("insert default redpacket");
            RobotUtil.insertDefaultRedPacket(DBHelper.getRedPacketBUtil(dbUtils));
        } else {

            if (!DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_TYPE)) {//image
                DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_TYPE);
            }
           /* if (!DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_GROUPNICKNAME)) {//image
                DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_GROUPNICKNAME);
            }
            if (!DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_NICKNAME)) {//image
                DBHelper.getRedPacketBUtil(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(RedPacketBean.class), Cns.FIELD_NICKNAME);
            }*/

        }
        //floor record
        result = DBHelper.getFloorUtil(dbUtils).createTable(FloorBean.class);
        if (result) {
            //LogUtil.writeLog("init default floor");
        }


        //QQ群管理黑名单
        result = DBHelper.getQQGroupManagerDBUtil(dbUtils).createTable(TwoBean.class);
        if (result) {
            //LogUtil.writeLog("insert default manager");
            RobotUtil.insertDefaultQQGroupAdmin(DBHelper.getQQGroupManagerDBUtil(dbUtils));
        }

        //初始化昵称
        result = DBHelper.getNickNameUtil(dbUtils).createTable(NickNameBean.class);
        if (result) {
            //LogUtil.writeLog("insert default nickname info");
            RobotUtil.insertDefaultNickInfo(DBHelper.getNickNameUtil(dbUtils));
        }

        //变量
        result = DBHelper.getVarTableUtil(dbUtils).createTable(VarBean.class);
        if (result) {
            //LogUtil.writeLog("insert default nickname info");
            RobotUtil.insertDefaultVarInfo(DBHelper.getVarTableUtil(dbUtils));
        }
        //群管理员表
        result = DBHelper.getVarTableUtil(dbUtils).createTable(GroupAdaminBean.class);
        if (result) {
            //LogUtil.writeLog("insert default nickname info");
            RobotUtil.insertDefaultGroupAdmin(DBHelper.getGroupAdminTableUtil(dbUtils));
        }


        //超级管理员
        result = DBHelper.getSuperManager(dbUtils).createTable(AdminBean.class);
        if (result) {
            //LogUtil.writeLog("insert super manager");
            RobotUtil.insertDefaultAdmin(DBHelper.getSuperManager(dbUtils));

        } else {
            DBUtils.insertNewCloumnFromClasss(DBHelper.getSuperManager(dbUtils), AdminBean.class, dbUtils.getDb());//修复字段

        }


        //违禁词 禁言管理
        result = DBHelper.getGagKeyWord(dbUtils).createTable(GagAccountBean.class);
        if (result) {
            //LogUtil.writeLog("insert contraband  words");
            RobotUtil.insertDefaultGagWrod(DBHelper.getGagKeyWord(dbUtils));
        } else {
            if (!DBHelper.getGagKeyWord(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(GagAccountBean.class), Cns.FIELD_ACTION)) {//image
                DBHelper.getGagKeyWord(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(GagAccountBean.class), Cns.FIELD_ACTION);
            }
            if (!DBHelper.getGagKeyWord(dbUtils).getSQLiteDatabaseObj().columnExist1(ReflectUtils.getTableNameByClass(GagAccountBean.class), Cns.FIELD_SILENCE)) {//image
                DBHelper.getGagKeyWord(dbUtils).getSQLiteDatabaseObj().addColumn(ReflectUtils.getTableNameByClass(GagAccountBean.class), Cns.FIELD_SILENCE);
            }

            DBUtils.insertNewCloumnFromClasss(DBHelper.getGagKeyWord(dbUtils), GagAccountBean.class, dbUtils.getDb());


        }


        //群违规记录

        result = DBHelper.getViolationRecordUtil(dbUtils).createTable(ViolationRecordBean.class);
        if (result) {
            //LogUtil.writeLog("insert Violation   words count succ");
            RobotUtil.insertDefaultGagWrod(DBHelper.getGagKeyWord(dbUtils));
        } else {

        }

        result = DBHelper.getViolationWordHistoryRecordUtil(dbUtils).createTable(ViolationWordRecordBean.class);
        if (result) {
            //LogUtil.writeLog("insert Violation   words record word column succ");
            RobotUtil.insertDefaultGagWrod(DBHelper.getGagKeyWord(dbUtils));
        } else {

        }


    }

    static {
        LogUtil.importPackage();

    }
}
