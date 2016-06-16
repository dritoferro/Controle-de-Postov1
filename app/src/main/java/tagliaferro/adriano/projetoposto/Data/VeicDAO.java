package tagliaferro.adriano.projetoposto.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adriano on 08/02/2016.
 */
public class VeicDAO {

    private static VeicDAO instance;

    private static SQLiteDatabase db;

    public static VeicDAO getInstance(Context context) {
        if (instance == null) {
            instance = new VeicDAO(context.getApplicationContext());
        }
        return instance;
    }

    private VeicDAO(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Veiculo> listVeic() {
        String[] columns = {
                VeiculoContract.Columns._IDVEIC,
                VeiculoContract.Columns.CATVEIC,
                VeiculoContract.Columns.COMBVEIC,
                VeiculoContract.Columns.NOMEVEIC
        };

        List<Veiculo> veiculos = new ArrayList<>();
        try (Cursor c = db.query(VeiculoContract.TABLE_NAME_VEIC, columns, null, null, null, null, VeiculoContract.Columns.NOMEVEIC)) {
            if (c.moveToFirst()) {
                do {
                    Veiculo vc = VeicDAO.fromCursor(c);
                    veiculos.add(vc);

                } while (c.moveToNext());
            }

            return veiculos;
        }
    }

    public List<Veiculo> listVeicNome() {
        String[] columns = {
                VeiculoContract.Columns.NOMEVEIC
        };

        List<Veiculo> veiculos = new ArrayList<>();
        Veiculo v0 = new Veiculo();
        v0.setNome("Selecione");
        veiculos.add(v0);
        try (Cursor c = db.query(VeiculoContract.TABLE_NAME_VEIC, columns, null, null, null, null, VeiculoContract.Columns.NOMEVEIC)) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex(VeiculoContract.Columns.NOMEVEIC));
                    Veiculo veiculo = new Veiculo();
                    veiculo.setNome(name);
                    veiculos.add(veiculo);

                } while (c.moveToNext());
            }

            return veiculos;
        }
    }

    public Veiculo listVeicComb(String nome) {
        String[] columns = {
                VeiculoContract.Columns._IDVEIC,
                VeiculoContract.Columns.NOMEVEIC,
                VeiculoContract.Columns.COMBVEIC,
                VeiculoContract.Columns.CATVEIC
        };


        Veiculo veiculo = new Veiculo();
        try (Cursor c = db.query(VeiculoContract.TABLE_NAME_VEIC, columns, VeiculoContract.Columns.NOMEVEIC + " = ?", new String[]{nome}, null, null, null)) {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex(VeiculoContract.Columns._IDVEIC));
                    String name = c.getString(c.getColumnIndex(VeiculoContract.Columns.NOMEVEIC));
                    String cat = c.getString(c.getColumnIndex(VeiculoContract.Columns.CATVEIC));
                    String comb = c.getString(c.getColumnIndex(VeiculoContract.Columns.COMBVEIC));
                    veiculo.setId(id);
                    veiculo.setNome(name);
                    veiculo.setCat(cat);
                    veiculo.setComb(comb);


                } while (c.moveToNext());
            }

            return veiculo;
        }
    }

    public String getNameById(int id) {
        String[] columns = {
                VeiculoContract.Columns._IDVEIC,
                VeiculoContract.Columns.NOMEVEIC,
                VeiculoContract.Columns.COMBVEIC,
                VeiculoContract.Columns.CATVEIC
        };
        String name = "";
        try (Cursor c = db.query(VeiculoContract.TABLE_NAME_VEIC, columns, VeiculoContract.Columns._IDVEIC + " = ?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (c.moveToFirst()) {
                do {
                    name = c.getString(c.getColumnIndex(VeiculoContract.Columns.NOMEVEIC));
                } while (c.moveToNext());
            }

            return name;
        }
    }

    private static Veiculo fromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex(VeiculoContract.Columns._IDVEIC));
        String cat = c.getString(c.getColumnIndex(VeiculoContract.Columns.CATVEIC));
        String comb = c.getString(c.getColumnIndex(VeiculoContract.Columns.COMBVEIC));
        String nome = c.getString(c.getColumnIndex(VeiculoContract.Columns.NOMEVEIC));
        return new Veiculo(id, nome, cat, comb);
    }

    public void save(Veiculo veic) {

        ContentValues content = new ContentValues();
        content.put(VeiculoContract.Columns.CATVEIC, veic.getCat());
        content.put(VeiculoContract.Columns.COMBVEIC, veic.getComb());
        content.put(VeiculoContract.Columns.NOMEVEIC, veic.getNome());

        long ret = db.insert(VeiculoContract.TABLE_NAME_VEIC, null, content);
        veic.setId((int) ret);
    }

    public void update(Veiculo veic) {

        ContentValues content = new ContentValues();
        content.put(VeiculoContract.Columns.CATVEIC, veic.getCat());
        content.put(VeiculoContract.Columns.COMBVEIC, veic.getComb());
        content.put(VeiculoContract.Columns.NOMEVEIC, veic.getNome());

        int ret = db.update(VeiculoContract.TABLE_NAME_VEIC, content, VeiculoContract.Columns._IDVEIC + " = ?", new String[]{String.valueOf(veic.getId())});
    }

    public void delete(int veic, Context context) {
        AbasDAO abasDAO = AbasDAO.getInstance(context);
        abasDAO.deleteByVec(getNameById(veic));
        int ret = db.delete(VeiculoContract.TABLE_NAME_VEIC, VeiculoContract.Columns._IDVEIC + " = ?", new String[]{String.valueOf(veic)});
    }
}
