package com.example.paragon.socialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UserTabFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener ,
        GoogleApiClient.ConnectionCallbacks {

    private final static String TAG = "UserTabFragment";

    private static final String KEY_GRIDITEM = "gridItem";

    private static final int REQUEST_CODE_SGIN_IN= 0;
    private static final int REQUEST_RESOLVE = 1;
    private static final int REQUEST_CODE_LODEIMAGE = 2;
    private static final int REQUEST_CODE = 3;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 200;

    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_TECHNIQUE = "technique";
    private static final String EXTRA_PHOTO = "photo";
    private static final String EXTRA_LIKE = "like";
    private static final String EXTRA_ID= "id";

    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    //signUp views
    private TextView mButtonSignIn;
    private Button mButtonSignUp;
    private TextView mTextViewDescription;
    private ImageView mImageSeprator;
    private TextView mTextWelcome;

    //profile view
    private GridView mGridArtwork;
    private Button mButtonSelectImage;
    private TextView mTextName;
    private ImageView mSeprator;
    private TextView mTextDescription;
    private ImageView mImageUser;
    private RelativeLayout mHolderProfile;
    private TextView mButtonSignOut;
    private TextView mTextInviteKey;

    private ProgressDialog mProgress ;
    private InputStream is = null;

    private ArrayList<GridItems> mArtWorksList;
    private Uri mGaleryImageUri;
    private GridAdapter mGridAdapter;

    private String mGoogleUserName;
    private String mUserName;
    private String mEmail;
    private String mTechniquGes;
    private String mImageFilePtah;
    private String filePath;
    private String mInviteKey;
    private String mDescription;

    private Boolean mState;
    private Boolean mChangeStat;
    private Boolean mIsFirstTime;
    String mName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //signUp view
        mButtonSignIn = (TextView) view.findViewById(R.id.button_signin);
        mButtonSignUp = (Button) view.findViewById(R.id.button_signup);
        mImageSeprator = (ImageView) view.findViewById(R.id.image_seprator);
        mTextViewDescription = (TextView) view.findViewById(R.id.description);
        mTextWelcome = (TextView) view.findViewById(R.id.text_welcome);

        //profile view
        mGridArtwork = (GridView) view.findViewById(R.id.grid_artworks);
        mButtonSelectImage = (Button) view.findViewById(R.id.button_selectImage);
        mTextName = (TextView) view.findViewById(R.id.text_name);
        mTextDescription = (TextView) view.findViewById(R.id.text_description);
        mSeprator = (ImageView) view.findViewById(R.id.seprator);
        mImageUser = (ImageView) view.findViewById(R.id.userphoto);
        mHolderProfile = (RelativeLayout) view.findViewById(R.id.holder_profile);
        mTextInviteKey = (TextView) view.findViewById(R.id.text_invitekey);
        mButtonSignOut = (TextView) view.findViewById(R.id.text_logout);

        //listeners
        mGridArtwork.setOnItemClickListener(mGridArtWorkOnclickListener);
        mButtonSelectImage.setOnClickListener(mButtonSelectImageOnClickListener);
        mButtonSignIn.setOnClickListener(mButtonSignInOnClicklistner);
        mButtonSignUp.setOnClickListener(mButtonSignUpOnClickListener);
        mTextInviteKey.setOnClickListener(mTextInviteKeyOnClickListener);
        mButtonSignOut.setOnClickListener(mTextLogOutOnClickListener);

        mIsFirstTime = false;
        updateUI(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    View.OnClickListener mButtonSignInOnClicklistner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mState = true;
            signInWithGplus();
        }

    };

    View.OnClickListener mButtonSignUpOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mState = false;
            signInWithGplus();
        }

    };

    View.OnClickListener mButtonSelectImageOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            loadImagefromGallery();
        }

    };

    AdapterView.OnItemClickListener mGridArtWorkOnclickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            GridItems artWorkItem = (GridItems) mGridAdapter.getItem(position);
            Bitmap photo = artWorkItem.getPhoto();
            String name = artWorkItem.getName();
            int like = artWorkItem.getLike();
            long photoId= artWorkItem.getID();
            mTechniquGes = artWorkItem.getTechniques();

            Intent intent = new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra(EXTRA_NAME,name);
            intent.putExtra(EXTRA_TECHNIQUE, mTechniquGes);
            intent.putExtra(EXTRA_PHOTO, photo);
            intent.putExtra(EXTRA_LIKE, like);
            intent.putExtra(EXTRA_ID, photoId );
            startActivity(intent);
        }

    };

    View.OnClickListener mTextInviteKeyOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Network.getGenerateInviteKey(mGenerateInviteKeyCallback);
        }

    };

    View.OnClickListener mTextLogOutOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            signOutFromGplus();
        }

    };

    private Network.GenerateInviteKeyCallback mGenerateInviteKeyCallback = new Network.GenerateInviteKeyCallback() {

        @Override
        public void InviteKey(Boolean isSuccessful, String inviteKey) {

            if(isSuccessful == true){
                mInviteKey = inviteKey;

                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(getContext());

                // Setting Dialog Title
                mAlertDialog
                        .setCancelable(false)
                        .setPositiveButton("شبکه های اجتماعی", mAlertDialogListener)
                        .setNegativeButton("پیامک", mAlertDialogListener)
                        .setIcon(R.drawable.socialnetwork)
//                        .setNegativeButton("خروج", mAlertDialogListener)
                        .setMessage("کد دعوت ورود به برنامه :" + mInviteKey)

                        .show();// show it
            } else {
                Toast.makeText(getContext(), "خطادر اتصال به شبکه", Toast.LENGTH_SHORT).show();
            }

        }

    };

    private DialogInterface.OnClickListener mAlertDialogListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mInviteKey);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareassage_dialog)));
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }

    };

    private Network.GetEmailCallback mGetEmailCallback = new Network.GetEmailCallback() {

        @Override
        public void CheckedEmailResult(Boolean isSuccessful, String email, String name) {
//            mTextName.setText(name);

            if(isSuccessful == true ){

                if(mChangeStat){
//                    updateUI(true);
                    Network.getUser(mEmail, mGetPhotosCallback);

                } else {
                    Toast.makeText(getContext(),"شما ثبلا ثبت نام نکرده اید",Toast.LENGTH_SHORT);
//                    Log.d(TAG, "status" + mState);
//                    Intent intent = new Intent(getContext(), RegisterActivity.class);
//                    intent.putExtra(EXTRA_EMAIL, mEmail);
//                    intent.putExtra("status", true);
//                    startActivityForResult(intent, REQUEST_CODE);
                }

            } else {

                Intent  intent = new Intent(getContext(), RegisterActivity.class);
                intent.putExtra(EXTRA_EMAIL, mEmail);
                intent.putExtra("status", false);
                startActivityForResult(intent, REQUEST_CODE);

            }

        }

    };


    private Network.newPhotoCallback mSendImageToServerCallback = new Network.newPhotoCallback() {
        @Override
        public void sendImageResult(Boolean isSuccessful, File imageurl) {

            if(isSuccessful == true){
//                mProgress = new ProgressDialog(getContext());
//                mProgress.setMessage(getResources().getString(R.string.PROGRESS_DIALOG_MASSAGE));
//                mProgress.setCancelable(false);
//                mProgress.show();

                Network.getUser(mEmail, mGetPhotosCallback);
            } else{
                Log.d(TAG, "registeractivity");
            }

        }
    };

    private Network.GetUserCallback mGetPhotosCallback = new Network.GetUserCallback() {

        @Override
        public void updateArtWorks(Boolean isSuccessful, ArrayList<GridItems> artWorksList, String email, String name, String mDescription) {
            if(isSuccessful == true){
                mArtWorksList = artWorksList;
                mGridAdapter = new GridAdapter(getContext(), mArtWorksList);

                mGridArtwork.setAdapter(mGridAdapter);
                mGridAdapter.setArtWorkList(mArtWorksList);

                String description = mDescription;
                Log.d(TAG, "description" + description);

                mUserName = name;

                mTextName.setText(mUserName);
                mTextDescription.setText(description);

            } else {
                Toast.makeText(getContext(),R.string.errorconnectingtonetwork, Toast.LENGTH_SHORT).show();
            }

        }

    };

    private void signInWithGplus() {
        mGoogleApiClient.connect();
    }

    public void onConnected(Bundle arg0) {
        getProfileInformation();

        if(mState){
            checkEmailRegistered();
            mChangeStat = true;

        } else {
            mChangeStat = false;
            checkEmailRegistered();

        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed");

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult((Activity) getContext(), REQUEST_RESOLVE);
                return;
            } catch (IntentSender.SendIntentException e) {
                Log.w(TAG, "خطا در اتصال ", e);

                resolveSignInError();

            }
        }

        // We are unable to resolve the problem.
        GooglePlayServicesUtil.getErrorDialog(
                result.getErrorCode(),
                (Activity) getContext(),
                0
        ).show();

    }

    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SGIN_IN:
                if (responseCode != Activity.RESULT_OK) {
                    mSignInClicked = false;

                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    intent.putExtra(EXTRA_EMAIL, mEmail);
                    startActivity(intent);
                }
                mIntentInProgress = false;

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;

            case REQUEST_RESOLVE:
                if (responseCode == Activity.RESULT_OK) {
                    if (!mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                }
                break;

            case REQUEST_CODE:
                if(responseCode == Activity.RESULT_OK){
                    mIsFirstTime = true;
                    Log.d(TAG,"mIsfirsttime" + mIsFirstTime);

                    updateUI(true);

                    mDescription = data.getStringExtra("description");
                    Log.d(TAG, "description" + mDescription);

                    mName = data.getStringExtra("name");
                    //Log.d(TAG, "name" + name);

                    // mTextDescription.setText(description);
                    mTextName.setText(mName);
                    Network.getUser(mEmail, mGetPhotosCallback);

                }
                break;

            case REQUEST_CODE_LODEIMAGE:

                if (data != null && responseCode == Activity.RESULT_OK) {

                    // Get the url from data
                    mGaleryImageUri = data.getData();
                    Log.d(TAG, " mGaleryImageUri :" + mGaleryImageUri);

                    if (mGaleryImageUri != null) {
                        try {
                            InputStream inputstream = getContext().getContentResolver()
                                    .openInputStream(mGaleryImageUri);
                            Log.d(TAG, "imagedecodableString: +" + is);

                            mImageFilePtah = convertToBitmap(inputstream);
                            Network.newPhoto(mEmail, mImageFilePtah, mSendImageToServerCallback);

                        } catch (FileNotFoundException e) {
                            Log.d(TAG, "exception: +" + e);

                        } catch (IOException e) {
                        }

                    } else {
//                        Toast.makeText(getContext(), "You haven't picked an image!",
//                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    // send data to network class
    protected void checkEmailRegistered() {
        Log.d(TAG, "mEmail" + mEmail);
        Network.checkEmailRegistered(mEmail, mGetEmailCallback);
    }

    //Sign-out from google
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            updateUI(false);
        }
    }

    //Choose an image from Gallery
    void loadImagefromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        super.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LODEIMAGE);
    }

    // Method to resolve any signin errors
    private void resolveSignInError() {
        Log.d(TAG, "resolveSignError");
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult((Activity) getContext(), REQUEST_CODE_SGIN_IN);

            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }

    private String convertToBitmap(InputStream path) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(path);
        //original measurements
        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();
        Bitmap bitmapScle = null;
        final int destWidth = 108;//or the width you need

        if (origWidth > destWidth) {
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight / (origWidth / destWidth);

            // we create an scaled bitmap so it reduces the image, not just trim it
            bitmapScle = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
            Log.d(TAG, "bitmapscle :" + bitmapScle);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            // compress to the format you want, JPEG, PNG...
            bitmapScle.compress(Bitmap.CompressFormat.JPEG, 70, outStream);

            // we save the file, at least until we have made use of it
            File file = new File(Environment.getExternalStorageDirectory()+ File.separator + "artgalery.jpg");
            //write the bytes in file
            file.createNewFile();
            Log.d(TAG, "file" + file);

            //write the bytes in file
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outStream.toByteArray());

            outputStream.close();

            filePath = Environment.getExternalStorageDirectory() + File.separator + "artgalery.jpg";
            Log.d(TAG, "filepath" + file);

        }
        return filePath;
    }

    // Fetching user's information name, mEmail, profile pic
    private void getProfileInformation() {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        if (currentPerson != null) {
            mGoogleUserName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();
            mEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

            Log.e(TAG, "Name: " + mGoogleUserName + ", mEmail: " + mEmail + ", Image: " + personPhotoUrl);

            // by default the profile url gives 50x50 px image only
            // we can replace the value with whatever dimension we want by
            // replacing sz=X
//            personPhotoUrl = personPhotoUrl.substring(0,
//                    personPhotoUrl.length() - 2)
//                    + PROFILE_PIC_SIZE;
//
//            new LoadProfileImage(mImageUser).execute(personPhotoUrl);

        } else {
            mGoogleApiClient.disconnect();
            Toast.makeText(getContext(),
                    "Person information is null", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(boolean isSignIn) {
        if(isSignIn) {
            Log.d(TAG, "updateui");
            mTextWelcome.setVisibility(View.GONE);
            mButtonSignIn.setVisibility(View.GONE);
            mTextViewDescription.setVisibility(View.GONE);
            mImageSeprator.setVisibility(View.GONE);
            mButtonSignUp.setVisibility(View.GONE);
            mSeprator.setVisibility(View.VISIBLE);
            mHolderProfile.setVisibility(View.VISIBLE);
            mButtonSignOut.setVisibility(View.VISIBLE);
            mTextInviteKey.setVisibility(View.VISIBLE);
            mImageUser.setVisibility(View.VISIBLE);
            mTextName.setVisibility(View.VISIBLE);
            mTextDescription.setVisibility(View.VISIBLE);
            mTextName.setVisibility(View.VISIBLE);
            mGridArtwork.setVisibility(View.VISIBLE);
            mButtonSelectImage.setVisibility(View.VISIBLE);
        } else {

            mTextWelcome.setVisibility(View.VISIBLE);
            mButtonSignIn.setVisibility(View.VISIBLE);
            mButtonSignUp.setVisibility(View.VISIBLE);
            mTextViewDescription.setVisibility(View.VISIBLE);
            mHolderProfile.setVisibility(View.GONE);
            mTextName.setVisibility(View.GONE);
            mSeprator.setVisibility(View.GONE);
            mImageUser.setVisibility(View.GONE);
            mTextDescription.setVisibility(View.GONE);
            mTextInviteKey.setVisibility(View.GONE);
            mTextName.setVisibility(View.GONE);
            mGridArtwork.setVisibility(View.GONE);
            mHolderProfile.setVisibility(View.GONE);
            mButtonSignOut.setVisibility(View.GONE);
            mButtonSelectImage.setVisibility(View.GONE);
        }

    }

    //Background Async task to load user profile picture from url
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static UserTabFragment newInstance(String gridItem) {
        UserTabFragment userTabFragment = new UserTabFragment();

        Bundle args = new Bundle();
        args.putString(KEY_GRIDITEM, gridItem);
        userTabFragment.setArguments(args);

        return userTabFragment;
    }

}
