package com.wefal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    Button join_btn = null;
    EditText join_email = null, join_pwd = null, join_pwd_check = null;

    String email = "", pwd = "", pwd_check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_btn = findViewById(R.id.join_btn);
        join_email = findViewById(R.id.join_email_editText);
        join_pwd = findViewById(R.id.join_pwd_editText);
        join_pwd_check = findViewById(R.id.join_pwdCheck_editText);

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = join_email.getText().toString();
                pwd = join_pwd.getText().toString();
                pwd_check = join_pwd_check.getText().toString();

                //유효성 검사

                //이메일 유효성 검사
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(getApplicationContext(),"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                //비밀번호와 비밀번호 확인 일치 여부 검사
                if(!pwd.equals(pwd_check)) {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pwd))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                InsertData task = new InsertData();
                task.execute("http://missing.dothome.co.kr/wefal/user/insertUser.php", email, pwd);
                Toast.makeText(getApplicationContext(), "전송 후 결과 받음", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String pwd = (String)params[2];
            String serverURL = (String)params[0];

            String postParameters = "email=" + email + "&pwd=" + pwd;

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
                inputStreamReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                System.out.println(sb.toString());
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
