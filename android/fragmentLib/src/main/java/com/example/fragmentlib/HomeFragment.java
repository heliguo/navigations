package com.example.fragmentlib;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.fragmentlib.base.BaseFragment;
import com.example.fragmentlib.common.RnRecyclerView;
import com.example.fragmentlib.liveBus.LiveDataBus;
import com.example.fragmentlib.liveBus.LiveDataBusKey;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends BaseFragment {

    public static final String KEY_TITLE = "key_title";

    private RnRecyclerView recyclerView;
    private StaggeredHomeAdapter adapter;
    private SmartRefreshLayout smartRefreshLayout;
    private List<String> list;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        TextView textView = view.findViewById(R.id.title);
        Bundle arguments = getArguments();
        if (arguments != null && !TextUtils.isEmpty(arguments.getString(KEY_TITLE))) {
            String des = "来自React-Native的参数：\n";
            String content = des + arguments.getString(KEY_TITLE);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(span, des.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        list = new ArrayList<>();
        setWaterfallView();
        setRefreshListener();
        LiveDataBus.get().with(LiveDataBusKey.DELETE).observe(this, o -> {
            int random = new Random().nextInt(list.size());
            showDelete(random);
        });
    }

    private void setWaterfallView() {
        loadData();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        //防止item 交换位置
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StaggeredHomeAdapter(list);
        adapter.setOnItemClickListener(new StaggeredHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LiveDataBus.get().with(LiveDataBusKey.KEY, Integer.class).postValue(position);

            }


            @Override
            public void onItemLongClick(View view, final int position) {
                showDelete(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setMoreListener(4, bottom -> {
            if (!bottom)
                loadMore();
            else
                Toast.makeText(getActivity(), "～～～到底了～～～", Toast.LENGTH_SHORT).show();
        });

    }

    private void showDelete(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(String.format("确认删除吗第%s条数据吗？", (position + 1)))
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    list.remove(position);
                    adapter.notifyDataSetChanged();//必须刷新数据
                })
                .show();
    }

    private void loadData() {
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i + 1));
        }
    }

    private void setRefreshListener() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            list.clear();
            loadData();
            adapter.notifyDataSetChanged();
            smartRefreshLayout.finishRefresh();

        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            loadMore();
            smartRefreshLayout.finishLoadMore();
        });
    }

    private void loadMore() {
        if (list.size() > 100) {
            return;
        }
        int nextInt = new Random().nextInt(10) + 3;
        int pre = list.size();
        for (int i = 0; i < nextInt; i++) {
            list.add(String.valueOf(list.size() + 1));
        }
        adapter.notifyItemRangeInserted(pre, nextInt);
    }
}
