package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;
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
    private static int id = 0;

    private INode mParentNode;
    private List<NodeView> mChildNodeViews = new ArrayList<>();
    private MapView mMapView;
    private MindNodeEnv mEnv;
    private Context mContext;
    private int mId;
    private int mIndex;

    private int mWidth;
    private int mHeight;
    private String mText = "主题";
    private TextPaint mTextPaint;
    //    private RectF mTextRect = new RectF();
    private State mState = State.STATE_NORMAL;

    private Bitmap mBitmapWhiteAdd;

    private IBorder mBorder;

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getText() {
        return mText;
    }

    public enum State {
        STATE_NORMAL,
        STATE_FOCUS,
        STATE_PRE_EDIT,
        STATE_EDIT
    }
//
//    public NodeView(MapView mapView, MindNodeEnv env, int width, int height) {
//        mMapView = mapView;
//        mContext = mMapView.getContext();
//        mEnv = env;
//        mWidth = width;
//        mHeight = height;
//        init();
//    }

    public NodeView(MapView mapView, MindNodeEnv env) {
        mId = ++id;
        mMapView = mapView;
        mContext = mMapView.getContext();
        mEnv = env;
        mWidth = 200;
        mHeight = 100;
        init();
    }

    private void init() {
        mBorder = BorderFactory.create(LineBorder.class, mEnv, this);
        mBitmapWhiteAdd = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.whitestyle_add_32);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(0xff333333);
        mTextPaint.setTextSize(40);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void setText(String text) {
        mText = text;
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
                canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);
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
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (getTextRect().centerY() - top / 2 - bottom / 2);
        canvas.drawText(mText+"_"+mId, getTextRect().centerX(), baseLineY, mTextPaint);
    }

    @Override
    public void onStartDrag() {

    }

    @Override
    public void onDragging(int left, int top, int moveX, int moveY) {

    }

    @Override
    public void onStopDrag(float xvel, float yvel) {
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
                    NodeView childNodeView = mMapView.addNodeView(this, RoundRectBorder.class, mId + "");
                    mChildNodeViews.add(childNodeView);
                    childNodeView.setIndex(mChildNodeViews.size() - 1);
                    mMapView.invalidate();
                } else {
                    setState(STATE_PRE_EDIT);
                }
                break;
            case STATE_PRE_EDIT:
                setState(STATE_EDIT);
                mMapView.showEditView();
                break;
        }

    }


    private void update() {
    }

    public void setParentNode(INode parentNode) {
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

    @Override
    public int getLeft() {
        if (mParentNode == null) {
            return 200;
        }
        return mParentNode.getRight() + 100;
    }

    @Override
    public int getTop() {
        if (mId == 3) {
            Log.i("jwd", "getTop");
        }
        if (mParentNode == null) {//全局第一个节点
            return mMapView.getHeight() / 2;
        }
        if (mIndex == 0) {
            return mParentNode.getTop() - 600;
        } else {
            return mParentNode.getChildViews().get(mIndex - 1).getBottom() + 100;
        }
    }

    @Override
    public int getBottom() {
        return getTop() + getHeight();
    }

    @Override
    public int getRight() {
        return getLeft() + getWidth();
    }

    @Override
    public RectF getViewRect() {
        RectF rectF = new RectF();
        rectF.set(getLeft(), getTop(), getRight(), getBottom());
        return rectF;
    }

    @Override
    public RectF getTextRect() {
        return getViewRect();
    }

    @Override
    public List<NodeView> getChildViews() {
        return mChildNodeViews;
    }

    @Override
    public NodeView getLastView() {
        if (mChildNodeViews.size() == 0) {
            return null;
        }
        return mChildNodeViews.get(mChildNodeViews.size() - 1);
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

    public TextPaint getTextPaint() {
        return mTextPaint;
    }
}
