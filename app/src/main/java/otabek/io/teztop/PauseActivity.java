package otabek.io.teztop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity {

    Button resumeBtn;
    Button restartBtn;
    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        resumeBtn = findViewById(R.id.resumeBtn);
        restartBtn = findViewById(R.id.restartBtn);
        exitBtn = findViewById(R.id.exitBtn);

        resumeBtn.setOnClickListener(v -> {
            Intent gameIntent = new Intent(PauseActivity.this, GameActivity.class);
            startActivity(gameIntent);
        });

        restartBtn.setOnClickListener(v -> {
            Intent levelIntent = new Intent(PauseActivity.this, LevelsActivity.class);
            startActivity(levelIntent);
        });

        exitBtn.setOnClickListener(v -> {
            finish();
            finishAffinity();
        });


    }
}
