package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private ArrayList<Record> recordList;
    private SQLiteHelper databaseHelper;

    // Constructor
    public RecordAdapter(Context context, ArrayList<Record> recordList, SQLiteHelper dbHelper) {
        this.context = context;
        this.recordList = recordList;
        this.databaseHelper = dbHelper;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position);

        // Set text to TextViews
        holder.itemPhotoLabel.setText(record.getPhotoLabel());
        holder.itemDateTime.setText(record.getDateTime());
        holder.itemLocation.setText(record.getLocationName());
        holder.itemDescription.setText(record.getDescription());

        // Load image from URI to ImageView using Glide (more reliable)
        Glide.with(context)
                .load(Uri.parse(record.getImagePath()))
                .into(holder.itemImage);

        // Delete button action
        holder.deleteBtn.setOnClickListener(v -> {
            databaseHelper.deleteRecord(record.getId());
            recordList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recordList.size());
            Toast.makeText(context, "Record deleted.", Toast.LENGTH_SHORT).show();
        });

        // Image click action — open fullscreen image
        holder.itemImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullscreenImageActivity.class);
            intent.putExtra("imageUri", record.getImagePath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    // ViewHolder inner class
    static class RecordViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemPhotoLabel, itemDateTime, itemLocation, itemDescription;
        Button deleteBtn;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemPhotoLabel = itemView.findViewById(R.id.itemPhotoLabel);
            itemDateTime = itemView.findViewById(R.id.itemDateTime);
            itemLocation = itemView.findViewById(R.id.itemLocation);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
