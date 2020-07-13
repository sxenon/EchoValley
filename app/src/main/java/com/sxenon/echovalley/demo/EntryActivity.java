package com.sxenon.echovalley.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sxenon.echovalley.R;
import com.sxenon.echovalley.demo.adapter.DemoAdapterActivity;
import com.sxenon.echovalley.demo.select.DemoSelectActivity;

public class EntryActivity extends AppCompatActivity {
    private Button btnToAdapter;
    private Button btnToSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);

        btnToAdapter = findViewById(R.id.entry_to_adapter);
        btnToAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryActivity.this, DemoAdapterActivity.class);
                startActivity(intent);
            }
        });
        btnToSelect = findViewById(R.id.entry_to_select);
        btnToSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryActivity.this, DemoSelectActivity.class);
                startActivity(intent);
            }
        });
    }
}
