package com.example.whatsappproyect.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsappproyect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUsername, getmTextInputEmail, getmTextInputPassword,getmTextInputConfirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mcircleImageViewBack = findViewById(R.id.circleImageBack);

        mTextInputUsername = findViewById(R.id.textInputUser);
        getmTextInputEmail = findViewById(R.id.textInputEmail);
        getmTextInputPassword = findViewById(R.id.textInputPassword);
        getmTextInputConfirmPassword = findViewById(R.id.textInputPasswordConfirmar);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void register(){
        String username = mTextInputUsername.getText().toString();
        String email = getmTextInputEmail.getText().toString();
        String password = getmTextInputPassword.getText().toString();
        String confirmpassword = getmTextInputConfirmPassword.getText().toString();

        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmpassword.isEmpty()){
            if(isEmailValid(email)){
                if(password.equals(confirmpassword)){
                    if (password.length() >= 6){
                        createUser(username,email,password);
                    }else {
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(this, "Datos bien capturados", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Has ingresado todos los datos pero el correno no es valdio :c", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Ingresa todos los datos para continuar :c", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

   private void createUser (final String username,final String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username",username);
                    map.put("email",email);

                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Usuario almacenado en la base de datos :)", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Usuario NO almacenado en la base de datos :/", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario :c", Toast.LENGTH_LONG).show();
                }
            }
        });
   }

}