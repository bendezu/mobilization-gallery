package com.bendezu.yandexphotos.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DimUtilsTest {

    @Mock Context context;
    @Mock Resources resources;
    @Mock DisplayMetrics displayMetrics;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        displayMetrics.density = 1f;
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(context.getResources()).thenReturn(resources);
    }

    @Test
    public void verifyTwoWayConversions() {

        float testDp = 123f;
        float px = DimUtils.dpToPx(context, testDp);
        float dp = DimUtils.pxToDp(context, px);

        assertEquals(testDp, dp, 0.1f);
    }

}
