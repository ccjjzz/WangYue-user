package com.jiuyue.user.net;


import com.jiuyue.user.entity.CityListBean;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.NumberEntity;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.entity.ReserveTimeEntity;
import com.jiuyue.user.entity.TechnicianEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.entity.TrafficEntity;
import com.jiuyue.user.entity.UserInfoEntity;
import com.jiuyue.user.entity.req.PlaceOrderReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiServer {
    /**
     * 下载文件
     * 如果下载大文件的一定要加上  @Streaming  注解
     *
     * @param fileUrl 文件的路径
     * @return 请求call
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    //上传带参数的多文件
    @Multipart
    @POST
    Observable<HttpResponse<Object>> uploadParamAndFiles(
            @Url String url,
            @PartMap Map<String, RequestBody> params,
            @Part List<MultipartBody.Part> files
    );

    /**************************************用户*******************************************/

    /*获取手机验证码
     * type:1=技师注册 2=技师找回密码 3=合伙人注册 4=合伙人找回密码 5=商户注册 6=商户找回密码 7=用户注册登录 8=用户修改手机号
     * */
    @POST("/api/common/sendMobileSms")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> sendMobileSms(@Field("mobile") String mobile, @Field("type") int type);

    //获取城市列表
    @POST("/api/common/citylist")
    @FormUrlEncoded
    Observable<HttpResponse<CityListBean>> cityList(@Field("os") String os);

    //获取配置信息
    @POST("/api/user/config")
    @FormUrlEncoded
    Observable<HttpResponse<ConfigEntity>> getConfig(@Field("os") String os);

    //获取个人信息
    @POST("/api/user/info")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoEntity>> getUserInfo(@Field("os") String os);

    //登录
    @POST("/api/user/login")
    @FormUrlEncoded
    Observable<HttpResponse<TokenEntity>> login(@Field("mobile") String mobile, @Field("smsCode") String smsCode);

    //获取隐私号码
    @POST("/api/user/privateNumber")
    @FormUrlEncoded
    Observable<HttpResponse<NumberEntity>> privateNumber(@FieldMap Map<String, Object> map);

    //首页数据
    @POST("/api/user/index")
    @FormUrlEncoded
    Observable<HttpResponse<HomeEntity>> index(@Field("os") String os);

    //技师列表
    @POST("/api/user/technicianList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<TechnicianEntity>>> technicianList(@FieldMap Map<String, Object> map);

    //发现TAB数据
    @POST("/api/user/dynamicList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<DynamicEntity>>> dynamicList(@Field("tabId") int tabId, @Field("page") int page);

    //项目套餐详情
    @POST("/api/user/productInfo")
    @FormUrlEncoded
    Observable<HttpResponse<ProductEntity>> productInfo(@Field("productId") int productId);

    //获取优惠券
    @POST("/api/user/discountList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<CouponEntity>>> discountList(@FieldMap Map<String, Object> map);

    //收藏/取消 套餐
    @POST("/api/user/collectProduct")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> collectProduct(@Field("productId") int productId, @Field("type") int type);

    //技师可预约时间列表
    @POST("/api/user/technicianServiceTimeList")
    @FormUrlEncoded
    Observable<HttpResponse<List<ReserveTimeEntity>>> technicianServiceTimeList(@Field("techId") int techId);

    //获取出行方式
    @POST("/api/user/orderTrafficSet")
    @FormUrlEncoded
    Observable<HttpResponse<TrafficEntity>> orderTrafficSet(@FieldMap HashMap<String,Object> map);

    //用户下单
    @POST("/api/user/orderProduct")
    Observable<HttpResponse<OrderInfoEntity>> orderProduct(@Body PlaceOrderReq req);

}
