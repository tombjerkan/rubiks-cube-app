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

        Button nextStep = view.findViewById(R.id.btnNextStep);
        nextStep.setOnClickListener((View onClickView) -> nextStepCallback.nextStep());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            nextStepCallback = (NextStepRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement NextStepRequestListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateImage();
    }

    public interface NextStepRequestListener {
        void nextStep();
    }

    public void setResultImage(Bitmap resultImage) {
        this.resultImage = resultImage;
        updateImage();
    }

    private void updateImage() {
        if (getView() != null && resultImage != null) {
            ImageView resultImageView = getView().findViewById(R.id.resultImage);
            resultImageView.setImageBitmap(resultImage);
        }
    }

    private NextStepRequestListener nextStepCallback;

    private Bitmap resultImage;
}
