package com.example.android.codelabs.paging.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.codelabs.paging.R;

public class SeparatorViewHolder extends RecyclerView.ViewHolder {
    private final TextView description;

    public SeparatorViewHolder(@NonNull View view) {
        super(view);
        description = view.findViewById(R.id.separator_description);
    }

    public void bind(String separatorText) {
        description.setText(separatorText);
    }

    public static SeparatorViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.separator_view_item, parent, false);
        return new SeparatorViewHolder(view);
    }
}
