package com.example.power;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    /**
     *向服务器端发送控制指令
     * @param data
     * @param secret
     * @return
     */
    @FormUrlEncoded
    @POST("test.php")
    Call<ResponseBody> set_status(@Field("data") String data, @Field("secret") String secret);

    /**
     *向服务器获取数据
     * @param data
     * @param secret
     * @return
     */
    @FormUrlEncoded
    @POST("test.php")
    Call<StatusBean> get_status(@Field("data") String data,@Field("secret") String secret);

}
