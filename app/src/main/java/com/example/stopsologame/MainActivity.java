package com.example.stopsologame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView textRecorde;
    SharedPreferences prefs;
    EditText edtNomeJogador;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textRecorde = findViewById(R.id.textRecorde);
        edtNomeJogador = findViewById(R.id.edtNomeJogador);
        imgLogo = findViewById(R.id.imgLogo);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imgLogo.startAnimation(anim);

        prefs = getSharedPreferences("jogo", MODE_PRIVATE);
        int recorde = prefs.getInt("recorde", 0);
        textRecorde.setText("Recorde: " + recorde);

        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnVerRecordes = findViewById(R.id.btnVerRecordes);

        btnIniciar.setOnClickListener(v -> {
            String nome = edtNomeJogador.getText().toString().trim();
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite seu nome para jogar!", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putString("nome_jogador", nome).apply();
                startActivity(new Intent(this, GameActivity.class));
            }
        });

        btnVerRecordes.setOnClickListener(v ->
                startActivity(new Intent(this, RecordesActivity.class)));
    }
}