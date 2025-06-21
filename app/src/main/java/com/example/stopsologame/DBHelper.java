package com.example.stopsologame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "stopgame.db", null, 2); // versão atualizada
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Jogadas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "data TEXT, " +
                "pontuacao INTEGER, " +
                "duracao INTEGER, " +
                "foiRecorde INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Jogadas");
        onCreate(db);
    }

    public void salvarJogada(String data, int pontos, int duracao, boolean foiRecorde, String nome) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("data", data);
        cv.put("pontuacao", pontos);
        cv.put("duracao", duracao);
        cv.put("foiRecorde", foiRecorde ? 1 : 0);
        cv.put("nome", nome); // adiciona o nome
        db.insert("Jogadas", null, cv);
    }


    public Cursor getUltimas10() {
        return getReadableDatabase().rawQuery(
                "SELECT id AS _id, nome, data, pontuacao, duracao, foiRecorde FROM Jogadas ORDER BY pontuacao ASC LIMIT 10",
                null
        );
    }

    public Cursor getTopRecordes() {
        return getReadableDatabase().rawQuery(
                "SELECT id AS _id, data, pontuacao, duracao, foiRecorde, nome FROM Jogadas ORDER BY pontuacao DESC LIMIT 10",
                null
        );
    }

}

