package com.codelooper.training.feature.home.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.codelooper.training.core.network.ResourceResult;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.codelooper.training.feature.train.domain.repository.MainRepository;
import com.codelooper.training.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Unit Test for HomeScreenViewModel.
 * 
 * WHY THIS TEST IS NEEDED:
 * ViewModel contains core business logic such as movie selection, deletion, searching,
 * and mapping API states (loading, success, error). Unit tests ensure this logic works
 * correctly in isolation without needing an Android device or real network calls.
 * 
 * KEY TESTING CONCEPTS USED:
 * 1. JUnit 4: Testing framework providing test runners, annotations (@Test, @Before), and assertions.
 * 2. Mockito: Mocking library used to fake dependencies like MainRepository.
 * 3. InstantTaskExecutorRule: Swaps the background executor used by Android Architecture Components 
 *    with a synchronous one so LiveData updates happen instantly on the test thread.
 * 4. AAA Pattern (Arrange - Act - Assert): Clear 3-step test structure.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeScreenViewModelTest {

    // This rule forces LiveData tasks to be executed synchronously on the test thread instead of main thread.
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // Mockito creates a fake instance of MainRepository so we don't call real network APIs during tests.
    @Mock
    private MainRepository mockMainRepository;

    private HomeScreenViewModel viewModel;

    // Synchronous Executor implementation for deterministic unit testing.
    private final ExecutorService testExecutor = new DirectExecutorService();

    @Before
    public void setUp() {
        // Initializes the ViewModel before each test using our fake repository and direct executor.
        viewModel = new HomeScreenViewModel(mockMainRepository, testExecutor, testExecutor);
    }

    @After
    public void tearDown() {
        // Shuts down the executor after each test.
        testExecutor.shutdown();
    }

    /**
     * Helper method to generate sample movie items for testing.
     */
    private List<MovieListResponseItem> createSampleMovies() {
        List<MovieListResponseItem> list = new ArrayList<>();

        MovieListResponseItem item1 = new MovieListResponseItem();
        item1.id = 1;
        item1.name = "Inception";
        item1.image = "https://example.com/inception.jpg";
        item1.rating = 8.8;
        item1.toggle = false;
        item1.isSelected = false;
        list.add(item1);

        MovieListResponseItem item2 = new MovieListResponseItem();
        item2.id = 2;
        item2.name = "Interstellar";
        item2.image = "https://example.com/interstellar.jpg";
        item2.rating = 8.6;
        item2.toggle = false;
        item2.isSelected = false;
        list.add(item2);

        MovieListResponseItem item3 = new MovieListResponseItem();
        item3.id = 3;
        item3.name = "The Dark Knight";
        item3.image = "https://example.com/darkknight.jpg";
        item3.rating = 9.0;
        item3.toggle = false;
        item3.isSelected = false;
        list.add(item3);

        return list;
    }

    // ==========================================
    // SECTION A: MOVIE SELECTION TESTS
    // ==========================================

    @Test
    public void testSelectOneMovie_updatesIsSelectedCorrectly() throws InterruptedException {
        // ARRANGE: Set up initial movie list in ViewModel
        List<MovieListResponseItem> initialMovies = createSampleMovies();
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(initialMovies));
        viewModel.loadData();

        // ACT: Toggle selection for Movie ID 1
        viewModel.toggleSelection(1);

        // ASSERT: Verify movie 1 is selected and toggle mode is enabled for all items
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        assertEquals(3, updatedList.size());
        assertTrue("Movie ID 1 should be selected", updatedList.get(0).isSelected);
        assertFalse("Movie ID 2 should NOT be selected", updatedList.get(1).isSelected);
        assertFalse("Movie ID 3 should NOT be selected", updatedList.get(2).isSelected);
        assertTrue("Toggle mode should be enabled", updatedList.get(0).toggle);
    }

    @Test
    public void testSelectMultipleMovies_updatesIsSelectedForBoth() throws InterruptedException {
        // ARRANGE: Load sample movies into ViewModel
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();

        // ACT: Select Movie ID 1, then Select Movie ID 2
        viewModel.toggleSelection(1);
        viewModel.toggleSelection(2);

        // ASSERT: Both Movie ID 1 and Movie ID 2 should be selected
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        assertTrue("Movie ID 1 should be selected", updatedList.get(0).isSelected);
        assertTrue("Movie ID 2 should be selected", updatedList.get(1).isSelected);
        assertFalse("Movie ID 3 should NOT be selected", updatedList.get(2).isSelected);
    }

    @Test
    public void testSelectAlreadySelectedMovie_togglesSelectionToFalse() throws InterruptedException {
        // ARRANGE: Load movies and select Movie ID 1
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();
        viewModel.toggleSelection(1); // First click -> Selected

        // ACT: Click Movie ID 1 again -> Should Deselect
        viewModel.toggleSelection(1);

        // ASSERT: Movie ID 1 should no longer be selected
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        assertFalse("Movie ID 1 selection should toggle back to false", updatedList.get(0).isSelected);
    }

    @Test
    public void testSelectInvalidMovieId_doesNotAffectExistingItems() throws InterruptedException {
        // ARRANGE: Load sample movies into ViewModel
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();

        // ACT: Try to select a non-existent movie ID 999
        viewModel.toggleSelection(999);

        // ASSERT: No item in the list should be marked as selected
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        boolean isItemFound = updatedList.stream().anyMatch(item -> item.id == 999);
        for (MovieListResponseItem item : updatedList) {
            assertFalse("No item should be selected for invalid ID", item.isSelected);
        }
        assertFalse("No item should be selected for invalid ID", isItemFound);
    }

    @Test
    public void testVerifySelectedMovieCount() throws InterruptedException {
        // ARRANGE: Load movies into ViewModel
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();

        // ACT: Select two movies (ID 1 and ID 3)
        viewModel.toggleSelection(1);
        viewModel.toggleSelection(3);

        // ASSERT: Count of selected movies should equal 2
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        long selectedCount = updatedList.stream().filter(item -> Boolean.TRUE.equals(item.isSelected)).count();
        assertEquals(2, selectedCount);
    }

    @Test
    public void testEnableSelectionMode_setsToggleTrueForAllItems() throws InterruptedException {
        // ARRANGE: Load sample movies
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();

        // ACT: Enable toggle selection mode on long press for Movie ID 2
        viewModel.enableToggle(2, true);

        // ASSERT: Toggle mode is true for all items, and Movie ID 2 is selected
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        for (MovieListResponseItem item : updatedList) {
            assertTrue("Toggle mode should be true for item ID " + item.id, item.toggle);
        }
        assertTrue("Movie ID 2 should be selected on long press", updatedList.get(1).isSelected);
    }

    @Test
    public void testDisableSelectionMode_resetsToggleAndSelections() throws InterruptedException {
        // ARRANGE: Enable toggle mode and select movie 1
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();
        viewModel.enableToggle(1, true);

        // ACT: Disable toggle selection mode (e.g., when back button is pressed)
        viewModel.enableToggle(0, false);

        // ASSERT: All items have toggle = false and isSelected = false
        List<MovieListResponseItem> updatedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(updatedList);
        for (MovieListResponseItem item : updatedList) {
            assertFalse("Toggle should be false for item ID " + item.id, item.toggle);
            assertFalse("Selection should be false for item ID " + item.id, item.isSelected);
        }
    }

    // ==========================================
    // SECTION B: DELETE FUNCTIONALITY TESTS
    // ==========================================

    @Test
    public void testDeleteOneSelectedMovie_removesSelectedItemOnly() throws InterruptedException {
        // ARRANGE: Load movies and select Movie ID 1
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();
        viewModel.toggleSelection(1);

        // ACT: Delete selected items
        viewModel.deleteItem();

        // ASSERT: List size decreases from 3 to 2, and Movie ID 1 is removed
        List<MovieListResponseItem> remainingList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(remainingList);
        assertEquals(2, remainingList.size());
        assertTrue("Movie ID 2 should remain", remainingList.stream().anyMatch(i -> i.id == 2));
        assertTrue("Movie ID 3 should remain", remainingList.stream().anyMatch(i -> i.id == 3));
        assertFalse("Movie ID 1 should be deleted", remainingList.stream().anyMatch(i -> i.id == 1));
    }

    @Test
    public void testDeleteMultipleSelectedMovies_removesAllSelectedItems() throws InterruptedException {
        // ARRANGE: Load movies and select Movie ID 1 and Movie ID 2
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();
        viewModel.toggleSelection(1);
        viewModel.toggleSelection(2);

        // ACT: Delete selected items
        viewModel.deleteItem();

        // ASSERT: Only 1 item (Movie ID 3) remains
        List<MovieListResponseItem> remainingList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(remainingList);
        assertEquals(1, remainingList.size());
        assertEquals(3, remainingList.get(0).id);
    }

    @Test
    public void testDeleteWhenNoMovieIsSelected_listRemainsUnchanged() throws InterruptedException {
        // ARRANGE: Load movies without selecting any item
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));
        viewModel.loadData();

        // ACT: Call deleteItem when zero items are selected
        viewModel.deleteItem();

        // ASSERT: All 3 movies remain intact
        List<MovieListResponseItem> remainingList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(remainingList);
        assertEquals(3, remainingList.size());
    }

    // ==========================================
    // SECTION C: SEARCH FUNCTIONALITY TESTS
    // ==========================================

    @Test
    public void testSearchMatchingMovieName_updatesSearchLiveData() throws InterruptedException {
        // ACT: Perform a search for "Inception"
        viewModel.search("Inception");

        // ASSERT: Search LiveData receives the searched query string
        String searchQuery = LiveDataTestUtil.getOrAwaitValue(viewModel.search);
        assertEquals("Inception", searchQuery);
    }

    @Test
    public void testSearchNonMatchingName_updatesSearchLiveData() throws InterruptedException {
        // ACT: Search for a query that matches no movies
        viewModel.search("Avatar");

        // ASSERT: Search LiveData is updated with the non-matching text
        String searchQuery = LiveDataTestUtil.getOrAwaitValue(viewModel.search);
        assertEquals("Avatar", searchQuery);
    }

    @Test
    public void testSearchWithEmptyText_updatesSearchLiveDataWithEmptyString() throws InterruptedException {
        // ACT: Search with empty string
        viewModel.search("");

        // ASSERT: Search LiveData receives empty string
        String searchQuery = LiveDataTestUtil.getOrAwaitValue(viewModel.search);
        assertEquals("", searchQuery);
    }

    // ==========================================
    // SECTION D: API AND LOADING STATE TESTS
    // ==========================================

    @Test
    public void testLoadData_success_updatesMovieListAndLoadingState() throws InterruptedException {
        // ARRANGE: Mock repository to return successful movie list
        List<MovieListResponseItem> sampleMovies = createSampleMovies();
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(sampleMovies));

        // ACT: Call loadData()
        viewModel.loadData();

        // ASSERT: movieList receives sample data, error is null, isLoading is false
        List<MovieListResponseItem> loadedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        Boolean loadingState = LiveDataTestUtil.getOrAwaitValue(viewModel.isLoading);
        String errorState = LiveDataTestUtil.getOrAwaitValue(viewModel.error);

        assertNotNull(loadedList);
        assertEquals(3, loadedList.size());
        assertEquals(Boolean.FALSE, loadingState);
        assertNull(errorState);
    }

    @Test
    public void testLoadData_failure_updatesErrorLiveDataAndLoadingState() throws InterruptedException {
        // ARRANGE: Mock repository to return network error
        String errorMessage = "Server Timeout: Unable to fetch movies";
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Error<>(new Exception(errorMessage)));

        // ACT: Call loadData()
        viewModel.loadData();

        // ASSERT: error LiveData contains the exception message, isLoading is false
        String actualError = LiveDataTestUtil.getOrAwaitValue(viewModel.error);
        Boolean loadingState = LiveDataTestUtil.getOrAwaitValue(viewModel.isLoading);

        assertEquals(errorMessage, actualError);
        assertEquals(Boolean.FALSE, loadingState);
    }

    @Test
    public void testLoadData_emptyApiResponse_updatesMovieListWithEmptyList() throws InterruptedException {
        // ARRANGE: Mock repository to return empty list
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(Collections.emptyList()));

        // ACT: Call loadData()
        viewModel.loadData();

        // ASSERT: movieList is empty and no error is posted
        List<MovieListResponseItem> loadedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        String errorState = LiveDataTestUtil.getOrAwaitValue(viewModel.error);

        assertNotNull(loadedList);
        assertTrue(loadedList.isEmpty());
        assertNull(errorState);
    }

    @Test
    public void testVerifyRepositoryInteractionsUsingMockito() {
        // ARRANGE: Mock repository response
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(createSampleMovies()));

        // ACT: Call loadData()
        viewModel.loadData();

        // ASSERT: Verify mockMainRepository.getShows() was invoked exactly once
        verify(mockMainRepository, times(1)).getShows();
    }

    @Test
    public void testVerifySelectListWhileEmpty() throws InterruptedException {
        // ARRANGE: Toggle item while list empty
        when(mockMainRepository.getShows()).thenReturn(new ResourceResult.Success<>(new ArrayList<>()));
        viewModel.loadData();

        // ACT: Enable toggle selection mode is true
        viewModel.enableToggle(0, true);

        // ASSERT: Verify the movie list remains empty
        List<MovieListResponseItem> loadedList = LiveDataTestUtil.getOrAwaitValue(viewModel.movieList);
        assertNotNull(loadedList);
        assertTrue(loadedList.isEmpty());
    }

    /**
     * Synchronous DirectExecutor for Unit Tests.
     * Executes submitted tasks immediately on the calling thread.
     */
    private static class DirectExecutorService extends AbstractExecutorService {
        private boolean isShutdown = false;

        @Override
        public void shutdown() { isShutdown = true; }

        @Override
        public List<Runnable> shutdownNow() { isShutdown = true; return Collections.emptyList(); }

        @Override
        public boolean isShutdown() { return isShutdown; }

        @Override
        public boolean isTerminated() { return isShutdown; }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
}
