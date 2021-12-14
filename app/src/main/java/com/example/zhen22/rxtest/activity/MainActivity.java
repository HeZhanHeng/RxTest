package com.example.zhen22.rxtest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhen22.rxtest.ApiStores;
import com.example.zhen22.rxtest.R;
import com.example.zhen22.rxtest.module.TestBean;

import junit.framework.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
private static final String TAG="RxJava";
private TextView tv;
private Unbinder unbinder;
@BindView(R.id.btn)
Button btn;
@BindView(R.id.btn2)
Button btn2;
@BindView(R.id.btn3)
Button btn3;
@BindView(R.id.btn4)
Button btn4;
@BindView(R.id.btn5)
Button btn5;
private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder=ButterKnife.bind(this);
        initView();
        initFirst();//方式一：分步骤实现
        initSecond();//方式二：基于事件流的链式调用
        initThird();//just、from和create的区别：前者适用于数据已经备齐，不可修改；而后者适用于数据的实时改变
    }
    private void initView(){
        tv=(TextView)findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initFirst(){
        //        创建被观察者&生产事件
        io.reactivex.Observable<Integer> observable= io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onComplete();
//                e.onError(new RuntimeException("aa错误"));
            }
        });
//        创建观察者并定义响应时间
        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
                disposable=d;
            }

            @Override
            public void onNext(Integer value) {
                if (value==3){
                    disposable.dispose();//取消订阅，一般用于stop和pause方法中
                    return;
                }
                Log.d(TAG, "对Next事件"+value+"做出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件做出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };
//        通过订阅连接被观察者和观察者(点菜)
        observable.subscribe(observer);
//        observable.subscribe(a)
    }
    private void initSecond(){
        io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe2连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件"+value+"做出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件做出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }
    private void initThird(){
        Observer<String[]> subscriber=new Observer<String[]>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String[] value) {
                Log.d(TAG, "onNext: "+value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
//        Observable<String> observable=Observable.just("test1","test2","test3");//自带遍历
        Observable<String[]>observable=Observable.just(new String[]{"test1","test2","test3"});//直接传整个数组
        observable.subscribe(subscriber);
    }
    final Observer<String> observerd=new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(String value) {
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                    .url("http://www.shljdb.cn/ifs/sys/organ/list?orgOid="+value)
                    .get()
                    .build();
            okhttp3.Call call=okHttpClient.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    String result=response.body().string();
                    Log.e("打印数据=",result);
                }
            });
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            Toast.makeText(MainActivity.this,"加载数据完成",Toast.LENGTH_SHORT).show();
        }
    };
    @OnClick({R.id.btn,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn:
//                Observable<String> observable=Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<String> e) throws Exception {
//                        Thread.sleep(6000);
//                        observer.onNext("图像处理完毕");
//                    }
//                });
//                observable.observeOn(AndroidSchedulers.mainThread());//观察者更新ui，主线程
//                observable.subscribeOn(Schedulers.io());//被观察者代码耗时在子线程
//                observable.subscribe(observer);
//                链式简写
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("1");
                        e.onNext("2");
                        e.onNext("3");
                        e.onNext("4");
                        e.onNext("5");
                        e.onNext("6");
                        e.onNext("7");
//                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(observerd);
                break;
            case R.id.btn2:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                            e.onNext(100);
                    }
                })
//                        单次转换
//                        .map(new Function<Integer, String>() {
//                            @Override
//                            public String apply(Integer integer) throws Exception {
//                                return "hzh"+integer;
//                            }
//                        })
//                        多次转换
                        .map(new Function<Integer,Float>() {
                            @Override
                            public Float apply(Integer integer) throws Exception {
                                return integer*30.0f;
                            }
                        })
                        .map(new Function<Float, String>() {
                            @Override
                            public String apply(Float aFloat) throws Exception {
                                return "hzh"+aFloat;
                            }
                        })
                        .subscribe(observerd);
                break;
            case R.id.btn3:
//                map可以将其转换为其他类型，而flatmap是先将其转换成一个新的Observable，进而遍历数组
                final String[]students={"张三","李四","王五"};
                Observable.create(new ObservableOnSubscribe<String[]>() {
                    @Override
                    public void subscribe(ObservableEmitter<String[]> e) throws Exception {
                            e.onNext(students);
                    }
                }).map(new Function<String[], String>() {
                    @Override
                    public String apply(String[] strings) throws Exception {
                        return students.toString();
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, "onNext:"+value );

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
            case R.id.btn4:
//                String BaseUrl="https://www.mxnzp.com/";
                Retrofit retrofit=new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ApiStores.BaseUrl)
                        .build();
                ApiStores apiStores=retrofit.create(ApiStores.class);
                Call<TestBean>call=apiStores.loadJokers("1");
                call.enqueue(new Callback<TestBean>() {
                    @Override
                    public void onResponse(Call<TestBean> call, Response<TestBean> response) {
                        //成功返回数据后在这里处理，使用response.body();获取得到的结果
                        TestBean testBean=response.body();
                        Log.e(TAG, "onResponse: code="+testBean.getCode());
                        Log.e(TAG, "onResponse: message="+testBean.getMsg());
                        Log.e(TAG, "onResponse: context="+testBean.getData().getList().get(0).getContent());
//                        response.body().toString();
//                        Log.d(TAG,s+"");
                    }

                    @Override
                    public void onFailure(Call<TestBean> call, Throwable t) {
                    //请求失败在这里处理
                        System.out.println("连接失败");
                    }
                });
                break;
            case R.id.btn5:

                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
