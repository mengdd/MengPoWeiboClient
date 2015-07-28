package com.ddmeng.mengpo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddmeng.mengpo.R;
import com.ddmeng.mengpo.viewholders.BaseViewHolder;
import com.ddmeng.mengpo.viewholders.ListContentViewHolder;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<String> mData;

    public MainListAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list_item, parent, false);
        return new ListContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        baseViewHolder.populate(mData.get(position));

    }

    @Override
    public int getItemCount() {
        if (null == mData) {
            return -1;
        }
        return mData.size();
    }
}
