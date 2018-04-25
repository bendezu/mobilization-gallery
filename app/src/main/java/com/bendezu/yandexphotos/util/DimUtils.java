package com.bendezu.yandexphotos.util;

import android.content.Context;

public class DimUtils {

    public static float dpToPx(final Context context, final float dp){
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
