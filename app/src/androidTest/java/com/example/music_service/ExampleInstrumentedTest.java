package com.example.music_service;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.music_service.views.MainActivity;

import androidx.test.espresso.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.music_service", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkContainerIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.main))
                .check(matches(isDisplayed()));
    }
}

class UnlockScreenRule<A extends AppCompatActivity> implements TestRule {
    ActivityTestRule<A> activityRule;

    UnlockScreenRule(ActivityTestRule<A> activityRule) {
        this.activityRule = activityRule;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                activityRule.runOnUiThread(() -> activityRule
                        .getActivity()
                        .getWindow()
                        .addFlags(
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON));
                statement.evaluate();
            }
        };
    }
}