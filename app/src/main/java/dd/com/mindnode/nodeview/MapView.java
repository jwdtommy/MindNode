package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static dd.com.mindnode.nodeview.NodeView.State.STATE_FOCUS;
import static dd.com.mindnode.nodeview.NodeView.State.STATE_NORMAL;

public class MapView extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private List<Line> mLines = new ArrayList<>();
//    private float mScaleFactor = 1;
//    private float mScale = 1;
//    private float mLastScale = 1;
//    private float mScalePivotX;
//    private float mScalePivotY;
//    private float mDistanceX;
//    private float mDistanceY;
//    private float mLastDistanceX;
//    private float mLastDistanceY;

    private NodeView mNodeView;
    private List<NodeView> mNodeViews = new ArrayList<>();

    private MindNodeEnv mEnv;

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            mEnv.setDistanceX(mEnv.getLastDistanceX() - distanceX);
            mEnv.setDistanceY(mEnv.getLastDistanceY() - distanceY);
            mEnv.setLastDistanceX(mEnv.getDistanceX());
            mEnv.setLastDistanceY(mEnv.getDistanceY());
            invalidate();
            return false;
        }
    };

    private SimpleOnScaleGestureListener mSimpleOnScaleGestureListener = new SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mEnv.setScaleFactor(detector.getScaleFactor());
            mEnv.setScale(mEnv.getLastScale() * mEnv.getScaleFactor());
            mEnv.setScalePivotX(detector.getFocusX());
            mEnv.setScalePivotY(detector.getFocusY());
            invalidate();
            return super.onScale(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            mEnv.setLastScale(mEnv.getScale());
        }
    };

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mEnv = MindNodeEnv.getEnv();
        post(new Runnable() {
            @Override
            public void run() {
                mNodeView = new NodeView(MapView.this, mEnv, 0, 0);
                mNodeView.setBorderStyle(NodeView.BorderStyle.STYLE_RECT);
                mNodeView.setState(NodeView.State.STATE_FOCUS);
                mCurrentView = mNodeView;
                mNodeViews.add(mNodeView);
                mScaleGestureDetector = new ScaleGestureDetector(getContext(), mSimpleOnScaleGestureListener);
                mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private NodeView mDownNodeView;
    private NodeView mUpNodeView;
    private NodeView mCurrentView;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            return mScaleGestureDetector.onTouchEvent(event);
        } else {
            super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("jwd", "action down x=" + event.getX());
                Log.i("jwd", "action down y=" + event.getY());
                mDownNodeView = findNodeView(event.getX(), event.getY());
                if (mDownNodeView == null) {
                    setCurrentViewNormal();
                    postInvalidate();
                    mGestureDetector.onTouchEvent(event);
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mUpNodeView = findNodeView(event.getX(), event.getY());
                if (mDownNodeView != null && mUpNodeView != null && mDownNodeView == mUpNodeView) {
                    mUpNodeView.handleOnTouchEvent(event);
                    postInvalidate();
                }

                if (mDownNodeView == null) {
                    mEnv.setLastDistanceX(mEnv.getDistanceX());
                    mEnv.setLastDistanceY(mEnv.getDistanceY());
                }
                if (mUpNodeView == null) {
                    setCurrentViewNormal();
                    postInvalidate();
                }
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (mDownNodeView == null) {
                    mGestureDetector.onTouchEvent(event);
                }
            }
        }
        return true;

    }

    private NodeView findNodeView(float x, float y) {
        for (int i = 0; i < mNodeViews.size(); i++) {
            NodeView nodeView = mNodeViews.get(i);
            RectF srcRect = nodeView.getViewRect();
            RectF newRect = mEnv.getTransRect(srcRect);
            if (newRect.contains(x, y)) {
                return nodeView;
            }
        }
        return null;
    }

    int awidth = 100;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.preTranslate(mEnv.getDistanceX(), mEnv.getDistanceY());
        matrix.preScale(mEnv.getScale(), mEnv.getScale(), mEnv.getScalePivotX(), mEnv.getScalePivotY());
        canvas.setMatrix(matrix);
        for (int i = 0; i < mNodeViews.size(); i++) {
            mNodeViews.get(i).onDraw(canvas);
        }
        drawLines(canvas);
        canvas.restore();

//        float tx = 100;
//        float ty = 100;
//        float sx = 2f;
//        float sy = 3f;
//        int px = 100;
//        int py = 80;
//
//        int srcLeft = 300 - awidth / 2;
//        int srcTop = 300 - awidth / 2;
//        int srcRight = 300 + awidth / 2;
//        int srcBottom = 300 + awidth / 2;
//        matrix.preTranslate(tx, ty);
//        matrix.preScale(sx, sy, px, py);
//        canvas.setMatrix(matrix);
//        canvas.drawRect(srcLeft, srcTop, srcRight, srcBottom, p2);
//        canvas.restore();
//
//        canvas.save();
//        Paint p3 = new Paint();
//        p3.setAlpha(100);
//        p3.setColor(0xaaffcd00);
//
//        int dstLeft = (int) ((srcLeft - px) * sx + px + tx);
//        int dstTop = (int) ((srcTop - py) * sy + py + ty);
//        int dstRight = (int) ((srcRight - px) * sx + px + tx);
//        int dstBottom = (int) ((srcBottom - py) * sy + py + ty);
//
//        canvas.drawRect(dstLeft, dstTop, dstRight, dstBottom, p3);

    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < mLines.size(); i++) {
            mLines.get(i).drawSelf(canvas);
        }
    }


    public void setCurrentView(NodeView currentView) {
        if (mCurrentView != null) {
            mCurrentView.setState(STATE_NORMAL);
        }
        currentView.setState(STATE_FOCUS);
        mCurrentView = currentView;
    }

    public void setCurrentViewNormal() {
        mCurrentView.setState(STATE_NORMAL);
    }

    public void addNodeView(NodeView nodeViewA, NodeView nodeViewB) {
        setCurrentView(nodeViewB);
        mNodeViews.add(nodeViewB);
        Line line = new Line(nodeViewA, nodeViewB);
        mLines.add(line);
        postInvalidate();
    }

    public NodeView addNodeView(NodeView parentNodeView, int childNodeX, int childNodeY, NodeView.BorderStyle borderStyle, String text) {
        NodeView nodeView = new NodeView(this, mEnv, childNodeX, childNodeY);
        nodeView.setBorderStyle(borderStyle);
        nodeView.setText(text);
        addNodeView(parentNodeView, nodeView);
        return nodeView;
    }


    public static int lineColorIndex = 0;


    public class Line {
        private Path mPath;
        private Paint mPaint;
        private NodeView mNodeViewA;
        private NodeView mNodeViewB;

        public Line(NodeView nodeViewA, NodeView nodeViewB) {
            mNodeViewA = nodeViewA;
            mNodeViewB = nodeViewB;
            mPath = new Path();

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Consts.LINE_COLORS[lineColorIndex]);
            if (++lineColorIndex == Consts.LINE_COLORS.length) {
                lineColorIndex = 0;
            }
        }

        public void drawSelf(Canvas canvas) {
            int[] xyA = mNodeViewA.getConnectStartPoint();
            int[] xyB = mNodeViewB.getConnectEndPoint();
            mPath.reset();
            canvas.save();
            canvas.translate(xyA[0], xyA[1]);
            mPath.moveTo(0, 0);
            mPath.quadTo((xyB[0] - xyA[0]) / 2, xyB[1] - xyA[1], xyB[0] - xyA[0], xyB[1] - xyA[1]);
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }
    }

}
