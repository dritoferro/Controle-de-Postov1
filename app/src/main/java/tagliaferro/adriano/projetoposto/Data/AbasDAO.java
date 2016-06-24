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
public class AbasDAO {

    private static AbasDAO instance;

    private SQLiteDatabase db;

    public static AbasDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AbasDAO(context.getApplicationContext());
        }
        return instance;
    }

    private AbasDAO(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Abastecimento> listAbas(String nomeVec) {

        String[] columns = {AbastecimentoContract.Columns._IDABS,
                AbastecimentoContract.Columns.DATAABS,
                AbastecimentoContract.Columns.POSTOABS,
                AbastecimentoContract.Columns.VALORABS,
                AbastecimentoContract.Columns.KMABS,
                AbastecimentoContract.Columns.VEICULO,
                AbastecimentoContract.Columns.PRECOL
        };

        List<Abastecimento> abastecimentos = new ArrayList<>();

        try (Cursor c = db.query(AbastecimentoContract.TABLE_NAME_ABS, columns, AbastecimentoContract.Columns.VEICULO + " = ?", new String[]{nomeVec}, null, null, AbastecimentoContract.Columns.DATAABS)) {
            if (c.moveToFirst()) {
                do {
                    Abastecimento ab = AbasDAO.fromCursor(c);
                    abastecimentos.add(ab);

                } while (c.moveToNext());
            }

            return abastecimentos;
        }

    }

    public List<Abastecimento> listAbasBf(String nomeVec, int month) {

        String[] columns = {AbastecimentoContract.Columns._IDABS,
                AbastecimentoContract.Columns.DATAABS,
                AbastecimentoContract.Columns.POSTOABS,
                AbastecimentoContract.Columns.VALORABS,
                AbastecimentoContract.Columns.KMABS,
                AbastecimentoContract.Columns.VEICULO,
                AbastecimentoContract.Columns.PRECOL
        };

        List<Abastecimento> abastecimentos = new ArrayList<>();

        try (Cursor c = db.query(AbastecimentoContract.TABLE_NAME_ABS, columns, AbastecimentoContract.Columns.VEICULO + " = ?", new String[]{nomeVec}, null, null, AbastecimentoContract.Columns.DATAABS)) {
            if (c.moveToFirst()) {
                do {
                    Abastecimento ab = AbasDAO.fromCursor(c);
                    String[] data = ab.getData().split("/");
                    if (data[1].equals(String.valueOf(month))) {
                        abastecimentos.add(ab);
                    }
                } while (c.moveToNext());
            }

            return abastecimentos;
        }

    }

    /*public String topAbas(List<Veiculo> veic) {


        String[] columns = {AbastecimentoContract.Columns._IDABS,
                AbastecimentoContract.Columns.DATAABS,
                AbastecimentoContract.Columns.POSTOABS,
                AbastecimentoContract.Columns.VALORABS,
                AbastecimentoContract.Columns.KMABS,
                AbastecimentoContract.Columns.VEICULO
        };

        List<String> veiculos = new ArrayList<>();

        try (Cursor c = db.query(AbastecimentoContract.TABLE_NAME_ABS, columns, null, null, null, null, null)) {
            if (c.moveToFirst()) {
                do {
                    veiculos.add(c.getString(c.getColumnIndex(AbastecimentoContract.Columns.VEICULO)));

                } while (c.moveToNext());
            }
            if (veiculos.size() != 0) {
                int[] cont = new int[veiculos.size()];
                for (int i = 0; i < veic.size(); i++) {
                    for (int j = 0; j < veiculos.size(); j++) {
                        if (veic.get(i).getNome().equals(veiculos.get(j))) {
                            cont[i]++;
                        }
                    }
                }
                int max, p;
                p = 0;
                max = cont[0];
                for (int i = 0; i < cont.length; i++) {
                    if (max < cont[i]) {
                        p = i;
                    }
                }

                return veiculos.get(p);
            }
        }
        return null;
    }*/

    private static Abastecimento fromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex(AbastecimentoContract.Columns._IDABS));
        String nomePosto = c.getString(c.getColumnIndex(AbastecimentoContract.Columns.POSTOABS));
        String dataAbas = c.getString(c.getColumnIndex(AbastecimentoContract.Columns.DATAABS));
        Double valorAbas = c.getDouble(c.getColumnIndex(AbastecimentoContract.Columns.VALORABS));
        Double kmAbas = c.getDouble(c.getColumnIndex(AbastecimentoContract.Columns.KMABS));
        String veic = c.getString(c.getColumnIndex(AbastecimentoContract.Columns.VEICULO));
        Double precoL = c.getDouble(c.getColumnIndex(AbastecimentoContract.Columns.PRECOL));
        return new Abastecimento(id, dataAbas, valorAbas, nomePosto, kmAbas, veic, precoL);
    }

    public void save(Abastecimento abs, Context context) {

        Veiculo veiculo;
        Posto posto;
        VeicDAO veicDAO = VeicDAO.getInstance(context);
        PosDAO posDAO = PosDAO.getInstance(context);
        veiculo = veicDAO.listVeicComb(abs.getVeiculo());
        posto = posDAO.listPosNome(abs.getPosto());
        String combustivelVec = veiculo.getComb();
        double valorL = 0.0;
        if (combustivelVec.equals(posto.getComb1())) {
            valorL = posto.getVal1();
        }
        if (combustivelVec.equals(posto.getComb2())) {
            valorL = posto.getVal2();
        }
        if (combustivelVec.equals(posto.getComb3())) {
            valorL = posto.getVal3();
        }

        ContentValues values = new ContentValues();
        values.put(AbastecimentoContract.Columns.POSTOABS, abs.getPosto());
        values.put(AbastecimentoContract.Columns.DATAABS, abs.getData());
        values.put(AbastecimentoContract.Columns.VALORABS, abs.getVal());
        values.put(AbastecimentoContract.Columns.KMABS, abs.getKm());
        values.put(AbastecimentoContract.Columns.VEICULO, abs.getVeiculo());
        values.put(AbastecimentoContract.Columns.PRECOL, valorL);

        long id = db.insert(AbastecimentoContract.TABLE_NAME_ABS, null, values);
        abs.setId((int) id);
    }

    public void update(Abastecimento abs) {

        ContentValues values = new ContentValues();
        values.put(AbastecimentoContract.Columns.POSTOABS, abs.getPosto());
        values.put(AbastecimentoContract.Columns.DATAABS, abs.getData());
        values.put(AbastecimentoContract.Columns.VALORABS, abs.getVal());
        values.put(AbastecimentoContract.Columns.KMABS, abs.getKm());
        values.put(AbastecimentoContract.Columns.PRECOL, abs.getPrecoL());

        db.update(AbastecimentoContract.TABLE_NAME_ABS, values, AbastecimentoContract.Columns._IDABS + " = ?", new String[]{String.valueOf(abs.getId())});

    }

    public void updateVec(String antNome, String novNome) {

        ContentValues values = new ContentValues();
        values.put(AbastecimentoContract.Columns.VEICULO, novNome);

        db.update(AbastecimentoContract.TABLE_NAME_ABS, values, AbastecimentoContract.Columns.VEICULO + " = ?", new String[]{antNome});

    }

    public void delete(int abs) {

        db.delete(AbastecimentoContract.TABLE_NAME_ABS, AbastecimentoContract.Columns._IDABS + " = ?", new String[]{String.valueOf(abs)});

    }

    public void deleteByVec(String name) {

        db.delete(AbastecimentoContract.TABLE_NAME_ABS, AbastecimentoContract.Columns.VEICULO + " = ?", new String[]{name});

    }

    public String findVeiculoById(long id) {
        String[] columns = new String[]{
                AbastecimentoContract.Columns.VEICULO
        };
        Cursor c = db.query(AbastecimentoContract.TABLE_NAME_ABS, columns, AbastecimentoContract.Columns._IDABS + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (c.moveToNext()) {
            return c.getString(c.getColumnIndex(AbastecimentoContract.Columns.VEICULO));
        }
        c.close();
        return null;
    }
}
