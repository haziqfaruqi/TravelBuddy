package com.example.travelbuddy;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Record> recordList;
    private RecordAdapter adapter;
    private SQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new SQLiteHelper(this);
        loadRecords();
    }

    private void loadRecords() {
        recordList = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Cursor cursor = databaseHelper.getRecordsByUser(currentUser.getUid());
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                    String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
                    String locationName = cursor.getString(cursor.getColumnIndexOrThrow("location_name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String photoLabel = cursor.getString(cursor.getColumnIndexOrThrow("photo_label"));

                    recordList.add(new Record(id, path, dateTime, locationName, description, photoLabel));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        adapter = new RecordAdapter(this, recordList, databaseHelper);
        recyclerView.setAdapter(adapter);
    }
}
