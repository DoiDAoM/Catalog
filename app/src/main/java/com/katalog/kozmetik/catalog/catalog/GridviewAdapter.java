package com.katalog.kozmetik.catalog.catalog;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.katalog.kozmetik.catalog.brands.Page;
import com.katalog.kozmetik.catalog.picasso.ImageLoader;
import com.katalog.kozmetik.catalog.picasso.ImageToLoad;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by user on 2.12.2017.
 */

public class GridviewAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater mInflater;
    int brandPosition;
    List<Page> pageList;
    ViewHolder viewHolder;




    GridviewAdapter(Context context, int brandPosition) {
        this.mContext = context;
        this.brandPosition = brandPosition;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (brandPosition == -1) {
            pageList = ((BaseActivity)mContext).getApp().currentUser.getFavouriteList();
        } else {
            pageList = ((BaseActivity)mContext).getApp().brandList.get(brandPosition).getPageList();
        }
        viewHolder = new ViewHolder();
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        convertView = mInflater.inflate(R.layout.layout_gridview_catalog_item, null);
        if (convertView.getTag() == null) {

            viewHolder.squaredImageView = convertView.findViewById(R.id.ivGridviewCatalogPage);
            if (viewHolder.squaredImageView == null) {
                viewHolder.squaredImageView = new SquaredImageView(mContext);
                viewHolder.squaredImageView.setScaleType(CENTER_CROP);
            }
            viewHolder.cvCatalogGridview = convertView.findViewById(R.id.cvCatalogGridview);
            viewHolder.tvGridviewCatalogPage = convertView.findViewById(R.id.tvGridviewCatalogPage);
            viewHolder.imageToLoad = new ImageToLoad();

            convertView.setTag(viewHolder);



            viewHolder.tvGridviewCatalogPage.setText(String.valueOf(i + 1) + ". sayfa");

            viewHolder.animationView = convertView.findViewById(R.id.btnLottieLoadingGridview);
            if (viewHolder.animationView != null) {
                if (i != 0) {
                    viewHolder.animationView.setVisibility(View.VISIBLE);
                    viewHolder.animationView.setAnimation("loading_animation.json");
                    viewHolder.animationView.loop(true);
                    viewHolder.animationView.playAnimation();
                }
            }


            viewHolder.imageToLoad.setDestination(viewHolder.squaredImageView);
            viewHolder.imageToLoad.setUrl(pageList.get(i).getUrl());
            viewHolder.imageToLoad.setErrorPlaceholderResourceId(R.drawable.logo);
            viewHolder.imageToLoad.setPlaceholderResourceId(R.drawable.logo);
            viewHolder.imageToLoad.setAnimationView(viewHolder.animationView);

            ImageLoader.loadImage( viewHolder.imageToLoad);

            viewHolder.cvCatalogGridview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseActivity) mContext).getApp().analyticsHelper.sendEvent(pageList.get(i).getUrl(), "grid_image_selected");
                    ((BaseActivity) mContext).getApp().selectedPage = i;

                    ((BaseActivity) mContext).replaceFragment(((CatalogActivity) mContext).frLayout, ((CatalogActivity)mContext).pagerCatalogFragment, false);

                    ((CatalogActivity) mContext).ivGridviewCatalog.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_gridview_32dp));
                    ((CatalogActivity) mContext).ivFullScreenCatalog.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_full_screen_activated_32dp));

                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }

    private class ImageLoadedCallback implements Callback {
        LottieAnimationView animationView;

        public  ImageLoadedCallback(LottieAnimationView animationView2){
            animationView = animationView2;
        }

        @Override
        public void onSuccess() {
            animationView.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.destroyDrawingCache();
        }

        @Override
        public void onError() {
            animationView.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.destroyDrawingCache();
        }
    }

    private class ViewHolder {
        TextView tvGridviewCatalogPage;
        CardView cvCatalogGridview;
        SquaredImageView squaredImageView;
        LottieAnimationView animationView;
        ImageToLoad imageToLoad;
    }

}
