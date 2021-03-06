package com.taskerlite.logic.actions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.taskerlite.R;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.taskerlite.source.Fonts;
import com.taskerlite.source.Types.*;

public class aTimer extends mAction {

	private int hour, minute;
	
	public aTimer(Context context){

        Calendar cal = new GregorianCalendar();

		this.hour   = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);

        setName(context.getString(R.string.a_time_short));
	}

	@Override
	public boolean isMyAction(Context context, TYPES type) {
		
		boolean state = false;
		
		if(type == TYPES.A_TIME){
			
			Calendar cal = new GregorianCalendar();
			
			if( cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == minute)
				state = true;			
		}
		
		return state;
	}

    @Override
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "");
    }

    public static class UI extends DialogFragment {

        private aTimer action;
        private TimePicker timePicker;

        public void setParent (aTimer action){
            this.action = action;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_action_time, container);

            timePicker = (TimePicker) view.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            timePicker.setCurrentMinute(action.minute);
            timePicker.setCurrentHour(action.hour);

            Fonts fonts = new Fonts(getActivity());
            fonts.setupLayoutTypefaces(view);

            return view;
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            action.hour   = timePicker.getCurrentHour();
            action.minute = timePicker.getCurrentMinute();

            dismiss();
        }
    }
}
