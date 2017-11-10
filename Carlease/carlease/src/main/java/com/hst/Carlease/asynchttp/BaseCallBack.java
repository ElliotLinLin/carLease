package com.hst.Carlease.asynchttp;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/4
 */

public abstract class BaseCallBack<T>{
    public void onStart() {

    }
    public void onFinish() {

    }
    public abstract void onSuccess(T t);

    public void onFailure(String arg2, Throwable arg3) {

    }

}
