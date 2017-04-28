package com.xpf.strongerlistview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.yujin.interfaces.OnListViewScrollListener;
import com.yujin.view.XListView;
import com.yujin.view.XListView.IXListViewListener;

public class MainActivity extends ActionBarActivity {

    private Context mContext;
    private Handler handler;
    private XListView mListView;
    private OnListViewScrollListener onListViewScrollListener;

    public void setOnListViewScrollListener(OnListViewScrollListener onListViewScrollListener) {
        this.onListViewScrollListener = onListViewScrollListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        handler = new Handler();
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.stopRefresh();
                        Toast.makeText(MainActivity.this, "refresh",
                                Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopLoadMore();
                        Toast.makeText(MainActivity.this, "loadMore",
                                Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });
        mListView.setAdapter(new ListViewAdapter(mContext));

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                          private SparseArray recordSp = new SparseArray(0);
                                          private int mCurrentfirstVisibleItem = 0;

                                          @Override
                                          public void onScrollStateChanged(AbsListView absListView, int i) {

                                          }

                                          @Override
                                          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                              if (onListViewScrollListener != null) {
                                                  onListViewScrollListener.onScroll();
                                              }
                                              mCurrentfirstVisibleItem = firstVisibleItem;
                                              View firstView = view.getChildAt(0);
                                              if (null != firstView) {
                                                  ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                                                  if (null == itemRecord) {
                                                      itemRecord = new ItemRecod();
                                                  }
                                                  itemRecord.height = firstView.getHeight();
                                                  itemRecord.top = firstView.getTop();
                                                  recordSp.append(firstVisibleItem, itemRecord);
                                                  int distance = getScrollY();//滚动距离

                                                  //在此进行你需要的操作
                                                  if (distance > 40) {
                                                      if (onListViewScrollListener != null) {
                                                          onListViewScrollListener.onScroll();
                                                      }
                                                  }
                                              }
                                          }

                                          private int getScrollY() {
                                              int height = 0;
                                              for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                                                  ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                                                  height += itemRecod.height;
                                              }
                                              ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                                              if (null == itemRecod) {
                                                  itemRecod = new ItemRecod();
                                              }
                                              return height - itemRecod.top;
                                          }

                                          class ItemRecod {
                                              int height = 0;
                                              int top = 0;
                                          }
                                      }


        );


    }

}
