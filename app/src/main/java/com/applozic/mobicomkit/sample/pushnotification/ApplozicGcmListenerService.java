package com.applozic.mobicomkit.sample.pushnotification;

import android.os.Bundle;
import android.util.Log;

import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.google.android.gms.gcm.GcmListenerService;

public class ApplozicGcmListenerService extends GcmListenerService {

    private static final String TAG = "ApplozicGcmListener";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (MobiComPushReceiver.isMobiComPushNotification(data)) {
            Log.i(TAG, "Applozic notification processing...");
            MobiComPushReceiver.processMessageAsync(this, data);
            return;
        }
    }

}