package br.com.onibusnahora.ui.app;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import br.com.onibusnahora.R;

public class Notificacao extends IntentService {
    private static String CHANNEL_ID = "CANAL_1";

    public Notificacao(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String rota = intent.getStringExtra("rota");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bus_fab)
                .setContentTitle(rota)
                .setContentText("Testando conteúdo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "onibus-na-hora";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Canal de notificação para ônibus na hora");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;

            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, builder.build());
        } else {
            builder.build();
        }



    }


}
