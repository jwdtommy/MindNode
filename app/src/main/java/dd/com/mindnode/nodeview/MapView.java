package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MapView extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private ScaleGestureDetector mScaleGestureDetector;
    private List<Line> mLines = new ArrayList<>();
    private float mScaleFactor = 1;

    private SimpleOnScaleGestureListener mSimpleOnScaleGestureListener = new SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i("jwd", "onScaleBegin SPAN" + detector.getCurrentSpan());
            Log.i("jwd", "onScaleBegin X" + detector.getCurrentSpanX());
            Log.i("jwd", "onScaleBegin Y" + detector.getCurrentSpanY());
            Log.i("jwd", "onScaleBegin time" + detector.getTimeDelta());
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("jwd", "onScale " + detector.getScaleFactor());
            Log.i("jwd", "onScale span " + detector.getCurrentSpan());
            mScaleFactor = detector.getScaleFactor();
            for (int i = 0; i < getChildCount(); i++) {
                NodeView nodeView = (NodeView) getChildAt(i);
                nodeView.onScale(detector);
            }
            invalidate();
            return super.onScale(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.i("jwd", "onScaleEnd " + detector.getCurrentSpan());
            Log.i("jwd", "onScaleEnd " + detector.getCurrentSpanX());
            Log.i("jwd", "onScaleEnd " + detector.getCurrentSpanY());
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
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mSimpleOnScaleGestureListener);
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                ((NodeView) capturedChild).onStartDrag();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                ((NodeView) changedView).onDragging(left, top, dx, dy);
                invalidate();
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                ((NodeView) releasedChild).onStopDrag(xvel, yvel);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }
        });

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() >= 2) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return mViewDragHelper.shouldInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            return mScaleGestureDetector.onTouchEvent(event);
        } else {
            mViewDragHelper.processTouchEvent(event);
            return true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < mLines.size(); i++) {
            mLines.get(i).drawSelf(canvas);
        }
    }

    public void addNodeView(NodeView nodeViewA, NodeView nodeViewB) {
        addView(nodeViewB);
        Line line = new Line(nodeViewA, nodeViewB);
        mLines.add(line);
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
            Matrix matrix = new Matrix();
            matrix.preTranslate(xyA[0], xyA[1]);
            matrix.preScale(mScaleFactor, mScaleFactor);
            canvas.setMatrix(matrix);
            mPath.moveTo(0, 0);
            mPath.quadTo((xyB[0] - xyA[0]) / 2, xyB[1]-xyA[1], xyB[0] - xyA[0], xyB[1] - xyA[1]);
            canvas.drawPath(mPath, mPaint);
            canvas.drawLine(0, 0, xyB[0] - xyA[0], xyB[1] - xyA[1], new Paint());
            canvas.restore();
        }
    }

}
