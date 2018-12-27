package de.htwg.mobilecomputing.aid.Fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.htwg.mobilecomputing.aid.Library.ItemOffsetDecorationList;
import de.htwg.mobilecomputing.aid.Library.LibraryAdapter;
import de.htwg.mobilecomputing.aid.Library.LibraryElement;
import de.htwg.mobilecomputing.aid.Library.LibraryItemClickListener;
import de.htwg.mobilecomputing.aid.R;
import de.htwg.mobilecomputing.aid.Rest.HttpUtils;
import de.htwg.mobilecomputing.aid.Rest.RestCalls;

public class LibraryFragment extends Fragment implements LibraryItemClickListener {
    public static final String TAG = LibraryFragment.class.getSimpleName();

    private SwipeRefreshLayout swipe;
    private LibraryAdapter adapter;
    private ArrayList<LibraryElement> elements;
    private ProgressBar progressBar;
    private TextView annotation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_library));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        progressBar = view.findViewById(R.id.progress_bar);
        annotation = view.findViewById(R.id.annotation);
        annotation.setVisibility(View.GONE);

        elements = new ArrayList<>();
        HttpUtils.setIp(sp.getString("ipKey", null));

        swipe = view.findViewById(R.id.swipeRefreshLayout);
        swipe.setOnRefreshListener(onRefreshListener);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            progressBar.setVisibility(View.VISIBLE);
            RestCalls.getAllDocs(allDocsResponeHandler);
            swipe.setRefreshing(false);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RestCalls.getAllDocs(allDocsResponeHandler);

        //Inflate library
        RecyclerView library = view.findViewById(R.id.LibraryRecycler);
        adapter = new LibraryAdapter(elements, (LibraryItemClickListener) this);
        library.setAdapter(adapter);

        //Create grid layout //todo: maybe add option to switch between views
        /*int spanCount = 4; //Number of columns in Portrait
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            spanCount = 5; //Number of columns in Landscape
        library.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));

        //Add margin between library items
        ItemOffsetDecorationGrid itemDecoration = new ItemOffsetDecorationGrid(12, spanCount);
        library.addItemDecoration(itemDecoration);*/

        //Create list layout
        library.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemOffsetDecorationList itemDecoration = new ItemOffsetDecorationList(20);
        library.addItemDecoration(itemDecoration);
    }

    //Response Handler for getting all Events
    private AsyncHttpResponseHandler allDocsResponeHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
            JsonArray jsonArray = jsonObject.get("rows").getAsJsonArray();
            elements.clear();
            for(int i=0; i<jsonArray.size(); i++)
                RestCalls.getDoc(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), docResponseHandler);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
            //elements = new ArrayList<>();
            progressBar.setVisibility(View.GONE);
            annotation.setVisibility(View.VISIBLE);
        }
    };

    //Response Handler for getting one Event
    private AsyncHttpResponseHandler docResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new Gson();
            LibraryElement element = gson.fromJson(new String(responseBody), LibraryElement.class);

            JsonObject jsonObject = gson.fromJson(new String(responseBody), JsonObject.class);
            String imageName = Objects.requireNonNull(jsonObject.get("_attachments").getAsJsonObject().keySet().toArray())[0].toString();
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
            elements.add(element); //todo: add in chronological order
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(), getString(R.string.error_doc_not_found), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onLibraryItemClickListener(int position, LibraryElement element, ImageView imageView) {
        LibraryViewPagerFragment libraryViewPagerFragment = LibraryViewPagerFragment.newInstance(position, elements);

        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(imageView, Objects.requireNonNull(ViewCompat.getTransitionName(imageView)))
                .addToBackStack(TAG)
                .replace(R.id.fragment, libraryViewPagerFragment)
                .commit();
    }
}
