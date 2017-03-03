package com.liulei.retrofitrxdemoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulei.retrofitrxdemoo.consts.Consts;
import com.liulei.retrofitrxdemoo.model.BaseResponse;
import com.liulei.retrofitrxdemoo.model.UserModel;
import com.liulei.retrofitrxdemoo.model.tngou.Cook;
import com.liulei.retrofitrxdemoo.model.tngou.DeviceVO;
import com.liulei.retrofitrxdemoo.model.tngou.TngouResponse;
import com.liulei.retrofitrxdemoo.model.tngou.UserVO;
import com.liulei.retrofitrxdemoo.upload.ProgressRequestBody;
import com.liulei.retrofitrxdemoo.upload.UploadProgressListener;
import com.liulei.retrofitrxdemoo.util.RetrofitUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private static final String TAG = "MainActivity";
    private String token;
    private NumberProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        progressBar=(NumberProgressBar)findViewById(R.id.number_progress_bar);
        mListener = new SubscriberOnNextListener<TngouResponse<List<Cook>>>() {
            @Override
            public void onNext(TngouResponse<List<Cook>> listTngouResponse) {
                mTextView.setText(listTngouResponse.tngou.toString());
                showToast(listTngouResponse.tngou.toString());
            }
        };
    }

    /**
     * 普通的retrofit获取数据方法
     *
     * @param view
     */
    public void btnSimpleClick(View view) {
        HashMap<String,String>map = new HashMap<>();
        map.put("mobile","18682176281");
        RetrofitUtil.getInstance().verfcationNum(map,new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"验证手机号完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"验证手机号出错",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                Toast.makeText(MainActivity.this,"验证手机号成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnBeanClick(View view) {
        HashMap<String,String>map = new HashMap<>();
        map.put("mobile","18682176281");
        map.put("password","e10adc3949ba59abbe56e057f20f883e");
        RetrofitUtil.getInstance().getUserBean(map,new Subscriber<BaseResponse<UserVO>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"登录完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"登录出错",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseResponse<UserVO> userVOBaseResponse) {
                String user = userVOBaseResponse.data.toString();
                token = userVOBaseResponse.data.getUser_token();
                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,user,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnListClick(View view) {
        HashMap<String,String> map = new HashMap<>();
        if(token!=null){
            map.put("token",token);
        }
        RetrofitUtil.getInstance().MyDevices(map,new Subscriber<BaseResponse<List<DeviceVO>>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"获取设备列表完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"获取设备列表出错",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseResponse<List<DeviceVO>> deviceVOBaseResponse) {
                String devices = deviceVOBaseResponse.data.toString();

                Toast.makeText(MainActivity.this,devices,Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void btnUploadClick(View view) {
        if(token == null){
            Toast.makeText(MainActivity.this,"token为空，请先点击获取bean的Retrofit的按钮进行登录并获取token",Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,RequestBody> map = new HashMap<>();
        map.put("token",RequestBody.create(MediaType.parse("text/plain"),token));
        map.put("client_id",RequestBody.create(MediaType.parse("text/plain"),"0c313613013040acae32f088b069932a"));
        map.put("device_name",RequestBody.create(MediaType.parse("text/plain"),"傻了"));
        map.put("device_header",RequestBody.create(MediaType.parse("text/plain"),"device_header"));
        File file=new File("/storage/emulated/0/Download/11.jpg");
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/jpeg"),file);
        MultipartBody.Part part= MultipartBody.Part.createFormData("device_header", file.getName(), new ProgressRequestBody(requestBody,
                new UploadProgressListener() {
                    @Override
                    public void onProgress(long currentBytesCount, long totalBytesCount) {
//                        tvMsg.setText("提示:上传中");
                        progressBar.setMax((int) totalBytesCount);
                        progressBar.setProgress((int) currentBytesCount);
                    }
                }));
        RetrofitUtil.getInstance().uploadImg(part,map, new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"上传头像完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"上传头像出错",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                Toast.makeText(MainActivity.this,"上传头像成功",Toast.LENGTH_SHORT).show();
            }
        });
    }



    private Toast mToast;

    public void showToast(String desc) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getApplicationContext(), desc, Toast.LENGTH_LONG);
        } else {
            mToast.setText(desc);
        }
        mToast.show();
    }

    /**
     * 使用rx+retrofit获取数据
     * <p>
     * 【subscribeOn和observeOn区别】
     * 1、subscribeOn用于切换之前的线程
     * 2、observeOn用于切换之后的线程
     * 3、observeOn之后，不可再调用subscribeOn切换线程
     * <p>
     * ————————  下面是来自扔物线的额外总结 ————————————
     * 1、下面提到的“操作”包括产生事件、用操作符操作事件以及最终的通过 subscriber 消费事件
     * 2、只有第一subscribeOn() 起作用（所以多个 subscribeOn() 毛意义）
     * 3、这个 subscribeOn() 控制从流程开始的第一个操作，直到遇到第一个 observeOn()
     * 4、observeOn() 可以使用多次，每个 observeOn() 将导致一次线程切换()，这次切换开始于这次 observeOn() 的下一个操作
     * 5、不论是 subscribeOn() 还是 observeOn()，每次线程切换如果不受到下一个 observeOn() 的干预，线程将不再改变，不会自动切换到其他线程
     *
     * @param view
     */
    public void btnClick1(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<BaseResponse<List<UserModel>>> observable = apiService.getUsersByRx();
        observable
                .subscribeOn(Schedulers.io())  // 网络请求切换在io线程中调用
                .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                .observeOn(AndroidSchedulers.mainThread())// 观察后放在主线程调用
                .subscribe(new Subscriber<BaseResponse<List<UserModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("rx失败:" + e.getMessage());
                        Log.e(TAG, "rx失败:" + e.getMessage());
                        mTextView.setText("rx失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<List<UserModel>> listBaseResponse) {
                        showToast("rx成功:" + listBaseResponse.data.toString());
                        mTextView.setText("rx成功:" + listBaseResponse.data.toString());
                        Log.e(TAG, "rx成功:" + listBaseResponse.data.toString());
                    }
                });
    }


    private SubscriberOnNextListener mListener;

    public void btnProgressClick(View view) {

        RetrofitUtil.getInstance().getCookList(2,5,new ProgressSubscriber<TngouResponse<List<Cook>>>(mListener,this));
    }
}
