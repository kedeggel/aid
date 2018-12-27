package de.htwg.mobilecomputing.aid.Library;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecorationList extends RecyclerView.ItemDecoration {
    private final int itemOffset;

    public ItemOffsetDecorationList(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        if(position > 0) {
            outRect.top = itemOffset;
        }
    }
}
