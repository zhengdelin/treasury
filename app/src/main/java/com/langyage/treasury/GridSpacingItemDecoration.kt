package com.langyage.treasury

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount:Int, private val spacing:Int, private val includeEdge:Boolean = false)
    :RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //現在的位置
        val pos:Int = parent.getChildAdapterPosition(view)
        //在第幾列
        val column = pos % spanCount

        if(includeEdge){
            outRect.apply {
                left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (pos < spanCount) { // top edge
                    top = spacing;
                }
                bottom = spacing; // item bottom
            }
        }else{
            outRect.apply {
            left = column * spacing / spanCount
            right = spacing - (column + 1) * spacing / spanCount
            if(pos >= spanCount)
                top = spacing
        }
        }


    }
}