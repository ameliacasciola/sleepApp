package c.cpen391.alarms.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
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
import c.cpen391.alarms.tabs.CreateAlarm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeRecyclerViewAdapter  extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {

    private Context mContext;
    private List<Alarm> alarmList;
    protected static CustomSharedPreference mPref;

    public SwipeRecyclerViewAdapter(Context context, List<Alarm> alarms) {
        this.mContext = context;
        this.alarmList = alarms;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Alarm item = alarmList.get(position);

        viewHolder.time.setText(item.getTime());
        viewHolder.date.setText(item.getRelativeDay());
        viewHolder.title.setText(item.getAlarmDescription());

        viewHolder.editIcon.setImageResource(R.drawable.ic_outline_edit_24px);
        viewHolder.editIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.SRC_ATOP);
        viewHolder.viewIcon.setImageResource(R.drawable.ic_outline_pageview_24px);
        viewHolder.viewIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);
        viewHolder.deleteIcon.setImageResource(R.drawable.ic_outline_cancel_24px);
        viewHolder.deleteIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);

        viewHolder.timeUntil.setText(alarmList.get(position).getTimeUtil());

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(new OkHttp3Downloader(mContext));
        builder.build().load(R.drawable.sun)
                .placeholder((R.drawable.blue_plane))
                .error(R.drawable.bg_round_rect)
                .into(viewHolder.coverImage);

        final boolean tempBoo = alarmList.get(position).getActive();
        viewHolder.switchButton.setChecked(tempBoo);

        // On Off button
        viewHolder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
                Call<ResponseBody> call = service.updateOnOff(!tempBoo, alarmList.get(position).getID());

                if (tempBoo == true){
                    // delete alarm from alarm manager
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent((Activity) mContext, AlarmReceiver.class);
                    mPref = ((CustomApplication)mContext.getApplicationContext()).getShared();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), alarmList.get(position).alarmDescription.hashCode() + mPref.getUserID(), intent, 0);

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
                            Intent refresh = new Intent(mContext, home.class);
                            mContext.startActivity(refresh);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "Cannot Set On/Off Switch", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //Swipe action
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //Bottomview is showing
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " onClick : " + item.getAlarmDescription() + " \n" + item.getVolume(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.timeUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPref = ((CustomApplication)mContext.getApplicationContext()).getShared();
                mPref.setAlarmFlag(1);
                mPref.setAlarmID(alarmList.get(position).getID());

                Intent intent = new Intent(mContext, CreateAlarm.class);
                mContext.startActivity(intent);
            }
        });

        viewHolder.alarmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPref = ((CustomApplication)mContext.getApplicationContext()).getShared();
                mPref.setAlarmFlag(1);
                mPref.setAlarmID(alarmList.get(position).getID());

                Intent intent = new Intent(mContext, CreateAlarm.class);
                mContext.startActivity(intent);
            }
        });


        viewHolder.alarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete alarm from alarm manager
                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent((Activity) mContext, AlarmReceiver.class);
                mPref = ((CustomApplication)mContext.getApplicationContext()).getShared();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), alarmList.get(position).alarmDescription.hashCode() + mPref.getUserID(), intent, 0);

                try {
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                } catch (Exception e){
                }
                //delete alarm from database
                mPref = ((CustomApplication)mContext.getApplicationContext()).getShared();
                SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
                final String temp = Integer.toString(alarmList.get(position).getID());
                Call<ResponseBody> call = service.deleteAlarm(alarmList.get(position).getID());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(!response.isSuccessful()) {
                            Toast.makeText(mContext, "Delete Alarm No Response", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Delete Alarm Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "Delete Alarm Failure", Toast.LENGTH_SHORT).show();
                    }
                });

                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                alarmList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, alarmList.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView date;
        TextView time;
        TextView title;
        RelativeLayout alarmDelete;
        RelativeLayout alarmEdit;
        RelativeLayout alarmView;
        TextView timeUntil;

        ImageView editIcon;
        ImageView viewIcon;
        ImageView deleteIcon;
        com.suke.widget.SwitchButton switchButton;


        ImageView coverImage;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);

            alarmDelete = (RelativeLayout) itemView.findViewById(R.id.alarmDelete);
            alarmEdit = (RelativeLayout) itemView.findViewById(R.id.alarmEdit);
            alarmView = (RelativeLayout) itemView.findViewById(R.id.alarmView);
            timeUntil = (TextView) itemView.findViewById(R.id.timeUntil);

            editIcon = (ImageView) itemView.findViewById(R.id.edit_icon);
            viewIcon = (ImageView) itemView.findViewById(R.id.view_icon);
            deleteIcon = (ImageView) itemView.findViewById(R.id.delete_icon);

            switchButton = (com.suke.widget.SwitchButton) itemView.findViewById(R.id.switch_button);

            coverImage = itemView.findViewById(R.id.nextAlarmImage);

        }
    }
}