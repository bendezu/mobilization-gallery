package com.bendezu.yandexphotos.gallery;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.bendezu.yandexphotos.R;

public class ParallaxPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(1);
        } else if (position <= 1) { // [-1,1]
            View image = page.findViewById(R.id.iv_image);
            View fullImage = page.findViewById(R.id.iv_fullsize);
            image.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
            fullImage.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(1);
        }
    }
}