package cn.qssq666.robot.utils;
import android.text.TextUtils;

import java.util.List;

import androidx.annotation.NonNull;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.interfaces.IAccountBean;
import cn.qssq666.robot.interfaces.IDisable;

/**
 * Created by qssq on 2017/11/29 qssq666@foxmail.com
 */

public class AccountUtil {

    public static boolean removeAccount(List<? extends IAccountBean> list, String accont) {
        if (list == null) {
            return false;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            IAccountBean bean = list.get(i);
            if (bean.getAccount().equals(accont)) {
                list.remove(i);
                return true;
            }

        }
        return false;

    }

    public static boolean isContainByArray(String findKey, String splitSymobol, String strs) {

        return isContainByArray(findKey, strs == null ? null : formatSplitArr(splitSymobol, strs));
    }

    public static boolean isContainByArray(String findKey, String[] arrs) {
        if (arrs == null || arrs.length == 0) {
            return false;
        } else {

            for (String current : arrs) {
                if (findKey.equals(current)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String addValueByArray(String findKey, String splitSymobol, String args) {
        if (args != null) {
            if (args.startsWith(splitSymobol)) {
                return findKey + args;
            } else {
                return findKey + splitSymobol + args;
            }
        } else {
            return findKey;
        }
    }


    public static String removeValueByArray(String findKey, String splitSymobol, String args) {

        if (args == null || TextUtils.isEmpty(args)) {
            return "";
        } else {
            String[] arrs = null;
            arrs = formatSplitArr(splitSymobol, args);

            if (arrs == null || arrs.length == 0) {
                return "";
            } else {
                StringBuffer sb = new StringBuffer();
                for (String current : arrs) {

                    if (current.equals(findKey)) {
                        continue;
                    }
                    sb.append(current);
                    sb.append(splitSymobol);

                }
                return sb.toString();
            }
        }
    }

    @NonNull
    public static String[] formatSplitArr(String splitSymobol, String args) {
        String[] arrstr;

        if (splitSymobol.equals(",")) {
            arrstr = args.split("," );

        } else if (splitSymobol.equals("|")) {
            arrstr = args.split("\\|" );//ignore_include

        } else {
            arrstr = args.split(splitSymobol);

        }
        return arrstr;
    }


    public static boolean isContainAccount(List<? extends IAccountBean> list, String account) {
        return isContainAccount(list, account, false);

    }

    public static boolean isContainAccount(List<? extends IAccountBean> list, String account, boolean checkDisable) {
        return findAccount(list, account, checkDisable) != null;
    }
    // <T> T queryByID(Class<T>

    public static <T extends IAccountBean> T findAccount(List<T> list, String account, boolean checkDisable) {
        if (list == null) {
            return null;
        }
        for (IAccountBean bean : list) {
            if (bean.getAccount().equals(account)) {
                if (!checkDisable) {
                    return (T) bean;
                } else {
                    if (bean instanceof IDisable) {

                        if (((IDisable) bean).isDisable()) {
                            LogUtil.writeLog("包含 但是被禁用了，因此返回告知不包含  account " + account);
                            return null;
                        } else {
                            return (T) bean;//true 包含
                        }
                    }

                }
            }
        }

        return null;

    }

    static {
        LogUtil.importPackage();

    }


    public static GroupWhiteNameBean createGroupWhiteNameBeanFrom(RobotContentProvider contentProvider, String group) {
        GroupWhiteNameBean account = new GroupWhiteNameBean();
        account.setAccount(group);
        account.setLocalword(contentProvider.mCfBaseEnableLocalWord);
        account.setRedpackettitlebanedword(true);
        account.setJoingroupword(contentProvider.mCfGroupJoinGroupReplyStr);
        account.setAdmin(true);
        account.setBannedword(contentProvider.mCFBaseEnableCheckKeyWapGag);
        account.setNetword(contentProvider.mCfBaseEnableNetRobotGroup);
        account.setBanpasswordredpacket(true);
        account.setFrequentmsg(true);
        account.setFrequentmsgcount(IGnoreConfig.frequentMsgDistanceSecondCount);
        account.setFrequentmsgduratiion(IGnoreConfig.frequentMsgDistanceDurationSecond * 60);
        account.setGroupnickanmegagtime((int) IGnoreConfig.SHUAPIN_GAG_SECOND);
        return account;
    }
}
