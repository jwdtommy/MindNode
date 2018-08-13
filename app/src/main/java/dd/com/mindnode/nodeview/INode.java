package dd.com.mindnode.nodeview;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.ScaleGestureDetector;

import java.util.List;

import dd.com.mindnode.nodeview.border.IBorder;

/**
 * @J.Tommy 一个节点，可以被拖拽，也可以连接其他节点
 */
public interface INode extends IDraggable {
    int[] getConnectStartPoint();

    int[] getConnectEndPoint();

    boolean onScale(ScaleGestureDetector detector);

    void onDraw(Canvas canvas);

    int getLeft();

    int getRight();

    int getTop();

    int getBottom();

    RectF getViewRect();

    RectF getTextRect();

    List<NodeView> getChildViews();

    NodeView getLastView();

    IBorder getBorder();
}