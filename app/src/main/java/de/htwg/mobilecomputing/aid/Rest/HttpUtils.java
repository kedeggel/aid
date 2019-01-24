package de.htwg.mobilecomputing.aid.Rest;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtils {
    private static String baseUrl = null;
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void setIp(String ip) {
        baseUrl = String.format("http://%s:5984/", ip);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("RestGet", getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postJSON(Context context, String url, JSONObject json, AsyncHttpResponseHandler responseHandler) {
        Log.d("RestGet", getAbsoluteUrl(url));
        Log.d("RestGet", json.toString());
        StringEntity entity = null;
        try {
            entity = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }
}