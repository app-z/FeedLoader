package net.appz.feedloader;

/**
 * Created by App-z.net on 05.04.15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 *
 *
 *
 * @param <D>
 */
class FeedLoaderWrapper<D> extends Loader<D> {

    private boolean DEBUG = true;
    private String TAG = getClass().getSimpleName();

    private DispatcherData dispatcherData;

    private RequestQueue requestQueue;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public FeedLoaderWrapper(Context context, Bundle bundle) {
        super(context);
        dispatcherData = bundle.getParcelable("dispatcherData");
        requestQueue = Volley.newRequestQueue(context);
        // run only once
        onContentChanged();
    }

    /**
     * Get Data
     */
    private void doRequest(Class<?> clazz) {
        String urlFeed = dispatcherData.urlFeeds.get(getId());
        final GsonRequest gsonRequest = new GsonRequest(urlFeed,
                clazz,
                null,
                new Response.Listener<D>() {
                    @Override
                    public void onResponse(D data) {
                        deliverResult(data);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null)
                    //deliverResult(null);
                    if (DEBUG) Log.e(TAG, "volleyError: " + volleyError.getMessage());
                dispatcherData.callBackMap.get(getId()).onErrorResponse(volleyError);
            }
        });
        requestQueue.add(gsonRequest);
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        if (DEBUG) Log.i(TAG, "Loader onStopLoading()");
        requestQueue.cancelAll(this);
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        if (DEBUG) Log.i(TAG, "Loader onReset()");
        requestQueue.cancelAll(this);
        super.onReset();
    }

    @Override
    public void onForceLoad() {
        super.onForceLoad();

        String urlFeed = dispatcherData.urlFeeds.get(getId());
        if (DEBUG) Log.d(TAG, "Loader onForceLoad() : feedUrl = " + urlFeed);
        doRequest(dispatcherData.loaderClazzMap.get(getId()));
    }
}
