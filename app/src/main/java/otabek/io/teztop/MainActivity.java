package otabek.io.teztop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "bos";
    Intent intent;
    Button newGameButton;
    Button leaderBoardButton;
    Button settingsButton;
    Button exitButton;
    public static String username;
    TextView usernameLogoText;
//    Boolean  isFirstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState !=null){
//            isFirstTime = savedInstanceState.getBoolean("isFirst");
//            Log.i(TAG, "saved instanec "+isFirstTime);
//        }
//
//        ArrayList<Score> scoreArraylist = new ArrayList<Score>();
//
//        Log.i(TAG, "isfirdt "+isFirstTime);
        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
//
        username = sharedPref.getString("username", "none");
        if (username.equals("none")) {
            callRegActivity();

        }
//        if (isFirstTime == null) {
//            Log.i(TAG, "first time: ");
//            isFirstTime = false;
//            Score currentScore = new Score("username", "Easy", 15);
//
//            scoreArraylist.add(currentScore);
//            try {
//                String serialized = ObjectSerializer.serialize(scoreArraylist);
//                sharedPref.edit().putString("scores", serialized).apply();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


//        Log.i(TAG, "username " + username);


        newGameButton = findViewById(R.id.newGameButton);
        leaderBoardButton = findViewById(R.id.leaderBoardButton);
        settingsButton = findViewById(R.id.settingsButton);
        exitButton = findViewById(R.id.exitButton);
        usernameLogoText = findViewById(R.id.usernameLogoText);

        usernameLogoText.setText(getString(R.string.username_text).concat(username));
        usernameLogoText.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, R.string.edit_username_message, Toast.LENGTH_SHORT).show();

        });

        usernameLogoText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callRegActivity();
//                sharedPref.edit().remove("username").commit();
                return true;
            }
        });

        newGameButton.setOnClickListener(this);
        leaderBoardButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

    }

    private void callRegActivity() {
        Intent regIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(regIntent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newGameButton:
                intent = new Intent(this, LevelsActivity.class);
                break;

            case R.id.leaderBoardButton:
                intent = new Intent(this, ChooseRankingType.class);
                break;

            case R.id.settingsButton:
                intent = new Intent(this, AboutActivity.class);
                break;

            case R.id.exitButton:
                finish();
                finishAffinity();
                System.exit(0);
                break;

        }

        startActivity(intent);
//        finish();
//        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            finishAffinity();
            System.exit(0);
            return true;
        }
        return false;
    }
}
