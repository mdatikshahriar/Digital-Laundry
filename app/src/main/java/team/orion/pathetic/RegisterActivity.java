package team.orion.pathetic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail,UserPassword,UserConfirmPassword;
    private Button CreateAccountButton;
    private TextView AlreadyHaveAccount;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        AlreadyHaveAccount = (TextView) findViewById(R.id.reguster_textview);
        UserEmail = (EditText) findViewById(R.id.register_mail);
        UserPassword = (EditText) findViewById(R.id.register_pass);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_pass);
        CreateAccountButton = (Button) findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);

        //when user has already an account but wanna go back for login
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        //when a user click's save button
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


    }

    //when a user is already logged in then we don't need to check anything again.So,we will directly
    //send the user to main activity!
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currenUser = mAuth.getCurrentUser();

        if(currenUser != null){
            SendUserToMainActivity();
        }

    }

    private void CreateNewAccount() {

        String email = UserEmail.getText().toString();
        String pass = UserPassword.getText().toString();
        String confirmpass = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email) ){
            UserEmail.requestFocus();
            UserEmail.setError("This Field is required to sign into your account!");
            return;
        }

        else if(TextUtils.isEmpty(pass) ){
            UserPassword.requestFocus();
            UserPassword.setError("This Field is required to secure your account");
            return;
        }

        else if (TextUtils.isEmpty(pass) ){
            UserConfirmPassword.requestFocus();
            UserConfirmPassword.setError("This Field is required to save your password!");
            return;

        }

        else if(!pass.equals(confirmpass)){
            UserConfirmPassword.requestFocus();
            UserConfirmPassword.setError("Your Passwords don't match!");
            return;

        }

        else{

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait,while we are creating your new Account..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                SendUserToSetupActivity();

                                Toast.makeText(RegisterActivity.this,"you are Authenticated successfully", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else{
                                String messege = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Attention: "+messege,Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                return;

                            }
                        }
                    });

        }



    }



    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }


}
