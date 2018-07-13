package com.jokin.framework.cwindow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ModuleService extends Service {
    public ModuleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FloatView floatView = new FloatView(this);
        floatView.setLayout(R.layout.layout_module);
        floatView.show();
    }
}
