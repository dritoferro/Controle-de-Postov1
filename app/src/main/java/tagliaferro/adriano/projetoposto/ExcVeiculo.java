package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Adriano on 25/01/2016.
 */
public class ExcVeiculo extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    Fragment_ExcVeiculo excVeiculo = new Fragment_ExcVeiculo();
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exc_veiculo);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frmExcVeic, excVeiculo, "excVeiculo");
        ft.commit();
        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

    }

}
