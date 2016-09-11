package com.applozic.mobicomkit.sample.pushnotification;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by sunil on 9/4/16.
 */
public class GcmInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        GCMRegistrationUtils gcmRegistrationUtils = new GCMRegistrationUtils(this);
        gcmRegistrationUtils.setUpGcmNotification();
    }
}
