package com.jiuyue.user.mvp.model;

import androidx.annotation.NonNull;

import com.jiuyue.user.entity.CityListBean;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.entity.NumberEntity;
import com.jiuyue.user.entity.ReserveTimeEntity;
import com.jiuyue.user.entity.UserInfoEntity;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;
import com.jiuyue.user.net.ResultListener;
import com.jiuyue.user.net.uploadFile.UploadFileRetrofit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommonModel {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    /**
     * 获取验证码 1=技师注册 2=技师找回密码 3=合伙人注册 4=合伙人找回密码 5=商户注册 6=商户找回密码 7=用户注册登录 8=用户修改手机号
     */
    public void sendMobileSms(String mobile, int type, ResultListener<Object> resultListener) {
        apiServer.sendMobileSms(mobile, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    public void onSuccess(HttpResponse<Object> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }


    /**
     * 获取公共配置信息
     */
    public void getConfig(ResultListener<ConfigEntity> resultListener) {
        apiServer.getConfig("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ConfigEntity>() {
                    @Override
                    public void onSuccess(HttpResponse<ConfigEntity> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    /**
     * 获取个人信息
     */
    public void getUserInfo(ResultListener<UserInfoEntity> resultListener) {
        apiServer.getUserInfo("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserInfoEntity>() {
                    @Override
                    public void onSuccess(HttpResponse<UserInfoEntity> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }


    /**
     * 获取城市列表
     */
    public void getCityList(ResultListener<CityListBean> resultListener) {
        apiServer.cityList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<CityListBean>() {
                    @Override
                    public void onSuccess(HttpResponse<CityListBean> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    /**
     * 获取隐私号码
     */
    public void getPrivateNumber(String selfNumber, String callNumber, String orderNo, ResultListener<NumberEntity> resultListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("callerNumber", selfNumber);
        map.put("calleeNumber", callNumber);
        map.put("orderNo", orderNo);
        apiServer.privateNumber(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<NumberEntity>() {
                    @Override
                    public void onSuccess(HttpResponse<NumberEntity> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    /**
     * 获取隐私号码
     */
    public void technicianServiceTimeList(int techId, ResultListener<List<ReserveTimeEntity>> resultListener) {
        apiServer.technicianServiceTimeList(techId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<ReserveTimeEntity>>() {
                    @Override
                    public void onSuccess(HttpResponse<List<ReserveTimeEntity>> data) {
                        resultListener.onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String msg, int code) {
                        resultListener.onError(msg, code);
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    /**
     * 修改个人资料
     */
    public void modifyInfo(@NonNull String name, int gender,File headImg, ResultListener<Object> resultListener) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("gender", gender);
        HashMap<String, File> fileMap = new HashMap<>();
        if (headImg != null) {
            fileMap.put("headImg", headImg);
        }
        UploadFileRetrofit.uploadParamAndFiles("/api/user/modifyInfo", paramsMap, fileMap, new BaseObserver<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(HttpResponse<Object> data) {
                resultListener.onSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                resultListener.onError(msg, code);
            }

            @Override
            public void complete() {

            }
        });
    }
}
