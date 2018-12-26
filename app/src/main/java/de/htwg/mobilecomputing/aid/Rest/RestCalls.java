package de.htwg.mobilecomputing.aid.Rest;

import com.loopj.android.http.AsyncHttpResponseHandler;

import de.htwg.mobilecomputing.aid.Library.LibraryElement;

public class RestCalls {
    private static final String ALL_DOCS = "_all_docs";

    public static void getAllDocs(AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(ALL_DOCS, null, responseHandler);
    }

    public static void getDoc(String id, AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(id, null, responseHandler);
    }

    public static void getImage(String doc, String path, AsyncHttpResponseHandler responseHandler) {
        HttpUtils.get(doc + "/" + path, null, responseHandler);
    }
}
