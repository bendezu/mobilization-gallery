package com.bendezu.yandexphotos;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import com.bendezu.yandexphotos.gallery.GalleryActivity;
import com.bendezu.yandexphotos.util.PreferencesUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.bendezu.yandexphotos.AuthScreenTest.TEST_TOKEN;
import static org.hamcrest.Matchers.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class ImageDetailScreenTest {

    @Rule
    public ActivityTestRule<GalleryActivity> testRule =
            new ActivityTestRule<>(GalleryActivity.class, true, false);

    private String accessToken;

    @Before
    public void setUp() {
        accessToken = PreferencesUtils.getAccessToken();
        PreferencesUtils.setAccessToken(TEST_TOKEN);
        testRule.launchActivity(new Intent());
        //show image detail
        onView(withId(R.id.gallery_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }

    @After
    public void tearDown() {
        PreferencesUtils.setAccessToken(accessToken);
        testRule.finishActivity();
    }

    @Test
    public void shouldHideAndShowTopBar_whenClicking() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

        onView(isRoot()).perform(clickPercent(0.5f, 0.5f));
        SystemClock.sleep(100);
        onView(withId(R.id.toolbar)).check(matches(not(isDisplayed())));

        onView(isRoot()).perform(clickPercent(0.5f, 0.5f));
        SystemClock.sleep(100);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    public static ViewAction clickPercent(final float pctX, final float pctY){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        int w = view.getWidth();
                        int h = view.getHeight();

                        float x = w * pctX;
                        float y = h * pctY;

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER,
                InputDevice.SOURCE_MOUSE,
                MotionEvent.BUTTON_PRIMARY);
    }

}
