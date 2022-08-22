package com.jiuyue.user.net;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.jiuyue.user.App;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.utils.AppUtils;
import com.jiuyue.user.utils.glide.AesUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class ParamsInterceptor implements Interceptor{
    private final String TAG = "ParamsInterceptor %s";
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        //公共参数
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("os", "android");
        jsonObject.addProperty("phoneType", AppUtils.getOsName());  //手机型号
        jsonObject.addProperty("imei", App.getSharePre().getString(SpKey.UUID)); //手机唯一序列号
        jsonObject.addProperty("appVersion", AppUtils.getVersionName(App.getAppContext()));   //app版本号
        jsonObject.addProperty("channel", App.getSharePre().getString(SpKey.CHANNEL)); //分发渠道
        jsonObject.addProperty("id", App.getSharePre().getInt(SpKey.USER_ID));//用户id,没登录时传0
        jsonObject.addProperty("token", App.getSharePre().getString(SpKey.TOKEN)); //登录令牌,没登录时空串
        jsonObject.addProperty("cityCode", App.getSharePre().getString(SpKey.CITY_CODE));  //城市编码
        jsonObject.addProperty("latitude", App.getSharePre().getDouble(SpKey.LATITUDE)); //纬度(没开启定位传0
        jsonObject.addProperty("longitude", App.getSharePre().getDouble(SpKey.LONGITUDE));//经度(没开启定位传0)

        if ("POST".equals(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                if (formBody != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        jsonObject.addProperty(formBody.encodedName(i), formBody.value(i));
                    }
                }
                String json = jsonObject.toString();
                Logger.wtf(TAG, "请求原始参数： | " + json);
                //加密
                String encryptJson = AesUtils.getInstance().encryptWithBase64(json);
//                //构建新的请求字符串
//                String newRequestBodyStr = newJson.toString();
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newRequestBodyStr);
//                request = request.newBuilder()
//                        .post(requestBody)
//                        .build();
                // 构造新的请求表单
                FormBody.Builder builder = new FormBody.Builder();
                //追加新的参数
                builder.add("reqData", encryptJson);
                //构造新的请求体
                request = request.newBuilder()
                        .post(builder.build())
                        .build();
            } else {
                RequestBody body = request.body();
                if (body != null) {
                    Buffer buffer = new Buffer();
                    try {
                        body.writeTo(buffer);
                        Charset charset = StandardCharsets.UTF_8;
                        MediaType mediaType = body.contentType();
                        if (mediaType != null) {
                            charset = mediaType.charset(charset);
                        }
                        //原参数
                        String params = buffer.readString(charset);
                        //公共参数
                        String json = jsonObject.toString();
                        //拼接原参数和公共参数
                        String oldParams = params.substring(0, params.length() - 1);
                        String newParams = json.substring(1);
                        String reqParams = oldParams + "," + newParams;
                        Logger.e(TAG, "拼接后请求参数： | " + reqParams);
                        //加密
                        String encryptJson = AesUtils.getInstance().encryptWithBase64(reqParams);
                        Logger.e(TAG, "拼接后加密请求参数： | " + encryptJson);
                        // 构造新的请求表单
                        FormBody.Builder builder = new FormBody.Builder();
                        //追加新的参数
                        builder.add("reqData", encryptJson);
                        //构造新的请求体
                        request = request.newBuilder()
                                .post(builder.build())
                                .build();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        Response response = chain.proceed(request);
//        String newResponseBodyStr = AesUtils.getInstance().decryptWithBase64(content); //解密
        String newResponseBodyStr = Objects.requireNonNull(response.body()).string();
        //重新构造新response
        MediaType mediaType = Objects.requireNonNull(response.body()).contentType();
        ResponseBody newResponseBody = ResponseBody.create(mediaType, newResponseBodyStr);
        Logger.wtf(TAG, "----------Request Start----------------");
        printParams(request.body());
        Logger.e(TAG, "| " + request.toString() + "===========" + request.headers().toString());
        Logger.json(newResponseBodyStr);
        Logger.e(TAG, "| 响应结果：" + newResponseBodyStr);
        Logger.wtf(TAG, "----------Request End:" + duration + "毫秒----------");
        response = response.newBuilder().body(newResponseBody).build();
        return response;
    }

    private void printParams(RequestBody body) {
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
