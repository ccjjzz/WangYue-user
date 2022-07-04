package com.jiuyue.user.net;

import com.jiuyue.user.global.Constant;
import com.jiuyue.user.utils.AppUtils;
import com.jiuyue.user.App;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    private static ApiRetrofit apiRetrofit;
    private final Retrofit retrofit;
    private ApiServer apiServer;
    private String TAG = "ApiRetrofit %s";
    //设置所有的超时时间
    private static final int DEFAULT_TIMEOUT = 15;

    //创建日志拦截器
    public ApiRetrofit() {
        OkHttpClient.Builder OkHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClientBuilder
                //创建的日志拦截器
                .addInterceptor(interceptor)
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Logger.d("zcb", "OkHttp====Message:" + message));
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


    public Interceptor interceptor = chain -> {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response proceed = chain.proceed(request);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        if ("POST".equals(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                JsonObject jsonObject = new JsonObject();
                if (formBody != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        jsonObject.addProperty(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                    jsonObject.addProperty("appVersion", AppUtils.getVersionName(App.getAppContext()));
                    String json = jsonObject.toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    request = request.newBuilder()
                            .post(requestBody)
                            .build();
                }
            }
        }
        Response response = chain.proceed(request);
        String content = Objects.requireNonNull(response.body()).string();
        //解密
        //String newResponseBodyStr = AESUtil.getInstance().decryptWithBase64(content);
        //重新构造新request
        MediaType mediaType = Objects.requireNonNull(proceed.body()).contentType();
        Logger.wtf(TAG, "----------Request Start----------------");
        printParams(request.body());
        Logger.e(TAG, "| " + request.toString() + "===========" + request.headers().toString());
        Logger.json(content);
        Logger.e(content);
        Logger.wtf(TAG, "----------Request End:" + duration + "毫秒----------");
        return proceed.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    };


    private void printParams(RequestBody body) {
        if (body != null) {
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    charset = mediaType.charset(charset);
                }
                String params = buffer.readString(charset);
                Logger.e(TAG, "请求参数： | " + params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据地址获取json
     * @param address
     * @param observer
     */
    public void getDataForJson(String address, Observer<String> observer) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(address)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == 200) {
                        emitter.onNext(response.body().string());
                    }
                }
            });
        }).subscribeOn(Schedulers.io()) //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) //回到主线程去处理请求注册结果
                .subscribe(observer);
    }
}