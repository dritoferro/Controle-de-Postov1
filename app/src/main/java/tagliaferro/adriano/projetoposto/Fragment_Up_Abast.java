package tagliaferro.adriano.projetoposto;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;


/**
 * Created by Adriano on 01/02/2016.
 */
public class Fragment_Up_Abast extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_up_abast, container, false);
    }

}
