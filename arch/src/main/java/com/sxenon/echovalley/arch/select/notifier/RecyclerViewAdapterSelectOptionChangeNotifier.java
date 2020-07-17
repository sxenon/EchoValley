package com.sxenon.echovalley.arch.select.notifier;

import androidx.recyclerview.widget.DiffUtil;

import com.sxenon.echovalley.arch.adapter.rv.RecyclerViewAdapter;
import com.sxenon.echovalley.arch.select.IOptionChangeNotifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewAdapterSelectOptionChangeNotifier<T> implements IOptionChangeNotifier {
    private final RecyclerViewAdapter<T> adapter;

    public RecyclerViewAdapterSelectOptionChangeNotifier(RecyclerViewAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onOptionRemoved(int position) {
        adapter.removeItem(position);
    }

    @Override
    public void onSelectedOptionsRemoved(List<Boolean> selectedFlags) {
        int size = selectedFlags.size();
        List<T> newValues = new ArrayList<>(adapter.getValues());

        for (int position = size - 1; position >= 0; position--) {
            if (selectedFlags.get(position)) {
                newValues.remove(position);
            }
        }

        //TODO may need optimize
        Iterator<Boolean> iterator = selectedFlags.iterator();
        while (iterator.hasNext()) {
            if (iterator.next()) {
                iterator.remove();
            }
        }

        adapter.resetAllItems(newValues);
    }

    @Override
    public void notifySelectChange(List<Boolean> oldSelectedFlags, List<Boolean> newSelectedFlags) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new SelectDiffCallBack(oldSelectedFlags, newSelectedFlags), false);
        result.dispatchUpdatesTo(adapter);
    }
}
