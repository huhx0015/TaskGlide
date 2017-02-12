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
import com.devhack.taskglide.models.File;
import com.devhack.taskglide.ui.adapters.FilesAdapter;
import java.util.LinkedList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class FilesFragment extends Fragment {

    private static final int NUMBER_OF_COLUMNS = 2;

    private Unbinder unbinder;

    @BindView(R.id.fragment_recyclerview) RecyclerView fragmentRecyclerView;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static FilesFragment newInstance() {
        return new FilesFragment();
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
        List<File> fileList = new LinkedList<>();
        fileList.add(new File("Graphic_Asset_1.jpg", R.drawable.file_1));
        fileList.add(new File("Graphic_Asset_2.jpg", R.drawable.file_2));
        fileList.add(new File("Graphic_Asset_3.jpg", R.drawable.file_3));
        fileList.add(new File("Graphic_Asset_4.jpg", R.drawable.file_4));
        fileList.add(new File("Graphic_Asset_5.jpg", R.drawable.file_5));

        FilesAdapter adapter = new FilesAdapter(fileList, getContext());
        fragmentRecyclerView.setAdapter(adapter);
    }
}
