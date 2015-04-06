package net.appz.feedloader;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.VolleyError;

import java.util.HashMap;

/**
 * Created by App-z.net on 05.04.15.
 */
public class DispatcherData implements Parcelable {
    HashMap<Integer, Class> loaderClazzMap = new HashMap<>();
    HashMap<Integer, Listener> callBackMap = new HashMap<>();
    HashMap<Integer, String> urlFeeds = new HashMap<>();

    public DispatcherData(){}

    protected DispatcherData(Parcel in) {
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
