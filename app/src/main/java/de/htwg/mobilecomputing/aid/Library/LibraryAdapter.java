package de.htwg.mobilecomputing.aid.Library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwg.mobilecomputing.aid.R;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private ArrayList<LibraryElement> elements;
    private final LibraryItemClickListener libraryItemClickListener;

    // todo group items by date: https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        //final TextView label;
        private final Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            image = itemView.findViewById(R.id.item_library_image);
            //label = itemView.findViewById(R.id.item_library_label);
        }
    }

    // Pass in the contact array into the constructor
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
        View contactView = inflater.inflate(R.layout.item_library, parent, false);

        // Return a new holder instance
        return new ViewHolder(context, contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull final LibraryAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final LibraryElement element = elements.get(position);

        // Set item views based on your views and data model
        //TextView label = viewHolder.label;
        //label.setText(element.getLabel());
        ImageView image = viewHolder.image;
        image.setImageResource(R.drawable.sample_image2); //todo: Get image

        // Set transition name same as the Image name
        ViewCompat.setTransitionName(viewHolder.image, element.getLabel());

        viewHolder.image.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryItemClickListener.onLibrarytemClickListener(viewHolder.getAdapterPosition(), element, viewHolder.image);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return elements.size();
    }
}
