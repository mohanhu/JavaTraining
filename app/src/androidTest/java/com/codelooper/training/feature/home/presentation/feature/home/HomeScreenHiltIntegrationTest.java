package com.codelooper.training.feature.home.presentation.feature.home;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.codelooper.training.core.di.CoreDiModule;
import com.codelooper.training.core.di.MockServerHolder;
import com.codelooper.training.core.network.ResourceResult;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.codelooper.training.feature.train.domain.repository.MainRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@HiltAndroidTest
@UninstallModules(CoreDiModule.class)
@RunWith(AndroidJUnit4.class)
public class HomeScreenHiltIntegrationTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    MainRepository mainRepository;

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockServerHolder.server = mockWebServer;

        hiltRule.inject();
    }

    @After
    public void tearDown() throws Exception {

        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }

        MockServerHolder.server = null;
    }

    // Test 1: Verify Hilt repository injection.
    @Test
    public void verifyMainRepositoryInjection() {

        assertNotNull(mainRepository);
    }

    // Test 2: Verify API success response and model mapping.
    @Test
    public void getShows_success_returnsMappedMovieList() throws Exception {

        String response = "["
                + "{"
                + "\"id\": 1,"
                + "\"image\": {\"medium\": \"https://example.com/image.jpg\"},"
                + "\"name\": \"Test Show\","
                + "\"rating\": {\"average\": 8.5}"
                + "}"
                + "]";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(response)
                        .addHeader("Content-Type", "application/json")
        );

        ResourceResult<List<MovieListResponseItem>> result =
                mainRepository.getShows();

        assertTrue(result instanceof ResourceResult.Success);

        List<MovieListResponseItem> shows =
                ((ResourceResult.Success<List<MovieListResponseItem>>) result)
                        .getData();

        assertNotNull(shows);
        assertEquals(1, shows.size());

        MovieListResponseItem item = shows.get(0);

        assertEquals(1, item.id);
        assertEquals("Test Show", item.name);
        assertEquals("https://example.com/image.jpg", item.image);
        assertEquals(Double.valueOf(8.5), item.rating);

        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/shows", request.getPath());
    }

    // Test 3: Verify empty API response.
    @Test
    public void getShows_emptyResponse_returnsEmptyList() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("[]")
                        .addHeader("Content-Type", "application/json")
        );

        ResourceResult<List<MovieListResponseItem>> result =
                mainRepository.getShows();

        assertTrue(result instanceof ResourceResult.Success);

        List<MovieListResponseItem> shows =
                ((ResourceResult.Success<List<MovieListResponseItem>>) result)
                        .getData();

        assertNotNull(shows);
        assertTrue(shows.isEmpty());
    }

    // Test 4: Verify API error response.
    @Test
    public void getShows_serverError_returnsResourceError() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal Server Error")
        );

        ResourceResult<List<MovieListResponseItem>> result =
                mainRepository.getShows();

        assertTrue(result instanceof ResourceResult.Error);

        ResourceResult.Error<?> error =
                (ResourceResult.Error<?>) result;

        assertNotNull(error.getException());
    }
}