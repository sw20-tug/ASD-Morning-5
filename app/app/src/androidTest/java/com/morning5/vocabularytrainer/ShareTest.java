package com.morning5.vocabularytrainer;

import android.content.Context;

import androidx.test.espresso.action.CloseKeyboardAction;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ShareTest {
    @Rule
    public ActivityTestRule<ShareActivity> activityRule
            = new ActivityTestRule<>(ShareActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.morning5.vocabularytrainer", appContext.getPackageName());
    }
    @Test
    public void checkAllVocabButton() {
        onView(withId(R.id.button_all_vocabulary))
                .perform(click());

    }
    @Test
    public void checkSomeOfVocab() {
        onView(withId(R.id.button_some_of_the_vocabulary))
                .perform();

    }
}
