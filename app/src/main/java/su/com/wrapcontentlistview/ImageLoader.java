package su.com.wrapcontentlistview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private static class ImageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            ImageHolder holder= (ImageHolder) msg.obj;
            ImageView imageView=holder.imageView;
            String url=holder.url;
            if(imageView.getTag().equals(url)){
                Bitmap bitmap=holder.bitmap;
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    ImageHandler handler=new ImageHandler();

    private static ImageLoader loader;

    private ImageLoader(){
        lruCache=new LruCache<String,Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
        executorService=Executors.newFixedThreadPool(1);
    }

    public static ImageLoader getLoader(){
        if(loader==null){
            synchronized (ImageLoader.class){
                if(loader==null){
                    loader=new ImageLoader();
                }
            }
        }
        return loader;
    }

    private LruCache<String,Bitmap> lruCache;
    private ExecutorService executorService;

    public void load(final ImageView imageView, final String url){
        final Bitmap[] bitmap = {lruCache.get(url)};
        if(bitmap[0] !=null){
            imageView.setImageBitmap(bitmap[0]);
        }else{
            imageView.setTag(url);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection conn=null;
                    try{
                        URL u=new URL(url);
                        conn= (HttpURLConnection) u.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);
                        conn.setRequestMethod("GET");
                        conn.connect();
                        int code=conn.getResponseCode();
                        if(code==200){
                            bitmap[0] =BitmapFactory.decodeStream(conn.getInputStream());
                            lruCache.put(url,bitmap[0]);
                            ImageHolder holder=new ImageHolder(bitmap[0],imageView,url);
                            Message msg=handler.obtainMessage();
                            msg.obj=holder;
                            handler.sendMessage(msg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(conn!=null){
                            conn.disconnect();
                        }
                    }
                }
            });
        }
    }

    private class ImageHolder{
        Bitmap bitmap;
        ImageView imageView;
        String url;
        public ImageHolder(Bitmap bitmap, ImageView imageView,String url) {
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.url=url;
        }
    }
}
