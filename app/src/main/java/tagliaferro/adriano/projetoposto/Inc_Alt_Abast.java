package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tagliaferro.adriano.projetoposto.Data.AbasDAO;
import tagliaferro.adriano.projetoposto.Data.Abastecimento;
import tagliaferro.adriano.projetoposto.Data.AbastecimentoContract;
import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.Posto;
import tagliaferro.adriano.projetoposto.Data.PostoContract;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Inc_Alt_Abast extends AppCompatActivity implements DialogInterface.OnClickListener {

    private EditText dataAbast;
    private EditText valorAbast;
    private EditText kmAbast;
    private EditText precoL;
    private Spinner postoAbast;
    private String vec, posto, combustivel;
    private int id, positionP = -1, posComb;
    Intent i;
    PosDAO posDAO;
    VeicDAO veicDAO;
    AbasDAO abasDAO;
    ListAbast listAbast;
    Alt_Posto altPosto;
    ArrayAdapter<String> adapter;
    TextView lblExcPosto, lblExcPreco;
    private boolean deleted = false;
    private Double kmInit = -1.0, kmFinal = -1.0, valorL = 0.0, precoAntigo = 0.0;
    private String postoSp;
    ActionBar bar;
    Posto novoPosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inc_alt_abast);

        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        precoL = (EditText) findViewById(R.id.txtPreco);
        dataAbast = (EditText) findViewById(R.id.txtData);
        valorAbast = (EditText) findViewById(R.id.txtValor);
        kmAbast = (EditText) findViewById(R.id.txtKm);
        postoAbast = (Spinner) findViewById(R.id.spPosto);
        lblExcPosto = (TextView) findViewById(R.id.lblPostoExcluded);
        lblExcPreco = (TextView) findViewById(R.id.lblPrecoExcluded);
        posDAO = PosDAO.getInstance(this);
        veicDAO = VeicDAO.getInstance(this);
        abasDAO = AbasDAO.getInstance(this);
        altPosto = new Alt_Posto();
        listAbast = new ListAbast();

        dataAbast.setEnabled(false);
        i = getIntent();
        Bundle d;
        d = i.getExtras();
        vec = d.getString(AbastecimentoContract.Columns.VEICULO);
        loadAdapter();
        //criar um metodo para verificar em qual faixa de data se encaixa a data de abastecimento do momento
        // para descobrir a qual faixa de km deve pertencer a abastecida correspondente
        postoAbast.setAdapter(adapter);
        if (i.getFlags() == 1) {
            if (adapter.getCount() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.askAltInc)
                        .setPositiveButton("Cadastrar", this)
                        .setNegativeButton("Alterar", this);
                builder.show();
            }

            String data = d.getInt("dia") + "/" + d.getInt("mes") + "/" + d.getInt("ano");

            dataAbast.setText(data);

        }
        if (i.getFlags() == 2) {
            dataAbast.setText(d.getString(AbastecimentoContract.Columns.DATAABS));
            valorAbast.setText(String.valueOf(d.getDouble(AbastecimentoContract.Columns.VALORABS)));
            kmAbast.setText(String.valueOf(d.getDouble(AbastecimentoContract.Columns.KMABS)));
            id = d.getInt(AbastecimentoContract.Columns._IDABS);
            positionP = altPosto.findPositionPos(d.getString(AbastecimentoContract.Columns.POSTOABS), adapter);
            if (positionP != -1) {
                postoAbast.setSelection(positionP);
                deleted = false;
                postoSp = postoAbast.getSelectedItem().toString();
                valorL = precoAnt();
                precoL.setText(String.valueOf(valorL));
            } else {
                postoAbast.setVisibility(View.INVISIBLE);
                lblExcPosto.setText(d.getString(AbastecimentoContract.Columns.POSTOABS));
                lblExcPosto.setVisibility(View.VISIBLE);
                precoL.setVisibility(View.INVISIBLE);
                lblExcPreco.setText(String.valueOf(d.getDouble(AbastecimentoContract.Columns.PRECOL)));
                lblExcPreco.setVisibility(View.VISIBLE);
                postoSp = d.getString(AbastecimentoContract.Columns.POSTOABS);

                deleted = true;
            }
        }
        faixaKm();
    }

    public void saveAbast(View view) {
        Double val, km;
        String data = dataAbast.getText().toString();
        if (!deleted) {
            posto = postoAbast.getSelectedItem().toString();
        } else {
            posto = lblExcPosto.getText().toString();
        }
        if (valorAbast.getText().toString().equals("")) {
            val = 0.0;
        } else {
            val = Double.parseDouble(valorAbast.getText().toString());
        }
        if (kmAbast.getText().toString().equals("")) {
            km = 0.0;
        } else {
            km = Double.parseDouble(kmAbast.getText().toString());
        }
        Abastecimento a = new Abastecimento();
        if (i.getFlags() == 2 && !deleted) {

            try {
                Double vl = Double.parseDouble(precoL.getText().toString());
                a = new Abastecimento(data, val, posto, km, vec, vl);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                Double vl = Double.parseDouble(precoL.getText().toString());
                if (!(valorL.equals(vl))) {

                    if (posComb == 1) {
                        novoPosto.setComb1(combustivel);
                        novoPosto.setVal1(vl);
                    } else if (posComb == 2) {
                        novoPosto.setComb2(combustivel);
                        novoPosto.setVal2(vl);
                    } else if (posComb == 3) {
                        novoPosto.setComb3(combustivel);
                        novoPosto.setVal3(vl);
                    }
                    posDAO.update(novoPosto);
                    a = new Abastecimento(data, val, posto, km, vec, vl);
                } else {
                    a = new Abastecimento(data, val, posto, km, vec, valorL);
                }

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }


        if (val == 0.0) {
            valorAbast.setError("Digite o valor");
        } else {
            valorAbast.setError(null);
        }
        if (km != 0.0) {
            if (kmFinal != 0.0 || kmInit != 0.0) {
                int p = 0;
                if (kmFinal != 0) {
                    if (km > kmFinal) {
                        kmAbast.setError("Km deve ser menor que: " + kmFinal);
                        p++;
                    }
                }
                if (kmInit != 0) {
                    if (km < kmInit) {
                        kmAbast.setError("Km deve ser maior que: " + kmInit);
                        p++;
                    }
                }
                if (p == 0) {
                    kmAbast.setError(null);
                }
            } else {
                kmAbast.setError(null);
            }
        } else {
            kmAbast.setError("Digite o valor");
        }
        if (valorAbast.getError() == null && kmAbast.getError() == null) {
            try {
                if (i.getFlags() == 1) {
                    abasDAO.save(a, this);
                    Toast.makeText(this, R.string.includeSucess, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

                if (i.getFlags() == 2) {
                    a.setId(id);
                    abasDAO.update(a);
                    Toast.makeText(this, R.string.updateSucess, Toast.LENGTH_SHORT).show();
                    setResult(901);
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadAdapter() {
        combustivel = veicDAO.listVeicComb(vec).getComb();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posDAO.listPosByVec(veicDAO.listVeicComb(vec)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadAdapter();
            postoAbast.setAdapter(adapter);
            if (adapter.getCount() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.askAltInc)
                        .setPositiveButton("Cadastrar", this)
                        .setNegativeButton("Alterar", this);
                builder.show();
            }
        }
    }

    public void faixaKm() {
        try {
            int dia;
            String[] date = dataAbast.getText().toString().split("/");
            List<Abastecimento> list1 = abasDAO.listAbasBf(vec, Integer.valueOf(date[1]));
            List<Abastecimento> list2 = listAbast.ordAbast(list1);
            int today = Integer.valueOf(date[0]);
            if (list2.size() > 0) {
                if (i.getFlags() == 2) {
                    if (list2.size() == 1) {
                        List<Abastecimento> listFull = abasDAO.listAbas(vec);
                        if (listFull.size() != list2.size()) {
                            List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                            kmInit = listFull.get(listFullOrdered.size() - 2).getKm();
                            kmFinal = 0.0;
                        } else {
                            kmInit = 0.0;
                            kmFinal = 0.0;
                        }
                    } else {
                        for (int i = 0; i < list2.size(); i++) {
                            dia = getDiaList(list2.get(i).getData());
                            if (today < dia) {
                                if (i == 1) {
                                    if (today == getDiaList(list2.get(i - 1).getData())) {
                                        kmInit = 0.0;
                                        kmFinal = list2.get(i).getKm();
                                    } else {
                                        kmFinal = list2.get(i).getKm();
                                        kmInit = list2.get(i - 1).getKm();
                                    }
                                } else {
                                    kmFinal = list2.get(i).getKm();
                                    if ((i - 1) >= 0) {
                                        kmInit = list2.get(i - 2).getKm();
                                    } else {
                                        List<Abastecimento> listFull = abasDAO.listAbas(vec);
                                        if (listFull.size() != list2.size()) {
                                            List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                                            kmInit = listFull.get(listFullOrdered.size() - 1).getKm();
                                        } else {
                                            kmInit = 0.0;
                                        }
                                    }
                                }
                                break;
                            }
                            if (today == dia) {
                                if (id < list2.get(i).getId()) {
                                    List<Abastecimento> listFull = abasDAO.listAbas(vec);
                                    if (listFull.size() != list2.size()) {
                                        List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                                        kmInit = listFullOrdered.get(listFullOrdered.size() - 1).getKm();
                                    } else {
                                        kmInit = 0.0;
                                    }
                                    kmFinal = list2.get(i).getKm();
                                }
                                if ((i + 1) == list2.size()) {
                                    kmInit = list2.get(list2.size() - 2).getKm();
                                    kmFinal = 0.0;
                                }
                            }

                        }
                    }
                } else {
                    if (list2.size() != 1) {
                        for (int i = 0; i < list2.size(); i++) {
                            dia = getDiaList(list2.get(i).getData());
                            if (today < dia) {
                                if (i == 1) {
                                    if (today == getDiaList(list2.get(i - 1).getData())) {
                                        kmInit = 0.0;
                                        kmFinal = list2.get(i).getKm();
                                    } else {
                                        kmFinal = list2.get(i).getKm();
                                        kmInit = list2.get(i - 1).getKm();
                                    }
                                } else {
                                    kmFinal = list2.get(i).getKm();
                                    if ((i - 1) >= 0) {
                                        kmInit = list2.get(i - 1).getKm();
                                    } else {
                                        List<Abastecimento> listFull = abasDAO.listAbas(vec);
                                        if (listFull.size() != list2.size()) {
                                            List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                                            if((listFullOrdered.size() - 3) >= 0 ) {
                                                kmInit = listFull.get(listFullOrdered.size() - 3).getKm();
                                            } else {
                                                kmInit = 0.0;
                                            }
                                        } else {
                                            kmInit = 0.0;
                                        }
                                    }
                                }
                                break;
                            }
                            if (today == dia) {
                                kmInit = list2.get(i).getKm();
                                if ((i + 1) < list2.size()) {
                                    kmFinal = list2.get(i + 1).getKm();
                                } else {
                                    kmFinal = 0.0;
                                }
                                /*if (id < list2.get(i).getId()) {
                                    List<Abastecimento> listFull = abasDAO.listAbas(vec);
                                    if (listFull.size() != list2.size()) {
                                        List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                                        kmInit = listFullOrdered.get(listFullOrdered.size() - 1).getKm();
                                    } else {
                                        kmInit = 0.0;
                                    }
                                    kmFinal = list2.get(i).getKm();
                                }
                                if ((i + 1) == list2.size()) {
                                    kmInit = list2.get(list2.size() - 2).getKm();
                                    kmFinal = 0.0;
                                }*/
                            }

                        }
                    }
                }
                if (kmFinal == -1.0) {
                    kmInit = list2.get(list2.size() - 1).getKm();
                    kmFinal = 0.0;
                }
            } else {
                List<Abastecimento> listFull = abasDAO.listAbas(vec);
                List<Abastecimento> listFullOrdered = listAbast.ordAbast(listFull);
                if (listFullOrdered.size() != 0) {
                    kmInit = listFullOrdered.get(listFull.size() - 1).getKm();
                    kmFinal = 0.0;
                } else {
                    kmFinal = 0.0;
                    kmInit = 0.0;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public int getDiaList(String data) {
        String[] prov = data.split("/");
        return Integer.valueOf(prov[0]);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Intent nova = new Intent(this, Inc_Alt_Posto.class);
            startActivityForResult(nova, 1);
        } else {
            Intent nova = new Intent(this, Alt_Posto.class);
            startActivityForResult(nova, 1);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);



        postoAbast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Double preco;
                if (i.getFlags() == 1) {
                    preco = pegaPreco(parent.getSelectedItem().toString());
                    precoL.setText(String.valueOf(preco));
                } else if (i.getFlags() == 2 && (!postoSp.equals(parent.getSelectedItem().toString()))) {
                    preco = pegaPreco(parent.getSelectedItem().toString());
                    precoL.setText(String.valueOf(preco));
                } else {
                    precoL.setText(String.valueOf(precoAntigo));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Double pegaPreco(String postoAt) {
        Double p = 0.0;
        novoPosto = posDAO.listPosNome(postoAt);
        if (novoPosto.getComb1().equals(combustivel)) {
            p = novoPosto.getVal1();
            posComb = 1;
        } else if (novoPosto.getComb2().equals(combustivel)) {
            p = novoPosto.getVal2();
            posComb = 2;
        } else if (novoPosto.getComb3().equals(combustivel)) {
            p = novoPosto.getVal3();
            posComb = 3;
        }
        valorL = p;
        return p;
    }

    public Double precoAnt() {
        Double p = 0.0;
        int idProvi;
        List<Abastecimento> abastList = abasDAO.listAbas(vec);
        for (int i = 0; i < abastList.size(); i++) {
            idProvi = abastList.get(i).getId();
            if(idProvi == id){
                p = abastList.get(i).getPrecoL();
                break;
            }
        }
        precoAntigo = p;
        return p;
    }
}
