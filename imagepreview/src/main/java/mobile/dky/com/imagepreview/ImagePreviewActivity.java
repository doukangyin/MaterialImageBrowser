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
 * browser activity
 */
public class ImagePreviewActivity extends Activity {
    //all view in viewpager
    private View[] mViews;

    // current page
    private int mInitPageNumber;

    // image urls
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
            //enter activity transition name
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
                    //must set viewPager transition name
                    viewPager.setTransitionName(mTransitionName + position);
                    //clear all view TransitionName
                    for (int i = 0; i < mViews.length; i++) {
                        if (mViews[i] != null) {
                            mViews[i].setTransitionName("");
                        }
                    }
                    //set current view TransitionName
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
            //exit transition name
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

