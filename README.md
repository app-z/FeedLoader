# FeedLoader
Volley FeedLoader

This is approach how to parse Json feed for Fragment in one line. There is only 4 files witch you should include in your project. Use package net.appz.feedloader;

File list:
```
DispatcherData.java
FeedLoader.java
FeedLoaderWrapper.java
GsonRequest.java
```

How to use<br>

Json file on local server http://192.168.1.103/shop.json
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
    private String name;
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
```
<br>
Json file on local server http://192.168.1.103/shop1.json
```
[
{"name":"Samsung","price":51200.6},
{"name":"Lg","price":5400.6},
{"name":"Alcatel","price":4500.6},
{"name":"iPhone","price":4800.3},
{"name":"iPad","price":2850.1}
]
```
Data class is the same
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
    String url1 = "http://192.168.1.103/shop1.json";
    private static final int LOADER_GOODS_ID_1 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FeedLoader.with(this).addLoader(LOADER_GOODS_ID_1,
                url1,
                GoodsItem[].class,
                new DispatcherData.Listener<GoodsItem[]>() {
                    @Override
                    public void onResponse(int loaderId, GoodsItem[] goodsItems) {
                        for (GoodsItem goodsItem : goodsItems){
                            Log.d(TAG, "Loader " + loaderId + " : " + goodsItem.getName() + " : " + goodsItem.getPrice());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError data) {

                    }
                }).start(LOADER_GOODS_ID_1, this);

```
<br>
Note:
For add Fragment use Handler
```
   ...
   @Override
   public void onResponse(int loaderId, Icons icons) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Here you fragment operation
                Fragment iconsGridFragment = IconsGridFragment.newInstance(icons);
                // Add the fragment to the activity, pushing this transaction on to the back stack.
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, iconsGridFragment, IconsGridFragment.class.getSimpleName());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            });
        }
    }
```







Copyright (c) 2015 Appz (http://app-z.net)
