package tagliaferro.adriano.projetoposto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tagliaferro.adriano.projetoposto.Data.Abastecimento;
import tagliaferro.adriano.projetoposto.Data.Veiculo;
import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano on 08/02/2016.
 */
public class VecAdapter extends BaseAdapter {

    private Context context;
    private List<Veiculo> veics = new ArrayList<>();
    LayoutInflater inflater;

    public VecAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return veics.size();
    }

    @Override
    public Object getItem(int position) {
        return veics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return veics.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_exc_veiculo, parent, false);
            holder = new ViewHolder();
            holder.lblExcVecNome = (TextView) view.findViewById(R.id.lblExcVecNome);
            holder.lblExcVecComb = (TextView) view.findViewById(R.id.lblExcVecComb);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Veiculo vc = veics.get(position);

        holder.lblExcVecNome.setText(vc.getNome());
        holder.lblExcVecComb.setText(vc.getComb());

        return view;
    }

    public void setItems(List<Veiculo> veiculos) {
        this.veics = veiculos;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView lblExcVecNome;
        public TextView lblExcVecComb;

    }
}
