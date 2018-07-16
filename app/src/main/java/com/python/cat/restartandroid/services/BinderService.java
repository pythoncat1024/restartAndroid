package com.python.cat.restartandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.restartandroid.services.interfaces.ServiceCallBack;
import com.python.cat.restartandroid.services.interfaces.ServiceInterface;

import java.net.InterfaceAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class BinderService extends Service implements ServiceInterface {


    private ServiceCallBack mCallBack;

    private BinderService get() {
        return this;
    }

    public BinderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new InnerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void add(int a, int b) {
        Executors.newCachedThreadPool()
                .execute(() -> {
                    int c = a + b;
                    LogUtils.w("c==" + (a + b) + " ## " + c);
                    if (mCallBack != null) {
                        mCallBack.onResult(c);
                    } else {
                        throw new RuntimeException("you should call service#setCallback before this!");
                    }
                });

    }

    public ServiceCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(ServiceCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class InnerBinder extends Binder {

        public BinderService getService(ServiceCallBack callBack) {
            mCallBack = callBack;
            return get();
        }
    }
}
