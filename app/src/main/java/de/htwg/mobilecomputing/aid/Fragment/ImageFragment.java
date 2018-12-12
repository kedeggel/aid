package de.htwg.mobilecomputing.aid.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.R;

public class ImageFragment extends Fragment {
    private static final String EXTRA_TRANSITION_NAME= "transition_name";
    private static final String EXTRA_IMAGE = "image_item";

    public static ImageFragment newInstance(LibraryElement element) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_IMAGE, element);
        args.putString(EXTRA_TRANSITION_NAME, element.getLabel());
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
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LibraryElement element = getArguments().getParcelable(EXTRA_IMAGE);
        final PhotoView imageView = view.findViewById(R.id.detail_image);

        imageView.setImageResource(R.drawable.sample_image2); //todo: Set actual image from library element

        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(transitionName);
        }
    }
}
