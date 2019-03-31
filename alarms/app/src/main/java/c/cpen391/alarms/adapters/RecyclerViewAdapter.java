package c.cpen391.alarms.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import c.cpen391.alarms.R;
import c.cpen391.alarms.models.Alarm;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private List<Alarm> dataList;
    private Context context;
    private String[] cardUrls = {
            "http://www.4usky.com/data/out/61/164540853-minimal-wallpapers.jpg"
    };

    public RecyclerViewAdapter(Context context,List<Alarm> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private ImageView coverImage;
        TextView txtTitle;
        TextView date;
        TextView time;
        com.suke.widget.SwitchButton switchButton;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            date = mView.findViewById(R.id.date);
            time = mView.findViewById(R.id.time);
            coverImage = mView.findViewById(R.id.nextAlarmImage);
            switchButton = (com.suke.widget.SwitchButton) mView.findViewById(R.id.switch_button);
            switchButton.setChecked(true);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_card, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getAlarmDescription());
        holder.time.setText(dataList.get(position).getTime());
        holder.date.setText(dataList.get(position).getRelativeDay());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(R.drawable.sun)
                .placeholder((R.drawable.blue_plane))
                .error(R.drawable.bg_round_rect)
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}