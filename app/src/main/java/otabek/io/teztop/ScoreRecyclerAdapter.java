package otabek.io.teztop;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreRecyclerAdapter extends RecyclerView.Adapter<ScoreRecyclerAdapter.ViewHolder> {

    private static final String TAG = "bos";
    public List<Score> scoreList;

    public ScoreRecyclerAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }


    @NonNull
    @Override
    public ScoreRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_score_view, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreRecyclerAdapter.ViewHolder holder, int position) {

        String username = scoreList.get(position).getUsername();
        int score = scoreList.get(position).getScore();
        String level = scoreList.get(position).getLevel();

//        Log.i(TAG, "onBindViewHolder: "+scoreList.get(position).toString());


        holder.setViewData(username, score, level);

    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView usernameTextView;
        public TextView levelTextView;
        public TextView scoreTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setViewData(String username, int score, String level) {
            usernameTextView = mView.findViewById(R.id.usernameTextRanking);
            levelTextView = mView.findViewById(R.id.levelTextRaning);
            scoreTextView = mView.findViewById(R.id.scoreTextRanking);

            usernameTextView.setText(username);
            levelTextView.setText(level);
            scoreTextView.setText(score + "");


        }
    }
}
