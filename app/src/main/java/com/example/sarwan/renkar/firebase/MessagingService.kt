package com.example.sarwan.renkar.firebase

import android.util.Log
import com.example.sarwan.renkar.extras.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService() : FirebaseMessagingService(){

    var TAG = "MessagingService"
    var CHANNEL_ID = "Renkar_Channel"// The id of the channel.
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("onMessageReceived",remoteMessage?.notification?.title + " " +
                remoteMessage?.notification?.body)
        //sendNotification(remoteMessage?.notification?.name,remoteMessage?.notification?.body)
    }


    override fun onNewToken(token: String?) {
        Log.d(TAG, "Registration token$token")
        saveFirebaseTokenInSharePreference(token)


    }

    private fun saveFirebaseTokenInSharePreference(token: String?) {
        val sharedPreference = SharedPreferences(applicationContext)
        sharedPreference.saveFirebaseToken(token!!)

        var userProfile = sharedPreference.userProfile;
        /*if (userProfile?.token != null)
        {
            sendTokenToServer(token, userProfile.token)
        }*/
    }

    /*private fun sendTokenToServer(token: String, authToken: String?) {
        val map : HashMap<String, String> = HashMap()
        map.put("fcm_token",token)
        map.put("device_type","android")
        RestClient.getRestAuthenticatedAdapter(authToken).sendFCM(map).enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                Log.d(TAG,response?.message())
            }
        })
    }


    private fun sendNotification(name : String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(name)
                .setBadgeIconType(R.mipmap.ic_launcher_round)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            R.drawable.ic_launcher_round_white
                        else R.mipmap.ic_launcher_round)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                    "Visconn",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build())
    }*/
}