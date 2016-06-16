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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import tagliaferro.adriano.projetoposto.Adapter.PosAdapter;
import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.Posto;
import tagliaferro.adriano.projetoposto.Data.Veiculo;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Fragment_ExcPosto extends ListFragment implements DialogInterface.OnClickListener{

    private PosAdapter adapter;
    private PosDAO posDAO;
    private int idExc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        posDAO = PosDAO.getInstance(getActivity());
        adapter = new PosAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateListPos();
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

    protected void updateListPos() {
        List<Posto> produtos = posDAO.listPos();
        if(produtos != null) {
            adapter.setItems(produtos);
        }
        else {
            Toast.makeText(getActivity(), "Nenhum veículo encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            if(idExc != -1) {
                posDAO.delete(idExc);
                Toast.makeText(getActivity(), R.string.excSucess, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Abast.class);
                startActivity(intent);
            }
        }
    }
}
