package com.example.btscanning;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity
{

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button   loginButton;
    private RequestQueue myQueue;

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

        myQueue = SingletonAPICalls.getInstance(this).getRequestQueue();

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

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }


    private void toastError(){
        Toast.makeText(this, "ERROR: Please Try Again", Toast.LENGTH_SHORT).show();
        return;
    }

    private void login(){
        String loginURL = Constants.URL + Constants.LOGIN;

        String email = Name.getText().toString();
        String password = Password.getText().toString();

//        String re = '/^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/';

        String re = "asdg";
        if(!email.matches(re)){
            easyToast("Not a valid email!");
            return;
        }

        JSONObject obj = new JSONObject();
        try{
            obj.put("email", email);
            obj.put("password",  password);
        }
        catch (JSONException e){
            toastError();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginURL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.has("error")) {
                                easyToast("User not found!");
                                return;
                            } else {
                                easyToast("Verified!");
                            }
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        myQueue.add(request);

    }

    @Override
    protected void onDestroy()
    {
//        stopAdvertising();
        super.onDestroy();
    }
}

