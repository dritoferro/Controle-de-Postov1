package tagliaferro.adriano.projetoposto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tagliaferro.adriano.projetoposto.Data.Posto;
import tagliaferro.adriano.projetoposto.Data.Veiculo;
import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano on 08/02/2016.
 */
public class PosAdapter extends BaseAdapter {

    private Context context;
    private List<Posto> postos = new ArrayList<>();
    LayoutInflater inflater;

    public PosAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postos.size();
    }

    @Override
    public Object getItem(int position) {
        return postos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return postos.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_exc_posto, parent, false);
            holder = new ViewHolder();
            holder.lblExcPosto = (TextView) view.findViewById(R.id.lblExcPosto);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Posto p = postos.get(position);

        holder.lblExcPosto.setText(p.getNome());

        return view;
    }

    public void setItems(List<Posto> postos) {
        this.postos = postos;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView lblExcPosto;
    }
}
