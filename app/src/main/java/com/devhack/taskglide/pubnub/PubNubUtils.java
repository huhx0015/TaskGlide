package com.devhack.taskglide.pubnub;

import android.content.Context;
import android.util.Log;
import com.devhack.taskglide.R;
import com.devhack.taskglide.constants.TaskGlideConstants;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.Arrays;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class PubNubUtils {

    private static final String LOG_TAG = PubNubUtils.class.getSimpleName();

    public static void initPubNub(Context context) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(context.getString(R.string.pubnub_subscribe_key));
        pnConfiguration.setPublishKey(context.getString(R.string.pubnub_publish_key));
        PubNub pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus status) {
                // the status object returned is always related to subscribe but could contain
                // information about subscribe, heartbeat, or errors
                // use the operationType to switch on different options
                switch (status.getOperation()) {
                    // let's combine unsubscribe and subscribe handling for ease of use
                    case PNSubscribeOperation:
                    case PNUnsubscribeOperation:
                        // note: subscribe statuses never have traditional
                        // errors, they just have categories to represent the
                        // different issues or successes that occur as part of subscribe
                        switch(status.getCategory()) {
                            case PNConnectedCategory:
                                Log.d(LOG_TAG, "status(): Connected Category");
                                // this is expected for a subscribe, this means there is no error or issue whatsoever

                                // Connect event. You can do stuff like publish, and know you'll get it.
                                // Or just use the connected event to confirm you are subscribed for
                                // UI / internal notifications, etc
                                pubnub.publish()
                                        .channel(TaskGlideConstants.TUTORIAL_CHANNEL)
                                        .message("hello!!")
                                        .async(new PNCallback<PNPublishResult>() {
                                            @Override
                                            public void onResponse(PNPublishResult result, PNStatus status) {
                                                Log.d(LOG_TAG, "onResponse() called.");

                                                // Check whether request successfully completed or not.
                                                if (!status.isError()) {
                                                    Log.d(LOG_TAG, "onResponse(): Is not error.");
                                                    // Message successfully published to specified channel.
                                                }
                                                // Request processing failed.
                                                else {
                                                    Log.d(LOG_TAG, "onResponse(): Request processing failed.");
                                                    // Handle message publish error. Check 'category' property to find out possible issue
                                                    // because of which request did fail.
                                                    //
                                                    // Request can be resent using: [status retry];
                                                }
                                            }
                                        });
                            case PNReconnectedCategory:
                                Log.d(LOG_TAG, "status(): Reconnected Category");
                                // this usually occurs if subscribe temporarily fails but reconnects. This means
                                // there was an error but there is no longer any issue
                            case PNDisconnectedCategory:
                                Log.d(LOG_TAG, "status(): Disconnected Category");
                                // this is the expected category for an unsubscribe. This means there
                                // was no error in unsubscribing from everything
                            case PNUnexpectedDisconnectCategory:
                                Log.d(LOG_TAG, "initPubNub(): " + status.getErrorData());
                                // this is usually an issue with the internet connection, this is an error, handle appropriately
                            case PNAccessDeniedCategory:
                                Log.d(LOG_TAG, "status(): Access Denied Category");
                                // this means that PAM does allow this client to subscribe to this
                                // channel and channel group configuration. This is another explicit error
                            default:
                                // More errors can be directly specified by creating explicit cases for other
                                // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
                        }

                    case PNHeartbeatOperation:
                        Log.d(LOG_TAG, "status(): Heartbeat Operation");
                        // heartbeat operations can in fact have errors, so it is important to check first for an error.
                        // For more information on how to configure heartbeat notifications through the status
                        // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                        if (status.isError()) {
                            // There was an error with the heartbeat operation, handle here
                        } else {
                            // heartbeat operation was successful
                        }
                    default: {
                        // Encountered unknown status type
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // handle incoming messages
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                // handle incoming presence data
            }
        });

        pubnub.subscribe().channels(Arrays.asList(TaskGlideConstants.TUTORIAL_CHANNEL)).execute();
    }
}
