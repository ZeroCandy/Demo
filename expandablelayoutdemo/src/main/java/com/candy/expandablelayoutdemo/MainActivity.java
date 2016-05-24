package com.candy.expandablelayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 扩展单个内容
     * @param view
     */
    public void showExpandableLayout(View view){
        startActivity(new Intent(this,ExpandableLayoutActivity.class));
    }

    /**
     * 扩展ListView
     * @param view
     */
    public void showExpandableLayoutListView(View view){
        startActivity(new Intent(this,ExpandableLayoutListViewActivity.class));
    }
}
