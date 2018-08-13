package dd.com.mindnode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hyena.framework.app.fragment.AnimType;
import com.hyena.framework.app.fragment.DialogFragment;

/**
 * Created by apolunor on 16/6/2.
 */
public abstract class FrameDialog extends DialogFragment<UIFragmentHelper> {

    private Activity mActivity;
    private Bundle   mBundle;
    private int mMargin = 0;

    public static FrameDialog create(Activity activity, Class<?> cls, Bundle bundle) {
        final FrameDialog fragment = create(activity, cls, 20, bundle);
        fragment.setBundle(bundle);
        return fragment;
    }

    public static FrameDialog create(Activity activity, Class<?> cls, int margin, Bundle bundle) {
        return createCenterDialog(activity, cls, margin, bundle);
    }

    public static FrameDialog create(Activity activity, Class<?> cls, int horizonalMargin, int verticalMargin, AnimStyle animStyle, Bundle bundle) {
        final FrameDialog fragment = DialogFragment.newFragment(activity, cls);
        fragment.setAnimationType(AnimType.ANIM_NONE);
        fragment.setSlideable(false);
        fragment.setAnimStyle(animStyle);
        fragment.setTitleStyle(STYLE_NO_TITLE);
        fragment.setHorizontalMarginDp(horizonalMargin);
        fragment.setCanceledOnTouchOutside(true);
        fragment.setAlign(RelativeLayout.ALIGN_PARENT_TOP);
        fragment.setMargin(verticalMargin);
        fragment.setActivityIn(activity);
        fragment.setActivity(activity);
        View view = fragment.onCreateView(bundle);
        fragment.setContent(view);
        return fragment;
    }

    public static FrameDialog createCenterDialog(Activity activity, Class<?> cls, int margin, Bundle bundle) {
        final FrameDialog fragment = DialogFragment.newFragment(activity, cls);
        fragment.setAnimationType(AnimType.ANIM_NONE);
        fragment.setSlideable(false);
        fragment.setAnimStyle(AnimStyle.STYLE_DROP);
        fragment.setTitleStyle(STYLE_NO_TITLE);
        fragment.setHorizontalMarginDp(margin);
        fragment.setCanceledOnTouchOutside(true);
        fragment.setActivityIn(activity);
        fragment.setActivity(activity);
        View view = fragment.onCreateView(bundle);
        //        view.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //                    fragment.dismiss();
        //                }
        //                return false;
        //            }
        //        });
        fragment.setContent(view);
        return fragment;
    }

    public static FrameDialog createBottomDialog(Activity activity, Class<?> cls, int margin, Bundle bundle) {
        final FrameDialog fragment = DialogFragment.newFragment(activity, cls);
        fragment.setAnimationType(AnimType.ANIM_NONE);
        fragment.setSlideable(false);
        fragment.setAnimStyle(AnimStyle.STYLE_BOTTOM);
        fragment.setTitleStyle(STYLE_NO_TITLE);
        fragment.setHorizontalMarginDp(margin);
        fragment.setCanceledOnTouchOutside(true);
        fragment.setActivityIn(activity);
        fragment.setActivity(activity);
        fragment.setAlign(RelativeLayout.ALIGN_PARENT_BOTTOM);
        View view = fragment.onCreateView(bundle);
        //        view.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //                    fragment.dismiss();
        //                }
        //                return false;
        //            }
        //        });
        fragment.setContent(view);
        return fragment;
    }

    public abstract View onCreateView(Bundle bundle);

    public void setActivityIn(Activity activity) {
        this.mActivity = activity;
    }

    public Activity getActivityIn() {
        return this.mActivity;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public OnDialogDismissListener mOnDialogDismissListener;

    public interface OnDialogDismissListener {
        void onDialogDismiss();
    }

    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        mOnDialogDismissListener = onDialogDismissListener;
    }

    @Override
    public void finish() {
        if (mOnDialogDismissListener != null) {
            mOnDialogDismissListener.onDialogDismiss();
        }
        super.finish();
    }

    public void setHorizontalMarginDp(int margin) {
        mMargin = margin;
    }

    @Override
    protected int getWinsHorizontalMarginDp() {
        return mMargin;
    }

    protected void initAllViews() {
        super.initAllViews();
        this.mContentPanel.setBackgroundColor(0);
    }

    protected void initTitleBar() {
    }

    protected void initContent(View contentView) {
        super.initContent(contentView);
    }

    protected void initCtrlPanel() {
    }
}
