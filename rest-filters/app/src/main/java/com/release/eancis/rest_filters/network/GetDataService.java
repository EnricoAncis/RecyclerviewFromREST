package com.release.eancis.rest_filters.network;

import com.release.eancis.rest_filters.model.PostList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Enrico on 08/06/2018.
 */

public interface GetDataService {

    @GET("/v2/59f2e79c2f0000ae29542931")
    Call<PostList> getAllPosts();

}
