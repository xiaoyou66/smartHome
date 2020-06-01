package com.example.power;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    /**
     *GET请求服务器开锁
     * @return
     */
    @GET("lock")
    Call<ResponseBody> open_lock();
}
