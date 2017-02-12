package com.devhack.taskglide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devhack.taskglide.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class FilesFragment extends Fragment {

    private Unbinder unbinder;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static FilesFragment newInstance() {
        return new FilesFragment();
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_files, container, false);
        unbinder = ButterKnife.bind(this, fragment_view);
        return fragment_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
