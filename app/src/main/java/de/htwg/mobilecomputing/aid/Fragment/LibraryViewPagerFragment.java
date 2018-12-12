package de.htwg.mobilecomputing.aid.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.Library.LibraryPagerAdapter;
import de.htwg.mobilecomputing.aid.R;

public class LibraryViewPagerFragment extends Fragment {
    private static final String EXTRA_INITIAL_POS = "initial_pos";
    private static final String EXTRA_IMAGES = "images";

    public static LibraryViewPagerFragment newInstance(int current, ArrayList<LibraryElement> elements) {
        LibraryViewPagerFragment fragment = new LibraryViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INITIAL_POS, current);
        args.putParcelableArrayList(EXTRA_IMAGES, elements);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = getArguments().getInt(EXTRA_INITIAL_POS);
        ArrayList<LibraryElement> elements = getArguments().getParcelableArrayList(EXTRA_IMAGES);

        LibraryPagerAdapter libraryPagerAdapter = new LibraryPagerAdapter(getChildFragmentManager(), elements);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.library_view_pager);
        viewPager.setAdapter(libraryPagerAdapter);
        viewPager.setCurrentItem(currentItem);
    }
}
