package com.example.btscanning;

import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;


public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    ClassAdapter adapter;
    Boolean flag = false;

    List<Class> classList;

    private RequestQueue myQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        classList = new ArrayList<>();

        myQueue = SingletonAPICalls.getInstance(this).getRequestQueue();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadClasses();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar a = getSupportActionBar();
        //a.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_item:
                return logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean logout(){
        Constants.LOGGED_IN = false;
        SaveSharedPreference.setUserName(Dashboard.this, "");
        SaveSharedPreference.setPrefProfNID(Dashboard.this, "");
        finish();
        easyToast("Logging out");
        Intent backLogin = new Intent(Dashboard.this, MainActivity.class);
        //backLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backLogin);
        return true;
    }

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Dashboard.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }

    private void loadClasses(){
        String loginURL = Constants.URL + Constants.Classes + SaveSharedPreference.getPrefProfNid(Dashboard.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray classes = new JSONArray(response);

                            for (int i = 0; i < classes.length(); i++) {
                                JSONObject classObject = classes.getJSONObject(i);

                                Class newClass = new Class(
                                        classObject.getString("_id"),
                                        classObject.getString("courseID"),
                                        classObject.getString("className"),
                                        classObject.getString("startTime"),
                                        classObject.getString("endTime"));

                                classList.add(newClass);

                            }

                            adapter = new ClassAdapter(Dashboard.this, classList);
                            recyclerView.addItemDecoration(new DividerItemDecoration(Dashboard.this,LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(adapter);

                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Class curClass = classList.get(position);
                                    //Toast.makeText(getApplicationContext(), curClass.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Dashboard.this, StartLectureActivity.class);
                                    intent.putExtra("classID", curClass.getCourse_id());
                                    intent.putExtra("className", curClass.getName());
                                    intent.putExtra("classDbID", curClass.getDatabase_id());
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //easyToast("Failed to get Classes from Database!");
            }
        });

        myQueue.add(stringRequest);

    }
}
