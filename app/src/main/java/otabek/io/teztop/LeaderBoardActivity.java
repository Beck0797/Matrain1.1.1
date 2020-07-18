package otabek.io.teztop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    //Global Variables

    private static final String TAG = "bos";
    FirebaseFirestore mFirestore;
    FirebaseAuth mFirebaseAuth;
    List<Score> mScoreList = new ArrayList<>();
    RecyclerView scoresRecyclerView;
    ScoreRecyclerAdapter mScoreRecyclerAdapter;
    AdRequest adRequest;
    ProgressBar mProgressBar;
    TextView loadingAd;
    private boolean reachedBottom;
    private boolean isFirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;
    private InterstitialAd mInterstitialAd;

    //Gets Ranking data from Firebase
    private void loadData() {

        Query firstQuery = mFirestore.collection("Scores").orderBy("score", Query.Direction.DESCENDING).limit(6);

        firstQuery.addSnapshotListener(LeaderBoardActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {

                    if (isFirstPageFirstLoad) {
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                        if (documentChange.getType() == DocumentChange.Type.ADDED) {

//                                Log.i(TAG, "size : " + queryDocumentSnapshots.size());


                            Score score = documentChange.getDocument().toObject(Score.class);

//                            if list is in first page, new item is added to the end else to the beginning of the list
                            if (isFirstPageFirstLoad) {

                                mScoreList.add(score);

                            } else {

                                mScoreList.add(0, score);

                            }

                            mScoreRecyclerAdapter.notifyDataSetChanged();

                        }


                    }

                    isFirstPageFirstLoad = false;

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadAd();
    }

    // Shows the Google Ad
    private void loadAd() {


        if (mInterstitialAd.isLoaded()) {

            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    loadData();
                }
            });
            mProgressBar.setVisibility(View.INVISIBLE);
            loadingAd.setVisibility(View.INVISIBLE);

        }


    }


    //    Checks whether use has Internet Connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        if (!isOnline()) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("In order to view Rankings, you need Internet connection. Do you want to turn on Wi-Fi")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setPositiveButton("Connect to Wi-fi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }).show();


        } else {


            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                    Log.i(TAG, "onInitializationComplete: " + initializationStatus.toString());
                }

            });

            mFirestore = FirebaseFirestore.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();

            scoresRecyclerView = findViewById(R.id.scoreRecyclerView);
            mProgressBar = findViewById(R.id.adProgressBar);
            loadingAd = findViewById(R.id.loadingAdText);

            mScoreRecyclerAdapter = new ScoreRecyclerAdapter(mScoreList);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

            adRequest = new AdRequest.Builder().build();

            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    mProgressBar.setVisibility(View.VISIBLE);

                    loadAd();
                }
            });

            mProgressBar.setIndeterminate(true);


            scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            scoresRecyclerView.setAdapter(mScoreRecyclerAdapter);


            //Check whether RecyclerView has reached bottom and if so loads more data from Firebase
            scoresRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
//                    Toast.makeText(LeaderBoardActivity.this,"Reached bottom",Toast.LENGTH_SHORT).show();
                        loadMore();
                    }
                }
            });


        }

    }


    //    Loads 6 more score item from Firebase
    private void loadMore() {

        Query nextQuery = mFirestore.collection("Scores").orderBy("score", Query.Direction.DESCENDING).startAfter(lastVisible).limit(6);

        nextQuery.addSnapshotListener(LeaderBoardActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (!queryDocumentSnapshots.isEmpty()) {


                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
//                                Log.i(TAG, "size : " + queryDocumentSnapshots.size());


                            Score score = documentChange.getDocument().toObject(Score.class);

                            mScoreList.add(score);
                            mScoreRecyclerAdapter.notifyDataSetChanged();

                        }
//                            Log.i(TAG, "onEvent: " + mSecretList.size());

                    }


                }
            }
        });


    }
}
