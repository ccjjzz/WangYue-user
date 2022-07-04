package com.jiuyue.user.net;


import com.jiuyue.user.mvp.model.entity.UserRegisterBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServer {
    //注册
    @POST("/api/userReg.ashx")
    @FormUrlEncoded
    Observable<HttpResponse<UserRegisterBean>> registerUser(@FieldMap Map<String, Object> map);
}
