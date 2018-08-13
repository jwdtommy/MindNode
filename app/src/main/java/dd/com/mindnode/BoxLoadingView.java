/**
 * Copyright (C) 2015 The AndroidRCStudent Project
 */
package dd.com.mindnode;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyena.framework.app.widget.LoadingView;

/**
 * @name 速算总动员老师端通用页面加载动画视图
 * @author Fanjb
 * @date 2015年9月1日
 */
public class BoxLoadingView extends LoadingView {

	private ImageView mLoadingImg;
	private TextView mLoadingHintTxt;

	public BoxLoadingView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.layout_common_loading, this);
	}

	@Override
	public void showLoading(final String hint) {
	}
}
