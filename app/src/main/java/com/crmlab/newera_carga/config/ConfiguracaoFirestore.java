package com.crmlab.newera_carga.config;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConfiguracaoFirestore {
    private static FirebaseFirestore db;

    public  static FirebaseFirestore getFirebaseDb() {
        if (db == null) {
            db = FirebaseFirestore.getInstance().getInstance();
        }
        return db;
    }
}
