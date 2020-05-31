package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText Fname,Email,password,stetuse;
    Button Done;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Fname=findViewById(R.id.EName);
        stetuse=findViewById(R.id.stetusef);
        Email=findViewById(R.id.email);
        password=findViewById(R.id.Passwordbtn);
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        Done=findViewById(R.id.button);




        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=Email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                final String name=Fname.getText().toString();
                final String Ste=stetuse.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "you must Enter name!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                 if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Require");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("password is Require");
                    return;
                }
                if(pass.length()<6 ){
                    password.setError("password  Must be >=6 char");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            UserID =fAuth.getCurrentUser().getUid();
                            DocumentReference deDocumentReference=fStore.collection("users").document(UserID);

                            Map<String, Object> user = new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("stetuse",Ste);
                            deDocumentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","OnSuccess user profel is Create For" +UserID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","OnFiluer" +e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(Register.this, "Error The email oared Created", Toast.LENGTH_SHORT).show();
                        }}}); } });



    }
}
