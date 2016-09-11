package com.applozic.mobicomkit.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComMessageService;

/**
 * Created by devashish on 10/09/16.
 */
public class SendCommandActivity  extends Activity {

    EditText editText;
    MobiComMessageService mobiComMessageService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Command might be from Drop Down OR Manual.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_command_activity);
        Button sendButton =(Button) findViewById(R.id.send_command_btn);
        editText =(EditText) findViewById(R.id.send_command_editText);
        mobiComMessageService =  new MobiComMessageService(this, MessageIntentService.class);
        sendButton.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendMessageWithCommand();
            }
        });

    }

    private void sendMessageWithCommand() {

        if( TextUtils.isEmpty(editText.getText()) ) {
            Toast.makeText(this, "We caught you!!! Command is empty ", Toast.LENGTH_SHORT);
            return;
        }
         //Send message to applozic.
        Message message = new Message();
        message.setMessage(editText.getText().toString());
        message.setTo("hodor");
        mobiComMessageService.sendCustomMessage(message);

    }

}
