package com.sxenon.echovalley.demo.select;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sxenon.echovalley.R;

public class NormalFragment extends Fragment {
    private RecyclerView rvNormal;
    private String[] searchEngines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_select_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        searchEngines = getResources().getStringArray(R.array.search_engine);

        rvNormal = view.findViewById(R.id.demo_select_rv);
        rvNormal.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNormal.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    View itemView = LayoutInflater.from(getContext()).inflate(R.layout.demo_adapter_even_item,parent,false);
                    return new EvenViewHolder(itemView);
                }
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.demo_adapter_odd_item,parent,false);
                return new OddViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                int viewType = getItemViewType(position);
                if (viewType == 0){
                    EvenViewHolder evenViewHolder = (EvenViewHolder) holder;
                    evenViewHolder.tvEven.setText(searchEngines[position]);
                }else {
                    OddViewHolder oddViewHolder = (OddViewHolder) holder;
                    oddViewHolder.tvOdd.setText(searchEngines[position]);
                }
            }

            @Override
            public int getItemCount() {
                return searchEngines.length;
            }

            @Override
            public int getItemViewType(int position) {
                return position % 2;
            }
        });
    }

    private static class OddViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOdd;

        public OddViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOdd = itemView.findViewById(R.id.main_odd_text);
        }
    }

    private static class EvenViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEven;

        public EvenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEven = itemView.findViewById(R.id.main_even_text);
        }
    }

}
