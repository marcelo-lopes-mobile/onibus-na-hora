package br.com.onibusnahora.ui.app.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import br.com.onibusnahora.ui.app.HomeActivity;

public class TimePickerFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(), (TimePickerDialog.OnTimeSetListener) getActivity(), hora, minutos, true);
    }

}
