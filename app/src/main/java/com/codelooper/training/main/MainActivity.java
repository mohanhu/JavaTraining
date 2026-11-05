package com.codelooper.training.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.codelooper.training.R;
import com.codelooper.training.databinding.ActivityMainBinding;

import java.util.function.Consumer;
import java.util.function.Function;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        bindState(
                binding,
                viewModel
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