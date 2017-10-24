package pedrocosta.controlador.ListaMovimentos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pedrocosta.controlador.ListaMovimentos.Movimento;

/**
 * Created by Pedro-PC on 9/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ListaMovimentos.db";


    // Movimentos table name
    private static final String TABLE_MOVIMENTOS = "movimentos";

    // Movimentos Table Columns names
    private static final String KEY_ID = "id", KEY_NAME = "name", KEY_POLEGAR = "polegar", KEY_INDICADOR = "indicador",
                                KEY_MEDIO = "medio", KEY_ANELAR = "anelar", KEY_MINIMO = "minimo", KEY_ROTACAO = "rotacao";
    private static final String COMMA_SEP  = ",";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MOVIMENTOS = "CREATE TABLE " + TABLE_MOVIMENTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_POLEGAR + " INTEGER ,"
                + KEY_INDICADOR + " INTEGER ,"
                + KEY_MEDIO + " INTEGER ,"
                + KEY_ANELAR + " INTEGER ,"
                + KEY_MINIMO + " INTEGER ,"
                + KEY_ROTACAO + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_MOVIMENTOS);
        defaultValues(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMENTOS);

        // Create tables again
        onCreate(db);
    }


    private void defaultValues(SQLiteDatabase db) {
        db.execSQL(insertStatment("Polegar",100,0,0,0,0,0));
        db.execSQL(insertStatment("Indicador",0,100,0,0,0,0));
        db.execSQL(insertStatment("Medio",0,0,100,0,0,0));
        db.execSQL(insertStatment("Anelar",0,0,0,100,0,0));
        db.execSQL(insertStatment("Minimo",0,0,0,0,100,0));
        db.execSQL(insertStatment("Rotacao",0,0,0,0,0,100));
    }

    private String insertStatment(String nome, int polegar, int indicador, int medio, int anelar, int minimo, int rotacao){
        String str = "INSERT INTO " + TABLE_MOVIMENTOS + " (" + KEY_NAME + COMMA_SEP + KEY_POLEGAR + COMMA_SEP + KEY_INDICADOR + COMMA_SEP + KEY_MEDIO + COMMA_SEP + KEY_ANELAR + COMMA_SEP + KEY_MINIMO + COMMA_SEP + KEY_ROTACAO + ")" +
                                                    " VALUES (" + "\"" + nome + "\"" + COMMA_SEP + polegar +  COMMA_SEP + indicador + COMMA_SEP + medio + COMMA_SEP + anelar + COMMA_SEP + minimo + COMMA_SEP + rotacao + ")";

        return str;
    }




    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new movimento
    void addMovimento(Movimento movimento) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movimento.getNome());
        values.put(KEY_POLEGAR, movimento.getPolegar());
        values.put(KEY_INDICADOR, movimento.getIndicador());
        values.put(KEY_MEDIO, movimento.getMedio());
        values.put(KEY_ANELAR, movimento.getAnelar());
        values.put(KEY_MINIMO, movimento.getMinimo());
        values.put(KEY_ROTACAO, movimento.getRotacao());

        // Inserting Row
        db.insert(TABLE_MOVIMENTOS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single movimento
    Movimento getMovimento(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                KEY_ID,
                KEY_NAME,
                KEY_POLEGAR,KEY_INDICADOR,KEY_MEDIO,KEY_ANELAR,KEY_MINIMO
        };

        Cursor cursor = db.query(TABLE_MOVIMENTOS, projection , KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        //HashMap<String, Integer> hashDedoValor = new HashMap<>();
        //Integer.parseInt(cursor.getString(0)), hashDedoValor.put("polegar",Integer.parseInt(cursor.getString(1)))
        int idDB = Integer.parseInt(cursor.getString(0));
        String nome = cursor.getString(1);
        int polegar = Integer.parseInt(cursor.getString(2));
        int indicador = Integer.parseInt(cursor.getString(3));
        int medio = Integer.parseInt(cursor.getString(4));
        int anelar = Integer.parseInt(cursor.getString(5));
        int minimo = Integer.parseInt(cursor.getString(6));
        int rotacao = Integer.parseInt(cursor.getString(7));
        Movimento movimento = new Movimento(idDB,nome,polegar,indicador,medio,anelar,minimo,rotacao);

        return movimento;
    }

    // Getting All Movimentos
    public List<Movimento> getAllMovimentos() {
        List<Movimento> movimentoList = new ArrayList<Movimento>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIMENTOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movimento movimento = new Movimento(0,"Nome",0,0,0,0,0,0);
                movimento.setID(Integer.parseInt(cursor.getString(0)));
                movimento.setNome(cursor.getString(1));
                int polegar = Integer.parseInt(cursor.getString(2));
                int indicador = Integer.parseInt(cursor.getString(3));
                int medio = Integer.parseInt(cursor.getString(4));
                int anelar = Integer.parseInt(cursor.getString(5));
                int minimo = Integer.parseInt(cursor.getString(6));
                int rotacao = Integer.parseInt(cursor.getString(7));
                movimento.setDedos(polegar,rotacao,indicador,medio,anelar,minimo);

                // Adding movimento to list
                movimentoList.add(movimento);
            } while (cursor.moveToNext());
        }

        // return movimento list
        return movimentoList;
    }

    // Updating single movimento
    public int updateMovimento(Movimento movimento) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movimento.getNome());
        values.put(KEY_POLEGAR, movimento.getPolegar());
        values.put(KEY_INDICADOR, movimento.getIndicador());
        values.put(KEY_MEDIO, movimento.getMedio());
        values.put(KEY_ANELAR, movimento.getAnelar());
        values.put(KEY_MINIMO, movimento.getMinimo());
        values.put(KEY_ROTACAO, movimento.getRotacao());

        // updating row
        return db.update(TABLE_MOVIMENTOS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(movimento.getID()) });
    }

    // Deleting single movimento
    public void deleteMovimento(Movimento movimento) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIMENTOS, KEY_ID + " = ?",
                new String[] { String.valueOf(movimento.getID()) });
        db.close();
    }
    
    
    
    
    
    
    
}
