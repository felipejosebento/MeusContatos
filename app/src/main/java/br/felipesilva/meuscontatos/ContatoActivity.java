package br.felipesilva.meuscontatos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;

import models.ContatoModel;

public class ContatoActivity extends AppCompatActivity {
    private TextView tvTelefone;
    private TextView tvIdade;
    private TextView tvNome;
    private TextView tvSobrenome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        tvNome = (TextView)findViewById(R.id.tvNome);
        tvSobrenome = (TextView)findViewById(R.id.tvSobrenome);
        tvIdade = (TextView)findViewById(R.id.tvIdade);
        tvTelefone = (TextView)findViewById(R.id.tvTelefone);

        Intent intent;
        intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String nomeContato = bundle.getString("nome");
        tvNome.setText(nomeContato);

        String TelefoneContato = bundle.getString("telefone");
        tvTelefone.setText(TelefoneContato);

        String IdadeContato = bundle.getString("idade");
        tvIdade.setText(IdadeContato);

        String SobrenomeContato = bundle.getString("sobrenome");
        tvSobrenome.setText(SobrenomeContato);

    }

}
