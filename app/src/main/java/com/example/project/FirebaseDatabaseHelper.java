package com.example.project;

import androidx.annotation.NonNull;

import com.example.project.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceBooks;
    private List<User> users =new ArrayList<>();
    public interface DataStatus{
        void DataIsLoaded(List<User> users,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseDatabaseHelper(){
        mDatabase=FirebaseDatabase.getInstance();
        mReferenceBooks=mDatabase.getReference("users");
    }

    public void readUser(final DataStatus dataStatus){
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys=new ArrayList<>();
                for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    User user=keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataIsLoaded(users,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
