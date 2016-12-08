package mobile.dky.com.testdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import mobile.dky.com.imagepreview.ImagePreviewBuilder;

/**
 * Created by 11 on 2016/12/6.
 */

public class GridAdapter extends RecyclerView.Adapter {
    Context mContext;

    int mResId;

    List<String> mData;

    View[] views;


    public GridAdapter(int resId, List<String> data) {
        mResId = resId;
        mData = data;
        views = new View[mData.size()];
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new BaseViewHolder(getItemView(mResId, parent));
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layoutResId, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.image_view);
        if (views[position] == null) {
            views[position] = imageView;
        }
        Glide.with(mContext).load(mData.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePreviewBuilder builder = new ImagePreviewBuilder(mContext);
                builder.setViews(views)//所有图片容器
                        .setUrl((String[]) mData.toArray(new String[mData.size()]))//所有图片地址
                        .setTransitionName("image_preview_transition")//transitionName
                        .setCurrentPosition(position)//所有图片中选中那张图片
                        .setPageVisible(true)
                        .build();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
