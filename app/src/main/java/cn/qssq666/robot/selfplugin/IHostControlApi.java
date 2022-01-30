package cn.qssq666.robot.selfplugin;

import java.util.List;

/**
 * Created by qssq on 2018/8/26 qssq666@foxmail.com
 */
public interface IHostControlApi {
    boolean executeAndReturnBooleanResult(int action, Object... args);
    Object executeAndReturnObjectResult(int action, Object... args);
    Object[] executeAndReturnObjectArrResult(int action, Object... args);
    String executeAndReturnStringResult(int action, Object... args);
    String queryNickName(int istroop, String qq, String group);
    String queryGroupName(String group);
    List<IInfoModel> queryQQMember(int istroop, String group);
    List<IInfoModel> queryQQGroupMember();
}
