package com.bjerkan.rubikscubeapp.cubescanning;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bjerkan.rubikscubeapp.R;

public class DisplayResultFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_result, container, false);

        Bundle arguments = getArguments();
        Bitmap resultImage = (Bitmap) arguments.getParcelable(IMAGE_ARGUMENT);

        ImageView resultImageView = view.findViewById(R.id.resultImage);
        resultImageView.setImageBitmap(resultImage);

        Button nextStep = view.findViewById(R.id.btnNextStep);
        nextStep.setOnClickListener((View onClickView) -> {
            mNextStepCallback.nextStep();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mNextStepCallback = (NextStepRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement NextStepRequestListener");
        }
    }

    public interface NextStepRequestListener {
        void nextStep();
    }

    public static final String IMAGE_ARGUMENT = "com.bjerkan.rubikscubeapp.IMAGE_ARGUMENT";

    private NextStepRequestListener mNextStepCallback;
}
