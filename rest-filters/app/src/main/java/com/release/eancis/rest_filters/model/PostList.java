package com.release.eancis.rest_filters.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Enrico on 08/06/2018.
 */

public class PostList {
    @SerializedName("posts")
    private List<PostItem> posts;


    public PostList(List<PostItem> _posts){
        this.posts = _posts;
    }

    public List<PostItem> getPosts() {
        return this.posts;
    }

    public void setPosts(List<PostItem> _posts) {
        this.posts = _posts;
    }

    public PostItem getPostItem(Integer position){
        return posts.get(position);
    }

    public Integer getSize(){
        return posts.size();
    }
}
