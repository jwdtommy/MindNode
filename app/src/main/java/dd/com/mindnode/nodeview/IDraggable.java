package dd.com.mindnode.nodeview;

interface IDraggable {
    void onStartDrag(int x, int y);

    void onDragging(int moveX, int moveY);

    void onStopDrag(int x, int y);
}
