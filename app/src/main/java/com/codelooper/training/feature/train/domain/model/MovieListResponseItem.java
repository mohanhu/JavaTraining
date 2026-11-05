package com.codelooper.training.feature.train.domain.model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class MovieListResponseItem {
    public int id;
    public String image;
    public String name;
    public Double rating;

    public Boolean toggle = false;
    public Boolean isSelected = false;

    public MovieListResponseItem() {

    }

    MovieListResponseItem create(
            int id,
            String image,
            String name,
            Double rating
    ) {
        MovieListResponseItem item = new MovieListResponseItem();
        item.id = id;
        item.image = image;
        item.name = name;
        item.rating = rating;
        return item;
    }

    public MovieListResponseItem copy() {
        MovieListResponseItem item = new MovieListResponseItem();
        item.id = this.id;
        item.image = this.image;
        item.name = this.name;
        item.rating = this.rating;
        item.toggle = this.toggle;
        item.isSelected = this.isSelected;
        return item;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MovieListResponseItem) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.image, that.image) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.rating, that.rating) &&
                Objects.equals(this.toggle, that.toggle) &&
                Objects.equals(this.isSelected, that.isSelected)
                ;
    }

}