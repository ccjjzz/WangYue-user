package com.jiuyue.user.net;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jiuyue.user.App;
import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.ui.login.LoginActivity;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.NetWorkUtil;
import com.jiuyue.user.utils.ToastUtil;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<HttpResponse<T>> {
    /**
     * 于服务器约定  返回code为几是正常请求
     */
    public static final int CODE = ApiRetrofit.SUCCESS_CODE;
    protected BaseView view;
    /**
     * 网络连接失败  无网
     */
    public static final int NETWORK_ERROR = 100000;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 415;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 502;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 403;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 408;

    //错误的返回值
    private int errorCode = 404;

    public BaseObserver() {
    }

    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    public void onComplete() {
        complete();
    }

    @Override
    public void onNext(HttpResponse<T> tHttpResponse) {
        try {
            if (view != null) {
                view.hideLoading();
            }
            if (tHttpResponse.getCode() == CODE) {
                onSuccess(tHttpResponse);
            }else if (tHttpResponse.getCode() == -1){ //token失效
                ToastUtil.show("登录已失效，请重新登录");
                //重置缓存未登录状态
                App.getSharePre().putBoolean(SpKey.IS_LOGIN, false);
                //回到登录页面，并且结束掉其他的页面
                IntentUtils.startActivity(App.getAppContext(), LoginActivity.class);
                AppStockManage.getInstance().finishAllActivity(LoginActivity.class);
            }else {
                onError(tHttpResponse.getMsg(), tHttpResponse.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (view != null) {
                view.hideLoading();
            }
            if (e instanceof HttpException) {
                //Http错误
                onException(BAD_NETWORK, "", getErrorCode(e));
                e.printStackTrace();
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                //连接错误
                onException(CONNECT_ERROR, "", CONNECT_ERROR);
                e.printStackTrace();
            } else if (e instanceof InterruptedIOException) {
                //连接超时
                onException(CONNECT_TIMEOUT, "", CONNECT_TIMEOUT);
                e.printStackTrace();
            } else if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                //  解析错误
                onException(PARSE_ERROR, "", PARSE_ERROR);
                e.printStackTrace();
            } else {
                if (e != null) {
                    onError(e.toString(), errorCode);
                } else {
                    onError("未知错误", errorCode);
                }
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void onException(int badNetwork, String s, int errorCode) {
        if (!NetWorkUtil.isAvailableByPing()) {
            badNetwork = NETWORK_ERROR;
            s = "网络不可用，请检查网络连接！";
            errorCode = 100000;
        }
        onExceptions(badNetwork, s, errorCode);
    }

    public int getErrorCode(Throwable e) {
        ResponseBody body = ((HttpException) e).response().errorBody();
        try {
            String json = body.string();
            HttpResponse baseModel = new Gson().fromJson(json, HttpResponse.class);
            errorCode = baseModel.getCode();
        } catch (IOException IOe) {
            IOe.printStackTrace();
        }
        return errorCode;
    }


    private void onExceptions(int unknownError, String message, int code) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误", code);
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时", code);
                break;
            case BAD_NETWORK:
                onError("网络超时", code);
                break;
            case PARSE_ERROR:
                onError("数据解析失败", code);
                break;
            //网络不可用
            case NETWORK_ERROR:
                onError("网络不可用，请检查网络连接！", code);
                break;
            default:
                break;
        }
    }

    public abstract void onSuccess(HttpResponse<T> data);

    public abstract void onError(String msg, int code);

    public abstract void complete();
}