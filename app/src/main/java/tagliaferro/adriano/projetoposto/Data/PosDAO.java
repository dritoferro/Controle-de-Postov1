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
public class PosDAO {

    private static PosDAO instance;

    private static SQLiteDatabase db;

    public static PosDAO getInstance(Context context) {
        if (instance == null) {
            instance = new PosDAO(context.getApplicationContext());
        }
        return instance;
    }

    private PosDAO(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Posto> listPos() {
        String[] columns = {
                PostoContract.Columns._IDPOS,
                PostoContract.Columns.COMB1POS,
                PostoContract.Columns.COMB2POS,
                PostoContract.Columns.COMB3POS,
                PostoContract.Columns.NOMEPOS,
                PostoContract.Columns.VAL1POS,
                PostoContract.Columns.VAL2POS,
                PostoContract.Columns.VAL3POS
        };

        List<Posto> postos = new ArrayList<>();

        try (Cursor c = db.query(PostoContract.TABLE_NAME_POS, columns, null, null, null, null, PostoContract.Columns.NOMEPOS)) {
            if (c.moveToFirst()) {
                do {
                    Posto pt = PosDAO.fromCursor(c);
                    postos.add(pt);

                } while (c.moveToNext());
            }

            return postos;
        }
    }

    private static Posto fromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex(PostoContract.Columns._IDPOS));
        String nome = c.getString(c.getColumnIndex(PostoContract.Columns.NOMEPOS));
        String comb1 = c.getString(c.getColumnIndex(PostoContract.Columns.COMB1POS));
        String comb2 = c.getString(c.getColumnIndex(PostoContract.Columns.COMB2POS));
        String comb3 = c.getString(c.getColumnIndex(PostoContract.Columns.COMB3POS));
        Double val1 = c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL1POS));
        Double val2 = c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL2POS));
        Double val3 = c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL3POS));

        return new Posto(id, nome, comb1, comb2, comb3, val1, val2, val3);
    }

    public Posto listPosNome(String nome) {
        String[] columns = {
                PostoContract.Columns._IDPOS,
                PostoContract.Columns.COMB1POS,
                PostoContract.Columns.COMB2POS,
                PostoContract.Columns.COMB3POS,
                PostoContract.Columns.NOMEPOS,
                PostoContract.Columns.VAL1POS,
                PostoContract.Columns.VAL2POS,
                PostoContract.Columns.VAL3POS
        };

        Posto postos = new Posto();

        try (Cursor c = db.query(PostoContract.TABLE_NAME_POS, columns, PostoContract.Columns.NOMEPOS + " = ?", new String[]{nome}, null, null, null)){
            if (c.moveToFirst()) {
                do {
                    postos.setId(c.getInt(c.getColumnIndex(PostoContract.Columns._IDPOS)));
                    postos.setNome(nome);
                    postos.setComb1(c.getString(c.getColumnIndex(PostoContract.Columns.COMB1POS)));
                    postos.setComb2(c.getString(c.getColumnIndex(PostoContract.Columns.COMB2POS)));
                    postos.setComb3(c.getString(c.getColumnIndex(PostoContract.Columns.COMB3POS)));
                    postos.setVal1(c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL1POS)));
                    postos.setVal2(c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL2POS)));
                    postos.setVal3(c.getDouble(c.getColumnIndex(PostoContract.Columns.VAL3POS)));

                } while (c.moveToNext());
            }

            return postos;
        }
    }

    public List<String> listPosAll() {
        String[] columns = {
                PostoContract.Columns._IDPOS,
                PostoContract.Columns.COMB1POS,
                PostoContract.Columns.COMB2POS,
                PostoContract.Columns.COMB3POS,
                PostoContract.Columns.NOMEPOS,
                PostoContract.Columns.VAL1POS,
                PostoContract.Columns.VAL2POS,
                PostoContract.Columns.VAL3POS
        };

        List<String> postos = new ArrayList<>();

        try (Cursor c = db.query(PostoContract.TABLE_NAME_POS, columns, null, null, null, null, PostoContract.Columns.NOMEPOS)) {
            if (c.moveToFirst()) {
                do {
                    postos.add(c.getString(c.getColumnIndex(PostoContract.Columns.NOMEPOS)));

                } while (c.moveToNext());
            }

            return postos;
        }
    }

    public List<String> listPosByVec(Veiculo veiculo) {
        String[] columns = {
                PostoContract.Columns._IDPOS,
                PostoContract.Columns.COMB1POS,
                PostoContract.Columns.COMB2POS,
                PostoContract.Columns.COMB3POS,
                PostoContract.Columns.NOMEPOS,
                PostoContract.Columns.VAL1POS,
                PostoContract.Columns.VAL2POS,
                PostoContract.Columns.VAL3POS
        };

        List<String> postos = new ArrayList<>();

        try (Cursor c = db.query(PostoContract.TABLE_NAME_POS, columns, null, null, null, null, PostoContract.Columns.NOMEPOS)) {
            if (c.moveToFirst()) {
                do {
                    if(veiculo.getComb().equals(c.getString(c.getColumnIndex(PostoContract.Columns.COMB1POS)))){
                        postos.add(c.getString(c.getColumnIndex(PostoContract.Columns.NOMEPOS)));
                    } else if(veiculo.getComb().equals(c.getString(c.getColumnIndex(PostoContract.Columns.COMB2POS)))){
                        postos.add(c.getString(c.getColumnIndex(PostoContract.Columns.NOMEPOS)));
                    } else if(veiculo.getComb().equals(c.getString(c.getColumnIndex(PostoContract.Columns.COMB3POS)))){
                        postos.add(c.getString(c.getColumnIndex(PostoContract.Columns.NOMEPOS)));
                    }


                } while (c.moveToNext());
            }

            return postos;
        }
    }

    public void save(Posto posto) {

        ContentValues values = new ContentValues();
        values.put(PostoContract.Columns.NOMEPOS, posto.getNome());
        values.put(PostoContract.Columns.COMB1POS, posto.getComb1());
        values.put(PostoContract.Columns.COMB2POS, posto.getComb2());
        values.put(PostoContract.Columns.COMB3POS, posto.getComb3());
        values.put(PostoContract.Columns.VAL1POS, posto.getVal1());
        values.put(PostoContract.Columns.VAL2POS, posto.getVal2());
        values.put(PostoContract.Columns.VAL3POS, posto.getVal3());

        long ret = db.insert(PostoContract.TABLE_NAME_POS, null, values);
        posto.setId((int) ret);
    }

    public void update(Posto posto) {

        ContentValues values = new ContentValues();
        values.put(PostoContract.Columns.NOMEPOS, posto.getNome());
        values.put(PostoContract.Columns.COMB1POS, posto.getComb1());
        values.put(PostoContract.Columns.COMB2POS, posto.getComb2());
        values.put(PostoContract.Columns.COMB3POS, posto.getComb3());
        values.put(PostoContract.Columns.VAL1POS, posto.getVal1());
        values.put(PostoContract.Columns.VAL2POS, posto.getVal2());
        values.put(PostoContract.Columns.VAL3POS, posto.getVal3());

        int ret = db.update(PostoContract.TABLE_NAME_POS, values, PostoContract.Columns._IDPOS + " = ?", new String[]{String.valueOf(posto.getId())});
    }

    public void delete(int posto) {

        int ret = db.delete(PostoContract.TABLE_NAME_POS, PostoContract.Columns._IDPOS + " = ?", new String[]{String.valueOf(posto)});
    }
}
