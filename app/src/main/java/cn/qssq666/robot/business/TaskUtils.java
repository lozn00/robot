package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;import android.os.Looper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.config.IGnoreConfig;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.utils.BatchUtil;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.NickNameUtils;
import cn.qssq666.robot.utils.ParseUtils;

/**
 * Created by qssq on 2017/12/17 qssq666@foxmail.com
 * 明天写违法踢人任务倒计时逻辑 //
 */

public class TaskUtils {


    public static void insertBatchGagTask(RobotContentProvider contentProvider, MsgItem item, String group, String cancelCmd, List<GroupAtBean> second, long taskms, long duration) {

        MultiBean multiBean = new MultiBean();
        multiBean.setList(second);
        multiBean.setObject(duration);
        joinTask(cancelCmd, item, TaskType.GAG_BATCH, taskms, multiBean);


    }

    public static int immediateExecute() {
        Set<Map.Entry<String, List<TaskBean>>> entries = getTask();



        int count=entries.size();

        for (Map.Entry<String, List<TaskBean>> entry : entries) {

            List<TaskBean> value = entry.getValue();
            for (TaskBean bean : value) {
                handler.removeCallbacks(bean.getRunnable());
                bean.runnable.run();

            }
        }



        taskBeanHashMap.clear();
        return count;
    }

    public static boolean hasTask() {
        return taskBeanHashMap!=null&&!taskBeanHashMap.isEmpty();
    }

    public static class MultiBean {
        public List<? extends GroupAtBean> getList() {
            return list;
        }

        public void setList(List<? extends GroupAtBean> list) {
            this.list = list;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        List<? extends GroupAtBean> list;
        Object object;
    }

    public interface TaskType {
        int KICK = 0;
        int GAG_BATCH = 3;
        int GAG = 1;
        int SEND_MSG = 2;
    }

    public static RobotContentProvider robotContentProvider;

    /**
     * @param provider
     * @param key
     * @param item
     * @Deprecated 默认值不可靠
     */
    public static void insertRedpacketKickTask(RobotContentProvider provider, final String key, IMsgModel item) {
        insertRedpacketKickTask(provider, key, item);
    }

    public static void insertRedpacketKickTask(RobotContentProvider provider, final String key, IMsgModel item, long ms) {
        insertRedpacketKickTask(provider, key, item, ms, false);
    }

    public static void insertRedpacketKickTask(RobotContentProvider provider, final String key, IMsgModel item, long ms, boolean forver) {
        TaskUtils.robotContentProvider = provider;
        final IMsgModel kickItem = item.clone();
        joinTask(key, kickItem, TaskType.KICK, ms, forver);


    }

    /**
     * @param provider
     * @param key
     * @param item
     * @param ms        执行毫秒
     * @param gagsecond 禁言秒
     */
    public static void insertGagTask(RobotContentProvider provider, final String key, IMsgModel item, long ms, long gagsecond) {
        TaskUtils.robotContentProvider = provider;
        final IMsgModel kickItem = item.clone();
        joinTask(key, kickItem, TaskType.GAG, ms, gagsecond);

    }

    public static void insertGagMultiTaskFromAtMsg(RobotContentProvider provider, final String key, IMsgModel item, List<? extends GroupAtBean> list, long ms, long gagsecond) {
        TaskUtils.robotContentProvider = provider;
        MultiBean bean = new MultiBean();
        bean.setList(list);
        bean.setObject(gagsecond);


        joinTask(key, item, TaskType.GAG_BATCH, ms, bean);

    }


    public static void insertSendMsgKickTask(RobotContentProvider provider, final String key, IMsgModel item, long ms, String message) {
        TaskUtils.robotContentProvider = provider;
        final IMsgModel kickItem = item.clone();
        joinTask(key, kickItem, TaskType.SEND_MSG, ms, message);

    }

    /**
     * @param key
     * @Deprecated 默认值不可靠
     */

    public static boolean joinTask(final String key, final IMsgModel kickItem, int type) {

        return joinTask(key, kickItem, type, ParseUtils.parseMinuteToMs(IGnoreConfig.REDPACKET_KICK_DELAY_TIME_MINUTE));
    }

    public static boolean joinTask(final String key, final IMsgModel kickItem, final int type, long ms) {
        return joinTask(key, kickItem, type, ms, null);

    }

    public static boolean joinTask(final String key, final IMsgModel kickItem, final int type, long ms, final Object extraParam) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TaskType.KICK:
                        MsgReCallUtil.notifyKickPersonMsgNoJump(robotContentProvider, kickItem, (Boolean) extraParam);
                        MsgReCallUtil.notifyJoinReplaceMsgJump(robotContentProvider, "时间到,执行 请求踢出" + NickNameUtils.formatNickname(kickItem) + "任务", kickItem);
                        break;
                    case TaskType.GAG:
                        MsgReCallUtil.notifyGadPersonMsgNoJump(robotContentProvider, (Long) extraParam, kickItem);
                        MsgReCallUtil.notifyJoinReplaceMsgJump(robotContentProvider, "时间到,执行 请求禁言" + NickNameUtils.formatNickname(kickItem) + DateUtils.getGagTime((Long) extraParam) + "任务", kickItem);


