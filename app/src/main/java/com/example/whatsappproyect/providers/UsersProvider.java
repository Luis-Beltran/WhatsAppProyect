package com.example.whatsappproyect.providers;

import com.example.whatsappproyect.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProvider {
    private CollectionReference mCollecion;

    public UsersProvider(){
        mCollecion = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return  mCollecion.document(id).get();
    }

    public Task<Void> create(User user){
        return mCollecion.document(user.getId()).set(user);
    }
    public Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        return   mCollecion.document(user.getId()).update(map);
    }
}
