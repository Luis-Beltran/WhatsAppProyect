package com.example.whatsappproyect.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.whatsappproyect.models.Comment;

public class CommentsProvider {

    CollectionReference mCollection;

    public CommentsProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment) {
        return mCollection.document().set(comment);
    }

}


