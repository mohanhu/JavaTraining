
package com.codelooper.training.feature.home.presentation;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.codelooper.training.core.utils.ViewUtils;
import com.codelooper.training.databinding.FragmentHomeBinding;
import com.codelooper.training.databinding.MovieItemBinding;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    private HomeScreenViewModel viewModel;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                List<MovieListResponseItem> list = viewModel.movieList.getValue();
                if (list != null && list.stream()
                        .anyMatch(item ->
                                Boolean.TRUE.equals(item.toggle)
                        )
                ) {
                    viewModel.enableToggle(0, false);
                } else {
                    requireActivity().finish();
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@org.jspecify.annotations.NonNull View view, @org.jspecify.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeScreenViewModel.class);

        bindState(
                binding,
                viewModel
        );

        viewModel.loadData();
    }

    private void bindState(
            FragmentHomeBinding binding,
            HomeScreenViewModel viewModel
    ) {

        bindList(
                binding,
                viewModel.movieList,
                viewModel.isLoading,
                viewModel.error,
                viewModel.search
        );

        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (binding.swipeRefresh.isRefreshing()) {
                viewModel.loadData();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        binding.delete.setOnClickListener(v ->
                viewModel.deleteItem()
        );
    }

    private void bindList(
            FragmentHomeBinding binding,
            LiveData<List<MovieListResponseItem>> movieList,
            LiveData<Boolean> isLoading,
            LiveData<String> error,
            LiveData<String> search
    ) {

        MovieAdapter adapter = new MovieAdapter(
                item -> {
                    viewModel.toggleSelection(item);
                    System.out.println(TAG + ">> bindList: onItemClick  " + item);
                },
                item -> {
                    viewModel.enableToggle(item, true);
                    System.out.println(TAG + ">> bindList: onItemLongClick  " + item);
                }
        );
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));

        //viewLifecycleOwner.lifecycleScope.launch {
        //  repeatOnLifecycle(Lifecycle.State.STARTED){

        isLoading.observe(getViewLifecycleOwner(), load -> {
            binding.progressCircular.setVisibility(load ? View.VISIBLE : View.GONE);
            binding.rvList.setVisibility(load ? View.GONE : View.VISIBLE);
        });

        search.observe(getViewLifecycleOwner(), searchText -> {
            List<MovieListResponseItem> item = viewModel.movieList.getValue();
            if (item == null) return;
            List<MovieListResponseItem> list = Objects.requireNonNull(item).stream()
                    .filter(movieListResponseItem -> movieListResponseItem.name.contains(searchText))
                    .collect(Collectors.toList());
            adapter.submitList(list);
        });

        movieList.observe(getViewLifecycleOwner(), items -> {
            System.out.println(TAG + " List size: " + items.size());

            long deleteVisible = items.stream()
                    .filter(item -> Boolean.TRUE.equals(item.isSelected))
                    .count();

            System.out.println(TAG + " Selected count: " + deleteVisible);
            binding.deleteContainer.setVisibility(deleteVisible > 0 ? View.VISIBLE : View.GONE);
            binding.tvSelectCount.setText(String.valueOf(deleteVisible));
            adapter.submitList(items);
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                CountDownTimer timer = new CountDownTimer(500, 1000) {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println(TAG + ">> bindList: onTick  " + millisUntilFinished);
                        viewModel.search(s.toString());
                    }
                };
                timer.start();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    RecyclerView.LayoutManager layoutManager = binding.rvList.getLayoutManager();
                    if (layoutManager == null) return;

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        System.out.println(TAG + ">> onScrolled: load more " + visibleItemCount);
                    }
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


sealed interface UiState permits Loading, Success, Error {
}

final class Loading implements UiState {
}

record Success(String message) implements UiState {
    public Success(String message) {
        this.message = message;
    }
}

record Error(String message) implements UiState {
}


class MovieAdapter extends ListAdapter<MovieListResponseItem, MovieAdapter.ViewHolder> {

    private final Consumer<Integer> onItemClick;
    private final Consumer<Integer> onItemLongClick;

    protected MovieAdapter(
            Consumer<Integer> onItemClick,
            Consumer<Integer> onItemLongClick
    ) {
        super(MovieDiffCallback);
        this.onItemClick = onItemClick;
        this.onItemLongClick = onItemLongClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(
                getItem(position),
                onItemClick,
                onItemLongClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            System.out.println("onBindViewHolder payloads " + payloads.get(0));
            if (payloads.get(0) instanceof Bundle bundle) {
                if (bundle.containsKey("toggle")) {
                    holder.binding.cbSelect.setVisibility(bundle.getBoolean("toggle") ? View.VISIBLE : View.GONE);
                }
                if (bundle.containsKey("isSelected")) {
                    System.out.println("onBindViewHolder payloads isSelected " + bundle.getBoolean("isSelected"));
                    holder.binding.cbSelect.setChecked(bundle.getBoolean("isSelected"));
                }
            }
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final MovieItemBinding binding;

        public ViewHolder(
                MovieItemBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder from(ViewGroup parent) {
            return new ViewHolder(
                    MovieItemBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }

        void bind(
                MovieListResponseItem item,
                Consumer<Integer> onItemClick,
                Consumer<Integer> onItemLongClick
        ) {

            Glide.with(binding.getRoot())
                    .load(item.image)
                    .centerCrop()
                    .into(binding.ivMovie);

            binding.cbSelect.setOnCheckedChangeListener(null);

            binding.cbSelect.setVisibility(
                    item.toggle ? View.VISIBLE : View.GONE
            );

            binding.cbSelect.setChecked(item.isSelected);

            binding.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.isSelected = isChecked;
            });

            ViewUtils.setOnDebounceClickListener(
                    binding.ivMovie,
                    view -> onItemClick.accept(item.id)
            );

            binding.ivMovie.setOnLongClickListener(view -> {
                onItemLongClick.accept(item.id);
                return true;
            });
        }
    }

    private static final DiffUtil.ItemCallback<MovieListResponseItem> MovieDiffCallback = new DiffUtil.ItemCallback<MovieListResponseItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieListResponseItem oldItem, @NonNull MovieListResponseItem newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull MovieListResponseItem oldItem,
                @NonNull MovieListResponseItem newItem
        ) {
            return oldItem.equals(newItem);
        }

        @NonNull
        @Override
        public Object getChangePayload(@NonNull MovieListResponseItem oldItem, @NonNull MovieListResponseItem newItem) {
            Bundle bundle = new Bundle();

            if (oldItem.toggle != newItem.toggle) {
                bundle.putBoolean("toggle", newItem.toggle);
            }
            if (oldItem.isSelected != newItem.isSelected) {
                bundle.putBoolean("isSelected", newItem.isSelected);
            }
            return bundle;
        }
    };
}
