package com.hypersphere.timelinetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    List<Integer> cards = new ArrayList<>();
    TimePointAdapter timeAdapter;
    private int itemWidth = 0;

    public void attachTimelineAdapter(TimePointAdapter adapter){
        timeAdapter = adapter;
        adapter.setMainAdapter(CardAdapter.this);
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        itemWidth = parent.getMeasuredWidth();

        return new CardHolder(itemView);
    }

    public int getContentWidth(){
        return itemWidth * getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        holder.setNumber(position);
    }

    public void addCard(){
        cards.add(cards.size() + 1);
        notifyItemInserted(cards.size() - 1);
        if(cards.size() < 4)
            timeAdapter.newItem(TimePointAdapter.TimePointState.STATE_COMPLETE);
        else
            timeAdapter.newItem(TimePointAdapter.TimePointState.STATE_FUTURE);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        TextView content;
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.card_text);
        }

        void setNumber(int n){
            content.setText("Card " + n);
        }
    }
}
