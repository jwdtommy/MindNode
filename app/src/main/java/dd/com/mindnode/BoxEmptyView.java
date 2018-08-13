/**
 * Copyright (C) 2015 The AndroidRCStudent Project
 */
package dd.com.mindnode;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyena.framework.app.widget.EmptyView;

/**
 * @name 速算总动员老师端通用空页面视图
 * @author Fanjb
 * @date 2015年9月1日
 */
public class BoxEmptyView extends EmptyView {

	private ImageView mEmptyHintImg;
	private TextView mEmptyHintTxt;
	private TextView mDescText;
	private TextView mEmptyFaqText;
	private TextView mEmptyBtn;

	public BoxEmptyView(Context context) {
		super(context);
		View.inflate(getContext(), R.layout.layout_common_empty, this);
	}

	@Override
	public void showNoNetwork() {
	}

	@Override
	public void showEmpty(String errorCode, String hint) {
	}
}
