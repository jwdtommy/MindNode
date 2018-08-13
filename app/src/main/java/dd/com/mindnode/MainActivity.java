package dd.com.mindnode;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hyena.framework.app.activity.NavigateActivity;
import com.hyena.framework.utils.UiThreadHandler;

public class MainActivity extends NavigateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                MainFragment fragment = (MainFragment) Fragment.instantiate(MainActivity.this,
                        MainFragment.class.getName());
                showFragment(fragment);
            }
        });
    }
}
