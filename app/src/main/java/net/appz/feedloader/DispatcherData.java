package net.appz.feedloader;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.VolleyError;

import java.util.HashMap;

/**
 * Created by App-z.net on 05.04.15.
 */
public class DispatcherData implements Parcelable {
    private HashMap<Integer, Class> loaderClazzMap = new HashMap<>();
    private HashMap<Integer, Listener> callBackMap = new HashMap<>();
    private HashMap<Integer, String> urlFeeds = new HashMap<>();
    private HashMap<Integer, Boolean> useCache = new HashMap<>();


    public DispatcherData(){}

    void putUseCache(int loaderId, boolean cache){
        useCache.put(loaderId, cache);
    }

    boolean getUseCache(int loaderId){
        return useCache.get(loaderId);
    }

    void putCallBack(int loaderId, Listener callback){
        callBackMap.put(loaderId, callback);
    }

    Listener getCallBack(int loaderId){
        return callBackMap.get(loaderId);
    }

    String getUrlFeed(int loaderId){
        return urlFeeds.get(loaderId);
    }

    void putUrlFeed(int loaderId, String url){
        urlFeeds.put(loaderId, url);
    }


    Class getLoaderClazz(int loaderId){
        return loaderClazzMap.get(loaderId);
    }

    void putLoaderClazz(int loaderId, Class loaderClazz){
        loaderClazzMap.put(loaderId, loaderClazz);
    }

    protected DispatcherData(Parcel in) {
        useCache = (HashMap) in.readValue(HashMap.class.getClassLoader());
        loaderClazzMap = (HashMap) in.readValue(HashMap.class.getClassLoader());
        callBackMap = (HashMap) in.readValue(HashMap.class.getClassLoader());
        urlFeeds = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(useCache);
        dest.writeValue(loaderClazzMap);
        dest.writeValue(callBackMap);
        dest.writeValue(urlFeeds);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DispatcherData> CREATOR = new Parcelable.Creator<DispatcherData>() {
        @Override
        public DispatcherData createFromParcel(Parcel in) {
            return new DispatcherData(in);
        }

        @Override
        public DispatcherData[] newArray(int size) {
            return new DispatcherData[size];
        }
    };

    public interface Listener<D>{
        void onResponse(int loaderId, D data);
        void onErrorResponse(VolleyError data);
    }
}
