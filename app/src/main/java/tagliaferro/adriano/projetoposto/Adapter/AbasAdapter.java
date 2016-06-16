package tagliaferro.adriano.projetoposto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import tagliaferro.adriano.projetoposto.Abast;
import tagliaferro.adriano.projetoposto.Data.AbasDAO;
import tagliaferro.adriano.projetoposto.Data.Abastecimento;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;
import tagliaferro.adriano.projetoposto.ListAbast;
import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano on 08/02/2016.
 */
public class AbasAdapter extends BaseAdapter {

    private static final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private Context context;
    private List<Abastecimento> abs = new ArrayList<>();
    LayoutInflater inflater;
    protected String nome;
    ListAbast listAbast;
    VeicDAO veicDAO;
    Double kmActual, kmNext;

    public AbasAdapter(Context context, String nome) {
        this.context = context;
        this.nome = nome;
        inflater = LayoutInflater.from(context);
        listAbast = new ListAbast();
        veicDAO = VeicDAO.getInstance(context);
    }

    @Override
    public int getCount() {
        return abs.size();
    }

    @Override
    public Object getItem(int position) {
        return abs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return abs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_down_abast, parent, false);
            holder = new ViewHolder();
            holder.lblData = (TextView) view.findViewById(R.id.lblData);
            holder.lblPosto = (TextView) view.findViewById(R.id.lblPosto);
            holder.lblValor = (TextView) view.findViewById(R.id.lblValor);
            holder.lblLitros = (TextView) view.findViewById(R.id.lblLitros);
            holder.lblKm = (TextView) view.findViewById(R.id.lblKm);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Abastecimento abastecimento = abs.get(position);

        kmActual = abastecimento.getKm();
        kmNext = 0.0;
        Double kmFinal = 0.0;
        if ((position + 1) <= (abs.size() - 1)) {
            kmNext = abs.get(position + 1).getKm();
            if (kmActual < kmNext) {
                kmFinal = kmNext - kmActual;
            }
        }
        try {
            if ((position + 1) == abs.size()) {
                AbasDAO abasDAO = AbasDAO.getInstance(context);
                List<Veiculo> v = new ArrayList<>();
                //v = veicDAO.listVeicNome();
                //v.remove(0);
                if (abs.size() > 1) {
                    //nome = abasDAO.topAbas(v);
                    List<Abastecimento> ab = abasDAO.listAbas(nome);
                    List<Abastecimento> abastecimentos = listAbast.ordAbast(ab);
                    if (abs.size() != abastecimentos.size()) {
                        setDates(abastecimentos, abastecimento.getData());
                        if (kmActual < kmNext) {
                            kmFinal = kmNext - kmActual;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(context, "Km Atual: " + String.valueOf(kmActual) + " Km Anterior: " + String.valueOf(kmAnt), Toast.LENGTH_SHORT).show();
        DecimalFormat df = new DecimalFormat("0.00");
        String litros = df.format(abastecimento.getVal() / abastecimento.getPrecoL());

        holder.lblData.setText(abastecimento.getData());
        holder.lblValor.setText(nf.format(abastecimento.getVal()));
        holder.lblPosto.setText(abastecimento.getPosto());
        holder.lblLitros.setText("Litros:" + litros);
        holder.lblKm.setText(String.valueOf(kmFinal));

        return view;
    }

    public void setItems(List<Abastecimento> abastecimentos) {
        this.abs = abastecimentos;
        notifyDataSetChanged();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private static class ViewHolder {
        public TextView lblData;
        public TextView lblPosto;
        public TextView lblValor;
        public TextView lblLitros;
        public TextView lblKm;
    }

    public void setDates(List<Abastecimento> abList, String date) {
        for (int i = 1; i < abList.size() - 1; i++) {
            if(abList.get(i).getData().equals(date)){
                kmActual = abList.get(i).getKm();
                if((i + 1) < abList.size()){
                    kmNext = abList.get(i+1).getKm();
                } else {
                    kmNext = 0.0;
                }
            }
        }
    }
}
