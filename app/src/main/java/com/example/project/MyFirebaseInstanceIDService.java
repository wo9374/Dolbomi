//package com.example.project;
//
//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//import java.io.IOException;
//
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import okhttp3.Request;
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//    private static final String TAG = "MyFirebaseIIDService";
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//    // [START refresh_token]
//    @Override
//    public void onTokenRefresh() {
//        // 앱설치시 토근을 자동적으로 만든어준다
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        System.out.println("토큰이다!!!!"+refreshedToken);
//        // 토큰을 서버로 날려서 저장하기 위해 만듬
//        sendRegistrationToServer(refreshedToken);
//    }
//    // [END refresh_token]
//
//    /**
//     * Persist token to third-party servers.
//     *
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        // TODO: Implement this method to send token to your app server.
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token", token)
//                .build();
//        Request request = new Request.Builder()
//                .url("https://fcm.googleapis.com/fcm/send")
//                .post(body)
//                .build();
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}