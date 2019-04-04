package c.cpen391.alarms.custom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import c.cpen391.alarms.R;
import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.tabs.CreateAlarm;

public class AlarmDetailsSlidePageFragment extends Fragment {

    private Calendar dateCalendar;
    private SeekBar volumeBar;
    private Calendar timeCalendar;
    private int currentHour;
    private int currentMinute;
    private String amPm;
    private EditText descriptionEditText;
    private Alarm newAlarm;
    private EditText dateEditText;
    private int volume;

    private StringBuilder alarmTime;
    private EditText timeEditText;

    private DatePickerDialog.OnDateSetListener date;

    private TimePickerDialog time;

    private CardView nextBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        dateSelector(rootView);
        timeSelector(rootView);
        setVolumeSeekBar(rootView);
        setNextBtn(rootView);

        newAlarm = ((CreateAlarm)getActivity()).getAlarm();

        return rootView;
    }

    private void setNextBtn(View rootview){
        descriptionEditText = rootview.findViewById(R.id.description);
        nextBtn = rootview.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditText.getText().toString();
                alarmTime = new StringBuilder();
                alarmTime.append(String.valueOf(dateCalendar.get(Calendar.YEAR)) + '-' + String.valueOf(dateCalendar.get(Calendar.MONTH) + 1) + '-' +
                        String.valueOf(dateCalendar.get(Calendar.DAY_OF_MONTH)) + ' ' + String.valueOf(currentHour) + ':' +
                        String.valueOf(currentMinute));
                newAlarm.setTiffanyDate(String.valueOf(dateCalendar.get(Calendar.YEAR)) + '/' + '0' + String.valueOf(dateCalendar.get(Calendar.MONTH) + 1) + '/' + '0'+
                        String.valueOf(dateCalendar.get(Calendar.DAY_OF_MONTH)) + ' ' + String.valueOf(currentHour) + ':' +
                        String.valueOf(currentMinute));
                newAlarm.setAlarmDescription(description);
                newAlarm.setActive(true);
                newAlarm.setTime(alarmTime.toString());
                newAlarm.setVolume(volume);
                ((CreateAlarm) getActivity()).setTab(1);
            }
        });
    }

    private void setVolumeSeekBar(View rootview){
        volumeBar = rootview.findViewById(R.id.volume_bar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volume = progress;
            }
        });
    }
    private void dateSelector(View rootview){

        dateEditText= (EditText) rootview.findViewById(R.id.date);
        dateCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void timeSelector(View rootview){

        timeEditText= (EditText) rootview.findViewById(R.id.time);
        timeCalendar = Calendar.getInstance();

        timeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeCalendar = Calendar.getInstance();
                currentHour = timeCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = timeCalendar.get(Calendar.MINUTE);

                time = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        timeEditText.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        currentHour = hourOfDay;
                        currentMinute = minutes;
                    }
                }, currentHour, currentMinute, false);

                time.show();
            }

        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

        dateEditText.setText(sdf.format(dateCalendar.getTime()));
    }

}