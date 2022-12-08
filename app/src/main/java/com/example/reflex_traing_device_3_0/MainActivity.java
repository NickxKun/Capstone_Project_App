package com.example.reflex_traing_device_3_0;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, contact, dob, gender, diagnosis;
    Button insert, update, delete, view;
    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Rymo Technologies");
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);
        diagnosis = findViewById(R.id.diagnosis);
        insert = findViewById(R.id.btnInsert);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);
        DB = new DatabaseHelper(this);

        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        view.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                return true;
            case R.id.menu_reflex:
                toReflexTrainingActivity();
                return true;
            case R.id.menu_strength:
                toStrengthTrainingActivity();
                return true;
            case R.id.bluetooth:
                toBleActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toReflexTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, ReflexTraining.class);
        startActivity(switchActivityIntent);
    }

    public void toStrengthTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, StrengthTraining.class);
        startActivity(switchActivityIntent);
    }

    public void toBleActivity() {
        Intent switchActivityIntent = new Intent(this, Bluetooth.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onClick(View view) {
        String nameTXT = name.getText().toString();
        String contactTXT = contact.getText().toString();
        String dobTXT = dob.getText().toString();
        String genderTXT = gender.getText().toString();
        String diagnosisTXT = diagnosis.getText().toString();
        switch (view.getId()) {
            case R.id.btnInsert:
                Boolean checkinsertdata = DB.insertuserdata(nameTXT, contactTXT, dobTXT, genderTXT, diagnosisTXT);
                if(checkinsertdata==true)
                    Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnUpdate:
                Boolean checkupdatedata = DB.updateuserdata(nameTXT, contactTXT, dobTXT, genderTXT, diagnosisTXT);
                if(checkupdatedata==true)
                    Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnDelete:
                Boolean checkudeletedata = DB.deletedata(nameTXT);
                if(checkudeletedata==true)
                    Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnView:
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Name :"+res.getString(0)+"\n");
                    buffer.append("Contact :"+res.getString(1)+"\n");
                    buffer.append("Date of Birth :"+res.getString(2)+"\n\n");
                    buffer.append("Gender :"+res.getString(3)+"\n\n");
                    buffer.append("Diagnosis :"+res.getString(4)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
                break;
            default:
                break;
        }
    }

}