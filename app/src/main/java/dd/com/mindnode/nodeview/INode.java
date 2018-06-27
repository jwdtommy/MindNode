package dd.com.mindnode.nodeview;

/**
 * @J.Tommy 一个节点，可以被拖拽，也可以连接其他节点
 */
interface INode extends IDraggable {
    void onConnected(INode parentNode);

}
