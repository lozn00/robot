package cn.qssq666.robot.activity.datamanager;
import cn.qssq666.CoreLibrary0;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.qssq666.robot.activity.GroupDetailConfigActivity;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.AccountBean;
import cn.qssq666.robot.bean.GroupWhiteNameBean;
import cn.qssq666.robot.constants.AccountType;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.constants.Fields;
import cn.qssq666.robot.event.AccountAddOrChangeEvent;
import cn.qssq666.robot.event.GroupDetailEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.service.RemoteService;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class GroupWhiteNamesActivity extends BaseAccountManagerActivity<GroupWhiteNameBean> {


    protected boolean onQueryExist(String account) {

        return DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).queryColumnExist(GroupWhiteNameBean.class, Fields.FIELD_ACCOUNT, account);

    }

    @Override
    protected boolean onClickHelpMenu(MenuItem item) {
        DialogUtils.showDialog(this, "您可以直接输入命令添加群管理员也可以通过在这里输入，通过命令输入需要机器人自身输入方可操作,或者是管理员输入傻瓜模式也可以进行快速添加。如果某个群不在本列表中，那么这个群也就自然不会进行回复了。");
        return true;
    }

    protected String getInsertTipTitle() {
        return "请输入要添加的白名单群号";
    }

    protected String getInsertDefaultValue() {
        return Cns.DEFAULT_GROUP;
    }

    protected int getAdapterType() {
        return AccountType.TYPE_QQGROUP_WHITE_NAME;
    }


    protected void doSubmitInsertSuccEvent(GroupWhiteNameBean bean) {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setBean(bean);
        event.setType(AccountType.TYPE_QQGROUP_WHITE_NAME);
        event.setPosition(0);
        EventBus.getDefault().post(event);
    }


    protected long onInsertToDb(GroupWhiteNameBean GroupWhiteNameBean) {
        return DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).insert(GroupWhiteNameBean);

    }

    @Override
    protected void onLongEditClick(int position) {
        GroupWhiteNameBean groupWhiteNameBean = adapter.getList().get(position);
        GroupDetailEvent event = new GroupDetailEvent();
        event.setBean(groupWhiteNameBean);
        EventBus.getDefault().postSticky(event);
        AppUtils.skipActivity(this, GroupDetailConfigActivity.class);
    }

    @Override
    protected String[] getLongPressMenu() {


        String[] OPERA_MENU = {"编辑", "删除", "复制此群配置给其它群", "复制此群配置给所有群", "获取群信息" + (RemoteService.isIsInit() ? "" : "(暂时无法获取)"), "定位群聊天"};
        return OPERA_MENU;
    }

    @Override
    protected void onLongOtherClick(final int position, int which) {
        final List<GroupWhiteNameBean> list = adapter.getList();
        final GroupWhiteNameBean srcBean = list.get(position);


        switch (which) {
            case 2: {

                final String[] menus = new String[list.size() + 1];

                for (int i = 0; i < list.size(); i++) {
                    GroupWhiteNameBean bean = list.get(i);
                    menus[i] = bean.getAccount() + (bean.getAccount().equals(srcBean.getAccount()) ? "【不可用】" : "" + bean.getRemark());
                }
                menus[menus.length - 1] = "填写其它群";
                DialogUtils.showMenuDialog(GroupWhiteNamesActivity.this, "复制给目标群", menus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == menus.length - 1) {


                            DialogUtils.showEditDialog(GroupWhiteNamesActivity.this, "请输入要创建的新群配置", "", new INotify<String>() {
                                @Override
                                public void onNotify(String param) {

                                    if (param.equals(srcBean.getAccount())) {
                                        AppContext.showToast("群号不能相同");
                                        return;
                                    }


                                    GroupWhiteNameBean cloneBean = srcBean.clone();
                                    cloneBean.setId(0);
                                    cloneBean.setRemark("复制从" + srcBean.getRemark() + "群");
                                    cloneBean.setAccount(param);


                                    long update = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).insert(cloneBean);

                                    if (update > 0) {
                                        list.add(0, cloneBean);
                                        adapter.notifyDataSetChanged();
                                        doDataChanageSuccEvent();
                                        AppContext.showToast("创建成功 ");


                                    } else {
                                        AppContext.showToast("创建失败 ");
                                    }

                                }


                            });
                        } else {

                            if (list.size() == 1) {
                                AppContext.showToast("没有可以选择的群");
                                return;
                            }


                            GroupWhiteNameBean targetBefore = list.get(which);

                            if (targetBefore.getAccount().equals(srcBean.getAccount())) {
                                AppContext.showToast("复制群和目标群号不能相同!!");
                                return;
                            }


                            GroupWhiteNameBean cloneBean = srcBean.clone();
                            cloneBean.setAccount(targetBefore.getAccount());
                            cloneBean.setId(targetBefore.getId());
                            cloneBean.setRemark(targetBefore.getRemark());


                            int update = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).updateAllByField(cloneBean, FieldCns.FIELD_ACCOUNT, cloneBean.getAccount());
                            if (update > 0) {

                                list.set(position, cloneBean);
                                adapter.notifyDataSetChanged();
                                doDataChanageSuccEvent();
                                AppContext.showToast("复制配置成功!");
                            } else {

                                AppContext.showToast("复制配置失败!");

                            }
                        }

                    }
                });


            }
            break;


            case 3: {


                int failCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    GroupWhiteNameBean targetBefore = list.get(i);
                    if (targetBefore.getAccount().equals(srcBean.getAccount())) {
                        continue;
                    } else {


                        GroupWhiteNameBean cloneBean = srcBean.clone();
                        cloneBean.setAccount(targetBefore.getAccount());
                        cloneBean.setRemark(targetBefore.getRemark());

                        cloneBean.setId(targetBefore.getId());
                        int result = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).updateAllByField(cloneBean, FieldCns.FIELD_ACCOUNT, cloneBean.getAccount());
                        if (result <= 0) {
                            failCount++;
                        } else {
                            adapter.getList().set(i, cloneBean);
                        }

                    }

                }

                if (failCount > 0) {

                    AppContext.showToast("复制配置失败!失败总数:" + failCount);
                } else {
                    adapter.notifyDataSetChanged();
                    AppContext.showToast("复制配置给所有群成功!");
                    doDataChanageSuccEvent();
                }

            }


            break;

            case 4: {

                if (RemoteService.isIsInit()) {

                    Map map = RemoteService.queryGroupInfo(list.get(position).getAccount());
                    if (map == null) {
                        Toast.makeText(this, "查询失败!", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtils.showDialog(this, map + "");

                    }

                } else {
                    AppContext.showToast("此功能需要Q++插件 1.0.8以及一上版本,且需要在Q++中勾选绑定服务!");
                }
            }
            break;
            case 5: {

                try {

                    AppUtils.openQQGroupChat(this, srcBean.getAccount());
                } catch (Exception e) {
                    Toast.makeText(this, "启动失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            break;
        }
    }

    @Override
    protected int deleteAllFromDb() {
        return DBHelper.getQQGroupWhiteNameDBUtil(AppContext.dbUtils).deleteAll(GroupWhiteNameBean.class);
    }

    protected void onLongDeleteClick(final int position) {
        DialogUtils.showConfirmDialog(this, "真的要删除么?", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {

                GroupWhiteNameBean bean = adapter.getList().get(position);
                int i = onDeleteToDb(bean);
                if (i <= 0) {
                    DialogUtils.showDialog(GroupWhiteNamesActivity.this, "删除失败");
                } else {
                    adapter.getList().remove(position);
                    adapter.notifyItemRemoved(position);
                    doDataChanageSuccEvent();
                }
            }
        });

    }


    protected void onAddItemClick() {
        List<String> tiplist = getTitleList();
        List<String> defaultvaluelist = new ArrayList<>();
        defaultvaluelist.add("" + getInsertDefaultValue());
        defaultvaluelist.add("");

        DialogUtils.showEditDialog(this, getInsertTipTitle(), tiplist, defaultvaluelist, tiplist.size(), false, new INotify<List<String>>() {
            @Override
            public void onNotify(List<String> param) {
                String group = param.get(0);
                String descript = param.get(1);
                if (TextUtils.isEmpty(group)) {
                    return;
                } else {
                    if (onQueryExist(group)) {
                        AppContext.showToast("已存在无法添加！");
                        return;
                    }

                    GroupWhiteNameBean object = onCreateBean(group);
                    object.setRemark(descript);
                    long insert = onInsertToDb(object);
//                        long insert = DBHelper.getIgnoreQQDBUtil(AppContext.getInstance().getDbUtils()).insert(object);
                    DialogUtils.showDialog(GroupWhiteNamesActivity.this, "添加是否成功 " + (insert > 0));
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

    protected void doDataChanageSuccEvent() {
        AccountAddOrChangeEvent event = new AccountAddOrChangeEvent();
        event.setType(AccountType.TYPE_QQGROUP_WHITE_NAME);
        EventBus.getDefault().post(event);
    }

    protected int onDeleteToDb(GroupWhiteNameBean bean) {
        int i = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).deleteById(GroupWhiteNameBean.class, bean.getId());
        return i;
    }

    protected List<GroupWhiteNameBean> queryDataFromDb() {
        List<GroupWhiteNameBean> list = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).queryAllIsDesc(GroupWhiteNameBean.class, true, null);

        if (list != null && RemoteService.isIsInit()) {
            for (GroupWhiteNameBean bean : list) {

                if (TextUtils.isEmpty(bean.getRemark())) {
                    String s = RemoteService.queryGroupName(bean.getAccount());

                    if (!TextUtils.isEmpty(s)) {
                        bean.setRemark(s);
                        try {

                            DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).update(bean);
                        } catch (Exception e) {

                        }

                    }
                }
            }
        }
//        List<GroupWhiteNameBean> list = DBHelper.getQQGroupWhiteNameDBUtil(AppContext.getDbUtils()).queryAllIsDesc(GroupWhiteNameBean.class, true, FieldCns.ID);
        return list;
    }

    public List<String> getTitleList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("群号");
        list.add("备注");
        return list;
    }


}
