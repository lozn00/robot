package cn.qssq666.robot.utils;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qssq666.db.DBUtils;
import cn.qssq666.robot.bean.NickNameBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.service.RemoteService;

/**
 * Created by qssq on 2017/12/23 qssq666@foxmail.com
 * 2017年12月24日 00:44:58 如果有机会的话可以做一个 bean toString json工具类
 */

public class NickNameUtils {


    public static boolean disableNickNameCache = false;
    private static HashMap<String, List<NickNameBean>> nickNameMap = new HashMap<>();

    /**
     * 如果只需要查询昵称的话
     *
     * @param group
     * @param qq
     * @return
     */
    public static String queryMatchNickname(String group, String qq) {
        return queryMatchNickname(group, qq, true);
    }

    /**
     * 如果查询失败 则从默认昵称取，如果也没有则 返回qq
     *
     * @param group
     * @param qq
     * @param defaultNickname
     * @return
     */

    public static String queryMatchNicknameAndNullReturnDefault(String group, String qq, String defaultNickname) {
        String s = queryMatchNickname(group, qq, true);
        if (s == null) {
            if (defaultNickname != null) {
                return defaultNickname;
            }
            return qq;
        } else {
            return s;
        }

    }

    public static String queryMatchNickname(String group, String qq, boolean allorEmpty) {
        List<NickNameBean> nickNameBeans = nickNameMap.get(qq);
        if (nickNameBeans == null) {
            String nickname = queryNicknameFromHost(qq, group, -1);
            if (!allorEmpty) {

                if (TextUtils.isEmpty(nickname)) {
                    return qq;
                }
                return nickname;
            }
            return nickname;
        } else {
            synchronized (nickNameBeans) {
                NickNameBean matchBean = null;
                for (NickNameBean bean : nickNameBeans) {

                    if (TextUtils.isEmpty(bean.getTroopno()) && TextUtils.isEmpty(group)
                            || (bean.getTroopno() != null && bean.getTroopno().equals(group))) {
                        matchBean = bean;
                        break;
                    }
                }
                if (matchBean == null) {
                    matchBean = nickNameBeans.get(0);
                }
                String nickname = matchBean.getNickname();


                if (TextUtils.isEmpty(nickname)) {

                    nickname = queryNicknameFromHost(qq, group, -1);

                }


                if (!allorEmpty && TextUtils.isEmpty(nickname)) {
                    return qq;
                } else {
                    return nickname;

                }

            }
        }
    }


    public static String queryNicknameFromHost(String qq, String group, int istroop) {
        if (RemoteService.isIsInit()) {
            String nickname = RemoteService.queryNickname(qq, group, istroop);
            return nickname;
        } else if (RobotContentProvider.getInstance().isAsPluginLoad() && RobotContentProvider.getInstance().mHostControlApi != null) {
            if (istroop == -1) {
                istroop = !qq.equals(group) ? 1 : 0;
            }
            return RobotContentProvider.getInstance().mHostControlApi.queryNickName(istroop, qq, group);

        }

        return null;
    }

// Cns.formatNickname(gagItem.getSenderuin(), gagItem.getNickname())

    public static String formatNickname(DBUtils dbUtils, IMsgModel item) {
        return formatNickname(dbUtils, item.getFrienduin(), item.getSenderuin(), item.getNickname());
    }

    public static String formatNickname(IMsgModel item) {
        return formatNickname(RobotContentProvider.getDbUtils(), item.getFrienduin(), item.getSenderuin(), item.getNickname());
    }

    public static String formatNickname(String group, String qq) {
        if("0".equals(qq)){
            return "所有人";
        }
        return formatNickname(RobotContentProvider.getDbUtils(), group, qq, "");
    }

    public static String formatNickname(String qq) {
        return formatNickname(RobotContentProvider.getDbUtils(), qq, qq, "");
    }

    public static String formatNickname(String group, String qq, String nickname) {
        return formatNickname(RobotContentProvider.getDbUtils(), group, qq, nickname);
    }

