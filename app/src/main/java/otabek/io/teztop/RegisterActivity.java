package otabek.io.teztop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText usernameEditText;
    String username;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEdittext);
        sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if (sharedPref.getString("username", "0") != "0") {
            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        editor = sharedPref.edit();


        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!usernameEditText.getText().toString().isEmpty()) {
                        username = usernameEditText.getText().toString();
                        editor.putString("username", username);
                        editor.commit();
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();


                        return true;
                    }
                }
                return false;
            }
        });


    }


}

