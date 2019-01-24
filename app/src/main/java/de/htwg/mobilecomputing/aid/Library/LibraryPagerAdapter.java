package de.htwg.mobilecomputing.aid.Library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import de.htwg.mobilecomputing.aid.Fragment.ImageFragment;

public class LibraryPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<LibraryElement> elements;

    public LibraryPagerAdapter(FragmentManager fm, ArrayList<LibraryElement> elements) {
        super(fm);
        this.elements = elements;
    }

    @Override
    public Fragment getItem(int position) {
        LibraryElement element = elements.get(position);
        return ImageFragment.newInstance(element);
    }

    @Override
    public int getCount() {
        return elements.size();
    }
}
