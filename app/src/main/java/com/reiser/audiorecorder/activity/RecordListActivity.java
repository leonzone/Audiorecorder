package com.reiser.audiorecorder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.reiser.audiorecorder.R;
import com.reiser.audiorecorder.adapter.RecordAdapter;
/**
 * Created by reiserx on 2018/12/10.
 * desc : 录音列表
 */
public class RecordListActivity extends AppCompatActivity {
    RecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        initView();
    }

    private void initView() {

        RecyclerView recyclerView = findViewById(R.id.rv_record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RecordAdapter(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        mAdapter.onDestroy();
        super.onDestroy();
    }
}
