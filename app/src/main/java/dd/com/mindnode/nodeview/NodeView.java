package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dd.com.mindnode.R;

public class NodeView extends RelativeLayout implements INode {
    private INode mParentNode;
    private boolean mIsDragging;
    private Node mNode;
    private Rect mBorderRect = new Rect();
    private List<NodeView> mChildNodeViews = new ArrayList<>();
    private MapView mMapView;
    private TextView mEditText;
    private ImageView mIvAddNode;
    private View mUnderLineBorder;
    private boolean isFirst;
    private int srcLeft;
    private int srcTop;

    private float mScaleFactor;

    public NodeView(Context context) {
        super(context);
        init(false);
    }

    public NodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(true);
    }

    public NodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(true);
    }

    private void init(boolean isFirst) {
        this.isFirst = isFirst;
        mEditText = new TextView(getContext());
        mEditText.setId(View.generateViewId());
        mEditText.setGravity(Gravity.CENTER);
        mEditText.setTextSize(14);
        mEditText.setText("主题  ");
        mEditText.setTextColor(0xff333333);
        mEditText.setBackgroundResource(0);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, -2);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEditText.setLayoutParams(p);
        addView(mEditText);

        mIvAddNode = new ImageView(getContext());
        mIvAddNode.setId(View.generateViewId());
        mIvAddNode.setImageResource(R.drawable.icon_add_topic);
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(-2, -2);
        p1.addRule(RelativeLayout.CENTER_VERTICAL);
        p1.addRule(RelativeLayout.RIGHT_OF, mEditText.getId());
        p1.leftMargin = 90;
        mIvAddNode.setLayoutParams(p1);
        addView(mIvAddNode);

        mUnderLineBorder = new View(getContext());
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(-2, 10);
        p3.addRule(RelativeLayout.ALIGN_LEFT, mEditText.getId());
        p3.addRule(RelativeLayout.ALIGN_RIGHT, mIvAddNode.getId());
        p3.addRule(RelativeLayout.BELOW, mIvAddNode.getId());
        p3.topMargin = 15;
        mUnderLineBorder.setLayoutParams(p3);
        mUnderLineBorder.setBackgroundColor(Consts.LINE_COLORS[1]);
        addView(mUnderLineBorder);

        if (isFirst) {
            setBackgroundResource(R.drawable.border_underline_8be9fd);
            mUnderLineBorder.setVisibility(GONE);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            mUnderLineBorder.setVisibility(VISIBLE);
        }

        mIvAddNode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createChildNode();
            }
        });
    }

    public void setText(String text) {
        mEditText.setText(text + "");
    }

    private void createChildNode() {
        NodeView nodeView = new NodeView(getContext());
        int dy = 0;
        if (mChildNodeViews.size() > 0) {
            NodeView lastChildView = mChildNodeViews.get(mChildNodeViews.size() - 1);
            dy = lastChildView.getTop() + lastChildView.getHeight() + 30;
        } else {
            dy = getTop() - 100;
        }
        nodeView.setText("子主题" + (mChildNodeViews.size() + 1));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.leftMargin = getLeft() + getWidth() + 100;
        params.topMargin = dy;
        nodeView.setLayoutParams(params);
        mMapView = (MapView) getParent();
        mMapView.addNodeView(this, nodeView);
        mChildNodeViews.add(nodeView);
    }

    public void setNode(Node node) {
        mNode = node;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
    }

    private void drawBorder(Canvas canvas) {

    }

    private void drawContent(Canvas canvas) {

    }


    @Override
    public void onStartDrag() {
        onStartDragImpl();
        srcLeft = ((FrameLayout.LayoutParams) getLayoutParams()).leftMargin;
        srcTop = ((FrameLayout.LayoutParams) getLayoutParams()).topMargin;
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
        int[] xy = new int[2];
        if (isFirst) {
            xy[0] = (int) (getX() + getWidth());
            xy[1] = (int) (getY() + getHeight() / 2);
        } else {
            xy[0] = (int) (getX() + getWidth());
            xy[1] = (int) (getY() + getHeight());
        }
        return xy;
    }

    @Override
    public int[] getConnectEndPoint() {
        int[] xy = new int[2];
        xy[0] = (int) getX();
        xy[1] = (int) (getY() + getHeight() - 5);
        return xy;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor = detector.getScaleFactor();
        setPivotX(0);
        setPivotY(0);
        setScaleX(mScaleFactor);
        setScaleY(mScaleFactor);
        return true;
    }

    private void onStartDragImpl() {
        mIsDragging = true;
    }

    private void onDraggingImpl(int left, int top, int moveX, int moveY) {
//        ((FrameLayout.LayoutParams)getLayoutParams()).leftMargin=left;
//        ((FrameLayout.LayoutParams)getLayoutParams()).topMargin=top;
    }

    private void onStopDragImpl(float xvel, float yvel) {
        mIsDragging = false;
        FrameLayout.LayoutParams st = (FrameLayout.LayoutParams) getLayoutParams();
        st.gravity = Gravity.NO_GRAVITY;
        st.leftMargin = getLeft();
        st.topMargin = getTop();
//        setLeft(0);
//        setTop(0);
        setLayoutParams(st);
    }

    private void onConnectImpl(INode parentNode) {
        mParentNode = parentNode;
    }

}
