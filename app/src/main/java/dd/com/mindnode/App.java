package dd.com.mindnode;

import com.hyena.framework.app.fragment.SafeFragment;
import com.hyena.framework.clientlog.LogUtil;
import com.hyena.framework.clientlog.Logger;
import com.hyena.framework.config.FrameworkConfig;
import com.hyena.framework.network.executor.UrlConnectionHttpExecutor;
import com.hyena.framework.servcie.ServiceProvider;
import com.hyena.framework.utils.BaseApp;

import java.io.File;
public class App extends BaseApp {

    @Override
    public void initApp() {
        super.initApp();
        // 开启日志功能
        LogUtil.setDebug(true);
        SafeFragment.DEBUG = true;
        LogUtil.setLevel(Logger.DEBUG);
        // 初始化底层框架
        FrameworkConfig.init(this).setAppRootDir(getRootDir()).setDebug(true)
                .setHttpExecutor(new UrlConnectionHttpExecutor());
        // 注册应用系统服务
        ServiceProvider.getServiceProvider().registServiceManager(new BoxServiceManager());
    }

    /**
     * 获得引用根路径
     *
     * @return
     */
    public static File getRootDir() {
        File rootDir = new File(android.os.Environment
                .getExternalStorageDirectory(), "mindnode");
        return rootDir;
    }
}
