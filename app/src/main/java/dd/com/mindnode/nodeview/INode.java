package dd.com.mindnode.nodeview;

import android.graphics.Canvas;
import android.view.ScaleGestureDetector;

/**
 * @J.Tommy 一个节点，可以被拖拽，也可以连接其他节点
 */
interface INode extends IDraggable {
    void onConnected(INode parentNode);

    int[] getConnectStartPoint();

    int[] getConnectEndPoint();

    boolean onScale(ScaleGestureDetector detector);

    void onDraw(Canvas canvas);
}
