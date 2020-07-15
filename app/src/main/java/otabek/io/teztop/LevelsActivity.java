package otabek.io.teztop;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelsActivity extends AppCompatActivity {

    int level;
    LinearLayout levelsHolder;
    TextView secondsToStart;
    TextView getReadyText;
    CountDownTimer mCountDownTimer;

    public void setTimer() {
        mCountDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000) != 0) {

                    secondsToStart.setText(Long.toString(millisUntilFinished / 1000));

                } else {

                    secondsToStart.setText("GO");
                    Intent gameIntent = new Intent(LevelsActivity.this, GameActivity.class);
                    gameIntent.putExtra("level", level);
                    startActivity(gameIntent);
                    finish();

                }


            }

            @Override
            public void onFinish() {
//                secondsToStart.setText("GO");


//                timeLeft = 0;
            }
        }.start();
    }


    public void chooseLevel(View view) {
        switch (view.getId()) {
            case R.id.easyBtn:
                level = 1;
                break;
            case R.id.mediumBtn:
                level = 2;
                break;
            case R.id.hardBtn:
                level = 3;
                break;
//            default:
//                level = 0;
        }

        levelsHolder.setVisibility(View.INVISIBLE);
        getReadyText.setVisibility(View.VISIBLE);
        secondsToStart.setVisibility(View.VISIBLE);
        setTimer();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        levelsHolder = findViewById(R.id.levelsContainer);
        secondsToStart = findViewById(R.id.secondsToStartText);
        getReadyText = findViewById(R.id.getReadyText);

        secondsToStart.setVisibility(View.INVISIBLE);
        getReadyText.setVisibility(View.INVISIBLE);

    }
}
