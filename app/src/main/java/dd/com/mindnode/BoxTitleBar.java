/**
 * Copyright (C) 2015 The AndroidRCStudent Project
 */
package dd.com.mindnode;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyena.framework.app.widget.CommonTitleBar;
import com.hyena.framework.utils.UiThreadHandler;

/**
 * @author Fanjb
 * @name 老师端页面通用头部
 * @date 2015年9月1日
 */
public class BoxTitleBar extends CommonTitleBar {

    private TextView mLeftTxt;
    private ImageView mTitleLeftIcon;
    private ImageView mTitleRightIcon;
    private ImageView mRightMessageIcon;
    private View leftRedTips;

    public BoxTitleBar(Context context) {
        super(context);
    }

    public BoxTitleBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void init() {
        super.init();
        reCreateTitlePanel();
        initLeftText();
        initRightMessageIcon();
        //标题
        mTitleTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mSubTitleTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        //更多
        mMenuTxtBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) mSubTitleTxt.getLayoutParams();
        params.topMargin = com.hyena.framework.utils.UIUtils.dip2px(0);

        RelativeLayout.LayoutParams tipsParams = (RelativeLayout.LayoutParams) mTipView.getLayoutParams();
        tipsParams.width = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 6);
        tipsParams.height = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 6);
        tipsParams.topMargin = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 15);
        tipsParams.rightMargin = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 13);


    }
    protected void initRightMessageIcon(){
        //提示
        mRightMessageIcon = new ImageView(getContext());
        int size = com.hyena.framework.animation.utils.UIUtils.dp2px(getContext(), 14);
        RelativeLayout.LayoutParams barIconParams = new RelativeLayout.LayoutParams(
                size, size);
        barIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = com.hyena.framework.animation.utils.UIUtils.dp2px(getContext(), 5);
        barIconParams.rightMargin = margin;
        barIconParams.topMargin = margin;
        addView(mRightMessageIcon, barIconParams);
        mRightMessageIcon.setVisibility(View.GONE);
    }
    private void initLeftText() {
        LinearLayout leftLayout = new LinearLayout(getContext());
        leftLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams leftLayoutParams = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(leftLayout, leftLayoutParams);

        TextView leftText = new TextView(getContext());
        int padding = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 15);
        int rightPadding = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 2);
        leftText.setPadding(padding, padding, rightPadding, padding);
        leftText.setTextColor(Color.BLACK);
        mLeftTxt = leftText;
        leftLayout.addView(leftText);
        View redTips =new View(getContext());
        LinearLayout.LayoutParams redParams = new LinearLayout.LayoutParams(
                com.hyena.framework.utils.UIUtils.dip2px(getContext(), 10),
                com.hyena.framework.utils.UIUtils.dip2px(getContext(), 10));
        redParams.topMargin=com.hyena.framework.utils.UIUtils.dip2px(getContext(), 15);
        redTips.setVisibility(View.GONE);
        leftLayout.addView(leftRedTips=redTips,redParams);

    }

    private void reCreateTitlePanel() {
        //Remove titlePanel
        removeView((View) mTitleTxt.getParent());
        //初始化标题
        LinearLayout titlePanel = new LinearLayout(getContext());
        titlePanel.setOrientation(LinearLayout.VERTICAL);

        LinearLayout titleMainPanel = new LinearLayout(getContext());
        titleMainPanel.setOrientation(LinearLayout.HORIZONTAL);
        titleMainPanel.setVerticalGravity(Gravity.CENTER_VERTICAL);
        //add title left
        LinearLayout.LayoutParams titleLeftParams = new LinearLayout.LayoutParams(
                com.hyena.framework.utils.UIUtils.dip2px(getContext(), 30),
                com.hyena.framework.utils.UIUtils.dip2px(getContext(), 30));
        titleLeftParams.rightMargin = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 5);
        titleMainPanel.addView(mTitleLeftIcon = getTitleLeftImage(), titleLeftParams);
        int titleLeftPadding = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 1);
        mTitleLeftIcon.setPadding(titleLeftPadding, titleLeftPadding, titleLeftPadding, titleLeftPadding);
        mTitleLeftIcon.setVisibility(View.GONE);
        //add title center
        titleMainPanel.addView(mTitleTxt = getTitleTextView());
        //add title Right
        LinearLayout.LayoutParams titleRightParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleRightParams.leftMargin = com.hyena.framework.utils.UIUtils.dip2px(getContext(), 5);
        titleMainPanel.addView(mTitleRightIcon = getTitleRightImage(), titleRightParams);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_HORIZONTAL;
        titlePanel.addView(titleMainPanel, titleParams);

        //初始化副标题
        LinearLayout.LayoutParams subTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subTitleParams.gravity = Gravity.CENTER_HORIZONTAL;
        subTitleParams.topMargin = com.hyena.framework.animation.utils.UIUtils.dp2px(getContext(), 10);
        titlePanel.addView(mSubTitleTxt = getSubTitleTextView(), subTitleParams);
        mSubTitleTxt.setVisibility(View.GONE);

        //添加标题栏
        RelativeLayout.LayoutParams titlePanelParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titlePanelParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(titlePanel, titlePanelParams);
        //add line
        View lineView = new View(getContext());
        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(lineView, lineParams);
    }

    @Override
    protected TextView getSubTitleTextView() {
        TextView textView = new TextView(getContext());
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setSingleLine(true);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxWidth(com.hyena.framework.animation.utils.UIUtils.dp2px(getContext(), 200));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        return textView;
    }

    @Override
    protected TextView getTitleTextView() {
        TextView textView = new TextView(getContext());
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(true);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxWidth(com.hyena.framework.animation.utils.UIUtils.dp2px(getContext(), 200));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        return textView;
    }

    private ImageView getTitleLeftImage() {
        ImageView imageView = new ImageView(getContext());
        return imageView;
    }

    private ImageView getTitleRightImage() {
        ImageView imageView = new ImageView(getContext());
        return imageView;
    }

    @Override
    public void setTitle(final String title) {
    }

    public TextView getTitleView() {
        return mTitleTxt;
    }

    public ImageView getTitleRightIcon() {
        return mTitleRightIcon;
    }

    public void setSubTitle(final String subTitle) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                mSubTitleTxt.setVisibility(VISIBLE);
                if (mSubTitleTxt != null) {
                    mSubTitleTxt.setText(subTitle);
                }
            }
        });
    }

    public void setSubTitle(final String subTitle, final int color) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                mSubTitleTxt.setVisibility(VISIBLE);
                if (mSubTitleTxt != null) {
                    mSubTitleTxt.setText(subTitle);
                    mSubTitleTxt.setTextColor(color);
                }
            }
        });
    }

    private PopupWindow mMenuPopWindows;

    /**
     * 设置返回键是否可见
     *
     * @param visible
     */
    public void setBackBtnVisible(final boolean visible) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                mBackBtn.setVisibility(visible ? VISIBLE : GONE);
            }
        });
    }

    public void setTitleBarLeftTxt(final String text, final View.OnClickListener listener) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mLeftTxt.setVisibility(VISIBLE);
                mLeftTxt.setText(text);
                if (listener != null) {
                    mLeftTxt.setOnClickListener(listener);
                }
            }
        });
    }

    public TextView getTitleBarLeftTxtView() {
        return mLeftTxt;
    }

    public void setTitleBarLeftVisible(final boolean visible) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                mLeftTxt.setVisibility(visible ? VISIBLE : GONE);
            }
        });
    }
   public void setRightTextVisible(final boolean visible){
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                getRightTextView().setVisibility(visible?VISIBLE:GONE);
            }
        });
   }
    /**
     * 获得更多图片
     *
     * @return
     */
    public void setTitleMore(final int resId, final View.OnClickListener clickListener) {
        setMenuMoreImage(resId, clickListener);
    }

    /**
     * 设置右侧图片是否可用
     *
     * @param isEnable
     */
    public void setRightTextEnable(boolean isEnable) {
        setTitleMoreEnable(isEnable);
    }

    /**
     * 右侧点击动作
     *
     * @param txt
     * @param clickListener
     */
    public void setRightText(final String txt, final View.OnClickListener clickListener) {
        setMenuMoreTxt(txt, clickListener);
    }

    public void setRightText(final String txt) {
        setMenuMoreTxt(txt);
    }

    public TextView getRightTextView() {
        return mMenuTxtBtn;
    }

    public ImageView getRightMenuBtn() {
        return mMenuImageBtn;
    }

    public void setTitleBarLeftIcon(String url, int defaultRes) {
        setTitleBarLeftIcon(url, defaultRes, null);
    }


    public void setTitleBarLeftIcon(String url, int defaultRes, View.OnClickListener listener) {
    }

    public void setTitleBarLeftIconVisible(int visible) {
        mTitleLeftIcon.setVisibility(visible);
    }

    /**
     * 设置右侧是否有提醒消息
     *
     * @param visibility
     */
    public void setRightMessageVisibility(final int visibility) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mTipView.setVisibility(visibility);
            }
        });
    }

    public void setRightTextColor(final int color) {
        UiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mMenuTxtBtn.setTextColor(color);
            }
        });

    }
    public void setRightMessageIcon(final int messageIcon){
        mRightMessageIcon.setVisibility(VISIBLE);
        mRightMessageIcon.setImageResource(messageIcon);
    }
    public void hideRightMessageIcon(){
        mRightMessageIcon.setVisibility(GONE);
    }

    public View getLeftRedTips(){
        return  leftRedTips;
    }
}
