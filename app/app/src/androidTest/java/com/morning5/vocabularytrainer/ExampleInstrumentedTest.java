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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<AddWordActivity> activityRule
            = new ActivityTestRule<>(AddWordActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.morning5.vocabularytrainer", appContext.getPackageName());
    }

    @Test
    public void insertWord() {
        String englishInput = "Door";
        String germanInput = "Tuer";

        // Fill English Text
        onView(withId(R.id.editText_english))
                .perform(replaceText(englishInput));

        // Fill German Text
        onView(withId(R.id.editText_german))
                .perform(replaceText(germanInput));

        // Click on button add word
        onView(withId(R.id.button))
                .perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.snackbar_success)));
    }
}
