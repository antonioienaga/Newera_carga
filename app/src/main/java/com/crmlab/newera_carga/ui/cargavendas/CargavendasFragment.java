package com.crmlab.newera_carga.ui.cargavendas;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.crmlab.newera_carga.R;
import com.crmlab.newera_carga.config.FormataCPF;
import com.crmlab.newera_carga.models.GlobalVariables;
import com.crmlab.newera_carga.models.Linhas;
import com.crmlab.newera_carga.models.Visita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CargavendasFragment extends Fragment {

    private FirebaseAuth autenticacao;
    private FirebaseFirestore db;
    private EditText edtNomeArquivo, edtUser, edtTipo;
    private TextView txtNome, txtTelefone, txtSucesso;
    private List<Linhas> listalinhas = new ArrayList<>();
    private Button btInserir, btPular, btCarregar;
    private StringBuilder stringBuilder = new StringBuilder();
    private ProgressBar progressBar;
    private Integer i, intDia, intMes, intAno;;
    private String strNome, CPF, Telefone, strData, strValor;
    private Visita visita;
    private Boolean bArquivo, bRetorno;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Timestamp tmData;

    private String strBu, strUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cargavendas, container, false);
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtSucesso = view.findViewById(R.id.textView3);
        txtNome = view.findViewById(R.id.textView4);
        txtTelefone = view.findViewById(R.id.textView5);
        btCarregar = view.findViewById(R.id.buttonIniciar);
        btInserir = view.findViewById(R.id.buttonInserir);
        btPular = view.findViewById(R.id.buttonPular);
        progressBar = view.findViewById(R.id.progressBar3);

        GlobalVariables mApp = (GlobalVariables) getActivity().getApplicationContext();
        strBu = mApp.getmGlobalVarValues();
        strUser = mApp.getmGlobalUser();


        btCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bArquivo = false;
                listalinhas.clear();
                btInserir.setEnabled(false);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, 0);
                i = 1;
                btInserir.setEnabled(true);
            }

        });

        btInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bArquivo) {
                    Toast.makeText(getActivity().getApplicationContext(), "Arquivo incorreto", Toast.LENGTH_SHORT).show();
                } else {
                    btInserir.setEnabled(false);
                    Linhas linhas;
                    if (i < listalinhas.size()) {
                        linhas = listalinhas.get(i);
                        String[] valorComSplit = linhas.getLinha().split("\",\"");
                        Integer q = 1;
                        for (String s : valorComSplit) {
                            q = q + 1;
                        }
                        if (q == 5 && !valorComSplit[2].equals("(blank)")) {
                            InserirRegistro(linhas);
                        } else {
                            btInserir.setEnabled(true);
                            i++;
                            ChamaBotao();
                        }
                    }else{
                        btInserir.setEnabled(false);
                        Toast.makeText(getActivity().getApplicationContext(), "Arquivo Processado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = listalinhas.size() + 2;
                btInserir.setEnabled(true);
            }
        });
    }

    public void InserirRegistro(Linhas linhas){

        try {
            String nome[] = linhas.getLinha().split("\",\"", 4);
            strData = nome[0].substring(1,nome[0].length());
            strValor = nome[3].substring(0,nome[3].length()-1);

            if (strValor.indexOf(".")!=-1){
                strValor = strValor.substring(0,strValor.indexOf("."))+
                        strValor.substring(strValor.indexOf(".")+1,strValor.length());}

            strValor = strValor.replaceAll(",", ".");

            strNome = nome[1];
            Number numValor = Double.parseDouble(strValor);

            txtNome.setText(strData);
            txtTelefone.setText(nome[1]);
            if (!nome[2].equals("(blank)")) {

                visita = new Visita();
                visita.setCriado(Timestamp.now());
                visita.setCriadoPor("Carga");
                visita.setValor(numValor);
                visita.setDocumento(strNome);

                if (nome[2] != "") {
                    FormataCPF cpf = new FormataCPF();
                    CPF = cpf.FormataCPF(nome[2]);
                }
                tmData = null;
                try {
                    tmData = new Timestamp(simpleDateFormat.parse(strData));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                visita.setData(tmData);

                db = FirebaseFirestore.getInstance();
                CollectionReference colRef = db.collection("Usuarios").document(strBu).
                        collection("Clientes");
                colRef.whereEqualTo("cpf", CPF).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            i = i + 1;
                            ChamaBotao();
                        }else {
                            String idCliente="";
                            Double qtdVisitas=0.0;
                            String strVisita1, strVisita2, strVisita3, strNovaVisita;
                            strVisita1="";
                            strVisita2="";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idCliente = document.getId();
                                qtdVisitas = document.getDouble("qtdProd")+1;
                                strVisita1 = document.getString("visita1");
                                strVisita2 = document.getString("visita2");
                                strVisita3 = document.getString("visita3");
                            }
                            String strHoje = String.valueOf(DateFormat.format("dd-MM-yyyy", tmData.toDate()));
                            strNovaVisita = strHoje + " - " + strValor;
                            db.collection("Usuarios").document(strBu).
                                    collection("Clientes").document(idCliente).
                                    update("ultVisita", tmData,
                                            "visita1", strNovaVisita,
                                            "visita2", strVisita1,
                                            "visita3", strVisita2,
                                            "qtdProd", qtdVisitas.intValue(),
                                            "atualizado", Timestamp.now()
                                    );
                            db.collection("Usuarios").document(strBu).
                                    collection("Clientes").document(idCliente).
                                    collection("Visitas").document(strNome).set(visita);
                            i = i + 1;
                            ChamaBotao();
                        }
                    }
                });
            }else{
                i = i + 1;
                ChamaBotao();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Erro ao processar registro "+i, Toast.LENGTH_SHORT).show();

        }
    }

    public void ChamaBotao() {
        if (i<listalinhas.size()){
            progressBar.incrementProgressBy(1);
            btInserir.callOnClick();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Arquivo Processado", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        Uri uri = null;
        uri = resultData.getData();

        AssetManager assetManager = getResources().getAssets();
        try (
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(
                        new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Linhas linhas = new Linhas();
                linhas.setLinha(line);
                listalinhas.add(linhas);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Linhas linhas;
        linhas = listalinhas.get(0);
        String[] primeiroReg = linhas.getLinha().split("\",\"");

        if (primeiroReg[0].contains("Data de")){
            bArquivo = true;
        }

        txtSucesso.setText("Tamanho do arquivo "+listalinhas.size()+ " linhas");
        progressBar.setMax(listalinhas.size());

    }
}