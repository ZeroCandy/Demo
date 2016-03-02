package com.candy.ipcdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class MessengerService extends Service {
    /**服务信使*/
    private Messenger mMessenger=new Messenger(new MessengerHandler(this));

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回服务信使用于通讯的IBinder
        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler{
        Context context;

        public MessengerHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(context,"MessengerService receive:"+msg.getData().get("msg"),Toast.LENGTH_LONG).show();
                    //通过客户端传过来消息中获取客户端信使，通过该信使对象再返回数据给客户端
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, 2);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","I'm MessengerService,Copy that!");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
