package teetst;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.music_service.R;
import com.example.music_service.views.MainActivity;
import com.example.music_service.views.SearchActivity;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void test_title_display() {
        onView(withText("RECOMMENDATIONS")).check(matches(isDisplayed()));
        onView(withText("RECOMMENDATIONS")).check(isCompletelyBelow(withId(R.id.morning_region)));
    }

    @Test
    public void test_playlists_amount() {
        onView(withId(R.id.morning_region)).check(new RecyclerViewItemCountAssertion(6));
    }

    @Test
    public void test_open_search_activity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SearchActivity.class);
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            SearchActivity targetActivity = (SearchActivity) activity;
            assertNotNull(targetActivity);
        });
    }

}


