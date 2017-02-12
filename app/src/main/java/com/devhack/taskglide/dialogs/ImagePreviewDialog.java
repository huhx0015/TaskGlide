package com.devhack.taskglide.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.devhack.taskglide.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class ImagePreviewDialog extends DialogFragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private int imagePreviewResource;
    private String imagePreviewUrl;
    private Unbinder unbinder;

    @BindView(R.id.image_preview) SubsamplingScaleImageView imagePreview;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public ImagePreviewDialog(int resource) {
        this.imagePreviewResource = resource;
    }

    public static ImagePreviewDialog newInstance(int stageMap) {
        return new ImagePreviewDialog(stageMap);
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View image_preview_view = inflater.inflate(R.layout.dialog_image_preview, container, false);
        unbinder = ButterKnife.bind(this, image_preview_view);

        // Sets the dialog background transparent.
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();

        return image_preview_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imagePreview.recycle();
        unbinder.unbind();
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void initView() {

        if (imagePreviewResource == 0) {
            imagePreviewResource = R.drawable.file_1;
        }

        imagePreview.setImage(ImageSource.resource(imagePreviewResource));
    }
}