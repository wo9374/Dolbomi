package com.example.project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class PushMsgTask extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    public final String API_KEY = "AAAAK4e5SaM:APA91bFsjSQE6v1IGZ3Dx4EuGteIcNK1YoHSqR7Vul-wx_Mjqu1SI56UEgaCz0mpLBLNORSv6cNJWP2D5eBu1I6u-5_ScBj8AXSxtwxlrEiy9HVL-XIF4d64dPFgAkr90OigVk-gyD0X";


    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            String token=strings[0];
            //String token = "dZ1MAS9pOtM:APA91bEfn-N7Dk5BxaOZSJYiyLUmEvkr1DYzs9WD3lsbZgU9kG-ev0-9hx9D85UJDDxxJXf68JTa2nvJJlcSDWpuO8FhQUgci2rHPW1f5DmXt-q1vRe9UEIWvon-IoJgJ4DFvLVlcwtL";
            String title = strings[1];
            String body = strings[2];
            // 접속할 서버 주소
            URL url = new URL("https://fcm.googleapis.com/fcm/send");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key="+API_KEY);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());


            HashMap<String, String> map = new HashMap<>();
            HashMap<String, String> map2 = new HashMap<>();
            HashMap<String, String> notification = new HashMap<>();

            notification.put("title", title);
            notification.put("body", body);

            map.put("score", "5x1"); // 샘플 데이터
            map.put("time", "15:10"); // 샘플 데이터
            //map2.put("to", "cekjQJ8ZQd8:APA91bFfwcdk9DiRBItqMwfoqsKyEKvPvoIMaya302EctUwXgANpbwFG7ibifIqgPYWB2wMwZcKTA72gQooQnuWIhoPKWnDse4mz5amyCDAaaHxr7eokW5gl_3xAD-ostwM7DF-XqYHK");
            map2.put("to", token);
            JSONObject data = new JSONObject(map);
            JSONObject jsonObject = new JSONObject(map2);
            try {
                jsonObject.put("data", data);
                jsonObject.put("notification", new JSONObject(notification));
            } catch(Exception e) {
                Log.e("푸시알람오류", "오류");
            }
            sendMsg = jsonObject.toString();
            Log.i("json", sendMsg);
            osw.write(sendMsg);
            osw.flush();

            //jsp와 통신 성공 시 수행
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp에서 보낸 값을 받는 부분
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                // 통신 실패
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //jsp로부터 받은 리턴 값
        return receiveMsg;


    }


}
