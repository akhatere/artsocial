package com.example.paragon.socialapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeTabFragment extends Fragment {

    private final static String TAG = "HomeTabFragment";

    private static final String KEY_LISTITEM = "listItem";

    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_NAME = "name";

    private ListAdapter mListAdapter;
    private ArrayList<ListItem> mArtWorksListItem;

    private ProgressBar mProgressUpdate ;
    private ListView mListArtWorks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListArtWorks = (ListView) view.findViewById(R.id.list_artworks);
        mProgressUpdate = (ProgressBar) view.findViewById(R.id.progress_update);

        mListArtWorks.setOnItemClickListener(mListArtWorkOnclickListener);

        mProgressUpdate.setVisibility(View.VISIBLE);
        Network.getRandomPhoto(mGetRandomPhotosCallback);

    }

    private Network.GetRandomPhotoCallback mGetRandomPhotosCallback = new Network.GetRandomPhotoCallback() {
        @Override
        public void updatPhotos(Boolean isSuccessful,ArrayList<ListItem> artWorksList) {
            if(isSuccessful == true){

                mArtWorksListItem = artWorksList;
                mListAdapter = new ListAdapter(getContext(), mArtWorksListItem);
                mListArtWorks.setAdapter(mListAdapter);
                mProgressUpdate.setVisibility(View.INVISIBLE);

            } else {
                mProgressUpdate.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load photo!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private AdapterView.OnItemClickListener mListArtWorkOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ListItem artWorkItems = (ListItem) mListAdapter.getItem(position);
            String email = artWorkItems.getEmail();
            Log.d(TAG, "randemail :" + email);

            String name = artWorkItems.getName();
            String techniquGes = artWorkItems.getTechnique();

            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_NAME, name);

            startActivity(intent);
        }
    };


    public static HomeTabFragment newInstance(String ListItem) {
        HomeTabFragment homeFragment = new HomeTabFragment();

        Bundle args = new Bundle();
        args.putString(KEY_LISTITEM, ListItem);
        homeFragment.setArguments(args);

        return homeFragment;
    }
}