package cn.com.lttc.loginui.utils;


import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import okhttp3.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

public class FileTools {

    //读取txt文件中的内容
    public static String readFile(String file) throws IOException {
        String storageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileInputStream inputStream = new FileInputStream(storageDirectory + "/" + file);
        int len = inputStream.available();
        byte[] buffer = new byte[len];
        inputStream.read(buffer);
        inputStream.close();
        String content = new String(buffer);
        return content;

    }

    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param file
     * @param content
     */
    public static void addWriteFile(String file, String content) {
        String storageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(storageDirectory + "/" + file, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(String file) {
        File file1 = new File(file);
        if (file1.exists()) {
            file1.delete();
        }
    }

    public static void post_file(final String url, final Map<String, Object> map, File file, final Context context) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq", "onFailure");
                // TODO: 2021/4/1 注意一下这里
                Looper.prepare();
                Toast.makeText(context,"上传图片失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);
                    //Can't toast on a thread that has not called Looper.prepare()
                    Looper.prepare();
                    Toast.makeText(context,"上传图片成功",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Log.i("lfq", response.message() + " error : body " + response.body().string());
                }
            }
        });

    }


    public static void main(String[] args) throws IOException {
        addWriteFile("a.txt", "hello");
        String s = readFile("a.txt");
        System.out.println(s);
        deleteFile("a.txt");
        String s2 = readFile("a.txt");
        System.out.println(s2);
    }
}
