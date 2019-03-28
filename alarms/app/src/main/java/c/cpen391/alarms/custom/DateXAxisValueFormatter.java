package c.cpen391.alarms.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class DateXAxisValueFormatter extends ValueFormatter {

    private String[] mValues;

    public DateXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }


}