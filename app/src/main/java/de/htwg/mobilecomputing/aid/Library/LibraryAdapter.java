package de.htwg.mobilecomputing.aid.Library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.htwg.mobilecomputing.aid.R;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private List<LibraryElement> elements;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    // todo group items by date: https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView image;
        public TextView label;
        private Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.context = context;
            image = itemView.findViewById(R.id.item_library_image);
            label = itemView.findViewById(R.id.item_library_label);
            itemView.setOnClickListener(this);
        }

        // Handles the row being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                LibraryElement element = elements.get(position);

                //todo: Open selected image
            }
        }
    }

    // Pass in the contact array into the constructor
    public LibraryAdapter(List<LibraryElement> elements) {
        this.elements = elements;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_library, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(LibraryAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        LibraryElement element = elements.get(position);

        // Set item views based on your views and data model
        TextView label = viewHolder.label;
        label.setText(element.getLabel());
        //todo Galleriebild setzen
        ImageView image = viewHolder.image;
        image.setImageResource(R.drawable.sample_image2);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return elements.size();
    }
}
