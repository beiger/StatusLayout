package com.bing.statuslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 里面只有一个layout，类似ScrollView
 */
public class StatusLayout extends FrameLayout {
	public static final int STATUS_CONTENT = 0x00;
	public static final int STATUS_LOADING = 0x01;
	public static final int STATUS_EMPTY = 0x02;
	public static final int STATUS_ERROR = 0x03;
	public static final int STATUS_NO_NETWORK = 0x04;

	private static final int NULL_RESOURCE_ID = -1;
	private FrameLayout.LayoutParams mLayoutParams;
	private View mEmptyView;
	private View mErrorView;
	private View mLoadingView;
	private View mNoNetworkView;
	private View mContentView;
	private int mEmptyViewResId;
	private int mErrorViewResId;
	private int mLoadingViewResId;
	private int mNoNetworkViewResId;
	private int mContentViewResId;
	private int mViewStatus;
	private LayoutInflater mInflater;
	private OnClickListener mOnEmptyClickListener;
	private OnClickListener mOnErrorClickListener;
	private OnClickListener mOnLoadingClickListener;
	private OnClickListener mOnNoNetworkClickListener;

	public StatusLayout(Context context) {
		this(context, null);
	}

	public StatusLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		final TypedArray a =
				context.obtainStyledAttributes(attrs, R.styleable.StatusLayout, defStyleAttr, 0);

		mEmptyViewResId = a.getResourceId(R.styleable.StatusLayout_emptyView, R.layout.layout_empty);
		mErrorViewResId = a.getResourceId(R.styleable.StatusLayout_errorView, R.layout.layout_no_network);
		mLoadingViewResId = a.getResourceId(R.styleable.StatusLayout_loadingView, R.layout.layout_loading);
		mNoNetworkViewResId = a.getResourceId(R.styleable.StatusLayout_noNetworkView, R.layout.layout_no_network);
		mContentViewResId = a.getResourceId(R.styleable.StatusLayout_contentView, NULL_RESOURCE_ID);
		mInflater = LayoutInflater.from(getContext());
		a.recycle();

		mLayoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		mLayoutParams.gravity = Gravity.CENTER;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mContentViewResId != NULL_RESOURCE_ID) {
			mContentView = findViewById(mContentViewResId);
		} else {
			if (getChildCount() > 1) {
				try {
					throw new StatusException("content is not a single View or Layout");
				} catch (StatusException e) {
					e.printStackTrace();
				}
			}
			mContentView = getChildAt(0);
		}
	}

	static class StatusException extends Exception {
		StatusException(String message) {
			super(message);
		}
	}

	/**
	 * 获取当前状态
	 */
	public int getViewStatus() {
		return mViewStatus;
	}

	public void setOnEmptyClickListener(OnClickListener onEmptyClickListener) {
		mOnEmptyClickListener = onEmptyClickListener;
	}

	public void setOnErrorClickListener(OnClickListener onErrorClickListener) {
		mOnErrorClickListener = onErrorClickListener;
	}

	public void setOnLoadingClickListener(OnClickListener onLoadingClickListener) {
		mOnLoadingClickListener = onLoadingClickListener;
	}

	public void setOnNoNetworkClickListener(OnClickListener onNoNetworkClickListener) {
		mOnNoNetworkClickListener = onNoNetworkClickListener;
	}

	public void showViewByStatus(int viewStatus) {
		mViewStatus = viewStatus;
		createAndAddView(viewStatus);
		if (null != mLoadingView) {
			mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
		}
		if (null != mEmptyView) {
			mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
		}
		if (null != mNoNetworkView) {
			mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
		}
		if (null != mContentView) {
			mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
		}
	}

	private void createAndAddView(int viewStatus) {
		switch (viewStatus) {
			case STATUS_LOADING:
				createAndAddView(mLoadingView, mLoadingViewResId, mOnLoadingClickListener);
				break;

			case STATUS_EMPTY:
				createAndAddView(mEmptyView, mEmptyViewResId, mOnEmptyClickListener);
				break;

			case STATUS_ERROR:
				createAndAddView(mErrorView, mErrorViewResId, mOnErrorClickListener);
				break;

			case STATUS_NO_NETWORK:
				createAndAddView(mNoNetworkView, mNoNetworkViewResId, mOnNoNetworkClickListener);
				break;
		}
	}

	private void createAndAddView(View view, int resId, OnClickListener listener) {
		if (view == null) {
			view = mInflater.inflate(resId, null);
			addView(view, 0, mLayoutParams);
			view.setLayoutParams(mLayoutParams);
			view.setOnClickListener(listener);
		}
	}

}
