package com.example.btscanning;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button   loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initialize();
        //startAdvertising();
        //setTimeout();

        Name = findViewById(R.id.name);
        Password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();

            }
        });

    }

    private void login(){

    }

    @Override
    protected void onDestroy()
    {
//        stopAdvertising();
        super.onDestroy();
    }
}

