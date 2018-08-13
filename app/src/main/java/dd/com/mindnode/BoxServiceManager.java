/*
 * Copyright (C) 2015 The AndroidJoke Project
 */
package dd.com.mindnode;

import com.hyena.framework.app.fragment.BaseFragment;
import com.hyena.framework.app.fragment.BaseUIFragment;
import com.hyena.framework.app.fragment.BaseUIFragmentHelper;
import com.hyena.framework.servcie.BaseServiceManager;
import com.hyena.framework.servcie.debug.DebugService;
import com.hyena.framework.servcie.debug.DebugServiceImpl;
import com.hyena.framework.servcie.navigate.UIHelperService;

public class BoxServiceManager extends BaseServiceManager {

    public BoxServiceManager() {
        super();
        //初始化框架服务
        initFrameServices();
        // 初始化所有服务
        initServices();
    }

    @Override
    public Object getService(String name) {
        return super.getService(name);
    }

    @Override
    public void releaseAll() {
        super.releaseAll();
    }

    /**
     * 初始化所有服务
     */
    private void initServices() {
        registerService(DebugService.SERVICE_NAME, new DebugServiceImpl());// 调试服务
        registerService(UIHelperService.SERVICE_NAME, new UIHelperService() {
            @Override
            public <T extends BaseUIFragmentHelper> T getUIFragmentHelper(BaseFragment fragment) {
                return (T) new UIFragmentHelper((BaseUIFragment<?>) fragment);
            }
        });
    }
}