    public static String formatNickname(DBUtils dbUtils, String group, String qq, String nickname) {
        String nicknameDb = queryMatchNickname(group, qq);
        if (TextUtils.isEmpty(nickname) || nickname.equals(qq)) {//如果是和qq号码相同就从数据库取，而数据库存储也确保了不能和qq号码相同


            if (nicknameDb != null) {
                nickname = nicknameDb;
            }


        } else {
            if (!nickname.equals(nicknameDb)) {
                if (TextUtils.isEmpty(nickname)) {
                    //LogUtil.writeLog("忽略昵称为空的qq存档 " + qq + ",所在群:" + group);
                } else if (nickname.equals(qq)) {

                    //LogUtil.writeLog("忽略昵称和qq号码相同的qq存档 " + qq + ",所在群:" + group);
                } else {

                    if (!disableNickNameCache) {
                        updateOrWriteNickname(dbUtils, group, qq, nickname);
                    }


                }
            }
        }
        if (TextUtils.isEmpty(nickname)) {


            nickname = queryNicknameFromHost(qq, group, -1);
            if (TextUtils.isEmpty(nickname)) {
                return qq;
            }

            return qq;
        }
        return formatNicknameFromNickName(qq, nickname);

    }

    public static String formatNicknameFromNickName(String qq, String nickname) {
        return String.format("%s(%s)", qq, nickname);
    }

    private static void updateOrWriteNickname(DBUtils dbUtils, String group, String qq, String nickname) {


        putInfoToMeoryPool(group, qq, nickname);
        if (TextUtils.isEmpty(group)) {
            group = qq;
        }

        if (disableNickNameCache) {
            return;
        }

        List<NickNameBean> nickNameBeans = DBHelper.getNickNameUtil(dbUtils).queryAllByField(NickNameBean.class, FieldCns.FIELD_ACCOUNT, qq, FieldCns.TROOPNO, group);
        NickNameBean bean = generateNickNameBean(group, qq, nickname);
        if (disableNickNameCache) {
            return;
        }


        if (nickNameBeans == null)
            if (nickNameBeans != null && nickNameBeans.size() > 0) {

                long update = DBHelper.getNickNameUtil(dbUtils).update(bean);

                //LogUtil.writeLog("升级昵称数据,数据库已经存在" + nickNameBeans.get(0).getAccount() + ",老昵称为" + nickNameBeans.get(0).getNickname() + "，更新昵称为" + nickname + ",数据库结果:" + nickNameBeans.size() + ",update result:" + update);
            } else {
                if (qq.equals(group)) {
                    bean.setIstroop(1);

                }
                long insert = DBHelper.getNickNameUtil(dbUtils).insert(bean);
                //LogUtil.writeLog("插入昵称数据,,insert result:" + insert);
            }
    }

    private static void putInfoToMeoryPool(String group, String qq, String nickname) {

        List<NickNameBean> nickNameBeans = nickNameMap.get(qq);
        NickNameBean bean = generateNickNameBean(group, qq, nickname);
        if (nickNameBeans == null) {
            nickNameBeans = new ArrayList<>();
            nickNameBeans.add(bean);
            synchronized (nickNameMap) {
                nickNameMap.put(qq, nickNameBeans);
            }

        } else {

            synchronized (nickNameBeans) {
                if (nickNameBeans.contains(bean)) {
                    //LogUtil.writeLog("搞啥子,发现包含" + nickNameBeans);
                } else {
                    nickNameBeans.add(bean);
                }

            }

        }
      /*  if (TextUtils.isEmpty(group) || group.equals(qq)) {
            nickNameMap.put(qq, nickname);
        } else {
            nickNameMap.put(bean.getAccount() + "")
        }*/


    }

    static {
        LogUtil.importPackage();

    }

    @NonNull
    private static NickNameBean generateNickNameBean(String group, String qq, String nickname) {
        NickNameBean bean = new NickNameBean();
        bean.setNickname(nickname);
        bean.setTroopno(group);
        bean.setAccount(qq);
        return bean;
    }


    public static void initNicknames(DBUtils dbUtils) {
        if (disableNickNameCache) {
            return;
        }
        List<NickNameBean> beans = DBHelper.getNickNameUtil(dbUtils).queryAll(NickNameBean.class);
        if (beans == null) {
            return;
        }
        for (NickNameBean bean : beans) {
            putInfoToMeoryPool(bean.getTroopno(), bean.getAccount(), bean.getNickname());


        }
    }


    public static void clearFromMemory() {
        nickNameMap.clear();
    }
}
