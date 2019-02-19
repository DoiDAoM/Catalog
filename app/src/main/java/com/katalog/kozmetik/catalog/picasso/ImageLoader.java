package com.katalog.kozmetik.catalog.picasso;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.katalog.kozmetik.catalog.catalog.AnimationHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by user on 24.03.2018.
 */

public class ImageLoader {

    private static ImageLoader currentInstance = null;
    private static Picasso currentPicassoInstance = null;
    private static AnimationHelper animationHelper;

    protected ImageLoader(Context context) {
        initPicassoInstance(context);
    }

    private void initPicassoInstance(Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        currentPicassoInstance = builder.build();
        animationHelper = new AnimationHelper();
    }

    public static ImageLoader getInstance(Context context) {
        if (currentInstance == null) {
            currentInstance = new ImageLoader(context);
        }
        return currentInstance;
    }

    public static void loadImage(ImageToLoad loadingInfo) {
        String imageUrl = loadingInfo.getUrl().trim();
        ImageView destination = loadingInfo.getDestination();

        if (imageUrl.isEmpty()) {
            destination.setImageResource(loadingInfo.getErrorPlaceholderResourceId());
        } else {
            currentPicassoInstance
                    .load(imageUrl)
                    .error(loadingInfo.getErrorPlaceholderResourceId())
                    .centerInside()
                    .fit()
                    .into(destination, new ImageLoadedCallback(loadingInfo.getAnimationView()));
        }
    }

    public static class ImageLoadedCallback implements Callback {
        private LottieAnimationView mAnimationView;

        ImageLoadedCallback(LottieAnimationView animationView) {
            mAnimationView = animationView;
        }

        @Override
        public void onSuccess() {
            animationHelper.destroyAnimation(mAnimationView);
        /*    mAnimationView.setVisibility(View.GONE);
            mAnimationView.pauseAnimation();
            mAnimationView.destroyDrawingCache();
            mAnimationView.clearAnimation();*/
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onError() {
           animationHelper.destroyAnimation(mAnimationView);
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


}

