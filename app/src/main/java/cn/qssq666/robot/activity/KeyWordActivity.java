package cn.qssq666.robot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.qssq666.robot.R;
import cn.qssq666.robot.adapter.DefaultAdapter;
import cn.qssq666.robot.adapter.ReplyWordAdapter;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.databinding.ActivityWordManagerBinding;
import cn.qssq666.robot.event.WordEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.ui.ListDividerItemDecoration;
import cn.qssq666.robot.utils.ClearUtil;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.StringUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class KeyWordActivity extends SuperActivity {

    private static final String TAG = "词库管理";
    private ActivityWordManagerBinding binding;

    public ReplyWordAdapter getAdapter() {
        return adapter;
    }

    private ReplyWordAdapter adapter;

    @Subscribe
    public void onReceveData(WordEvent event) {
        if (event.isAll()) {

            queryData();
        } else if (event.isEdit()) {

            if(event.getPosition()<0){
                queryData();

            }else{

            adapter.getList().set(event.getPosition(), event.getBean());
            adapter.notifyItemChanged(event.getPosition());

            }

        } else {
            List<ReplyWordBean> list = adapter.getList();
            if (list == null) {
                list = new ArrayList<>();
                list.add(event.getBean());
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            } else {
                list.add(0, event.getBean());
                adapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_word_manager);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("关键词回复");
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        ;
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.addItemDecoration(new ListDividerItemDecoration(this, R.drawable.divider));
        adapter = new ReplyWordAdapter();
//        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
//        fastScroller.setRecyclerView(binding.recyclerview);
//        binding.recyclerview.addOnScrollListener(fastScroller.getOnScrollListener());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new DefaultAdapter.OnItemLongClickListener<ReplyWordBean>() {
            @Override
            public boolean onItemClick(ReplyWordBean model, View view, final int position) {
                DialogUtils.showMenuDialog(KeyWordActivity.this, "操作", Cns.OPERA_MENU, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                ReplyWordBean bean = adapter.getList().get(position);
                                Intent intent = new Intent(KeyWordActivity.this, AddWordActivity.class);
                                intent.putExtra(Cns.TYPE_EDITATA, true);
                                intent.putExtra(Cns.INTENT_POSITION, position);
                                intent.putExtra(Cns.BEAN, bean);
                                startActivity(intent);
                            }
                            break;
                            case 1: {
                                ReplyWordBean bean = adapter.getList().get(position);
                                int i = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).deleteById(ReplyWordBean.class, bean.getId());
                                if (i <= 0) {
                                    DialogUtils.showDialog(KeyWordActivity.this, "删除失败");
                                } else {
                                    adapter.getList().remove(position);
                                    adapter.notifyItemRemoved(position);

                                }
                            }
                            break;
                        }
                    }
                });
                return true;
            }
        });


        binding.swiperefreshlayout.setRefreshing(true);
        binding.swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                queryData();
            }
        });

        queryData();

    }

    public void queryData() {

        if (searchIng) {
            MenuItem item = binding.toolbar.getMenu().findItem(R.id.action_search);
            searchIng = false;

            if (item != null) {
                item.setTitle("搜索");
            }
        }
        new QssqTask<List<ReplyWordBean>>(new QssqTask.ICallBack<List<ReplyWordBean>>() {
            @Override
            public List<ReplyWordBean> onRunBackgroundThread() {
                List<ReplyWordBean> list = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryAllIsDesc(ReplyWordBean.class, false, FieldCns.ASK);
                return list;
            }

            @Override
            public void onRunFinish(List<ReplyWordBean> list) {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                binding.toolbar.setSubtitle("共" + (list == null ? 0 : list.size()) + "条");
                binding.swiperefreshlayout.setRefreshing(false);
            }
        }).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_word_activity, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        if (item != null) {
            if (searchIng) {
                item.setTitle("取消搜索");

            }
        }
        return true;


    }


    boolean searchIng = false;


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddWordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_import) {

            Intent intent = new Intent(this, ImportWordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {

            DialogUtils.showConfirmDialog(this, "真的要删除所有数据吗?", "", new INotify<Void>() {
                @Override
                public void onNotify(Void param) {
                    int i = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).deleteAll(ReplyWordBean.class);
                    if (i > 0) {
                        Toast.makeText(KeyWordActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        binding.swiperefreshlayout.setRefreshing(true);
                        queryData();
                    } else {
                        Toast.makeText(KeyWordActivity.this, "删除失败", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        } else if (id == R.id.action_remove_repeat) {

            if (adapter.getItemCount() == 0) {
                AppContext.showToast("没有数据!");
                return true;
            }

            new QssqTaskFix<Object, String>(new QssqTaskFix.ICallBackImp<Object, String>() {
                @Override
                public String onRunBackgroundThread(Object[] params) {


                    List<ReplyWordBean> list = adapter.getList();
                    HashMap<String, ReplyWordBean> askMap = new HashMap<>();
                    StringBuilder sb = new StringBuilder();
                    String tempLog = null;
                    for (ReplyWordBean bean : list) {

                        String answerOld = bean.getAnswer();
                        String askOld = bean.getAsk();
                        String ask = ClearUtil.removeRepeat(ClearUtil.wordSplit, askOld);
                        String answer = ClearUtil.removeRepeat(ClearUtil.wordSplit, answerOld);

                        if (!ask.equals(askOld)) {

                            bean.setAsk(ask);
                            int update = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(bean);

                            tempLog = "id" + bean.getId() + "的提问[" + askOld + "]调整为[" + ask + "],是否成功:" + (update > 0);
                            sb.append(tempLog + "\n");
                            LogUtil.writeLog(TAG, tempLog);

                            ;


                        }


                        if (!answerOld.equals(answer)) {

                            ;

                            bean.setAnswer(answer);
                            int update = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(bean);
                            tempLog = "id" + bean.getId() + "的答案[" + answerOld + "]调整为[" + answer + "],是否成功:" + (update > 0);
                            sb.append(tempLog + "\n");
                            LogUtil.writeLog(TAG, tempLog);


                        }


                        {


                            HashSet<String> splitAsk = ClearUtil.word2HashSet(ClearUtil.wordSplit, bean.getAsk());

                            int askCount = splitAsk.size();
                            boolean needChange = false;


                            Iterator<String> iterator = splitAsk.iterator();

                            while (iterator.hasNext()) {
                                String next = iterator.next();

                                if (askMap.get(next) != null) {

                                    if (bean != askMap.get(next)) {
//                                    ReplyWordBean replyWordBean = askMap.get(next);
                                        //删现在的原则。
                                        askCount--;
                                        if (askCount == 0) {


                                            ReplyWordBean queryBean = askMap.get(next);
                                            queryBean.setAnswer(queryBean.getAnswer() + ClearUtil.wordSplit + bean.getAnswer());
                                            DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(queryBean);
                                            DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).deleteById(ReplyWordBean.class, bean.getId());

                                            askCount = -1;


                                            tempLog = "应用问题彻底清空,问:[" + bean.getAsk() + "],id" + bean.getId()+"对应的答案["+bean.getAnswer()+"],清空后附加到id"+queryBean.getId()+",答:["+queryBean.getAnswer()+"]";
                                            sb.append(tempLog+"\n");


                                            LogUtil.writeLog(TAG, tempLog);
                                            needChange = false;
                                            break;

                                        } else {


                                            tempLog = "发现重复的问,删除," + bean.getId() + "中的" + next + "";
                                            sb.append(tempLog);

                                            LogUtil.writeLog(TAG, tempLog);

                                        }
                                        needChange = true;
                                        iterator.remove();


                                    } else {


                                        tempLog = "发现重复的问,但是是自身，忽略" + bean;
                                        sb.append(tempLog);


                                        LogUtil.writeLog(TAG, tempLog);
                                    }

                                } else {
                                    askMap.put(next, bean);
                                }
                            }


                            if (needChange) {

                                String string = ClearUtil.hashSet2String(ClearUtil.wordSplit, splitAsk);


                                tempLog = "应用问题的改变" + bean.getId() + "的" + bean.getAsk() + "改变为" + string;
                                sb.append(tempLog);

                                LogUtil.writeLog(TAG, tempLog);
                                bean.setAsk(string);

                                int update = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(bean);
                                tempLog = "更新" + bean.getId() + "的" + update;
                                sb.append(tempLog);

                                LogUtil.writeLog(TAG, tempLog);


                            }


                        }


                    }


                    return sb.toString();


                }

                @Override
                public void onRunFinish(String o) {

                    queryData();
                    DialogUtils.showDialog(KeyWordActivity.this, TextUtils.isEmpty(o) ? "没有发现问题" : o);
                }
            }).execute();


        } else if (id == R.id.action_search) {

            if (searchIng) {
                binding.swiperefreshlayout.setRefreshing(true);
                queryData();
                item.setTitle("搜索");
                searchIng = false;

                return true;
            }
            List<String> tiplist = new ArrayList<>();
            tiplist.add("要搜索的问题(为空只答)");
            tiplist.add("要搜索的答案(为空只搜问)");
            List defaultvalueList = new ArrayList();
            DialogUtils.showEditDialog(this, "搜索", tiplist, defaultvalueList, 2, false, new INotify<List<String>>() {
                @Override
                public void onNotify(final List<String> param) {


                    searchIng = true;

                    new QssqTask<List<ReplyWordBean>>(new QssqTask.ICallBack<List<ReplyWordBean>>() {
                        @Override
                        public List<ReplyWordBean> onRunBackgroundThread() {

                            if (TextUtils.isEmpty(param.get(1))) {
                                return DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryAllByFieldLike(ReplyWordBean.class, "ask", param.get(0));

                            } else if (TextUtils.isEmpty(param.get(0))) {
                                return DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryAllByFieldLike(ReplyWordBean.class, "answer", param.get(1));
                            } else {

                                //"answer", param.get(1)
                                String[] queryFieldStr = new String[]{"ask", "answer"};
                                String[] selectionArgs = new String[]{param.get(0), param.get(1)};
                                return DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumnArrLike(ReplyWordBean.class, queryFieldStr, selectionArgs);

                            }

                        }

                        @Override
                        public void onRunFinish(List<ReplyWordBean> list) {
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            binding.swiperefreshlayout.setRefreshing(false);
                        }
                    }).execute();


                    item.setTitle("取消搜索");


                }
            });


        } else if (id == R.id.action_split_replace) {


            if (adapter.getItemCount() == 0) {
                AppContext.showToast("没有数据");
                return true;
            }
            DialogUtils.showEditDialog(KeyWordActivity.this, "把什么替换为分割词【" + ClearUtil.wordSplit + "】??", "", "", new INotify<String>() {
                @Override
                public void onNotify(String param) {
                    if (TextUtils.isEmpty(param)) {
                        AppContext.showToast("输入为空，无法替换");
                    } else {
                        List<ReplyWordBean> list = adapter.getList();
                        boolean hasChange = false;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < list.size(); i++) {
                            ReplyWordBean bean = list.get(i);

                            String answerOld = bean.getAnswer();
                            String answer = StringUtils.replaceAllByStr(answerOld, param, ClearUtil.wordSplit);
                            String askOld = bean.getAsk();
                            String ask = StringUtils.replaceAllByStr(askOld, param, ClearUtil.wordSplit);

                            if (!askOld.equals(ask) ||ask.length() != askOld.length()) {
                                hasChange = true;
                                bean.setAsk(ask);
                                int update = DBHelper.getKeyWordDBUtil(AppContext.dbUtils).update(bean);
                                sb.append("第" + (i + 1) + "条数据的问[" + askOld + "]替换为[" + ask + "]是否成功" + (update > 0) + "\n");

                            }
                            if (!answerOld.equals(answer) || answer.length() != answerOld.length()) {
                                hasChange = true;
                                bean.setAnswer(answer);
                                int update = DBHelper.getKeyWordDBUtil(AppContext.dbUtils).update(bean);
                                sb.append("第" + (i + 1) + "条数据的答[" + answerOld + "]替换为[" + answer + "]是否成功" + (update > 0) + "\n");

                            }
                        }

                        DialogUtils.showDialog(KeyWordActivity.this, "操作完成" + sb.toString());

                        if (hasChange) {
                            queryData();
                        }
                    }

                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}
