package com.tongjiapp.remirobert.tongji_localisation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
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
    private Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

    public void createNewDevice(Device device, final RecordApiManagerListener listener) {

        final String jsonString = "[\n" +
                " {\n" +
                "  \"value\": 1\n" +
                " }\n" +
                "]";


        JSONObject student1 = new JSONObject();
        try {
            student1.put("did", "3");
            student1.put("test", "OK");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject student2 = new JSONObject();
        try {
            student2.put("id", "3");
            student2.put("test", "OK");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JSONArray jsonArray = new JSONArray();

        jsonArray.put(student1);
        jsonArray.put(student2);


        List<Device> deviceList = new ArrayList<Device>();
        deviceList.add(device);
        deviceList.add(device);

        RecordApiService recordApiService = retrofit.create(RecordApiService.class);

        Call<ResponseApi> call = recordApiService.postRecords(deviceList);

//        call.enqueue(new Callback<ResponseApi>() {
//            @Override
//            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
//                Log.v("OK", "response");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseApi> call, Throwable t) {
//                Log.e("Err", "Error");
//            }
//        });
    }

    public RecordApiManager(Context context) {
        mContext = context;
    }
}
