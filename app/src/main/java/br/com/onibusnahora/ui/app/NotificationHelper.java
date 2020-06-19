package br.com.onibusnahora.ui.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import br.com.onibusnahora.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CANAL_1_ID = "canal1ID";
    public static final String CANAL_1_NOME = "canal 1";

    private NotificationManager mNotificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            criarCanalDeNotificacao();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criarCanalDeNotificacao() {
        NotificationChannel channel = new NotificationChannel(CANAL_1_ID, CANAL_1_NOME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);

        getNotificationManager().createNotificationChannel(channel);
    }

    public NotificationManager getNotificationManager(){
        if (mNotificationManager == null){
            mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message, PendingIntent pendingIntent){
        return new NotificationCompat.Builder(getApplicationContext(), CANAL_1_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_bus_fab)
                .setContentIntent(pendingIntent);
    }
}
