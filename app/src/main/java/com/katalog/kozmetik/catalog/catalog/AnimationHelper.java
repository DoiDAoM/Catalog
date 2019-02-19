package com.katalog.kozmetik.catalog.catalog;

import android.animation.Animator;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.katalog.kozmetik.catalog.R;

/**
 * Created by user on 4.04.2018.
 */

public class AnimationHelper {


    public void showAnimation(LottieAnimationView animationView){

        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("loading_animation.json", LottieAnimationView.CacheStrategy.Strong);
        animationView.loop(true);
        animationView.useHardwareAcceleration(true);
        animationView.playAnimation();

    }

    public void destroyAnimation(LottieAnimationView animationView) {
        animationView.pauseAnimation();
        animationView.setVisibility(View.GONE);
        animationView.clearAnimation();
    }



}
