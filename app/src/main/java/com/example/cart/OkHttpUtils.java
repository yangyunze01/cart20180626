package com.example.cart;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 杨运泽 on 2018/6/26.
 */

public class OkHttpUtils {
    public static OkHttpUtils oKhttpUtils;
    private final OkHttpClient okHttpClient;
    private final Handler myhandler;


    private OkHttpUtils() {
        //日志拦截器
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //主线程Handler
        myhandler = new Handler(Looper.getMainLooper());
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                //  .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    public static OkHttpUtils getoKhttpUtils() {
        if (oKhttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (oKhttpUtils == null) {
                    return oKhttpUtils = new OkHttpUtils();
                }
            }
        }
        return oKhttpUtils;
    }



    //异步get
    public void doGet(String url, final IOKhttpUtilsCallback ioKhttpUtilsCallback) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (ioKhttpUtilsCallback != null) {
                    //切换到主线程
                    myhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ioKhttpUtilsCallback.onFailure(e.getMessage());
                        }
                    });
                }
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String json = response.body().string();
                    if (ioKhttpUtilsCallback != null) {
                        //切换到主线程
                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ioKhttpUtilsCallback.onResponse(json);
                            }
                        });
                    }


                } else {
                    if (ioKhttpUtilsCallback != null) {
                        //切换到主线程
                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ioKhttpUtilsCallback.onFailure("网络异常");
                            }
                        });
                    }
                }
            }
        });


    }


    //异步post
    public void doPost(String url, Map<String, String> map, final IOKhttpUtilsCallback ioKhttpUtilsCallback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (ioKhttpUtilsCallback != null) {
                    //切换到主线程
                    myhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ioKhttpUtilsCallback.onFailure(e.getMessage());
                        }
                    });
                }
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String json = response.body().string();
                    if (ioKhttpUtilsCallback != null) {
                        //切换到主线程
                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ioKhttpUtilsCallback.onResponse(json);
                            }
                        });
                    }


                } else {
                    if (ioKhttpUtilsCallback != null) {
                        //切换到主线程
                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ioKhttpUtilsCallback.onFailure("网络异常");
                            }
                        });
                    }
                }
            }
        });
    }


    //接口回调
    public interface IOKhttpUtilsCallback {
        void onFailure(String error);


        void onResponse(String json);
    }
}
