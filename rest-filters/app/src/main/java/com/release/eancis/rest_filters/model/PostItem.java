package com.release.eancis.rest_filters.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Enrico on 11/06/2018.
 */

public class PostItem {
    @SerializedName("id")
    private Long id;
    @SerializedName("user_id")
    private Long user_id;
    @SerializedName("published_at")
    private String published_at;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;

    public PostItem(Long _id,
                    Long _userId,
                    String _title,
                    String _description,
                    String _publishedAt,
                    String _image){

        this.id = _id;
        this.user_id = _userId;
        this.published_at = _publishedAt;
        this.title = _title;
        this.description = _description;
        this.image = _image;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long _id) {
        this.id = _id;
    }

    public Long getUserId() {
        return this.user_id;
    }

    public void setUserId(Long _userId) {
        this.user_id = _userId;
    }

    public String getPublishedAt() {
        return this.published_at;
    }

    public void setPublishedAt(String _publishedAt) {
        this.published_at = _publishedAt;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String _description) {
        this.description = _description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String _image) {
        this.image = _image;
    }
}
