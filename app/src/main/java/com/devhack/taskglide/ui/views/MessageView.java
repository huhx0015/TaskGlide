package com.devhack.taskglide.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.devhack.taskglide.R;
import com.makeramen.roundedimageview.RoundedImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class MessageView extends RelativeLayout {

    private boolean isMe = false;
    private String message;
    private String username;
    private Context context;

    @BindView(R.id.view_other_name) TextView messageOtherName;
    @BindView(R.id.view_me_name) TextView messageMeName;
    @BindView(R.id.view_other_container) RelativeLayout messageOtherContainer;
    @BindView(R.id.view_me_container) RelativeLayout messageMeContainer;
    @BindView(R.id.view_message_me) RoundedImageView messageAvatarMe;
    @BindView(R.id.view_message_other) RoundedImageView messageAvatarOther;
    @BindView(R.id.view_message_text) TextView messageText;

    public MessageView(String message, boolean isMe, String userName, Context context) {
        super(context);
        this.message = message;
        this.isMe = isMe;
        this.username = userName;
        this.context = context;
        initView();
    }

    public MessageView(String message, boolean isMe, String userName, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.message = message;
        this.isMe = isMe;
        this.username = userName;
        this.context = context;
        initView();
    }

    public MessageView(String message, boolean isMe, String userName, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.message = message;
        this.isMe = isMe;
        this.username = userName;
        this.context = context;
        initView();
    }

    public MessageView(String message, boolean isMe, String userName, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.message = message;
        this.isMe = isMe;
        this.username = userName;
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View message_view = inflater.inflate(R.layout.view_message, this, true);
        ButterKnife.bind(this, message_view);

        if (isMe) {
            messageOtherContainer.setVisibility(View.GONE);
            messageText.setGravity(Gravity.RIGHT);
            messageMeName.setText(username);
        } else {
            messageMeContainer.setVisibility(View.GONE);
            messageText.setGravity(Gravity.LEFT);
            messageOtherName.setText(username);
        }

        if (message != null) {
            messageText.setText(message);
        }
    }
}
