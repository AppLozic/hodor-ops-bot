package com.applozic.mobicomkit.sample;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.applozic.mobicomkit.api.attachment.NotificationHelper;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComMessageService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.commons.image.ImageLoader;
import com.applozic.mobicommons.people.contact.Contact;

public class NotificationActivity extends Activity {

    BaseContactService baseContactService;
    MobiComMessageService messageService;
    ImageLoader mImageLoader;
    boolean responded;
    private BroadcastReceiver applozicBroadCastReceiver;
    Contact contact;
    String inComingCallId;
    Vibrator vibrator;
    Ringtone r;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_received);


        //Notifications and Vibrations...
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vibrator.vibrate(pattern, 0);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        baseContactService = new AppContactService(this);
        messageService = new MobiComMessageService(this, MessageIntentService.class);
        Intent intent = getIntent();

        //// contactId /////////
        final String contactId = intent.getStringExtra("CONTACT_ID");
        inComingCallId = intent.getStringExtra(NotificationHelper.NOTIFICATION_ID);

        contact = baseContactService.getContactById(contactId);

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking_ani);
        shake.setRepeatCount(Animation.INFINITE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Button accept = (Button) findViewById(R.id.alarmlistitem_acceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    responded = true;
                    //TODO: responded ..may be neviagte to detail page, define some actions....like clean, stop etc.
                    vibrator.cancel();
                    if (r.isPlaying()) {
                        r.stop();
                    }
                    //TODO:Send command back
                    Intent hodorChat = new Intent(NotificationActivity.this, ConversationActivity.class);
                    hodorChat.putExtra("takeOrder", true);
                    hodorChat.putExtra(ConversationUIService.CONTACT_ID, contactId);
                    startActivity(hodorChat);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        accept.startAnimation(shake);

        Button reject = (Button) findViewById(R.id.alarmlistitem_rejectButton);
        ImageView profileImage = (ImageView) findViewById(R.id.notification_profile_image);
        TextView textView = (TextView) findViewById(R.id.notification_user_name);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectCall();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!responded) {
                        rejectCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, NotificationHelper.MAX_NOTIFICATION_RING_DURATION);


        mImageLoader = new ImageLoader(this, profileImage.getHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                return baseContactService.downloadContactImage(NotificationActivity.this, (Contact) data);
            }
        };
        mImageLoader.setLoadingImage(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_ic_contact_picture_holo_light);
        // Add a cache to the image loader
        mImageLoader.setImageFadeIn(false);
        mImageLoader.loadImage(contact, profileImage);
        textView.setText(contact.getDisplayName());
        applozicBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String callId = intent.getStringExtra(NotificationHelper.NOTIFICATION_ID);
                // TODO: Some one else has responded to notification...
                boolean isNotificationForSameId =  (inComingCallId.equals(callId));
//                if (NotificationHelper.CALL_CANCELED.equals(intent.getAction()) && isNotificationForSameId
//                         ) {
//                    responded=true;
//                    vibrator.cancel();
//                    if (r.isPlaying()) {
//                        r.stop();
//                    }
//                    finish();
//
//                }
            }
        };
        registerForBroadcast();

    }

    private void rejectCall() {
      // TODO: PERSON HAS REJECTED TO RESPONSE.
        try {
            responded = true;
            NotificationHelper helper = new NotificationHelper(NotificationActivity.this);
            //Reject a call...
            vibrator.cancel();
            if (r.isPlaying()) {
                r.stop();
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void registerForBroadcast() {
        IntentFilter intentFilter = new IntentFilter();

       // intentFilter.addAction(NotificationHelper.CALL_CANCELED);
        LocalBroadcastManager.getInstance(this).registerReceiver(applozicBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(applozicBroadCastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rejectCall();
    }
}