package com.example.stopsologame;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

public class RecordesActivity extends AppCompatActivity {
    ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordes);

        listView = findViewById(R.id.listView);
        dbHelper = new DBHelper(this);

        Cursor c = dbHelper.getTopRecordes();

        String[] from = {"nome", "data", "pontuacao", "foiRecorde"};
        int[] to = {R.id.txtNome, R.id.txtData, R.id.txtPontos, R.id.txtRecorde};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.item_jogada, c, from, to, 0);

        adapter.setViewBinder((view, cursor, columnIndex) -> {
            if (view.getId() == R.id.txtRecorde) {
                int recorde = cursor.getInt(columnIndex);
                view.setVisibility(recorde == 1 ? View.VISIBLE : View.GONE);
                return true;
            }
            return false;
        });

        listView.setAdapter(adapter);
    }
}
