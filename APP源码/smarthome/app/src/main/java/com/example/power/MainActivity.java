package com.example.power;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.progresviews.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.power)
    ProgressWheel power;
    @BindView(R.id.open_air)
    Button powerUp;
    @BindView(R.id.close_air)
    Button powerClose;
    @BindView(R.id.temp_up)
    Button tempUp;
    @BindView(R.id.temp_down)
    Button tempDown;
    @BindView(R.id.water)
    ProgressWheel water;
    @BindView(R.id.make_hot)
    Button makeHot;
    @BindView(R.id.make_cold)
    Button makeCold;
    @BindView(R.id.light)
    Button light;
    @BindView(R.id.dark)
    Button dark;
    @BindView(R.id.open_curtain)
    Button open;
    @BindView(R.id.close_curtain)
    Button close;

    ApiService request;
    TextView temp_show;
    TextView waterPattern;
    TextView curtain_show;
    TextView light_show;
    TextView lock_show;
    Vibrator vibrator;
    Button open_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        temp_show = findViewById(R.id.temp_show);
        waterPattern = findViewById(R.id.water_pattern);
        curtain_show = findViewById(R.id.curtain_show);
        light_show = findViewById(R.id.light_show);
        lock_show = findViewById(R.id.lock_show);
        open_lock = findViewById(R.id.open_lock);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.79.4.151:7008")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        request = retrofit.create(ApiService.class);

        open_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("http://zmblog.wang/welock/open/")
                        .build();
                API request2 = retrofit2.create(API.class);
                vibrator.vibrate(30);
                Log.d("ok", "开锁！");
                request2.open_lock().enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            lock_show.setText("门锁状态：开锁");
                            Log.d("ok", "开锁成功！");
                        }
                    }
                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
            }
        });
    }

    @OnClick({R.id.open_air, R.id.close_air, R.id.temp_up, R.id.temp_down, R.id.make_hot,
            R.id.make_cold, R.id.light, R.id.dark, R.id.open_curtain, R.id.close_curtain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.open_air:
                vibrator.vibrate(30);
                Log.d("ok", "开空调！");
                request.set_status("open_air", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "开空调成功！"+response.body());
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.close_air:
                vibrator.vibrate(30);
                Log.d("ok", "关空调！");
                request.set_status("close_air", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "关空调成功！");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.temp_up:
                vibrator.vibrate(30);
                Log.d("ok", "升温前查询家居当前数据！");
                request.get_status("getdata", "12345678").enqueue(new Callback<StatusBean>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<StatusBean> call, @NonNull Response<StatusBean> response) {
                        if (response.body() != null) {
                          //  Log.d("ok", "查询成功！"+response.body().toString());
                            temp_show.setText("当前温度：" + (Integer.parseInt(response.body().getAir_temple()) + 1) + "℃");
                            request.set_status("temp_" + (Integer.parseInt(response.body().getAir_temple()) + 1), "12345678").enqueue(new Callback<ResponseBody>() {
                                //请求成功时回调
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.body() != null) {
                                        Log.d("ok", "升温成功！");
                                    }
                                }
                                //请求失败时回调
                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                                    Log.d("ok", "请求失败");
                                }
                            });
                        }
                    }


                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<StatusBean> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.temp_down:
                vibrator.vibrate(30);
                Log.d("ok", "降温前查询家居当前数据！");
                request.get_status("getdata", "12345678").enqueue(new Callback<StatusBean>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<StatusBean> call, @NonNull Response<StatusBean> response) {
                        if (response.body() != null) {
                            Log.d("ok", "查询成功！");
                            temp_show.setText("当前温度：" + (Integer.parseInt(response.body().getAir_temple()) - 1) + "℃");
                            request.set_status("temp_" + (Integer.parseInt(response.body().getAir_temple()) - 1), "12345678").enqueue(new Callback<ResponseBody>() {
                                //请求成功时回调
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.body() != null) {
                                        Log.d("ok", "降温成功！");
                                    }
                                }

                                //请求失败时回调
                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                                    Log.d("ok", "请求失败");
                                }
                            });
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<StatusBean> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.make_hot:
                vibrator.vibrate(30);
                Log.d("ok", "制热！");
                request.set_status("open_drinkingfountain", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "制热模式启动！");
                            waterPattern.setText("当前模式：制热");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.make_cold:
                vibrator.vibrate(30);
                Log.d("ok", "常温！");
                request.set_status("close_drinkingfountain", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "常温模式启动！");
                            waterPattern.setText("当前模式：常温");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.light:
                vibrator.vibrate(30);
                Log.d("ok", "开灯！");
                request.set_status("close_right", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "开灯成功！");
                            light_show.setText("电灯状态：灯灭");
                        }
                    }
                    //请求失败时回调

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.dark:
                vibrator.vibrate(30);
                Log.d("ok", "关灯！");
                request.set_status("open_right", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "关灯成功！");
                            light_show.setText("电灯状态：灯亮");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.open_curtain:
                vibrator.vibrate(30);
                Log.d("ok", "开窗帘！");
                request.set_status("close_curtain", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "开窗帘成功！");
                            curtain_show.setText("窗帘状态：窗关");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
            case R.id.close_curtain:
                vibrator.vibrate(30);
                Log.d("ok", "关窗帘！");
                request.set_status("open_curtain", "12345678").enqueue(new Callback<ResponseBody>() {
                    //请求成功时回调
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("ok", "关窗帘成功！");
                            curtain_show.setText("窗帘状态：窗开");
                        }
                    }

                    //请求失败时回调
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        Log.d("ok", "请求失败");
                    }
                });
                break;
        }
    }
}
