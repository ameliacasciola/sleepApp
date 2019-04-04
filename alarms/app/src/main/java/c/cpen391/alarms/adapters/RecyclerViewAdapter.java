package c.cpen391.alarms.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import java.util.List;

import c.cpen391.alarms.AlarmReceiver;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.home;
import c.cpen391.alarms.models.Alarm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private List<Alarm> dataList;
    private Context context;
    protected static CustomSharedPreference mPref;

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

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_card, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.txtTitle.setText(dataList.get(position).getAlarmDescription());
        holder.time.setText(dataList.get(position).getTime());
        holder.date.setText(dataList.get(position).getRelativeDay());

        final boolean tempBoo = dataList.get(position).getActive();
        holder.switchButton.setChecked(tempBoo);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(R.drawable.sun)
                .placeholder((R.drawable.blue_plane))
                .error(R.drawable.bg_round_rect)
                .into(holder.coverImage);


        // On Off button
        holder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
                Call<ResponseBody> call = service.updateOnOff(!tempBoo, dataList.get(position).getID());

                if (tempBoo){
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent((Activity) context, AlarmReceiver.class);
                    mPref = ((CustomApplication)context.getApplicationContext()).getShared();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), dataList.get(position).alarmDescription.hashCode() + mPref.getUserID(), intent, 0);

                    try {
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                    } catch (Exception e){
                    }
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            // refresh, jump
                            Intent refresh = new Intent(context, home.class);
                            context.startActivity(refresh);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Cannot Set On/Off Switch", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}