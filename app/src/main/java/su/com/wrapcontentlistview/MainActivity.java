package su.com.wrapcontentlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import su.com.library.WrapContentListView;

public class MainActivity extends AppCompatActivity {

    WrapContentListView wrapContentListView;
    ListView listView;
    Adapter adapter;
    List<String> data;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrapContentListView=findViewById(R.id.wrap);
        wrapContentListView.setStateCallback(new WrapContentListView.StateCallback() {
            @Override
            public void onClose() {
                //System.out.println("关闭了");
            }

            @Override
            public void onOpen() {
                //System.out.println("打开了");
            }

            @Override
            public void onProgress(float progress) {
                //System.out.println("过程："+progress);
            }
        });

        listView=findViewById(R.id.list);
        data=new ArrayList<>();
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/f7e4bcbe40f7442180ac3305031f8ea6.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/c2f37a844064185680bf0968d255e7d8.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/5a08abfd4005067c8083f81cee91637d.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/c35bd89540445d01806951cd3a5d67e7.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/b2bc388d404778278010db10841cf307.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/a5bbc4f340ef1ec980f332dcb0d9dec9.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/e38cff37407a03e680b0aa261478a2dd.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/f875a64e401d9357803744490dc70953.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/cf4e0c9540006ab78007c499bfc194e4.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/287aa2f640728cb38011531661c1bc03.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/28c4c3ab401baed8800215a87aa51713.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/d76c70a540d8838f8088ea4b24bc75a8.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/f8ba19824036130380507f5dcd9fa5ec.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/1479264d40b4892280b52ed258b5ce75.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/c275df9c4006bd51805a1160e1bcad31.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/6da4fac4409949418078dc2e6b9f4b26.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/8319a253402be5d8806fda0e600cdc7d.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/e41f13ee40484fe58020dcddf7c72054.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/23f325ac40b65b8d8012d4cb7c8c6640.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/2736068a4039a5db8001c966b9f46efd.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/b5f00c1b4091b45680d32977a97781d5.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/3d0b805b40b5dff680116166e15c8338.jpg");
        data.add("http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/1232ba83401a67f480fc9251853452f9.jpg");
        adapter=new Adapter(this,data);
        listView.setAdapter(adapter);

        img=findViewById(R.id.img);
        ImageLoader.getLoader().load(img,"http://bmob-cdn-20674.b0.upaiyun.com/2018/10/10/c35bd89540445d01806951cd3a5d67e7.jpg");
    }
}
