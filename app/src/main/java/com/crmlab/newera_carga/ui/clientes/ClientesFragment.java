package com.crmlab.newera_carga.ui.clientes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crmlab.newera_carga.R;
import com.crmlab.newera_carga.adapter.GestaoAdapter;
import com.crmlab.newera_carga.config.ConfiguracaoFirestore;
import com.crmlab.newera_carga.models.Gestao;
import com.crmlab.newera_carga.models.GlobalVariables;
import com.crmlab.newera_carga.utils.MaskEditUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientesFragment extends Fragment {


    private FirebaseFirestore db;
    private List<Gestao> listaGestao= new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private EditText edtStart, edtEnd, edtTelefone;
    private RecyclerView recyclerView;
    private ImageView imgStart, imgEnd;
    private Button btExecutar;
    private Switch swVendedor;
    private String strBu, strUser;

    DatePickerDialog picker;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clientes, container, false);


        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewResumo);
        edtStart = view.findViewById(R.id.editTextDateCliente);
        edtEnd = view.findViewById(R.id.editTextDateCliente2);
        imgStart = view.findViewById(R.id.imageViewCliente);
        imgEnd = view.findViewById(R.id.imageViewCliente2);

        btExecutar = view.findViewById(R.id.button);
        edtTelefone = view.findViewById(R.id.editTextTelefone);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        GlobalVariables mApp = (GlobalVariables) getActivity().getApplicationContext();
        strBu = mApp.getmGlobalVarValues();
        strUser = mApp.getmGlobalUser();

        edtTelefone.addTextChangedListener(MaskEditUtil.mask(edtTelefone, MaskEditUtil.FORMAT_FONE));

        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate("inicio");
            }
        });
        imgEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate("fim");
            }
        });
        btExecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEnd.getText().toString().isEmpty()&&!edtStart.getText().toString().isEmpty()) {
                    if (!edtStart.getText().toString().isEmpty()){
                        if(edtEnd.getText().toString().isEmpty()){
                            Toast.makeText(getContext(),"Preencher datas Inicio e Fim", Toast.LENGTH_LONG).show();
                        }else{
                            MontaRelatorio();
                        }
                    }else{
                        if (edtTelefone.getText().toString().isEmpty()){
                            Toast.makeText(getContext(),"Preencher telefone", Toast.LENGTH_LONG).show();
                        }else{
                            MontaRelatorio();
                        }
                    }

                }
            }
        });
    }

    public void PickDate (final String strCampo){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(this.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (strCampo == "inicio"){
                            edtStart.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);}
                        else{edtEnd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);}
                    }
                }, year, month, day);
        picker.show();

    }
    public void MontaRelatorio() {

        Timestamp tmStart = null, tmEnd=null;
        try {
            tmStart = new Timestamp(simpleDateFormat.parse(edtStart.getText().toString()));
            tmEnd = new Timestamp(simpleDateFormat.parse(edtEnd.getText().toString()));

        }catch (ParseException e) {
            e.printStackTrace();
        }

        db = ConfiguracaoFirestore.getFirebaseDb();
        CollectionReference colRef = db.collection("Usuarios").document(strBu).
                collection("Clientes");

        Query queryRef;
        if(!edtTelefone.getText().toString().isEmpty()){
            queryRef = colRef.whereEqualTo("telefone", edtTelefone.getText().toString());
        }else {
            queryRef = colRef.whereGreaterThanOrEqualTo("criado", tmStart).whereLessThanOrEqualTo("criado", tmEnd);
            queryRef = queryRef.orderBy("criado", Query.Direction.DESCENDING);
        }

        queryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Monta lista de oportunidades no period
                if (task.isSuccessful()) {
                    Gestao gestaoOportunidade;
                    listaGestao.clear();
                    String aux=null;
                    gestaoOportunidade = new Gestao();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        gestaoOportunidade = new Gestao();

                        gestaoOportunidade.setResponsavel(doc.getString("nome"));
                        gestaoOportunidade.setTxtColuna2(doc.getString("telefone"));
                        gestaoOportunidade.setTxtColuna3(doc.getString("categoria"));
                        if(doc.get("bMigrar")!=null){
                            if (doc.getBoolean("bMigrar")){aux = "Sim";}else{aux="Não";}}
                        else{aux="Não";}
                        gestaoOportunidade.setTxtColuna4("migrado : "+ aux);
                        if(doc.get("noCall")!=null){
                            if (doc.getBoolean("noCall")){aux = "Não";}else{aux="Sim";}}
                        else{aux="Sim";}
                        gestaoOportunidade.setTxtColuna5("contatar : "+ aux);
                        gestaoOportunidade.setTxtColuna6(doc.getString("cpf"));
                        gestaoOportunidade.setId(doc.getId());
                        listaGestao.add(gestaoOportunidade);
                    }

                    GestaoAdapter adapter = new GestaoAdapter(listaGestao);
                    recyclerView.setAdapter(adapter);
                }
            }
        });


    }

}