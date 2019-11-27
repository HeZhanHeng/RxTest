package com.example.zhen22.rxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirst();//方式一：分步骤实现
        initSecond();//方式二：基于事件流的链式调用
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
}
