
package com.codelooper.training.feature.home.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.codelooper.training.feature.train.data.SampleApiService;
import com.codelooper.training.feature.train.data.repository.DefaultMainRemoteDataSource;
import com.codelooper.training.feature.train.data.repository.DefaultMainRepository;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeScreenViewModelIntegrationTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MockWebServer mockWebServer;
    private HomeScreenViewModel viewModel;

    @Before
    public void setUp() throws Exception {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SampleApiService apiService =
                retrofit.create(SampleApiService.class);

        DefaultMainRemoteDataSource remoteDataSource =
                new DefaultMainRemoteDataSource(apiService);

        DefaultMainRepository repository =
                new DefaultMainRepository(remoteDataSource);

        viewModel = new HomeScreenViewModel(repository);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    // 1. Successful API response
    @Test
    public void loadData_success_returnsMovieList() throws Exception {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(SUCCESS_JSON)
        );

        AtomicReference<List<MovieListResponseItem>> result =
                new AtomicReference<>();

        CountDownLatch latch = new CountDownLatch(1);

        Observer<List<MovieListResponseItem>> observer = items -> {
            if (items != null && !items.isEmpty()) {
                result.set(items);
                latch.countDown();
            }
        };

        viewModel.movieList.observeForever(observer);

        try {
            viewModel.loadData();

            assertTrue(latch.await(5, TimeUnit.SECONDS));
            assertNotNull(result.get());
            assertFalse(result.get().isEmpty());

            assertEquals("/shows",
                    mockWebServer.takeRequest().getPath());

        } finally {
            viewModel.movieList.removeObserver(observer);
        }
    }

    // 2. Empty API response
    @Test
    public void loadData_emptyResponse_returnsEmptyList() throws Exception {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(EMPTY_JSON)
        );

        AtomicReference<List<MovieListResponseItem>> result =
                new AtomicReference<>();

        CountDownLatch latch = new CountDownLatch(1);

        Observer<List<MovieListResponseItem>> observer = items -> {
            if (items != null) {
                result.set(items);
                latch.countDown();
            }
        };

        viewModel.movieList.observeForever(observer);

        try {
            viewModel.loadData();

            assertTrue(latch.await(5, TimeUnit.SECONDS));
            assertNotNull(result.get());
            assertTrue(result.get().isEmpty());

        } finally {
            viewModel.movieList.removeObserver(observer);
        }
    }

    // 3. Server error
    @Test
    public void loadData_serverError_handlesError() throws Exception {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal Server Error")
        );

        CountDownLatch latch = new CountDownLatch(1);

        Observer<String> observer = error -> {
            if (error != null && !error.isEmpty()) {
                latch.countDown();
            }
        };

        viewModel.error.observeForever(observer);

        try {
            viewModel.loadData();

            assertTrue(latch.await(5, TimeUnit.SECONDS));

        } finally {
            viewModel.error.removeObserver(observer);
        }
    }

    // 4. Network disconnected
    @Test
    public void loadData_networkDisconnected_handlesError() throws Exception {

        mockWebServer.enqueue(
                new MockResponse()
                        .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        );

        CountDownLatch latch = new CountDownLatch(1);

        Observer<String> observer = error -> {
            if (error != null && !error.isEmpty()) {
                latch.countDown();
            }
        };

        viewModel.error.observeForever(observer);

        try {
            viewModel.loadData();

            assertTrue(latch.await(5, TimeUnit.SECONDS));

        } finally {
            viewModel.error.removeObserver(observer);
        }
    }

    // 5. Invalid JSON response
    @Test
    public void loadData_invalidJson_handlesError() throws Exception {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody("{ invalid json }")
        );

        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<String> errorMessage = new AtomicReference<>();

        Observer<String> observer = error -> {
            if (error != null && !error.isEmpty()) {
                errorMessage.set(error);
                latch.countDown();
            }
        };

        viewModel.error.observeForever(observer);

        try {
            viewModel.loadData();

            assertTrue(latch.await(5, TimeUnit.SECONDS));
            assertNotNull(errorMessage.get());
            assertFalse(errorMessage.get().isEmpty());

        } finally {
            viewModel.error.removeObserver(observer);
        }
    }

    // Replace these fixtures with your actual API response structure.
    private static final String SUCCESS_JSON =
            "[{\"id\":1,\"name\":\"Test Movie\"}]";

    private static final String EMPTY_JSON = "[]";
}
