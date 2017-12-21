package roshaan.mapapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    EditText email;
    EditText password;
    EditText fullName;
    String emailString, passwordString, fullNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.signupEmail);
        password = (EditText) findViewById(R.id.signupPassword);
        fullName = (EditText) findViewById(R.id.signupFullName);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private boolean validateForm(EditText email, EditText password, EditText fullName) {

        Boolean valid = true;

        emailString = String.valueOf(email.getText());
        passwordString = String.valueOf(password.getText());
        fullNameString = String.valueOf(fullName.getText());

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

        if (TextUtils.isEmpty(passwordString)) {
            fullName.setError("Required.");
            valid = false;
        } else {
            fullName.setError(null);
        }

        return valid;
    }

    void signUp(EditText email, EditText password, EditText fullName) {

        if (validateForm(email, password, fullName)) {

            mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            mDatabaseReference = mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            mDatabaseReference.child("name").setValue(fullNameString);

                            startActivity(new Intent(Signup.this, MapActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Signup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    public void signUpClicked(View v) {

        signUp(email, password, fullName);

    }

    public void openLogin(View v) {

        startActivity(new Intent(this, Login.class));
        finish();
    }

}
