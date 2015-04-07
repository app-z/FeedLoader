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


//http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html

/**
 *
 *
 *
 * @param <D>
 */
class FeedLoaderWrapper<D> extends Loader<Response<D>> {

    private boolean DEBUG = true;
    private String TAG = getClass().getSimpleName();

    private DispatcherData dispatcherData;

    private RequestQueue requestQueue;

    private Response<D> mCachedResponse;

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
        final String urlFeed = dispatcherData.getUrlFeed(getId());
        final GsonRequest gsonRequest = new GsonRequest(urlFeed,
                clazz,
                null,
                new Response.Listener<D>() {
                    @Override
                    public void onResponse(D data) {
                        mCachedResponse = Response.success(data, null);
                        deliverResult(mCachedResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mCachedResponse = Response.error(volleyError);
                deliverResult(mCachedResponse);
            }
        });
        requestQueue.add(gsonRequest);
    }


    @Override
    public void deliverResult(Response<D> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            //releaseResources(data);
            if (DEBUG) Log.i(TAG, "Loader deliverResult() isReset()");
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Response<D> oldData = mCachedResponse;
        mCachedResponse = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
            if (DEBUG) Log.i(TAG, "Loader deliverResult() isStarted()");
        }

        if (DEBUG) Log.i(TAG, "Loader deliverResult()");
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
        onStopLoading();
        requestQueue.cancelAll(this);
        super.onReset();
    }

    @Override
    public void onForceLoad() {
        super.onForceLoad();

        String urlFeed = dispatcherData.getUrlFeed(getId());
        if (DEBUG) Log.d(TAG, "Loader onForceLoad() : feedUrl = " + urlFeed);
        doRequest(dispatcherData.loaderClazzMap.get(getId()));
    }
}
