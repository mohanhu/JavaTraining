package com.codelooper.training.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.codelooper.training.R;
import com.codelooper.training.databinding.ActivityMainBinding;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ❌ Intentional memory leak
    private static MainActivity leakedActivity;

    private MainViewModel viewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        leakedActivity = this;

        EdgeToEdge.enable(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setUpNavigation(binding,navController);

        bindState(
                binding,
                viewModel
        );
    }

    private void observeKeyboard(
            ActivityMainBinding binding,
            NavController navController
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.getRoot(),
                (v, insets) -> {

                    boolean isKeyboardVisible = insets.isVisible(
                            WindowInsetsCompat.Type.ime()
                    );

                    System.out.println("isKeyboardVisible: " + isKeyboardVisible);

                    if (navController!=null){
                        int destinationId = Objects.requireNonNull(navController.getCurrentDestination()).getId();
                        boolean isBottomNavVisible = binding.bottomNavigationView.getMenu().findItem(destinationId) != null;

                        System.out.println("isKeyboardVisible: isBottomNavVisible " + isBottomNavVisible);

                        if (isKeyboardVisible && isBottomNavVisible) {
                            binding.bottomNavigationView.setVisibility(View.GONE);
                        } else {
                            binding.bottomNavigationView.setVisibility(isBottomNavVisible ? View.VISIBLE : View.GONE);
                        }
                    }

                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );
    }

    private void setUpNavigation(
            ActivityMainBinding binding,
            NavController navController
    ) {

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            NavController finalNavController = navController;
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                int itemId = item.getItemId();

                if (itemId == R.id.homeFragment) {
                    finalNavController.navigate(R.id.homeFragment);
                    return true;
                }

                return false;
            });

            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                    int destinationId = navDestination.getId();
                    boolean isBottomNavVisible = binding.bottomNavigationView.getMenu().findItem(destinationId) != null;
                    binding.bottomNavigationView.setVisibility(
                            isBottomNavVisible ? View.VISIBLE : View.GONE
                    );
                }
            });
        }

        observeKeyboard(
                binding,
                navController
        );
    }

    private void bindState(
            ActivityMainBinding binding,
            MainViewModel viewModel
    ) {

        Consumer<String> append = value -> {
            String update = value + "new";
            System.out.println(TAG + ">> update: " + update);
        };

        Runnable runnable = () -> {
            System.out.println(TAG + ">> update: Welcome runnable");
        };

        Function<String, String> replace = text -> {
            return text.replace("a", "m");
        };

        append.accept("Welcome");
        runnable.run();
        String value = replace.apply("Angael");
        System.out.println(TAG + ">> Function: " + value);
    }
}