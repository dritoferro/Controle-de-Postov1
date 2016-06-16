package tagliaferro.adriano.projetoposto;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.CalendarView;

import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import java.util.List;


import tagliaferro.adriano.projetoposto.Adapter.AbasAdapter;
import tagliaferro.adriano.projetoposto.Data.AbasDAO;
import tagliaferro.adriano.projetoposto.Data.Abastecimento;
import tagliaferro.adriano.projetoposto.Data.AbastecimentoContract;
import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;
import tagliaferro.adriano.projetoposto.Tips.Tip3;

/**
 * Created by Adriano on 01/02/2016.
 */
public class Abast extends AppCompatActivity {
    CalendarView calendarView;
    FragmentManager fm;
    FragmentTransaction ft;
    ListAbast fdown;
    Fragment_Up_Abast fup = new Fragment_Up_Abast();
    int m;
    int dia = 0;
    static final int REQ_COD = 100;
    static final int REQ_COD_SETVEC = 110;
    String[] mes;
    private TextView txtVeic;
    AbasDAO abasDAO;
    VeicDAO veicDAO;
    PosDAO posDAO;
    int mesActual;
    AbasAdapter abasAdapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        calendarView = (CalendarView) findViewById(R.id.calAbast);


        try {
            calendarView.setMinDate(getPackageManager().getPackageInfo("tagliaferro.adriano.projetoposto", 0).firstInstallTime);
            calendarView.setMaxDate(maxData());
            calendarView.setDate(dataInit());

        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                                 @Override
                                                 public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                                                     if ((dayOfMonth <= dia) && (m == month)) {

                                                         //Toast.makeText(getBaseContext(), "Dia: " + dayOfMonth + " Mês: " + (month + 1) + " Ano: " + year, Toast.LENGTH_SHORT).show();

                                                         Intent intent = new Intent(getBaseContext(), Inc_Alt_Abast.class);
                                                         Bundle novaAbast = new Bundle();
                                                         novaAbast.putInt("ano", year);
                                                         novaAbast.putInt("mes", month + 1);
                                                         novaAbast.putInt("dia", dayOfMonth);
                                                         novaAbast.putString(AbastecimentoContract.Columns.VEICULO, txtVeic.getText().toString());
                                                         intent.setFlags(1);
                                                         intent.putExtras(novaAbast);
                                                         startActivityForResult(intent, REQ_COD);


                                                     } else {
                                                         //chama método para consulta dos abastecimentos com base no mês
                                                         fdown.updateListBf(txtVeic.getText().toString(), (month + 1));
                                                         //Toast.makeText(getBaseContext(), "Abastecimentos do mês " + (month + 1), Toast.LENGTH_SHORT).show();
                                                         calcBillsMonth(month + 1);

                                                     }

                                                 }
                                             }
        );
        if(abasDAO.listAbas(txtVeic.getText().toString()).size() == 0){
            Tip3 tip3 = new Tip3();
            tip3.show(getFragmentManager(), "tip3");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            abasAdapter = fdown.getAdapter();
            if (requestCode == REQ_COD && resultCode == RESULT_OK) {

                if (veicDAO.listVeic().size() != 0 && data != null) {
                    String vec = data.getStringExtra("nome");
                    txtVeic.setText(vec);
                    abasAdapter.setNome(vec);
                    fdown.updateListBf(vec, mesActual);
                }
                if (veicDAO.listVeic().size() == 0) {
                    Intent intent = new Intent(this, IncAltVeiculo.class);
                    startActivityForResult(intent, REQ_COD);
                } else {
                    abasAdapter.setNome(txtVeic.getText().toString());
                    fdown.updateListBf(txtVeic.getText().toString(), mesActual);
                }
            }
            if (requestCode == REQ_COD_SETVEC && resultCode == RESULT_OK) {
                String vec = data.getStringExtra("nome");
                txtVeic.setText(vec);
                abasAdapter.setNome(vec);
                fdown.updateListBf(vec, mesActual);
            }
            calcBillsMonth(mesActual);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.abast);
        veicDAO = VeicDAO.getInstance(this);
        abasDAO = AbasDAO.getInstance(this);
        posDAO = PosDAO.getInstance(this);
        Intent i = getIntent();
        txtVeic = (TextView) findViewById(R.id.lblVeiculo);
        txtVeic.setText(i.getStringExtra("nome"));


        if(txtVeic.getText().toString().equals("")) {
            if (posDAO.listPos().size() > 0) {
                List<Veiculo> v;
                v = veicDAO.listVeicNome();
                if (v.size() != 0) {
                    Intent intent = new Intent(this, Choose_Veiculo.class);
                    startActivityForResult(intent, REQ_COD);

                } else {
                    Intent intent = new Intent(this, IncAltVeiculo.class);
                    startActivityForResult(intent, REQ_COD);
                }

            } else {
                Intent intent = new Intent(this, Inc_Alt_Posto.class);
                startActivityForResult(intent, REQ_COD);
            }
        }

        fdown = new ListAbast();
        Bundle bundle = new Bundle();
        bundle.putString("nomeVeiculo", txtVeic.getText().toString());
        fdown.setArguments(bundle);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frmAbastDown, fdown, "FragDownAbast");
        ft.add(R.id.frmAbastUp, fup, "FragUpAbast");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actInsVec) {
            Intent intent = new Intent(this, IncAltVeiculo.class);
            startActivityForResult(intent, REQ_COD);
        }
        if (item.getItemId() == R.id.actInsPosto) {
            Intent intent = new Intent(this, Inc_Alt_Posto.class);
            startActivityForResult(intent, REQ_COD);
        }
        if (item.getItemId() == R.id.actAltPosto) {
            Intent intent = new Intent(this, Alt_Posto.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.actAltVec) {
            Bundle d = new Bundle();
            d.putString("veicAlt", txtVeic.getText().toString());
            Intent intent = new Intent(this, IncAltVeiculo.class);
            intent.putExtras(d);
            intent.setFlags(2);
            startActivityForResult(intent, REQ_COD_SETVEC);
        }
        if (item.getItemId() == R.id.actChooseVec) {
            Intent intent = new Intent(this, Choose_Veiculo.class);
            startActivityForResult(intent, REQ_COD_SETVEC);
        }
        if (item.getItemId() == R.id.actDelVec) {
            Intent intent = new Intent(this, ExcVeiculo.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.actExcPosto) {
            Intent intent = new Intent(this, ExcPosto.class);
            startActivity(intent);
        }

        return true;
    }

    public long maxData() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String dateString = sdf.format(data_atual);
        mes = dateString.split("/");
        //Toast.makeText(this, "Mês Atual: " + mes[1], Toast.LENGTH_SHORT).show();
        m = Integer.valueOf(mes[1]) - 1;
        cal.set(Calendar.MONTH, m);
        int diahoje = Integer.valueOf(mes[0]);
        mes[0] = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //Toast.makeText(this, "Último dia do mês: " + mes[0], Toast.LENGTH_SHORT).show();
        if (diahoje == Integer.valueOf(mes[0])) {
            cal.set(Integer.valueOf(mes[2]), m + 1, 1, 23, 59);
        } else {
            cal.set(Integer.valueOf(mes[2]), m, Integer.valueOf(mes[0]), 23, 59);
        }
        return cal.getTimeInMillis();

    }

    public long dataInit() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String dateString = sdf.format(data_atual);
        String[] hoje = dateString.split("/");
        mesActual = Integer.valueOf(hoje[1]);
        //Toast.makeText(this, "Mês Atual: " + mes[1], Toast.LENGTH_SHORT).show();
        int n = Integer.valueOf(hoje[1]) - 1;
        cal.set(Calendar.MONTH, n);
        int maximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (Integer.valueOf(hoje[0]) == maximum) {
            maximum = 1;
            cal.set(Integer.valueOf(hoje[2]), n + 1, maximum, 00, 00);
            //Toast.makeText(this, "Dia para hoje: " + String.valueOf(cal.getTime()), Toast.LENGTH_LONG).show();
        } else {
            maximum = Integer.valueOf(hoje[0]) + 1;
            cal.set(Integer.valueOf(hoje[2]), n, maximum, 00, 00);
        }
        dia = Integer.valueOf(hoje[0]);
        //Toast.makeText(this, "Último dia do mês: " + mes[0], Toast.LENGTH_SHORT).show();

        return cal.getTimeInMillis();
    }

    public void calcBillsMonth(int m) {
        List<Abastecimento> list1 = abasDAO.listAbasBf(txtVeic.getText().toString(), m);
        List<Abastecimento> list2 = fdown.ordAbast(list1);
        if (list2.size() != 0) {
            Double valor = 0.0, kmRodado = 0.0;
            for (int i = 0; i < list2.size(); i++) {
                valor += list2.get(i).getVal();
                if ((i + 1) < list2.size()) {
                    kmRodado += ((list2.get(i + 1).getKm()) - (list2.get(i).getKm()));
                } else {
                    List<Abastecimento> listNextM = abasDAO.listAbasBf(txtVeic.getText().toString(), (m + 1));
                    if (!listNextM.isEmpty()) {
                        List<Abastecimento> listNextMOrd = fdown.ordAbast(listNextM);
                        kmRodado += ((listNextMOrd.get(0).getKm()) - (list2.get(list2.size() - 1).getKm()));
                    }
                }
            }
            Toast.makeText(this, "Km Mês: " + String.valueOf(kmRodado) + " Mês R$: " + String.valueOf(valor), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Nao existem abastecimentos para o mês", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getTxtVeic() {
        return txtVeic;
    }

    public void setTxtVeic(TextView txtVeic) {
        this.txtVeic = txtVeic;
    }
}
