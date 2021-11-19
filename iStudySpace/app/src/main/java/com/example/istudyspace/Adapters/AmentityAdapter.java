package com.example.istudyspace.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.istudyspace.R;

import java.util.List;

/**
 * This whole class is basically from android docs
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class AmentityAdapter extends RecyclerView.Adapter<AmentityAdapter.ViewHolder> {
    private List<Integer> resourceIds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.amentity_image);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public AmentityAdapter(List<Integer> resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.amentity_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getImageView().setImageResource(resourceIds.get(position));
    }

    @Override
    public int getItemCount() {
        return resourceIds.size();
    }
}