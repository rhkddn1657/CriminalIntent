package com.bignerdranch.android.criminallntent;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;

/**
 * Created by rhkdd on 2017-01-21.
 */

public class PictureZoomInFragment extends DialogFragment {
    private static File mPhotoFile;
    private static ImageView mImageView;

    public static void setImageView(File photoFile, ImageView imageView) {
        mPhotoFile = photoFile;
        mImageView = imageView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Glide.with(this).load(mPhotoFile.getPath())
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(mImageView);
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.dialog_image, container, false);
//        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_image_picker);
//        Glide.with(this).load(mPhotoFile.getPath())
//                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                .into(imageView);
//
//        return view;
//    }
}
