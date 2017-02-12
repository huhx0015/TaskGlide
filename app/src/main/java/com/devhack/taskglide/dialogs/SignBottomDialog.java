package com.devhack.taskglide.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import com.devhack.taskglide.R;
import com.devhack.taskglide.models.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class SignBottomDialog extends BottomSheetDialogFragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final String LOG_TAG = SignBottomDialog.class.getSimpleName();

    private Task task;
    private Unbinder unbinder;

    @BindView(R.id.sign_button) Button signButton;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public SignBottomDialog(Task task) {
        this.task = task;
    }

    public static SignBottomDialog newInstance(Task task) {
        return new SignBottomDialog(task);
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View dialog_sign_bottom = View.inflate(getContext(), R.layout.dialog_sign_bottom, null);
        dialog.setContentView(dialog_sign_bottom);
        unbinder = ButterKnife.bind(this, dialog_sign_bottom);

        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void initView() {
        initButton();
    }

    private void initButton() {
        signButton.setShadowLayer(2, 2, 2, Color.BLACK);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Launch sign action.
            }
        });
    }
}
