package com.applozic.mobicomkit.uiwidgets.conversation.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.people.fragment.ProfileFragment;
import com.applozic.mobicomkit.uiwidgets.uilistener.MobicomkitUriListener;

/**
 * Created by sunil on 25/5/16.
 */
public class PictureUploadPopUpFragment extends DialogFragment {

    private static final String TAG = "PictureUploadPopUpFrag" ;
    LinearLayout removeLayout, galleryLayout, cameraLayout;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attach_photo_popup_window_layout, container, false);
        getDialog().setCancelable(Boolean.TRUE);
        cameraLayout = (LinearLayout) view.findViewById(R.id.upload_camera_layout);
        removeLayout = (LinearLayout) view.findViewById(R.id.upload_remove_image_layout);
        removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(ProfileFragment.REQUEST_REMOVE_PHOTO,Activity.RESULT_OK,getActivity().getIntent());
                getDialog().dismiss();
            }
        });
        galleryLayout = (LinearLayout) view.findViewById(R.id.upload_gallery_layout);
        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();

                try {
                    Intent getContentIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(getContentIntent, ProfileFragment.REQUEST_CODE_ATTACH_PHOTO);
                } catch (Exception e) {

                }

            }
        });

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                try {
                    imageCapture();
                } catch (Exception e) {

                }

            }
        });


        return view;

    }

    public void imageCapture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if( !(getActivity() instanceof  MobicomkitUriListener) ){
           Log.d(TAG , "Activity must implement MobicomkitUriListener to get image file uri");
            return;
        }

        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                Uri capturedImageUri = ((MobicomkitUriListener) getActivity()).getCurrentImageUri();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
                getActivity().startActivityForResult(cameraIntent, MultimediaOptionFragment.REQUEST_CODE_TAKE_PHOTO);
        }
    }
}
