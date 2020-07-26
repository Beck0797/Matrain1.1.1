package otabek.io.teztop;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameActivity extends AppCompatActivity {


    //  Global variables
    private static final String TAG = "bos";
    public static Activity gameActivity;
    private TextView firstNum;
    private TextView secondNum;
    private TextView operatorSign;
    private TextView userAnswerTextView;
    private SeekBar timerSeekBar;
    private int level = 1;
    private Random random = new Random();
    private List<Integer> randomFourNumbers = new ArrayList<>();
    private int answer;
    private TextView scoreTextView;
    public static Boolean isSoundOn = true;
    private CountDownTimer mCountDownTimer;
    private int score = 0;
    private String userAnswer = "";
    private String problem = "";
    private int timeLeft = 20000;
    private MediaPlayer mediaPlayer;
    private TableLayout mTableLayout;

    public void numPressed(View view) {
        if (userAnswer.length() < 5) {

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
                            int userAnswerInt = Integer.parseInt(userAnswer);
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
        }


    }

    private void checkAnswer(int userAnswerInt) {


        if (userAnswerInt == answer) {
            if (level == 1) {
                score++;
            } else if (level == 2) {
                score += 2;
            } else if (level == 3) {
                score += 3;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.correct);
//            Log.i(TAG, "checkAnswer: correct");


            timeLeft += 2000;
            mCountDownTimer.cancel();
            setTimer();

            scoreTextView.setText(String.valueOf(score));
        } else {

            timeLeft -= 1000;
            mCountDownTimer.cancel();
            setTimer();
            mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
//            Log.i(TAG, "checkAnswer: wrong");
        }
        problemGenerator();
        if (isSoundOn) {
            mediaPlayer.start();
        }
    }


    public void setTimer() {
//        Log.i(TAG, "setTimer: called ");

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
        int operatorNumber;
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
                int hardNumber = random.nextInt((10 - 1) + 1) + 1;


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
                        secondNum.setText(String.valueOf(hardNumber));
                        operatorSign.setText("*");

                        answer = randomFourNumbers.get(0) * hardNumber;
                        break;


                    }

                    case 3: {
                        if (randomFourNumbers.get(0) % randomFourNumbers.get(1) == 0) {

                            firstNum.setText(num1);
                            secondNum.setText(num2);
                            operatorSign.setText("/");

                            answer = randomFourNumbers.get(0) / randomFourNumbers.get(1);

                        }

                        break;
                    }
                }
                break;

            }
            default:
                Log.i(TAG, "default problem generator");
                break;

        }

        randomFourNumbers.clear();
        problem = problem.concat("=");


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_layout);
//        Log.i(TAG, "time " + timeLeft);

        gameActivity = this;

        TextView pauseBtn = findViewById(R.id.pauseBtn);
        timerSeekBar = findViewById(R.id.timerSeekBar);
        TextView currentLevelText = findViewById(R.id.modeTextView);
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
            pauseTheGame();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pauseTheGame();
            return true;
        }
        return false;
    }

    private void pauseTheGame() {
        mCountDownTimer.cancel();
        Intent pauseIntent = new Intent(GameActivity.this, PauseActivity.class);
        pauseIntent.putExtra("score", score);
        pauseIntent.putExtra("timeLeft", timeLeft);
        pauseIntent.putExtra("sound", isSoundOn);
        startActivity(pauseIntent);
    }
}
