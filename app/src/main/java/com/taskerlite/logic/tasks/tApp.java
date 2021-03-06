package com.taskerlite.logic.tasks;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taskerlite.R;
import com.taskerlite.source.Fonts;
import com.taskerlite.source.mNotification;

import java.util.List;

public class tApp extends mTask {

    private String packageName = "";

	@Override
	public void start(Context context) {

        try {

            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);

            String header = context.getResources().getString(R.string.t_app_short);
            mNotification.getInstance(context).createInfoNotification(header, getName());

        }catch (Exception e){ }
	}

    @Override
    public void stop(Context context) {
/*
        try {

            if(isPackageRunning(context)) {

                ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                mActivityManager.killBackgroundProcesses(packageName);
            }

        }catch (Exception e){ }
*/
    }

    @Override
    public void show(FragmentManager fm, Context context) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "T_APP");
    }

    public boolean isPackageRunning(Context context) {

        return findPIDbyPackageName(context) != -1;
    }

    public int findPIDbyPackageName(Context context) {

        int result = -1;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (am != null) {
            for (RunningAppProcessInfo pi : am.getRunningAppProcesses()){
                if (pi.processName.equalsIgnoreCase(packageName))
                    result = pi.pid;
                if (result != -1)
                    break;
            }
        } else {
            result = -1;
        }
        return result;
    }

    public static class UI extends DialogFragment {

        private List<ApplicationInfo> mAppList;
        private mAdapter adapter;
        private tApp task;

        public void setParent (tApp task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_app, container);
            ListView lvMain = (ListView) view.findViewById(R.id.listView);

            mAppList = getActivity().getPackageManager().getInstalledApplications(0);
            adapter = new mAdapter(getActivity());
            lvMain.setAdapter(adapter);
            lvMain.setOnItemClickListener(listListener);

            Fonts fonts = new Fonts(getActivity());
            fonts.setupLayoutTypefaces(view);

            return view;
        }

        AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ApplicationInfo item = mAppList.get(position);
                task.setName(String.valueOf(item.loadLabel(getActivity().getPackageManager())));
                task.packageName = item.packageName;
                dismiss();
            }
        };

        class mAdapter extends BaseAdapter {

            private Context context;

            public mAdapter(Context context){
                this.context = context;
            }

            public int getCount() {
                return mAppList.size();
            }

            public ApplicationInfo getItem(int position) {
                return mAppList.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View rowView = View.inflate(context, R.layout.list_item_element, null);

                ApplicationInfo item = getItem(position);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.imageId);
                imageView.setImageDrawable(item.loadIcon(context.getPackageManager()));
                TextView textView = (TextView) rowView.findViewById(R.id.textDescriptionId);
                textView.setText(item.loadLabel(context.getPackageManager()));

                Fonts fonts = new Fonts(getActivity());
                fonts.setupLayoutTypefaces(rowView);

                return rowView;
            }
        }
    }
}
