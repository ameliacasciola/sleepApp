package c.cpen391.alarms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import c.cpen391.alarms.R;
import c.cpen391.alarms.models.HighScore;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.ScoreViewHolder> {

    private List<HighScore> highScoreList;
    private Context context;

    private String[] avatarUrl = {
            "https://cdn3.iconfinder.com/data/icons/avatars-round-flat/33/woman1-512.png",
            "http://52.14.71.118/uploads/user.jpg",
            "https://cdn4.iconfinder.com/data/icons/people-of-business/512/People_Business_woman_jacket_name_tag-512.png",
            "https://cdn1.iconfinder.com/data/icons/education-1-15/151/26-512.png",
            "https://www.directlink.coop/img/icons/avatars/145841-avatar-set/png/girl-1.png",
            "http://www.pvhc.net/img240/uyttxprhsqycyximpzjb.png",
            "https://www.shareicon.net/data/2016/09/01/822739_user_512x512.png",
            "https://www.dimagriresipuo.it/images/avatar/man2.png",
            "http://profilepicturesdp.com/wp-content/uploads/2018/07/profile-pictures-512x512-7.png"
    };

    public LeaderboardRecyclerViewAdapter(Context context, List<HighScore> dataList){
        this.context = context;
        this.highScoreList = dataList;
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private ImageView userPic;
        TextView username;
        TextView number;
        TextView score;

        ScoreViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            username = mView.findViewById(R.id.username);
            number = mView.findViewById(R.id.number);
            score = mView.findViewById(R.id.score);
            userPic = mView.findViewById(R.id.profilepic);

        }
    }
    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_card, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderboardRecyclerViewAdapter.ScoreViewHolder holder, final int position) {
        holder.username.setText(highScoreList.get(position).getUsername());
        holder.number.setText(Integer.toString(position + 2));
        Integer score = highScoreList.get(position).getScore();
        if (score != null){
            holder.score.setText(Integer.toString(score));
        } else{
            holder.score.setText(Integer.toString(0));
        }

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(avatarUrl[position])
                .error(R.drawable.bg_round_rect)
                .into(holder.userPic);

    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }
}