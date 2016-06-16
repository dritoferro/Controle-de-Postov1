package tagliaferro.adriano.projetoposto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import tagliaferro.adriano.projetoposto.Adapter.VecAdapter;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Fragment_ExcVeiculo extends ListFragment implements DialogInterface.OnClickListener{

    private VecAdapter vecAdapter;
    private VeicDAO veicDAO;
    private int idExc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vecAdapter = new VecAdapter(getActivity());
        setListAdapter(vecAdapter);
        veicDAO = VeicDAO.getInstance(getActivity());

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateListVec();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        idExc = (int) id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setMessage(R.string.askDelete)
                .setPositiveButton("Sim", this)
                .setNegativeButton("Não", this);
        builder.show();

    }

    protected void updateListVec() {
        List<Veiculo> produtos = veicDAO.listVeic();
        if(produtos != null) {
            vecAdapter.setItems(produtos);
        }
        else {
            Toast.makeText(getActivity(), "Nenhum veículo encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            if(idExc != -1) {
                veicDAO.delete(idExc, getActivity());
                Toast.makeText(getActivity(), R.string.excSucess, Toast.LENGTH_SHORT).show();
                updateListVec();
                Intent intent = new Intent(getActivity(), Abast.class);
                startActivity(intent);
            }
        }
    }


}

