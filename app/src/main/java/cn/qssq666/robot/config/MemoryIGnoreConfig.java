package cn.qssq666.robot.config;

import java.util.HashMap;

import cn.qssq666.robot.bean.MsgItem;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 * 临时屏蔽
 */

public class MemoryIGnoreConfig {
    public static HashMap<String, MsgItem> getIgnorePersonMap() {
        return ignorePersonMap;
    }

    static HashMap<String, MsgItem> ignorePersonMap = new HashMap<>();

    public static HashMap<String, MsgItem> getIgnoreGroupMap() {
        return ignoreGroupMap;
    }

    static HashMap<String, MsgItem> ignoreGroupMap = new HashMap<>();

    public static MsgItem addIgnorePerson(String ignorePerson, MsgItem item) {
        return ignorePersonMap.put(ignorePerson, item);
    }

    public static MsgItem removeIgnorePerson(String ignorePerson) {
        return ignorePersonMap.remove(ignorePerson);
    }

    public static boolean isTempIgnorePerson(String ignorePerson) {
        return ignorePersonMap.containsKey(ignorePerson);
    }

    public static MsgItem addIgnoreGroupNo(String ignorePerson, MsgItem item) {
        return ignoreGroupMap.put(ignorePerson, item);
    }

    public static MsgItem removeIgnoreGroupNo(String ignorePerson) {
        return ignoreGroupMap.remove(ignorePerson);
    }

    public static boolean isIgnoreGroupNo(String ignorePerson) {
        return ignoreGroupMap.containsKey(ignorePerson);
    }
}
