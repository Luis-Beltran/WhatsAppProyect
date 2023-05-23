package com.example.whatsappproyect.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsappproyect.R;
import com.example.whatsappproyect.models.User;
import com.example.whatsappproyect.providers.AuthProvider;
import com.example.whatsappproyect.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.UserDataHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUsername, getmTextInputEmail, getmTextInputPassword,getmTextInputConfirmPassword;
    Button btnRegister;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;
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

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere por favor")
                .setCancelable(false)
                .build();

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
        String password  = getmTextInputPassword.getText().toString();
        String confirmPassword = getmTextInputConfirmPassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(confirmPassword)) {
                    if (password.length() >= 6) {
                        createUser(username, email, password);
                    }
                    else {
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Las contraseña no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Insertaste todos los campos pero el correo no es valido", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

   private void createUser (final String username,final String email,String password) {
        mDialog.show();
       mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   String id = mAuthProvider.getUid();

                   User user = new User();
                   user.setId(id);
                   user.setEmail(email);
                   user.setUsername(username);

                   mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           mDialog.dismiss();
                           if (task.isSuccessful()) {
                               Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           } else {
                               Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               } else {
                   mDialog.dismiss();
                   Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
               }
           }
       });
   }
}