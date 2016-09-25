package com.example.paragon.socialapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter implements android.widget.ListAdapter {

    private static String TAG = "ListAdapter ";
    private Context mContext;



    private ArrayList<ListItem> mListArtWorksItem = new ArrayList<>();

    protected ArrayList<DataSetObserver> mObservers = new ArrayList<>(2);

    public ListAdapter(Context context, ArrayList<ListItem> listArtWorkItem) {
        mContext = context;
        mListArtWorksItem = listArtWorkItem;
    }

    // Updates grid data and refresh grid items.
    public void setArtWorkListItem(ArrayList<ListItem> artWorksList) {
        this.mListArtWorksItem = artWorksList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public int getCount() {
        return mListArtWorksItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListArtWorksItem.get(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ArtWorksDetailsListIem artWorkDetailItem;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(mContext);
            view = inflater.inflate(R.layout.list_item, null);
            artWorkDetailItem = new ArtWorksDetailsListIem(view);
            view.setTag(artWorkDetailItem);

        } else {
            view = convertView;
            artWorkDetailItem = (ArtWorksDetailsListIem) view.getTag();
        }

        ListItem artWorkItem = mListArtWorksItem.get(position);
        artWorkDetailItem.loadValue(artWorkItem);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1 ;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private class ArtWorksDetailsListIem{

        private TextView textNumberLike;
        private TextView textName;
        private TextView mTextEmail;
        private TextView mTextTechnique;
        private ImageView mImageRandomArtWork;

        public ArtWorksDetailsListIem(View root) {
            textName = (TextView) root.findViewById(R.id.item_name);
            mImageRandomArtWork= (ImageView) root.findViewById(R.id.item_randomaretworks);
            mTextTechnique= (TextView) root.findViewById(R.id.item_tecnigues);
            mImageRandomArtWork.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageRandomArtWork.setPadding(5, 5, 5, 5);
        }

        private void loadValue(ListItem artWorksList) {
//          textNumberLike.setText(String.valueOf(artWorksList.getLike()));
          mTextTechnique.setText(artWorksList.getTechnique());
            textName.setText(artWorksList.getName());
            mImageRandomArtWork.setImageBitmap(artWorksList.getPhoto());
        }
    }
}
