package com.jiuyue.user.net;


import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityListBean;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.NumberEntity;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.entity.PayEntity;
import com.jiuyue.user.entity.PayTypeEntity;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.entity.ReserveTimeEntity;
import com.jiuyue.user.entity.TechnicianDynamicEntity;
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

    //技师详情
    @POST("/api/user/technicianInfo")
    @FormUrlEncoded
    Observable<HttpResponse<TechnicianEntity>> technicianInfo(@Field("techId") int techId);

    //技师动态列表
    @POST("/api/user/technicianDynamicList")
    @FormUrlEncoded
    Observable<HttpResponse<TechnicianDynamicEntity>> technicianDynamicList(@Field("techId") int techId, @Field("page") int page);

    //关注、取关技师
    @POST("/api/user/followTechnician")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> followTechnician(@Field("techId") int techId, @Field("type") int type);

    //发现TAB数据
    @POST("/api/user/dynamicList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<DynamicEntity>>> dynamicList(@Field("tabId") int tabId, @Field("page") int page);

    //点赞/取消点赞动态
    @POST("/api/user/likeDynamic")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> likeDynamic(@Field("techId") int techId, @Field("dynamicId") int dynamicId, @Field("type") int type);

    //收藏/取消收藏动态
    @POST("/api/user/collectDynamic")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> collectDynamic(@Field("techId") int techId, @Field("dynamicId") int dynamicId, @Field("type") int type);

    //项目套餐详情
    @POST("/api/user/productInfo")
    @FormUrlEncoded
    Observable<HttpResponse<ProductEntity>> productInfo(@Field("productId") int productId);

    //收藏套餐列表
    @POST("/api/user/collectProductList")
    @FormUrlEncoded
    Observable<HttpResponse<FollowCommoditBean>> collectProductList(@Field("os") String os);

    //关注技师列表
    @POST("/api/user/followTechnicianList")
    @FormUrlEncoded
    Observable<HttpResponse<FollowTechnicianBean>> followTechnicianList(@Field("os") String os);

    //用户地址列表
    @POST("/api/user/addressList")
    @FormUrlEncoded
    Observable<HttpResponse<AddressListBean>> addressList(@Field("os") String os);

    //删除用户地址
    @POST("/api/user/delAddress")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> delAddress(@Field("addressId") int addressId);

    //设置用户默认地址
    @POST("/api/user/setAddress")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> setAddress(@Field("addressId") int addressId);

    //新增/修改用户地址
    @POST("/api/user/saveAddress")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> saveAddress(@Field("addressId") int addressId, @Field("userName") String userName,
                                                 @Field("genderName") String genderName, @Field("mobile") String mobile,
                                                 @Field("address") String address, @Field("addressHouse") String addressHouse,
                                                 @Field("addressCityCode") String addressCityCode, @Field("addressCity") String addressCity,
                                                 @Field("addressLatitude") double addressLatitude, @Field("addressLongitude") double addressLongitude);


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
    Observable<HttpResponse<TrafficEntity>> orderTrafficSet(@FieldMap HashMap<String, Object> map);

    //用户下单
    @POST("/api/user/orderProduct")
    Observable<HttpResponse<OrderInfoEntity>> orderProduct(@Body PlaceOrderReq req);

    //订单列表
    @POST("/api/user/orderList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<OrderInfoEntity>>> orderList(@Field("status") int status);

    //订单详情
    @POST("/api/user/orderInfo")
    @FormUrlEncoded
    Observable<HttpResponse<OrderInfoEntity>> orderInfo(@Field("orderNo") String orderNo);

    //取消订单
    @POST("/api/user/cancelOrder")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> cancelOrder(@Field("orderNo") String orderNo);

    //删除订单
    @POST("/api/user/delOrder")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> delOrder(@Field("orderNo") String orderNo);

    //评价列表
    @POST("/api/user/ratingsList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<OrderInfoEntity>>> ratingsList(@Field("os") String os);

    //用户评价订单
    @POST("/api/user/ratingOrder")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> ratingOrder(@Field("orderNo") String orderNo,
                                                 @Field("ratings") int ratings,
                                                 @Field("comment") String comment,
                                                 @Field("anonymous") int anonymous);

    //获取支付方式
    @POST("/api/user/paySiteList")
    @FormUrlEncoded
    Observable<HttpResponse<ListBean<PayTypeEntity>>> paySiteList(@Field("os") String os);

    /**
     * 获取支付参数
     *
     * @param paySite 1=微信 2=支付宝 3=银联
     */
    @POST("/api/user/orderProductPayInfo")
    @FormUrlEncoded
    Observable<HttpResponse<PayEntity>> orderProductPayInfo(@Field("orderNo") String orderNo, @Field("paySite") int paySite);

    /**
     * 获取结果查询
     *
     */
    @POST("/api/user/orderProductPayResult")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> orderProductPayResult(@Field("orderNo") String orderNo);

}
