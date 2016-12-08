package mobile.dky.com.imagepreview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 图片预览activity
 */
public class ImagePreviewActivity extends Activity {
    //管理viewpager中的所有view用于设置transitionName
    private View[] mViews;

    // 进来时的page
    private int mInitPageNumber;

    // 存储所有图片路径
    private String[] mImageUrls;

    private HackyViewPager viewPager;

    private ProgressBar mProgressBar;

    private String mTransitionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mImageUrls = intent.getStringArrayExtra("urls");
        mInitPageNumber = intent.getIntExtra("setCurrentPosition", 0);
        mTransitionName = intent.getStringExtra("transitionName");

        setContentView(R.layout.activity_image_preview);
        mProgressBar = (ProgressBar) findViewById(R.id.image_preview_activity_progressbar);

        LinearLayout countIndicatorLayout = (LinearLayout) findViewById(R.id.image_preview_activity_count_layout);
        if (countIndicatorLayout != null) {
            countIndicatorLayout.setVisibility(intent.getBooleanExtra("visible", true) ? View.VISIBLE : View.GONE);
        }

        TextView totalCountView = (TextView) findViewById(R.id.image_preview_activity_total);
        final TextView selectedPageNumView = (TextView) findViewById(R.id.image_preview_activity_current);
        if (totalCountView != null) {
            totalCountView.setText(String.valueOf(mImageUrls.length));
        }
        if (selectedPageNumView != null) {
            selectedPageNumView.setText(String.valueOf((mInitPageNumber + 1)));
        }


        viewPager = (HackyViewPager) findViewById(R.id.image_preview_activity_viewpager);
        assert viewPager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置进入此activity动画
            viewPager.setTransitionName(mTransitionName + mInitPageNumber);
        }
        ViewpagerAdapter pagerAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(mInitPageNumber);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (selectedPageNumView != null) {
                    selectedPageNumView.setText(String.valueOf((position + 1)));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //返回动画中没有viewPager的话将会跑飞
                    viewPager.setTransitionName(mTransitionName + position);
                    //清除所有view的TransitionName，如果所有view都设置TransitionName，返回时各个view都会有动画，乱飞
                    for (int i = 0; i < mViews.length; i++) {
                        if (mViews[i] != null) {
                            mViews[i].setTransitionName("");
                        }
                    }
                    //只设置当前选中的view的TransitionName
                    mViews[position].setTransitionName(mTransitionName + position);
                }
            }
        });
        mViews = new View[mImageUrls.length];
    }


    private class ViewpagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mImageUrls.length;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final ImageView imageView = new ImageView(ImagePreviewActivity.this);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            String url = mImageUrls[position];
            Glide.with(ImagePreviewActivity.this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new GlideRequestListener(imageView)).into(imageView);
            mViews[position] = imageView;
            //为刚进入时显示的图片设置返回动画
            if (position == mInitPageNumber) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName(mTransitionName + position);
                }
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        class GlideRequestListener implements RequestListener<String, GlideDrawable> {

            private ImageView mImageView;

            GlideRequestListener(ImageView imageView) {
                mImageView = imageView;
            }

            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PhotoViewAttacher attache = new PhotoViewAttacher(mImageView);
                        attache.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                            @Override
                            public void onPhotoTap(View view, float x, float y) {
                                ImagePreviewActivity.super.onBackPressed();
                            }

                            @Override
                            public void onOutsidePhotoTap() {
                                ImagePreviewActivity.super.onBackPressed();
                            }
                        });
                    }
                }, 600);
                return false;
            }
        }
    }
}

