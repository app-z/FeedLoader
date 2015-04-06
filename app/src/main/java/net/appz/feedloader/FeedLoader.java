package net.appz.feedloader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

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

    LoaderManager.LoaderCallbacks<Object> callback = new LoaderManager.LoaderCallbacks<Object>() {
        @Override
        public Loader<Object> onCreateLoader(int id, Bundle args) {
            return new FeedLoaderWrapper(context , args);
        }

        @Override
        public void onLoadFinished(Loader<Object> loader, Object data) {
            dispatcherData.callBackMap.get(loader.getId()).onResponse(loader.getId(), data);
        }

        @Override
        public void onLoaderReset(Loader<Object> loader) {

        }
    };

    public FeedLoader addLoader(int loaderId,
                                String url,
                                Class<?> loaderClazz,
                                DispatcherData.Listener callback){
        dispatcherData.loaderClazzMap.put(loaderId, loaderClazz);
        dispatcherData.callBackMap.put(loaderId, callback);
        dispatcherData.urlFeeds.put(loaderId, url);
        return singleton;
    }

    public void start(int loaderId, final FragmentActivity context) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dispatcherData", dispatcherData);
        assert context instanceof FragmentActivity : "Run possible only from FragmentActivity";
        context.getSupportLoaderManager().
                restartLoader(loaderId, bundle, callback);
    }

    public void stop(int loaderId, final Context context) {
        ((FragmentActivity)context).getSupportLoaderManager().destroyLoader(loaderId);
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
