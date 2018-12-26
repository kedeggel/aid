package de.htwg.mobilecomputing.aid.Rest;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtils {
    public static String baseUrl = null;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void setIp(String ip) {
        baseUrl = "http://" + ip + ":5984/aid/";
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("asdf", getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }
}