package br.com.onibusnahora.ui.app.dialogs;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import br.com.onibusnahora.ui.app.HomeActivity;
import br.com.onibusnahora.ui.app.Notificacao;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hora, minutos, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!HomeActivity.codigoDaRota.isEmpty()){

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            Intent intent = new Intent(getContext(), Notificacao.class);
            intent.putExtra("rota", HomeActivity.codigoDaRota);
            PendingIntent pendingIntent =  PendingIntent.getService(getContext(), 1, intent, PendingIntent.FLAG_NO_CREATE);

            AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

        }
    }
}
