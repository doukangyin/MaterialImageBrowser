package mobile.dky.com.imagepreview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

/**
 * Created by 11 on 2016/6/2.
 * <p/>
 * 创建图片查看界面
 */
public class ImagePreviewBuilder {

    private Context mContext;

    private View[] mViews;

    private int mCurrentPage;

    private String[] mImageUrls;

    private String mTransitionName;

    private boolean mVisible;

    /**
     * @param mContext Context
     */
    public ImagePreviewBuilder(Context mContext) {
        this.mContext = mContext;
    }

    public ImagePreviewBuilder setViews(View[] views) {
        mViews = views;
        return this;
    }

    public ImagePreviewBuilder setUrl(String... urls) {
        mImageUrls = urls;
        return this;
    }

    public ImagePreviewBuilder setCurrentPosition(int pos) {
        mCurrentPage = pos;
        return this;
    }

    public ImagePreviewBuilder setTransitionName(String transitionName) {
        mTransitionName = transitionName;
        return this;
    }

    public ImagePreviewBuilder setPageVisible(boolean visible) {
        mVisible = visible;
        return this;
    }


    public void build() {
        int size = mImageUrls.length;
        if (mImageUrls.length > 0 && mViews.length > 0) {
            Intent intent = new Intent(mContext, ImagePreviewActivity.class);
            intent.putExtra("urls", mImageUrls);
            intent.putExtra("setCurrentPosition", mCurrentPage);
            intent.putExtra("visible", mVisible);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && !TextUtils.isEmpty(mTransitionName)) {
                intent.putExtra("transitionName", mTransitionName);
                Pair[] pairs = new Pair[size];
                for (int i = 0; i < size; i++) {
                    mViews[i].setTransitionName(mTransitionName + i);
                    Pair pair = Pair.create(mViews[i], mTransitionName + i);
                    pairs[i] = pair;
                }
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs);
                mContext.startActivity(intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        }
    }

}
