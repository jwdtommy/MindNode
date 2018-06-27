package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NodeView extends View implements INode {
    private INode mParentNode;
    private boolean mIsDragging;
    private Node mNode;
    private Rect mBorderRect = new Rect();

    public NodeView(Context context) {
        super(context);
        init();
    }

    public NodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setNode(Node node) {
        mNode = node;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
    }

    private void DrawBorder(Canvas canvas){

    }

    @Override
    public void onStartDrag(int x, int y) {
        onStartDragImpl(x, y);
    }

    @Override
    public void onDragging(int moveX, int moveY) {
        onDraggingImpl(moveX, moveY);
    }

    @Override
    public void onStopDrag(int x, int y) {
        onStopDragImpl(x, y);
    }

    @Override
    public void onConnected(INode parentNode) {
        onConnectImpl(parentNode);
    }

    private void onStartDragImpl(int x, int y) {
        mIsDragging = true;
    }

    private void onDraggingImpl(int moveX, int moveY) {

    }

    private void onStopDragImpl(int x, int y) {
        mIsDragging = false;
    }

    private void onConnectImpl(INode parentNode) {
        mParentNode = parentNode;
    }

}
