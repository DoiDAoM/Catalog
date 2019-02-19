package com.katalog.kozmetik.catalog.catalog;

import android.view.View;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;

/**
 * Created by user on 28.11.2017.
 */

public class DrawerTransformer extends ABaseTransformer {
    @Override
    protected void onTransform(View page, float position) {
        if (position <= 0) {
            page.setTranslationX(0);
        } else if (position > 0 && position <= 1) {
            page.setTranslationX(-page.getWidth() / 2 * position);
        }
    }
}
