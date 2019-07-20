package com.example.sqlite;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    Button btn, deleteBtn;
    EditText name, surname, marks;
    TextView showData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        name = findViewById(R.id.et_name);
        surname = findViewById(R.id.et_surname);
        marks = findViewById(R.id.et_marks);
        btn = findViewById(R.id.btn);
        showData = findViewById(R.id.tv_showdata);
        deleteBtn = findViewById(R.id.delete_btn);
        addData();
        getData();
        deleteData();
    }

    public void addData() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean found = true;
                Cursor res = myDb.getAllData();

                while (res.moveToNext()) {
                    if (res.getString(1).contains(name.getText().toString())) {
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }

                if (found == false) {
                    boolean isInserted = myDb.insertData(name.getText().toString(),
                            surname.getText().toString(),
                            marks.getText().toString());
                    if (isInserted == true) {
                        Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                    }
                } else if (found == true) {
                    boolean isUpdated = myDb.updateData(name.getText().toString(),
                            surname.getText().toString(),
                            marks.getText().toString());
                    if (isUpdated == true) {
                        Toast.makeText(MainActivity.this, name.getText().toString() + " updated", Toast.LENGTH_SHORT).show();
                    }
                }

                getData();
            }
        });
    }

    public void getData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() < 1) {
            Toast.makeText(MainActivity.this, "Nothing found", Toast.LENGTH_SHORT).show();
            showData.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :" + res.getString(0) + "\n");
            buffer.append("Name :" + res.getString(1) + "\n");
            buffer.append("Surname :" + res.getString(2) + "\n");
            buffer.append("Marks :" + res.getString(3) + "\n\n");
        }

        if (buffer != null) {
            showData.setText(buffer.toString());
        }
    }

    public void deleteData() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isDeleted = myDb.deleteData(name.getText().toString());
                if (isDeleted == 1) {
                    Toast.makeText(MainActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Data not deleted", Toast.LENGTH_SHORT).show();
                }
                getData();
            }
        });
    }
}
