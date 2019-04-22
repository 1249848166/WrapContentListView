package su.com.wrapcontentlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class Adapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private int sw=0;

    Adapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        sw=context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView==null){
            itemView=LayoutInflater.from(context).inflate(R.layout.item_txt,null);
        }
        ImageView img=itemView.findViewById(R.id.img);
        img.setImageResource(R.drawable.holder);
        ImageLoader.getLoader().load(img,data.get(position));
        ViewGroup.LayoutParams params=img.getLayoutParams();
        params.width=sw;
        params.height=400;
        img.setLayoutParams(params);
        ViewGroup.LayoutParams params1=itemView.getLayoutParams();
        if(params1==null){
            params1=new ViewGroup.LayoutParams(0,0);
        }
        params1.width=sw;
        params.height=400;
        itemView.setLayoutParams(params1);
        return itemView;
    }

}
