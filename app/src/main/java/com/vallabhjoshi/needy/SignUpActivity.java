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
import android.util.Log;
import android.util.Patterns;
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

    TextView ReturnToLoginTextView, BannerTextView;
    Button SignUpFinal;
    EditText FirstName, LastName, Email, Password, ReEnterPassword;
    ProgressBar SignUpProgressBar;
    FirebaseAuth fAuth;
    boolean isNameValid, isEmailValid, isPasswordValid;

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

        FirstName = (EditText) findViewById(R.id.FirstNameEditText);
        LastName =(EditText) findViewById(R.id.LastNameEditText);
        Email = (EditText) findViewById(R.id.EmailEditText);
        Password =(EditText) findViewById(R.id.EnterPassEditText);
        ReEnterPassword =(EditText) findViewById(R.id.ReEnterPassEditText);
        SignUpFinal =(Button) findViewById(R.id.buttonSignUpFinal);
        BannerTextView = (TextView) findViewById(R.id.BannerTextView);

        BannerTextView.setVisibility(View.INVISIBLE);
        BannerTextView.postDelayed(new Runnable()
        {
            public void run() {
                BannerTextView.setVisibility(View.VISIBLE);
            }
        }, 800);

        SignUpProgressBar =(ProgressBar) findViewById(R.id.SignInprogressBar);
        fAuth = FirebaseAuth.getInstance();

        // Check if user is already signed up and Signed In we have to directly offer user to MainActivity..
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
            SignUpFinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validation();
                }
            });

        BannerTextView.setVisibility(View.INVISIBLE);
        BannerTextView.postDelayed(new Runnable()
        {
            public void run() {
                BannerTextView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }



    private void validation()
    {
        // Check for a valid name.
        if (FirstName.getText().toString().isEmpty()) {
            FirstName.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }

        // Check for a valid email address.
        if (Email.getText().toString().isEmpty()) {
            Email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
            Email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid password.
        if (Password.getText().toString().isEmpty()) {
            Password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (Password.getText().length() < 6) {
            Password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else if (!Password.getText().toString().equals(ReEnterPassword.getText().toString())) {
            ReEnterPassword.setError(getResources().getString(R.string.re_enter_password));
            isPasswordValid = false;
        }
        else {
            isPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPasswordValid) {
            fAuth.createUserWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toasty.normal(SignUpActivity.this, "User Created", Toasty.LENGTH_SHORT).show();
                        Toasty.normal(SignUpActivity.this, "Please reset your password after logout and that will be your final password!", Toasty.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toasty.normal(getApplicationContext(), "Some Error is Occurred !" + task.getException().getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void ReturnLogin(View view)
    {
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}