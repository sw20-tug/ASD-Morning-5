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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BackupTest {
    @Rule
    public ActivityTestRule<BackupActivity> activityRule
            = new ActivityTestRule<>(BackupActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.morning5.vocabularytrainer", appContext.getPackageName());
    }

    @Test
    public void checkImportButton() {

        // Click on button Import
        onView(withId(R.id.button_import))
                .perform(click());


    }

    public void checkExportButton() {

        // Click on button Export
        onView(withId(R.id.button_export))
                .perform(click());
    }
}
