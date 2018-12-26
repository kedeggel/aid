package de.htwg.mobilecomputing.aid.Fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.Library.ItemOffsetDecoration;
import de.htwg.mobilecomputing.aid.Library.LibraryAdapter;
import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.Library.LibraryItemClickListener;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;

public class LibraryFragment extends Fragment implements LibraryItemClickListener {
    public static final String TAG = LibraryFragment.class.getSimpleName();
    //private OnFragmentInteractionListener mListener;

    private SharedPreferences sp;
    private LibraryAdapter adapter;
    private ArrayList<LibraryElement> elements;
    private ProgressBar progressBar;
    private TextView annotation;

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.library));
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        progressBar = view.findViewById(R.id.progress_bar);
        annotation = view.findViewById(R.id.annotation);
        annotation.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elements = new ArrayList<>();
        HttpUtils.setIp(sp.getString("ipKey", null));
        RestCalls.getAllDocs(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
                //int count = jsonObject.get("total_rows").getAsInt(); //todo: display number of hits
                JsonArray jsonArray = jsonObject.get("rows").getAsJsonArray();
                for(int i=0; i<jsonArray.size(); i++) {
                    Log.d("asdf", jsonArray.get(i).toString());
                    RestCalls.getDoc(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), docResponseHandler);
                }
                inflateLibrary(view);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
                //elements = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                annotation.setVisibility(View.VISIBLE);
            }
        });
    }

    private AsyncHttpResponseHandler docResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new Gson();
            LibraryElement element = gson.fromJson(new String(responseBody), LibraryElement.class);

            JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
            String imageName = jsonObject.get("_attachments").getAsJsonObject().keySet().toArray()[0].toString();
            RestCalls.getImage(element.getId(), imageName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                    element.setImage(bmp);
                    adapter.notifyItemChanged(elements.indexOf(element));
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(), getString(R.string.error_img_not_found), Toast.LENGTH_LONG).show();
                }
            });
            element.setImgUrl(HttpUtils.baseUrl + element.getId() + "/" + imageName); //todo: Maybe get image via url

            elements.add(element);
            //adapter.notifyItemChanged(elements.size()-1);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), getString(R.string.error_doc_not_found), Toast.LENGTH_LONG).show();
        }
    };

    private void inflateLibrary(View view) {
        //Inflate library
        RecyclerView library = view.findViewById(R.id.LibraryRecycler);
        adapter = new LibraryAdapter(elements, (LibraryItemClickListener) this);
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
                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
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
