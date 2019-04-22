package su.com.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WrapContentListView extends ViewGroup {

    public interface StateCallback{
        void onClose();
        void onOpen();
        void onProgress(float progress);
    }
    private StateCallback stateCallback;
    public void setStateCallback(StateCallback stateCallback) {
        this.stateCallback = stateCallback;
    }
    private View contentView;
    private View listView;
    private int scrollY;
    private float rate = 0.6f;
    private float clamp=0.18f;
    public enum State {
        Close, Open
    }
    private State state = State.Open;
    private int scrollTop;
    private boolean flag = false;//拦截标志，是否当前控件控制事件
    private int topHeight;
    private boolean firstMeasure = true;
    private boolean useFilter=true;
    private ExecutorService executorService;
    private Runnable topRun = new Runnable() {
        @Override
        public void run() {
            while (scrollTop < topHeight / 2) {
                scrollTop -= step;
                if(stateCallback!=null){
                    stateCallback.onProgress((float)scrollTop/topHeight);
                }
                if (scrollTop <= 0) {
                    scrollTop = 0;
                    state = State.Close;
                    if(stateCallback!=null){
                        stateCallback.onClose();
                    }
                    break;
                }
                post(refreshRun);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Runnable bottomRun = new Runnable() {
        @Override
        public void run() {
            while (scrollTop >= topHeight / 2) {
                scrollTop += step;
                if(stateCallback!=null){
                    stateCallback.onProgress((float)scrollTop/topHeight);
                }
                if (scrollTop >= topHeight) {
                    scrollTop = topHeight;
                    state = State.Open;
                    if(stateCallback!=null){
                        stateCallback.onOpen();
                    }
                    break;
                }
                post(refreshRun);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Runnable refreshRun = new Runnable() {
        @Override
        public void run() {
            requestLayout();
        }
    };
    private int sleep = 3;
    private int step=10;

    public WrapContentListView(Context context) {
        this(context, null);
    }

    public WrapContentListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        executorService = Executors.newFixedThreadPool(1);
        initStyle(context,attrs);
    }

    private void initStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray=null;
        try{
            typedArray=context.obtainStyledAttributes(attrs,R.styleable.WrapContentListView);
            int len=typedArray.getIndexCount();
            for(int i=0;i<len;i++){
                if(typedArray.getIndex(i)==R.styleable.WrapContentListView_clamp){
                    clamp=typedArray.getFloat(i,0.18f);
                }else if(typedArray.getIndex(i)==R.styleable.WrapContentListView_rate){
                    rate=typedArray.getFloat(i,0.6f);
                }else if(typedArray.getIndex(i)==R.styleable.WrapContentListView_sleep){
                    sleep=typedArray.getInteger(i,3);
                }else if(typedArray.getIndex(i)==R.styleable.WrapContentListView_step){
                    step=typedArray.getInteger(i,10);
                }else if(typedArray.getIndex(i)==R.styleable.WrapContentListView_useFilter){
                    useFilter=typedArray.getBoolean(i,true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(typedArray!=null){
                typedArray.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (contentView == null || listView == null) {
            contentView = getChildAt(0);
            listView = getChildAt(1);
            listView.requestLayout();
        }
        if (contentView == null || listView == null) {
            throw new RuntimeException("请确保控件内有两个子view，一个将会被包裹，另一个是底部滑动的列表视图");
        }
        LayoutParams params = contentView.getLayoutParams();
        int height = params.height;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int hSpec;
        if (height == LayoutParams.WRAP_CONTENT) {
            if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) {
                hSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            } else {
                hSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
        } else if (height == LayoutParams.MATCH_PARENT) {
            if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) {
                hSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            } else {
                hSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
        } else {
            hSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        }
        contentView.measure(widthMeasureSpec, hSpec);
        topHeight = contentView.getMeasuredHeight();
        if (firstMeasure) {
            scrollTop = topHeight;
            firstMeasure = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        contentView.layout((int) (l+(scrollTop-topHeight)*clamp),
                (int) ((int) (t+(scrollTop-topHeight)*clamp)+(scrollTop-topHeight)*clamp),
                (int) (r-(scrollTop-topHeight)*clamp),
                (int) ((int) (t + topHeight+(scrollTop-topHeight)*clamp)-(scrollTop-topHeight)*clamp));
        if(flag&&useFilter//拦截子view事件并且使用过滤色
                &&(contentView.getClass()==ImageView.class)//是否是imageview
                ||contentView.getClass().getSuperclass()==ImageView.class)//或者继承imageview
        {
            float alpha=Math.abs((0.5f-(float)scrollTop/topHeight));
            ((ImageView)contentView).setColorFilter(Color.parseColor("#"
                    +(((int)(99*alpha))<10?("0"+((int)(99*alpha))):((int)(99*alpha)))+"000000"));
        }
        listView.layout(l, t + scrollTop, r, b);
    }

    private int y_touch;
    private int lastY_touch;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y_touch = (int) ev.getY();
                lastY_touch = (int) ev.getY();
                flag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                y_touch = (int) ev.getY();
                if (state == State.Open) {//打开状态
                    flag = true;
                } else{//关闭状态
                    if (listView.getClass() == ListView.class) {
                        int firstPosition = ((ListView) listView).getFirstVisiblePosition();
                        if (firstPosition == 0 && ((ListView) listView).getChildCount() > 0
                                && ((ListView) listView).getChildAt(0).getScrollY() <= 5) {//列表到达顶部
                            ((ListView) listView).getChildAt(0).setTop(0);
                            if (y_touch - lastY_touch < 0) {
                                flag = false;
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }
                }

                if (flag) {//打开状态或者关闭状态且下方列表在顶端的时候下拉
                    y_touch = (int) ev.getY();
                    scrollY = y_touch - lastY_touch;
                    scrollTop += scrollY * rate;
                    lastY_touch = y_touch;
                    state=State.Open;
                    if (scrollTop > topHeight) {
                        scrollTop = topHeight;
                        state = State.Open;
                        if(stateCallback!=null){
                            stateCallback.onOpen();
                        }
                    }
                    if (scrollTop < 0) {
                        scrollTop = 0;
                        state = State.Close;
                        if(stateCallback!=null){
                            stateCallback.onClose();
                        }
                    }
                    if(stateCallback!=null){
                        stateCallback.onProgress((float)scrollTop/topHeight);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (scrollTop >= topHeight / 2&&scrollTop<topHeight) {
                    state=State.Open;
                    executorService.execute(bottomRun);
                } else if(scrollTop<topHeight/2&&scrollTop>0){
                    state=State.Open;
                    executorService.execute(topRun);
                }
                break;
        }
        requestLayout();
        requestFocus();
        if(state==State.Close) {
            return super.dispatchTouchEvent(ev);
        }else{
            return true;
        }
    }
}
