package com.example.ameli.module2tutorials;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ListpastmealActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, DetailsDialog.DetailsDialogListener{
    ListView listView;
    private String FileName = "restaurantDetails1.txt";
    private boolean[] entered = new boolean[10];

    private String when_data;
    private String where_data;
    private String howmuch_data;
    private String tip_data;
    private String numberpeople_data;
    private String rating;

    String[] fruitNames = {"Meal 1","Meal 2","Meal 3","Meal 4","Meal 5","Meal 6","Meal 7","Meal 8","Meal 9","Meal 10"};
    int[] fruitImages = {R.drawable.apple,R.drawable.oranges,R.drawable.kiwi,R.drawable.watermelon,R.drawable.banana
    ,R.drawable.chocolate,R.drawable.cookie,R.drawable.hotpot,R.drawable.konjac,R.drawable.noodles};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpastmeal);

        listView = findViewById(R.id.listview);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!entered[i] ){
                    entered[i] = true;
                    openDialog();
                } else {
                    openDetailsDialog();
                    fromFile();
                }
            }
        });
    }

    private class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return fruitImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view  , ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            //getting view in row_data
            TextView name = view1.findViewById(R.id.fruits);
            ImageView image = view1.findViewById(R.id.images);

            name.setText(fruitNames[i]);
            image.setImageResource(fruitImages[i]);
            return view1;

        }
    }

    private void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"example dialog");
    }

    private void openDetailsDialog(){
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.show(getSupportFragmentManager(),"details dialog");
    }

    @Override
    public void toFile(String when_data, String where_data, String howmuch_data, String tip_data,
                       String numberpeople_data, String rating) {
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        try {
            fos = openFileOutput ( FileName, Context.MODE_PRIVATE  );
            dos = new DataOutputStream(fos);

            Toast.makeText(this, "Details saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error File: ", Toast.LENGTH_LONG).show();
            return;
        } catch (IOException e) {
            Toast.makeText(this, "Error File woot: ", Toast.LENGTH_LONG).show();
        }

        try
        {
            String newLine = System.getProperty("line.separator");

            dos.writeBytes(when_data);
            dos.writeBytes(newLine);
            dos.writeBytes(where_data);
            dos.writeBytes(newLine);
            dos.writeBytes(howmuch_data);
            dos.writeBytes(newLine);
            dos.writeBytes(tip_data);
            dos.writeBytes(newLine);
            dos.writeBytes(numberpeople_data);
            dos.writeBytes(newLine);
            dos.writeBytes(rating);
            dos.writeBytes(newLine);

        } catch (IOException e) {
            Toast.makeText(this, "Error Writing File…", Toast.LENGTH_LONG).show();
            return;
        }
// close the file
        try {
            fos.close();
        } catch (IOException ex) {
            Toast.makeText(this, "Error Closing File…", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void fromFile(){
        FileInputStream fis;
        InputStreamReader inputStreamReader;
        DataInputStream dis;
        BufferedReader bufferedReader;
        StringBuffer stringBuffer;

        // open the file for reading
        try {
            fis = openFileInput (FileName);
            inputStreamReader = new InputStreamReader(fis);
            bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();

            dis = new DataInputStream (fis);

        } catch (FileNotFoundException e)
        {
            Toast.makeText(this, "Error File: ", Toast.LENGTH_LONG).show();
            return ;
        }

        try
        {
            String lines;

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                when_data = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                where_data = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                howmuch_data = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                tip_data = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                numberpeople_data = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

            if((lines = bufferedReader.readLine())!= null){
                stringBuffer.append(lines);
                rating = stringBuffer.toString();
                stringBuffer.setLength(0);
            }

        } catch (IOException e) {
            Toast.makeText(this, "Error Reading File…", Toast.LENGTH_LONG).show();
            return;
        }

// close the file
        try {
            fis.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error Closing File…", Toast.LENGTH_LONG).show();
            return;
        }


    }

    public String getRating_data(){
        return this.rating;
    }

    public String getHowmuch_data() {
        return this.howmuch_data;
    }

    public String getNumberpeople_data() {
        return this.numberpeople_data;
    }

    public String getTip_data() {
        return this.tip_data;
    }

    public String getWhen_data() {
        return this.when_data;
    }

    public String getWhere_data() {
        return where_data;
    }
}