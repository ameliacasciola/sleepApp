package c.cpen391.alarms.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import c.cpen391.alarms.R;
import c.cpen391.alarms.models.Alarm;

public class SwipeRecyclerViewAdapter  extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {

    private Context mContext;
    private List<Alarm> alarmList;

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
        viewHolder.viewIcon.setImageResource(R.drawable.ic_outline_cancel_24px);
        viewHolder.viewIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);
        viewHolder.deleteIcon.setImageResource(R.drawable.ic_outline_cancel_24px);
        viewHolder.deleteIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(new OkHttp3Downloader(mContext));
        builder.build().load(R.drawable.sun)
                .placeholder((R.drawable.blue_plane))
                .error(R.drawable.bg_round_rect)
                .into(viewHolder.coverImage);

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

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/

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

                Toast.makeText(view.getContext(), "Clicked on View " + viewHolder.time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.alarmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.alarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            switchButton.setChecked(true);

            coverImage = itemView.findViewById(R.id.nextAlarmImage);

        }
    }
}