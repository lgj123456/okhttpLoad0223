package com.example.yhdj.ad0223load;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mButton;
    private String url = "http://192.168.134.79:8080/Okhttp/";
    private Button btnLoad;
    private static final String TAG = "MainActivity";
    private SeekBar mSeekBar;
    private Button btn_post;
    private TextView tv_content;
    private Button btn_postString;
    private Button btn_postFile;
    private Button btn_img;
    OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();


    }

    private void initViews() {
        mButton = (Button) findViewById(R.id.btn_img);
        mImageView = (ImageView) findViewById(R.id.iv_img);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        btn_post = (Button) findViewById(R.id.btn_doPost);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_postString = (Button) findViewById(R.id.btn_doPostString);
        btn_postFile = (Button) findViewById(R.id.btn_doPostFile);
        btn_img = (Button)findViewById(R.id.btn_image);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imge();
            }
        });
        btn_postFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(),"snake");
                if(!file.exists()){
                    Log.e(TAG, "onClick: " + file.getAbsolutePath() + "no exist");
                    return;
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
                com.squareup.okhttp.Request request = builder.url(url + "postFile").post(requestBody).build();
                executeRequest(request);
            }
        });
        btn_postString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPostString(v);
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
                RequestBody requestBody =  requestBodyBuilder.add("userName", "abcd").add("password","123456789").build();
                com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
                com.squareup.okhttp.Request request = builder.url(url + "login").post(requestBody).build();
                executeRequest(request);

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoad();

            }
        });

        btnLoad = (Button) findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://gdown.baidu.com/data/wisegame/054778c66b49fff5/tanchishedazuozhan_2056.apk";
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "snakeGame")//
                        {

                            @Override
                            public void onBefore(Request request, int id)
                            {
                                Toast.makeText(MainActivity.this, "load.......", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void inProgress(float progress, long total, int id)
                            {

                                Log.e(TAG, "inProgress :" + (int) (100 * progress));
                                mSeekBar.setProgress((int) (100 * progress));
                            }

                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onError :" + e.getMessage());
                            }

                            @Override
                            public void onResponse(File file, int id)
                            {
                                Toast.makeText(MainActivity.this, "下载成功！！！", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                            }
                        });
            }
        });
    }

    private void imge() {
        // 加载图片
        String urlImg = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils.get().url(urlImg).tag(this).build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        mImageView.setImageBitmap(response);
                    }
                });
    }

    //get方式上传参数
    private void upLoad() {
        //原生okhttp的get方法连接服务器

//        com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
//       com.squareup.okhttp.Request request =  builder.get().url("http://192.168.134.79:8080/Okhttp/" + "login?userName=abc&password=1234").build();
//        executeRequest(request);

        //okhttpUtil工具类连接服务器
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName","qwe");
        params.put("password", "123456");
        OkHttpUtils.get().url("http://192.168.134.79:8080/Okhttp/login").params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(MainActivity.this, response+"上传成功！！！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeRequest(com.squareup.okhttp.Request request) {

        com.squareup.okhttp.Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                Log.d("error","error");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d(TAG, "onResponse: success");
            }
        });
    }

    public void doPostString(View view){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "{userName:a1,password:a123}");
        com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
        com.squareup.okhttp.Request request = builder.url(url + "postString").post(requestBody).build();
        executeRequest(request);
    }
}
