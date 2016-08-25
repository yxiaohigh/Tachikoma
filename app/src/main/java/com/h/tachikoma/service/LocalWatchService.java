package com.h.tachikoma.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 本地服务
 * Created by tony on 2016/8/22.
 */
public class LocalWatchService extends Service {

    private ServiceConnection myServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalWatchService", "本地服务启动成功");
        startRemote();
        return START_STICKY;
    }

    /**
     * 启动LocalRemote
     */
    private void startRemote() {
        Intent intent1 = new Intent();
        intent1.setAction("com.h.tachikoma.service.RemoteWatchService");
        intent1.setPackage(getPackageName());
        startService(intent1);
        Log.i("LocalWatchService", " 启动远程");
        if (myServiceConnection == null) {
             myServiceConnection = new MyServiceConnection();
        }
        bindService(intent1, myServiceConnection, Service.BIND_IMPORTANT);
        Log.i("LocalWatchService", " 绑定远程");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IWatchAidl.Stub() {
            @Override
            public void onServiceStart() throws RemoteException {
                Log.i("LocalWatchService", "onServiceStart");
            }

            @Override
            public void onServiceDath() throws RemoteException {
                Log.i("LocalWatchService", "onServiceDath");
            }
        };
    }

    private class MyServiceConnection implements ServiceConnection {



        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("LocalWatchService", "本地绑定远程服务成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("LocalWatchService", "监视到远程服务被杀");
            startRemote();
        }

    }


}
