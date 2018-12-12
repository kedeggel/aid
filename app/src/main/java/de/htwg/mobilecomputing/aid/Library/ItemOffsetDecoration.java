package de.htwg.mobilecomputing.aid.Library;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private final int itemOffset;
    private final int spanCount;

    public ItemOffsetDecoration(int itemOffset, int spanCount) {
        this.itemOffset = itemOffset;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        //Horizontal space
        if (spanCount > 1) {
            int columnPosition = position % spanCount;
            int nudge = Math.round((float) itemOffset / spanCount);

            outRect.left = columnPosition * nudge;
            outRect.right = (spanCount - columnPosition - 1) * nudge;
        }

        //Vertical space
        if(position >= spanCount)
            outRect.top = itemOffset;
    }
}