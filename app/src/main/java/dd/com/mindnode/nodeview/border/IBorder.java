package dd.com.mindnode.nodeview.border;

import android.graphics.Canvas;

public interface IBorder {
    void onDraw(Canvas canvas);

    int[] getConnectStartPoint();
    int[] getConnectEndPoint();
}
