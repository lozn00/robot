package cn.qssq666.robot.interfaces;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by luozheng on 2016/4/5.  qssq.space
 */
public interface OnItemClickListener {

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    void onItemClick(ViewGroup parent, View view, int position);
}