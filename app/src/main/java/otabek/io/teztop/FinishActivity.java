package otabek.io.teztop;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class FinishActivity extends AppCompatActivity {


    private static final String TAG = "bos";
    private static File file;
    int level;
    FirebaseFirestore mFirestore;
    Bitmap img;
    String levelString;
    private Button playAgainBtn;
    private TextView finishScoreView;
    String username;
    int height;
    int width;
    private Button shareScore;
    private int score;

    //    Gets screenshot of the current view and returns Bitmap of it
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    //    Checks whether the user has the Internet connection
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    //    saves screenshot image once the user allows storage permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                img = getScreenShot(rootView);

                saveImage(img, "matrain");
//                    shareImage(file);

            } else {

                Toast.makeText(FinishActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //    Makes a global intent to share the screenshot image
    private void shareImage(File file) {

        Uri uri = Uri.fromFile(file);
        Uri uriForFile = FileProvider.getUriForFile(FinishActivity.this, getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/jpeg");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Tez Top score");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, String.valueOf(score));
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(FinishActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    //    saves screenshot image to internal storage
    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "/Image-" + image_name + ".jpg";
        file = new File(myDir, fname);

        if (file.exists()) {

            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        playAgainBtn = findViewById(R.id.playAgainBtn);
        finishScoreView = findViewById(R.id.finishScoreTextView);
        shareScore = findViewById(R.id.shareScoreBtn);

        mFirestore = FirebaseFirestore.getInstance();

        if (MainActivity.username != null && !MainActivity.username.isEmpty()) {
            username = MainActivity.username;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        level = getIntent().getIntExtra("level", 0);
        score = getIntent().getIntExtra("score", 0);


        final KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);
        if (score >= 15) {
            viewKonfetti.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition((height / 2f) - 200, (width / 2f) - (100))
                    .burst(500);
        }


        switch (level) {

            case 1: {
                levelString = "Easy";
                break;
            }
            case 2: {
                levelString = "Medium";
                break;
            }

            case 3: {

                levelString = "Hard";
                break;
            }
            default: {
                levelString = "Unknown";
                break;
            }

        }


        finishScoreView.setText("Your score is: " + score);


        shareScore.setOnClickListener((View v) -> {
            if (score > 5) {
                askToShare();

                if (ContextCompat.checkSelfPermission(
                        FinishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                    img = getScreenShot(rootView);
                    // You can use the API that requires the permission.
                    saveImage(img, "matrain");
                    shareImage(file);
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
//                    ActivityCompat.requestPermissions(FinishActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
//                saveImage(img,"ok");
//

            } else {
                Toast.makeText(FinishActivity.this, "Sorry, Only scores above 5 can be shared. Try harder", Toast.LENGTH_LONG).show();

            }
        });


        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGameIntent = new Intent(FinishActivity.this, LevelsActivity.class);
                startActivity(newGameIntent);
                finish();
            }
        });

    }

    private void askToShare() {
        Log.i(TAG, "askToShare: ");

        if (isOnline()) {

            Map<String, Object> scoreMap = new HashMap<>();
            scoreMap.put("username", username);
            scoreMap.put("score", score);
            scoreMap.put("level", levelString);


            mFirestore.collection("Scores").document().set(scoreMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(FinishActivity.this, "Your score has been shared with the world", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: " + e.getMessage());
                }
            });


        } else {
            Log.i(TAG, "not online");
        }
    }


}
