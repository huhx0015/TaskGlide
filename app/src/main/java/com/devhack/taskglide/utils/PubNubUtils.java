package com.devhack.taskglide.utils;

import android.content.Context;
import android.util.Log;
import com.devhack.taskglide.R;
import com.devhack.taskglide.constants.TaskGlideConstants;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;

import java.util.Arrays;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class PubNubUtils {

    private static final String LOG_TAG = PubNubUtils.class.getSimpleName();

    public static void initPubNub(SubscribeCallback callback, String channel, Context context) {
        Log.d(LOG_TAG, "initPubNub(): Initializing PubNub connection.");

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(context.getString(R.string.pubnub_subscribe_key));
        pnConfiguration.setPublishKey(context.getString(R.string.pubnub_publish_key));
        PubNub pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(callback);
        pubnub.subscribe().channels(Arrays.asList(channel)).execute();
    }
}
