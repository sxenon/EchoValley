package com.sxenon.echovalley.demo.adapter;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sxenon.echovalley.arch.adapter.rv.RecyclerViewAdapter;
import com.sxenon.echovalley.arch.adapter.rv.RecyclerViewItemViewTypeEntity;
import com.sxenon.echovalley.arch.adapter.rv.RecyclerViewViewHolder;
import com.sxenon.echovalley.R;

public class OptimizedFragment extends Fragment {
    private RecyclerView rvOptimized;
    private String[] searchEngines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_adapter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchEngines = getResources().getStringArray(R.array.search_engine);

        rvOptimized = view.findViewById(R.id.demo_adapter_rv);
        rvOptimized.setLayoutManager(new LinearLayoutManager(getContext()));
        SparseArray<RecyclerViewItemViewTypeEntity> entitySparseArray = new SparseArray<>();
        entitySparseArray.put(0,new RecyclerViewItemViewTypeEntity(R.layout.demo_adapter_even_item,EvenViewHolder.class));
        entitySparseArray.put(1,new RecyclerViewItemViewTypeEntity(R.layout.demo_adapter_odd_item,OddViewHolder.class));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter<String>(entitySparseArray) {

            @Override
            public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
                holder.onBind(searchEngines[position]);
            }

            @Override
            public int getItemViewType(int position) {
                return position%2;
            }

            @Override
            public int getItemCount() {
                return searchEngines.length;
            }
        };
        rvOptimized.setAdapter(adapter);
    }

    private static class OddViewHolder extends RecyclerViewViewHolder<String> {
        private TextView tvOdd;

        public OddViewHolder(@NonNull View itemView,RecyclerViewAdapter adapter) {
            super(itemView,adapter);
            tvOdd = itemView.findViewById(R.id.main_odd_text);
        }

        @Override
        public void onBind(String s) {
            tvOdd.setText(s);
        }
    }

    private static class EvenViewHolder extends RecyclerViewViewHolder<String> {
        private TextView tvEven;

        public EvenViewHolder(@NonNull View itemView,RecyclerViewAdapter adapter) {
            super(itemView,adapter);
            tvEven = itemView.findViewById(R.id.main_even_text);
        }

        @Override
        public void onBind(String s) {
            tvEven.setText(s);
        }
    }
}
