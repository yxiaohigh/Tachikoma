package com.h.tachikoma.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.h.tachikoma.R;

public class RemoteWatchService extends Service {


    private MyServiceConnection myServiceConnection;
    private Intent intent1;

    @Override
    public void onCreate() {
        super.onCreate();
        intent1 = new Intent();
        intent1.setAction("com.h.tachikoma.service.LocalWatchService");
        intent1.setPackage(getPackageName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getNotfication();
        Log.i("RemoteWatchService", "远程服务启动成功");
        bindLocal();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return new IWatchAidl.Stub() {
            @Override
            public void onServiceStart() throws RemoteException {
                Log.i("RemoteWatchService", "onServiceStart");
            }

            @Override
            public void onServiceDath() throws RemoteException {
                Log.i("RemoteWatchService", "onServiceDath");

            }
        };
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("RemoteWatchService", "远程绑定本地服务成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("RemoteWatchService", "检测到本地被杀 ");
            startService(intent1);
            Log.i("RemoteWatchService", "启动本地 ");
            bindLocal();
        }


    }

    /**
     * 通知栏常驻
     */
    private void getNotfication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setTicker("后台服务启用")
                    .setContentTitle("后台")
                    .setContentText("ssssssssssssssssssssssss")
                    .setNumber(1)
                    .getNotification();
            startForeground(0x111, notification);
        }
    }





    private void bindLocal(){
        Log.i("RemoteWatchService", "绑定本地 ");
        if (myServiceConnection == null) {
            myServiceConnection = new MyServiceConnection();
        }
        bindService(intent1,myServiceConnection , Service.BIND_IMPORTANT);
    }
}
