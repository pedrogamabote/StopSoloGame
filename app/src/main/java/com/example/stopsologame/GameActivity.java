package com.example.stopsologame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView txtLetra, txtTimer, txtPontuacao;
    EditText edtNome, edtAnimal, edtFruta, edtObjeto, edtLugar;
    int pontuacao = 0;
    CountDownTimer timer;
    DBHelper dbHelper;
    SharedPreferences prefs;
    long tempoRestante = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtLetra = findViewById(R.id.txtLetra);
        txtTimer = findViewById(R.id.txtTimer);
        txtPontuacao = findViewById(R.id.txtPontuacao);
        edtNome = findViewById(R.id.edtNome);
        edtAnimal = findViewById(R.id.edtAnimal);
        edtFruta = findViewById(R.id.edtFruta);
        edtObjeto = findViewById(R.id.edtObjeto);
        edtLugar = findViewById(R.id.edtLugar);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        dbHelper = new DBHelper(this);
        prefs = getSharedPreferences("jogo", MODE_PRIVATE);

        iniciarRodada();

        btnEnviar.setOnClickListener(v -> {
            timer.cancel();

            String letraAtual = txtLetra.getText().toString().substring(7).toUpperCase();
            if (verificarAcerto(letraAtual)) {
                pontuacao += 50;
                txtPontuacao.setText("Pontuação: " + pontuacao);
                iniciarRodada();
            } else {
                finalizarJogo(); // vai contar apenas os campos certos aqui também
            }
        });
    }

    private void iniciarRodada() {
        String letra = gerarLetraAleatoria();
        txtLetra.setText("Letra: " + letra);
        limparCampos();

        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempoRestante = millisUntilFinished;
                txtTimer.setText("Tempo: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                finalizarJogo(); // tempo acabou, vamos contar os pontos corretos
            }
        }.start();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtAnimal.setText("");
        edtFruta.setText("");
        edtObjeto.setText("");
        edtLugar.setText("");
    }

    private boolean verificarAcerto(String letra) {
        return edtNome.getText().toString().toUpperCase().startsWith(letra)
                && edtAnimal.getText().toString().toUpperCase().startsWith(letra)
                && edtFruta.getText().toString().toUpperCase().startsWith(letra)
                && edtObjeto.getText().toString().toUpperCase().startsWith(letra)
                && edtLugar.getText().toString().toUpperCase().startsWith(letra);
    }

    private String gerarLetraAleatoria() {
        Random r = new Random();
        return String.valueOf((char)(r.nextInt(26) + 'A'));
    }

    private void finalizarJogo() {
        String letraAtual = txtLetra.getText().toString().substring(7).toUpperCase();

        // Soma pontos por cada campo correto
        if (edtNome.getText().toString().toUpperCase().startsWith(letraAtual)) pontuacao += 10;
        if (edtAnimal.getText().toString().toUpperCase().startsWith(letraAtual)) pontuacao += 10;
        if (edtFruta.getText().toString().toUpperCase().startsWith(letraAtual)) pontuacao += 10;
        if (edtObjeto.getText().toString().toUpperCase().startsWith(letraAtual)) pontuacao += 10;
        if (edtLugar.getText().toString().toUpperCase().startsWith(letraAtual)) pontuacao += 10;

        txtPontuacao.setText("Pontuação: " + pontuacao);

        boolean foiRecorde = false;
        int recorde = prefs.getInt("recorde", 0);
        if (pontuacao > recorde) {
            foiRecorde = true;
            prefs.edit().putInt("recorde", pontuacao).apply();
        }

        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        int duracao = (int) ((60000 - tempoRestante) / 1000);
        if (duracao < 0) duracao = 0;

        String nomeJogador = prefs.getString("nome_jogador", "Jogador");
        dbHelper.salvarJogada(data, pontuacao, duracao, foiRecorde, nomeJogador);

        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Pontuação final: " + pontuacao)
                .setPositiveButton("Jogar novamente", (dialog, which) -> {
                    pontuacao = 0;
                    iniciarRodada();
                })
                .setNegativeButton("Sair", (dialog, which) -> finish())
                .show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Sair do jogo?")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> super.onBackPressed())
                .setNegativeButton("Não", null)
                .show();
    }
}
