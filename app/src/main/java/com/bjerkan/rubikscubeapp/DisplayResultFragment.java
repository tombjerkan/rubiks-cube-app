package com.bjerkan.rubikscubeapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DisplayResultFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_result, container, false);

        Bundle arguments = getArguments();
        Bitmap resultImage = (Bitmap) arguments.getParcelable(IMAGE_ARGUMENT);

        ImageView resultImageView = view.findViewById(R.id.resultImage);
        resultImageView.setImageBitmap(resultImage);

        return view;
    }

    public static final String IMAGE_ARGUMENT = "com.bjerkan.rubikscubeapp.IMAGE_ARGUMENT";
}
