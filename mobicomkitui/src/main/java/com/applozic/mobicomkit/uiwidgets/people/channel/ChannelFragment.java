package com.applozic.mobicomkit.uiwidgets.people.channel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.OnContactsInteractionListener;
import com.applozic.mobicommons.people.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 28/1/16.
 */
public class ChannelFragment extends ListFragment implements
        AdapterView.OnItemClickListener {


    public static final String PACKAGE_TO_EXCLUDE_FOR_INVITE = "net.mobitexter";
    private static final String SHARE_TEXT = "share_text";
    private static String inviteMessage;
    private ChannelAdapter mAdapter; // The main query adapter
    // Contact selected listener that allows the activity holding this fragment to be notified of
// a contact being selected
    private OnContactsInteractionListener mOnChannelSelectedListener;

    private Button shareButton;
    private TextView resultTextView;

    private List<Channel> channelList;
    private boolean syncStatus = true;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelList = ChannelService.getInstance(getActivity()).getChannelList();
        inviteMessage = Utils.getMetaDataValue(getActivity().getApplicationContext(), SHARE_TEXT);
        mAdapter = new ChannelAdapter(getActivity().getApplicationContext());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mOnChannelSelectedListener.onGroupSelected(channelList.get(position));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the list fragment layout
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        shareButton = (Button) view.findViewById(R.id.actionButton);
        shareButton.setVisibility(View.GONE);
        resultTextView = (TextView) view.findViewById(R.id.result);
        resultTextView.setText("No Groups");
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnChannelSelectedListener = (OnContactsInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactsInteractionListener");
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND)
                        .setType("text/plain").putExtra(Intent.EXTRA_TEXT, inviteMessage);

                List<Intent> targetedShareIntents = new ArrayList<Intent>();

                List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                if (!resInfo.isEmpty()) {
                    for (ResolveInfo resolveInfo : resInfo) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        if (packageName.equals(PACKAGE_TO_EXCLUDE_FOR_INVITE)) {
                            continue;
                        }
                        Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
                        targetedShareIntent.setType("text/plain")
                                .setAction(Intent.ACTION_SEND)
                                .putExtra(Intent.EXTRA_TEXT, inviteMessage)
                                .setPackage(packageName);
                        targetedShareIntents.add(targetedShareIntent);
                    }
                    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Share Via");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                    startActivity(chooserIntent);
                }
            }
        });

        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

    }


    private class ChannelAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater; // Stores the layout inflater
        private AlphabetIndexer mAlphabetIndexer; // Stores the AlphabetIndexer instance
        private TextAppearanceSpan highlightTextSpan; // Stores the highlight text appearance style

        /**
         * Instantiates a new Contacts Adapter.
         *
         * @param context A context that has access to the app's layout.
         */
        public ChannelAdapter(Context context) {
            this.context = context;
            // Stores inflater for use later
            mInflater = LayoutInflater.from(context);

            final String alphabet = context.getString(R.string.alphabet);

            highlightTextSpan = new TextAppearanceSpan(getActivity(), R.style.searchTextHiglight);
        }


        /**
         * Overrides newView() to inflate the list item views.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Inflates the list item layout.
            final View itemLayout =
                    mInflater.inflate(R.layout.contact_list_item, parent, false);

            Channel channel = channelList.get(position);
            TextView text1 = (TextView) itemLayout.findViewById(R.id.text1);
            TextView text2 = (TextView) itemLayout.findViewById(R.id.text2);
            CircleImageView icon = (CircleImageView) itemLayout.findViewById(R.id.contactImage);
            text1.setText(channel.getName());
            icon.setImageResource(R.drawable.applozic_group_icon);
            return itemLayout;
        }


        /**
         * An override of getCount that simplifies accessing the Cursor. If the Cursor is null,
         * getCount returns zero. As a result, no test for Cursor == null is needed.
         */
        @Override
        public int getCount() {
            return channelList.size();
        }

        @Override
        public Object getItem(int position) {
            return channelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

}
