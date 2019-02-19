package com.katalog.kozmetik.catalog.picasso;

import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.chrisbanes.photoview.PhotoView;
import com.katalog.kozmetik.catalog.catalog.SquaredImageView;

/**
 * Created by user on 24.03.2018.
 */

public class ImageToLoad {


    private String url;
    private ImageView destination;
    private int placeholderResourceId;
    private int errorPlaceholderResourceId;
    private LottieAnimationView animationView;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageView getDestination() {
        return destination;
    }

    public void setDestination(ImageView destination) {
        this.destination = destination;
    }

    public int getPlaceholderResourceId() {
        return placeholderResourceId;
    }

    public void setPlaceholderResourceId(int placeholderResourceId) {
        this.placeholderResourceId = placeholderResourceId;
    }

    public int getErrorPlaceholderResourceId() {
        return errorPlaceholderResourceId;
    }

    public void setErrorPlaceholderResourceId(int errorPlaceholderResourceId) {
        this.errorPlaceholderResourceId = errorPlaceholderResourceId;
    }

    public LottieAnimationView getAnimationView() {
        return animationView;
    }

    public void setAnimationView(LottieAnimationView animationView) {
        this.animationView = animationView;
    }


}