                        break;
                    case TaskType.SEND_MSG:
//                        MsgReCallUtil.notifyJoinReplaceMsgJump(robotContentProvider, "时间到,执行 发送消息" + NickNameUtils.formatNickname(kickItem) + "任务", kickItem);
                        MsgReCallUtil.notifyHasDoWhileReply(robotContentProvider, extraParam + "", kickItem);
                        break;
                    case TaskType.GAG_BATCH:
                        MultiBean bean = (MultiBean) extraParam;
                        List<? extends GroupAtBean> list = bean.getList();
                        if (list != null) {
                            for (GroupAtBean atBean : list) {

                                IMsgModel msgItem = kickItem.clone();
                                msgItem.setSenderuin(atBean.getUin());
                                MsgReCallUtil.notifyGadPersonMsgNoJump(TaskUtils.robotContentProvider, (Long) bean.getObject(), msgItem);


                            }
                            String str = BatchUtil.buildAtNickSource(kickItem, list);
                            MsgReCallUtil.notifyJoinReplaceMsgJump(robotContentProvider, "时间到,执行 请求批量禁言" + str + " 时间为" + DateUtils.getGagTime((Long) extraParam) + "任务", kickItem);
                        }


                        break;
                }
                taskBeanHashMap.remove(key);
                clrearTaskByTitleAndMatchAllKey(key);
            }
        };

//        handler.postDelayed(runnable, 1000 * 60 * ms);
        TaskBean taskBean = new TaskBean();
        taskBean.setMsgItem(kickItem);
        taskBean.setRunnable(runnable);
        taskBean.setTime(System.currentTimeMillis());
        taskBean.setType(type);

        List<TaskBean> list = taskBeanHashMap.get(key);
        handler.postDelayed(runnable, ms);
        if (list == null) {
            list = new ArrayList<>();
            list.add(taskBean);
            taskBeanHashMap.put(key, list);
            return false;
        } else {
            list.add(taskBean);
            return true;

        }

    }


    public static int clrearAllTask() {
        int size = taskBeanHashMap.size();
        int count = 0;
        for (Map.Entry<String, List<TaskBean>> entry : taskBeanHashMap.entrySet()) {

            List<TaskBean> value = entry.getValue();
            if (!value.isEmpty()) {
                count += value.size();
            }
        }
        taskBeanHashMap.clear();
        handler.removeCallbacksAndMessages(null);
        return count;
    }

    public static Set<Map.Entry<String, List<TaskBean>>> getTask(){
        Set<Map.Entry<String, List<TaskBean>>> entries = taskBeanHashMap.entrySet();
        return entries;
    }


    /**
     * 根据群号 发送者QQ进行匹配
     *
     * @param title
     * @return
     */
    public static int clrearTaskByTitleAndMatchAllKey(String title) {
//        List<TaskBean> findList = taskBeanHashMap.get(title);
        int count = 0;


        for (Map.Entry<String, List<TaskBean>> entry : taskBeanHashMap.entrySet()) {
            List<TaskBean> findList = entry.getValue();
            if (findList != null && findList.size() > 0) {
                for (int i = 0; i < findList.size(); i++) {
                    TaskBean taskBean = findList.get(i);
                    if (taskBean.getMsgItem().getSenderuin().equals(title) || taskBean.getMsgItem().getFrienduin().equals(title)) {
                        findList.remove(i);
                        handler.removeCallbacks(taskBean.getRunnable());
                        count++;
                    }
                }


                if (findList.isEmpty()) {
                    taskBeanHashMap.remove(entry.getKey());
                }

            } else {
           /*     taskBeanHashMap.remove(title);//集合列表没必要清空虽然有一定优化内存作用但是为了统计总数还是清除吧111
                TaskBean taskBean = findList.get(0);
                handler.removeCallbacks(taskBean.getRunnable());*/
            }

        }


        return count;
    }

    public static boolean clrearTaskByTitle(String title) {
        return clrearTaskByTitleAndFetchCount(title)>0;


    }
    public static int clrearTaskByTitleAndFetchCount(String title) {
        List<TaskBean> remove = taskBeanHashMap.remove(title);
        if (remove != null) {
            int size = remove.size();
            for (TaskBean taskBean : remove) {
                handler.removeCallbacks(taskBean.getRunnable());
            }
            return size;
        } else {
            return 0;
        }


    }

    public static android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());


    //1分钟内的的不算 所以当收到
    public static boolean isRecentPasswordMsg(String title) {

        List<TaskBean> taskBeanList = taskBeanHashMap.get(title);
        if (taskBeanList != null && taskBeanList.size() > 0) {
            TaskBean taskBean = taskBeanList.get(0);
            long timeDistance = DateUtils.getTimeDistance(DateUtils.TYPE_SECOND, new Date().getTime(), taskBean.getTime());
            if (timeDistance <= 50) {
                return true;
            }

        }

        return false;
 /*       for (Map.Entry<String, TaskBean> entry : taskBeanHashMap.entrySet()) {

            if (entry.getKey().equals(title)) {


            }
        }
        return false;
*/
    }


    static HashMap<String, List<TaskBean>> taskBeanHashMap = new HashMap<>();

    static class TaskBean {

        public IMsgModel getMsgItem() {
            return msgItem;
        }

        public void setMsgItem(IMsgModel msgItem) {
            this.msgItem = msgItem;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        IMsgModel msgItem;
        Runnable runnable;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        private long time;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        /**
         * 暂时只支持踢人操作，因为如果是红包口令导致的禁言，可以批量楼层解除禁言
         */
        private int type = TaskType.KICK;
    }
}
