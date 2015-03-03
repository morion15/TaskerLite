package com.taskerlite.logic.actions;

import android.app.FragmentManager;
import android.content.Context;
import com.taskerlite.R;
import com.taskerlite.source.Types.*;

public class aBootComplete extends mAction{

    public aBootComplete(Context context){
        setName(context.getResources().getString(R.string.a_boot_complete_short));
    }

    @Override
    public boolean isMyAction(Context context, TYPES type) {
        return type == TYPES.A_BOOT_COMPLETE;
    }

    @Override
    public void show(FragmentManager fm) {

    }
}
