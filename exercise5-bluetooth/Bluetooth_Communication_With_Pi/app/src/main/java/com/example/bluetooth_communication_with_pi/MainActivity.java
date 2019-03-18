package com.example.bluetooth_communication_with_pi;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    Button buttonOn, buttonOff, buttonScan, buttonSendData;
    BluetoothAdapter myBluetoothAdapter;
    ListView scanListView;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    private ArrayList < BluetoothDevice >  Discovereddevices = new ArrayList < BluetoothDevice > ( ) ;

    private BluetoothSocket mmSocket = null;
    private static InputStream mmInStream = null;
    private static OutputStream mmOutStream = null;
    boolean Connected = false;

    Intent BTEnable_Intent;
    int requestCodeForEnable;

    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOn = (Button) findViewById(R.id.enable);
        buttonOff = (Button) findViewById(R.id.disable);
        buttonScan = (Button) findViewById(R.id.scan);
        buttonSendData = (Button) findViewById(R.id.senddata);

        status = (TextView) findViewById(R.id.connectionstatus);
        status.setText("Disconnected");

        scanListView = (ListView) findViewById(R.id.listview);


        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // check to see if your android device even has a bluetooth device !!!!,
        if(myBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "No Bluetooth", Toast.LENGTH_LONG).show();
            finish(); // if no bluetooth device on this tablet don’t go any further.
            return;
        }

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isDiscovering())
                {
                    myBluetoothAdapter.cancelDiscovery();

                    myBluetoothAdapter.startDiscovery();

                    IntentFilter filterFound = new IntentFilter (BluetoothDevice.ACTION_FOUND);
                    registerReceiver (myReceiver, filterFound);
                } else
                {
                    myBluetoothAdapter.startDiscovery();

                    IntentFilter filterFound = new IntentFilter (BluetoothDevice.ACTION_FOUND);
                    registerReceiver (myReceiver, filterFound);
                }
            }
        });


        BTEnable_Intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
        scanListView.setAdapter(arrayAdapter);

        // Choose one to connect
        scanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = "Discovered Device: " + stringArrayList.get(position);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                if(Connected == true)
                {
                    closeConnection();
                    status.setText("Disconnected");
                }

                // Connection
                CreateSerialBluetoothDeviceSocket(Discovereddevices.get(position)) ;
                ConnectToSerialBlueToothDevice();

                // Update view list
                arrayAdapter.notifyDataSetChanged();
            }
        });

        bluetoothONMethod();
        bluetoothOFFMethod();
        senddataMethod();
    }

    public void WriteToBTDevice (String message) {
        String s = new String("\r\n") ;

        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        try {
            mmOutStream.write(msgBuffer) ;
            mmOutStream.write(newline) ;
        } catch (IOException e) { }
    }


    // do this when user clicks send data
    private void senddataMethod() {
        buttonSendData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(Connected == true) {
                    WriteToBTDevice("Data Sent to Pi for Testing");
                }
            }
        });
    }

    private void ConnectToSerialBlueToothDevice() {
        // Cancel discovery because it will slow down the connection
        myBluetoothAdapter.cancelDiscovery();
        try {
            // Attempt connection to the device through the socket.
            mmSocket.connect();
            Toast.makeText(getApplicationContext(), "Connection Made", Toast.LENGTH_LONG).show();
            status.setText("Connected");
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                status.setText("Disconnected");
                return;
            }
        }

        //create the input/output stream and record fact we have made a connection
        GetInputOutputStreamsForSocket();
        Connected = true;
        status.setText("Connected");
    }

    private void GetInputOutputStreamsForSocket() {
        try {
            mmInStream= mmSocket.getInputStream();
            mmOutStream= mmSocket.getOutputStream();
        } catch (IOException e) { }
    }

    private void closeConnection() {
        try{
            mmInStream.close();
            mmInStream= null;
        } catch (IOException e) {}
        try{
            mmOutStream.close();
            mmOutStream= null;
        } catch (IOException e) {}
        try{
            mmSocket.close();
            mmSocket= null;
        } catch (IOException e) {}
        Connected= false ;
        status.setText("Disconnected");
    }

    private void CreateSerialBluetoothDeviceSocket(BluetoothDevice bluetoothDevice) {
        mmSocket = null;

        // universal UUID for a serial profile RFCOMM blue tooth device
        // this is just one of those “things” that you have to do and just works
        UUID MY_UUID = UUID.fromString ("00001101-0000-1000-8000-00805F9B34FB");

        // Get a Bluetooth Socket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            mmSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Socket Creation Failed", Toast.LENGTH_LONG).show();
        }
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action));
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Add the name and address to the custom array adapter to show in a ListView
                String theDevice = new String( device.getName() + "\nMAC Address = " + device.getAddress());
                Toast.makeText(context, "Scanning", Toast.LENGTH_SHORT).show(); // create popup for device

                Discovereddevices.add(device);
                stringArrayList.add(theDevice);

                // notify array adaptor that the contents of String Array have changed
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);

        super.onDestroy();
    }

    private void bluetoothOFFMethod() {
        buttonOff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled())
                {
                    myBluetoothAdapter.disable();

                    stringArrayList.clear();

                    // notify array adaptor that the contents of String Array have changed
                    arrayAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    // Purpose: Show system's status
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == requestCodeForEnable)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bluetoothONMethod() {
        buttonOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter == null)
                {
                    Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_LONG).show();
                } else
                {
                    if(!myBluetoothAdapter.isEnabled())
                    {
                        // make our device's bluetooth visible to other devices
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
                        startActivity(intent);

                        // turning on our bluetooth
                        startActivityForResult(BTEnable_Intent,requestCodeForEnable);
                    }
                }
            }
        });
    }
}
