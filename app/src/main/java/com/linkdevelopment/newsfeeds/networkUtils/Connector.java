package com.linkdevelopment.newsfeeds.networkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.linkdevelopment.newsfeeds.models.NewModel;
import com.linkdevelopment.newsfeeds.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Connector {

    private Context mContext;
    private LoadCallback mLoadCallback;
    private ErrorCallback mErrorCallback;
    private RequestQueue mQueue;


    public interface LoadCallback {

        void onComplete(String tag, String response);

    }

    public interface ErrorCallback {

        void onError(VolleyError error);

    }

    public Connector(Context mContext, LoadCallback mLoadCallback, ErrorCallback mErrorCallback) {
        this.mContext = mContext;
        this.mLoadCallback = mLoadCallback;
        this.mErrorCallback = mErrorCallback;
    }


    public void getRequest(final String tag, String url) {
        if (isOnline(mContext)) {
            mQueue = Volley.newRequestQueue(mContext);
            StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Helper.writeToLog(response);
                            mLoadCallback.onComplete(tag, response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    mErrorCallback.onError(error);
                }
            });
            mStringRequest.setTag(tag);
            mQueue.add(mStringRequest);
        } else {
            mErrorCallback.onError(new NoConnectionError());
        }


    }

    public void cancelAllRequests(final String tag) {
        if (mQueue != null) {
            mQueue.cancelAll(tag);
        }
    }

    public static String createUrl() {
        Uri.Builder builder = Uri.parse(Constants.NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(Constants.SOURCE_KEY,"the-next-web")
                .appendQueryParameter(Constants.API_KEY,Constants.API_KEY_VALUE);

        return builder.toString();

    }

    public static ArrayList<NewModel> getNews(String response) {
        ArrayList<NewModel> newModels = new ArrayList<>();
        if (isJSONValid(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray news = jsonObject.optJSONArray("articles");
                for (int i = 0; i < news.length(); i++) {
                    JSONObject mNew = news.getJSONObject(i);
                    String author = mNew.optString("author");
                    String image = mNew.optString("urlToImage");
                    String url = mNew.optString("url");
                    String publishedAt = mNew.optString("publishedAt");
                    String description = mNew.optString("description");
                    String title = mNew.optString("title");
                    newModels.add(new NewModel(author,title,description,url,image,publishedAt));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return newModels;
    }



    private static boolean isJSONValid(String test) {
        if (test == null)
            return false;

        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }




    private static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}
