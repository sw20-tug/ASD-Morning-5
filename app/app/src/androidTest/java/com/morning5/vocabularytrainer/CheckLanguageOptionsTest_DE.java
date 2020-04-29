package com.morning5.vocabularytrainer;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import java.util.concurrent.TimeUnit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CheckLanguageOptionsTest_DE {
    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.morning5.vocabularytrainer", appContext.getPackageName());
    }

    @Test
    public void insertWord() throws InterruptedException {

        // Click button to change to German
        onView(withId(R.id.button_change_language_DE))
                .perform(click());

        TimeUnit.SECONDS.sleep(2);

        // check if button is correct
        onView(withId(R.id.button_overview))
                .check(matches(withText("Überblick")));

        //check if 2nd button is correct
        onView(withId(R.id.button_add_word))
                .check(matches(withText("Wortschatz hinzufügen")));
    }
}
