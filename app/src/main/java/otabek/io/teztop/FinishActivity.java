package otabek.io.teztop;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class FinishActivity extends AppCompatActivity {


    private static final String TAG = "bos";
    private static File file;
    Button shareScore;
    int score;
    Bitmap img;
    private Button playAgainBtn;
    private TextView finishScoreView;

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage(img, "matrain");
                    shareImage(file);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(FinishActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

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

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "/Image-" + image_name + ".jpg";
        file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "saveImage: " + file.getPath());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        playAgainBtn = findViewById(R.id.playAgainBtn);
        finishScoreView = findViewById(R.id.finishScoreTextView);
        shareScore = findViewById(R.id.shareScoreBtn);

        score = getIntent().getIntExtra("score", 0);

        finishScoreView.setText("Your score is: " + score);

//        ActivityCompat.requestPermissions(FinishActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
//        ActivityCompat.requestPermissions(FinishActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        shareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                img = getScreenShot(rootView);
                if (ContextCompat.checkSelfPermission(
                        FinishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    saveImage(img, "matrain");
                    shareImage(file);
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    ActivityCompat.requestPermissions(FinishActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
//                saveImage(img,"ok");
//

            }
        });


        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGameIntent = new Intent(FinishActivity.this, GameActivity.class);
                startActivity(newGameIntent);
                finish();
            }
        });

    }

}
