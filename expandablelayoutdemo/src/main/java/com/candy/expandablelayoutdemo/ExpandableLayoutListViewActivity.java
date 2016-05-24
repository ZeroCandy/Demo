package com.candy.expandablelayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;

public class ExpandableLayoutListViewActivity extends AppCompatActivity {
    private String[] headers={"标题1","标题2","标题3","标题4","标题5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_layout_listview);
        initViews();
    }

    private void initViews() {
        ExpandableLayoutListView elLv = (ExpandableLayoutListView) findViewById(R.id.ellv);
        elLv.setAdapter(new ArrayAdapter<>(this, R.layout.layout_item, R.id.header_tv, headers));

    }
}
