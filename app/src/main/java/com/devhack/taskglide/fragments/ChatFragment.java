package com.devhack.taskglide.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.devhack.taskglide.R;
import com.devhack.taskglide.constants.TaskGlideConstants;
import com.devhack.taskglide.models.Message;
import com.devhack.taskglide.ui.views.MessageView;
import com.devhack.taskglide.utils.PubNubUtils;
import com.devhack.taskglide.utils.SnackbarUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class ChatFragment extends Fragment {

    private static final String LOG_TAG = ChatFragment.class.getSimpleName();

    private boolean isConnected = false;
    private PubNub pubNub;
    private Unbinder unbinder;

    @BindView(R.id.chat_message_container) LinearLayout chatMessageContainer;
    @BindView(R.id.fragment_chat_layout) LinearLayout fragmentChatLayout;
    @BindView(R.id.chat_send_message_button) ImageButton sendMessageButton;
    @BindView(R.id.chat_input_text) EditText messageInputField;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, fragment_view);

        initView();
        initPubNubConnection();

        return fragment_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {
        initButtons();
    }

    private void initButtons() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Hides the soft keyboard.
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fragmentChatLayout.getWindowToken(), 0);

                if (pubNub != null && isConnected) {

                    JsonObject message = new JsonObject();
                    message.addProperty("user", getString(R.string.demo_user_name));
                    message.addProperty("message", messageInputField.getText().toString());
                    Log.d(LOG_TAG, "initButtons(): JsonObject conversion: " + message.toString());

                    Log.d(LOG_TAG, "status(): ConnectedCategory publish initializing...");
                    pubNub.publish().channel(TaskGlideConstants.TUTORIAL_CHANNEL)
                            .message(message)
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {
                                    // Check whether request successfully completed or not.
                                    if (!status.isError()) {
                                        Log.d(LOG_TAG, "onResponse(): Response successful: " + status.getStatusCode());
                                        messageInputField.setText("");
                                        SnackbarUtils.displaySnackbar(fragmentChatLayout,
                                                getString(R.string.chat_send_success),
                                                Snackbar.LENGTH_SHORT,
                                                ContextCompat.getColor(getContext(), R.color.colorAccent));
                                    }
                                    // Request processing failed.
                                    else {
                                        SnackbarUtils.displaySnackbarWithAction(fragmentChatLayout,
                                                getString(R.string.chat_send_error),
                                                Snackbar.LENGTH_INDEFINITE,
                                                ContextCompat.getColor(getContext(), android.R.color.holo_red_light),
                                                getString(R.string.chat_retry),
                                                new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sendMessageButton.performClick();
                                            }
                                        });
                                        Log.d(LOG_TAG, "onResponse(): Response failure.");
                                        // Handle message publish error. Check 'category' property to find out possible issue
                                        // because of which request did fail.
                                        //
                                        // Request can be resent using: [status retry];
                                    }
                                }
                            });
                }
            }
        });
    }

    /** PUBNUB METHODS _________________________________________________________________________ **/

    private void initPubNubConnection() {
        SubscribeCallback callback = new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus status) {

                Log.d(LOG_TAG, "status(): Status callback called: " + status.getStatusCode());

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    Log.d(LOG_TAG, "status(): UnexpectedDisconnectCategory");
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    Log.d(LOG_TAG, "status(): ConnectedCategory");
                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        pubNub = pubnub;
                        isConnected = true;

                        if (pubnub != null) {
                            PubNubUtils.setPushNotificationListener(pubNub, TaskGlideConstants.TUTORIAL_CHANNEL);
                        }
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    Log.d(LOG_TAG, "status(): Reconnected category.");
                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    Log.d(LOG_TAG, "status(): DecryptionErrorCategory");
                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Log.d(LOG_TAG, "message(): Message callback invoked.");

                JsonElement messageElement = message.getMessage();
                Gson gson = new Gson();
                final Message receivedMessage = gson.fromJson(messageElement, Message.class);

                boolean isMe = false;
                if (receivedMessage.getUser().equals(getString(R.string.demo_user_name))) {
                    isMe = true;
                }

//                JsonObject messageObject = messageElement.getAsJsonObject();
//                String messageUser = messageObject.get("user").toString() + ": ";
//                messageUser = messageUser.replace("\"", "");
//                String messageMessage = messageObject.get("message").toString();
//                messageMessage = messageMessage.replace("\"", "");
//                final String messageString = messageUser + messageMessage;

                if (isVisible()) {
                    final boolean finalIsMe = isMe;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getChatResponse(receivedMessage.getMessage(), receivedMessage.getUser(), finalIsMe);
                        }
                    });
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                Log.d(LOG_TAG, "presence(): Presence callback invoked.");
                // handle incoming presence data
            }
        };

        PubNubUtils.initPubNub(callback, TaskGlideConstants.TUTORIAL_CHANNEL, getContext());
    }

    private void getChatResponse(String response, String user, boolean isMe) {
        MessageView messageView = new MessageView(response, isMe, user, getContext());
        chatMessageContainer.addView(messageView);
    }

    private void getChatHistory() {

        pubNub.history()
                .channel(TaskGlideConstants.TUTORIAL_CHANNEL) // where to fetch history from
                .count(100) // how many items to fetch
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {

                    }
                });
    }
}
