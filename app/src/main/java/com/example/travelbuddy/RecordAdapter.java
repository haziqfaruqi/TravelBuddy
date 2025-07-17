package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private ArrayList<Record> recordList;
    private SQLiteHelper databaseHelper;

    public RecordAdapter(Context context, ArrayList<Record> recordList, SQLiteHelper databaseHelper) {
        this.context = context;
        this.recordList = recordList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        Record record = recordList.get(position);

        holder.photoLabel.setText(record.getPhotoLabel());
        holder.dateTime.setText(record.getDateTime());
        holder.location.setText(record.getLocationName());
        holder.description.setText(record.getDescription());

        // Load image using Glide - adjust path accordingly
        Glide.with(context)
                .load(record.getImagePath())
                .placeholder(android.R.color.darker_gray)
                .into(holder.imageView);

        // Delete button
        holder.deleteBtn.setOnClickListener(v -> {
            // Delete from database
            boolean deleted = databaseHelper.deleteRecord(record.getId());
            if (deleted) {
                recordList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, recordList.size());
                Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Share button
        holder.shareBtn.setOnClickListener(v -> {
            shareImage(record);
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    private void shareImage(Record record) {
        String imageUriString = record.getImagePath();
        Uri imageUri = Uri.parse(imageUriString);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        String description = record.getDescription();
        if (description != null && !description.isEmpty()) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, description);
            shareIntent.setType("*/*");  // for image + text
        }

        // No explicit package, let system show chooser
        context.startActivity(Intent.createChooser(shareIntent, "Share image via"));
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView photoLabel, dateTime, location, description;
        Button deleteBtn, shareBtn;

        public RecordViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImage);
            photoLabel = itemView.findViewById(R.id.itemPhotoLabel);
            dateTime = itemView.findViewById(R.id.itemDateTime);
            location = itemView.findViewById(R.id.itemLocation);
            description = itemView.findViewById(R.id.itemDescription);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
        }
    }
}
