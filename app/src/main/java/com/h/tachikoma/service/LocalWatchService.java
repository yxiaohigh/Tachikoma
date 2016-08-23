package com.h.tachikoma.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.h.tachikoma.R;

/**
 * 本地服务
 * Created by tony on 2016/8/22.
 */
public class LocalWatchService extends Service {

    private ServiceConnection serviceConnection;
    private Intent intent1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        serviceConnection = new MyServiceConnection();
        intent1 = new Intent(this, RemoteWatchService.class);
        bindService(intent1, serviceConnection, Service.BIND_AUTO_CREATE);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IWatchAidl.Stub stub = new IWatchAidl.Stub() {
            @Override
            public void onServiceStart() throws RemoteException {

            }

            @Override
            public void onServiceDath() throws RemoteException {

            }
        };
        return stub;
    }

    private class MyServiceConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(intent1);
            bindService(intent1, serviceConnection, Service.BIND_AUTO_CREATE);
        }

    }


}
