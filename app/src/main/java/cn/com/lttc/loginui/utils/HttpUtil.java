package cn.com.lttc.loginui.utils;

import org.json.JSONObject;

import java.io.*;
import java.io.IOException;
import java.net.*;

public class HttpUtil {

    public String get(String url) {
        try {
            URL myUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String temp = "";
            StringBuilder builder = new StringBuilder();
            while ((temp = buf.readLine()) != null) {
                builder.append(temp);
            }
            connection.disconnect();
            buf.close();
            return String.valueOf(builder);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //post请求体
    //param = new JSONObject("请求体字符串");
    public static String sendPost(String url, JSONObject param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            // 设置连接超时时间
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
