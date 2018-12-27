package de.htwg.mobilecomputing.aid.Library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwg.mobilecomputing.aid.R;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private ArrayList<LibraryElement> elements;
    private final LibraryItemClickListener libraryItemClickListener;

    // todo group items by date: https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout layout;
        final ImageView image;
        final TextView line1;
        final TextView line2;
        final TextView line3;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_library);
            image = itemView.findViewById(R.id.item_library_image);
            line1 = itemView.findViewById(R.id.item_library_line1);
            line2 = itemView.findViewById(R.id.item_library_line2);
            line3 = itemView.findViewById(R.id.item_library_line3);
        }
    }

    public LibraryAdapter(ArrayList<LibraryElement> elements, LibraryItemClickListener libraryItemClickListener) {
        this.elements = elements;
        this.libraryItemClickListener = libraryItemClickListener;
    }

    @NonNull
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_library_list, parent, false);

        // Return a new holder instance
        return new ViewHolder(context, contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull final LibraryAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final LibraryElement element = elements.get(position);

        // Set item views based on your views and data model
        ImageView image = viewHolder.image;
        image.setImageBitmap(element.getImage());

        TextView line1 = viewHolder.line1;
        line1.setText(element.getFormattedTimestamp());

        TextView line2 = viewHolder.line2;
        line2.setText(element.getSensor());

        TextView line3 = viewHolder.line3;
        line3.setText(element.getLocation());

        // Set transition name same as the Image name
        ViewCompat.setTransitionName(viewHolder.image, element.getId());

        viewHolder.layout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryItemClickListener.onLibraryItemClickListener(viewHolder.getAdapterPosition(), element, viewHolder.image);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return elements.size();
    }
}
