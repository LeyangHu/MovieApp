package com.example.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class WaitingActivity extends AppCompatActivity {

    private DocumentReference docRef;
    private FirebaseFirestore db;
    private String roomName;
    private boolean isSwiping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Intent intent = getIntent();
        roomName = intent.getStringExtra(NewRoomActivity.ROOM_TAG);

        db = FirebaseFirestore.getInstance();
        docRef = db.collection("rooms").document(roomName); //we're referencing the roomName in the rooms collection on firestore
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) { //so we want to constantly be updating the number of activeMembers
                    try {
                        Object b = snapshot.get("isSwiping");
                        if(Boolean.parseBoolean(b.toString())) {
                            startSwipingActivity();
                        }
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.toString());
                    }

                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
        //

    }

    private void startSwipingActivity() {
        Intent intent = new Intent(this, SwipingActivity.class);
        startActivity(intent);
    }
}