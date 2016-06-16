package tagliaferro.adriano.projetoposto.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adriano on 08/02/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "prposto";
    public static final int DB_VERSION = 1;

    private static final String TB_ABS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, %s CURRENCY NOT NULL, %s TEXT NOT NULL, %s DOUBLE NOT NULL, %s TEXT NOT NULL, %s DOUBLE NOT NULL)", AbastecimentoContract.TABLE_NAME_ABS, AbastecimentoContract.Columns._IDABS,
            AbastecimentoContract.Columns.DATAABS, AbastecimentoContract.Columns.VALORABS, AbastecimentoContract.Columns.POSTOABS, AbastecimentoContract.Columns.KMABS, AbastecimentoContract.Columns.VEICULO, AbastecimentoContract.Columns.PRECOL);

    private static final String TB_POS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s CURRENCY NOT NULL, %s CURRENCY NOT NULL, %s CURRENCY NOT NULL)", PostoContract.TABLE_NAME_POS, PostoContract.Columns._IDPOS, PostoContract.Columns.NOMEPOS,
            PostoContract.Columns.COMB1POS, PostoContract.Columns.COMB2POS, PostoContract.Columns.COMB3POS, PostoContract.Columns.VAL1POS, PostoContract.Columns.VAL2POS, PostoContract.Columns.VAL3POS);

    private static final String TB_VEIC = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL)", VeiculoContract.TABLE_NAME_VEIC, VeiculoContract.Columns._IDVEIC, VeiculoContract.Columns.NOMEVEIC, VeiculoContract.Columns.CATVEIC, VeiculoContract.Columns.COMBVEIC);

    private static final String DROP_TB_ABS = "DROP TABLE IF EXISTS " + AbastecimentoContract.TABLE_NAME_ABS;
    private static final String DROP_TB_POS = "DROP TABLE IF EXISTS " + PostoContract.TABLE_NAME_POS;
    private static final String DROP_TB_VEIC = "DROP TABLE IF EXISTS " + VeiculoContract.TABLE_NAME_VEIC;

    private static DBHelper instance;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_ABS);
        db.execSQL(TB_POS);
        db.execSQL(TB_VEIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TB_ABS);
        db.execSQL(DROP_TB_POS);
        db.execSQL(DROP_TB_VEIC);
        onCreate(db);
    }
}
