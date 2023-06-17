package com.bearya.robot.programme.retrofit;

import com.bearya.robot.programme.entity.ImageSearchEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import io.reactivex.Observable;

public interface ApiService {

    @POST("/search/photos")
    @FormUrlEncoded
    Observable<ImageSearchEntity> searchImageByName(@Field("query") String imageName, @Field("page") int page);

}
