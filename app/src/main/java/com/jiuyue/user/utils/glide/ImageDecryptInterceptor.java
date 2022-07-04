package com.jiuyue.user.utils.glide;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageDecryptInterceptor implements Interceptor {
    private static final String TAG = ImageDecryptInterceptor.class.getSimpleName();

    // 私有构造器
    public ImageDecryptInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        // 关闭输入流
        inputStream.close();
        byte[] data = os.toByteArray();
        //byte[] newImgData = new byte[1024];
        try {
            data = TestAesUtils.desEncrypt(data);
            MediaType mediaType = Objects.requireNonNull(response.body()).contentType();
            ResponseBody newResponseBody = ResponseBody.create(data, mediaType);
            response = response.newBuilder().body(newResponseBody).build();
        } catch (Exception e) {
            Log.d(TAG, "图片解码异常");
            e.printStackTrace();
            return chain.proceed(request);
        } finally {
            os.close();
        }
        return response;
    }
}
