package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Setting extends AppCompatActivity {
    TextView fname,email,sstetuse;
    ImageButton B ;

    TextView forgetPassword ,Hname;
    FirebaseUser user;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        fname = findViewById(R.id.name);
        sstetuse = findViewById(R.id.stetuse);




        email = findViewById(R.id.email);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
//        forgetPassword = findViewById(R.id.forgetPassword);





        DocumentReference deDocumentReference = fStore.collection("users").document(UserID);
        deDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fname.setText(documentSnapshot.getString("name"));
                sstetuse.setText(documentSnapshot.getString("stetuse"));
                String Image = documentSnapshot.getString("email");

            }
        });



        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final EditText ResetPassword = new EditText(v.getContext());


                final AlertDialog.Builder Forget_Passowrd = new AlertDialog.Builder(v.getContext());
                Forget_Passowrd.setTitle("Reset Password");
                Forget_Passowrd.setMessage("Enter Your  New Password >6 char");
                Forget_Passowrd.setView(ResetPassword);


                Forget_Passowrd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String newPassword = ResetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Setting.this, "Password Reset Succcessfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Setting.this, "Password Filed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Forget_Passowrd.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                    }
                });
                Forget_Passowrd.create().show();

            }
        });





    }
}
