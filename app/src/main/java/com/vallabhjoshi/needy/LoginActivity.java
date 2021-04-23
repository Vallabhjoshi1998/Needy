package com.vallabhjoshi.needy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonLogin;
    EditText emailEditText, editTextPassword;
    TextView BannerTextView, SignUpTextView,forgotPasswordTextView;
    ProgressBar LoginProgressBar;
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
                if(mFirebaseUser != null)
                {
                    Toasty.normal(LoginActivity.this,"You are logged in !",Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Toasty.normal(LoginActivity.this,"Please log in !",Toasty.LENGTH_SHORT).show();
                }
            }
        };

        SignUpTextView = (TextView) findViewById(R.id.SignUpTextView);
        String text = "Are you new? Register";
        SpannableString ss = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SignUpTextView.setText(ss);

        Window window = LoginActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.StatusBar));

        emailEditText = findViewById(R.id.EmailEditText);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        BannerTextView = findViewById(R.id.BannerTextView);
        forgotPasswordTextView = findViewById(R.id.ForgotPasswordTextView);
        LoginProgressBar = findViewById(R.id.SignInprogressBar);
        fAuth = FirebaseAuth.getInstance();

        BannerTextView.setVisibility(View.INVISIBLE);
        BannerTextView.postDelayed(new Runnable() {
            public void run() {
                BannerTextView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.buttonLogin:
                UserLogin();
                break;
            case R.id.SignUpTextView:
                SignUpMethod();
                break;
            case R.id.ForgotPasswordTextView:
                ForgotPassMethod();
                break;

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(mAuthStateListener);
    }

    private void ForgotPassMethod()
    {

        final EditText resetMail = new EditText(LoginActivity.this);
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(LoginActivity.this);
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // extract the email and send reset link
                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close the dialog
            }
        });

        passwordResetDialog.create().show();


    }

    private void SignUpMethod()
    {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void UserLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        try {
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is Required");
                return;
            }
            if (password.length() < 6) {
                editTextPassword.setError("Password must be greater or equal to 6 characters");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginProgressBar.setVisibility(View.VISIBLE);

        //Authenticate user
//        fAuth.signInWithEmailAndPassword(email, editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                    Intent inToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(inToMainActivity);
//                } else {
//                    Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    LoginProgressBar.setVisibility(View.GONE);
//                }
//
//            }
//        });

        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                Intent inToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(inToMainActivity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error !" +e.getMessage(), Toast.LENGTH_SHORT).show();
                LoginProgressBar.setVisibility(View.GONE);
            }
        });
    }
}