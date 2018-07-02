package dd.com.mindnode.nodeview;

interface IDraggable {
    void onStartDrag();

    void onDragging(int left, int top, int moveX, int moveY);

    void onStopDrag(float xvel, float yvel);
}
