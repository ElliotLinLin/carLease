package com.hst.Carlease.ui.thirdperiodui;

import android.view.View;

import com.google.gson.Gson;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI2;
import com.tools.json.GJson;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/4
 */

public abstract class ThirdBaseUi extends AbsUI2 {

    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, param, clazz, "", true, null, baseCallBack);
    }

    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, param, clazz, "", true, view, baseCallBack);
    }

    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, boolean isdialogShow, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, param, clazz, "", isdialogShow, view, baseCallBack);
    }

    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, String showText, boolean isdialogShow, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, param, clazz, showText, isdialogShow, null, baseCallBack);
    }

    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, String showText, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, param, clazz, showText, true, view, baseCallBack);
    }


    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, map, clazz, "", true, null, baseCallBack);
    }

    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, map, clazz, "", true, view, baseCallBack);
    }

    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, boolean isdialogShow, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, map, clazz, "", isdialogShow, view, baseCallBack);
    }

    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, String showText, boolean isdialogShow, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, map, clazz, showText, isdialogShow, null, baseCallBack);
    }

    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, String showText, View view, final BaseCallBack<T> baseCallBack) {
        sendRequest(url, map, clazz, showText, true, view, baseCallBack);
    }

    public <T> void sendRequest(String url, HashMap map, final Class<T> clazz, String showText, boolean isdialogShow, View view, final BaseCallBack<T> baseCallBack) {

        AsyncCallBackHandler res = new AsyncCallBackHandler(ui, showText, isdialogShow, view) {
            @Override
            public void mySuccess(int arg0, Header[] arg1, String arg2) {

                String s = arg2;
                if (arg2.startsWith("<?xml") || arg2.startsWith("<string")) {
                    s = parseXMLwithPull(arg2);
                }

                //                Bean bean = GJson.parseObject(s, Bean.class);
                T t = null;
                try {
                    t = GJson.parseObject(s, clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                    baseCallBack.onFailure("json解析异常", new IllegalStateException("json解析异常"));
                }
                baseCallBack.onSuccess(t);
            }

            @Override
            public void myFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                baseCallBack.onFailure(arg2, arg3);
            }

            @Override
            public void onStart() {
                super.onStart();
                baseCallBack.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                baseCallBack.onFinish();
            }
        };
        try {
            AsyncHttpUtil.post(context, url, map, "application/json", res);
        } catch (UnsupportedEncodingException e) {
            baseCallBack.onFailure(e.getMessage(), e);
            e.printStackTrace();
        }

    }


    public <T> void sendRequest(String url, RequestParams param, final Class<T> clazz, String showText, boolean isdialogShow, View view, final BaseCallBack<T> baseCallBack) {
        if (null == param) param = new RequestParams();
        AsyncCallBackHandler res = new AsyncCallBackHandler(ui, showText, isdialogShow, view) {
            @Override
            public void mySuccess(int arg0, Header[] arg1, String arg2) {
                String s = arg2;
                if (arg2.startsWith("<?xml") || arg2.startsWith("<string")) {
                     s = parseXMLwithPull(arg2);
                }

                //                Bean bean = GJson.parseObject(s, Bean.class);
                T t = null;
                try {
//                    t =  new Gson().p(s, clazz);
                    t = new Gson().fromJson(s, clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                    baseCallBack.onFailure("json解析异常", new IllegalStateException("json解析异常"));
                }
                baseCallBack.onSuccess(t);
            }

            @Override
            public void myFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                baseCallBack.onFailure(arg2, arg3);
            }

            @Override
            public void onStart() {
                super.onStart();
                baseCallBack.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                baseCallBack.onFinish();
            }
        };
        AsyncHttpUtil.post(url, context, param, res);
    }


    private String parseXMLwithPull(String xml) {
        String content = null;
        try {
            //XmlPullParserFactory首先是获取实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //利用实例调用setinput将数据写进去
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xml));
            //通过调用此方法得到当前解析事件
            int eventType = xmlPullParser.getEventType();
            while (eventType != xmlPullParser.END_DOCUMENT) {
                String nodename = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        //开始解析某个节点
                        if ("string".equals(nodename)) {
                            try {
                                content = xmlPullParser.nextText();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG: {
                        if ("string".equals(nodename)) {
                            return content;
                        }
                        break;
                    }
                    default:
                        break;
                }
                try {
                    eventType = xmlPullParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return content;
    }

}
