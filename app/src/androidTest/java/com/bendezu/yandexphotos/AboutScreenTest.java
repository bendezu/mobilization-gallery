package com.bendezu.yandexphotos;


import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AboutScreenTest {

    @Rule
    public IntentsTestRule<AboutActivity> testRule =
            new IntentsTestRule<>(AboutActivity.class);

    @After
    public void tearDown() {
        testRule.finishActivity();
    }

    @Test
    public void shouldFireIntent_whenClickAuthorCard() {
        String link = App.getContext().getString(R.string.github_link);

        onView(withId(R.id.author_card)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_VIEW),
                       hasData(link)));
    }

    @Test
    public void shouldOpen_whenClickLink() {
        onView(withId(R.id.tv_link)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_VIEW),
                hasData("https://www.academy.yandex.ru")));
    }
}
