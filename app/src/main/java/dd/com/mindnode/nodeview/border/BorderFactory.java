package dd.com.mindnode.nodeview.border;

import java.lang.reflect.Constructor;

import dd.com.mindnode.nodeview.MindNodeEnv;
import dd.com.mindnode.nodeview.NodeView;

public class BorderFactory {

    public static <T extends IBorder> T create(Class<T> clazz, MindNodeEnv env, NodeView nodeView) {
        try {
            Constructor c1 = clazz.getDeclaredConstructor(new Class[]{MindNodeEnv.class, NodeView.class});
            c1.setAccessible(true);
            T object = (T) c1.newInstance(new Object[]{env, nodeView});
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
