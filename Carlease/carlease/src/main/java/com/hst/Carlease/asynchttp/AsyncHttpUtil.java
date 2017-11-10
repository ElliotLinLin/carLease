package com.hst.Carlease.asynchttp;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tools.bean.BeanTool;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

import org.apache.http.HttpEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Android-async-http框架的封装工具类 可根据返回的数据类型调用不同的请求方法
 *
 * @author HL
 */

public class AsyncHttpUtil {

    private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

    static {

        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.setTimeout(500000); // 设置链接超时，如果不设置，默认为10s
    }

    /**
     * get方法返回一个字符串
     *
     * @param urlString
     * @param res
     */
    public static void get(String url, Object bean, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        /**
         * 这里是与后台规定的调用接口的方式
         */
        String urlParame = BeanTool.toURLEncoder(bean, null);
        String loginurl;
        if (bean != null) {
            loginurl = url + "&" + urlParame;
        } else {
            loginurl = url;
        }
        Log.e("AsyncHttpUtil get", loginurl);
        client.get(loginurl, res);
    }

    /**
     * 不带参的get方法（专门获取token调用）
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void get(String urlString, AsyncHttpResponseHandler res) // url里面带参数
    {
        client.get(urlString, res);
    }

    /**
     * 带参数的get方法
     *
     * @param urlString
     * @param params
     * @param res
     */
    // public static void get(String urlString, RequestParams params,
    // AsyncHttpResponseHandler res) // url里面带参数
    // {
    // client.get(urlString, params, res);
    // }

    /**
     * get不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param res
     */
    public static void get(String urlString, JsonHttpResponseHandler res) {
        client.get(urlString, res);
    }

    /**
     * get带参数，获取json对象或者数组
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    /**
     * get方法获取二进制数据（byte）
     *
     * @param uString
     * @param bHandler
     */
    public static void get(String uString, BinaryHttpResponseHandler bHandler) {
        client.get(uString, bHandler);
    }

    /**
     * post方法获取字符串返回
     *
     * @param urlString
     * @param res
     */
    public static void post(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        client.post(urlString, res);
    }

    /**
     * post方法传递entity数据返回字符串
     *
     * @param context
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     * @throws UnsupportedEncodingException
     */
    public static void post(Context context, String posturl, Object bean, String contentType, AsyncHttpResponseHandler responseHandler)
            throws UnsupportedEncodingException {
        String paramString = GJson.toJsonString(bean);
        HttpEntity entity = new StringEntity(paramString, "utf-8");
        client.post(context, posturl, entity, contentType, responseHandler);
    }


    /**
     * post返回json对象或数组
     *
     * @param context
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public static void post(Context context, String url, HttpEntity entity, String contentType, JsonHttpResponseHandler responseHandler) {
//		client.addHeader("token", SPUtils.get(context, Constants.tokenID, "").toString());
        client.post(context, url, entity, contentType, responseHandler);
    }

    /**
     * post已key,value形式传参，返回一个字符串数据
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(String urlString, Context context, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
    {
        NetworkState state = new NetworkState(context);
        if (state.isConnected() == false) {
            Prompt.showWarning(context, "请检查您的网络");
            return;
        }
        client.post(urlString, params, res);
    }

    /**
     * 不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param res
     */
    public static void post(String urlString, JsonHttpResponseHandler res) {
        client.post(urlString, res);
    }

    /**
     * 带参数，获取json对象或者数组
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void post(String urlString, RequestParams params, JsonHttpResponseHandler res) {
        client.post(urlString, params, res);
    }

    /**
     * 下载数据使用，会返回byte数据
     *
     * @param uString
     * @param bHandler
     */
    public static void post(String uString, BinaryHttpResponseHandler bHandler) {
        client.post(uString, bHandler);
    }

    /**
     * 获取网络请求对象
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        return client;
    }

}
