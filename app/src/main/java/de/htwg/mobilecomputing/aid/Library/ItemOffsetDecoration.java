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
        //if(parent.getChildAdapterPosition(view) % 4 != 0)
        //    outRect.left = itemOffset;
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
    }
}