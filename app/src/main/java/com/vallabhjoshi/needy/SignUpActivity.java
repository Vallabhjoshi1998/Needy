package com.vallabhjoshi.needy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {

    TextView ReturnToLoginTextView;
    Button SignUpFinal;
    EditText FirstName, LastName, Email, Password, ReEnterPassword;
    ProgressBar SignUpProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ReturnToLoginTextView = (TextView) findViewById(R.id.ReturnToLoginTextView);
        String text = "Already a User? Login";
        SpannableString ss = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 16, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ReturnToLoginTextView.setText(ss);

        FirstName = findViewById(R.id.FirstNameEditText);
        LastName = findViewById(R.id.LastNameEditText);
        Email = findViewById(R.id.EmailEditText);
        Password = findViewById(R.id.EmailEditText);
        ReEnterPassword = findViewById(R.id.ReEnterPassEditText);
        SignUpFinal = findViewById(R.id.buttonSignUpFinal);

        SignUpProgressBar = findViewById(R.id.SignInprogressBar);
        fAuth = FirebaseAuth.getInstance();

        // Check if user is already signed up and Signed In we have to directly offer user to MainActivity..
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        SignUpFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String re_enterPassword = ReEnterPassword.getText().toString().trim();
                Boolean temp = true;

                try {
                    if (TextUtils.isEmpty(email)) {
                        Email.setError("Email is Required");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Password.setError("Password is Required");
                        return;
                    }
                    if (password.length() < 6) {
                        Password.setError("Password must be greater or equal to 6 characters");
                        return;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

//                try {
//                    SignUpProgressBar.setVisibility(View.VISIBLE);
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }

                //Now register the user in the Firebase.....

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()) {
                            Toasty.normal(SignUpActivity.this,"User Created",Toasty.LENGTH_SHORT).show();
                            Toasty.normal(SignUpActivity.this,"Please reset your password after logout and that will be your final password!",Toasty.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            Toasty.normal(getApplicationContext(),"Some Error is Occurred !"+ task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }



    public void ReturnLogin(View view)
    {
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}