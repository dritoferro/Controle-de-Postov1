package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import tagliaferro.adriano.projetoposto.Data.PosDAO;

/**
 * Created by Adriano on 25/01/2016.
 */
public class ExcPosto extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    Fragment_ExcPosto excPosto = new Fragment_ExcPosto();
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exc_posto);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frmExcPosto, excPosto, "excPosto");
        ft.commit();
        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }


}
