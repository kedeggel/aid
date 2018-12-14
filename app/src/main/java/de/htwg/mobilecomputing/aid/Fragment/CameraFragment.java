package de.htwg.mobilecomputing.aid.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import de.htwg.mobilecomputing.aid.R;

public class CameraFragment extends Fragment {
    private WebView cameraView;
    private Button startCamera;
    private Button takePicture;

    private boolean cameraOn = false;

    //private OnFragmentInteractionListener mListener;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraView = view.findViewById(R.id.camera_view);
        startCamera = view.findViewById(R.id.button_start_camera);
        startCamera.setOnClickListener(startCameraOnClickListener);
        takePicture = view.findViewById(R.id.button_take_picture);
        takePicture.setOnClickListener(takePictureOnClickListener);

        return view;
    }

    private final Button.OnClickListener startCameraOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(cameraOn) {
                takePicture.setEnabled(false);
                startCamera.setText(getString(R.string.start_camera));
                cameraView.loadUrl("about:blank");
            } else {
                takePicture.setEnabled(true);
                startCamera.setText(getString(R.string.stop_camera));
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                cameraView.loadUrl(sp.getString("ipKey", "127.0.0.1") + ":" + sp.getString("portKey", "22"));
                //todo: Show spinner while loading: https://stackoverflow.com/questions/11241513/android-progessbar-while-loading-webview
            }
            cameraOn = !cameraOn;
        }
    };

    private final Button.OnClickListener takePictureOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            //todo: take still image with camera and save in library
        }
    };

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/
}
