package com.devhack.taskglide.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.devhack.taskglide.R;
import com.devhack.taskglide.fragments.ChatFragment;
import com.devhack.taskglide.fragments.FilesFragment;
import com.devhack.taskglide.fragments.TasksFragment;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class TaskGlidePagerAdapter extends FragmentStatePagerAdapter {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final int NUMBER_OF_ACTION_VIEWS = 3;

    private Context context;

    /** PAGER ADAPTER METHODS __________________________________________________________________ **/

    public TaskGlidePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ACTION_VIEWS;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return TasksFragment.newInstance();
            case 1:
                return ChatFragment.newInstance();
            case 2:
                return FilesFragment.newInstance();
            default:
                return TasksFragment.newInstance();
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return context.getString(R.string.tab_tasks);
            case 1:
                return context.getString(R.string.tab_chat);
            case 2:
                return context.getString(R.string.tab_files);
            default:
                return context.getString(R.string.tab_tasks);
        }
    }
}