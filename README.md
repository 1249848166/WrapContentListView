# 效果
![效果图](gif5新文件.gif)
# 使用
```java
<su.com.library.WrapContentListView
        android:id="@+id/wrap"
        android:fitsSystemWindows="true"
        su:rate="0.6"
        su:clamp="0.18"
        su:useFilter="true"
        su:sleep="3"
        su:step="10"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <!--顶部视图-->
        <ImageView
            android:id="@+id/img"
            android:scaleType="centerCrop"
            android:layout_width="match_parent"
            android:layout_height="700px"/>
        <!--底部列表-->
        <ListView
            android:id="@+id/list"
            android:dividerHeight="5px"
            android:divider="#ddd"
            android:background="#fff"
            android:scrollbars="none"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </su.com.library.WrapContentListView>
```