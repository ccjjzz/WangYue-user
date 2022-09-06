package com.jiuyue.user.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.global.Constant;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.pay.PayHelper;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private final String TAG = this.getClass().getSimpleName();

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayHelper.wxAppId);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e(TAG,"onPayReq, openId = " + req.openId + "  transaction-" + req.transaction + "  resp-" + req.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG,"onPayFinish, errCode = " + resp.errCode + "  errStr-" + resp.errStr + "  resp-" + resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //通知回调结果
            LiveEventBus.get(EventKey.WX_PAY_RESULT,Integer.class).post(resp.errCode);
            finish();
        }
    }
}
