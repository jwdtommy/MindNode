package dd.com.mindnode.nodeview.border;

import android.graphics.Canvas;
import android.graphics.Paint;

import dd.com.mindnode.nodeview.MindNodeEnv;
import dd.com.mindnode.nodeview.NodeView;

public class LineBorder extends BaseBorder {
    private Paint mBorderPaint;

    public LineBorder(MindNodeEnv env, NodeView nodeView) {
        super(env, nodeView);
        mBorderPaint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mBorderPaint.reset();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setColor(0xff8be9fd);
        canvas.drawRect(mNodeView.getLeft(), mNodeView.getBottom() - 10, mNodeView.getRight(), mNodeView.getBottom(), mBorderPaint);
    }

    @Override
    public int[] getConnectStartPoint() {
        int[] xy = new int[2];
        xy[0] = (int) (mNodeView.getRight());
        xy[1] = (int) (mNodeView.getBottom());
        return xy;
    }

    @Override
    public int[] getConnectEndPoint() {
        int[] xy = new int[2];
        xy[0] = (int) mNodeView.getLeft();
        xy[1] = (int) (mNodeView.getBottom() - 5);
        return xy;
    }
}
