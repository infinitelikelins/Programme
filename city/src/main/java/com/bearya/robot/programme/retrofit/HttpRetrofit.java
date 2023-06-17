package com.bearya.robot.programme.retrofit;

import androidx.annotation.NonNull;

import com.bearya.robot.base.BuildConfig;
import com.bearya.robot.programme.entity.ImageSearchEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRetrofit {

    private static HttpRetrofit httpRetrofit;
    private final ApiService apiService;

    private HttpRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> cookies) {
                cookieStore.put(httpUrl.host(), cookies);
            }

            @NonNull
            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : Collections.emptyList();
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5 * 1000L, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 1000L, TimeUnit.MILLISECONDS)
                .callTimeout(5 * 1000L, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://api.fotomore.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private static ApiService getApiService() {
        if (httpRetrofit == null) {
            httpRetrofit = new HttpRetrofit();
        }
        return httpRetrofit.apiService;
    }

    public static Observable<ImageSearchEntity> searchImageByName(String imageName, int page) {
        return getApiService().searchImageByName(imageName, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}