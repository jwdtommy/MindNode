package dd.com.mindnode;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hyena.framework.annotation.AttachViewId;
import com.hyena.framework.app.fragment.BaseUIFragment;

public class MainFragment extends BaseUIFragment {
    @AttachViewId(R.id.iv_theme)
    ImageView mIvTheme;
    @Override
    public View onCreateViewImpl(Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreatedImpl(View view, Bundle savedInstanceState) {
        super.onViewCreatedImpl(view, savedInstanceState);
        mIvTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectThemeDialog dialog= (SelectThemeDialog) FrameDialog.createBottomDialog(getActivity(),SelectThemeDialog.class,0,null);
                dialog.show(MainFragment.this);
            }
        });
    }
}
