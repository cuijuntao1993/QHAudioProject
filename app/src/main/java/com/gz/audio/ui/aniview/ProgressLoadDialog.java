package com.gz.audio.ui.aniview;

import android.content.Context;
import android.view.View;

/**
 * Created by 键盘 on 2016/1/26.
 *
 */
public class ProgressLoadDialog extends BaseProgressDialog{
    private int mMaxProgress;
    private AnnularView mDeterminateView;
    private boolean mIsAutoDismiss = true;//加载完是否自动dismiss

    @Override
    protected View setContentView() {
        mDeterminateView = new AnnularView(getContext());
        return mDeterminateView;
    }

    public ProgressLoadDialog(Context context) {
        super(context);
    }

    /**
     * 进度条的最大值
     */
    public ProgressLoadDialog setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        mDeterminateView.setMax(maxProgress);
        return this;
    }

    /**
     * 进度条到最大值时,dialog是否自动关闭
     */
    public ProgressLoadDialog setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public void setProgress(int progress) {
        if (mDeterminateView != null) {
            mDeterminateView.setProgress(progress);
            if (mIsAutoDismiss && progress >= mMaxProgress) {
                dismiss();
            }
        }
    }
}
