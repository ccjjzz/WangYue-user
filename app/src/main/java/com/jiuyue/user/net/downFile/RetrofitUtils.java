package com.jiuyue.user.net.downFile;


import com.jiuyue.user.global.Constant;
import com.jiuyue.user.net.ApiServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static <T> ApiServer getRetrofitService(final RetrofitCallback<T> callback) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        clientBuilder.addInterceptor(new ParamsInterceptor());
        clientBuilder.addInterceptor(chain -> {
            okhttp3.Response response = chain.proceed(chain.request());
            //将ResponseBody转换成我们需要的FileResponseBody
            return response.newBuilder().body(new FileResponseBody<T>(response.body(), callback)).build();
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_HOST_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiServer.class);
    }

    public static void downLoadFile(String url, String path, DownloadCallBack downloadCallBack) {
        RetrofitCallback<ResponseBody> callback = new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                downloadCallBack.onStart();
                new Thread(() -> {
                    try {
                        InputStream is = response.body().byteStream();
                        //获取文件总长度
                        long totalLength = is.available();
                        File file = new File(path);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            //此处进行更新操作
                            //len即可理解为已下载的字节数
                            //onLoading(len, totalLength);
                        }
                        fos.flush();
                        fos.close();
                        bis.close();
                        is.close();
                        //此处就代表更新结束
                        downloadCallBack.onSuccess();
                    } catch (IOException e) {
                        e.printStackTrace();
                        downloadCallBack.onFailure(e);
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                HttpLoggingInterceptor.Logger.DEFAULT.log("onFailure:" + t.toString());
                downloadCallBack.onFailure(t);
            }

            @Override
            public void onLoading(long total, long progress) {
                super.onLoading(total, progress);
                int pro = (int) (progress * 100 / total);
                if (total < 1000) {
                    pro = 0;
                }
                HttpLoggingInterceptor.Logger.DEFAULT.log("onLoading:" + total + "/" + progress + "/" + pro);
                downloadCallBack.onProgress(pro);
            }
        };

        Call<ResponseBody> call = getRetrofitService(callback).downloadFile(url);
        call.enqueue(callback);
    }

    public interface DownloadCallBack {
        void onStart();

        void onSuccess();

        void onFailure(Throwable t);

        void onProgress(int progress);
    }
}
