package com.ddmeng.mengpo.viewholders;

import android.view.View;
import android.widget.TextView;

import com.ddmeng.mengpo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListContentViewHolder extends BaseViewHolder<String> {
    @InjectView(R.id.content_text)
    TextView mContentText;

    public ListContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    @Override
    public void populate(String model) {
        mContentText.setText(model);

    }
}
