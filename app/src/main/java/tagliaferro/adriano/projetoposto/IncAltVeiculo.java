package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tagliaferro.adriano.projetoposto.Data.AbasDAO;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;
import tagliaferro.adriano.projetoposto.Tips.Tip2;

/**
 * Created by Adriano on 25/01/2016.
 */
public class IncAltVeiculo extends AppCompatActivity {

    private Spinner spComb;
    private Spinner spCat;
    private EditText txtNomeVec;
    private VeicDAO veicDAO;
    private Veiculo veiculo;
    private AbasDAO abasDAO;
    private int id;
    Bundle d;
    Intent i;
    private String antigoNome;
    ArrayAdapter<CharSequence> adapter, adapter2;
    ActionBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d = new Bundle();
        i = getIntent();
        d = i.getExtras();

        setContentView(R.layout.inc_alt_veiculo);
        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        spComb = (Spinner) findViewById(R.id.spComb);
        adapter = ArrayAdapter.createFromResource(this, R.array.tiposComb, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComb.setAdapter(adapter);


        spCat = (Spinner) findViewById(R.id.spCat);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.catVec, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCat.setAdapter(adapter2);

        txtNomeVec = (EditText) findViewById(R.id.txtNomeVeic);
        veicDAO = VeicDAO.getInstance(this);
        abasDAO = AbasDAO.getInstance(this);

        if (i.getFlags() != 2) {
            if(veicDAO.listVeic().size() == 0){
                Tip2 tip2 = new Tip2();
                tip2.show(getFragmentManager(), "tip2");
            }
            Toast.makeText(this, R.string.putVecInfo, Toast.LENGTH_SHORT).show();
        }

        if (i.getFlags() == 2) {

            veiculo = new Veiculo();
            veiculo = veicDAO.listVeicComb(d.getString("veicAlt"));
            spComb.setSelection(adapter.getPosition(veiculo.getComb()));
            spCat.setSelection(adapter2.getPosition(veiculo.getCat()));
            id = veiculo.getId();
            txtNomeVec.setText(d.getString("veicAlt"));
            antigoNome = txtNomeVec.getText().toString();
        }


    }

    public void saveVec(View view) {
        String nome = txtNomeVec.getText().toString();
        String comb = spComb.getSelectedItem().toString();
        String cat = spCat.getSelectedItem().toString();
        TextView erroComb = (TextView) findViewById(R.id.lblErroComb);
        TextView erroCat = (TextView) findViewById(R.id.lblErroCat);

        if (nome.equals("")) {
            txtNomeVec.setError("Digite um nome");
        } else {
            txtNomeVec.setError(null);
        }
        if (comb.equals("Selecione")) {
            erroComb.setError("Escolha um combustível");
        } else {
            erroComb.setError(null);
        }
        if (cat.equals("Selecione")) {
            erroCat.setError("Escolha uma categoria");
        } else {
            erroCat.setError(null);
        }
        if (txtNomeVec.getError() == null && erroCat.getError() == null && erroComb.getError() == null) {
            if (i.getFlags() != 2) {
                veiculo = new Veiculo(nome, cat, comb);
                veicDAO.save(veiculo);
                Toast.makeText(this, R.string.includeSucess, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("nome", nome);

                setResult(RESULT_OK, intent);
            } else {
                veiculo = new Veiculo(id, nome, cat, comb);
                veicDAO.update(veiculo);
                abasDAO.updateVec(antigoNome, nome);
                Toast.makeText(this, R.string.updateSucess, Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.putExtra("nome", nome);
                setResult(RESULT_OK, i);
            }
            finish();
        }
    }
}
