package com.h.tachikoma.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteWatchService extends Service {
    public RemoteWatchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
