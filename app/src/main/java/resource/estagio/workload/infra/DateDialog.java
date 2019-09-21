package resource.estagio.workload.infra;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import resource.estagio.workload.R;

public class DateDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    public DateDialog(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.CalendarTheme
                , listener, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, -1);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return datePickerDialog;
    }
}
