package com.google.example.android.basicactivityrecognitiontransitionsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetectedActivitiesIntentService extends Service {
    protected static final String TAG = "DetectedActivitiesIS";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super();
    }

    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.ON_FOOT:
                return "ON FOOT";
            default:
                return "UNKNOWN";
        }
    }

    private void sendMessage(String s) {
        Intent intent = new Intent("my-integer");
        intent.putExtra("message", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        DetectedActivity activity = result.getMostProbableActivity();
        Log.d("DetectedActivityService", "Activity Detected Notifying Activity");

        sendMessage(toActivityString(activity.getType()) + " detected at " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
