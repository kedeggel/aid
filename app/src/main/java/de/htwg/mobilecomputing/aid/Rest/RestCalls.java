package de.htwg.mobilecomputing.aid.Rest;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.htwg.mobilecomputing.aid.Library.LibraryElement;

public class RestCalls {
    private static final String ALL_DOCS = "_all_docs";
    private static final String FIND = "_find";

    public static void getAllDocs(AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(ALL_DOCS, null, responseHandler);
    }

    public static void getDoc(String id, AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(id, null, responseHandler);
    }

    public static void getImage(String doc, String path, AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(doc + "/" + path, null, responseHandler);
    }

    public static void getSelector(Context context, int limit, AsyncHttpResponseHandler responseHandler) {
        JSONObject json = new JSONObject();
        try {
            JSONObject id = new JSONObject();
            id.put("$gt", JSONObject.NULL);

            JSONObject selector = new JSONObject();
            selector.put("_id", id);

            JSONObject timestamp = new JSONObject();
            timestamp.put("timestamp", "desc");

            JSONArray sort = new JSONArray();
            sort.put(timestamp);

            json.put("selector", selector);
            json.put("sort", sort);

            if(limit > 0)
                json.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("RestGet", json.toString());

        HttpUtils.postJSON(context, FIND, json, responseHandler);
    }
}
