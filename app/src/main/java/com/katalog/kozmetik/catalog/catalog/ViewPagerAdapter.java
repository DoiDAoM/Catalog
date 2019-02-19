package com.katalog.kozmetik.catalog.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.github.chrisbanes.photoview.PhotoView;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.katalog.kozmetik.catalog.brands.Page;
import com.katalog.kozmetik.catalog.picasso.ImageLoader;
import com.katalog.kozmetik.catalog.picasso.ImageToLoad;

import java.util.List;

/**
 * Created by user on 27.11.2017.
 */

public class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
    private Context mContext;
    LayoutInflater mInflater;
    int brandPosition;
    List<Page> pageList;
    ImageToLoad imageToLoad;
    public ViewHolder viewHolder;
    AnimationHelper animationHelper;



    public ViewPagerAdapter(Context context, int brandPosition) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brandPosition = brandPosition;

        if (brandPosition == -1) {
            pageList = ((BaseActivity)mContext).getApp().currentUser.getFavouriteList();
        } else {
            pageList = ((BaseActivity)mContext).getApp().brandList.get(brandPosition).getPageList();
        }
        imageToLoad = new ImageToLoad();
        viewHolder = new ViewHolder();
        animationHelper = new AnimationHelper();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ViewGroup layout = (ViewGroup) mInflater.inflate(R.layout.layout_viewpager_item, container, false);
        container.addView(layout);

        viewHolder.animationView = layout.findViewById(R.id.lottiePagerLoading);

        animationHelper.showAnimation(viewHolder.animationView);

        viewHolder.photoView = layout.findViewById(R.id.ivViewPager);

        imageToLoad.setDestination( viewHolder.photoView);
        imageToLoad.setUrl(pageList.get(position).getUrl());
        imageToLoad.setErrorPlaceholderResourceId(R.drawable.logo);
        imageToLoad.setPlaceholderResourceId(R.drawable.logo);
        imageToLoad.setAnimationView(viewHolder.animationView);

        ImageLoader.loadImage(imageToLoad);

        layout.setTag("view" + position);
        return layout;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public class ViewHolder {
        LottieAnimationView animationView;
        PhotoView photoView;

    }


}
