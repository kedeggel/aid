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
    private static final String ALL_DOCS = "aid/_design/count/_view/count";
    private static final String FIND = "aid/_find";
    private static final String TOKEN = "register";

    public static void getAllDocs(AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(ALL_DOCS, null, responseHandler);
    }

    public static void getImage(String doc, String path, AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get("aid/" + doc + "/" + path, null, responseHandler);
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
        HttpUtils.postJSON(context, FIND, json, responseHandler);
    }

    public static void sendDeviceToken(Context context, String token, AsyncHttpResponseHandler responseHandler) {
        JSONObject json = new JSONObject();
        try {
            json.put("device_token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.postJSON(context, TOKEN, json, responseHandler);
    }
}
