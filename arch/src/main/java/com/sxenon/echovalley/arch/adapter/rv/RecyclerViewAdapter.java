package com.sxenon.echovalley.arch.adapter.rv;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sxenon.echovalley.arch.adapter.IAdapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewViewHolder> implements IAdapter<T> {
    private final SparseArray<RecyclerViewItemViewTypeEntity> mItemViewTypeEntryArray;
    private final Object mLock = new Object();
    private List<T> mValues = new ArrayList<>();

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

    @Override
    public void addItem(int position, T value) {
        synchronized (mLock) {
            if (position > mValues.size() || position < 0 || value == null) {
                return;
            }
            mValues.add(position, value);
            notifyItemInserted(position);
        }
    }

    @Override
    public void addItems(int position, Collection<? extends T> values) {
        synchronized (mLock) {
            if (position > mValues.size() || position < 0 || values == null) {
                return;
            }
            mValues.addAll(position, values);
            notifyItemRangeInserted(position, values.size());
        }
    }

    @Override
    public void removeItems(Collection<? extends T> values) {
        synchronized (mLock) {
            mValues.removeAll(values);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeItem(int position) {
        synchronized (mLock) {
            if (position >= mValues.size() || position < 0) {
                return;
            }
            mValues.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void removeItem(T value) {
        synchronized (mLock) {
            int position = mValues.indexOf(value);
            if (position >= 0) {
                mValues.remove(position);
                if(mValues.size() == 0){
                    notifyDataSetChanged();
                } else {
                    notifyItemRemoved(position);
                }
            }
        }
    }

    @Override
    public List<T> getValues() {
        return mValues;
    }

    @Override
    public T getValue(int position) {
        synchronized (mLock) {
            if (position < 0 || position >= mValues.size()) {
                return null;
            }
            return mValues.get(position);
        }
    }

    @Override
    public void resetAllItems(List<T> values) {
        synchronized (mLock) {
            final List<T> oldValue = new ArrayList<>(mValues);
            mValues = values==null?new ArrayList<T>():values;
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldValue.size();
                }

                @Override
                public int getNewListSize() {
                    return mValues.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return oldValue.get(oldItemPosition).equals(mValues.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }
            });
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void setItem(int position, T value) {
        synchronized (mLock) {
            if (value == null || position >= mValues.size() || position < 0) {
                return;
            }
            mValues.set(position, value);
            notifyItemChanged(position);
        }
    }

    @Override
    public void invalidate(T oldValue, T newValue) {
        setItem(mValues.indexOf(oldValue), newValue);
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        synchronized (mLock) {
            if (fromPosition < 0 || fromPosition >= mValues.size() || toPosition < 0 || toPosition >= mValues.size()) {
                return;
            }
            if (fromPosition == toPosition) {
                return;
            }
            Collections.swap(mValues, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public int getItemCount() {
        synchronized (mLock){
            return mValues.size();
        }
    }
}
