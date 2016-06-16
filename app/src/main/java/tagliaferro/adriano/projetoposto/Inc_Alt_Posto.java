package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.Posto;
import tagliaferro.adriano.projetoposto.Tips.Tip1;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Inc_Alt_Posto extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment_Inc_Alt_Posto frag = new Fragment_Inc_Alt_Posto();
    Spinner sp1, sp2, sp3;
    EditText val1, val2, val3, nomePosto;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;
    int i = 0;
    private String[] c = new String[3];
    private String[] cAnt = new String[3];
    PosDAO posDAO;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inc_alt_posto);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frmIncAltPosto, frag, "IncAltPosto");
        ft.commit();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toast.makeText(this, "Informe os dados do posto", Toast.LENGTH_SHORT).show();

        sp1 = (Spinner) findViewById(R.id.spComb1);
        sp2 = (Spinner) findViewById(R.id.spComb2);
        sp3 = (Spinner) findViewById(R.id.spComb3);
        val1 = (EditText) findViewById(R.id.txtComb1);
        val2 = (EditText) findViewById(R.id.txtComb2);
        val3 = (EditText) findViewById(R.id.txtComb3);
        nomePosto = (EditText) findViewById(R.id.txtNomePosto);
        posDAO = PosDAO.getInstance(this);

        if(posDAO.listPos().size() == 0){
            Tip1 tip1 = new Tip1();
            tip1.show(getFragmentManager(), "tip1");
        }

        Resources res = getResources();
        String[] comb = res.getStringArray(R.array.tiposComb);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);
        sp2.setAdapter(arrayAdapter2);
        sp3.setAdapter(arrayAdapter3);
        cAnt[0] = "Selecione";
        cAnt[1] = "Selecione";
        cAnt[2] = "Selecione";

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c[0] = parent.getSelectedItem().toString();
                if (id != 0) {

                    arrayAdapter2.remove(c[0]);
                    if (cAnt[0] != c[0]) {
                        if (cAnt[0] != "Selecione") {
                            arrayAdapter2.add(cAnt[0]);
                        } else {
                            cAnt[0] = c[0];
                        }
                    }
                    arrayAdapter2.notifyDataSetChanged();
                    sp2.setSelection(findPosition(c[1], arrayAdapter2));

                    arrayAdapter3.remove(c[0]);
                    if (cAnt[0] != c[0]) {
                        if (cAnt[0] != "Selecione") {
                            arrayAdapter3.add(cAnt[0]);
                            cAnt[0] = c[0];
                        } else {
                            cAnt[0] = c[0];
                        }
                    }
                    arrayAdapter3.notifyDataSetChanged();
                    sp3.setSelection(findPosition(c[2], arrayAdapter3));
                } else {
                    if ((c[0].equals("Selecione")) && (cAnt[0] != "Selecione")) {
                        arrayAdapter2.add(cAnt[0]);
                        arrayAdapter2.notifyDataSetChanged();
                        sp2.setSelection(findPosition(c[1], arrayAdapter2));
                        arrayAdapter3.add(cAnt[0]);
                        arrayAdapter3.notifyDataSetChanged();
                        sp3.setSelection(findPosition(c[2], arrayAdapter3));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c[1] = parent.getSelectedItem().toString();
                if (id != 0) {

                    arrayAdapter.remove(c[1]);
                    if (cAnt[1] != c[1]) {
                        if (cAnt[1] != "Selecione") {
                            arrayAdapter.add(cAnt[1]);

                        } else {
                            cAnt[1] = c[1];
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    sp1.setSelection(findPosition(c[0], arrayAdapter));

                    arrayAdapter3.remove(c[1]);
                    if (cAnt[1] != c[1]) {
                        if (cAnt[1] != "Selecione") {
                            arrayAdapter3.add(cAnt[1]);
                            cAnt[1] = c[1];
                        } else {
                            cAnt[1] = c[1];
                        }
                    }
                    arrayAdapter3.notifyDataSetChanged();
                    sp3.setSelection(findPosition(c[2], arrayAdapter3));
                } else {
                    if ((c[1].equals("Selecione")) && (cAnt[1] != "Selecione")) {
                        arrayAdapter.add(cAnt[1]);
                        arrayAdapter.notifyDataSetChanged();
                        sp1.setSelection(findPosition(c[0], arrayAdapter));
                        arrayAdapter3.add(cAnt[1]);
                        arrayAdapter3.notifyDataSetChanged();
                        sp3.setSelection(findPosition(c[2], arrayAdapter3));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c[2] = parent.getSelectedItem().toString();
                if (id != 0) {

                    arrayAdapter.remove(c[2]);
                    if (cAnt[2] != c[2]) {
                        if (cAnt[2] != "Selecione") {
                            arrayAdapter.add(cAnt[2]);

                        } else {
                            cAnt[2] = c[2];
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    sp1.setSelection(findPosition(c[0], arrayAdapter));

                    arrayAdapter2.remove(c[2]);
                    if (cAnt[2] != c[2]) {
                        if (cAnt[2] != "Selecione") {
                            arrayAdapter2.add(cAnt[2]);
                            cAnt[2] = c[2];
                        } else {
                            cAnt[2] = c[2];
                        }
                    }
                    arrayAdapter2.notifyDataSetChanged();
                    sp2.setSelection(findPosition(c[1], arrayAdapter2));
                } else {
                    if ((c[2].equals("Selecione")) && (cAnt[2] != "Selecione")) {
                        arrayAdapter2.add(cAnt[2]);
                        arrayAdapter2.notifyDataSetChanged();
                        sp2.setSelection(findPosition(c[1], arrayAdapter2));
                        arrayAdapter.add(cAnt[2]);
                        arrayAdapter.notifyDataSetChanged();
                        sp1.setSelection(findPosition(c[0], arrayAdapter));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public int findPosition(String com, ArrayAdapter<String> array) {
        //Toast.makeText(this, "Valor do Array: " + array.getCount(), Toast.LENGTH_SHORT).show();

        for (int i = 0; i < array.getCount(); i++) {
            if (array.getItem(i) == com) {
                return i;
            }
        }

        return 0;
    }

    public void savePosto(View view) {
        String comb1, comb2, comb3, posto;
        Double valor1, valor2, valor3;
        String v1, v2, v3;

        posto = nomePosto.getText().toString();

        comb1 = sp1.getSelectedItem().toString();
        comb2 = sp2.getSelectedItem().toString();
        comb3 = sp3.getSelectedItem().toString();

        v1 = val1.getText().toString();
        v2 = val2.getText().toString();
        v3 = val3.getText().toString();

        if (v1.equals("")) {
            valor1 = 0.0;
        } else {
            valor1 = Double.parseDouble(v1);
        }
        if (v2.equals("")) {
            valor2 = 0.0;
        } else {
            valor2 = Double.parseDouble(v2);
        }
        if (v3.equals("")) {
            valor3 = 0.0;
        } else {
            valor3 = Double.parseDouble(v3);
        }


        if (!comb1.equals("Selecione")) {
            if (valor1 == 0) {
                val1.setError("Informe o valor");
            } else {
                val1.setError(null);
            }
        } else {
            val1.setError(null);
        }
        if (!comb2.equals("Selecione")) {
            if (valor2 == 0) {
                val2.setError("Informe o valor");
            } else {
                val2.setError(null);
            }
        } else {
            val2.setError(null);
        }
        if (!comb3.equals("Selecione")) {
            if (valor3 == 0) {
                val3.setError("Informe o valor");
            } else {
                val3.setError(null);
            }
        } else {
            val3.setError(null);
        }
        if (posto.equals("")) {
            nomePosto.setError("Informe o nome");
        } else {
            nomePosto.setError(null);
        }

        try {
            if ((!comb1.equals("Selecione")) || (!comb2.equals("Selecione")) || (!comb3.equals("Selecione"))) {
                if (val1.getError() == null && val2.getError() == null && val3.getError() == null && nomePosto.getError() == null) {
                    Posto p = new Posto(nomePosto.getText().toString(), comb1, comb2, comb3, valor1, valor2, valor3);
                    posDAO.save(p);
                    Toast.makeText(this, R.string.includeSucess, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                Toast.makeText(this, "Selecione ao menos um combustível", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
