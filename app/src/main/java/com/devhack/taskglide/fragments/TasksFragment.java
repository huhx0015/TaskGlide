package com.devhack.taskglide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.devhack.taskglide.R;
import com.devhack.taskglide.constants.TaskGlideConstants;
import com.devhack.taskglide.models.Task;
import com.devhack.taskglide.ui.TasksAdapter;
import com.devhack.taskglide.utils.PubNubUtils;
import com.devhack.taskglide.utils.SnackbarUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.LinkedList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class TasksFragment extends Fragment {

    private static final String LOG_TAG = TasksFragment.class.getSimpleName();

    private static final int NUMBER_OF_COLUMNS = 1;

    private boolean isInitialCall = true;
    private boolean isConnected = false;
    private List<Task> taskList;
    private PubNub pubNub;
    private Unbinder unbinder;

    @BindView(R.id.fragment_list_layout) RelativeLayout fragmentTaskLayout;
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
        initPubNubConnection();
        return fragment_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {

        // TODO: Change list of tasks later, once network response is completed.
        taskList = new LinkedList<>();
        taskList.add(new Task("Couple Photos at Golden Gate Park", "$1000", false));
        taskList.add(new Task("Family Photos at Engagement Party at Palace of Fine Arts", "$2000", false));
        taskList.add(new Task("Wedding Rehearsal Photos", "$3000", false));
        taskList.add(new Task("Wedding Photos", "$10000", false));

        initRecyclerView(taskList);
    }

    private void initRecyclerView(List<Task> taskList) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        fragmentRecyclerView.setLayoutManager(layoutManager);

        TasksAdapter adapter = new TasksAdapter(taskList, getContext());
        fragmentRecyclerView.setAdapter(adapter);
    }

    /** PUBNUB METHODS _________________________________________________________________________ **/

    //"name"
    //"amount"
    //"type"

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

                        queryAllTasks();
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
                if (!isInitialCall) {
                    getUpdatedTasks(message);
                } else {
                    isInitialCall = false;
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                Log.d(LOG_TAG, "presence(): Presence callback invoked.");
                // handle incoming presence data
            }
        };

        PubNubUtils.initPubNub(callback, TaskGlideConstants.TASKS_CHANNEL, getContext());
    }

    private void queryAllTasks() {
        if (pubNub != null && isConnected) {
            JsonObject message = new JsonObject();
            message.addProperty("name", "don't care");

            Log.d(LOG_TAG, "status(): ConnectedCategory publish initializing...");
            pubNub.publish().channel(TaskGlideConstants.TASKS_CHANNEL)
                    .message(message)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            // Check whether request successfully completed or not.
                            if (!status.isError()) {
                                Log.d(LOG_TAG, "onResponse(): Response successful: " + status.getStatusCode());

                                SnackbarUtils.displaySnackbar(fragmentTaskLayout,
                                        getString(R.string.tasks_update_success),
                                        Snackbar.LENGTH_SHORT,
                                        ContextCompat.getColor(getContext(), R.color.colorAccent));
                            }
                            // Request processing failed.
                            else {
                                SnackbarUtils.displaySnackbarWithAction(fragmentTaskLayout,
                                        getString(R.string.tasks_update_error),
                                        Snackbar.LENGTH_INDEFINITE,
                                        ContextCompat.getColor(getContext(), android.R.color.holo_red_light),
                                        getString(R.string.chat_retry),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                queryAllTasks();
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

    // TYPE:
    // 0 = clientupdate
    // 1 = serverupdate

    private void getUpdatedTasks(PNMessageResult result) {

        Log.d(LOG_TAG, "getUpdatedTasks(): Result: " + result.getMessage().toString());

        JsonElement jsonElement = result.getMessage();
        Log.d(LOG_TAG, "getUpdatedTasks(): jsonElement: " + jsonElement.toString());

        JsonObject jsonTask = jsonElement.getAsJsonObject();
        Log.d(LOG_TAG, "getUpdatedTasks(): jsonTask: " + jsonTask.toString());

        int type = jsonTask.get("type").getAsInt();
        Log.d(LOG_TAG, "getUpdatedTasks(): type: " + type);

        JsonArray jsonArray = jsonTask.get("tasks").getAsJsonArray();
        Log.d(LOG_TAG, "getUpdatedTasks(): jsonArray: " + jsonArray.toString());

        taskList = new LinkedList<>();
        for (JsonElement element : jsonArray) {
            JsonObject taskObject = element.getAsJsonObject();

            String name = taskObject.get("name").toString();
            String amount = taskObject.get("amount").toString();
            int statusValue = taskObject.get("status").getAsInt();

            boolean status;
            if (statusValue == 0) {
                status = false;
            } else {
                status = true;
            }

            Task newTask = new Task(name, amount, status);
            taskList.add(newTask);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initRecyclerView(taskList);
            }
        });
    }
}
