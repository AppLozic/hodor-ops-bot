package com.applozic.mobicomkit.uiwidgets.conversation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.R;

/**
 * Created by sunil on 10/3/16.
 */
public class ChannelNameActivity extends AppCompatActivity {
    private EditText channelName;
    private Button ok, cancel;
    public static final String CHANNEL_NAME = "CHANNEL_NAME";
    String oldChannelName;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_channel_name_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.update_channel_title_name));
        if (getIntent().getExtras() != null) {
            oldChannelName = getIntent().getExtras().getString(CHANNEL_NAME);
        }
        channelName = (EditText) findViewById(R.id.newChannelName);
        ok = (Button) findViewById(R.id.channelNameOk);
        cancel = (Button) findViewById(R.id.channelNameCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldChannelName.equals(channelName.getText().toString())) {
                    ChannelNameActivity.this.finish();
                }
                if (TextUtils.isEmpty(channelName.getText().toString()) || channelName.getText().toString().trim().length() == 0) {
                    Toast.makeText(ChannelNameActivity.this, getString(R.string.channel_name_empty), Toast.LENGTH_SHORT).show();
                    ChannelNameActivity.this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(ChannelInfoActivity.CHANNEL_NAME, channelName.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelNameActivity.this.finish();

            }
        });
    }

}
