package com.wefal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    Button join_btn = null;
    EditText join_id = null, join_pwd = null;

    String id = "", pwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_btn = findViewById(R.id.join_btn);
        join_id = findViewById(R.id.join_id_editText);
        join_pwd = findViewById(R.id.join_pwd_editText);

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = join_id.getText().toString();
                pwd = join_pwd.getText().toString();

                System.out.println(id + "**********************");
                System.out.println(pwd + "**********************");
                //HttpPostData();
                InsertData task = new InsertData();
                task.execute("http://rpdst.dothome.co.kr/php/insert.php", id, pwd);
                Toast.makeText(getApplicationContext(), "전송 후 결과 받음", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void HttpPostData() {
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            String postData = "id="+id+"&pwd="+pwd;
            URL url = new URL("http://missing.dothome.co.kr/wefal/user/insertUser.php");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            System.out.println("코드: "+http.getResponseCode());
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                        // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("GET");
            http.connect();

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
//            buffer.append("id").append("=").append(id).append("&");                 // php 변수에 값 대입
//            buffer.append("pwd").append("=").append(pwd);   // php 변수 앞에 '$' 붙이지 않는다

            OutputStream outputStream = http.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            String result = builder.toString();
            ((TextView)(findViewById(R.id.join_pwdCheck_editText))).setText(result);

        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

    class InsertData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String pwd = (String)params[2];
            String serverURL = (String)params[0];

            String postParameters = "id=" + id + "&pwd=" + pwd;

            try {
                System.out.println("try 직후 실행");
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                System.out.println("connect() 후 실행");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                System.out.println(outputStream.toString());
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                System.out.println("POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                System.out.println(sb.toString());
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
