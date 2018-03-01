package me.silverfern.desktop.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by HAlexTM on 28/02/2018.
 */
public class API {

    private static String buildPostData(Map<String, String> data) throws UnsupportedEncodingException {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            joiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return joiner.toString();
    }

    public static JsonElement sendPost(String url, Map<String, String> dataMap) {
        try {
            String data = buildPostData(dataMap);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return new JsonParser().parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonElement sendPost(String url, JsonElement data) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(data.toString().getBytes());
            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return new JsonParser().parse(reader);
        } catch (IOException e) {
            // Get error stream as JSON if it's not a server error
            try {
                if (conn != null) {
                    int code = conn.getResponseCode();
                    if (code > 400 && code < 500) {
                        return new JsonParser().parse(new BufferedReader(new InputStreamReader(conn.getErrorStream())));
                    }
                    e.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
