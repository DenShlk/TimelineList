package com.hypersphere.timelinetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.chauthai.overscroll.DecelerateSmoothScroller;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    int timelineRecyclerScroll = 0;
    int contentRecyclerScroll = 0;
    RecyclerView lastDraggedRecycler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recycler = findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
        final CardAdapter adapter = new CardAdapter();
        recycler.setAdapter(adapter);
        final PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler);

        final RecyclerView timelineView = findViewById(R.id.timeline_recycler);
        timelineView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
        final TimePointAdapter timelineAdapter = new TimePointAdapter();
        adapter.attachTimelineAdapter(timelineAdapter);
        timelineView.setAdapter(timelineAdapter);
        LinearSnapHelper centerHelper = new LinearSnapHelper();
        centerHelper.attachToRecyclerView(timelineView);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    lastDraggedRecycler = recyclerView;
                   // timelineView.smoothScrollBy(timelineView.getWidth() / 5 * direction, 0, new DecelerateInterpolator());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                contentRecyclerScroll+=dx;
                if(lastDraggedRecycler == recyclerView) {
                    double k = 1.0 * (timelineAdapter.getContentWidth() - timelineView.getWidth()) / (adapter.getContentWidth() - recyclerView.getWidth());
                    timelineView.scrollBy((int) (contentRecyclerScroll * k - timelineRecyclerScroll), 0);
                }
            }
        });

        timelineView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("scrollstate", String.valueOf(newState));
                lastDraggedRecycler = recyclerView;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                timelineRecyclerScroll+=dx;
                timelineAdapter.centerMoved(timelineRecyclerScroll);

                if(lastDraggedRecycler == recyclerView) {
                    double k = 1.0 * (timelineAdapter.getContentWidth() - timelineView.getWidth()) / (adapter.getContentWidth() - recyclerView.getWidth());
                    recycler.scrollBy((int) (timelineRecyclerScroll / k - contentRecyclerScroll), 0);
                }


            }
        });

        for (int i = 0; i < 10; i++) {
            adapter.addCard();
        }
    }
}