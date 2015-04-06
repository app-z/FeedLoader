# FeedLoader
Volley FeedLoader

This is aproach how parse Json feed for Fragment in one line. There is only 4 files witch you should include in project. Use package net.appz.feedloader;

How to use
Json
```
{
"1":{"name":"Samsung","price":51200.6},
"2":{"name":"Lg","price":5400.6},
"3":{"name":"Alcatel","price":4500.6},
"4":{"name":"iPhone","price":4800.3},
"7":{"name":"iPad","price":2850.1}
}
```
Data class
```
public class GoodsItem {
    public String getName() {
        return name;
    }
    private String name;
    public float getPrice() {
        return price;
    }
    private float price;
}
```
Sample of use
```
    String url = "http://192.168.1.103/shop.json";
    private static final int LOADER_GOODS_ID = 1;

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
