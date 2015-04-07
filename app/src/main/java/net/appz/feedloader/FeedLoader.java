package net.appz.feedloader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.android.volley.Response;

/**
 * Created by App-z.net on 04.04.15.
 */
public class FeedLoader {

    private static volatile FeedLoader singleton = null;

    private Context context = null;
    private boolean DEBUG = true;
    private String TAG = getClass().getSimpleName();

    private DispatcherData dispatcherData = new DispatcherData();

    public FeedLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    LoaderManager.LoaderCallbacks<Response<Object>> callback = new LoaderManager.LoaderCallbacks<Response<Object>>() {
        @Override
        public Loader<Response<Object>> onCreateLoader(int id, Bundle args) {
            return new FeedLoaderWrapper(context , args);
        }

        @Override
        public void onLoadFinished(Loader<Response<Object>> loader, Response<Object> data) {
            if( data.isSuccess() )
                dispatcherData.callBackMap.get(loader.getId()).onResponse(loader.getId(), data.result);
            else
                dispatcherData.callBackMap.get(loader.getId()).onErrorResponse(data.error);
        }

        @Override
        public void onLoaderReset(Loader<Response<Object>> loader) {

        }
    };

    public FeedLoader addLoader(int loaderId,
                                String url,
                                Class<?> loaderClazz,
                                DispatcherData.Listener callback){
        dispatcherData.loaderClazzMap.put(loaderId, loaderClazz);
        dispatcherData.callBackMap.put(loaderId, callback);
        dispatcherData.putUrlFeed(loaderId, url);
        return singleton;
    }

    public void start(int loaderId, final FragmentActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dispatcherData", dispatcherData);
        assert activity instanceof FragmentActivity : "Run possible only from FragmentActivity";
        activity.getSupportLoaderManager().
                restartLoader(loaderId, bundle, callback);
    }

    public void stop(int loaderId, final FragmentActivity activity) {
        assert activity instanceof FragmentActivity : "Run possible only from FragmentActivity";
        activity.getSupportLoaderManager().destroyLoader(loaderId);
    }

    public static FeedLoader with(Context context) {
        if (singleton == null) {
            synchronized (FeedLoader.class) {
                if (singleton == null) {
                    singleton = new FeedLoader(context);
                }
            }
        }
        return singleton;
    }
}
