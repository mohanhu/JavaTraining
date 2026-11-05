package com.codelooper.training.feature.train.data.model;

import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.google.gson.annotations.SerializedName;

public class MovieListResponseItemDto {
    @SerializedName("id")
    int id;
    @SerializedName("image")
    ImageDto image;
    @SerializedName("name")
    String name;
    @SerializedName("rating")
    RatingDto rating;

    public MovieListResponseItemDto(
            int id,
            ImageDto image,
            String name,
            RatingDto rating
    ) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.rating = rating;
    }

    public MovieListResponseItem toMovieListResponseItem() {
        MovieListResponseItem item = new MovieListResponseItem();
        item.id = this.id;
        item.image = image != null ? image.medium : null;
        item.name = this.name;
        item.rating = rating != null ? rating.average : null;
        return item;
    }
}
