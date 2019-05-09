package com.example.surelywang.together;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.surelywang.plantbus.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {

    // Establish view
    View v;
    Context context;
    Fragment fragment;
    private FragmentManager fragmentManager;


    // For BLE
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bleScanner;
    private BluetoothGatt bleGatt;
    private static final int REQUEST_ENABLE_BT = 1;
    //private static final UUID UUID_Service = UUID.fromString("19fc95c0-c111–11e3–9904–0002a5d5c51b");
    private static final UUID UUID_Service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_characteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    // 0x2A37

    private static UUID sampleChar;


    // Value trackers
    TextView btn0, btn1, btn2, btn3, btn4, btn5;
    ImageView track0, track1, track2, track3, track4, track5, track6, track7, track8, track9, track10;


    // Counter trackers - updated to corresponding value textview
    int buttonCount0 = 0;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_home_fragment, container, false);
        context = container.getContext();

        // Initialize textview trackers for buttons
        btn0 = (TextView) v.findViewById(R.id.btnTracker0);
        track1 = (ImageView) v.findViewById(R.id.track1);
        track2 = (ImageView) v.findViewById(R.id.track2);
        track3 = (ImageView) v.findViewById(R.id.track3);
        track4 = (ImageView) v.findViewById(R.id.track4);
        track5 = (ImageView) v.findViewById(R.id.track5);
        track6 = (ImageView) v.findViewById(R.id.track6);
        track7 = (ImageView) v.findViewById(R.id.track7);
        track8 = (ImageView) v.findViewById(R.id.track8);
        track9 = (ImageView) v.findViewById(R.id.track9);
        track10 = (ImageView) v.findViewById(R.id.track10);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

        // BLE Initialization
        boolean initializeSuccess = initialize();
        if (initializeSuccess) {
            startScan();
        }

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        String TAG = "BLE Testing";
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
            Log.e(TAG, "BluetoothManager is initialized.");
        }

        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        Log.e(TAG, "BluetoothAdapter is initialized.");

        return true;
    }


    // Scan for nearby BLE device with our service + characteristic UUID
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScan() {
        Log.e("startScan", "Got to start of scan call.");
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled())
        {Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            bleScanner = bluetoothAdapter.getBluetoothLeScanner();
            if (bleScanner != null) {
                Log.e("startScan", "Got to end of scan call - about to call scanCallback");
                final ScanFilter scanFilter = new ScanFilter.Builder().build();
                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
                bleScanner.startScan(Collections.singletonList(scanFilter), settings, scanCallback);
            }
        }
    }

    // Scan callback: checks if feather is found - if so, connect to it!
    private ScanCallback scanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.e("scanCallback", "Inside onScanResult");
            Log.e("scanCallback", "Device found: " + result.getDevice().getName());
            if("Conner's Feather".equals(result.getDevice().getName())) {
                Toast.makeText(context, "Connected to Together",
                        Toast.LENGTH_SHORT).show();
                if(bleScanner != null) {
                    bleScanner.stopScan(scanCallback);
                }
                bleGatt = result.getDevice().connectGatt(context, false, bleGattCallback);
            } super.onScanResult(callbackType, result);
            // onScanResult: checks if device is found - if so, save device properties inside of bleGatt
            // and then connect to the device on bleGattCallback
        }
    };


    // BLE Gatt Callback
    private BluetoothGattCallback bleGattCallback = new BluetoothGattCallback()
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        // Called when connection is established to remote device
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int
                newState) {
            gatt.discoverServices();    // Start discovery service
            super.onConnectionStateChange(gatt, status, newState);
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
            BluetoothGattService service = gatt.getService(UUID_Service);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = null;
                List<BluetoothGattCharacteristic> chars = service.getCharacteristics();
                for (int i = 0 ; i < chars.size(); i++) {
                    characteristic = chars.get(i);
                    Log.d("onServicesDiscovered: ", "CHAR i FOUND: " + characteristic.getUuid().toString());
                }
                if (characteristic != null) {
                    Log.d("onServicesDiscovered", "Characteristic is not null");
                    Log.d("onServicesDiscovered", "Characteristic UUID: " + characteristic.getUuid().toString());
                    gatt.setCharacteristicNotification(characteristic, true);

                    //UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
//                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(characteristic.getUuid());
//                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                            UUID.fromString("1ED9E2C0-266F-11E6-850B-0002A5D5C51B"));
//                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                    gatt.writeDescriptor(descriptor);

                    if (gatt.readCharacteristic(characteristic) == false) {
                        Log.d("onServicesDiscovered", "readCharacteristic FAILED");
                    }
                } else {
                    Log.d("onServicesDiscovered", "Characteristic is null");
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        // Called to read values from characteristic that's discovered in service
        public void onCharacteristicRead(BluetoothGatt gatt, final
        BluetoothGattCharacteristic characteristic, int status) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String value = characteristic.getStringValue(1);
                    Log.d("onCharacteristicRead", "Got here, value read is: " + value);
                    Log.d("onCharacteristicRead", "Characteristic is read - about to set button value.");
                    btn0.setText(value);    // set button's value

                    int readVal = Integer.parseInt(value);

                    switch (readVal) {
                        case 1:
                            track1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            track2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            track3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            track4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            track5.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            track6.setVisibility(View.VISIBLE);
                            break;
                        case 7:
                            track7.setVisibility(View.VISIBLE);
                            break;
                        case 8:
                            track8.setVisibility(View.VISIBLE);
                            break;
                        case 9:
                            track9.setVisibility(View.VISIBLE);
                            break;
                        case 10:
                            track10.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }

                }
            });

            BluetoothGattService service = gatt.getService(UUID_Service);
            //readNextCharacteristic(gatt, characteristic);
            super.onCharacteristicRead(gatt, characteristic, status);

            Log.d("onCharacteristicRead", "Disconnecting from GATT");
            onServicesDiscovered(gatt, status);
        }


    };



}




// Excess Code

//        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//        @Override
//        // Called when services are discovered
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            BluetoothGattService service =
//                    gatt.getService(UUID_Service);
//
//            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//            for (int i = 0 ; i < characteristics.size(); i++) {
//                Log.e("onServicesDiscovered", "Characteristic UUID: " + characteristics.get(i).getUuid().toString());
//            }
//
//            Log.d("onServicesDiscovered", "Got here - about to read characteristic");
//
//            // Characteristic of button 0
//            BluetoothGattCharacteristic char0 = service.getCharacteristic(UUID_characteristic);
//
//            if (char0 != null) {
//                gatt.setCharacteristicNotification(char0, true);
//                Log.d("onServicesDiscovered", "Got here - about to read characteristic");
//            }
//
////            Log.d("onServicesDiscovered", "STRING VALUE: " + char0.getStringValue(1));
////            if (char0.getValue() == null) {
////                Log.d("onServicesDiscovered", "ERROR: Characteristic value is NULL.");
////            }
//
//            gatt.readCharacteristic(char0);
//            super.onServicesDiscovered(gatt, status);
//
//        }
