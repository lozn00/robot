package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.bean.AtBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.MsgReCallUtil;
import cn.qssq666.robot.business.MsgTyeUtils;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;

/**
 * Created by qssq on 2017/12/25 qssq666@foxmail.com
 */

public class BatchUtil {

    public static void atFloorData(RobotContentProvider contentProvider, MsgItem robotItem, List<MsgItem> itemList, String message) {
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            MsgItem item = itemList.get(i);
            if (MsgTyeUtils.isSelfMsg(item)) {
                continue;
            }
            String nickname = NickNameUtils.queryMatchNickname(item.getFrienduin(), item.getSenderuin(), false);

            nickname = "@" + nickname;
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(item.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);
            if (i != itemList.size() - 1) {
                sb.append(" 、  ");

            }
        }


        sb.append(" " + message);


        if (ConfigUtils.isDisableAtFunction(contentProvider)) {
            MsgReCallUtil.notifyJoinMsgNoJumpDisableAt(contentProvider, sb.toString(), robotItem);

        } else {
            MsgReCallUtil.notifyAtMsgJumpB(contentProvider, sb.toString(), atBeanList, robotItem);

        }
    }


    /**
     * @return
     */
    public static String buildAtNickSource(IMsgModel mainItem, List<? extends AtBeanModelI> list) {
        StringBuffer sb = new StringBuffer();
        List<AtBean> atBeanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AtBeanModelI item = list.get(i);
            if (item == null || TextUtils.isEmpty(item.getSenderuin())) {
                return "";
            }
            if (item.getSenderuin().equals(mainItem.getSelfuin())) {
                continue;
            }
            String nickname = TextUtils.isEmpty(item.getNickname()) ? " " : item.getNickname();
//            String nickname = TextUtils.isEmpty(list.)? NickNameUtils.queryMatchNickname(mainItem.getFrienduin(), item.getSenderuin(), false);
            nickname = "" + nickname;
            AtBean atBean = new AtBean();
            atBean.setNickname(nickname);
            atBean.setSenderuin(item.getSenderuin());
            atBeanList.add(atBean);
            sb.append(nickname);
            if (i != list.size() - 1) {
                sb.append(" 、  ");

            }
        }


        return sb.toString();
    }
}
