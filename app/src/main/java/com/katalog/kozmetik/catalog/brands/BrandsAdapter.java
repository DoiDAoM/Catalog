package com.katalog.kozmetik.catalog.brands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.katalog.kozmetik.catalog.Const;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.katalog.kozmetik.catalog.catalog.CatalogActivity;
import com.katalog.kozmetik.catalog.catalog.SquaredImageView;
import com.katalog.kozmetik.catalog.firebase.AdHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.katalog.kozmetik.catalog.picasso.ImageLoader;
import com.katalog.kozmetik.catalog.picasso.ImageToLoad;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by user on 23/11/2017.
 */

public class BrandsAdapter extends BaseAdapter {
    private Context mContext;
    List<Brand> brandList;
    LayoutInflater mInflater;
    ViewHolder viewHolder;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;


    BrandsAdapter(Context context, List<Brand> brands) {
        this.mContext = context;
        this.brandList = brands;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewHolder = new ViewHolder();
        prefs = mContext.getSharedPreferences(AdHelper.PREF_NAME_AD, MODE_PRIVATE);

    }

    @Override
    public int getCount() {
        return brandList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {



        view = mInflater.inflate(R.layout.layout_gridview_brand_item, null);

        if (view.getTag() == null ) {


            viewHolder.sqView = view.findViewById(R.id.ivGridviewBrand);
            if (viewHolder.sqView == null) {
                viewHolder.sqView = new SquaredImageView(mContext);
                viewHolder.sqView.setScaleType(CENTER_CROP);
            }

            viewHolder.tvBrandName = view.findViewById(R.id.tvGridviewBrand);
            viewHolder.tvContent = view.findViewById(R.id.tvContent);
            viewHolder.imageToLoad = new ImageToLoad();


            viewHolder.animationView = view.findViewById(R.id.LottieLoadingGridviewBrands);
            if (viewHolder.animationView != null) {

                if (i != 0) {
                    viewHolder.animationView.setVisibility(View.VISIBLE);
                    viewHolder.animationView.setAnimation("loading_animation.json");
                    viewHolder.animationView.loop(true);
                    viewHolder.animationView.setPerformanceTrackingEnabled(true);
                    viewHolder.animationView.playAnimation();
                }
            }
           view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvBrandName.setText(brandList.get(i).getName());
        viewHolder.tvContent.setText(brandList.get(i).getTitle());


        viewHolder.imageToLoad.setDestination(viewHolder.sqView);
        viewHolder.imageToLoad.setUrl(brandList.get(i).getCoverPageUrl());
        viewHolder.imageToLoad.setErrorPlaceholderResourceId(R.drawable.logo);
        viewHolder.imageToLoad.setPlaceholderResourceId(R.drawable.logo);
        viewHolder.imageToLoad.setAnimationView(viewHolder.animationView);

        ImageLoader.loadImage( viewHolder.imageToLoad);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) mContext).getApp().analyticsHelper.sendEvent(brandList.get(i).getName(), "Clicked");

                ((BrandsActivity) mContext).rewardAdCounter++;
                editor = prefs.edit();
                editor.putInt("rewarded_ad_counter", ((BrandsActivity) mContext).rewardAdCounter);
                editor.apply();

                ((BrandsActivity)mContext).getApp().rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded() {
                        System.out.println("loadd1");
                    }

                    @Override
                    public void onRewardedVideoAdOpened() {
                        System.out.println("loadd2");
                        ((BaseActivity) mContext).getApp().analyticsHelper.sendEvent(brandList.get(i).getName(), "reward_video_ad_opened");

                    }

                    @Override
                    public void onRewardedVideoStarted() {
                        System.out.println("loadd3");
                    }

                    @Override
                    public void onRewardedVideoAdClosed() {

                        ((BaseActivity) mContext).getApp().analyticsHelper.sendEvent(brandList.get(i).getName(), "reward_ad_closed");
                        loadVideoAd();
                    }

                    @Override
                    public void onRewarded(RewardItem rewardItem) {
                        ((BrandsActivity) mContext).rewardAdCounter = 0;

                        editor.putInt("rewarded_ad_counter", ((BrandsActivity) mContext).rewardAdCounter);
                        editor.apply();

                        ((BaseActivity) mContext).getApp().analyticsHelper.sendEvent(brandList.get(i).getName(), "ad_rewarded");
                        loadVideoAd();
                        Intent intent = new Intent(mContext, CatalogActivity.class);
                        intent.putExtra("is_favorite", false);
                        intent.putExtra("brand_position", i);
                        mContext.startActivity(intent);

                    }

                    @Override
                    public void onRewardedVideoAdLeftApplication() {
                        System.out.println("loadd6");
                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(int i) {
                        loadVideoAd();
                        System.out.println("loadd");
                    }

                    @Override
                    public void onRewardedVideoCompleted() {
                        System.out.println("loadd7");
                    }
                });


                if (((BrandsActivity) mContext).rewardAdCounter <= Const.REWARDED_AD_LIMIT) {
                    Intent intent = new Intent(mContext, CatalogActivity.class);
                    intent.putExtra("is_favorite", false);
                    intent.putExtra("brand_position", i);
                    mContext.startActivity(intent);
                } else {
                    if (((BrandsActivity) mContext).getApp().rewardedVideoAd.isLoaded())
                        ((BrandsActivity) mContext).getApp().rewardedVideoAd.show();
                    else {
                        Toast toast = Toast.makeText(mContext, "Katalogun açılması için videoyu izlemelisiniz.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 128);
                        View view2 = toast.getView();
                        view2.setBackgroundResource(R.drawable.shape_toast_bg);
                        toast.show();
                    }
                }

            }
        });


        return view;
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

    private void loadVideoAd() {
        ((BrandsActivity)mContext).getApp().rewardedVideoAd.loadAd(mContext.getResources().getString(R.string.rewarded_ad_id),
                new AdRequest.Builder().build());
    }

    private class ViewHolder {
        TextView tvBrandName;
        TextView tvContent;
        LottieAnimationView animationView;
        SquaredImageView sqView;
        ImageToLoad imageToLoad;
    }

}
