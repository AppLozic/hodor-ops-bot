package com.applozic.mobicomkit.api.attachment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicommons.people.contact.Contact;

import java.util.Map;

/**
 * Created by devashish on 08/08/16.
 */
public class NotificationHelper {



    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String SEVERITY_LEVEL= " SEVERITY_LEVEL";
    public static final String ERROR_DETAILS  = "ERROR_DETAILS";
    public static final String COMMAND_TO_ACTION  = "COMMAND_TO_ACTION";
    public static final String RING= "RING";

    public static final String NOTIFICATION_ACTIVITY_NAME = "com.applozic.mobicomkit.sample.NotificationActivity";

    public static final int MAX_NOTIFICATION_RING_DURATION = 1 * 20 * 1000;

    String videoCallId;
    Context context;

    private MobiComConversationService conversationService;
    private AppContactService baseContactService;
    private MessageDatabaseService messageDatabaseService;
    private String TAG = "NotificationHelper";


    public NotificationHelper(Context context){
        this.context= context;
        init();
    }

    public void init(){

        this.conversationService = new MobiComConversationService(context);
        this.baseContactService = new AppContactService(context);
        this.messageDatabaseService =  new MessageDatabaseService(context);

    }




    @NonNull
    private Message getNotificationMessage(Contact contact) {
        Message notificationMessage = new Message();
        MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(context);

        notificationMessage.setContactIds(contact.getContactIds());
        notificationMessage.setTo(contact.getContactIds());
        notificationMessage.setCreatedAtTime(System.currentTimeMillis());

        notificationMessage.setStoreOnDevice(Boolean.TRUE);
        notificationMessage.setSendToDevice(Boolean.TRUE);
        notificationMessage.setContentType(Message.ContentType.VIDEO_CALL_NOTIFICATION_MSG.getValue());
        notificationMessage.setDeviceKeyString(userPreferences.getDeviceKeyString());
        notificationMessage.setMessage(videoCallId);
        return notificationMessage;
    }



    public void handleCustomNotificationMessages(final Message message) {
        handleIncomingNotification(message);
    }


    private void handleIncomingNotification(Message msg) {

        Map<String,String>valueMap = msg.getMetadata();

        Class activityToOpen = null;
        try {
            activityToOpen = Class.forName(NOTIFICATION_ACTIVITY_NAME);
        } catch (Exception e) {

        }
        Intent intent1 = new Intent(context, activityToOpen);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent1.putExtra("CONTACT_ID", msg.getTo());
        intent1.putExtra(RING, valueMap.get(RING));
        intent1.putExtra("MESSAGE", msg.getMessage());

        context.startActivity(intent1);

        return;
    }

}
