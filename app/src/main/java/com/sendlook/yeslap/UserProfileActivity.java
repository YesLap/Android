package com.sendlook.yeslap;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Utils;
import com.sendlook.yeslap.model.gps;
import com.squareup.picasso.Picasso;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FloatingActionButton btnEditUserProfile;
    private ImageView btnGotoProfile, btnGoToSettings, btnFavorite;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private TextView tvUsername;
    private CircleImageView cvImageUser;
    private ProgressDialog dialog;

    private Location location;
    private LocationManager locationManager;
    private Address address;

    double latitude = 0.0;
    double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        try {
            getLocation();
        } catch (IOException e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
        }

        //Cast
        btnEditUserProfile = (FloatingActionButton) findViewById(R.id.btnEditUserProfile);
        btnGotoProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);
        btnFavorite = (ImageView) findViewById(R.id.btnFavorite);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToStart();
            }
        });

        //btnEditUserProfile Event Button
        btnEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                startActivity(intent);
            }
        });

        //btnGoToSetting Event Button
        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //btnCalendar Event Button
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //btnSearch Event Button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsernameAndImage();
            }
        });

        //btnChat Event Button
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        //btnFavorite Event Button
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showSpotlight() {
        SimpleTarget targetEditProfile = new SimpleTarget.Builder(this)
                .setPoint(440f, 343f)
                .setRadius(50f)
                .setTitle(getString(R.string.welcome_yeslap))
                .setDescription(getString(R.string.click_edit_profile))
                .build();

        SimpleTarget targetChatMessages = new SimpleTarget.Builder(this)
                .setPoint(160f, 600f)
                .setRadius(90f)
                .setTitle(getString(R.string.chat_messages))
                .setDescription(getString(R.string.chat_messages_mgs))
                .build();

        SimpleTarget targetCalendar = new SimpleTarget.Builder(this)
                .setPoint(360f, 600f)
                .setRadius(90f)
                .setTitle(getString(R.string.appointment_book))
                .setDescription(getString(R.string.appointment_book_msg))
                .build();

        SimpleTarget targetFind = new SimpleTarget.Builder(this)
                .setPoint(565f, 600f)
                .setRadius(90f)
                .setTitle(getString(R.string.find_users))
                .setDescription(getString(R.string.find_users_msg))
                .build();

        SimpleTarget targetFavorite = new SimpleTarget.Builder(this)
                .setPoint(360f, 820f)
                .setRadius(90f)
                .setTitle(getString(R.string.favorite_users))
                .setDescription(getString(R.string.favorite_users_msg))
                .build();

        SimpleTarget targetSettings = new SimpleTarget.Builder(this)
                .setPoint(618f, 120f)
                .setRadius(50f)
                .setTitle(getString(R.string.settings))
                .setDescription(getString(R.string.settings_msg))
                .build();

        SimpleTarget targetEnd = new SimpleTarget.Builder(this)
                .setPoint(300f, 300f)
                .setRadius(1f)
                .setTitle(getString(R.string.enjoy_yeslap))
                .setDescription(getString(R.string.enjoy_yeslap_msg))
                .build();

        Spotlight.with(this)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(targetEditProfile, targetChatMessages, targetCalendar, targetFind, targetFavorite, targetSettings, targetEnd)
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
                    @Override
                    public void onStarted() {

                    }
                })
                .setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
                    @Override
                    public void onEnded() {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                        HashMap<String, Object> spotlight = new HashMap<>();
                        spotlight.put("spotlight", "true");
                        mDatabase.updateChildren(spotlight);
                    }
                })
                .start();
    }

    //Get the user data from Firebase
    private void getUserData() {
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.loading));
            dialog.setMessage(getString(R.string.loading_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                    String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                    String spotlight = dataSnapshot.child(Utils.SPOTLIGHT).getValue(String.class);

                    if (!(username == null || Objects.equals(username, ""))) {
                        tvUsername.setText((username));
                    } else {
                        tvUsername.setText(getString(R.string.username));
                    }

                    if (image != null && !Objects.equals(image, "")) {
                        Picasso.with(UserProfileActivity.this).load((image)).placeholder(R.drawable.img_profile).into(cvImageUser);
                    }

                    dialog.dismiss();

                    if (!Objects.equals(spotlight, "true")) {
                        showSpotlight();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void checkConnection() {
        if (!haveNetworkConnection()) {
            // Display message in dialog box if you have not internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.no_internet_connection);
            alertDialogBuilder.setMessage(R.string.no_internet_connection_msg);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            //Get the user data
            getUserData();
            //Set status Online
            setStatusOnline();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If there is no one logged in to Firebase then it sends you to the SignIn screen.
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            sendToStart();
        } else {
            checkConnection();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUser = mAuth.getCurrentUser();
        //Chek if the user is logged
        if (mUser != null) {
            //Set status Offline
            setStatusOffline();
        }
    }

    private void sendToStart() {
        //Method that displaces the user and sends to the SignIn screen
        mAuth.signOut();
        Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Check if the username and image profile isn't null
    private void checkUsernameAndImage() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                if (Objects.equals(username, "Username") || Objects.equals(image, "")) {
                    dialog.dismiss();
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.please_change_image_username));
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(UserProfileActivity.this, FindUsersActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setStatusOnline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "online");
        mDatabase.updateChildren(status);
    }

    private void setStatusOffline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "offline");
        mDatabase.updateChildren(status);
    }

    public void getLocation() throws IOException {
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utils.toastyInfo(getApplicationContext(), "No Permissions to Access the Location!");
            } else {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    Utils.toastyInfo(getApplicationContext(), "Latitude: " + latitude + "\nLongitude: " + longitude);
                }

                address = getAddress(latitude, longitude);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                Map<String, Object> location = new HashMap<>();
                location.put("city", address.getLocality());
                location.put("state", address.getAdminArea());
                location.put("country", address.getCountryName());
                mDatabase.updateChildren(location);
            }

        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
        }

    }

    public Address getAddress(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        Address addres = null;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            addres = addresses.get(0);
        }

        return addres;
    }

}
