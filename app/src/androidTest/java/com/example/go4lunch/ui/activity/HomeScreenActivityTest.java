package com.example.go4lunch.ui.activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.example.go4lunch.matchers.BottomNavigationItemViewMatcher.withIsChecked;
import static com.example.go4lunch.matchers.BottomNavigationItemViewMatcher.withTitle;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.go4lunch.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    private Context context;

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> mHomeScreenActivity = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void checkBottomNavigationButtonSelection() {
        onView(ViewMatchers.withId(R.id.action_map)).check(matches(withIsChecked(true)));
        onView(ViewMatchers.withId(R.id.action_list)).check(matches(withIsChecked(false)));
        onView(ViewMatchers.withId(R.id.action_workmates)).check(matches(withIsChecked(false)));
    }

    @Test
    public void checkBottomNavigationButtonTitle() {
        onView(ViewMatchers.withId(R.id.action_map)).check(matches(withTitle(context.getString(R.string.bottom_navigation_menu_map))));
        onView(ViewMatchers.withId(R.id.action_list)).check(matches(withTitle(context.getString(R.string.bottom_navigation_menu_list))));
        onView(ViewMatchers.withId(R.id.action_workmates)).check(matches(withTitle(context.getString(R.string.bottom_navigation_menu_workmates))));
    }

    @Test
    public void checkMapViewFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.action_map)).perform(click());
        onView(ViewMatchers.withId(R.id.map_view_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void checkListViewFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.action_list)).perform(click());
        onView(ViewMatchers.withId(R.id.list_view_fragment)).check(matches(isDisplayed()));
    }


    @Test
    public void checkWorkMatesViewFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.action_workmates)).perform(click());
        onView(ViewMatchers.withId(R.id.workmates_view_fragment)).check(matches(isDisplayed()));
    }
}