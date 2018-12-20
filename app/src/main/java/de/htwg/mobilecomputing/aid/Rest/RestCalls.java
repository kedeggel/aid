package de.htwg.mobilecomputing.aid.Rest;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class RestCalls {
    private static final String ALL_DOCS = "_all_docs";

    public static void getAllDocs(AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(ALL_DOCS, null, responseHandler);
    }
}
