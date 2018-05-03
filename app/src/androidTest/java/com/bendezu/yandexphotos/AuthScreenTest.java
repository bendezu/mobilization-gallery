package com.bendezu.yandexphotos;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.bendezu.yandexphotos.authorization.AuthActivity;
import com.bendezu.yandexphotos.util.NetworkUtils;
import com.bendezu.yandexphotos.util.PreferencesUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class AuthScreenTest {

    public static final String TEST_TOKEN = "AQAAAAALonLyAADLW8645GF7gEAOjzvMzLiu4QU";
    public static final String WRONG_TOKEN = "definitely_wrong_token";

    @Rule
    public IntentsTestRule<AuthActivity> testRule =
            new IntentsTestRule<>(AuthActivity.class, true, false);

    private String accessToken;

    @Before
    public void setUp() {
        accessToken = PreferencesUtils.getAccessToken();
    }

    @After
    public void tearDown() {
        PreferencesUtils.setAccessToken(accessToken);
        testRule.finishActivity();
    }

    @Test
    public void shouldOpenAuth_whenClickLogIn() {
        String link = NetworkUtils.buildAuthUrl();
        PreferencesUtils.setAccessToken(null);

        testRule.launchActivity(new Intent());

        onView(withId(R.id.btn_log_in)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_VIEW),
                hasData(link)));
    }

    @Test
    public void shouldOpenGallery_whenHaveToken() {
        PreferencesUtils.setAccessToken(TEST_TOKEN);

        testRule.launchActivity(new Intent());

        onView(withId(R.id.gallery_recycler_view)).check(matches(isDisplayed()));
    }
}
