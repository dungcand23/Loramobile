package com.example.myapplication;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    View khuvuc1, khuvuc2;
    Button buttonon,buttonoff ;
    TimePickerDialog timePickerDialog;
    private  TextView lightset1, lightset2,hum, hum2, hum3, temp,temp2,temp3,timeHour,timeMinute,timeHour1,timeMinute1;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    Switch switch1, switch2 , switch4 ;
    ImageView light1,light2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightset1 = findViewById(R.id.textView2);
        lightset2 = findViewById(R.id.textView3);

        light1 = findViewById(R.id.light);
        light2 = findViewById(R.id.light2);

        timeHour = findViewById(R.id.timeHour);
        timeMinute = findViewById(R.id.timeMinute);
        timeHour1 = findViewById(R.id.timeHour1);
        timeMinute1 = findViewById(R.id.timeMinute1);
        buttonon = findViewById(R.id.buttonon);
        buttonoff = findViewById(R.id.buttonoff);
        khuvuc1 = findViewById(R.id.khuvuc1);
        khuvuc2 = findViewById(R.id.khuvuc2);
        switch1  =findViewById(R.id.switch1);
        switch2 =findViewById(R.id.switch2);

        switch4 =findViewById(R.id.switch4);
        temp = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        temp3 = findViewById(R.id.temp3);
        hum = findViewById(R.id.hum1);
        hum2 = findViewById(R.id.hum2);
        hum3 = findViewById(R.id.hum3);

        readDatabase();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mylight = database.getReference("ESP32/light");

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    light1.setImageResource(R.drawable.idea);
                    mylight.setValue("On");
                } else {
                    light1.setImageResource(R.drawable.idea1);
                    mylight.setValue("Off");
                }
            }
        });

// Lấy giá trị từ Firebase
        mylight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value.equals("On")) {
                    switch1.setChecked(true);
                    light1.setImageResource(R.drawable.idea);
                } else {
                    switch1.setChecked(false);
                    light1.setImageResource(R.drawable.idea1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRef3 = database.getReference("ESP32/gio mo");
        DatabaseReference myRef4 = database.getReference("ESP32/gio tat");
        DatabaseReference myRef5 = database.getReference("ESP32/phut mo");
        DatabaseReference myRef6 = database.getReference("ESP32/phut tat");
        DatabaseReference myRef7 = database.getReference("ESP32/kiem tra");
        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    myRef3.setValue(timeHour.getText().toString().trim());
                    myRef4.setValue(timeHour1.getText().toString().trim());
                    myRef5.setValue(timeMinute.getText().toString().trim());
                    myRef6.setValue(timeMinute1.getText().toString().trim());
                    myRef7.setValue("1");

                }else{


                    myRef3.setValue(timeHour.getText().toString().trim());
                    myRef4.setValue(timeHour1.getText().toString().trim());
                    myRef5.setValue(timeMinute.getText().toString().trim());
                    myRef6.setValue(timeMinute1.getText().toString().trim());
                    myRef7.setValue("2");
                }
            }
        });
        // Lắng nghe giờ mở
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gioMo = snapshot.getValue(String.class);
                timeHour.setText(gioMo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

// Lắng nghe giờ tắt
        myRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gioTat = snapshot.getValue(String.class);
                timeHour1.setText(gioTat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

// Lắng nghe phút mở
        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phutMo = snapshot.getValue(String.class);
                timeMinute.setText(phutMo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

// Lắng nghe phút tắt
        myRef6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phutTat = snapshot.getValue(String.class);
                timeMinute1.setText(phutTat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        

        buttonon.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(MainActivity.this, (timePicker, hourOfDay, minutes) -> {
                timeHour.setText(String.format("%02d", hourOfDay));
                timeMinute.setText(String.format("%02d", minutes));
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        });
        buttonoff.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(MainActivity.this, (timePicker, hourOfDay, minutes) -> {
                timeHour1.setText(String.format("%02d", hourOfDay));
                timeMinute1.setText(String.format("%02d", minutes));
            }, currentHour, currentMinute, false);
            timePickerDialog.show();
        });

    }




    public void readDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("ESP32/temp");
        DatabaseReference myref1 = database.getReference("ESP32/hum");
        DatabaseReference myref2 = database.getReference("ESP32/light");

        myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String dulieu = Snapshot.getValue(String.class);
                lightset1.setText(dulieu);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = Snapshot.getValue(Long.class);
                temp.setText(String.valueOf(value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long value = Snapshot.getValue(Long.class);
                hum.setText(String.valueOf(value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG,  "Failed to read value.", error.toException());
            }
        });
    }
}