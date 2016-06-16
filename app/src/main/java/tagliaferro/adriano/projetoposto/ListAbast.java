package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import tagliaferro.adriano.projetoposto.Data.VeicDAO;

/**
 * Created by Adriano on 08/02/2016.
 */
public class ListAbast extends ListFragment implements AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {

    private AbasAdapter adapter;
    private AbasDAO abasDAO;
    private VeicDAO veicDAO;
    private static final int REQ_COD = 101;
    private static final int RES_COD = 901;
    private int idExc = -1;
    int mesActual;

    private String nomeVec;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        abasDAO = AbasDAO.getInstance(getActivity());
        veicDAO = VeicDAO.getInstance(getActivity());

        getExtra();
    }


    public void getExtra() {
        Bundle extra = getArguments();
        nomeVec = extra.getString("nomeVeiculo");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getExtra();
        getMes();
        getListView().setOnItemLongClickListener(this);
        adapter = new AbasAdapter(getActivity(), nomeVec);
        setListAdapter(adapter);

        updateListBf(nomeVec, mesActual);

    }

    /*protected void updateList(String nomeVeic) {
        List<Abastecimento> produtos = abasDAO.listAbas(nomeVeic);
        List<Abastecimento> ordenada = ordAbast(produtos);
        adapter.setItems(ordenada);
        if(nomeVec.equals("")){
            nomeVec = nomeVeic;
        }
    }*/

    protected void updateListBf(String nomeVeic, int month) {
        List<Abastecimento> produtos = abasDAO.listAbasBf(nomeVeic, month);
        List<Abastecimento> ordenada = ordAbast(produtos);
        adapter.setItems(ordenada);
        if (nomeVec.equals("")) {
            nomeVec = nomeVeic;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Abastecimento ab = (Abastecimento) adapter.getItem(position);
        //Toast.makeText(getActivity(), String.valueOf(ab.getId()), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Inc_Alt_Abast.class);
        intent.setFlags(2);
        Bundle bundle = new Bundle();
        bundle.putInt(AbastecimentoContract.Columns._IDABS, ab.getId());
        bundle.putString(AbastecimentoContract.Columns.POSTOABS, ab.getPosto());
        bundle.putString(AbastecimentoContract.Columns.DATAABS, ab.getData());
        bundle.putDouble(AbastecimentoContract.Columns.KMABS, ab.getKm());
        bundle.putDouble(AbastecimentoContract.Columns.VALORABS, ab.getVal());
        bundle.putString(AbastecimentoContract.Columns.VEICULO, ab.getVeiculo());
        bundle.putDouble(AbastecimentoContract.Columns.PRECOL, ab.getPrecoL());
        intent.putExtras(bundle);
        startActivityForResult(intent, REQ_COD);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_COD && resultCode == RES_COD) {
            getMes();
            updateListBf(nomeVec, mesActual);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        idExc = (int) id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setMessage(R.string.askDelete)
                .setPositiveButton("Sim", this)
                .setNegativeButton("Não", this);
        builder.show();
        return true;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (idExc != -1) {
                String list = abasDAO.findVeiculoById(idExc);
                abasDAO.delete(idExc);
                Toast.makeText(getActivity(), R.string.excSucess, Toast.LENGTH_SHORT).show();
                getMes();
                updateListBf(list, mesActual);
                this.nomeVec = list;

            }

        }
    }

    public List<Abastecimento> ordAbast(List<Abastecimento> abas) {
        for (int i = 0; i < abas.size(); i++) {
            for (int j = 0; j < abas.size() - 1; j++) {
                String actual = abas.get(j).getData();
                String next = abas.get(j + 1).getData();
                String[] ac = actual.split("/");
                String[] ne = next.split("/");
                //Order by Year
                if (ac[2].equals(ne[2])) {
                    //Order by Month
                    if (ac[1].equals(ne[1])) {
                        //Order by Day
                        if (Integer.valueOf(ac[0]) > Integer.valueOf(ne[0])) {
                            Abastecimento aux = abas.get(j);
                            abas.set(j, abas.get(j + 1));
                            abas.set(j + 1, aux);
                        }
                        //Put in correct order by month
                    } else {
                        for (int k = 0; k < abas.size() - 1; k++) {
                            actual = abas.get(k).getData();
                            next = abas.get(k + 1).getData();
                            ac = actual.split("/");
                            ne = next.split("/");
                            if (ac[2].equals(ne[2])) {
                                if (Integer.valueOf(ac[1]) > Integer.valueOf(ne[1])) {
                                    Abastecimento aux = abas.get(k);
                                    abas.set(k, abas.get(k + 1));
                                    abas.set(k + 1, aux);
                                }
                            }
                        }
                    }
                    //Put in correct order by year
                } else {
                    for (int l = 0; l < abas.size() - 1; l++) {
                        actual = abas.get(l).getData();
                        next = abas.get(l + 1).getData();
                        ac = actual.split("/");
                        ne = next.split("/");
                        if (Integer.valueOf(ac[2]) > Integer.valueOf(ne[2])) {
                            Abastecimento aux = abas.get(l);
                            abas.set(l, abas.get(l + 1));
                            abas.set(l + 1, aux);
                        }

                    }
                }
            }

        }

        return abas;
    }

    public void getMes() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String dateString = sdf.format(data_atual);
        String[] hoje = dateString.split("/");
        mesActual = Integer.valueOf(hoje[1]);
    }

    public String getNomeVec() {
        return nomeVec;
    }

    public void setNomeVec(String nomeVec) {
        this.nomeVec = nomeVec;
    }

    public AbasAdapter getAdapter(){
        return this.adapter;
    }
}
