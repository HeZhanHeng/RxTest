package com.example.zhen22.rxtest;

import com.example.zhen22.rxtest.module.TestBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by HeZhanHeng on 2019/12/18.
 */

public interface ApiStores {
//       https://www.mxnzp.com/api/jokes/list?page=1
    String BaseUrl="https://www.mxnzp.com/";
//    问号后面的注解需要用query(动态)
    @POST("api/jokes/list/?")
    Call<TestBean> loadJokers(@Query("page") String page);

}
