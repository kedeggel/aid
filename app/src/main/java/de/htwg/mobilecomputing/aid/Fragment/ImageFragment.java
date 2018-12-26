package de.htwg.mobilecomputing.aid.Fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.text.SimpleDateFormat;

import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.R;

public class ImageFragment extends Fragment {
    private static final String EXTRA_TRANSITION_NAME= "transition_name";
    private static final String EXTRA_IMAGE = "image_item";

    public static ImageFragment newInstance(LibraryElement element) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_IMAGE, element);
        args.putString(EXTRA_TRANSITION_NAME, element.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ensures smoother transitions
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(
                    TransitionInflater.from(getContext())
                            .inflateTransition(android.R.transition.move)
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        //Enter fullsceen mode in landscape orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.setBackgroundColor(0xFF000000); //Set background color to black
            //Hide Status Bar, Action Bar and Navigation Bar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LibraryElement element = getArguments().getParcelable(EXTRA_IMAGE);
        final PhotoView imageView = view.findViewById(R.id.detail_image);
        final TextView labelView = view.findViewById(R.id.detail_label);
        final TextView dateView = view.findViewById(R.id.detail_date);

        imageView.setImageBitmap(element.getImage());
        //labelView.setText(element.getLabel());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //dateView.setText(getString(R.string.date_taken) + ": " + dateFormat.format(element.getDate()));

        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(transitionName);
        }
    }
}
