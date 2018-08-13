package dd.com.mindnode.nodeview.border;

import dd.com.mindnode.nodeview.MindNodeEnv;
import dd.com.mindnode.nodeview.NodeView;

public abstract class BaseBorder implements IBorder {
    protected MindNodeEnv mEnv;
    protected NodeView mNodeView;
    protected int mColor;

    public BaseBorder(MindNodeEnv env, NodeView nodeView) {
        mEnv = env;
        mNodeView = nodeView;
        int level = nodeView.getLevel();
        int index = nodeView.getIndex();

        if (level == 0) {
            mColor = mEnv.getCurrentColorStyle().mainNodeBorderColor;
        } else if (level == 1) {
            mColor = mEnv.getCurrentColorStyle().lineColors[index % mEnv.getCurrentColorStyle().lineColors.length];
        } else {
            mColor = nodeView.getParentNode().getBorder().getColor();
        }
    }

    @Override
    public int getColor() {
        return mColor;
    }
}
