package br.com.onibusnahora.ui.app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper mNotificationHelper = new NotificationHelper(context);
        Intent home = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, home, 0);

        NotificationCompat.Builder nb = mNotificationHelper
                .getChannelNotification("Hora de pegar o ônibus...",
                        "Clique aqui para ver o ponto mais próximo de você", pendingIntent);

        mNotificationHelper.getNotificationManager().notify(1, nb.build());
    }
}
