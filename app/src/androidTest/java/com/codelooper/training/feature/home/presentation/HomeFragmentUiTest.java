
package com.codelooper.training.feature.home.presentation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.codelooper.training.R;
import com.codelooper.training.core.di.MockServerHolder;
import com.codelooper.training.feature.train.data.model.MovieListResponse;
import com.codelooper.training.feature.train.data.model.MovieListResponseItemDto;
import com.codelooper.training.main.MainActivity;
import com.google.gson.Gson;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class HomeFragmentUiTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setup() throws IOException {

        // 1. Start MockWebServer
        MockServerHolder.start();

        // 2. Prepare fake API response BEFORE launching Activity
        List<MovieListResponseItemDto> response = MovieListResponse.createList();

        MockServerHolder.server.setDispatcher(new Dispatcher() {
            @NonNull
            @Override
            public MockResponse dispatch(@NonNull RecordedRequest recordedRequest) throws InterruptedException {

                assert recordedRequest.getPath() != null;
                if (recordedRequest.getPath().equals("/shows")) {
                    return jsonResponse(response);
                }

                return new MockResponse()
                        .setResponseCode(404);
            }
        });

        // 3. Inject Hilt dependencies
        hiltRule.inject();

        // 4. Launch Activity
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void homeScreen_searchField_canTypeText() throws InterruptedException {
        Thread.sleep(5000);

        onView(withId(R.id.et_search))
                .check(matches(isDisplayed()))
                .perform(typeText("flower"));

        Thread.sleep(5000);

        onView(withId(R.id.rv_list))
                .check(matches(isDisplayed()));
    }
    @Test
    public void homeScreen_selectAndDeleteFlow() {
        // Step 1 : Check movie list is displayed
        onView(withId(R.id.rv_list))
                .check(matches(isDisplayed()));

        // Step 2 : OnToggle a movie item
        onView(withId(R.id.rv_list))
                .perform(
                        actionOnItemAtPosition(
                                0,
                                childViewAction(R.id.cv_movie, longClick())
                        )
                );

        onView(withId(R.id.rv_list))
                .perform(
                        actionOnItemAtPosition(
                                2,
                                childViewAction(R.id.cv_movie, click())
                        )
                );

        onView(withId(R.id.rv_list))
                .perform(
                        actionOnItemAtPosition(
                                3,
                                childViewAction(R.id.cv_movie, click())
                        )
                );

        onView(withId(R.id.rv_list))
                .perform(
                        actionOnItemAtPosition(
                                5,
                                childViewAction(R.id.cv_movie, click())
                        )
                );

        onView(withId(R.id.delete_container))
                .check(matches(isDisplayed()));


        onView(withId(R.id.tv_select_count))
                .check(matches(
                        androidx.test.espresso.matcher.ViewMatchers
                                .withText("4")
                ));

        onView(withId(R.id.delete))
                .perform(click());

        onView(withId(R.id.delete_container))
                .check(
                        matches(
                                ViewMatchers.withEffectiveVisibility(
                                        ViewMatchers.Visibility.GONE
                                )
                        )
                )
        ;

    }

    private ViewAction childViewAction(int viewId, ViewAction viewAction) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View child = view.findViewById(viewId);
                if (child != null) {
                    viewAction.perform(uiController, child);
                }
            }
        };
    }

    @After
    public void tearDown() throws IOException {

        if (scenario != null) {
            scenario.close();
        }

        MockServerHolder.shutdown();
    }

    private MockResponse jsonResponse(Object body) {

        Gson gson = new Gson();
        String json = gson.toJson(body);

        return new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(json);
    }
}
