package de.htwg.mobilecomputing.aid.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import de.htwg.mobilecomputing.aid.Library.ItemOffsetDecoration;
import de.htwg.mobilecomputing.aid.Library.LibraryAdapter;
import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.Library.LibraryItemClickListener;
import de.htwg.mobilecomputing.aid.R;

//todo: Custom tool bar: https://blog.iamsuleiman.com/android-material-design-tutorial/

public class LibraryFragment extends Fragment implements LibraryItemClickListener {
    public static final String TAG = LibraryFragment.class.getSimpleName();
    //private OnFragmentInteractionListener mListener;

    ArrayList<LibraryElement> elements;

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create Library
        RecyclerView library = view.findViewById(R.id.LibraryRecycler);
        elements = LibraryElement.generateElements(20); //todo: Populate library
        LibraryAdapter adapter = new LibraryAdapter(elements, (LibraryItemClickListener) this);
        library.setAdapter(adapter);
        int spanCount = 4; //Number of columns in Portrait
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            spanCount = 5; //Number of columns in Landscape
        library.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));

        //Add margin between library items
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(12, spanCount);
        library.addItemDecoration(itemDecoration);
    }

    @Override
    public void onLibrarytemClickListener(int position, LibraryElement element, ImageView imageView) {
        LibraryViewPagerFragment libraryViewPagerFragment = LibraryViewPagerFragment.newInstance(position, elements);

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(TAG)
                .replace(R.id.fragment, libraryViewPagerFragment)
                .commit();
    }

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
