package com.xpf.strongerlistview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yujin.adapter.BaseSwipeAdapter;
import com.yujin.interfaces.OnListViewScrollListener;
import com.yujin.swipe.SimpleSwipeListener;
import com.yujin.swipe.SwipeLayout;

import java.util.List;

/**
 * ListView数据的适配器
 */
public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private final MainActivity mainActivity;
    boolean isClosing = false;

    public ListViewAdapter(Context mContext) {
        this.mContext = mContext;
        mainActivity = (MainActivity) mContext;
        mainActivity.setOnListViewScrollListener(new OnListViewScrollListener() {
            @Override
            public void onScroll() {
                Log.e("TAG", "接口被回调了...");
                synchronized (this) {
                    List<Integer> openItems = mItemManger.getOpenItems();
                    for (int i = 0; i < openItems.size(); i++) {
                        Integer integer = openItems.get(i);
                        mItemManger.closeItem(integer);
                    }
                }
            }
        });

    }

    // SwipeLayout的布局id
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        // 当隐藏的删除menu被打开的时候的回调函数
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                Toast.makeText(mContext, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        // 双击的回调函数
//        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout,
//                                      boolean surface) {
//                Toast.makeText(mContext, "DoubleClick",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        v.findViewById(R.id.llLayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "SingleClick", Toast.LENGTH_SHORT).show();
            }
        });
        // 添加删除布局的点击事件
        v.findViewById(R.id.llDelete).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                // 点击完成之后，关闭删除menu
                swipeLayout.close();
            }
        });
        v.findViewById(R.id.llPause).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(mContext, "Pause", Toast.LENGTH_SHORT).show();
                // 点击完成之后，关闭删除menu
                swipeLayout.close();
            }
        });


        return v;
    }

    // 对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView) convertView.findViewById(R.id.position);
        t.setText((position + 1) + "." + "人有的时候，身在其中是很难看清事情的真相的，因为我们都是渴望被爱的。宁愿相信是被爱的那一个，也不愿知道自己是被骗的那一个。");
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
