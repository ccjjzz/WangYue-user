package com.jiuyue.user.net;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.global.Constant;
import com.jiuyue.user.utils.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

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
    public static final int CODE = Constant.SUCCESS_CODE;
    protected BaseView view;
    /**
     * 网络连接失败  无网
     */
    public static final int NETWORK_ERROR = 100000;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;

    //错误的返回值
    private int code;

    public BaseObserver() {
    }

    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }
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
            } else {
                if (view != null) {
                    view.showError(tHttpResponse);
                }
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
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                //连接错误
                onException(CONNECT_ERROR, "", getErrorCode(e));
            } else if (e instanceof InterruptedIOException) {
                //连接超时
                onException(CONNECT_TIMEOUT, "", getErrorCode(e));
            } else if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                //  解析错误
                onException(PARSE_ERROR, "", getErrorCode(e));
                e.printStackTrace();
            } else {
                if (e != null) {
                    onError(e.toString(), getErrorCode(e));
                } else {
                    onError("未知错误", getErrorCode(e));
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
            code = baseModel.getCode();
        } catch (IOException IOe) {
            IOe.printStackTrace();
        }
        return code;
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