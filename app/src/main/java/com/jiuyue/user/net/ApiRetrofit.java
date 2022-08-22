package com.jiuyue.user.net;

import com.jiuyue.user.global.Constant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private ApiServer apiServer;
    //设置所有的超时时间
    public static final int SUCCESS_CODE = 1;
    private static final int DEFAULT_TIMEOUT = 15;

    //创建日志拦截器
    public ApiRetrofit() {
        OkHttpClient.Builder OkHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClientBuilder
                //创建的日志拦截器
                .addInterceptor(new ParamsInterceptor())
                .addInterceptor(getHttpInterceptor())
                //设置连接超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //设置写入文件的超时时间
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //设置读取文件的超时时间
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true);


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClientBuilder.build())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }


    private HttpLoggingInterceptor getHttpInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    //单例模式
    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

    public static void refreshBaseUrl() {
        apiRetrofit = null;
        getInstance();
    }

    /**
     * 根据地址获取json
     */
    public void getDataForJson(String address, ResultListener<String> listener) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(address)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            emitter.onError(e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) {
                            try {
                                if (response.code() == 1) {
                                    emitter.onNext(response.body().string());
                                } else {
                                    emitter.onError(new Throwable("获取错误"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                emitter.onError(e);
                            }
                        }
                    });
                }).subscribeOn(Schedulers.io()) //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) //回到主线程去处理请求注册结果
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (listener != null) {
                            listener.onSuccess(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e.getMessage(), -1);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}