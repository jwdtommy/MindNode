package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import dd.com.mindnode.nodeview.border.RoundRectBorder;

import static dd.com.mindnode.nodeview.NodeView.State.STATE_FOCUS;
import static dd.com.mindnode.nodeview.NodeView.State.STATE_NORMAL;

public class MapView extends FrameLayout {

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private List<Line> mLines = new ArrayList<>();
    private NodeView mNodeView;
    private List<NodeView> mNodeViews = new ArrayList<>();

    private MindNodeEnv mEnv;
    private EditText mEditText;

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
            if (mScaleGestureDetector.isInProgress()) {
                mEnv.setScaleFactor(detector.getScaleFactor());
                mEnv.setScale(mEnv.getLastScale() * mEnv.getScaleFactor());
                mEnv.setScalePivotX(detector.getFocusX());
                mEnv.setScalePivotY(detector.getFocusY());
                Log.i("jwd", "onScale fx=" + detector.getFocusX());
                Log.i("jwd", "onScale fy=" + detector.getFocusY());
                postInvalidate();
                return false;
            }
            Log.i("jwd", "onScale but not in progress");
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.i("jwd", "onScaleEnd fx=" + detector.getFocusX());
            Log.i("jwd", "onScaleEnd fy=" + detector.getFocusY());
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
        setBackgroundColor(mEnv.getCurrentColorStyle().bgColor);
        mEditText = new EditText(getContext());
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40 * mEnv.getScaleFactor());
        mEditText.setCursorVisible(false);
        mEditText.setFocusable(true);
        mEditText.setGravity(Gravity.CENTER);
        mEditText.setSelectAllOnFocus(true);
        mEditText.setBackgroundColor(0x00ffffff);
        mEditText.setVisibility(GONE);
        addView(mEditText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mText = s.toString();
                if (TextUtils.isEmpty(mText)) {
                    return;
                }
                Log.i("jwd", "aftertextChanged text=" + mText);
                int width = (int) StaticLayout.getDesiredWidth(mCurrentView.getText() + mText, mCurrentView.getTextPaint());
                mCurrentView.setTextWidth(width);
                mCurrentView.setText(mCurrentView.getText() + mText);
                mEditText.setText("");
                postInvalidate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        post(new Runnable() {
            @Override
            public void run() {
                mNodeView = new NodeView(MapView.this, mEnv);
                mNodeView.setLevel(0);
                mNodeView.setBorder(RoundRectBorder.class);
                mNodeView.setState(NodeView.State.STATE_FOCUS);
                mCurrentView = mNodeView;
                mNodeViews.add(mNodeView);
                mScaleGestureDetector = new ScaleGestureDetector(getContext(), mSimpleOnScaleGestureListener);
                mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
                invalidate();
            }
        });
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
                    mEditText.setVisibility(GONE);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.preTranslate(mEnv.getDistanceX(), mEnv.getDistanceY());
        matrix.preScale(mEnv.getScale(), mEnv.getScale(), mEnv.getScalePivotX(), mEnv.getScalePivotY());
//        matrix.preScale(mEnv.getScale(), mEnv.getScale());
        canvas.concat(matrix);
        for (int i = 0; i < mNodeViews.size(); i++) {
            mNodeViews.get(i).onDraw(canvas);
        }

        for (int i = 0; i < mLines.size(); i++) {
            mLines.get(i).drawSelf(canvas);
        }
        canvas.restore();
    }

    public void replaceCurrentView(NodeView currentView) {
        if (mCurrentView != null) {
            mCurrentView.setState(STATE_NORMAL);
        }
        currentView.setState(STATE_FOCUS);
        mCurrentView = currentView;
    }

    public void setCurrentViewNormal() {
        mCurrentView.setState(STATE_NORMAL);
    }

    public NodeView addNodeView(NodeView parentNodeView, Class borderStyle, String text) {
        NodeView nodeView = new NodeView(this, mEnv);
        parentNodeView.getChildNodeViews().add(nodeView);
        nodeView.setParentNode(parentNodeView);
        nodeView.setIndex(parentNodeView.getChildNodeViews().size() - 1);
        nodeView.setLevel(parentNodeView.getLevel() + 1);
        nodeView.setText(text);
        nodeView.setBorder(borderStyle);
        addNodeView(parentNodeView, nodeView);
        return nodeView;
    }

    private void addNodeView(NodeView nodeViewA, NodeView nodeViewB) {
        replaceCurrentView(nodeViewB);
        mNodeViews.add(nodeViewB);
        Line line = new Line(nodeViewA, nodeViewB);
        mLines.add(line);
    }

    public void showSoftKeyboard(View view, Context mContext) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showEditView() {
        RectF rect = mEnv.getTransRect(mCurrentView.getViewRect());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (rect.right - rect.left), (int) (rect.bottom - rect.top));
        params.leftMargin = (int) rect.left;
        params.topMargin = (int) rect.top;
        mEditText.setLayoutParams(params);
        mEditText.setVisibility(VISIBLE);
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEditText.requestFocus();
                showSoftKeyboard(mEditText, getContext());
            }
        }, 200);
        mCurrentView.setText("");
    }

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
            mPaint.setColor(mNodeViewB.getBorder().getColor());
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
