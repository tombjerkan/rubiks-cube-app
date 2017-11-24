package com.bjerkan.rubikscubeapp.cubescanning;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bjerkan.rubikscubeapp.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * A fragment for capturing an image using the phone's camera. A captured image is given to the
 * parent activity through the OnImageCapturedListener, which must be implemented by the parent.
 *
 * Public life-cycle methods (onCreateView, onAttach, onPause, onResume and onDestroy) and the
 * public camera methods (onCameraViewStarted, onCameraViewStopped, onCameraFrame) are for use
 * by the Android system and should not be called directly.
 */
public class CaptureImageFragment extends Fragment implements CvCameraViewListener2 {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_image, container, false);

        cameraView = view.findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);

        Button captureImage = view.findViewById(R.id.btnCaptureImage);
        captureImage.setOnClickListener((View onClickView) -> {
            if (lastFrame != null) {
                imageCapturedCallback.onImageCaptured(lastFrame.rgba());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            imageCapturedCallback = (OnImageCapturedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnImageCapturedListener");
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getActivity(),
                    loaderCallback);
        } else {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        updateTitleText();
    }

    public void onDestroy() {
        super.onDestroy();

        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        lastFrame = inputFrame;
        return inputFrame.rgba();
    }

    /**
     * Sets the text to display as the title of the image capturing screen. This is intended to be
     * the name of the side of the cube beings scanned.
     * @param title the text to display as the title
     */
    public void setTitle(String title) {
        this.title = title;
        updateTitleText();
    }

    /**
     * Interface to be implemented by parent activity so it can listen for an image capture event.
     */
    public interface OnImageCapturedListener {
        /**
         * Called to inform the listener that an image has been captured by this fragment.
         *
         * @param image the image that was captured
         */
        void onImageCaptured(Mat image);
    }

    private void updateTitleText() {
        if (getView() != null && title != null) {
            ((TextView) getView().findViewById(R.id.txtTitle)).setText(title);
        }
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                cameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    private CameraBridgeViewBase cameraView;

    private OnImageCapturedListener imageCapturedCallback;

    private CameraBridgeViewBase.CvCameraViewFrame lastFrame = null;

    private String title;
}
