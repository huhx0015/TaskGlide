package com.devhack.taskglide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devhack.taskglide.R;
import com.devhack.taskglide.models.Task;
import com.devhack.taskglide.ui.TasksAdapter;
import java.util.LinkedList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class TasksFragment extends Fragment {

    private static final int NUMBER_OF_COLUMNS = 1;

    private Unbinder unbinder;

    @BindView(R.id.fragment_recyclerview) RecyclerView fragmentRecyclerView;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, fragment_view);
        initView();
        return fragment_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        fragmentRecyclerView.setLayoutManager(layoutManager);

        // TODO: Change list of tasks later, once network response is completed.
        List<Task> taskList = new LinkedList<>();
        taskList.add(new Task("Couple Photos at Golden Gate Park"));
        taskList.add(new Task("Family Photos at Engagement Party at Palace of Fine Arts"));
        taskList.add(new Task("Wedding Rehearsal Photos"));
        taskList.add(new Task("Wedding Photos"));

        TasksAdapter adapter = new TasksAdapter(taskList, getContext());
        fragmentRecyclerView.setAdapter(adapter);
    }
}
