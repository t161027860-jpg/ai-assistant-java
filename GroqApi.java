package com.example.aiassistant;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroqApi {

    public interface Callback {
        void onSuccess(String answer);
        void onError(String error);
    }

    private final OkHttpClient client;

    public GroqApi() {
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void ask(String apiKey, String model, String systemPrompt,
                    String userQuery, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("model", model);
            body.put("max_tokens", 200);
            body.put("temperature", 0.7);

            JSONArray messages = new JSONArray();

            JSONObject sys = new JSONObject();
            sys.put("role", "system");
            sys.put("content", systemPrompt);
            messages.put(sys);

            JSONObject user = new JSONObject();
            user.put("role", "user");
            user.put("content", userQuery);
            messages.put(user);

            body.put("messages", messages);

            Request request = new Request.Builder()
                    .url(Constants.GROQ_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(body.toString(),
                            okhttp3.MediaType.parse("application/json")))
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                final Handler main = new Handler(Looper.getMainLooper());

                @Override
                public void onFailure(Call call, IOException e) {
                    main.post(() -> callback.onError(e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String resp = response.body().string();
                        JSONObject json = new JSONObject(resp);
                        if (json.has("error")) {
                            main.post(() -> callback.onError(
                                    json.optJSONObject("error").optString("message")));
                            return;
                        }
                        String answer = json.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                                .trim();
                        main.post(() -> callback.onSuccess(answer));
                    } catch (Exception e) {
                        main.post(() -> callback.onError(e.getMessage()));
                    }
                }
            });
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
}
