package com.taskerlite.main;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.taskerlite.R;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTaskList extends Fragment implements View.OnClickListener{

    private DataActivity dataActivity;

    public interface DataActivity {
        public ProfileController taskListAskProfileList();
    }

    ProfileController profileController;

    LayoutInflater inflater;
    Activity activity;
    Context context;

    AppAdapter mAdapter;
    SwipeMenuListView mListView;

    ImageButton buttonPlus;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        dataActivity = (DataActivity) activity;
        profileController = dataActivity.taskListAskProfileList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        this.inflater = inflater;
        activity = getActivity();
        context  = getActivity();

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(itemClickListener);
        mListView.setOnItemClickListener(onSceneClickListener);

        buttonPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        buttonPlus.setOnClickListener(this);

        return view;
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

           int bgSize   = getResources().getInteger(R.integer.swipe_menu_bg_size);

           SwipeMenuItem onItem = new SwipeMenuItem(context);
           onItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
           onItem.setWidth(Screen.dp2px(context, bgSize));
           onItem.setIcon(TaskerIcons.getInstance().getSwMenuStart());
           menu.addMenuItem(onItem);

           SwipeMenuItem offItem = new SwipeMenuItem(context);
           offItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
           offItem.setWidth(Screen.dp2px(context, bgSize));
           offItem.setIcon(TaskerIcons.getInstance().getSwMenuStop());
           menu.addMenuItem(offItem);

           SwipeMenuItem deleteItem = new SwipeMenuItem(context);
           deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
           deleteItem.setWidth(Screen.dp2px(context, bgSize));
           deleteItem.setIcon(TaskerIcons.getInstance().getSwMenuDelete());
           menu.addMenuItem(deleteItem);
        }
    };

    AdapterView.OnItemClickListener onSceneClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            goToBuilderFragment(position);
        }
    };

    OnMenuItemClickListener itemClickListener = new OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            switch (index) {
                case 0:
                    for(TaskElement task : profileController.getProfile(position).getTaskList()){
                        task.getTaskObject().start(context);
                    }
                    break;
                case 1:
                    for(TaskElement task : profileController.getProfile(position).getTaskList()){
                        task.getTaskObject().stop(context);
                    }
                    break;
                case 2:
                    profileController.removeProfileFromList(position);
                    mAdapter.notifyDataSetChanged();

                    break;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {

        profileController.addNewProfile("");
        goToBuilderFragment(profileController.getProfileListSize() - 1);
    }

    private void goToBuilderFragment(int index){

        getFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).
        replace(R.id.fragmentConteiner, FragmentTaskBuilder.getInstance(index)).
        addToBackStack(null).
        commit();
    }

    class AppAdapter extends BaseAdapter {

        public int getCount() {
            return profileController.getProfileListSize();
        }

        public Profile getItem(int position) {
            return profileController.getProfile(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = View.inflate(context, R.layout.list_item_scene, null);

            TextView t = (TextView) convertView.findViewById(R.id.sceneNameID);
            t.setText(getItem(position).getName());
            LinearLayout iconsLay = (LinearLayout) convertView.findViewById(R.id.sceneIconsPlaceId);
            iconsLay.removeAllViews();

            for(ActionElement action : getItem(position).getActionList()){
                ImageView img = new ImageView(context);
                img.setBackgroundDrawable(TaskerIcons.getInstance().getPreviewIcon(action.getActionType()));
                iconsLay.addView(img);
            }

            for(TaskElement task : getItem(position).getTaskList()){
                ImageView img = new ImageView(context);
                img.setBackgroundDrawable(TaskerIcons.getInstance().getPreviewIcon(task.getTaskType()));
                iconsLay.addView(img);
            }

            return convertView;
        }
    }
}