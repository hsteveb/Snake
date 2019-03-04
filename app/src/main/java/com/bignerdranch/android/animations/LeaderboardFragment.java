package com.bignerdranch.android.animations;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.android.animations.Model.Score;

import java.util.List;

public class LeaderboardFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private DummyScores mDummyScores;
    private TabLayout mTabLayout;
    private ImageButton mReturnImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDummyScores = new DummyScores();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.leaderboard_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.leaderboard_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(mDummyScores.getScores()));

        mTabLayout = v.findViewById(R.id.leaderboard_fragment_tablayout);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.darkRed2), getResources().getColor(R.color.darkRed2));
        mTabLayout.setClipToOutline(true);
        //mRecyclerView.invalidate();

        mReturnImageButton = v.findViewById(R.id.leaderboard_fragment_backbutton);
        mReturnImageButton.setOnClickListener(this);
        return v;
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
    {
        private List<Score> mDummyScores;
        public RecyclerViewAdapter(List<Score> dummyScores)
        {
            mDummyScores = dummyScores;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_view, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.BindHolder(mDummyScores.get(position), position + 1);
        }

        @Override
        public int getItemCount() {
            return mDummyScores.size();
        }


    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView mPosition, mName, mTime;
        public RecyclerViewHolder(View view)
        {
            super(view);
            mPosition = view.findViewById(R.id.leaderboard_view_position);
            mName = view.findViewById(R.id.leaderboard_view_alias);
            mTime = view.findViewById(R.id.leaderboard_view_time);
        }

        public void BindHolder(Score score, int position)
        {
            mPosition.setText(String.format("%02d", position));
            mName.setText(score.getName());
            mTime.setText(score.getScore());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leaderboard_fragment_backbutton:
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }
}
