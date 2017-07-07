package com.liulei.retrofitrxdemoo;

import com.liulei.retrofitrxdemoo.model.BaseResponse;
import com.liulei.retrofitrxdemoo.model.UserModel;
import com.liulei.retrofitrxdemoo.model.tngou.Cook;
import com.liulei.retrofitrxdemoo.model.tngou.DeviceVO;
import com.liulei.retrofitrxdemoo.model.tngou.TngouResponse;
import com.liulei.retrofitrxdemoo.model.tngou.UserVO;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    /**
     * 使用普通的retrofit方式获取数据
     *
     * @return
     */
    @GET("ezSQL/get_user.php")
    Call<BaseResponse<List<UserModel>>> getUsers();


    /**
     * 使用rx+retrofit的方式获取数据
     */
    @GET("ezSQL/get_user.php")
    Observable<BaseResponse<List<UserModel>>> getUsersByRx();


    @GET("api/cook/list")
    Observable<TngouResponse<List<Cook>>> getCookList(@Query("page") int page, @Query("rows") int rows);

    //POST请求
    @FormUrlEncoded
    @POST("api/user/checkMobile")
    Observable<BaseResponse> getVerfcationCodePostMap(@FieldMap Map<String, String> map);

    //POST请求
    @FormUrlEncoded
    @POST("api/user/loginByMobile")
    Observable<BaseResponse<UserVO>> getUser(@FieldMap Map<String, String> map);


    //POST请求
    @FormUrlEncoded
    @POST("api/user/devices")
    Observable<BaseResponse<List<DeviceVO>>> getDevices(@FieldMap Map<String, String> map);

    //    /*上传文件*/
//    @Multipart
//    @POST("api/user/modifyDevice")
//    Observable<BaseResponse> uploadImage(@Part("token") RequestBody token, @Part("client_id") RequestBody  client_id,
//                                         @Part("device_name") RequestBody device_name, @Part("device_header") RequestBody  device_header,
//                                                            @Part MultipartBody.Part file);
    /*上传文件*/
    @Multipart
    @POST("api/user/modifyDevice")
    Observable<BaseResponse> uploadImage(@PartMap Map<String,RequestBody>map, @Part MultipartBody.Part file);
}
