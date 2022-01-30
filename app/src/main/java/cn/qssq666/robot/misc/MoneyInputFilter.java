package cn.qssq666.robot.misc;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;


import cn.qssq666.robot.utils.RegexUtils;

/**
 * @author 情随事迁 原创 还原 与金额输入正则
 *
 * 支持 0.01  0.1 1.00 1.01 整数  不支持负数
 */

public class MoneyInputFilter implements InputFilter {
    private static final String TAG = "MoneyInputFilter";

    /**
     * 通过过滤器实现金额的格式化 http://blog.csdn.net/huiling815/article/details/52444229
     *
     * @param source :新输入的字符串
     * @param start  :新输入的字符串起始下标，一般为0  就是 输入内容的0
     * @param end    :新输入的字符串终点下标，一般为source长度-1  气死就是srouce.length-1 如果是删除操作 就是0 srouce为空字符串
     * @param dest   :输入之前文本框内容
     * @param dstart :原内容起始坐标，一般为0
     * @param dend   :原内容终点坐标，一般为dest长度-1
     *               Created by luozheng on 2016/9/8.  qssq.space
     *               CharSequence source,  //输入的文字
     *               int start,  // source 的开始位置
     *               int end,  //srouce的结束位置 不是指 编辑框位置 而是输入的字 输入法有时候可以一次输入很多字 但是 end貌似表示总数 不是结束为止 ，我粘贴了66 则为2 start=0 srouce 66   dstart有点像插入位置
     *               Spanned dest, //当前显示的内容  之前输入的内容
     *               int dstart,  //当前开始位置  之前的开始位置
     *               int dend //当前结束位置  应该叫 之前的 索引结束为止是多少 好像也不对 删除的时候 还是 比如删除后只有一个5那么 end=2 start=1 再变成2个，都是1了 所以
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Log.i(TAG, "srouce:" + source + ",start:" + start + ",end:" + end + ",desc:" + dest + ",dstart:" + dstart + ",dend:" + dend);
//        return source.length() < 1 ? dest.subSequence(dstart, dend) : "ATAAW.COM";
        //表示你输入了 5 srouce
        if (!TextUtils.isEmpty(source)) {
            String beforeStr = dest.toString();
            String insertStr = source.toString();

            StringBuilder builder = new StringBuilder();
            if (!TextUtils.isEmpty(beforeStr)) {
                builder.append("" + beforeStr); //544这里的5前面插入 9  start =1 dend =1  说明前面有几位
            }
            builder.insert(dend, insertStr);
            String editTextStr = builder.toString();
            Log.i(TAG, "还原后您编辑框输入的内容是;" + editTextStr);
            if (editTextStr.length() > 2) {// 0. 1.  0 虽然不合法允许输入 不把.抹掉 不然永远输入不了小数点
                if (!RegexUtils.checkIsAlipayMoney(editTextStr)) {
                    return "";//删除输入的字禁止输入
                }
            }
            Log.i(TAG, RegexUtils.checkIsAlipayMoney(editTextStr) + "");
            return source;//return source  or null都是原样不动
        } else {
            return null; //返回srouce 或者 null都 为不处理
        }
    }
}
