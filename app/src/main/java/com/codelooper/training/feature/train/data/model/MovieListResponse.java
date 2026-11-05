package com.codelooper.training.feature.train.data.model;

import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.codelooper.training.feature.train.data.model.MovieListResponseItemDto;

public class MovieListResponse extends ArrayList<MovieListResponseItemDto> {

    public List<MovieListResponseItem> toMovieListResponse() {
        return this.stream()
                .map(MovieListResponseItemDto::toMovieListResponseItem)
                .collect(Collectors.toList());
    }

    public static List<MovieListResponseItemDto> createList() {
        List<MovieListResponseItemDto> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MovieListResponseItemDto item = new MovieListResponseItemDto(
                    i,
                    new ImageDto("https://picsum.photos/200/300?random=" + i),
                    "Test Show " + i,
                    new RatingDto(8.5)
            );
            list.add(item);
        };
        return list;
    }
}


class ImageDto {
    @SerializedName("medium")
    String medium;

    public ImageDto(String url) {
        this.medium = url;
    }
}

class RatingDto {
    @SerializedName("average")
    Double average;

    public RatingDto(Double rating) {
        this.average = rating;
    }
}