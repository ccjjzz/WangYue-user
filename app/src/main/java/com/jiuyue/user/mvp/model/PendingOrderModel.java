package com.jiuyue.user.mvp.model;

import com.jiuyue.user.mvp.contract.PendingOrderContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;

public class PendingOrderModel implements PendingOrderContract.Model {
    ApiServer apiServer =  ApiRetrofit.getInstance().getApiService();
}
