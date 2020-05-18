package com.example.daggerpractice.network.main;

import com.example.daggerpractice.models.Post;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainApi {

    //Call format: BASE_URL/posts?userId=1

    @GET("posts")
    Flowable<List<Post>> getPostsFromUser(
            @Query("userId") int userId
    );

}
