package tagliaferro.adriano.projetoposto;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.Posto;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Alt_Posto extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment_Alt_Posto frag = new Fragment_Alt_Posto();
    Spinner sp1, sp2, sp3;
    EditText val1, val2, val3;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3, autoComp;
    int i = 0;
    private String[] c = new String[3];
    private String[] cAnt = new String[3];
    PosDAO posDAO;
    AutoCompleteTextView txtNome;
    FrameLayout frame;
    ImageButton atualizar;
    private int idP;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alt_posto);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frmAltPosto, frag, "AltPosto");
        ft.commit();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void load(){
        Resources res = getResources();
        String[] comb = res.getStringArray(R.array.tiposComb);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList(comb)));
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoComp = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, posDAO.listPosAll());
        sp1.setAdapter(arrayAdapter);
        sp2.setAdapter(arrayAdapter2);
        sp3.setAdapter(arrayAdapter3);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        txtNome = (AutoCompleteTextView) findViewById(R.id.txtAltPosto);
        frame = (FrameLayout) findViewById(R.id.frmAltPosto);
        sp1 = (Spinner) findViewById(R.id.spComb1);
        sp2 = (Spinner) findViewById(R.id.spComb2);
        sp3 = (Spinner) findViewById(R.id.spComb3);
        val1 = (EditText) findViewById(R.id.txtComb1);
        val2 = (EditText) findViewById(R.id.txtComb2);
        val3 = (EditText) findViewById(R.id.txtComb3);
        posDAO = PosDAO.getInstance(this);
        atualizar = (ImageButton) findViewById(R.id.btnSavePosto);
        atualizar.setOnClickListener(this);
        load();
        cAnt[0] = "Selecione";
        cAnt[1] = "Selecione";
        cAnt[2] = "Selecione";

        txtNome.setThreshold(1);
        txtNome.setAdapter(autoComp);
        frame.setVisibility(View.INVISIBLE);


        txtNome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                frame.setVisibility(View.INVISIBLE);
                load();
                Posto posto = new Posto();
                posto = posDAO.listPosNome(parent.getItemAtPosition(position).toString());
                sp1.setSelection(findPosition(posto.getComb1(), arrayAdapter));
                val1.setText(String.valueOf(posto.getVal1()));
                sp2.setSelection(findPosition(posto.getComb2(), arrayAdapter2));
                val2.setText(String.valueOf(posto.getVal2()));
                sp3.setSelection(findPosition(posto.getComb3(), arrayAdapter3));
                val3.setText(String.valueOf(posto.getVal3()));

                idP = posto.getId();
                frame.setVisibility(View.VISIBLE);
                c[0] = posto.getComb1();
                c[1] = posto.getComb2();
                c[2] = posto.getComb3();
            }
        });

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
            if (array.getItem(i).equals(com)) {
                return i;
            }
        }

        return 0;
    }

    public int findPositionPos(String com, ArrayAdapter<String> array) {
        //Toast.makeText(this, "Valor do Array: " + array.getCount(), Toast.LENGTH_SHORT).show();

        for (int i = 0; i < array.getCount(); i++) {
            if (array.getItem(i).equals(com)) {
                return i;
            }
        }

        return -1;
    }

    public void updatePosto() {
        String comb1, comb2, comb3, posto;
        Double valor1, valor2, valor3;
        String v1, v2, v3;

        posto = txtNome.getText().toString();

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
                ;
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
            txtNome.setError("Informe o nome");
        } else {
            txtNome.setError(null);
        }

        try {
            if (val1.getError() == null && val2.getError() == null && val3.getError() == null && txtNome.getError() == null) {
                Posto p = new Posto(idP, txtNome.getText().toString(), comb1, comb2, comb3, valor1, valor2, valor3);
                posDAO.update(p);
                Toast.makeText(this, R.string.updateSucess, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        updatePosto();
    }
}
