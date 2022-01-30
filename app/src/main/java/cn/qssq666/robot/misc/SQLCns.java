package cn.qssq666.robot.misc;

import androidx.annotation.Keep;

@Keep
public interface SQLCns {
    String SQL_CONSTANT_ENABLE_NET_WORK ="update groupconfig set bannedword=0 where account=" + "\"$g\"";
    String SQL_CONSTANT_DISENABLE_NET_WORK = "update groupconfig set bannedword=1 where account=" +
            "\"$g\"";
    String SQL_CONSTANT_ENABLE_GROUP = "select account as group,remark,disable from groupconfig";
}
