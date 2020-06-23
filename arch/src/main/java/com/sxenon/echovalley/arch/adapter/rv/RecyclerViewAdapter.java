package com.sxenon.echovalley.arch.adapter.rv;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
    private final SparseArray<RecyclerViewItemViewTypeEntity> mItemViewTypeEntryArray;

    public RecyclerViewAdapter(SparseArray<RecyclerViewItemViewTypeEntity> itemViewTypeEntryArray) {
        this.mItemViewTypeEntryArray = itemViewTypeEntryArray;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewViewHolder viewHolder = null;

        RecyclerViewItemViewTypeEntity itemViewTypeEntity = mItemViewTypeEntryArray.get(viewType);
        int resourceId = itemViewTypeEntity.getResourceId();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        Class<? extends RecyclerViewViewHolder> viewHolderClass = itemViewTypeEntity.getViewHolderClass();
        try {
            Constructor<? extends RecyclerViewViewHolder> constructor = viewHolderClass.getConstructor(View.class, RecyclerViewAdapter.class);
            constructor.setAccessible(true);
            viewHolder = constructor.newInstance(itemView, RecyclerViewAdapter.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    /**
     * You must override it according to the Constructor parameter.
     * position -> viewType -> resId + viewHoldClass -> viewHolder
     */
    @Override
    public abstract int getItemViewType(int position);
}
