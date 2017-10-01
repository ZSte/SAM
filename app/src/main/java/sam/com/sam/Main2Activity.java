package sam.com.sam;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private TextView locationTv;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    final int RC_SIGN_IN = 1;

    private TextView textViewName;
    private TextView textViewEMail;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = "ANONYMOUS";

        //locationTv = (TextView) findViewById(R.id.location_tv);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users"/*"messages"*/);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        textViewName = (TextView) headerView.findViewById(R.id.textView_name);
        textViewEMail = (TextView) headerView.findViewById(R.id.textView_eMail);

        /*if the App is restarted but a user is still logged in his name will be reset in the
        navigation drawer*/
        if (mFirebaseAuth.getCurrentUser() != null) {
            textViewName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
            textViewEMail.setText(mFirebaseAuth.getCurrentUser().getEmail());
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize references to views
        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //mMessageListView = (ListView) findViewById(R.id.messageListView);
        //mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        //mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        //mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        //List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        List<User> list = new ArrayList<>();
        //mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        //mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        //mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        //mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
        //    }
        //});

        //FloatingActionButton f = (FloatingActionButton) findViewById(R.id.fab);
        //f.setOnClickListener(this);

        // Enable Send button when there's text to send
        //mMessageEditText.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //    }

        //    @Override
        //    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //        if (charSequence.toString().trim().length() > 0) {
        //            mSendButton.setEnabled(true);
        //        } else {
        //            mSendButton.setEnabled(false);
        //        }
        //    }

        //    @Override
        //    public void afterTextChanged(Editable editable) {
        //    }
        //});
        //mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        //mSendButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
                // TODO: Send messages on click
        //        FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
        //        mMessagesDatabaseReference.push().setValue(friendlyMessage);

        //        // Clear input box
        //        mMessageEditText.setText("");
        //    }
        //});
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName());
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
                } else {
                    onSignedOutCleanup();
                    List<AuthUI.IdpConfig> providers = new ArrayList<>(); //= Arrays.asList(
                    //        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    //        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())/*providers*/)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == 0) {
                Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng location = place.getLatLng();
                Log.e("LAT", location.latitude + "");
                User u = new User(mFirebaseAuth.getCurrentUser().getDisplayName(), -1, mFirebaseAuth.getCurrentUser().getEmail(),
                        null, null, location.latitude, location.longitude);
                //databaseReference.child(firebaseAuth.getCurrentUser().getUid() + "/location/latitude").setValue(location.latitude);
                //databaseReference.child(firebaseAuth.getCurrentUser().getUid() + "/location/longitude").setValue(location.longitude);
                //mMessagesDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid() + "/location").setValue(location);
                mMessagesDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).setValue(u);
                //firebaseAddChildEventListener();
                //MapManager.addUserMarker(googleMap, new User(firebaseAuth.getCurrentUser().getDisplayName(), -1, firebaseAuth.getCurrentUser().getEmail(), null, null, /*firebaseAuth.getCurrentUser().getUid(),*/ location.longitude, location.latitude));
            }
        }
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this);
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
            final List<User> list = new ArrayList<>();

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    /*FriendlyMessage*/User friendlyMessage = dataSnapshot.getValue(/*FriendlyMessage*/User.class);
                    //list.add(dataSnapshot.child(/*messages*//*"name"*/).getValue(String.class));
                    //Log.e("LIST", list.toString());
                    //mMessageAdapter.add(friendlyMessage);
                    //locationTv.setText(locationTv.getText() + "\n" + friendlyMessage.getLocation());
                    //MapManager.addUserMarker(googleMap, friendlyMessage);
                    //User a = new User("a", -1, "aaaa", null, null, 0.0, 0.0);
                    //User a1 = new User("a!", -1, "aaa", null, null, 10.0, 10.0);

                    //List<User> list = new ArrayList<>();
                    //list.add(a);
                    //list.add(a1);
                    list.add(friendlyMessage);
                    MapManager.addUserMarker(googleMap, list);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void onSignedOutCleanup() {
        //mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if(mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //case R.id.sign_out_menu:
            //    AuthUI.getInstance().signOut(this);
            //    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        //mMessageAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.log_out) {
            signOut();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            sendEMail();
        } else if (id == R.id.item_lang_spoken) {
            Intent i = new Intent(this, SetSpokenActivity.class/*STest.classSetLanguagesActivity.class*/);
            startActivity(i);
        } else if (id == R.id.item_lang_learn) {
            Intent i = new Intent(this, SetLearnActivity.class);
            startActivity(i);
        } else if (id == R.id.place_pick) {
            chooseLocation();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/
        //firebaseAddChildEventListener();
        attachDatabaseReadListener();

        googleMap.setOnInfoWindowClickListener(this);
    }

    /*@Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;*/

        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/
        //firebaseAddChildEventListener();
        /*attachDatabaseReadListener();

        googleMap.setOnInfoWindowClickListener(this);
    }*/

    private void sendEMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    private void sendEMailTo(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    public void chooseLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), 2);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        /*Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();*/

        Log.e("do", "it");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact");
        builder.setPositiveButton("Send Mail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendEMailTo(marker.getSnippet());
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {

    }

    //@Override
    //public void onClick(View view) {
    //    if(view.getId() == R.id.fab) {
    //        int PLACE_PICKER_REQUEST = 1;
    //        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    //        try {
    //            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    //        }
    //        catch(Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}
}
