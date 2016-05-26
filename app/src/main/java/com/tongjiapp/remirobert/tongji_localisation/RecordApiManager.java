package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by remirobert on 02/05/16.
 */

enum RecordApiManagerStatus {
    SUCCESS, FAILED
}

interface RecordApiManagerListener {
    void onReceiveReponse(RecordApiManagerStatus status);
}

public class RecordApiManager {

    private static final String BASE_URL = "http://tztztztztz.org:3000/";

    private Context mContext;
    private Retrofit mRetrofit;
    private RecordApiService mRecordApiService;

    public void createNewDevice(Device device, final RecordApiManagerListener listener) {

        JSONObject json;
        try {
            json = device.toJson();
        } catch (JSONException e) {
            listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
            return;
        }

        Call<ResponseApi> call = mRecordApiService.postDevice(json);

        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                Log.v("OK", "response");
                listener.onReceiveReponse(RecordApiManagerStatus.SUCCESS);
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Err", "Error");
                listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
            }
        });
    }

    public void createNewRecord(Record record, String deviceId, final RecordApiManagerListener listener) {
        JSONObject json;
        try {
            json = record.toJson();
            json.put("device_id", deviceId);
        } catch (JSONException e) {
            listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
            return;
        }
        Call<ResponseApi> call = mRecordApiService.postRecord(json);

        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                Log.v("OK", "response Record");
                listener.onReceiveReponse(RecordApiManagerStatus.SUCCESS);
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Err", "Error");
                listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
            }
        });
    }

    public void createRecords(List<Record> records, final RecordApiManagerListener listener) {
        List<JSONObject> recordsList = new ArrayList<JSONObject>();

        for (Record record : records) {
            JSONObject recordJson;

            try {
                recordJson = record.toJson();
            } catch (JSONException e) {
                listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
                return;
            }
            recordsList.add(recordJson);
        }

        Call<ResponseApi> call = mRecordApiService.postRecords(recordsList);

        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                Log.v("OK", "response");
                listener.onReceiveReponse(RecordApiManagerStatus.SUCCESS);
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Err", "Error");
                listener.onReceiveReponse(RecordApiManagerStatus.FAILED);
            }
        });
    }

    public RecordApiManager(Context context) {
        mContext = context;
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        mRecordApiService = mRetrofit.create(RecordApiService.class);
    }
}
