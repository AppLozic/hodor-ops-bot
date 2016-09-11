package com.applozic.mobicomkit.api.conversation;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;

import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;

import java.math.BigInteger;

/**
 * Created by sunil on 26/12/15.
 */
public class ApplozicIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public static final String PAIRED_MESSAGE_KEY_STRING = "pairedMessageKey";
    public static final String CONTACT = "contact";
    public static final String CHANNEL = "channel";
    private static final String TAG = "ApplozicIntentService";

    public ApplozicIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String pairedMessageKeyString = intent.getStringExtra(PAIRED_MESSAGE_KEY_STRING);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final MessageClientService messageClientService = new MessageClientService(getApplicationContext());

                if (!TextUtils.isEmpty(pairedMessageKeyString)) {
                    messageClientService.updateReadStatusForSingleMessage(pairedMessageKeyString);
                }

            }
        });
        thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

    }
}

