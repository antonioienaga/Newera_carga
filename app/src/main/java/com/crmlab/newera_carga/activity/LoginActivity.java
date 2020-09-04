package com.crmlab.newera_carga.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crmlab.newera_carga.R;
import com.crmlab.newera_carga.config.ConfiguracaoFirebase;
import com.crmlab.newera_carga.config.ConfiguracaoFirestore;
import com.crmlab.newera_carga.models.GlobalVariables;
import com.crmlab.newera_carga.models.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private EditText campoEmail;
    private EditText campoSenha;
    private ProgressBar progressBar;

    private Button botaoEntrar;

    private Login login;
    private FirebaseFirestore db;

    String strEmail2, strStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editTextEmail);
        campoSenha = findViewById(R.id.editTextSenha);
        botaoEntrar = findViewById(R.id.buttonLogin);
        botaoEntrar.setEnabled(true);
        progressBar = findViewById(R.id.progressBar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                botaoEntrar.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                if ( !textoEmail.isEmpty() ){
                    if ( !textoSenha.isEmpty() ){

                        login = new Login();
                        login.setEmail( textoEmail );
                        login.setSenha( textoSenha );
                        validarLogin();
                    }else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void validarLogin(){
        /*  Vamos validar se o usuario já existe na base de autenticacao
            O usuario deverá existir também na base do Firebase (validarUsuario)

         */

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                login.getEmail(),
                login.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    validarUsuario(login.getEmail());
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    botaoEntrar.setEnabled(true);
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validarUsuario(final String strEmail){
        /*  O usuario deve exisir no Firestore.
            Temos que validar também a hierarquia dos usuários para identificar aonde os clientes estão associados
         */

        strEmail2 = "";
        strStatus = "";
        db = ConfiguracaoFirestore.getFirebaseDb();
        DocumentReference docRef = db.collection("Usuarios").document(strEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){

                        /*  o campo emailmaster define a associacao do usuario corrente
                            primeiro guardamos as informações do usuario corrente, caso tenha o master, substituimos pela do master
                         */

                        strEmail2 = document.getString("emailmaster");
                        strStatus = document.getString("status");

                        GlobalVariables mApp = (GlobalVariables)getApplicationContext();
                        mApp.setmGlobalUser(strEmail);
                        mApp.setmGlobalProduto1(document.getString("produto1"));
                        mApp.setmGlobalProduto2(document.getString("produto2"));
                        mApp.setmGlobalProduto3(document.getString("produto3"));
                        mApp.setmGlobalProduto4(document.getString("produto4"));
                        mApp.setmGlobalProduto5(document.getString("produto5"));
                        if (strEmail2.isEmpty()){
                            mApp.setmGlobalVarValues(strEmail);
                            abrirTelaPrincipal(strEmail);
                        }

                    }else{
                        botaoEntrar.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                "Usuário existe no banco, mas não no Firestore - Contate suporte",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    botaoEntrar.setEnabled(true);
                    Log.d("kop", "get failed with ", task.getException());
                };
                // rotina para hierarquia de organizações
                if (!strEmail2.isEmpty() && strStatus.contains("Associado")){

                    final DocumentReference docRef2 = db.collection("Usuarios").document(strEmail2);
                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    GlobalVariables mApp = (GlobalVariables)getApplicationContext();
                                    mApp.setmGlobalVarValues(strEmail2);
                                    mApp.setmGlobalProduto1(document.getString("produto1"));
                                    mApp.setmGlobalProduto2(document.getString("produto2"));
                                    mApp.setmGlobalProduto3(document.getString("produto3"));
                                    mApp.setmGlobalProduto4(document.getString("produto4"));
                                    mApp.setmGlobalProduto5(document.getString("produto5"));

                                    abrirTelaPrincipal(strEmail2);
                                    finish();
                                }else{
                                    botaoEntrar.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this,
                                            "Email master invalido - Contate suporte",
                                            Toast.LENGTH_LONG).show();
                                }

                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                botaoEntrar.setEnabled(true);
                                Log.d("kop", "get failed with ", task.getException());
                            }

                        }
                    });
                }else{
                    botaoEntrar.setEnabled(true);

                };
            };
        });
    }

    public void abrirTelaPrincipal(String strEmail){
        // Abre a tela de consulta de telefone, esse jeito pelo intent podemos transferir informação. O outro é através de uma global
        //Intent i = new Intent(MainActivity.this,  CampaignExecActivity.class);

        Intent i = new Intent(LoginActivity.this,  MainActivity.class);
        i.putExtra("email", strEmail);
        i.putExtra("Telefone", "11");
        startActivity(i);
        finish();
    }

}