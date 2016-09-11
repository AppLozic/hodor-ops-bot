package com.applozic.mobicomkit.uiwidgets.conversation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicommons.commons.core.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 3/2/16.
 */


public class ChannelCreateActivity extends AppCompatActivity {

    private EditText channelName;
    private CircleImageView circleImageView;
    private View focus;
    private ActionBar mActionBar;
    public static Activity channelActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_create_activty_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        channelActivity = this;
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(R.string.channel_create_title);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        channelName = (EditText) findViewById(R.id.channelName);
        circleImageView = (CircleImageView) findViewById(R.id.channelIcon);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_create_menu, menu);
        menu.removeItem(R.id.Done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.Next) {
            boolean check = true;
            if (channelName.getText().toString().trim().length() == 0 || TextUtils.isEmpty(channelName.getText().toString())) {
                focus = channelName;
                focus.requestFocus();
                check = false;
            }
            if (check) {
                Utils.toggleSoftKeyBoard(ChannelCreateActivity.this, true);
                Intent intent = new Intent(ChannelCreateActivity.this, ContactSelectionActivity.class);
                intent.putExtra(ContactSelectionActivity.CHANNEL, channelName.getText().toString());
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
