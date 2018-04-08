package com.bendezu.yandexphotos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bendezu.yandexphotos.dummy.DummyContent.DummyItem;

import java.util.List;


public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    private final List<DummyItem> mValues;
    private OnImageClickListener mClickHandler;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);
    }

    public ImageRecyclerViewAdapter(List<DummyItem> items, OnImageClickListener clickHandler) {
        mValues = items;
        mClickHandler = clickHandler;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        SquareImageView mImage;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ImageViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            mImage = view.findViewById(R.id.item_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int idFromView = Integer.valueOf(mIdView.getText().toString());
            mClickHandler.onImageSelected(idFromView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
