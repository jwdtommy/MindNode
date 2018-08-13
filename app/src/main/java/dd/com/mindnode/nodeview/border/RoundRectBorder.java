package dd.com.mindnode.nodeview.border;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import dd.com.mindnode.nodeview.MindNodeEnv;
import dd.com.mindnode.nodeview.NodeView;

public class RoundRectBorder extends BaseBorder {
    private Paint mBorderPaint;

    public RoundRectBorder(MindNodeEnv env, NodeView nodeView) {
        super(env, nodeView);
        mBorderPaint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mBorderPaint.reset();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(4);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mColor);
        RectF mBorderRect = new RectF();
        mBorderRect.set(mNodeView.getLeft(), mNodeView.getTop(), mNodeView.getRight(), mNodeView.getBottom());
        canvas.drawRoundRect(mBorderRect, 10, 10, mBorderPaint);
    }

    @Override
    public int[] getConnectStartPoint() {
        int[] xy = new int[2];
        xy[0] = (int) (mNodeView.getRight());
        xy[1] = (int) (mNodeView.getTop() + mNodeView.getHeight() / 2);
        return xy;
    }

    @Override
    public int[] getConnectEndPoint() {
        int[] xy = new int[2];
        xy[0] = (int) mNodeView.getLeft();
        xy[1] = (int) (mNodeView.getBottom() - 5);
        return xy;
    }

    @Override
    public int getpadding() {
        return 30;
    }
}

