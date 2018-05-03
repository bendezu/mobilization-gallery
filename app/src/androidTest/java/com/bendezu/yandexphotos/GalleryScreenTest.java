package com.bendezu.yandexphotos;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bendezu.yandexphotos.gallery.GalleryActivity;
import com.bendezu.yandexphotos.util.PreferencesUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.bendezu.yandexphotos.AuthScreenTest.TEST_TOKEN;
import static com.bendezu.yandexphotos.AuthScreenTest.WRONG_TOKEN;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class GalleryScreenTest {

    @Rule
    public ActivityTestRule<GalleryActivity> testRule =
            new ActivityTestRule<>(GalleryActivity.class, true, false);

    private String accessToken;

    @Before
    public void setUp() {
        accessToken = PreferencesUtils.getAccessToken();
        PreferencesUtils.setAccessToken(TEST_TOKEN);
    }

    @After
    public void tearDown() {
        PreferencesUtils.setAccessToken(accessToken);
        testRule.finishActivity();
    }

    @Test
    public void shouldLaunchAuth_whenTokenWasInvalid() {
        PreferencesUtils.setAccessToken(WRONG_TOKEN);
        testRule.launchActivity(new Intent());
        onView(withId(R.id.btn_log_in)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldLaunchAuth_whenTokenBecomeInvalid() {
        testRule.launchActivity(new Intent());
        PreferencesUtils.setAccessToken(WRONG_TOKEN);
        //swipe to refresh
        onView(withId(R.id.gallery_recycler_view)).perform(swipeDown());
        onView(withId(R.id.btn_log_in)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldScrollToImage_whenReturningToGallery() {
        testRule.launchActivity(new Intent());
        onView(withId(R.id.gallery_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(14, click()));
        onView(withId(R.id.image_view_pager)).perform(swipeLeft());
        pressBack();
        //image 15 should be visible
        onView(new RecyclerViewMatcher(R.id.gallery_recycler_view)
                .atPosition(15)).check(matches(isDisplayed()));

    }

}
