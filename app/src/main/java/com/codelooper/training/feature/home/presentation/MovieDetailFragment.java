package com.codelooper.training.feature.home.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelooper.training.databinding.FragmentMovieDetailBinding;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailFragment extends Fragment {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private FragmentMovieDetailBinding binding;

    public MovieDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindState(
                binding
        );

    }

    private void bindState(
            FragmentMovieDetailBinding binding
    ) {

        String movieFile = getArguments().getString("movie","");
        if (movieFile != null) {
            System.out.println(TAG + ">> bindState: " + movieFile);
            Glide.with(requireContext())
                    .load(movieFile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivMovieDetail);
        }

    }
}