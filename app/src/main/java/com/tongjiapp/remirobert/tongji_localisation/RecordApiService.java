package com.tongjiapp.remirobert.tongji_localisation;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by remirobert on 03/05/16.
 */
public interface RecordApiService {

    @POST("device")
    Call<ResponseApi> postDevice(@Body JSONObject device);

    @POST("record")
    Call<ResponseApi> postRecord(@Body JSONObject record);

    @POST("records")
    Call<ResponseApi> postRecords(@Body List<JSONObject> records);
}
