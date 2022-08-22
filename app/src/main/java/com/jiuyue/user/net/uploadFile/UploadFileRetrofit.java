package com.jiuyue.user.net.uploadFile;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.jiuyue.user.App;
import com.jiuyue.user.global.Constant;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.utils.AppUtils;
import com.jiuyue.user.utils.glide.AesUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadFileRetrofit {
    public static final String TAG = "UploadFileRetrofit %s";

    /**
     * 带参数的多文件上传
     *
     * @param params 业务参数map
     * @param files  文件map
     */
    public static void uploadParamAndFiles(
            @NonNull String url,
            @NonNull Map<String, Object> params,
            @NonNull Map<String, File> files,
            BaseObserver<Object> observer
    ) {
        //公共参数
        params.put("os", "android");
        params.put("phoneType", AppUtils.getOsName());  //手机型号
        params.put("imei", App.getSharePre().getString(SpKey.UUID)); //手机唯一序列号
        params.put("appVersion", AppUtils.getVersionName(App.getAppContext()));   //app版本号
        params.put("channel", App.getSharePre().getString(SpKey.CHANNEL)); //分发渠道
        params.put("id", App.getSharePre().getInt(SpKey.USER_ID));//用户id,没登录时传0
        params.put("token", App.getSharePre().getString(SpKey.TOKEN)); //登录令牌,没登录时空串
        params.put("cityCode", App.getSharePre().getString(SpKey.CITY_CODE));  //城市编码
        params.put("latitude", App.getSharePre().getDouble(SpKey.LATITUDE)); //纬度(没开启定位传0
        params.put("longitude", App.getSharePre().getDouble(SpKey.LONGITUDE));//经度(没开启定位传0)
        //业务参数map转成json字符串
        String reqParams = new Gson().toJson(params);
        Logger.wtf(TAG, "请求原始参数： | " + reqParams);
        //加密
        String encryptJson = AesUtils.getInstance().encryptWithBase64(reqParams);
        //构建最终业务参数map
        HashMap<String, RequestBody> reqMap = new HashMap<>();
        reqMap.put("reqData", toRequestBody(encryptJson));

        //构建文件MultipartBody
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        Set<String> keySet1 = files.keySet();
        for (String key : keySet1) { // 遍历keySet，并取出key，value的值
            File file = files.get(key);
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                fileParts.add(MultipartBody.Part.createFormData(key, file.getName(), requestFile));
            }
        }
        //请求接口
        getApiServer().uploadParamAndFiles(url, reqMap, fileParts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private static ApiServer getApiServer() {
        final int DEFAULT_TIMEOUT = 15;
        OkHttpClient.Builder OkHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClientBuilder
                //创建的日志拦截器
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    long startTime = System.currentTimeMillis();
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    Response response = chain.proceed(request);
                    String newResponseBodyStr = Objects.requireNonNull(response.body()).string();
                    //必须重新构造新response,否则回造成调用两次okhttp会抛出java.lang.IllegalStateException: closed
                    MediaType mediaType = Objects.requireNonNull(response.body()).contentType();
                    ResponseBody newResponseBody = ResponseBody.create(mediaType, newResponseBodyStr);
                    Logger.wtf(TAG, "----------Request Start----------------");
                    Logger.e(TAG, "| " + request.toString() + "===========" + request.headers().toString());
                    Logger.json(newResponseBodyStr);
                    Logger.e(TAG, "| 响应结果：" + newResponseBodyStr);
                    Logger.wtf(TAG, "----------Request End:" + duration + "毫秒----------");
                    response = response.newBuilder().body(newResponseBody).build();
                    return response;
                })
                //设置连接超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //设置写入文件的超时时间
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //设置读取文件的超时时间
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClientBuilder.build())
                .build();
        return retrofit.create(ApiServer.class);
    }

    private static void printParams(RequestBody body) {
        if (body != null) {
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
                Charset charset = StandardCharsets.UTF_8;
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
}
