package com.example.paragon.socialapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private static String TAG = "ArtWorkGridview";
    private ImageView mImageArtWork;
    private Context mContext;

    private ArrayList<GridItems> mArtWorksList = new ArrayList<GridItems>();

    public GridAdapter(Context context, ArrayList<GridItems> artWorkList) {
        mContext = context;
        mArtWorksList = artWorkList;
    }

    // Updates grid data and refresh grid items.
    public void setArtWorkList(ArrayList<GridItems> artWorksList) {
        this.mArtWorksList = artWorksList;
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    //returns the number of images---
    @Override
    public int getCount() {
        return mArtWorksList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArtWorksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //returns an ImageView view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ArtWorksDetailItem artWorkDetailItem;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(mContext);
            view = inflater.inflate(R.layout.grid_item, null);
            artWorkDetailItem = new ArtWorksDetailItem(view);
            view.setTag(artWorkDetailItem);

        } else {
            view = convertView;
            artWorkDetailItem = (ArtWorksDetailItem) view.getTag();
        }

        GridItems artWorkItem = mArtWorksList.get(position);
        artWorkDetailItem.loadValue(artWorkItem);
        return view;
    }

    private class ArtWorksDetailItem {

        private TextView mTextNumberLike;
        private TextView mTextName;
        private TextView mTextEmail;
        private TextView mTextTechniquGes;
        private ImageView mImageArtWork;
        private ImageView mHolderLike;

        boolean isPhoto =true;

        private ArtWorksDetailItem(View root) {

            mImageArtWork = (ImageView) root.findViewById(R.id.item_image);
//            mImageHeart = (ImageView) root.findViewById(R.id.image_heart);
            mImageArtWork.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
        }

        private void loadValue(GridItems artworks) {
//          mTextTitle.setText(artworks.getName());
//            mTextNumberLike.setText(String.valueOf(artworks.getLike()));
            mImageArtWork.setImageBitmap(artworks.getPhoto());
        }
    }
}
