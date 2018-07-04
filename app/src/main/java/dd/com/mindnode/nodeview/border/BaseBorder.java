package dd.com.mindnode.nodeview.border;

import dd.com.mindnode.nodeview.MindNodeEnv;
import dd.com.mindnode.nodeview.NodeView;

public abstract class BaseBorder implements IBorder {
    protected MindNodeEnv mEnv;
    protected NodeView mNodeView;

    public BaseBorder(MindNodeEnv env, NodeView nodeView) {
        mEnv = env;
        mNodeView = nodeView;
    }

}
