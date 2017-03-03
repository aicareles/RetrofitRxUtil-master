package com.liulei.retrofitrxdemoo.util;

import com.liulei.retrofitrxdemoo.ApiService;
import com.liulei.retrofitrxdemoo.consts.Consts;
import com.liulei.retrofitrxdemoo.model.BaseResponse;
import com.liulei.retrofitrxdemoo.model.tngou.Cook;
import com.liulei.retrofitrxdemoo.model.tngou.DeviceVO;
import com.liulei.retrofitrxdemoo.model.tngou.TngouResponse;
import com.liulei.retrofitrxdemoo.model.tngou.UserVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitUtil {

    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static RetrofitUtil mInstance;
    private static OkHttpClient mOkHttpClient;

    /**
     * 私有构造方法
     */
    private RetrofitUtil(){
        if (null == mOkHttpClient) {
            mOkHttpClient = OkHttp3Utils.getOkHttpClient();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
//                .client(builder.build())
                //设置使用okhttp网络请求
                .client(mOkHttpClient)
                .baseUrl(Consts.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance(){
        if (mInstance == null){
            synchronized (RetrofitUtil.class){
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

//    /**
//     * 用于获取用户信息
//     * @param subscriber
//     */
//    public void getUsers(Subscriber<BaseResponse<List<UserModel>>> subscriber){
//        mApiService.getUsersByRx()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//
//    public void getUsersByMore(Subscriber<BaseResponse<? extends Object>> subscriber){
//        mApiService.getUsersByRx()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

    /**
     *
     * @param page  请求页数，默认page=1
     * @param rows  每页返回的条数，默认rows = 20
     * @param subscriber
     */
    public void getCookList(int page, int rows, Subscriber<TngouResponse<List<Cook>>> subscriber){
//        mApiService.getCookList(page,rows)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

        toSubscribe(mApiService.getCookList(page,rows),subscriber);
    }

    private <T> void toSubscribe(Observable<T> observable,Subscriber<T> subscriber){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //验证手机号码   这里是我自己的接口
    public void verfcationNum(Map<String,String> map,Subscriber<BaseResponse> subscriber){
//        mApiService.getVerfcationCodePostMap(map)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
        toSubscribe(mApiService.getVerfcationCodePostMap(map),subscriber);
    }

    //获取用户个人信息   这里是我自己的接口
    public void getUserBean(Map<String,String> map,Subscriber<BaseResponse<UserVO>> subscriber){
//        mApiService.getUser(map)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
        toSubscribe(mApiService.getUser(map),subscriber);
    }

    //获取我的设备列表  这里是我自己的接口
    public void MyDevices(Map<String,String> map, Subscriber<BaseResponse<List<DeviceVO>>> subscriber){
//        mApiService.getDevices(map)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
        toSubscribe(mApiService.getDevices(map),subscriber);
    }

    //上传头像  这里是我自己的接口
    public void uploadImg(MultipartBody.Part part,Map<String,RequestBody>map,Subscriber<BaseResponse>subscriber){

        mApiService.uploadImage(map,part)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
