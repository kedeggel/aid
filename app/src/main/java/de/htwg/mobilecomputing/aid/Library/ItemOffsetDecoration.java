package de.htwg.mobilecomputing.aid.Library;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;
    private int spanCount;

    public ItemOffsetDecoration(int itemOffset, int spanCount) {
        this.itemOffset = itemOffset;
        this.spanCount = spanCount;
    }

    /*public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }*/

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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