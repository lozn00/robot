package cn.qssq666.robot.activity.datamanager;

import cn.qssq666.CoreLibrary0;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.ClearUtil;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DateUtils;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.PairFix;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.StringUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class GagKeyWordActivity extends BaseAccountManagerActivity<GagAccountBean> {

    @Override
    protected String[] getLongPressMenu() {
        String[] OPERA_MENU = {"编辑", "删除","测试"};
        return OPERA_MENU;
    }

    @Override
    protected void onLongOtherClick(int position, int which) {
        switch (which){
            case 2:
                if(position<0||position>=getAdapter().getList().size()){
                    Toast.makeText(this, "无效的itemPosition "+position, Toast.LENGTH_SHORT).show();
                    return;
                }

                        GagAccountBean gagAccountBean = getAdapter().getList().get(position);
                if(gagAccountBean.isDisable()){
                    Toast.makeText(this, "此规则已被禁用，请先打开!", Toast.LENGTH_SHORT).show();
                }
                DialogUtils.showEditDialog(GagKeyWordActivity.this, "请输入要测试的内容", "测试", new INotify<String>() {
                    @Override
                    public void onNotify(String param) {
                        PairFix<GagAccountBean, String> accountBean = RobotContentProvider.getInstance().getGagAccountBeanStringPairFixByGagAccountBean(param, gagAccountBean);
                        if(accountBean==null){
                           DialogUtils.showDialog(GagKeyWordActivity.this, "不匹配!");
                        }else{

                           DialogUtils.showDialog(GagKeyWordActivity.this, "匹配!"+accountBean.first);
                        }
                    }
                });
                break;
        }
    }

    private static final String TAG = "违禁词管理";

    protected boolean onQueryExist(String account) {

        return DBHelper.getGagKeyWord(AppContext.dbUtils).queryColumnExist(GagAccountBean.class, Fields.FIELD_ACCOUNT, account);


    }

    @Override
    protected boolean onClickHelpMenu(MenuItem item) {
        DialogUtils.showDialog(this, "fullreg表示每一个字进行匹配比如fullreg你麻痹会进行逐个匹配如fullreg操尼玛,操你或者，操你老妈或操ni妈，实际上会把正则里面每一个文字加上.*? 所以达到了全局匹配效果，\ngreg 代表整个语句都进行匹配也就是没有任何分隔符也就是一条item只有一个正则,而reg开头只是局部的,以每一个分隔符为界限,可以同时添加多个正则表达式，而这个分隔符新版本增加了自定义功能，避免分隔符和正则表达式的符号冲突。为了方便替换分隔符，也提供了批量替换符号功能。\n通常冲突的情况下用greg代替比较合适。\n如果不需要正则那么就是默认的用分隔符分割开来，不需要添加正则表达式，是不会识别的，除非有声明正则表达式前缀。");
        return true;
    }

    @Override
    protected boolean onClickMenu(MenuItem item, int id) {
        switch (id) {
            case R.id.action_all_remove_silence: {
                boolean silence = false;
                updaeAllSilence(silence);
            }
            break;
            case R.id.action_copy_split:
                AppUtils.copy(this,ClearUtil.wordSplit);
                Toast.makeText(this, "复制成功，默认分隔符:"+ClearUtil.wordSplit, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_all_silence: {

                boolean silence = true;
                updaeAllSilence(silence);
            }

            break;

            case R.id.action_split_replace:


                if (adapter.getItemCount() == 0) {
                    AppContext.showToast("没有数据");
                    return true;
                }
                DialogUtils.showEditDialog(GagKeyWordActivity.this, "本操作会把所有记录的什么一个词替换为默认为分割词【"+ClearUtil.wordSplit +"】", "", "", new INotify<String>() {
                    @Override
                    public void onNotify(String param) {


                        if (TextUtils.isEmpty(param)) {
                            AppContext.showToast("输入为空，无法替换");
                        } else {


                            List<GagAccountBean> list = adapter.getList();


                            boolean hasChange = false;
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < list.size(); i++) {
                                GagAccountBean gagAccountBean = list.get(i);

                                String accountOld = gagAccountBean.getAccount();
                                String accountNew = StringUtils.replaceAllByStr(accountOld, param, ClearUtil.wordSplit );

                                if (!accountOld.equals(accountNew) ||accountNew.length() != accountOld.length()) {


                                    hasChange = true;

                                    gagAccountBean.setAccount(accountNew);
                                    int update = DBHelper.getGagKeyWord(AppContext.dbUtils).update(gagAccountBean);
                                    sb.append("第" + (i + 1) + "条数据[" + accountOld + "]替换为[" + accountNew + "]是否成功:" + (update > 0) + "\n");

                                }
                            }

                            DialogUtils.showDialog(GagKeyWordActivity.this, "操作完成" + sb.toString());

                            if (hasChange) {
                                queryData();
                            }
                        }

                    }
                });

                break;
            case R.id.action_remove_repeat: {


                new QssqTaskFix<Object, String>(new QssqTaskFix.ICallBackImp<Object, String>() {
                    @Override
                    public String onRunBackgroundThread(Object[] params) {
                        List<GagAccountBean> list = adapter.getList();
                        if (list == null) {
                            return "数据为空！";
                        }
                        HashMap<String, GagAccountBean> askMap = new HashMap<>();
                        StringBuilder sb = new StringBuilder();

                        for (GagAccountBean bean : list) {

                            String ask = ClearUtil.removeRepeat(ClearUtil.wordSplit , bean.getAccount());
                            String accountOld = bean.getAccount();
                            if (!ask.equals(accountOld)) {

                                ;
                                bean.setAccount(ask);
                                int code = DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(bean);
                                String record = "[" + accountOld + "]调整为" + ask + ",id为" + bean.getId() + ",是否成功:" + (code > 0);
                                sb.append(record + "\n");
                                LogUtil.writeLog(TAG, record);
                                ;
                                accountOld = ask;


                            }


                            {


                                HashSet<String> splitAsk = ClearUtil.word2HashSet(ClearUtil.wordSplit , bean.getAccount());
                                if (splitAsk != null) {

                                }
                                int askCount = splitAsk.size();
                                boolean needChange = false;


                                Iterator<String> iterator = splitAsk != null ? splitAsk.iterator() : null;

                                while (iterator != null && iterator.hasNext()) {
                                    String next = iterator.next();

                                    if (askMap.get(next) != null) {

                                        if (bean != askMap.get(next)) {
//                                    ReplyWordBean replyWordBean = askMap.get(next);
                                            //删现在的原则。
                                            askCount--;
                                            GagAccountBean queryBean = askMap.get(next);
                                            if (askCount == 0) {


                                                DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(queryBean);
                                                DBHelper.getGagKeyWord(AppContext.getDbUtils()).deleteById(GagAccountBean.class, bean.getId());

                                                askCount = -1;


                                                String record = "敏感词词条彻底被清空[" + bean.getAccount() + "],id" + bean.getId();
                                                sb.append(record + "\n");
                                                LogUtil.writeLog(TAG, record);
                                                needChange = false;
                                                break;

                                            } else {

                                                String record = "发现重复的敏感词,删除id" + bean.getId() + "[" + accountOld + "]中的" + next + ",因为id" + queryBean.getId() + "中的[" + queryBean.getAccount() + "]包含了";
                                                sb.append(record + "\n");

                                                LogUtil.writeLog(TAG, record);

                                            }
                                            needChange = true;
                                            iterator.remove();


                                        } else {


                                            String record = "发现重复的敏感词,但是是自己," + bean;
                                            sb.append(record + "\n");

                                            LogUtil.writeLog(TAG, record);
                                        }

                                    } else {
                                        askMap.put(next, bean);
                                    }
                                }


                                if (needChange) {

                                    String string = ClearUtil.hashSet2String(ClearUtil.wordSplit , splitAsk);


                                    String record = "应用敏感词的改变" + bean.getId() + "的[" + accountOld + "]改变为[" + string + "]";
                                    sb.append(record + "\n");
                                    LogUtil.writeLog(TAG, record);
                                    bean.setAccount(string);

                                    int update = DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(bean);
                                    record = "更新" + bean.getId() + "的" + update;
                                    sb.append(record + "\n");

                                    LogUtil.writeLog(TAG, record);


                                }


                            }


                        }


                        return sb.toString();


                    }

                    @Override
                    public void onRunFinish(String o) {

                        queryData();


                        DialogUtils.showDialog(GagKeyWordActivity.this, TextUtils.isEmpty(o) ? "没有发现问题" : o);
                    }
                }).execute();


            }


            break;

        }
        return true;
    }

    private void updaeAllSilence(boolean silence) {
        int failtResult = 0;
        List<GagAccountBean> list = adapter.getList();
        if (list != null) {
            for (GagAccountBean bean : list) {

                bean.setSilence(silence);
                int update = DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(bean);
                if (update > 0) {
                } else {
                    bean.setSilence(!bean.isSilence());//更新失败就还原
                    failtResult++;
                }
            }
            adapter.notifyDataSetChanged();
            doDataChanageSuccEvent();
            AppContext.showToast("更新完成,失败总数：" + failtResult);

        } else {
            AppContext.showToast("数据为空!");
        }
    }

    protected String getInsertTipTitle() {
        return "添加与修改[违规词|禁言时间|是否踢出|是否静默]";
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_GAG_WORD;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_GAG;
    }

    @Override
    protected void doSubmitInsertSuccEvent(GagAccountBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_GAG);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }

    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getGagKeyWord(AppContext.dbUtils).deleteAll(GagAccountBean.class);
    }


    @Override
    protected long onInsertToDb(GagAccountBean accountBean) {
        return DBHelper.getGagKeyWord(AppContext.dbUtils).insert(accountBean);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_gag, menu);
        return true;
    }


    protected List<String> getTitleList() {
        List<String> tiplist = new ArrayList<>();
        tiplist.add("关键词，多个用分隔符"+ClearUtil.wordSplit+"分割");
        tiplist.add("禁言/踢人延迟时间)");
        tiplist.add("是否踢");
        tiplist.add("是否静默操作");
        tiplist.add("操作的原因(留空显示触发的敏感词)");
        return tiplist;
    }

    protected void onAddItemClick() {
        List<String> tiplist = getTitleList();
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("加群");
        defaultvaluelist.add("1小时");
        defaultvaluelist.add("");
        defaultvaluelist.add("");
        defaultvaluelist.add(" ");
        DialogUtils.showEditDialog(this, getInsertTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String keyWord = param.get(0);
                long duration = ParseUtils.parseGagStr2Secound(param.get(1));
                int action = ParseUtils.parseDistanceInt(param.get(2));
                boolean silence = ParseUtils.parseBoolean(param.get(3));
                String reason = param.get(4);


                if (TextUtils.isEmpty(keyWord)) {
                    return;
                } else {
                    if (onQueryExist(keyWord)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    GagAccountBean object = new GagAccountBean(keyWord, duration, silence, action);
                    object.setReason(reason);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(GagKeyWordActivity.this, "添加是否成功 " + (insert > 0));
                    if (insert > 0) {
                        object.setId((int) insert);
                        if (adapter.getList() == null) {
                            ArrayList list = new ArrayList<AccountBean>();
                            list.add(object);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();


                        } else {
                            adapter.getList().add(0, object);
                            adapter.notifyItemInserted(0);
                        }
                        doSubmitInsertSuccEvent(object);


                    } else {
                        AppContext.showToast("添加失败!");
                    }
                }

            }
        });
    }


    protected void onLongDeleteClick(int position) {

        {
            GagAccountBean bean = adapter.getList().get(position);
            int i = onDeleteToDb(bean);
            if (i <= 0) {
                DialogUtils.showDialog(this, "删除失败");
            } else {
                adapter.getList().remove(position);
                adapter.notifyItemRemoved(position);
                doDataChanageSuccEvent();
            }
        }

    }

    @Override
    protected void onLongEditClick(int position) {
        final GagAccountBean bean = adapter.getList().get(position);
        List<String> tiplist = getTitleList();
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("" + bean.getAccount());
        defaultvaluelist.add(DateUtils.getGagTime(bean.getDuration()));
        defaultvaluelist.add(bean.getAction() + "");
        defaultvaluelist.add(bean.isSilence() + "");
        defaultvaluelist.add(TextUtils.isEmpty(bean.getReason()) ? "" : bean.getReason() + "");
        DialogUtils.showEditDialog(this, getInsertTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String first = param.get(0);
                long duration = ParseUtils.parseGagStr2Secound(param.get(1));
                int action = ParseUtils.parseDistanceInt(param.get(2));
                boolean silence = ParseUtils.parseBoolean(param.get(3));
                String reason = param.get(4);
                if (TextUtils.isEmpty(first)) {
                    Toast.makeText(GagKeyWordActivity.this, "禁止输入空值", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    bean.setAccount(first);
                    bean.setDuration(duration);
                    bean.setAction(action);
                    bean.setReason(reason);
                    bean.setSilence(silence);
                    int update = DBHelper.getGagKeyWord(AppContext.getDbUtils()).update(bean);
                    adapter.notifyDataSetChanged();
                    doDataChanageSuccEvent();
                    Toast.makeText(GagKeyWordActivity.this, "更新成功,影响记录总数:" + update, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_GAG);
        EventBus.getDefault().post(event);
    }

    @Override
    protected int onDeleteToDb(GagAccountBean bean) {
        int i = DBHelper.getGagKeyWord(AppContext.getDbUtils()).deleteById(GagAccountBean.class, bean.getId());
        return i;
    }

    @Override
    protected List<GagAccountBean> queryDataFromDb() {
        List list = DBHelper.getGagKeyWord(AppContext.getDbUtils()).queryAllIsDesc(GagAccountBean.class, true, FieldCns.FIELD_ACCOUNT);
        return list;
    }


}
