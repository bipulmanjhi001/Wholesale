package wholesalefactory.co.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
import wholesalefactory.co.R;
import wholesalefactory.co.main.Splashscreen;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
        private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            if (remoteMessage == null)
                return;
           // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
            }
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }
       private void handleNotification(String message) {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }else{
                // If the app is in background, firebase itself handles the notification
            }
        }

        private void handleDataMessage(JSONObject json) {
           // Log.e(TAG, "push json: " + json.toString());
            try {
                JSONObject data = json.getJSONObject("data");
                String title = data.getString("title");
                String message = data.getString("message");
                boolean isBackground = data.getBoolean("is_background");
                String imageUrl = data.getString("image");
                String timestamp = data.getString("timestamp");


              /*  Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                Log.e(TAG, "isBackground: " + isBackground);
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);*/


                Intent intent = new Intent(getApplicationContext(), Splashscreen.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + getApplicationContext().getPackageName() + "/raw/notification");

                Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                Notification notif = new Notification.Builder(getApplicationContext())
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(message))
                        .setSmallIcon(R.mipmap.ic_stat_w)
                        .setLargeIcon(big_bitmap_image)
                        .setSound(alarmSound)
                        .build();
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(1, notif);

            } catch (JSONException e) {
                Log.e(TAG, "Json Exception: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }