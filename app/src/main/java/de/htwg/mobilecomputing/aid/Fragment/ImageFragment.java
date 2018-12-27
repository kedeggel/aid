package de.htwg.mobilecomputing.aid.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import de.htwg.mobilecomputing.aid.Activity.SettingsActivity;
import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.R;

public class ImageFragment extends Fragment {
    private static final String EXTRA_TRANSITION_NAME= "transition_name";
    private static final String EXTRA_ELEMENT = "element_item";

    public static ImageFragment newInstance(LibraryElement element) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ELEMENT, element);
        args.putString(EXTRA_TRANSITION_NAME, element.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Ensures smoother transitions
        postponeEnterTransition();
        setSharedElementEnterTransition(
                TransitionInflater.from(getContext())
                        .inflateTransition(android.R.transition.move)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.toolbar_image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
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

        final LibraryElement element = getArguments().getParcelable(EXTRA_ELEMENT);
        final PhotoView image = view.findViewById(R.id.detail_image);
        final TextView line1 = view.findViewById(R.id.detail_line1);
        final TextView line2 = view.findViewById(R.id.detail_line2);
        final TextView line3 = view.findViewById(R.id.detail_line3);

        image.setImageBitmap(element.getImage());
        line1.setText(getString(R.string.element_date_taken) + ": " + element.getFormattedTimestamp());
        line2.setText(getString(R.string.element_sensor) + ": "+ element.getSensor());
        line3.setText(getString(R.string.element_location) + ": " + element.getLocation());

        image.setTransitionName(getArguments().getString(EXTRA_TRANSITION_NAME));
    }
}
