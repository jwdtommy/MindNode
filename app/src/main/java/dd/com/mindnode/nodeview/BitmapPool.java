package dd.com.mindnode.nodeview;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapPool {

    LruCache<Integer,Bitmap> mCache=new LruCache<>(10000);

    private BitmapPool(){
//        mCache.put(R.drawable.whitestyle_add_24,)

    }

    public BitmapPool getInstance(){
        return new BitmapPool();
    }

}
