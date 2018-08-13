package dd.com.mindnode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyena.framework.annotation.AttachViewId;
import com.hyena.framework.utils.UIUtils;

public class SelectThemeDialog extends FrameDialog {
    @AttachViewId(R.id.viewPager)
    private ViewPager mViewPager;
    @AttachViewId(R.id.viewPagerIndicator)
    private CirclePageIndicator mCirclePageIndicator;
    private static final int[] PICS = new int[]{R.drawable.theme1, R.drawable.theme2, R.drawable.theme3, R.drawable.theme4, R.drawable.theme5};

    @Override
    public View onCreateView(Bundle bundle) {
        return View.inflate(getActivityIn(), R.layout.dialog_select_theme, null);
    }

    @Override
    public void onViewCreatedImpl(View view, Bundle savedInstanceState) {
        super.onViewCreatedImpl(view, savedInstanceState);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return PICS.length;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(PICS[position]);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        });
        mCirclePageIndicator.setRealCount(PICS.length);
        mCirclePageIndicator.setViewPager(mViewPager);
        mCirclePageIndicator.setStrokeColor(0xff333333);
        mCirclePageIndicator.setStrokeWidth(UIUtils.dip2px(0.5f));
        mCirclePageIndicator.setFillColor(Color.WHITE);
        mCirclePageIndicator.setRadius(UIUtils.dip2px(3));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }
}
