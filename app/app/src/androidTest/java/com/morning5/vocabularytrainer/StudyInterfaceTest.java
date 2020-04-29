package com.morning5.vocabularytrainer;
import android.content.Context;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StudyInterfaceTest {
    @Rule
    public ActivityTestRule<StudyInterfaceActivity> activityRule
            = new ActivityTestRule<>(StudyInterfaceActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.morning5.vocabularytrainer", appContext.getPackageName());
    }

    @Test
    public void showWord() {

        //cheking if list is displayed
        onView(withId(R.id.list_view_study_interface)).check(matches(isDisplayed()));
        int position = 1;

        //clicking on the item in position from list
        onData(allOf())
                .inAdapterView(withId(R.id.list_view_study_interface))
                .atPosition(position)
                .perform(click());

        ListView listview = activityRule.getActivity().findViewById(R.id.list_view_study_interface);

        //checking if the alert dialog is showing the right translation
        onView(withText(StudyInterfaceActivity.words.get((String)listview.getItemAtPosition(position)))).check(matches(isDisplayed()));
    }
}