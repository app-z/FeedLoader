package net.appz.feedloader.testsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;

import net.appz.feedloader.DispatcherData;
import net.appz.feedloader.FeedLoader;
import net.appz.feedloader.GoodsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private boolean DEBUG = true;
    private String TAG = getClass().getSimpleName();

    String url = "http://192.168.1.103/shop.json";
    private static final int LOADER_GOODS_ID = 1;

    String url1 = "http://192.168.1.103/shop1.json";
    private static final int LOADER_GOODS_ID_1 = 2;

    String url2 = "http://192.168.1.103/shop2.json";
    private static final int LOADER_GOODS_ID_2 = 3;

    Map<Integer, GoodsItem> mGoodsMap = new HashMap<>();

    ArrayList<GoodsItem> mGoodsList = new ArrayList<GoodsItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FeedLoader.with(this).addLoader(LOADER_GOODS_ID,
                url,
                HashMap.class,
                new DispatcherData.Listener<Map<Integer, GoodsItem>>() {
            @Override
            public void onResponse(int loaderId, Map<Integer, GoodsItem> goodsMap) {
                for (Map.Entry<Integer, GoodsItem> entry : goodsMap.entrySet()){
                    Log.d(TAG , "Loader " + loaderId + " : " + entry.getKey() + " : " + entry.getValue());
                }
            }

            @Override
            public void onErrorResponse(VolleyError data) {
                Log.d(TAG , "onErrorResponse :" + data);
            }
        }).start(LOADER_GOODS_ID, this);


        FeedLoader.with(this).addLoader(LOADER_GOODS_ID_1,
                url1,
                GoodsItem[].class,
                new DispatcherData.Listener<GoodsItem[]>() {
                    @Override
                    public void onResponse(int loaderId, GoodsItem[] goodsItems) {
                        Log.d(TAG, " Items . " + goodsItems.getClass() + goodsItems );
                        for (GoodsItem goodsItem : goodsItems){
                            Log.d(TAG, "Loader " + loaderId + " : " + goodsItem.getName() + " : " + goodsItem.getPrice());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError data) {

                    }
                }).start(LOADER_GOODS_ID_1, this);


        FeedLoader.with(this).addLoader(LOADER_GOODS_ID_2,
                url2,
                mGoodsMap.getClass(),
                new DispatcherData.Listener<Map<Integer, GoodsItem>>() {
                    @Override
                    public void onResponse(int loaderId, Map<Integer, GoodsItem> goodsMap) {
                        for (Map.Entry<Integer, GoodsItem> entry : goodsMap.entrySet()) {
                            Log.d(TAG, "Loader " + loaderId + " : " + entry.getKey() + " : " + entry.getValue());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError data) {
                        Log.d(TAG, "onErrorResponse :" + data);
                    }
                }).start(LOADER_GOODS_ID_2, this);

    }

    @Override
    public void onStop(){
        super.onStop();
        FeedLoader.with(this).stop(LOADER_GOODS_ID, this);
        FeedLoader.with(this).stop(LOADER_GOODS_ID_2, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
