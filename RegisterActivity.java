package com.example.paragon.socialapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paragon.socialapp.Webservice.CheckNet;

public class RegisterActivity extends Activity {

    private static final String TAG = "registerActivity";

    public EditText mEditEmail;
    private EditText mEditName;
    private EditText mEditDescription;
    private EditText mEditInvitekey;
    private Button mButtonSignUp;
    private Button mButtonEdit;

    private Boolean mState;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditDescription = (EditText) findViewById(R.id.edit_description);
        mEditInvitekey = (EditText) findViewById(R.id.edit_invitekey);
        mButtonSignUp = (Button) findViewById(R.id.button_signup);
        mButtonEdit = (Button) findViewById(R.id.button_edit);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        mState = intent.getBooleanExtra("status", true);
        Log.d(TAG, "email" + mEmail);

        mEditEmail.setText(mEmail);

        mButtonSignUp.setOnClickListener(mButtonSignUpOnClickListener);
        mButtonEdit.setOnClickListener(mButtonEditOnClickListener);

        if (mState){
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private Network.CreateUserCallback mGetUserInfoCallback = new Network.CreateUserCallback() {

        @Override
        public void onUpdateUserInfoResult(Boolean isSuccessful, String email, String name, String description) {

            if(isSuccessful == true){

                Intent intent = new Intent();
                intent.putExtra("description", description);
                Log.d(TAG, "description" + description);

                intent.putExtra("name", name);
                Log.d(TAG, "name" + name);

                setResult(RESULT_OK, intent);
                finish();

            } else {
                finish();
                Toast.makeText(RegisterActivity.this, "کد وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
            }
        }

    };

    View.OnClickListener mButtonSignUpOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            signUpUser();
            Log.d(TAG, "signup");
        }

    };

    View.OnClickListener mButtonEditOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateInfo();
            Log.d(TAG, "edit");

        }

    };

    private void updateUI(boolean state) {
        if(state) {
            mButtonEdit.setVisibility(View.VISIBLE);
            mButtonSignUp.setVisibility(View.GONE);
            mEditEmail.setVisibility(View.VISIBLE);
            mEditName.setVisibility(View.VISIBLE);
            mEditDescription.setVisibility(View.VISIBLE);
            mEditInvitekey.setVisibility(View.GONE);
        } else {
            mButtonEdit.setVisibility(View.INVISIBLE);
            mButtonSignUp.setVisibility(View.VISIBLE);
            mEditEmail.setVisibility(View.VISIBLE);
            mEditName.setVisibility(View.VISIBLE);
            mEditDescription.setVisibility(View.VISIBLE);
            mEditInvitekey.setVisibility(View.VISIBLE);

        }
    }

    private Network.UpdateUserCallback mUpdateCallback = new Network.UpdateUserCallback() {

        @Override
        public void updateUser(Boolean isSuccessful, String description, String name) {
            Log.d(TAG, "mupdatecalback");

            if(isSuccessful == true ){

                Intent intent = new Intent();
                intent.putExtra("description", description);
                Log.d(TAG, "description" + description);

                intent.putExtra("name", name);
                Log.d(TAG, "name" + name);

                setResult(RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(RegisterActivity.this,R.string.errorconnectingtonetwork,Toast.LENGTH_LONG);
            }
        }

    };

    private void signUpUser(){
        Log.d(TAG, "rejister" );

        register();
    }

    private void register() {
        Log.d(TAG, "rejister" );

        Log.d(TAG, "email" + mEmail);

        String name = mEditName.getText().toString();
        String description = mEditDescription.toString();
        String invitekey = mEditInvitekey.toString();

        if ( name.isEmpty() || description.isEmpty() || invitekey.isEmpty()) {
            Toast.makeText(this, "Please Enter input", Toast.LENGTH_LONG).show();
        } else {
            if (CheckNet.isNetworkAvailable(this)) {
                Network.createUser(mEmail, name, description, invitekey, mGetUserInfoCallback);
            } else {
                Toast.makeText(this, R.string.errorconnectingtonetwork
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateInfo(){
        String name = mEditName.getText().toString();
        String description = mEditDescription.toString();

        if ( name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "فیلد هارا پر کنید", Toast.LENGTH_LONG).show();
        } else {
            if (CheckNet.isNetworkAvailable(this)) {
                Network.updateUser(mEmail, name, description, mUpdateCallback);
            } else {
                Toast.makeText(this, R.string.errorconnectingtonetwork, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
