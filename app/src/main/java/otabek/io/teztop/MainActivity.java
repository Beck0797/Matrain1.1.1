package otabek.io.teztop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "bos";
    Intent intent;
    Button newGameButton;
    Button leaderBoardButton;
    Button settingsButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "none");


        Log.i(TAG, "username " + username);


        newGameButton = findViewById(R.id.newGameButton);
        leaderBoardButton = findViewById(R.id.leaderBoardButton);
        settingsButton = findViewById(R.id.settingsButton);
        exitButton = findViewById(R.id.exitButton);

        newGameButton.setOnClickListener(this);
        leaderBoardButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newGameButton:
                intent = new Intent(this, LevelsActivity.class);
                break;

            case R.id.leaderBoardButton:
                intent = new Intent(this, LeaderBoardActivity.class);
                break;

            case R.id.settingsButton:
                intent = new Intent(this, SettingsActivity.class);
                break;

            case R.id.exitButton:
                this.finish();
                System.exit(0);
                break;

        }
        startActivity(intent);
    }
}
