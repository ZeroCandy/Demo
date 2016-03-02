package com.candy.ipcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.candy.ipcdemo.ui.MessengerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.messenger_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class clazz=null;
        switch (v.getId()){
            case R.id.messenger_btn:
                clazz= MessengerActivity.class;
                break;
        }
        if(clazz!=null){
            Intent intent=new Intent(this,clazz);
            startActivity(intent);
        }
    }
}
