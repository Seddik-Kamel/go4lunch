package com.example.go4lunch.matchers;


import android.view.View;

import androidx.test.espresso.matcher.BoundedMatcher;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by Philippe from OC on 29/03/2018.
 */

public final class BottomNavigationItemViewMatcher {

    private BottomNavigationItemViewMatcher(){}

    public static Matcher<View> withIsChecked(final boolean isChecked) {
        return new BoundedMatcher<>(BottomNavigationItemView.class) {

            private boolean triedMatching;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with isChecked: " + isChecked);
                    description.appendText("But was: " + !isChecked);
                }
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                triedMatching = true;
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }

    public static Matcher<View> withTitle(final String titleTested) {
        return new BoundedMatcher<>(BottomNavigationItemView.class) {

            private boolean triedMatching;
            private String title;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with title: " + titleTested);
                    description.appendText("But was: " + title);
                }
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                this.triedMatching = true;
                this.title = item.getItemData().getTitle().toString();
                return title.equals(titleTested);
            }
        };
    }
}

