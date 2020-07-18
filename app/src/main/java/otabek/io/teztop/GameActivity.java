package otabek.io.teztop;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameActivity extends AppCompatActivity {


    private static final String TAG = "bos";
    TextView firstNum;
    TextView secondNum;
    TextView operatorSign;
    TextView pauseBtn;
    TextView userAnswerTextView;
    SeekBar timerSeekBar;
    int level = 1;
    TextView currentLevelText;
    Random random = new Random();
    List<Integer> randomFourNumbers = new ArrayList<>();
    int operatorNumber;
    int answer;
    TextView scoreTextView;
    CountDownTimer mCountDownTimer;
    public static Boolean isSoundOn = true;
    int score = 0;
    String userAnswer = "";
    String problem = "";
    int userAnswerInt;
    int timeLeft = 20000;
    boolean isTimerWorking = false;
    MediaPlayer mediaPlayer;
    TableLayout mTableLayout;



    public void numPressed(View view) {

        switch (view.getId()) {
            case R.id.minusSign: {
                if (userAnswer.length() == 0) {
                    userAnswer = userAnswer.concat("-");

                }
                break;
            }

            case R.id.oneNum: {
                userAnswer = userAnswer.concat("1");
                break;
            }
            case R.id.twoNum: {
                userAnswer = userAnswer.concat("2");
                break;
            }
            case R.id.threeNum: {
                userAnswer = userAnswer.concat("3");
                break;
            }
            case R.id.fourNum: {
                userAnswer = userAnswer.concat("4");
                break;
            }
            case R.id.fiveNum: {
                userAnswer = userAnswer.concat("5");
                break;
            }
            case R.id.sixNum: {
                userAnswer = userAnswer.concat("6");
                break;
            }
            case R.id.sevenNum: {
                userAnswer = userAnswer.concat("7");
                break;
            }
            case R.id.eightNum: {
                userAnswer = userAnswer.concat("8");
                break;
            }
            case R.id.nineNum: {
                userAnswer = userAnswer.concat("9");
                break;
            }
            case R.id.zeroNum: {
                userAnswer = userAnswer.concat("0");
                break;
            }
            case R.id.delBtn: {
                if (userAnswer != null && !userAnswer.isEmpty()) {
                    userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                    break;
                }
                break;


            }
            case R.id.okBtn: {
                if (userAnswer != null && !userAnswer.isEmpty()) {
                    if (userAnswer.length() < 7) {
                        userAnswerInt = Integer.parseInt(userAnswer);
                        userAnswerTextView.setText("");
                        userAnswer = "";
                        checkAnswer(userAnswerInt);
                    }
                }


                break;
            }

            default:
                Log.i(TAG, "default");
                break;
        }

        userAnswerTextView.setText(userAnswer);
//        problem = problem.concat(userAnswer);
//        problemPrompt.setText(problem);
//        Log.i(TAG, "numPressed: " + problem);
//        userAnswer = "";


    }

    private void checkAnswer(int userAnswerInt) {
//        Log.i(TAG, "checkAnswer: " + userAnswerInt);
        if (userAnswerInt == answer) {

            mediaPlayer = MediaPlayer.create(this, R.raw.correct);
//            Log.i(TAG, "checkAnswer: correct");
            score++;

            timeLeft += 2000;
            mCountDownTimer.cancel();
            setTimer();

            scoreTextView.setText(String.valueOf(score));
            problemGenerator();
        } else {

            timeLeft -= 1000;
            mCountDownTimer.cancel();
            setTimer();
            mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
//            Log.i(TAG, "checkAnswer: wrong");
            problemGenerator();
        }
        if (isSoundOn) {
            mediaPlayer.start();
        }
    }


    public void setTimer() {
//        Log.i(TAG, "setTimer: called ");
        isTimerWorking = true;
        mCountDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerSeekBar.setProgress((int) (millisUntilFinished / 100));
                timeLeft = (int) (millisUntilFinished);

            }

            @Override
            public void onFinish() {
                timerSeekBar.setProgress(0);
                problem = "";
                userAnswer = "";

                isTimerWorking = false;
                if (isSoundOn) {
                    mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.complete);
                    mediaPlayer.start();
                }
                Intent finishIntent = new Intent(GameActivity.this, FinishActivity.class);
                finishIntent.putExtra("score", score);
                finishIntent.putExtra("level", level);

                startActivity(finishIntent);
                score = 0;
                finish();
//                timeLeft = 0;
            }
        }.start();
    }


    public void problemGenerator() {


        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        String num1 = randomFourNumbers.get(0).toString();
        String num2 = randomFourNumbers.get(1).toString();
        switch (level) {

            case 1: {


                firstNum.setText(num1);
                secondNum.setText(num2);
                operatorSign.setText("+");
                answer = randomFourNumbers.get(0) + randomFourNumbers.get(1);
                break;
            }

            case 2: {

                operatorNumber = random.nextInt(2);
                switch (operatorNumber) {
                    case 0: {

                        firstNum.setText(num1);
                        secondNum.setText(num2);
                        operatorSign.setText("+");
                        answer = randomFourNumbers.get(0) + randomFourNumbers.get(1);

                    }

                    case 1: {
                        firstNum.setText(num1);
                        secondNum.setText(num2);
                        operatorSign.setText("-");
                        answer = randomFourNumbers.get(0) - randomFourNumbers.get(1);
                    }
                }
                break;

            }
            case 3: {

                operatorNumber = random.nextInt(4);
                switch (operatorNumber) {
                    case 0: {

                        firstNum.setText(num1);
                        secondNum.setText(num2);
                        operatorSign.setText("+");
                        answer = randomFourNumbers.get(0) + randomFourNumbers.get(1);
                        break;

                    }

                    case 1: {
                        firstNum.setText(num1);
                        secondNum.setText(num2);
                        operatorSign.setText("-");
                        answer = randomFourNumbers.get(0) - randomFourNumbers.get(1);
                        break;
                    }

                    case 2: {

                        firstNum.setText(num1);
                        secondNum.setText(num2);
                        operatorSign.setText("*");
                        answer = randomFourNumbers.get(0) * randomFourNumbers.get(1);
                        break;

                    }

                }
                break;

            }
            default:
                operatorNumber = random.nextInt(1);
                Log.i(TAG, "default problem generator");
                break;

        }

        randomFourNumbers.clear();
        problem = problem.concat("=");



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_layout);
//        Log.i(TAG, "time " + timeLeft);

        pauseBtn = findViewById(R.id.pauseBtn);
        timerSeekBar = findViewById(R.id.timerSeekBar);
        currentLevelText = findViewById(R.id.modeTextView);
        scoreTextView = findViewById(R.id.scoreTextview);
        firstNum = findViewById(R.id.firstNumber);
        secondNum = findViewById(R.id.secondNumber);
        operatorSign = findViewById(R.id.operatorSign);
        userAnswerTextView = findViewById(R.id.userAnswer);
        mTableLayout = findViewById(R.id.tableLayout);

        level = getIntent().getIntExtra("level", 1);

        timerSeekBar.setEnabled(false);

        switch (level) {

            case 1: {
                currentLevelText.setText("Mode: Easy");
                break;
            }
            case 2: {
                currentLevelText.setText("Mode: Medium");
                break;
            }

            case 3: {
                Log.i(TAG, "3rd level");
                currentLevelText.setText("Mode: Hard");
                break;
            }
            default: {
                currentLevelText.setText("Mode: Unknown");
                break;
            }

        }

        problemGenerator();


        pauseBtn.setOnClickListener(v -> {
            mCountDownTimer.cancel();

            Intent pauseIntent = new Intent(GameActivity.this, PauseActivity.class);
            pauseIntent.putExtra("score", score);
            pauseIntent.putExtra("timeLeft", timeLeft);
            pauseIntent.putExtra("sound", isSoundOn);
            startActivity(pauseIntent);
        });


        problemGenerator();
//        setTimer();


    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.i(TAG, "onPause: ");
        mCountDownTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSoundOn) {
            mTableLayout.setSoundEffectsEnabled(false);
            muteAll();
        }
        Log.i(TAG, "onResume: " + isSoundOn);
//        if (!isTimerWorking){
        setTimer();
        problemGenerator();

    }

    private void muteAll() {
        Log.i(TAG, "muteAll: " + mTableLayout.getChildCount());
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            for (int j = 0; j < ((TableRow) mTableLayout.getChildAt(i)).getChildCount(); j++) {
                ((TableRow) mTableLayout.getChildAt(i)).getChildAt(j).setSoundEffectsEnabled(false);
            }

        }
    }
}
