package com.taskerlite.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.taskerlite.main.TService;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            if(!TService.isRunning(context))
                context.startService(new Intent(context, TService.class));
        }
    }
}
