package com.candy.ipcdemo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.candy.ipcdemo.R;
import com.candy.ipcdemo.service.MessengerService;

public class MessengerActivity extends AppCompatActivity {
    /**客户端的信使*/
    private Messenger mMessenger = new Messenger(new SubHandler(this));
    /**服务的信使*/
    private Messenger mServiceMessenger;

    /**服务连接*/
    private ServiceConnection mConnection=new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过IBinder获取服务信使
            mServiceMessenger = new Messenger(service);
            //生成消息
            Message msg = Message.obtain(null, 1);
            //消息放入数据
            Bundle bundle = new Bundle();
            bundle.putString("msg","MessengerService do you copy?");
            msg.setData(bundle);
            //消息放入客户端信使
            msg.replyTo= mMessenger;
            try {
                //通过服务信息发送消息给服务
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

    }

    public void startComm(View view){
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private static class SubHandler extends Handler{
        Context context;

        public SubHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    Toast.makeText(context,"clitent receive:"+msg.getData().get("reply"),Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
