package cn.qssq666.robot.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.ReplyWordBean;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.FieldCns;
import cn.qssq666.robot.databinding.ActivityAddWordBinding;
import cn.qssq666.robot.event.WordEvent;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.ClearUtil;
import cn.qssq666.robot.utils.DBHelper;
import cn.qssq666.robot.utils.DialogUtils;

/**
 * Created by luozheng on 2017/3/13.  qssq.space
 */

public class AddWordActivity extends SuperActivity implements View.OnClickListener {

    private ActivityAddWordBinding binding;
    private boolean editMode;
    private ReplyWordBean mBean;
    private int position;
    private long result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_word);
        setSupportActionBar(binding.toolbar);
        Intent intent = getIntent();
        if (intent != null) {
            editMode = intent.getBooleanExtra(Cns.TYPE_EDITATA, false);
            if (editMode) {
                position = intent.getIntExtra(Cns.INTENT_POSITION, 0);
                mBean = intent.getParcelableExtra(Cns.BEAN);

                binding.evKey.setText(mBean.getAsk());
                binding.evValue.setText(mBean.getAnswer());
            }

        }

        binding.btnSubmit.setText(editMode ? "更新词库" : "添加词库");
        binding.btnSubmit.setOnClickListener(this);
        binding.getRoot().findViewById(R.id.btn_copy_split_flag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.copy(v.getContext(), ClearUtil.wordSplit + "");
                AppContext.showToast("复制完成");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                try {
                    AlertDialog dialog = null;
                    if (TextUtils.isEmpty(binding.evKey.getText())) {
                        Toast.makeText(this, "必须填写问题", Toast.LENGTH_SHORT).show();
                        binding.evKey.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(binding.evValue.getText())) {
                        Toast.makeText(this, "必须填写答案", Toast.LENGTH_SHORT).show();
                        binding.evValue.requestFocus();
                        return;
                    }

                    if (!editMode) {
                        mBean = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryByColumn(ReplyWordBean.class, FieldCns.ASK, binding.evKey.getText().toString());

                        if (mBean != null) {
                            DialogUtils.showDialog(this, "词库已经存在 对应的回复语是:" + mBean.getAnswer());
                            return;
                        }


                        List<ReplyWordBean> wordBeans = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).queryAllByFieldLike(ReplyWordBean.class, FieldCns.ASK, binding.evKey.getText().toString());
                        //只能检查一个 词条的重复，无法检查多个。多个的话更麻烦，反正有去重功能，不管这个东西。
                        if (wordBeans != null) {
                            for (ReplyWordBean wordBeanLike : wordBeans) {

                                HashSet<String> strings = ClearUtil.word2HashSet(ClearUtil.wordSplit, wordBeanLike.getAsk());
                                if (strings != null) {
                                    boolean remove = strings.remove(binding.evKey.getText().toString());
                                    if (remove) {
                                        mBean = wordBeanLike;
                                        break;
                                    }
                                }

                            }


                            if (mBean != null) {

                                String msg = "词库已经存在 id" + mBean.getId() + "问[" + mBean.getAsk() + "]\n回复语[" + mBean.getAnswer() + "]\n如果要添加多条回复语,请打开此词条，然后编辑并分隔符" + ClearUtil.wordSplit + "进行分割多个词语";
                                DialogUtils.showConfirmDialog(this, msg, "是否马上编辑", new INotify<Void>() {
                                    @Override
                                    public void onNotify(Void param) {
                                        ReplyWordBean bean = mBean;
                                        Intent intent = new Intent(AddWordActivity.this, AddWordActivity.class);
                                        intent.putExtra(Cns.TYPE_EDITATA, true);
                                        intent.putExtra(Cns.INTENT_POSITION, -1);
                                        intent.putExtra(Cns.BEAN, bean);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                return;
                            }

                        }


                        mBean = new ReplyWordBean(binding.evKey.getText().toString(), binding.evValue.getText().toString());
                        initBeanValue(mBean);
                        result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).insert(mBean);
                        if (result > 0) {
                            mBean.setId((int) result);
                        }
                        dialog = DialogUtils.showDialog(this, result <= 0 ? "插入失败! value:" + result : "插入数据成功");

                    } else {
                        initBeanValue(mBean);
                        result = DBHelper.getKeyWordDBUtil(AppContext.getDbUtils()).update(mBean);
                        dialog = DialogUtils.showDialog(this, result <= 0 ? "更新失败! value:" + result : "更新数据成功");

                    }
                    if (dialog != null) {
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (result > 0) {
                                    WordEvent event = new WordEvent();
                                    event.setBean(mBean);
                                    event.setEdit(editMode);
                                    event.setPosition(position);
                                    EventBus.getDefault().post(event);

                                    finish();
                                } else {


                                }
                            }
                        });
                    }

                } catch (Throwable e) {
                    Toast.makeText(this, "查询出现故障,请不要输入特殊字符!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void initBeanValue(ReplyWordBean bean) {
        bean.setAsk(binding.evKey.getText().toString());
        bean.setAnswer(binding.evValue.getText().toString());


    }
}
