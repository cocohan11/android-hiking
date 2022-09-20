package com.example.iamhere.Interface;

import androidx.recyclerview.widget.RecyclerView;

import com.example.iamhere.CustomerAdapter;

public interface ItemTouchHelperListener {

    void setAdapter(CustomerAdapter adapter);

    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
    void onRightClick(int position, RecyclerView.ViewHolder viewHolder);

}
