package com.wiryaimd.pc_bali;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.wiryaimd.pc_bali.dto.ResponseDto;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpUtils {

    private static final String TAG = "HttpUtils";
    public static final String BASE_URL = "http://10.0.2.2:5000/";

    public static ResponseDto get(String endpoint, String token, boolean isFile){
        int code = 400;

        try {
            Log.d(TAG, "get: data open");
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + endpoint).openConnection();
            if (token != null){
                connection.addRequestProperty("Authorization", "Bearer " + token);
            }

            Log.d(TAG, "get: data check1");

            code = connection.getResponseCode();
            Log.d(TAG, "get: code: " + code);
            return isFile ? new ResponseDto(toBytes(connection.getInputStream()), code) : new ResponseDto(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()), code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseDto("", code);
    }

    public static ResponseDto post(String endpoint, String token, String json){
        int code = 400;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + endpoint).openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("content-type", "application/json");

            if (token != null){
                connection.addRequestProperty("Authorization", "Bearer " + token);
            }

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(json);
            dataOutputStream.flush();

            code = connection.getResponseCode();
            return new ResponseDto(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()), code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseDto("", code);
    }

    public static byte[] toBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = inputStream.read(buffer);
        while (len != -1){
            baos.write(buffer, 0, len);
            len = inputStream.read(buffer);
        }

        return baos.toByteArray();
    }

    public static void showToast(Context context, String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
