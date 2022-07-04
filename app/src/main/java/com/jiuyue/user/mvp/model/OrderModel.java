package com.jiuyue.user.mvp.model;

import com.jiuyue.user.mvp.contract.OrderContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;

public class OrderModel implements OrderContract.Model {
    ApiServer apiServer =  ApiRetrofit.getInstance().getApiService();
}
