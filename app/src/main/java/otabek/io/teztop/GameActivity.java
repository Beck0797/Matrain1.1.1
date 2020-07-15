package otabek.io.teztop;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
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
    //    TextView problemPrompt;
    TextView scoreTextView;
    CountDownTimer mCountDownTimer;
    int timeLeft = 10000;
    int score = 0;
    String userAnswer = "";
    String problem = "";
    int userAnswerInt;


    public void numPressed(View view) {

        switch (view.getId()) {
            case R.id.minusSign: {
                userAnswer = userAnswer.concat("-");
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
                }

            }
            case R.id.okBtn: {
                if (userAnswer != null && !userAnswer.isEmpty()) {
                    userAnswerInt = Integer.parseInt(userAnswer);
                    userAnswerTextView.setText("");
                    userAnswer = "";
                    checkAnswer(userAnswerInt);
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
        Log.i(TAG, "numPressed: " + problem);
//        userAnswer = "";


    }

    private void checkAnswer(int userAnswerInt) {
        Log.i(TAG, "checkAnswer: " + userAnswerInt);
        if (userAnswerInt == answer) {
//            Log.i(TAG, "checkAnswer: correct");
            score++;
            timeLeft += 3000;
            mCountDownTimer.cancel();
            setTimer();

            scoreTextView.setText(String.valueOf(score));
            problemGenerator();
        } else {
//            Log.i(TAG, "checkAnswer: wrong");
            problemGenerator();
        }

    }


    public void setTimer() {
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
                score = 0;

                Intent finishIntent = new Intent(GameActivity.this, FinishActivity.class);
                finishIntent.putExtra("score", score);
                startActivity(finishIntent);
                finish();
//                timeLeft = 0;
            }
        }.start();
    }


//    public void operatorGenerator() {
//        switch (level){
//
//            case 1: operatorNumber = random.nextInt(1);
//            case 2: operatorNumber = random.nextInt(2);
//            case 3: operatorNumber = random.nextInt(4);
//            default: operatorNumber = random.nextInt(1);
//
//        }
//        problemGenerator();
//    }

    public void problemGenerator() {


        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        randomFourNumbers.add(random.nextInt((99) + 1) + 1);
        String num1 = randomFourNumbers.get(0).toString();
        String num2 = randomFourNumbers.get(1).toString();
        switch (level) {

            case 1: {

                Log.i(TAG, "level 1 ");

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
//        Log.i(TAG, "problemGenerator: " +problem);
//        problemPrompt.setText(problem);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_layout);


//        problemPrompt = findViewById(R.id.problemPrompt);
        pauseBtn = findViewById(R.id.pauseBtn);
        timerSeekBar = findViewById(R.id.timerSeekBar);
        currentLevelText = findViewById(R.id.modeTextView);
        scoreTextView = findViewById(R.id.scoreTextview);
        firstNum = findViewById(R.id.firstNumber);
        secondNum = findViewById(R.id.secondNumber);
        operatorSign = findViewById(R.id.operatorSign);
        userAnswerTextView = findViewById(R.id.userAnswer);

        level = getIntent().getIntExtra("level", 1);

        timerSeekBar.setEnabled(false);
        Log.i(TAG, "level " + level);
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
            startActivity(pauseIntent);
        });

//        Log.i(TAG, "onCreate: "+level);


//       problemGenerator();
        setTimer();


    }


}
