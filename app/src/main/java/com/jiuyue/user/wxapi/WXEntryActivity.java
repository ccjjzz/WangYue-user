package com.jiuyue.user.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.pay.PayHelper;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String openid;
    private boolean isOK;
    private String access;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayHelper.wxAppId, false);
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
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
        }
        //微信登录逻辑
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp= (SendAuth.Resp) resp;
                getAccessToken(sendResp.code);
                isOK=true;
//              WXEntryActivity.this.finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(WXEntryActivity.this,"用户拒绝授权",Toast.LENGTH_SHORT).show();
                isOK=false;
                WXEntryActivity.this.finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "用户取消授权", Toast.LENGTH_SHORT).show();
                WXEntryActivity.this.finish();
                break;
            default:
                isOK=false;
                WXEntryActivity.this.finish();
                break;
        }

    }

    private void getAccessToken(String code) {
        //获取授权
        String path="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+PayHelper.wxAppId+"&secret="+PayHelper.wxSecret+"&code="+code+"&grant_type=authorization_code";
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(path)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    openid=jsonObject.getString("openid");
                    access=jsonObject.getString("access_token");
                    Log.d("onResponse", openid);
                    LiveEventBus.get(EventKey.WX_AUTH_RESULT,String.class).post(openid);
                    WXEntryActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//          //获取个人信息
//          String getUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openid;
//          OkHttpClient client1=new OkHttpClient();
//          final Request request1=new Request.Builder()
//                .url(getUserInfo)
//                .build();
//          Call info=client1.newCall(request1);
//          info.enqueue(new Callback() {
//             @Override
//             public void onFailure(Call call, IOException e) {
//                Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_SHORT).show();
//                finish();
//             }
//
//             @Override
//             public void onResponse(Call call, Response response) throws IOException {
//                String nickName=null;
//                String sex=null;
//                String province=null;
//                String country=null;
//                String city=null;
//                String avatarUrl=null;
//                final String result=response.body().string();
//                try {
//                   //解析出微信的常用信息，如性别，昵称，省市区等
//                   JSONObject jsonObject=new JSONObject(result);
//                   nickName=jsonObject.getString("nickname");
//                   sex=jsonObject.getString("sex");
//                   city=jsonObject.getString("city");
//                   province=jsonObject.getString("province");
//                   country=jsonObject.getString("country");
//                   avatarUrl=jsonObject.getString("headimgurl");
//                } catch (Exception e) {
//                   e.printStackTrace();
//                }
//             }
//          });
            }
        });
    }
}