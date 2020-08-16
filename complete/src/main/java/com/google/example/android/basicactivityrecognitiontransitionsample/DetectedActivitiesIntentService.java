package com.google.example.android.basicactivityrecognitiontransitionsample;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetectedActivitiesIntentService extends Service {
    protected static final String TAG = "DetectedActivitiesIS";
    private SharedPrefManager sharedPrefManager;

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
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        DetectedActivity activity = result.getMostProbableActivity();
        if(!sharedPrefManager.getLastActivity().isEmpty() && !sharedPrefManager.getLastActivity().equals(toActivityString(activity.getType()))) {
            Log.d("DetectedActivityService", "Transition Detected Notifying Activity");

            Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
//            newIntent.setAction("example.transition");
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, newIntent,
                    0);

            String channelId = getApplicationContext().getString(R.string.notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle("Me").
                    addMessage("New transition", System.currentTimeMillis(), "Sample app");

            Notification transitionNotification =
                    new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            //build summary info into InboxStyle template
                            .setStyle(style)
                            .setContentIntent(pendingIntent)
                            .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

//      notificationId is a unique int for each notification that you must define
            notificationManager.notify(1208, transitionNotification);

            sendMessage("Transition from " + sharedPrefManager.getLastActivity() + " to " + toActivityString(activity.getType()) + " detected at " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));
        }

        sharedPrefManager.setLastActivity(toActivityString(activity.getType()));

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
