package roshaan.mapapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText email;
    EditText password;
    String emailString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);

        mAuth = FirebaseAuth.getInstance();

    }

    private void login(EditText email, EditText password) {

        if (validateForm(email, password)) {
            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        startActivity(new Intent(Login.this, MapActivity.class));
                        finish();

                    }
                    else {
                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private boolean validateForm(EditText email, EditText password) {

        Boolean valid = true;

        emailString = String.valueOf(email.getText());
        passwordString = String.valueOf(password.getText());

        if (TextUtils.isEmpty(emailString)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void loginClicked(View v) {

        login(email, password);

    }

    public void openSignup(View v) {

        startActivity(new Intent(this, Signup.class));
        finish();
    }

}
