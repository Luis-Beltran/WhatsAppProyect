package com.example.whatsappproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUsername, getmTextInputEmail, getmTextInputPassword,getmTextInputConfirmPassword;
    Button btnRegister;
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
            if(isEmailValid(email))Toast.makeText(this, "Has ingresado todos los datos y el correno es valdio", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Has ingresado todos los datos pero el correno no es valdio :c", Toast.LENGTH_LONG).show();
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

}