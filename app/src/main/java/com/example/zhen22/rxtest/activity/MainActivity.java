package com.example.zhen22.rxtest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zhen22.rxtest.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
private static final String TAG="RxJava";
private TextView tv;
private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
