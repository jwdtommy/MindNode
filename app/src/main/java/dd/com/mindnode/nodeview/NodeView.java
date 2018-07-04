package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.ArrayList;
import java.util.List;

import dd.com.mindnode.R;
import dd.com.mindnode.nodeview.border.BorderFactory;
import dd.com.mindnode.nodeview.border.IBorder;
import dd.com.mindnode.nodeview.border.LineBorder;
import dd.com.mindnode.nodeview.border.RoundRectBorder;

import static dd.com.mindnode.nodeview.NodeView.State.STATE_EDIT;
import static dd.com.mindnode.nodeview.NodeView.State.STATE_PRE_EDIT;

public class NodeView extends NodeViewBase implements INode {
    private static final int LINE_WIDTH = 100;
    private static final int DISTANCE_Y = 40;

    private INode mParentNode;
    private boolean mIsDragging;
    private Node mNode;
    private List<NodeView> mChildNodeViews = new ArrayList<>();
    private MapView mMapView;
    private MindNodeEnv mEnv;
    private Context mContext;

    private int mWidth;
    private int mHeight;
    private int x;
    private int y;
    private String mText = "主题";
    private Paint mBorderPaint;
    private Paint mTextPaint;
    private RectF mViewRect = new RectF();
    private RectF mTextRect = new RectF();
    private State mState = State.STATE_NORMAL;

    private Bitmap mBitmapWhiteAdd;


    private IBorder mBorder;

    public enum State {
        STATE_NORMAL,
        STATE_FOCUS,
        STATE_PRE_EDIT,
        STATE_EDIT
    }

    public NodeView(MapView mapView, MindNodeEnv env, int width, int height, int x, int y) {
        mMapView = mapView;
        mContext = mMapView.getContext();
        mEnv = env;
        mWidth = width;
        mHeight = height;
        init();
    }

    public NodeView(MapView mapView, MindNodeEnv env, int x, int y) {
        mMapView = mapView;
        mContext = mMapView.getContext();
        mEnv = env;
        mWidth = 200;
        mHeight = 100;
        this.x = x;
        this.y = y;
        init();
    }

    private void init() {
        mBorder = BorderFactory.create(LineBorder.class, mEnv, this);
        mBitmapWhiteAdd = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.whitestyle_add_32);
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(4);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(0xff8be9fd);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xff333333);
        mTextPaint.setTextSize(40);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL);
        mViewRect.set(x, y, x + mWidth, y + mHeight);

    }

    public void setText(String text) {
        mText = text;
    }

    public void setNode(Node node) {
        mNode = node;
    }

    @Override
    public void onDraw(Canvas canvas) {
        switch (mState) {
            case STATE_NORMAL:
                drawBorder(canvas);

                drawText(canvas);
                break;
            case STATE_FOCUS:
                drawBorder(canvas);
                drawText(canvas);
                drawFocusLayer(canvas);
                break;
            case STATE_PRE_EDIT:
                drawBorder(canvas);
                drawText(canvas);
                Paint paint = new Paint();
                paint.setAlpha(100);
                paint.setColor(0x33000000);
                canvas.drawRect(x, y, x + mWidth, y + mHeight, paint);
                break;
            case STATE_EDIT:
                drawBorder(canvas);
                drawText(canvas);
                break;
        }
    }

    private void drawFocusLayer(Canvas canvas) {
        canvas.drawBitmap(mBitmapWhiteAdd, getRight() - mBitmapWhiteAdd.getWidth(), getBottom() - mBitmapWhiteAdd.getHeight(), null);
    }

    private void drawBorder(Canvas canvas) {
        mBorder.onDraw(canvas);
//        switch (mBorderStyle) {
//            case STYLE_LINE:
//                mBorderPaint.reset();
//                mBorderPaint.setAntiAlias(true);
//                mBorderPaint.setStyle(Paint.Style.FILL);
//                mBorderPaint.setColor(0xff8be9fd);
//                canvas.drawRect(x, y + mHeight - 10, x + mWidth, y + mHeight, mBorderPaint);
//                break;
//            case STYLE_RECT:
//                mBorderPaint.reset();
//                mBorderPaint.setAntiAlias(true);
//                mBorderPaint.setStrokeWidth(4);
//                mBorderPaint.setStyle(Paint.Style.STROKE);
//                mBorderPaint.setColor(0xff8be9fd);
//                mBorderRect.set(x, y, x + mWidth, y + mHeight);
//                canvas.drawRoundRect(mBorderRect, 10, 10, mBorderPaint);
//                break;
//            default:
//                break;
//        }
    }

    private void drawText(Canvas canvas) {
        mTextRect.set(x, y, x + mWidth, y + mHeight);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (mTextRect.centerY() - top / 2 - bottom / 2);
        canvas.drawText(mText + "", mTextRect.centerX(), baseLineY, mTextPaint);
    }

    @Override
    public void onStartDrag() {
        onStartDragImpl();
    }

    @Override
    public void onDragging(int left, int top, int moveX, int moveY) {
        onDraggingImpl(left, top, moveX, moveY);
    }

    @Override
    public void onStopDrag(float xvel, float yvel) {
        onStopDragImpl(xvel, yvel);
    }

    @Override
    public void onConnected(INode parentNode) {
        onConnectImpl(parentNode);
    }

    @Override
    public int[] getConnectStartPoint() {
        return mBorder.getConnectStartPoint();
    }

    @Override
    public int[] getConnectEndPoint() {
        return mBorder.getConnectEndPoint();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return true;
    }


    public void handleOnTouchEvent(MotionEvent event) {
        State state = getState();
        switch (state) {
            case STATE_NORMAL:
                mMapView.replaceCurrentView(this);
                break;
            case STATE_FOCUS:
                RectF rectF = new RectF();
                rectF.set(getRight() - mBitmapWhiteAdd.getWidth(), getBottom() - mBitmapWhiteAdd.getHeight(), getRight(), getBottom());
                rectF = mEnv.getTransRect(rectF);
                if (rectF.contains(event.getX(), event.getY())) {
                    int childNodeX = getRight() + 100;
                    int childNodeY;
                    if (mChildNodeViews.size() > 0) {
                        NodeView lastChildView = mChildNodeViews.get(mChildNodeViews.size() - 1);
                        childNodeY = lastChildView.getBottom() + 30;
                    } else {
                        childNodeY = getTop() - 100;
                    }
                    String text = "子主题" + (mChildNodeViews.size() + 1);
                    NodeView childNodeView = mMapView.addNodeView(this, childNodeX, childNodeY, RoundRectBorder.class, text);
                    mChildNodeViews.add(childNodeView);
                } else {
                    setState(STATE_PRE_EDIT);
                }
                break;
            case STATE_PRE_EDIT:
                setState(STATE_EDIT);
                break;
        }
    }

    private void onStartDragImpl() {
        mIsDragging = true;
    }

    private void onDraggingImpl(int left, int top, int moveX, int moveY) {
    }

    private void onStopDragImpl(float xvel, float yvel) {
        mIsDragging = false;
    }

    private void onConnectImpl(INode parentNode) {
        mParentNode = parentNode;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getLeft() {
        return x;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return y + mHeight;
    }

    public int getRight() {
        return x + mWidth;
    }

    public RectF getViewRect() {
        return mViewRect;
    }

    public void setState(State state) {
        mState = state;
    }

    public State getState() {
        return mState;
    }

    public void setBorder(Class<? extends IBorder> border) {
        mBorder = BorderFactory.create(border, mEnv, this);
    }

}
