package com.sxenon.echovalley.arch.adapter.rv;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewViewHolder<T> extends RecyclerView.ViewHolder {
    private final RecyclerViewAdapter adapter;
    private int height, width;

    public RecyclerViewViewHolder(@NonNull View itemView,RecyclerViewAdapter adapter) {
        super(itemView);
        this.adapter = adapter;

        height = itemView.getLayoutParams().height;
        width = itemView.getLayoutParams().width;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setVisibility(boolean isVisible) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (isVisible) {
            param.height = this.height;
            param.width = this.width;
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }

    public abstract void onBind(T t);
}
