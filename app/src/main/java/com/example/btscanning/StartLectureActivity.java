package com.example.btscanning;

import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.util.UUID;
import android.os.ParcelUuid;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class StartLectureActivity extends AppCompatActivity {

    private String className;
    private String classID;
    private String classDbID;
    private TextView classNameView;
    private ParcelUuid lectureUUID;
    private Button startLecture;
    private Button stopLecture;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private AdvertiseCallback mAdvertiseCallback;
    private Handler mHandler;
    private Runnable timeoutRunnable;
    private final static int REQUEST_COARSE_LOCATION = 1;
    private long TIMEOUT = 10000;

    public static final String ADVERTISING_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";
    public static final String ADVERTISING_FAILED_EXTRA_CODE = "failureCode";

    private RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lecture);

        classNameView = findViewById(R.id.className);
        startLecture = findViewById(R.id.startScanning);
        stopLecture = findViewById(R.id.stopScanning);

        myQueue = SingletonAPICalls.getInstance(this).getRequestQueue();

        stopLecture.setEnabled(false);

        setIntentInstance();

        startLecture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(StartLectureActivity.this, "Starting new Lecture", Toast.LENGTH_SHORT).show();
                startLecture.setEnabled(false);
                initialize();
            }
        });

        stopLecture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(StartLectureActivity.this, "Stopping Lecture", Toast.LENGTH_SHORT).show();
                stopAdvertising();
                startLecture.setEnabled(true);

            }
        });

    }

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(StartLectureActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }


    private void setIntentInstance(){
        if(getIntent().hasExtra("classID") && getIntent().hasExtra("className") && getIntent().hasExtra("classDbID")){
            className = getIntent().getStringExtra("className");
            classID = getIntent().getStringExtra("classID");
            classDbID = getIntent().getStringExtra("classDbID");

            classNameView.setText(className);
        }
    }

    public void initialize()
    {
        if(mBluetoothLeAdvertiser == null)
        {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if(mBluetoothManager != null)
            {
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                if(mBluetoothAdapter != null)
                {
                    if(!mBluetoothAdapter.isEnabled())
                    {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 2);
                    }
                    else
                    {
                        if(mBluetoothAdapter.isMultipleAdvertisementSupported())
                        {
                            mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                            startAdvertising();

                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "BT adapter null", Toast.LENGTH_SHORT).show();
                    startLecture.setEnabled(true);
                }
            }
            else
            {
                Toast.makeText(this, "BT manager null", Toast.LENGTH_SHORT).show();
                startLecture.setEnabled(true);
            }
        }

        else{
            startAdvertising();
        }
    }

//    private void setTimeout()
//    {
//        mHandler = new Handler();
//        timeoutRunnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                sendFailureIntent(-1);
//                //stopAdvertising();
//            }
//        };
//        mHandler.postDelayed(timeoutRunnable, TIMEOUT);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        Log.d("advertise", "Entered on activity result");
        if(requestCode == 2)
        {
            if(resultCode == RESULT_OK)
            {
                if(mBluetoothAdapter.isMultipleAdvertisementSupported())
                {
                    mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                    startAdvertising();
                    //setTimeout();
                }
                else
                {
                    Toast.makeText(this, "Multiple ads not supported", Toast.LENGTH_SHORT).show();
                    startLecture.setEnabled(true);
                }
            }
        }
    }

    public void startAdvertising()
    {
        mAdvertiseCallback = null;

        if(mAdvertiseCallback == null)
        {
            AdvertiseSettings settings = buildAdvertiseSettings();
            AdvertiseData data = buildAdvertiseData();
            mAdvertiseCallback = new CustomAdvertiseCallback();

            if(mBluetoothLeAdvertiser != null && data != null)
            {
                mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
                Log.d("advertise", "Started advertising");
                stopLecture.setEnabled(true);
            }

            else{
                easyToast("Something went wrong please try again");
                startLecture.setEnabled(true);
                return;
            }
        }
    }

    public void stopAdvertising()
    {
        Log.d("advertise", "Service: Stopping Advertising");
        if (mBluetoothLeAdvertiser != null && mAdvertiseCallback != null)
        {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mAdvertiseCallback = null;
            stopLecture.setEnabled(false);
        }
    }

    private AdvertiseData buildAdvertiseData()
    {
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();

        lectureUUID = ParcelUuid.fromString(UUID.randomUUID().toString());

        try{
            createLecture();
        }

        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        dataBuilder.addServiceUuid(lectureUUID);
        Log.d("advertise", lectureUUID.toString());

        return dataBuilder.build();
    }

    private void createLecture(){

        String loginURL = Constants.URL + Constants.Lectures + classDbID + "/" + lectureUUID + "/" + APIKeys.apiKey;

        JSONObject obj = new JSONObject();

        try{
            obj.put("class_id", classDbID);
            obj.put("profUUID",  lectureUUID.toString());
        }
        catch (JSONException e){
            easyToast("ERROR: Please try again.");
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginURL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            if (response.has("error")) {
                                easyToast("Failed to find Lecture!");

                            } else {
                                easyToast("Created Lecture!");

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
                easyToast("error!!");

            }
        });

        myQueue.add(request);

    }

    private AdvertiseSettings buildAdvertiseSettings()
    {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setTimeout(0);

        return settingsBuilder.build();
    }

    private class CustomAdvertiseCallback extends AdvertiseCallback
    {
        @Override
        public void onStartFailure(int errorCode)
        {
            super.onStartFailure(errorCode);

            String description;
            if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED)
                description = "ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS)
                description = "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED)
                description = "ADVERTISE_FAILED_ALREADY_STARTED";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE)
                description = "ADVERTISE_FAILED_DATA_TOO_LARGE";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR)
                description = "ADVERTISE_FAILED_INTERNAL_ERROR";
            else description = "unknown";

            Log.d("advertise", "Advertise fail" + description);
            sendFailureIntent(errorCode);
            //finish();

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect)
        {
            super.onStartSuccess(settingsInEffect);
            Log.d("advertise", "Advertise success");
        }
    }

    private void sendFailureIntent(int errorCode)
    {
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISING_FAILED);
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
        Log.d("advertise", failureIntent.toString());
        startLecture.setEnabled(true);
    }

    @Override
    protected void onDestroy()
    {
        stopAdvertising();
        super.onDestroy();
    }
}
