package com.katalog.kozmetik.catalog.registiration;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private EditText edtName, edtSurname, edtEmail, edtPassword;
    private Button btnRegister;
    private String name, surname, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edtName = findViewById(R.id.edtNameReg);
        edtSurname = findViewById(R.id.edtSurnameReg);
        edtEmail = findViewById(R.id.edtEmailReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        btnRegister = findViewById(R.id.btnRegisterReg);

        setupButtons();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    private void setupButtons() {
       /* btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTexts();

                getApp().mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                   // Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = getApp().mAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.
                                 //   Log.d(TAG, "createUserWithEmail:success");
                                    System.out.println("asd");

                                }
                            }
                        });
            }
        });*/
    }


    private void getTexts() {
        name = edtName.getText().toString();
        surname = edtSurname.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
    }

}

