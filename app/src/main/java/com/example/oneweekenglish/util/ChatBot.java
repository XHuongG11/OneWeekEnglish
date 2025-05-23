package com.example.oneweekenglish.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot {
    public interface ChatBotCallback {
        void onResponse(String message);

        void onError(String errorMessage);
    }
    public static void sendToGeminiAPI(Context context, String message, ChatBotCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String apiKey = ""; // thay bằng API key thật
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        // Tạo JSON payload
        JSONObject part = new JSONObject();
        JSONObject content = new JSONObject();
        try {
            part.put("text", message+". Hãy trả lời dưới góc nhìn của một trợ lí học tập xoay quanh chủ đề học tiếng anh, đưa ra câu trả lời ngắn gọn, xúc tích. Chỉ trả lời tiếng việt thôi nha.");
            content.put("parts", new JSONArray().put(part));

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("contents", new JSONArray().put(content));

            RequestBody body = RequestBody.create(
                    MediaType.get("application/json; charset=utf-8"),
                    jsonBody.toString()
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                Handler mainHandler = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(Call call, IOException e) {
                    mainHandler.post(() -> callback.onError("Lỗi mạng: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resBody = response.body().string();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject resJson = new JSONObject(resBody);
                            String result = resJson
                                    .getJSONArray("candidates")
                                    .getJSONObject(0)
                                    .getJSONObject("content")
                                    .getJSONArray("parts")
                                    .getJSONObject(0)
                                    .getString("text");

                            mainHandler.post(() -> callback.onResponse(result));
                        } catch (JSONException e) {
                            mainHandler.post(() -> callback.onError("Lỗi phân tích JSON"));
                        }
                    } else {
                        mainHandler.post(() -> callback.onError("Lỗi từ server Gemini"));
                    }
                }
            });

        } catch (JSONException e) {
            callback.onError("Lỗi JSON: " + e.getMessage());
        }
    }



//    public static void sendMsgToChatBot(Context context, String msg, String id, ChatBotCallback callback) {
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("id", id);
//            jsonBody.put("msg", msg);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            callback.onError("Lỗi định dạng JSON");
//            return;
//        }
//
//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                jsonBody.toString()
//        );
//
//        Request request = new Request.Builder()
//                .url("https://n8n.nayamishop.id.vn/webhook/chatbot")
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            Handler mainHandler = new Handler(Looper.getMainLooper());
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                mainHandler.post(() -> {
//                    Toast.makeText(context, "Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
//                    callback.onError(e.getMessage());
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(responseText);
//                        String output = jsonObject.getString("output");
//                        mainHandler.post(() -> callback.onResponse(output));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        mainHandler.post(() -> callback.onError("Lỗi phân tích phản hồi"));
//                    }
//                } else {
//                    mainHandler.post(() -> {
//                        Toast.makeText(context, "Chatbot phản hồi lỗi", Toast.LENGTH_SHORT).show();
//                        callback.onError("Phản hồi lỗi");
//                    });
//                }
//            }
//        });
//    }
}
