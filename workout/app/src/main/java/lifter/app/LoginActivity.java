package lifter.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "login";
    private EditText etEmail, etPassword;
    private Button login;
    private TextView register, title;
    private FirebaseAuth mAuth;
    private ProgressBar progress;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // condition false take it user on login form
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        register = (TextView) findViewById(R.id.register);
        title = (TextView) findViewById(R.id.title);

        login = (Button) findViewById(R.id.login);
        progress = (ProgressBar) findViewById(R.id.progress);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.login:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    loginProcess(email, password);

                }
                else {
                    Snackbar.make(title, "Fields are empty!", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    private void loginProcess(final String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();

                            Snackbar.make(title, "Login Successful", Snackbar.LENGTH_LONG).show();
                            Log.d(TAG, "Success!");

                            startService(getCurrentFocus());

                            sharedpreferences=getApplicationContext().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("LOGIN", email);
                            editor.apply();

                            startActivity(new Intent(LoginActivity.this, Sidebar.class));

                        } else {

                            Snackbar.make(title, task.getException().getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                            Log.d(TAG, "fail!");
                        }
                        progress.setVisibility(View.INVISIBLE);
                    }
                });
    }


    public void startService(View view){
        Intent i = new Intent(this, BackgroundService.class);
        startService(i);
    }
}
