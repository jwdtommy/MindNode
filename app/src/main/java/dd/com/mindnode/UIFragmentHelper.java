/**
 * Copyright (C) 2015 The AndroidRCTeacher Project
 */
package dd.com.mindnode;

import com.hyena.framework.app.fragment.BaseUIFragment;
import com.hyena.framework.app.fragment.BaseUIFragmentHelper;
import com.hyena.framework.app.fragment.ViewBuilder;

/**
 * @author Fanjb
 * @name 通用视图帮助类
 * @date 2015年9月1日
 */
public class UIFragmentHelper extends BaseUIFragmentHelper {

    public UIFragmentHelper(BaseUIFragment<?> fragment) {
        super(fragment);
    }

    @Override
    public ViewBuilder getViewBuilder() {
        return new BoxViewBuilder();
    }

    @Override
    public void setVisibleToUser(boolean visible) {
        super.setVisibleToUser(visible);
    }

    public BoxTitleBar getTitleBar() {
        return (BoxTitleBar) getBaseUIFragment().getTitleBar();
    }

    public BoxEmptyView getEmptyView() {
        return (BoxEmptyView) getBaseUIFragment().getEmptyView();
    }

    public BoxLoadingView getLoadingView() {
        return (BoxLoadingView) getBaseUIFragment().getLoadingView();
    }
}
