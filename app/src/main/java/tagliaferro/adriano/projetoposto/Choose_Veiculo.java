package tagliaferro.adriano.projetoposto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;

/**
 * Created by Adriano on 03/02/2016.
 */
public class Choose_Veiculo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private VeicDAO veicDAO;
    List<Veiculo> veiculos;
    private Spinner spVec;
    private ArrayAdapter<String> listaVec;
    private boolean firstTime = true;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_veiculo);
        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        veicDAO = VeicDAO.getInstance(this);
        spVec = (Spinner) findViewById(R.id.spVec);
        loading();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!firstTime) {
            String name = parent.getItemAtPosition(position).toString();
            //Toast.makeText(this, "Veículo: " + name, Toast.LENGTH_SHORT).show();
            if(!name.equals("Selecione")) {
                Intent in = new Intent();
                in.putExtra("nome", name);
                setResult(RESULT_OK, in);
                finish();
            }
        } else {
            firstTime = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK){
            loading();
        }
    }

    public void loading(){
        veiculos = veicDAO.listVeicNome();
        List<String> nomes = new ArrayList<>();
        if (veiculos.size() > 1) {
            for (int i = 0; i < veiculos.size(); i++) {
                nomes.add(veiculos.get(i).getNome());
            }
            listaVec = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomes);
            spVec.setAdapter(listaVec);
            spVec.setOnItemSelectedListener(this);
        } else {
            Intent intent = new Intent(this, IncAltVeiculo.class);
            startActivityForResult(intent, 1);
        }
    }

    public void incVec(View view){
        Intent intent = new Intent(this, IncAltVeiculo.class);
        startActivityForResult(intent, 1);
    }
}
