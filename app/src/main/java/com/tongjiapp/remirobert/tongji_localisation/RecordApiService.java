package com.tongjiapp.remirobert.tongji_localisation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by remirobert on 03/05/16.
 */
public interface RecordApiService {

    @POST("records")
    Call<ResponseApi> postRecords(@Body List<Device> records);
}
