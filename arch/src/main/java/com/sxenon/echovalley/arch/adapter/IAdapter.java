package com.sxenon.echovalley.arch.adapter;

import java.util.Collection;
import java.util.List;

public interface IAdapter<T> {
    int getItemCount();

    void addItem(int position, T value);

    void addItems(int position, Collection<? extends T> values);

    void removeItems(Collection<? extends T>  values);

    void removeItem(int position);

    void removeItem(T value);

    List<T> getValues();

    T getValue(int position);

    void resetAllItems(List<T> values);

    void setItem(int position, T value);

    void invalidate(T oldValue, T newValue);

    void moveItem(int fromPosition, int toPosition);

    void notifyDataSetChanged();
}
